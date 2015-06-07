/**
 * org.darwin.genericDao.dao.EntityMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午5:26:33
 */
package org.darwin.genericDao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * created by Tianxin on 2015年5月28日 下午5:26:33
 */
public class EntityMapper<ENTITY extends Object> implements RowMapper<ENTITY> {
	
	// 无参构造函数私有化
	private EntityMapper() {
	}

	/**
	 * @param columns
	 * @param columnMapperMap
	 */
	public EntityMapper(List<String> columns, Map<String, ColumnMapper> columnMapperMap, Class<ENTITY> entityClass) {
		this();
		this.entityClass = entityClass;
		this.columnMappers = new ArrayList<ColumnMapper>(columns.size());
		for(String column : columns){
			columnMappers.add(columnMapperMap.get(column));
		}
	}
	
	/**
	 * 字段映射的集合
	 */
	private List<ColumnMapper> columnMappers = null;

	/**
	 * 要装载的实体类的class定义
	 */
	private Class<ENTITY> entityClass;

	public ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

		try {
			ENTITY entity = entityClass.newInstance();
			for (ColumnMapper mapper : columnMappers) {
				mapper.loadColumn2Field(rs, entity);
			}
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
