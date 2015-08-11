/**
 * org.darwin.genericDao.annotations.stat.StatType.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:05:23
 */
package org.darwin.genericDao.annotations.enums;

/**
 * 默认的几种统计类型
 * created by Tianxin on 2015年6月3日 下午1:55:45
 */
public enum Type {
  SUM("sum"), AVG("avg"), KEY(null), DATE(null), EXTEND(null), MAX("max"), MIN("min");

  private String value = null;

  private Type(String value) {
    this.value = value;
  }

  public String value() {
    return this.value;
  }

}
