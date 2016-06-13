/**
 * org.darwin.genericDao.query.QueryStat.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:27:00
 */
package org.darwin.genericDao.query;

import java.util.ArrayList;
import java.util.List;

import org.darwin.common.utils.Utils;
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

  /**
   * 构造函数
   * @param columns 处理好的列
   * @param matches
   * @param groups
   * @param orders
   * @param table
   */
  public QueryStat(List<String> columns, Matches matches, Groups groups, Orders orders, String table) {
    this();
    this.matches = matches;
    this.orders = orders;
    this.groups = groups;
    this.table = table;
    this.columns = columns;
  }

  /**
   * 
   * 构造函数
   * @param toSumColumns  要做sum的列
   * @param toAvgColumns  要做avg的列
   * @param otherColumns
   * @param matches
   * @param groups
   * @param orders
   * @param table
   */
  public QueryStat(List<String> toSumColumns, List<String> toAvgColumns, List<String> otherColumns, Matches matches, Groups groups, Orders orders,
      String table) {
    this(toSumColumns, toAvgColumns, otherColumns, matches, groups, orders, table, 0, 0);
  }

  /**
   * 构造函数
   * @param toSumColumns
   * @param toAvgColumns
   * @param otherColumns
   * @param matches
   * @param groups
   * @param orders
   * @param table
   * @param offset
   * @param rows
   */
  public QueryStat(List<String> toSumColumns, List<String> toAvgColumns, List<String> otherColumns, Matches matches, Groups groups, Orders orders,
      String table, int offset, int rows) {
    this(null, matches, groups, orders, table);

    this.columns = new ArrayList<String>(5);
    this.offset = offset;
    this.rows = rows;

    //添加统计列
    if (!Utils.isEmpty(otherColumns)) {
      columns.addAll(otherColumns);
    }
    //添加统计列
    if (!Utils.isEmpty(toSumColumns)) {
      for (String column : toSumColumns) {
        columns.add(Utils.connect("sum(", column, ") as ", column));
      }
    }
    if (!Utils.isEmpty(toAvgColumns)) {
      for (String column : toAvgColumns) {
        columns.add(Utils.connect("avg(", column, ") as ", column));
      }
    }
  }

  /**
   * 构造函数
   * @param columns 处理好的列
   * @param matches
   * @param groups
   * @param orders
   * @param table
   * @param offset
   * @param rows
   */
  public QueryStat(List<String> columns, Matches matches, Groups groups, Orders orders, String table, int offset, int rows) {
    this(columns, matches, groups, orders, table);
    this.offset = offset;
    this.rows = rows;
  }

  public String getSQL() {
    StringBuilder sb = new StringBuilder(256);
    sb.append("select ");
    for (String column : columns) {
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

  public Matches getMatches() {
    return matches;
  }

  /**
   * 看是否需要检查数量
   * @return
   * <br/>created by Tianxin on 2015年9月24日 上午11:23:43
   */
  public boolean need2CheckCount() {

    //有group by时候不需要check总的符合条件数量
    boolean hasGroups = groups != null && !groups.isEmpty();
    if (hasGroups) {
      return false;
    }

    //没有group by时，并且字段中有sum，min，max，avg等
    String[] operates = new String[] {"min(", "max(", "sum(", "avg("};
    for (String column : columns) {
      column = column.toLowerCase();
      for (String operate : operates) {
        if (column.startsWith(operate)) {
          return true;
        }
      }
    }
    return false;
  }
}
