/**
 * org.darwin.genericDao.mapper.ResultSetGetters.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午8:34:26
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;

/**
 * 从java.sql.ResultSet中获取某个字段值的方法
 * created by Tianxin on 2015年6月4日 下午8:34:26
 */
public class ResultSetGetters {
	
	private ResultSetGetters() {
	}

	/**
	 * 构造一个ResultSetters的解析类
	 * @param type
	 * @param indexGetter
	 * @param labelGetter
	 */
	ResultSetGetters(Class<?> type, Method indexGetter, Method labelGetter) {
		this();
		this.type = type;
		this.indexGetter = indexGetter;
		this.labelGetter = labelGetter;
	}

	/**
	 * 返回值的类型
	 */
	private Class<?> type;
	
	/**
	 * 以字段index获取字段值的方法，如getString(1)
	 */
	private Method indexGetter;
	
	/**
	 * 以sql中的label获取字段值的方法，如getString(column)
	 */
	private Method labelGetter;

	public Class<?> getType() {
		return type;
	}

	public Method getIndexGetter() {
		return indexGetter;
	}

	public Method getLabelGetter() {
		return labelGetter;
	}
	
	
	
}
