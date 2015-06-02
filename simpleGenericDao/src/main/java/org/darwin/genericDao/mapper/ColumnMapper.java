/**
 * org.darwin.genericDao.mapper.ColumnMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午7:50:10
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.Table.ColumnStyle;

/**
 * 数据表中某一列的映射规则
 * created by Tianxin on 2015年5月27日 下午7:50:10
 */
public class ColumnMapper {
	
	private ColumnMapper() {
	}

	/**
	 * 映射规则
	 * @param column
	 * @param getter
	 * @param setter
	 */
	public ColumnMapper(Method getter, Method setter, Column annotation, ColumnStyle columnStyle) {
		this();
		this.getter = getter;
		this.setter = setter;
		this.annotation = annotation;
		
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
	
	public boolean isModifiable(){
		return annotation == null || annotation.modifiable();
	}
}
