/**
 * org.darwin.genericDao.query.QueryHandler.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午6:46:45
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * created by Tianxin on 2015年5月27日 下午6:46:45
 */
public class QueryHandler<KEY extends Serializable, ENTITY extends BaseObject<KEY>> {
	
	//私有化无参构造器
	private QueryHandler() {
	}


	/**
	 * @param columnMappers
	 * @param configKeeper
	 */
	public QueryHandler(Map<String, ColumnMapper> columnMappers, AnnotationConfigKeeper configKeeper) {
		
		this();
		this.tableGenerator = configKeeper;
		this.columnMappers = columnMappers;
		
		int mapperCount = columnMappers.size();
		List<String> insertColumns = new ArrayList<String>(mapperCount);
		List<String> updateColumns = new ArrayList<String>(mapperCount);
		List<String> allColumns = new ArrayList<String>(mapperCount);
		
		Collection<ColumnMapper> mappers = columnMappers.values();
		for(ColumnMapper mapper : mappers){
			allColumns.add(mapper.getColumn());
			insertColumns.add(mapper.getColumn());
			if(mapper.getAnnotation() == null || mapper.getAnnotation().modifiable()){
				updateColumns.add(mapper.getColumn());
			}
		}
		
		initColumns(insertColumns, updateColumns, allColumns);
	}
	
	/**
	 * 初始化字段列表的设置
	 * @param insertColumns
	 * @param updateColumns
	 * @param allColumns
	 * created by Tianxin on 2015年6月1日 上午6:56:20
	 */
	private void initColumns(List<String> insertColumns, List<String> updateColumns, List<String> allColumns) {
		
		//普通的字段保存
		this.allColumns = allColumns;
		this.insertColumns = insertColumns;
		this.updateColumns = updateColumns;
		
		//构造insert语句的字段列表与展位符
		StringBuilder sInsertColumnBuilder = new StringBuilder(insertColumns.size() * 9);
		StringBuilder sInsertPHolderBuilder = new StringBuilder(insertColumns.size() * 2 + 2);
		for(String column : insertColumns){
			sInsertColumnBuilder.append(',').append(column);
			sInsertPHolderBuilder.append(",?");
		}
		sInsertColumnBuilder.append(')').setCharAt(0, '(');
		sInsertPHolderBuilder.append(')').setCharAt(0, '(');
		this.sInsertColumns = sInsertColumnBuilder.toString();
		this.sInsertPlaceHolder = sInsertPHolderBuilder.toString();
	}

	private List<String> allColumns;
	private List<String> updateColumns;

	/**
	 * insert的SQL中参数的展位符——(?,?,?,?)
	 */
	private String sInsertPlaceHolder;
	private List<String> insertColumns;
	private String sInsertColumns;

	private Map<String, ColumnMapper> columnMappers;
	private AnnotationConfigKeeper tableGenerator;

	/**
	 * @return created by Tianxin on 2015年5月27日 下午6:47:12
	 */
	public String table() {
		return tableGenerator.table();
	}

	/**
	 * 获取insert时的参数列表
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午7:42:28
	 */
	public Object[] generateInsertParams(Collection<ENTITY> entities) {

		ArrayList<Object> params = new ArrayList<Object>(insertColumns.size() * entities.size());
		for (ENTITY entity : entities) {

			if (entity == null) {
				continue;
			}

			params.addAll(getParamsByColumns(insertColumns, entity));
		}
		return params.toArray();
	}

	/**
	 * @param params
	 * @param entity
	 *            created by Tianxin on 2015年5月27日 下午8:22:04
	 */
	private List<Object> getParamsByColumns(List<String> columns, ENTITY entity) {
		ArrayList<Object> params = new ArrayList<Object>(columns.size() + 1);
		for (String column : insertColumns) {
			try {
				ColumnMapper cMapper = columnMappers.get(column);
				Object value = cMapper.getGetter().invoke(entity);
				params.add(value);
			} catch (Exception e) {
				throw new RuntimeException(column + "getter invoke error!", e);
			}
		}
		return params;
	}

	/**
	 * 生成insert语句
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午7:44:14
	 */
	public String generateInsertSQL(Collection<ENTITY> entities) {
		StringBuilder sb = new StringBuilder(512);
		sb.append("insert into ").append(table());
		sb.append(' ').append(sInsertColumns).append(" values ");
		for (ENTITY entity : entities) {
			if (entity != null) {
				sb.append(sInsertPlaceHolder).append(',');
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 生成update的参数列表
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午7:42:28
	 */
	public Object[] generateUpdateParams(ENTITY entity) {
		List<Object> params = getParamsByColumns(updateColumns, entity);
		params.add(entity.getId());
		return params.toArray();
	}

	/**
	 * 生成update的SQL语句
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午7:44:14
	 */
	public String generateUpdateSQL(ENTITY entity) {
		StringBuilder sb = new StringBuilder(512);
		sb.append("update ").append(table()).append(" set ");
		for (String column : updateColumns) {
			sb.append(column).append("=?,");
		}
		sb.setCharAt(sb.length() - 1, ' ');
		sb.append(" where ").append(tableGenerator.keyColumn()).append("=?");
		return sb.toString();
	}

	/**
	 * @return
	 * created by Tianxin on 2015年5月28日 下午5:22:32
	 */
	public List<String> allColumns() {
		return this.allColumns;
	}
}
