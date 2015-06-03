/**
 * org.darwin.genericDao.mapper.stat.StatColumnMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午2:46:33
 */
package org.darwin.genericDao.mapper.stat;

import java.lang.reflect.Method;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.annotations.stat.StatType;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * created by Tianxin on 2015年6月3日 下午2:46:33
 */
public class StatColumnMapper extends ColumnMapper{


	//无参构造函数私有化
	private StatColumnMapper() {
		super();
	}

	/**
	 * 映射规则
	 * @param column
	 * @param getter
	 * @param setter
	 */
	public StatColumnMapper(Method getter, Method setter, Column annotation, ColumnStyle columnStyle, StatType type) {
		this();
		this.getter = getter;
		this.setter = setter;
		this.annotation = annotation;
		this.type = type;
		
		if(annotation != null){
			this.column = annotation.value();
		}else{
			String fieldName = generateFieldName(setter.getName());
			columnStyle = columnStyle == null ? ColumnStyle.JAVA_TO_MYSQL : columnStyle;
			this.column = columnStyle.convert(fieldName);
		}
	}


	/**
	 * 推导field名字
	 * @param name
	 * @return
	 * created by Tianxin on 2015年6月1日 下午12:08:30
	 */
	private String generateFieldName(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(name.substring(3));
		sb.setCharAt(0, (char)(sb.charAt(0) + 32));
		return sb.toString();
	}

	/**
	 * 这个字段映射里锁对应的annotation
	 */
	private Column annotation;
	private String column;
	private Method getter;
	private Method setter;
	private StatType type;
	
	public String getColumn() {
		return column;
	}
	public Method getGetter() {
		return getter;
	}
	public Method getSetter() {
		return setter;
	}
	public Column getAnnotation() {
		return annotation;
	}
	public StatType getType() {
		return type;
	}

}
