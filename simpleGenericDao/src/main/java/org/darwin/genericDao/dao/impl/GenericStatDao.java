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
import org.darwin.genericDao.bo.BaseStatObject;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.EntityMapper;
import org.darwin.genericDao.mapper.stat.StatAnnotationKeeper;
import org.darwin.genericDao.mapper.stat.StatColumnMapper;
import org.darwin.genericDao.operate.Groups;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.param.SQLParams;
import org.darwin.genericDao.query.QuerySelect;
import org.darwin.genericDao.query.QueryStat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
	 * 获取所有的数据
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:15:38
	 */
	public List<ENTITY> findAll(){
		return find(Matches.empty());
	}
	
	/**
	 * 获取数据，如果需要按时间聚合，则按其他字段groupBy，把不同时间的汇总到一起
	 * @param aggregationByDate
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:15:49
	 */
	public List<ENTITY> findAll(boolean aggregationByDate) {
		Groups groups = aggregationByDate ? generateAggergationByDateGroups() : Groups.init();
		QueryStat query = new QueryStat(sumColumns, avgColumns, keyColumns, null, null, groups, configKeeper.table());
		return findByQuery(query);
	}

	/**
	 * 按Query进行查询
	 * @param query
	 * @return
	 * created by Tianxin on 2015年6月3日 下午4:06:19
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<ENTITY> findByQuery(QueryStat query) {
		String sql = query.getSQL();
		Object[] params = query.getParams();
		List<String> columns = query.getColumns();
		System.out.println(sql);
		return jdbcTemplate.query(sql, params, new EntityMapper(columns, columnMappers, entityClass));
	}
	
	/**
	 * 根据SQL进行查询，返回结果
	 * @param eClass
	 * @param sql
	 * @param args
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:16:20
	 */
	protected <E extends BaseStatObject> List<E> findBySQL(Class<E> eClass, String sql, Object...args){
		RowMapper<E> mapper = BasicMappers.getEntityMapper(eClass, sql);
		return jdbcTemplate.query(sql, args, mapper);
	}
	
	/**
	 * 查询某个时间范围的数据
	 * @param startDate
	 * @param endDate
	 * @param aggregationByDate
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:16:37
	 */
	public List<ENTITY> findByRange(int startDate, int endDate, boolean aggregationByDate) {
		Matches matches = Matches.one(dateColumn, SQLParams.between(startDate, endDate));
		Groups groups = aggregationByDate ? generateAggergationByDateGroups() : Groups.init();
		QueryStat query = new QueryStat(sumColumns, avgColumns, keyColumns, matches, null, groups, configKeeper.table());
		return findByQuery(query);
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
	 * 根据一个match来查找一个列表
	 * @param matches
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:22:19
	 */
	protected List<ENTITY> find(Matches matches) {
		return page(matches, 0, 0);
	}

	/**
	 * 根据一个match来分页查找
	 * @param matches	匹配条件
	 * @param offset	偏移量
	 * @param rows	记录条数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:21:55
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ENTITY> page(Matches matches, int offset, int rows) {
		QuerySelect query = new QuerySelect(allColumns, matches, null, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper(allColumns, columnMappers, entityClass));
	}

	/**
	 * 按条件取一条记录
	 * @param matches
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:18:31
	 */
	protected ENTITY findOne(Matches matches) {
		List<ENTITY> entities = find(matches);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		return entities.get(0);
	}

	/**
	 * 按条件取某一列，将其作为一个list
	 * @param rClass	结果装载为的类型
	 * @param matches	匹配条件
	 * @param column	列名
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:18:45
	 */
	protected <R extends Serializable> List<R> findOneColumn(Class<R> rClass, Matches matches, String column) {
		return pageOneColumn(rClass, matches, column, 0, 0);
	}

	/**
	 * 按条件，分页查询某一列
	 * @param rClass	结果装载的类型
	 * @param matches	匹配条件
	 * @param column	列名
	 * @param offset	偏移量
	 * @param rows	记录条数
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:19:25
	 */
	protected <R extends Serializable> List<R> pageOneColumn(Class<R> rClass, Matches matches, String column, int offset, int rows) {
		List<String> columns = Arrays.asList(column);
		QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, BasicMappers.getMapper(rClass));
	}

	/**
	 * 查询简单对象
	 * @param matches	匹配条件
	 * @param columns	获取的列集合
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:20:11
	 */
	protected List<ENTITY> findSimple(Matches matches, String... columns) {
		return pageSimple(matches, 0, 0, columns);
	}

	/**
	 * 分页查询一些简单对象
	 * @param matches	匹配条件
	 * @param offset	偏移量
	 * @param rows	记录条数
	 * @param columns	列名
	 * @return
	 * created by Tianxin on 2015年6月3日 下午8:20:56
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ENTITY> pageSimple(Matches matches, int offset, int rows, String... columns) {
		List<String> choozenColumns = Arrays.asList(columns);
		QuerySelect query = new QuerySelect(choozenColumns, matches, null, configKeeper.table(), offset, rows);
		String sql = query.getSQL();
		Object[] params = query.getParams();
		return jdbcTemplate.query(sql, params, new EntityMapper(choozenColumns, columnMappers, entityClass));
	}


}
