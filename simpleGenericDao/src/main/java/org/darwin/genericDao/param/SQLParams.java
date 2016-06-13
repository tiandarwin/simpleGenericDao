/**
 * org.darwin.genericDao.param.SQLParams.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:08:51
 */
package org.darwin.genericDao.param;

/**
 * created by Tianxin on 2015年5月27日 下午12:08:51
 */
public class SQLParams {

  public static Param like(String s) {
    return new LikeParam(s);
  }

  public static Param like(String s, boolean left, boolean right) {
    return new LikeParam(s, left, right);
  }

  public static Param lt(Object top) {
    return new LtParam(top, false);
  }

  public static Param lte(Object top) {
    return new LtParam(top, true);
  }

  public static Param gt(Object floor) {
    return new GtParam(floor, false);
  }

  public static Param gte(Object floor) {
    return new GtParam(floor, true);
  }

  public static Param between(Object start, Object end) {
    return new BetweenParam(start, end);
  }

  public static Param normal(Object value) {
    return new NormalParam(value);
  }

  public static Param increase(Number value) {
    return new IncreaseParam(value);
  }

  public static Param not(Object value) {
    return new NotParam(value);
  }

  public static Param express(String expression, Object... params) {
    return new ExpressParam(expression, params);
  }
}
