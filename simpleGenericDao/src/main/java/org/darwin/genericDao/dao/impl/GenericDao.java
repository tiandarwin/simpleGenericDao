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
import org.darwin.genericDao.operate.Modifies;
import org.darwin.genericDao.operate.Orders;
import org.darwin.genericDao.query.QueryDelete;
import org.darwin.genericDao.query.QueryModify;
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

	/**
	 * 该方法会对ENTITY逐条插入，每插入一条时会获取一下数据库中生成的key
	 * @param entities
	 * @return
	 */
	public int createAndFetchKey(Collection<ENTITY> entities) {
		if (entities == null || entities.size() == 0) {
			return 0;
		}
		
		int createdCount = 0;
		for(ENTITY entity :entities){
			createdCount += create(entity) ? 1 : 0;
		}
		return createdCount;
	}

	@SuppressWarnings("unchecked")
	public boolean create(ENTITY entity) {
		if (entity == null) {
			return false;
		}

		// 获取sql以及参数
		List<ENTITY> entities = GenericDaoUtils.toEntities(entity);
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

		String sql = queryHandler.generateInsertSQL(entities);
		Object[] params = queryHandler.generateInsertParams(entities);
		return jdbcTemplate.update(sql, params);
	}
	
	public int replace(Collection<ENTITY> entities) {
		if (entities == null) {
			return 0;
		}
		
		// 获取sql以及参数
		String sql = queryHandler.generateInsertSQL(entities);
		Object[] params = queryHandler.generateInsertParams(entities);
		
		// 只是将insert替换成replace就变成了replace的SQL语句
		sql = sql.replaceFirst("insert", "replace");
		
		return jdbcTemplate.update(sql, params);
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
		entities = GenericDaoUtils.trimEntities(entities);
		if (entities == null || entities.size() == 0) {
			return 0;
		}

		List<KEY> ids = GenericDaoUtils.extractKeys(entities);
		return delete(configKeeper.keyColumn(), ids);
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
	 * 如果column的取值match到了value则进行删除
	 * @param column	字段名
	 * @param value	匹配值
	 * @return	删除条数
	 * created by Tianxin on 2015年6月3日 下午8:33:29
	 */
	protected int delete(String column, Object value) {
		return delete(Matches.one(column, value));
	}

	/**
	 * 按照匹配条件删除数据
	 * @param matches	匹配条件，可为null
	 * @return	删除条数
	 * created by Tianxin on 2015年6月3日 下午8:34:03
	 */
	protected int delete(Matches matches) {
		QueryDelete query = new QueryDelete(matches, queryHandler.table());
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.update(sql, params);
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
		final List<ENTITY> list = GenericDaoUtils.trimEntities(entities);
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
		
		//计算影响的数据条数
		int updatedCount = 0;
		for (int i : results) {
			updatedCount += i;
		}
		return updatedCount;
	}
	
	/**
	 * 按照匹配条件进行更新
	 * @param modifies	数据修改定义	
	 * @param matches	匹配条件
	 * @return
	 */
	protected int update(Modifies modifies, Matches matches){
		QueryModify modify = new QueryModify(modifies, matches, configKeeper.table());
		String sql = modify.getSQL();
		Object[] args = modify.getParams();
		return jdbcTemplate.update(sql, args);
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
	 * 获取所有的数据
	 */
	public List<ENTITY> findAll() {
		return find(Matches.empty());
	}

	/**
	 * 获取符合条件的第一条记录
	 * @param column	字段名
	 * @param value	字段取值
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:34:36
	 */
	protected ENTITY findOne(String column, Object value) {
		return findOne(Matches.one(column, value));
	}

	/**
	 * 获取符合条件的数据
	 * @param column	字段名
	 * @param value	字段取值
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:43:57
	 */
	protected List<ENTITY> find(String column, Object value) {
		return find(Matches.one(column, value));
	}

	/**
	 * 获取符合匹配条件的数据
	 * @param matches	匹配条件，可为null
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:44:48
	 */
	protected List<ENTITY> find(Matches matches) {
		return find(matches, null);
	}
	
	/**
	 * 获取符合匹配条件的数据，并按orders排序
	 * @param matches	匹配条件，可为null
	 * @param orders	排序规则，可以为null
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:45:12
	 */
	protected List<ENTITY> find(Matches matches, Orders orders) {
		return page(matches, orders, 0, 0);
	}

	/**
	 * 获取符合匹配条件的第一条记录
	 * @param matches	匹配条件，可为null
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:46:04
	 */
	protected ENTITY findOne(Matches matches) {
		return findOne(matches, null);
	}
	
	/**
	 * 获取符合匹配条件的按orders排序后的第一调数据
	 * @param matches	匹配条件，可为null
	 * @param orders	排序规则，可为null
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:46:29
	 */
	protected ENTITY findOne(Matches matches, Orders orders) {
		List<ENTITY> entities = page(matches, orders, 0, 1);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		return entities.get(0);
	}

	/**
	 * 分页查询
	 * @param matches	匹配条件，可为null
	 * @param orders	匹配条件，可为null
	 * @param offset	起始位置
	 * @param rows	获取条数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:47:09
	 */
	protected List<ENTITY> page(Matches matches, Orders orders, int offset, int rows) {
		List<String> allColumns = queryHandler.allColumns();
		return pageColumns(matches, orders, offset, rows, allColumns.toArray(new String[allColumns.size()]));
	}

	/**
	 * 查询简单对象，只获取其中几列
	 * @param matches	匹配条件，可为null
	 * @param columns	字段名
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:49:03
	 */
	protected List<ENTITY> findSimple(Matches matches, String... columns) {
		return findSimple(matches, null, columns);
	}

	/**
	 * 查询简单对象，只获取几列
	 * @param matches	匹配条件，可为null
	 * @param orders	排序规则，可为null
	 * @param columns	字段集合
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:49:49
	 */
	protected List<ENTITY> findSimple(Matches matches, Orders orders, String... columns) {
		return pageColumns(matches, orders, 0, 0, columns);
	}

	/**
	 * 分页查询简单对象
	 * @param matches	匹配条件，可为null
	 * @param orders	排序规则，可为null
	 * @param offset	起始位置
	 * @param rows	获取条数
	 * @param columns	字段集合
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:50:57
	 */
	protected List<ENTITY> pageColumns(Matches matches, Orders orders, int offset, int rows, String... columns) {
		List<String> choozenColumns = Arrays.asList(columns);
		QuerySelect query = new QuerySelect(choozenColumns, matches, orders, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper<ENTITY>(choozenColumns, columnMappers, entityClass));
	}

	/**
	 * 查询某一列
	 * @param rClass	结果类型
	 * @param matches	匹配条件，可为null
	 * @param column	获取哪一列
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:47:59
	 */
	protected <R extends Serializable> List<R> findOneColumn(Class<R> rClass, Matches matches, String column) {
		return pageOneColumn(rClass, matches, column, 0, 0);
	}

	/**
	 * 分页查询某一列
	 * @param rClass	结果类型
	 * @param matches	匹配条件
	 * @param column	字段名
	 * @param offset	起始位置
	 * @param rows	获取条数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:48:26
	 */
	protected <R extends Serializable> List<R> pageOneColumn(Class<R> rClass, Matches matches, String column, int offset, int rows) {
		List<String> columns = Arrays.asList(column);
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, BasicMappers.getMapper(rClass));
	}

	/**
	 * 根据SQL查询结果
	 * @param eClass	结果映射到的对象
	 * @param sql	查询SQL
	 * @param params	参数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:50:26
	 */
	protected <E extends BaseObject<?>> List<E> findBySQL(Class<E> eClass, String sql, Object... params) {
		return jdbcTemplate.query(sql, params, BasicMappers.getEntityMapper(eClass, sql));
	}

	/**
	 * 查询记录条数
	 */
	public int countAll() {
		return count(Matches.empty());
	}

	/**
	 * 查询符合条件的记录条数
	 * @param column	字段名字
	 * @param value	字段匹配值
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:51:43
	 */
	protected int count(String column, Object value) {
		return count(Matches.one(column, value));
	}

	/**
	 * 查询符合条件的结果条数
	 * @param matches	匹配条件，可为null
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:52:01
	 */
	protected int count(Matches matches) {
		List<String> columns = Arrays.asList("count(1)");
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table());
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return countBySQL(sql, params);
	}
	
	/**
	 * 暴露SQL接口对外
	 * @param sql
	 * @param params
	 * @return
	 */
	protected int countBySQL(String sql, Object[] params){
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

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
