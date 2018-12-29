/**
 * org.darwin.genericDao.query.Match.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:23:41
 */
package org.darwin.genericDao.operate;

import java.util.List;

import org.darwin.genericDao.dao.ColumnNameConverter;
import org.darwin.genericDao.param.Param;
import org.darwin.genericDao.param.SQLParams;

/**
 * 该对象描述了一个条件匹配
 * created by Tianxin on 2015年5月26日 下午9:23:41
 */
public class Match implements Operate {

  /**
   * 构造一个Match对象
   * @param column
   * @param value
   */
  Match(String column, Object value) {
    this.column = column;
    if (value instanceof Param) {
      this.param = (Param) value;
    } else {
      this.param = SQLParams.normal(value);
    }
  }

  private String column;
  private Param param;

  public String getOperate(ColumnNameConverter columnNameConverter) {
    return param.buildOperate(columnNameConverter.convert(column));
  }

  public List<Object> getParams() {
    return param.getParams();
  }

  public boolean isEmpty() {
    return false;
  }
}
