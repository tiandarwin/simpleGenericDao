/**
 * org.darwin.genericDao.operate.Groups.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:28:48
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * created by Tianxin on 2015年6月3日 下午3:28:48
 */
public class Groups implements Operate {

  private Groups() {}

  public String getOperate() {
    StringBuilder sb = new StringBuilder(groups.size() * 10);
    for (String group : groups) {
      sb.append(group).append(',');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public List<Object> getParams() {
    return Collections.emptyList();
  }

  public boolean isEmpty() {
    return groups == null || groups.size() == 0;
  }

  /**
   * 初始化一个GroupBy的字段集
   * @return
   * created by Tianxin on 2015年6月3日 下午3:37:16
   */
  public static Groups init() {
    return new Groups();
  }

  /**
   * 生产一个GroupBy的字段集
   * @param column
   * @return
   * created by Tianxin on 2015年6月3日 下午3:37:00
   */
  public static Groups one(String column) {
    return init().groupBy(column);
  }

  /**
   * 多个groupBy的方法
   * @param columns
   * @return
   * <br/>created by Tianxin on 2015年7月3日 下午1:37:02
   */
  public static Groups some(String... columns) {
    Groups groups = init();
    for (String column : columns) {
      groups.groupBy(column);
    }
    return groups;
  }

  /**
   * 多个groupBy的方法
   * @param columns
   * @return
   * <br/>created by Tianxin on 2015年7月3日 下午1:37:02
   */
  public static Groups some(Collection<String> columns) {
    Groups groups = init();
    for (String column : columns) {
      groups.groupBy(column);
    }
    return groups;
  }

  /**
   * 添加groupBy的字段
   * @param column
   *            created by Tianxin on 2015年6月3日 下午3:35:42
   */
  public Groups groupBy(String column) {
    groups.add(column);
    return this;
  }

  /**
   * 获取groupby的字段列表
   * @return
   * created by Tianxin on 2015年6月7日 下午2:17:51
   */
  public List<String> getGroupByColumns() {
    return groups;
  }

  private List<String> groups = new ArrayList<String>();
}
