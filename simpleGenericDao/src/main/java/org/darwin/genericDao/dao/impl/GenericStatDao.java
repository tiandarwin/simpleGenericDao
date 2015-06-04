/**
 * org.darwin.genericDao.dao.impl.GenericStatDao.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午2:05:00
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.StatGenericDaoUtils;
import org.darwin.genericDao.annotations.stat.StatTable;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.bo.BaseStatObject;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.EntityMapper;
import org.darwin.genericDao.mapper.stat.StatAnnotationKeeper;
import org.darwin.genericDao.mapper.stat.StatColumnMapper;
import org.darwin.genericDao.operate.Groups;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Orders;
import org.darwin.genericDao.param.SQLParams;
import org.darwin.genericDao.query.QuerySelect;
import org.darwin.genericDao.query.QueryStat;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 统计表的默认DAO实现
 * created by Tianxin on 2015年6月3日 下午2:05:00
 */
public class GenericStatDao<ENTITY extends BaseStatObject>{
	
	/**
	 * 无参构造函数
	 */
	public GenericStatDao() {
		Class<ENTITY> entityClass = StatGenericDaoUtils.getEntityClass((Class<?>)(this.getClass()));
		StatTable table = StatGenericDaoUtils.getStatTable(entityClass);

		this.entityClass = entityClass;
		this.configKeeper = new StatAnnotationKeeper(table);
		this.columnMappers = StatGenericDaoUtils.generateStatColumnMappers(entityClass, table.columnStyle());
		this.analysisColumns(columnMappers);
	}
	
	/**
	 * 分析statObject中的所有列类型
	 * @param columnMappers
	 * created by Tianxin on 2015年6月3日 下午3:56:37
	 */
	private void analysisColumns(Map<String, StatColumnMapper> columnMappers) {
		Collection<StatColumnMapper> mappers = columnMappers.values();
		for (StatColumnMapper mapper : mappers) {
			allColumns.add(mapper.getColumn());
			switch (mapper.getType().value()) {
			case KEY:
				keyColumns.add(mapper.getColumn());
				break;
			case SUM:
				sumColumns.add(mapper.getColumn());
				break;
			case AVG:
				avgColumns.add(mapper.getColumn());
				break;
			case DATE:
				dateColumn = mapper.getColumn();
				keyColumns.add(dateColumn);
				break;
			default:
				break;
			}
		}
	}
	private String dateColumn = null;
	private List<String> sumColumns = new ArrayList<String>();
	private List<String> avgColumns = new ArrayList<String>();
	private List<String> keyColumns = new ArrayList<String>();
	private List<String> allColumns = new ArrayList<String>();
	
	private Class<ENTITY> entityClass = null;
	private JdbcTemplate jdbcTemplate;
	private StatAnnotationKeeper configKeeper;
	private Map<String, StatColumnMapper> columnMappers;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 获取数据，如果需要按时间聚合，则按其他字段groupBy，把不同时间的汇总到一起
	 * @param aggregationByDate
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:15:49
	 */
	public List<ENTITY> statAll(boolean aggregationByDate) {
		Groups groups = aggregationByDate ? generateAggergationByDateGroups() : Groups.init();
		QueryStat query = new QueryStat(sumColumns, avgColumns, keyColumns, null, null, groups, configKeeper.table());
		return statByQuery(query);
	}

	/**
	 * 按Query进行查询
	 * @param query
	 * @return
	 * created by Tianxin on 2015年6月3日 下午4:06:19
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<ENTITY> statByQuery(QueryStat query) {
		String sql = query.getSQL();
		Object[] params = query.getParams();
		List<String> columns = query.getColumns();
		System.out.println(sql);
		return jdbcTemplate.query(sql, params, new EntityMapper(columns, columnMappers, entityClass));
	}

	/**
	 * 查询某个时间范围的数据
	 * @param startDate
	 * @param endDate
	 * @param aggregationByDate
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:16:37
	 */
	public List<ENTITY> statByRange(int startDate, int endDate, boolean aggregationByDate) {
		Matches matches = Matches.one(dateColumn, SQLParams.between(startDate, endDate));
		Groups groups = aggregationByDate ? generateAggergationByDateGroups() : Groups.init();
		QueryStat query = new QueryStat(sumColumns, avgColumns, keyColumns, matches, null, groups, configKeeper.table());
		return statByQuery(query);
	}

	/**
	 * 当按时间聚合时，groupBy的字段为除去date字段的其他key字段
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:16:55
	 */
	private Groups generateAggergationByDateGroups(){
		Groups groups = Groups.init();
		for(String column : keyColumns){
			if(!column.equals(dateColumn)){
				groups.groupBy(column);
			}
		}
		return groups;
	}

	/**
	 * 计算DB中的数据总数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:17:22
	 */
	public int countAll() {
		return count(Matches.empty());
	}

	/**
	 * 计算column字段match到value时候的count数
	 * @param column
	 * @param value
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:17:35
	 */
	protected int count(String column, Object value) {
		return count(Matches.one(column, value));
	}

	/**
	 * 查询符合match的匹配条件的数据量
	 * @param matches
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:18:12
	 */
	protected int count(Matches matches) {
		List<String> columns = Arrays.asList("count(1)");
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table());
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.queryForInt(sql, params);
	}

	/**
	 * 获取所有的数据
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:15:38
	 */
	public List<ENTITY> findAll(){
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ENTITY> pageColumns(Matches matches, Orders orders, int offset, int rows, String... columns) {
		List<String> choozenColumns = Arrays.asList(columns);
		QuerySelect query = new QuerySelect(choozenColumns, matches, orders, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper(choozenColumns, columnMappers, entityClass));
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


}
