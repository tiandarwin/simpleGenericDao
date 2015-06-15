/**
 * org.darwin.genericDao.query.QueryStat.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:27:00
 */
package org.darwin.genericDao.query;

import java.util.List;

import org.darwin.genericDao.operate.Groups;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Orders;

/**
 * created by Tianxin on 2015年6月3日 下午3:27:00
 */
public class QueryStat implements Query {

  private String columns;
  private String table;
  private Matches matches;
  private Orders orders;
  private Groups groups;
  private int offset;
  private int rows;

  private QueryStat() {}

  private QueryStat(Matches matches, Orders orders, Groups groups, String table) {
    this();
    this.matches = matches;
    this.orders = orders;
    this.groups = groups;
    this.table = table;
  }

  public QueryStat(List<String> sumColumns, List<String> avgColumns, List<String> extendColumns, List<String> keyColumns, Matches matches,
      Orders orders, Groups groups, String table, int offset, int rows) {
    this(sumColumns, avgColumns, extendColumns, keyColumns, matches, orders, groups, table);
    this.offset = offset;
    this.rows = rows;
  }

  public QueryStat(List<String> sumColumns, List<String> avgColumns, List<String> extendColumns, List<String> keyColumns, Matches matches,
      Orders orders, Groups groups, String table) {
    this(matches, orders, groups, table);
    this.columns = buildColumns(sumColumns, avgColumns, extendColumns, keyColumns);
  }

  /**
   * 构建SQL中的字段集
   * 
   * @param sumColumns
   * @param avgColumns
   * @param keyColumns
   *            created by Tianxin on 2015年6月3日 下午3:45:43
   */
  private String buildColumns(List<String> sumColumns, List<String> avgColumns, List<String> extendColumns, List<String> keyColumns) {
    StringBuilder sb = new StringBuilder(512);
    for (String sumColumn : sumColumns) {
      sb.append("sum(").append(sumColumn).append(") as ").append(sumColumn).append(',');
    }
    for (String avgColumn : avgColumns) {
      sb.append("avg(").append(avgColumn).append(") as ").append(avgColumn).append(',');
    }
    for (String extColumn : extendColumns) {
      sb.append(extColumn).append(',');
    }
    for (String keyColumn : keyColumns) {
      sb.append(keyColumn).append(',');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public String getSQL() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("select ").append(columns).append(" from ").append(table);
    if (matches != null && !matches.isEmpty()) {
      sb.append(" where ").append(matches.getOperate());
    }
    if (groups != null && !groups.isEmpty()) {
      sb.append(" group by ").append(groups.getOperate());
    }
    if (orders != null && !orders.isEmpty()) {
      sb.append(" order by ").append(orders.getOperate());
    }
    if (rows != 0) {
      sb.append(" limit ").append(offset).append(',').append(rows);
    }
    return sb.toString();
  }

  public Object[] getParams() {
    if (matches == null) {
      return new Object[0];
    } else {
      List<Object> params = matches.getParams();
      return params.toArray();
    }
  }

}
