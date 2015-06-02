/**
 * org.darwin.genericDao.dao.EntityMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午5:26:33
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.darwin.genericDao.bo.BaseObject;
import org.springframework.jdbc.core.RowMapper;

/**
 * created by Tianxin on 2015年5月28日 下午5:26:33
 */
public class EntityMapper<ENTITY extends BaseObject<?>> implements RowMapper<ENTITY> {

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
				Object value = getObjectForType(rs, column, setter.getParameterTypes()[0]);
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
		
		//尝试快速获取到字段值
		try{
			//加载到
			Class<?> rsClass = rs.getClass();
			Map<Class<?>, Method> typeMethodMap = ensureTypeMethod(rsClass);
			Method m = typeMethodMap.get(rClass);
			if(m != null){
				return m.invoke(rs, column);
			}
		}catch(Exception e){
		}

		//如果无法快速获取，则通过一串的类型判断来获取
		// 如果是基本类型
		if (rClass.isPrimitive()) {
			if (Integer.TYPE.isAssignableFrom(rClass)) {
				return rs.getInt(column);
			} else if (Long.TYPE.isAssignableFrom(rClass)) {
				return rs.getLong(column);
			} else if (Float.TYPE.isAssignableFrom(rClass)) {
				return rs.getFloat(column);
			} else if (Double.TYPE.isAssignableFrom(rClass)) {
				return rs.getDouble(column);
			} else if (Short.TYPE.isAssignableFrom(rClass)) {
				return rs.getShort(column);
			} else if (Boolean.TYPE.isAssignableFrom(rClass)) {
				return rs.getBoolean(column);
			} else if (Byte.TYPE.isAssignableFrom(rClass)) {
				return rs.getByte(column);
			}
		//如果是Number的子类
		} else if (Number.class.isAssignableFrom(rClass)) {
			if (Integer.class.isAssignableFrom(rClass)) {
				return rs.getInt(column);
			} else if (Long.class.isAssignableFrom(rClass)) {
				return rs.getLong(column);
			} else if (Float.class.isAssignableFrom(rClass)) {
				return rs.getFloat(column);
			} else if (Double.class.isAssignableFrom(rClass)) {
				return rs.getDouble(column);
			} else if (Short.class.isAssignableFrom(rClass)) {
				return rs.getShort(column);
			} else if (Byte.class.isAssignableFrom(rClass)) {
				return rs.getByte(column);
			}
		//如果是字符串
		} else if (String.class.isAssignableFrom(rClass)) {
			return rs.getString(column);
		//如果是日期类型
		} else if (java.util.Date.class.isAssignableFrom(rClass)) {
			if (Date.class.isAssignableFrom(rClass)) {
				return rs.getDate(column);
			} else if (Timestamp.class.isAssignableFrom(rClass)) {
				return rs.getTimestamp(column);
			} else if (Time.class.isAssignableFrom(rClass)) {
				return rs.getTime(column);
			}
		//如果是boolean类型
		} else if (Boolean.class.isAssignableFrom(rClass)) {
			return rs.getBoolean(column);
		} else if (BigDecimal.class.isAssignableFrom(rClass)) {
			return rs.getBigDecimal(column);
		} else if (URL.class.isAssignableFrom(rClass)) {
			return rs.getURL(column);
		} else if (Blob.class.isAssignableFrom(rClass)) {
			return rs.getBlob(column);
		} else if (Clob.class.isAssignableFrom(rClass)) {
			return rs.getClob(column);
		}
		return rs.getObject(column);
	}
	
	/**
	 * resultset可能有多个实现，所以这里要根据实际的resultSet的class来获取一个getXXX的方法map
	 * @param rsClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private static Map<Class<?>, Method> ensureTypeMethod(Class<?> rsClass) throws NoSuchMethodException {
		Map<Class<?>, Method> typeMethodMap = rsTypeMethodMap.get(rsClass);
		if(typeMethodMap != null){
			return typeMethodMap;
		}
		
		typeMethodMap = new HashMap<Class<?>, Method>();
		loadTypeMethods(typeMethodMap, rsClass);
		rsTypeMethodMap.put(rsClass, typeMethodMap);
		return typeMethodMap;
	}

	/**
	 * 将某一个ResultSet的类解析为一个类型和对应getInt的方法集
	 * @param typeMethodMap
	 * @param rsClass
	 * @throws NoSuchMethodException
	 */
	private static void loadTypeMethods(Map<Class<?>, Method> typeMethodMap,
			Class<?> rsClass) throws NoSuchMethodException {
		
		typeMethodMap.put(Integer.TYPE, getParamStringMethod(rsClass, "getInt"));
		typeMethodMap.put(Integer.class, getParamStringMethod(rsClass, "getInt"));
		typeMethodMap.put(Long.TYPE, getParamStringMethod(rsClass, "getLong"));
		typeMethodMap.put(Long.class, getParamStringMethod(rsClass, "getLong"));
		typeMethodMap.put(Double.TYPE, getParamStringMethod(rsClass, "getDouble"));
		typeMethodMap.put(Double.class, getParamStringMethod(rsClass, "getDouble"));
		typeMethodMap.put(Float.TYPE, getParamStringMethod(rsClass, "getFloat"));
		typeMethodMap.put(Float.class, getParamStringMethod(rsClass, "getFloat"));
		typeMethodMap.put(Short.TYPE, getParamStringMethod(rsClass, "getShort"));
		typeMethodMap.put(Short.class, getParamStringMethod(rsClass, "getShort"));
		typeMethodMap.put(Boolean.TYPE, getParamStringMethod(rsClass, "getBoolean"));
		typeMethodMap.put(Boolean.class, getParamStringMethod(rsClass, "getBoolean"));
		typeMethodMap.put(Byte.TYPE, getParamStringMethod(rsClass, "getByte"));
		typeMethodMap.put(Byte.class, getParamStringMethod(rsClass, "getByte"));
		typeMethodMap.put(String.class, getParamStringMethod(rsClass, "getString"));
		typeMethodMap.put(java.util.Date.class, getParamStringMethod(rsClass, "getTimestamp"));
		typeMethodMap.put(Timestamp.class, getParamStringMethod(rsClass, "getTimestamp"));
		typeMethodMap.put(Time.class, getParamStringMethod(rsClass, "getTime"));
		typeMethodMap.put(Date.class, getParamStringMethod(rsClass, "getDate"));
		typeMethodMap.put(BigDecimal.class, getParamStringMethod(rsClass, "getBigDecimal"));
		typeMethodMap.put(URL.class, getParamStringMethod(rsClass, "getURL"));
	}

	/**
	 * 根据getXXX的方法名获取参数为字符串的方法
	 * @param rsClass
	 * @param methodName
	 * @return
	 * @throws NoSuchMethodException
	 */
	private static Method getParamStringMethod(Class<?> rsClass, String methodName) throws NoSuchMethodException {
		return rsClass.getMethod(methodName, String.class);
	}

	/**
	 * 第一层key是ResultSet的class,第二层key是setter的参数类型的class，value是rs.getXXX的方法
	 */
	private final static Map<Class<?>, Map<Class<?>, Method>> rsTypeMethodMap = new HashMap<Class<?>, Map<Class<?>,Method>>();

}
