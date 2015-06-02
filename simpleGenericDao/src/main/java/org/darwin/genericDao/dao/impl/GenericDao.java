/**
 * org.darwin.genericDao.dao.GenericDao.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:20:41
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.GenericDaoUtils;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.dao.BaseDao;
import org.darwin.genericDao.mapper.AnnotationConfigKeeper;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;
import org.darwin.genericDao.mapper.EntityMapper;
import org.darwin.genericDao.mapper.QueryHandler;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.query.QueryDelete;
import org.darwin.genericDao.query.QuerySelect;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * created by Tianxin on 2015年5月26日 下午9:20:41
 */
public class GenericDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> implements BaseDao<KEY, ENTITY> {

	/**
	 * 构造函数，在这里对最主要的几个属性进行初始化
	 */
	public GenericDao() {
		@SuppressWarnings("rawtypes")
		Class<ENTITY> entityClass = GenericDaoUtils.getEntityClass((Class) (this.getClass()));

		Table table = GenericDaoUtils.getTable(entityClass);
		Sequence sequence = GenericDaoUtils.getSequence(entityClass);

		this.entityClass = entityClass;
		this.keyClass = GenericDaoUtils.getKeyClass(entityClass);
		this.configKeeper = new AnnotationConfigKeeper(table, sequence);
		this.columnMappers = GenericDaoUtils.generateColumnMappers(entityClass, table.columnStyle());
		this.queryHandler = new QueryHandler<KEY, ENTITY>(columnMappers, configKeeper);
	}

	private Class<KEY> keyClass;
	private Class<ENTITY> entityClass;
	private JdbcTemplate jdbcTemplate;
	private AnnotationConfigKeeper configKeeper;
	private QueryHandler<KEY, ENTITY> queryHandler;
	private Map<String, ColumnMapper> columnMappers;

	@SuppressWarnings("unchecked")
	public boolean create(ENTITY entity) {
		if (entity == null) {
			return false;
		}

		// 获取sql以及参数
		List<ENTITY> entities = toEntities(entity);
		final String sql = queryHandler.generateInsertSQL(entities);
		final Object[] params = queryHandler.generateInsertParams(entities);
		
		//如果id不为null，则直接插入即可
		if(entity.getId() != null){
			return jdbcTemplate.update(sql, params) >= 1;
		}

		// ID为null时，执行插入操作同时要获取插入的key
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rowCount = this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				for (Object param : params) {
					ps.setObject(index++, param);
				}
				return ps;
			}
		}, keyHolder);

		// 如果插入成功则获取keyHolder中的key
		if (rowCount != 0 && keyHolder.getKey() != null) {
			KEY key = null;
			if (keyClass.equals(Integer.class)) {
				key = (KEY) Integer.valueOf(keyHolder.getKey().intValue());
			} else if (keyClass.equals(Long.class)) {
				key = (KEY) Long.valueOf(keyHolder.getKey().longValue());
			} else if (keyClass.equals(Short.class)) {
				key = (KEY) Short.valueOf(keyHolder.getKey().shortValue());
			} else if (keyClass.equals(String.class)) {
				key = (KEY) keyHolder.getKey().toString();
			} else {
				throw new RuntimeException(entityClass.getName() + " is unexcepted key class!");
			}
			entity.setId(key);
		}

		return rowCount >= 1;
	}

	public int create(Collection<ENTITY> entities) {
		if (entities == null || entities.size() == 0) {
			return 0;
		}
		
		//TODO 这里应该有一个分支，批量插入并且全部都获取key的做法

		String sql = queryHandler.generateInsertSQL(entities);
		Object[] params = queryHandler.generateInsertParams(entities);
		return jdbcTemplate.update(sql, params);
	}

	public boolean replace(ENTITY entity) {
		if (entity == null) {
			return false;
		}
		// TODO 暂不实现，意义不大
		return false;
	}

	public boolean update(ENTITY entity) {
		if (entity == null) {
			return false;
		}
		String sql = queryHandler.generateUpdateSQL(entity);
		Object[] params = queryHandler.generateUpdateParams(entity);

		return jdbcTemplate.update(sql, params) >= 1;
	}

	public int update(Collection<ENTITY> entities) {

		// 如果为空或这个集合都是空对象，则不更新
		final ArrayList<ENTITY> list = trimEntities(entities);
		if (list == null || list.size() == 0) {
			return 0;
		}

		String sql = queryHandler.generateUpdateSQL(list.get(0));
		int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Object[] params = queryHandler.generateUpdateParams(list.get(index));
				int i = 1;
				for (Object param : params) {
					ps.setObject(i++, param);
				}
			}

			public int getBatchSize() {
				return list.size();
			}
		});

		return sum(results);
	}

	/**
	 * 将数组内的元素加总
	 * 
	 * @param results
	 * @return created by Tianxin on 2015年5月28日 下午4:42:02
	 */
	private int sum(int[] results) {
		int total = 0;
		for (int i : results) {
			total += i;
		}
		return total;
	}

	/**
	 * @param entities
	 * @return created by Tianxin on 2015年5月28日 下午4:29:02
	 */
	private ArrayList<ENTITY> trimEntities(Collection<ENTITY> entities) {
		if (entities == null || entities.size() == 0) {
			return null;
		}

		ArrayList<ENTITY> list = new ArrayList<ENTITY>(entities.size());
		for (ENTITY e : entities) {
			if (e != null) {
				list.add(e);
			}
		}
		return list;
	}

	public boolean delete(KEY id) {
		if (id == null) {
			return false;
		}
		return delete(configKeeper.keyColumn(), id) > 0;
	}

	public boolean delete(ENTITY entity) {
		if (entity == null) {
			return false;
		}

		return delete(entity.getId());
	}

	public int delete(Collection<ENTITY> entities) {
		entities = trimEntities(entities);
		if (entities == null || entities.size() == 0) {
			return 0;
		}

		List<KEY> ids = extractKeys(entities);
		return delete(configKeeper.keyColumn(), ids);
	}

	/**
	 * @param entities
	 * @return created by Tianxin on 2015年5月28日 下午6:03:43
	 */
	private List<KEY> extractKeys(Collection<ENTITY> entities) {

		// TODO 这些private的方法该放在哪里
		List<KEY> keys = new ArrayList<KEY>(entities.size());
		for (ENTITY entity : entities) {
			keys.add(entity.getId());
		}
		return keys;
	}

	public int deleteByKeys(Collection<KEY> ids) {
		if (ids == null || ids.size() == 0) {
			return 0;
		}
		return delete(configKeeper.keyColumn(), ids);
	}
	
	public int deleteByKeys(KEY...ids) {
		if (ids == null || ids.length == 0) {
			return 0;
		}
		return delete(configKeeper.keyColumn(), ids);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:34:29
	 */
	protected int delete(String column, Object value) {
		return delete(Matches.one(column, value));
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:35:15
	 */
	protected int delete(Matches matches) {
		QueryDelete query = new QueryDelete(matches, queryHandler.table());
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.update(sql, params);
	}

	public ENTITY get(KEY id) {
		if (id == null) {
			return null;
		}
		List<ENTITY> entities = find(configKeeper.keyColumn(), id);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		return entities.get(0);
	}

	public List<ENTITY> get(Collection<KEY> ids) {
		if (ids == null || ids.size() == 0) {
			return null;
		}
		return find(configKeeper.keyColumn(), ids);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:29:59
	 */
	protected ENTITY findOne(String column, Object value) {
		List<ENTITY> entities = find(column, value);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		return entities.get(0);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:29:59
	 */
	protected List<ENTITY> find(String column, Object value) {
		return find(Matches.one(column, value));
	}

	public List<ENTITY> findAll() {
		return find(Matches.empty());
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected List<ENTITY> find(Matches matches) {
		return page(matches, 0, 0);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:29:59
	 */
	protected ENTITY findOne(Matches matches) {
		List<ENTITY> entities = find(matches);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		return entities.get(0);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected List<ENTITY> page(Matches matches, int offset, int limit) {
		List<String> allColumns = queryHandler.allColumns();
		QuerySelect query = new QuerySelect(allColumns, matches, null, configKeeper.table(), offset, limit);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper<ENTITY>(allColumns, columnMappers, entityClass));
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected <R extends Serializable> List<R> findOneColumn(Class<R> rClass, Matches matches, String column) {
		return pageOneColumn(rClass, matches, column, 0, 0);
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected <R extends Serializable> List<R> pageOneColumn(Class<R> rClass, Matches matches, String column, int offset, int limit) {
		List<String> columns = Arrays.asList(column);
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table(), offset, limit);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, BasicMappers.getMapper(rClass));
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected List<ENTITY> findSimple(Matches matches, String... columns) {
		return pageSimple(matches, 0, 0, columns);
	}

	protected <E extends BaseObject<?>> List<E> findBySQL(Class<E> eClass, String sql, Object... params) {
		return jdbcTemplate.query(sql, params, BasicMappers.getEntityMapper(eClass, sql));
	}

	/**
	 * created by Tianxin on 2015年5月26日 下午9:28:56
	 */
	protected List<ENTITY> pageSimple(Matches matches, int offset, int limit, String... columns) {
		List<String> choozenColumns = Arrays.asList(columns);
		QuerySelect query = new QuerySelect(choozenColumns, matches, null, configKeeper.table(), offset, limit);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper<ENTITY>(choozenColumns, columnMappers, entityClass));
	}

	public int countAll() {
		return count(Matches.empty());
	}

	protected int count(String column, Object value) {
		return count(Matches.one(column, value));
	}

	protected int count(Matches matches) {
		List<String> columns = Arrays.asList("count(1)");
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table());
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.queryForInt(sql, params);
	}

	protected List<Object> toList(Object... os) {
		if (os == null || os.length == 0) {
			return new ArrayList<Object>(0);
		}

		List<Object> list = new ArrayList<Object>(os.length);
		for (Object o : os) {
			list.add(o);
		}
		return list;
	}

	private List<ENTITY> toEntities(ENTITY entity) {
		List<ENTITY> entities = new ArrayList<ENTITY>(1);
		entities.add(entity);
		return entities;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
