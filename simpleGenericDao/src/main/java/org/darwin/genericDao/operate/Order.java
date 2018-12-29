/**
 * org.darwin.genericDao.query.Order.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:08:14
 */
package org.darwin.genericDao.operate;

import java.util.Collections;
import java.util.List;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.dao.ColumnNameConverter;

/**
 * created by Tianxin on 2015年5月27日 下午12:08:14
 */
public class Order implements Operate {

  /**
   * 返回一个以column的正向排序
   * created by Tianxin on 2015年5月27日 下午2:41:24
   */
  public static Order asc(String column) {
    return new Order(column, true);
  }

  /**
   * 返回一个以column的反向排序
   * created by Tianxin on 2015年5月27日 下午2:41:50
   */
  public static Order desc(String column) {
    return new Order(column, false);
  }

  private Order() {}

  private Order(String column, boolean asc) {
    this.column = column;
    this.asc = asc;
  }

  private String column;
  private boolean asc;

  public String getOperate(ColumnNameConverter columnNameConverter) {
    String realColumn = columnNameConverter.convert(column);
    return asc ? realColumn : Utils.connect(realColumn, " desc");
  }

  public List<Object> getParams() {
    return Collections.emptyList();
  }

  public boolean isEmpty() {
    return false;
  }

}
