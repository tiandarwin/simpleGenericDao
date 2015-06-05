/**
 * org.darwin.genericDao.mapper.ColumnMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午7:50:10
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.Method;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.annotations.stat.StatType;

/**
 * 数据表中某一列的映射规则
 * created by Tianxin on 2015年5月27日 下午7:50:10
 */
public class ColumnMapper {
	
	//私有化构造函数
	private ColumnMapper() {
	}

	/**
	 * 映射规则
	 * @param sqlColumn
	 * @param getter
	 * @param setter
	 */
	public ColumnMapper(Method getter, Method setter, Column annotation, ColumnStyle columnStyle) {
		this(getter, setter, annotation, columnStyle, null);
	}
	
	/**
	 * 映射规则
	 * @param sqlColumn
	 * @param getter
	 * @param setter
	 */
	public ColumnMapper(Method getter, Method setter, Column annotation, ColumnStyle columnStyle, StatType type) {
		this();
		this.type = type;
		this.getter = getter;
		this.setter = setter;
		this.annotation = annotation;
		
		if(annotation != null){
			this.sqlColumn = annotation.value();
		}else{
			String fieldName = generateFieldName(setter.getName());
			columnStyle = columnStyle == null ? ColumnStyle.JAVA_TO_MYSQL : columnStyle;
			this.sqlColumn = columnStyle.convert(fieldName);
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
	private String sqlColumn;
	private Method getter;
	private Method setter;
	private StatType type;
	
	/**
	 * 一般情况与getColumn的结果相同，当对应的是statdao中的扩展字段时，这里返回的是拼到SQL里面的字段
	 * @return
	 * created by Tianxin on 2015年6月4日 下午7:49:24
	 */
	public String getSQLColumn() {
		return sqlColumn;
	}
	
	/**
	 * 以最后一个空格往后的作为列名
	 * @return
	 * created by Tianxin on 2015年6月4日 下午7:51:08
	 */
	public String getColumn() {
		int index = sqlColumn.lastIndexOf(' ');
		return index == -1 ? sqlColumn : sqlColumn.substring(index + 1);
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

	public boolean isModifiable(){
		return annotation == null || annotation.modifiable();
	}
}
