/**
 * org.darwin.genericDao.mapper.stat.StatWriteSQLHandler.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午3:01:44
 */
package org.darwin.genericDao.mapper.stat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.genericDao.annotations.enums.Type;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * created by Tianxin on 2015年6月4日 下午3:01:44
 */
public class StatWriteSQLHandler<ENTITY> {

	
	//私有化无参构造器
	private StatWriteSQLHandler() {
	}


	/**
	 * @param columnMappers
	 * @param configKeeper
	 */
	public StatWriteSQLHandler(Map<String, ColumnMapper> columnMappers, StatAnnotationKeeper configKeeper) {
		
		this();
		this.tableGenerator = configKeeper;
		this.columnMappers = columnMappers;
		
		int mapperCount = columnMappers.size();
		List<String> insertColumns = new ArrayList<String>(mapperCount);
		List<String> updateColumns = new ArrayList<String>(mapperCount);
		List<String> allColumns = new ArrayList<String>(mapperCount);
		
		Collection<ColumnMapper> mappers = columnMappers.values();
		for(ColumnMapper mapper : mappers){
			//如果是扩展列，则不做处理
			if(mapper.getType() != null && mapper.getType().value() == Type.EXTEND){
				continue;
			}
			allColumns.add(mapper.getSQLColumn());
			insertColumns.add(mapper.getSQLColumn());
			if(mapper.getAnnotation() == null || mapper.getAnnotation().modifiable()){
				updateColumns.add(mapper.getSQLColumn());
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
		this.insertColumns = insertColumns;
		
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

	/**
	 * insert的SQL中参数的展位符——(?,?,?,?)
	 */
	private String sInsertPlaceHolder;
	private List<String> insertColumns;
	private String sInsertColumns;

	private Map<String, ColumnMapper> columnMappers;
	private StatAnnotationKeeper tableGenerator;

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

}
