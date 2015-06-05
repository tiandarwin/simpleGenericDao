/**
 * org.darwin.genericDao.mapper.ResultSetGettersCache.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午8:50:58
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

import org.darwin.common.utils.Utils;

/**
 * ResultSet的getter的解析缓存
 * created by Tianxin on 2015年6月4日 下午8:50:58
 */
public class ResultSetGettersCache {
	
	/**
	 * 根据当前的result获取到其getter方法集
	 * @param resultSetClass
	 * @return
	 * created by Tianxin on 2015年6月4日 下午9:04:01
	 */
	public final static ResultSetGettersCache getGetters(Class<?> resultSetClass){
		ResultSetGettersCache o = map.get(resultSetClass);
		if(o == null){
			o = new ResultSetGettersCache(resultSetClass);
			map.put(resultSetClass, o);
		}
		return o;
	}
	
	/**
	 * 缓存的持有者
	 */
	private final static Map<Class<?>, ResultSetGettersCache> map = Utils.newMap();
	
	private ResultSetGettersCache() {
	}
	
	/**
	 * 解析一个ResultSet的子类
	 * @param rsClass
	 */
	private ResultSetGettersCache(Class<?> rsClass) {
		this();
		cache.put(Integer.TYPE, buildResultSetGetters(rsClass, Integer.TYPE, "getInt"));
		cache.put(Integer.class, buildResultSetGetters(rsClass, Integer.class, "getInt"));
		cache.put(Long.TYPE, buildResultSetGetters(rsClass, Long.TYPE, "getLong"));
		cache.put(Long.class, buildResultSetGetters(rsClass, Long.class, "getLong"));
		cache.put(Double.TYPE, buildResultSetGetters(rsClass, Double.TYPE, "getDouble"));
		cache.put(Double.class, buildResultSetGetters(rsClass, Double.class, "getDouble"));
		cache.put(Float.TYPE, buildResultSetGetters(rsClass, Float.TYPE, "getFloat"));
		cache.put(Float.class, buildResultSetGetters(rsClass, Float.class, "getFloat"));
		cache.put(Short.TYPE, buildResultSetGetters(rsClass, Short.TYPE, "getShort"));
		cache.put(Short.class, buildResultSetGetters(rsClass, Short.class, "getShort"));
		cache.put(Boolean.TYPE, buildResultSetGetters(rsClass, Boolean.TYPE, "getBoolean"));
		cache.put(Boolean.class, buildResultSetGetters(rsClass, Boolean.class, "getBoolean"));
		cache.put(Byte.TYPE, buildResultSetGetters(rsClass, Byte.TYPE, "getByte"));
		cache.put(Byte.class, buildResultSetGetters(rsClass, Byte.class, "getByte"));
		cache.put(String.class, buildResultSetGetters(rsClass, String.class, "getString"));
		cache.put(java.util.Date.class, buildResultSetGetters(rsClass, java.util.Date.class, "getTimestamp"));
		cache.put(Timestamp.class, buildResultSetGetters(rsClass, Timestamp.class, "getTimestamp"));
		cache.put(Time.class, buildResultSetGetters(rsClass, Time.class, "getTime"));
		cache.put(Date.class, buildResultSetGetters(rsClass, Date.class, "getDate"));
		cache.put(BigDecimal.class, buildResultSetGetters(rsClass, BigDecimal.class, "getBigDecimal"));
		cache.put(URL.class, buildResultSetGetters(rsClass, URL.class, "getURL"));
		cache.put(Clob.class, buildResultSetGetters(rsClass, Clob.class, "getClob"));
		cache.put(Blob.class, buildResultSetGetters(rsClass, Blob.class, "getBlob"));
		this.objectGetters = buildResultSetGetters(rsClass, Object.class, "getObject");
	}
	

	/**
	 * @param rsClass
	 * @param type
	 * @param name
	 * @return
	 * created by Tianxin on 2015年6月4日 下午8:57:46
	 */
	private ResultSetGetters buildResultSetGetters(Class<?> rsClass, Class<?> type, String name) {
		try {
			Method indexGetter = rsClass.getMethod(name, Integer.TYPE);
			Method labelGetter = rsClass.getMethod(name, String.class);
			return new ResultSetGetters(type, indexGetter, labelGetter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取利用index来取得type类型字段值的方法
	 * @param type
	 * @return
	 * created by Tianxin on 2015年6月4日 下午8:38:28
	 */
	public final Method getIndexGetter(Class<?> type){
		if(type == null){
			return objectGetters.getIndexGetter();
		}
		ResultSetGetters o = cache.get(type);
		if(o == null){
			return objectGetters.getIndexGetter();
		}
		return o.getIndexGetter();
	}
	
	/**
	 * 获取利用label来取得type类型字段值的方法
	 * @param type
	 * @return
	 * created by Tianxin on 2015年6月4日 下午8:38:28
	 */
	public final Method getLableGetter(Class<?> type){
		if(type == null){
			return objectGetters.getLabelGetter();
		}
		ResultSetGetters o = cache.get(type);
		if(o == null){
			return objectGetters.getLabelGetter();
		}
		return o.getLabelGetter();
	}
	
	/**
	 * 缓存映射关系
	 */
	private Map<Class<?>, ResultSetGetters> cache = Utils.newMap(32);
	private ResultSetGetters objectGetters = null;
}
