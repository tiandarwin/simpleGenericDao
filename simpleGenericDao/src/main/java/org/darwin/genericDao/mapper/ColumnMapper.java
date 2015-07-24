/**
 * org.darwin.genericDao.mapper.ColumnMapper.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午7:50:10
 */
package org.darwin.genericDao.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.StatType;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.mapper.jdbc.FetcherCache;
import org.darwin.genericDao.mapper.jdbc.TypeFetcher;

/**
 * 数据表中某一列的映射规则
 * created by Tianxin on 2015年5月27日 下午7:50:10
 */
public class ColumnMapper {

  //私有化构造函数
  private ColumnMapper() {}

  /**
   * 映射规则
   * @param sqlColumn
   * @param getter
   * @param setter
   */
  public ColumnMapper(Method getter, Method setter, Class<?> fieldType, Column annotation, ColumnStyle columnStyle, StatType type, String keyColumn) {
    this();
    this.type = type;
    this.getter = getter;
    this.setter = setter;
    this.fetcher = FetcherCache.getFetcher(fieldType);
    this.fieldType = getter.getReturnType();

    this.annotation = annotation;
    if (keyColumn != null) {
      this.sqlColumn = keyColumn;
    } else if (annotation != null) {
      this.sqlColumn = annotation.value();
    } else {
      String fieldName = generateFieldName(setter.getName());
      columnStyle = columnStyle == null ? ColumnStyle.JAVA_TO_MYSQL : columnStyle;
      this.sqlColumn = columnStyle.convert(fieldName);
    }
  }

  /**
   * 映射规则
   * @param sqlColumn
   * @param getter
   * @param setter
   */
  public ColumnMapper(Method getter, Method setter, Class<?> fieldType, Column annotation, ColumnStyle columnStyle, StatType type) {
    this(getter, setter, fieldType, annotation, columnStyle, type, null);
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
    sb.setCharAt(0, (char) (sb.charAt(0) + 32));
    return sb.toString();
  }

  /**
   * 实体中反射的信息
   */
  private Method getter;
  private Method setter;
  private TypeFetcher fetcher;
  private Class<?> fieldType;

  /**
   * 数据库中的字段信息
   */
  private Column annotation;
  private String sqlColumn;
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

  /**
   * 将rs中此列的值，加载到目标对象中去
   * @param rs
   * @param target
   * created by Tianxin on 2015年6月5日 下午1:44:41
   * @throws SQLException 
   * @throws InvocationTargetException 
   * @throws IllegalArgumentException 
   * @throws IllegalAccessException 
   */
  public void loadColumn2Field(ResultSet rs, Object target) throws SQLException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    try {
      Object value = fetcher.getFromResultSet(rs, getColumn());
      if (value != null || !fieldType.isPrimitive()) {
        setter.invoke(target, value);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  public boolean isModifiable() {
    return annotation == null || annotation.modifiable();
  }
}
