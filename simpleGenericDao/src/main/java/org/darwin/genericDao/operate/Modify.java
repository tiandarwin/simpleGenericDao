/**
 * org.darwin.genericDao.query.Modify.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:07:53
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.List;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.dao.ColumnNameConverter;
import org.darwin.genericDao.param.Param;
import org.darwin.genericDao.param.SQLParams;

/**
 * created by Tianxin on 2015年5月27日 下午12:07:53
 */
public class Modify implements Operate {

  /**
   * 构造一个Modify对象
   * @param column
   * @param value
   */
  Modify(String column, Object value) {
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
    return Utils.connect(columnNameConverter.convert(column), "=?");
  }

  public List<Object> getParams() {
    List<Object> params = param.getParams();

    //如果返回是一个空集合，则证明是要设置为null
    if (params.size() == 0) {
      params = new ArrayList<Object>(0);
      params.add(null);
    }
    return params;
  }

  public boolean isEmpty() {
    return false;
  }
}
