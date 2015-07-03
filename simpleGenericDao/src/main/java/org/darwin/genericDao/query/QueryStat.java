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

  private List<String> columns;
  private String table;
  private Matches matches;
  private Orders orders;
  private Groups groups;
  private int offset;
  private int rows;

  private QueryStat() {}

  public QueryStat(List<String> columns, Matches matches,
      Orders orders, Groups groups, String table) {
    this();
    this.matches = matches;
    this.orders = orders;
    this.groups = groups;
    this.table = table;
    this.columns = columns;
  }
  
  public QueryStat(List<String> columns, Matches matches,
      Orders orders, Groups groups, String table, int offset, int rows) {
    this(columns, matches, orders, groups, table);
    this.offset = offset;
    this.rows = rows;
  }

  public String getSQL() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("select ");
    for(String column : columns){
      sb.append(column).append(',');
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(" from ").append(table);
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
