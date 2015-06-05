/**
 * org.darwin.genericDao.dao.EntityMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午5:26:33
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * @param columnMappers
	 */
	public EntityMapper(List<String> columns, Map<String, ColumnMapper> columnMappers, Class<ENTITY> entityClass) {
		this();
		this.columns = columns;
		this.entityClass = entityClass;
		this.columnMappers = columnMappers;
	}

	/**
	 * 该mapper需要获取的字段名集合
	 */
	private List<String> columns;

	/**
	 * 要装载的实体类的class定义
	 */
	private Class<ENTITY> entityClass;

	/**
	 * 字段映射的map
	 */
	private Map<String, ColumnMapper> columnMappers;

	public ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

		try {
			ENTITY entity = entityClass.newInstance();
			for (String column : columns) {
				ColumnMapper mapper = columnMappers.get(column);
				if (mapper == null) {
					// TODO 这里应该需要记录错误日志
					continue;
				}

				Method setter = mapper.getSetter();
				Class<?> paramClass = setter.getName().equals("setId") ? BasicMappers.getKeyClass(entityClass) : setter.getParameterTypes()[0];
				Object value = getObjectForType(rs, column, paramClass);
				setter.invoke(entity, value);
			}

			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从rs中获取数据，以targetClass的类型
	 * 
	 * @param rs
	 * @param column
	 * @param rClass
	 * @return created by Tianxin on 2015年6月2日 下午3:07:05
	 * @throws SQLException
	 */
	private Object getObjectForType(ResultSet rs, String column, Class<?> rClass) throws SQLException {
		
		//默认按照getObject来获取，如果类型正确则直接返回
		Object value = rs.getObject(column);
		if(rClass.isAssignableFrom(value.getClass())){
			return value;
		}
		
		//尝试快速获取到字段值
		try{
			//加载到
			Class<?> rsClass = rs.getClass();
			Method m = ResultSetGettersCache.getGetters(rsClass).getLableGetter(rClass);
			if(m != null){
				return m.invoke(rs, column);
			}
		}catch(Exception e){
		}
		return value;
	}
	


}
