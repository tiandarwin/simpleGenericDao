/**
 * org.darwin.genericDao.param.IncreaseParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:07:32
 */
package org.darwin.genericDao.param;

import java.util.Arrays;
import java.util.List;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年5月27日 下午2:07:32
 */
public class IncreaseParam implements Param {

  private Object value;

  /**
   * @param value
   */
  public IncreaseParam(Number value) {
    this.value = value;
  }

  public List<Object> getParams() {
    return Arrays.asList(value);
  }

  public String buildOperate(String column) {
    return Utils.connect(column, " = ", column, "+?");
  }

}
