package org.darwin.genericDao.dao;

/**
 * 拼接sql时用于转换传入的字段名称为真实数据库字段名称，再从1.0.0版本后支持使用Pojo field name作为查询字段名称。
 *
 * see {@link org.darwin.genericDao.annotations.UseQueryColumnFormat}
 *
 * @author hexiufeng
 * @date 2018/12/28下午6:23
 */
public interface ColumnNameConverter {

  /**
   * 转换字段名称为真实的数据库字段名称
   *
   * @param name
   * @return
   */
  String convert(String name);
}
