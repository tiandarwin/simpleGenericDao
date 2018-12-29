/**
 * org.darwin.genericDao.query.Query.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:46:17
 */
package org.darwin.genericDao.query;

import java.util.Collection;
import java.util.List;

import org.darwin.genericDao.dao.ColumnNameConverter;
import org.darwin.genericDao.operate.HintCtx;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Orders;

/**
 * 一个select类型的Query描述
 * created by Tianxin on 2015年5月27日 下午1:46:17
 */
public class QuerySelect implements Query {

  private QuerySelect() {}

  /**
   * 构造一个Select的Query对象
   * @param columns
   * @param matches
   * @param orders
   * @param table
   */
  public QuerySelect(Collection<String> columns, Matches matches, Orders orders, String table) {
    this();
    this.columns = columns;
    this.matches = matches;
    this.orders = orders;
    this.table = table;
  }

  /**
   * 构造一个Select的Query对象
   * @param columns
   * @param matches
   * @param orders
   * @param table
   * @param offset
   * @param rows
   */
  public QuerySelect(Collection<String> columns, Matches matches, Orders orders, String table, int offset, int rows) {
    this(columns, matches, orders, table);
    this.offset = offset;
    this.rows = rows;
  }

  private Collection<String> columns;
  private Matches matches;
  private Orders orders;
  private String table;
  private int offset = 0;
  private int rows = 0;

  public String getSQL(ColumnNameConverter columnNameConverter) {
    StringBuilder sb = new StringBuilder(256);
    sb.append("select ").append(buildColumns()).append(" from ").append(table);
    
    //如果有强制索引的设定
    String forceIndex = HintCtx.getForceIndex();
    if(forceIndex != null){
      sb.append(" force index(").append(forceIndex).append(')');
    }
    
    if (matches != null && !matches.isEmpty()) {
      sb.append(" where ").append(matches.getOperate(columnNameConverter));
    }
    if (orders != null && !orders.isEmpty()) {
      sb.append(" order by ").append(orders.getOperate(columnNameConverter));
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

  public Collection<String> getColumns() {
    return columns;
  }

  private String buildColumns() {
    StringBuilder sb = new StringBuilder(columns.size() * 8);
    for (String column : columns) {
      column = cleanStatOperator(column);
      sb.append(column).append(',');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * 清理列名中的统计操作符
   * @param column
   * @return
   * created by Tianxin on 2015年6月17日 下午4:06:12
   */
  private static String cleanStatOperator(String column) {

    //如果没有空格
    int index = column.lastIndexOf(' ');
    if (index == -1) {
      return column;
    }

    column = column.replaceAll("\\s+\\(", "(");
    StringBuilder sb = new StringBuilder(column.length());
    StringBuilder tmp = new StringBuilder(3);
    for (int i = 0; i < column.length(); i++) {
      char c = column.charAt(i);
      char smallChar = c;
      sb.append(c);

      //字符转小写
      if (c >= 65 && c <= 90) {
        smallChar = (char) (c + 32);
      }

      if (smallChar >= 97 && smallChar <= 122) {
        tmp.append(smallChar);
        if (tmp.length() == 3 && isStatOperator(tmp.toString())) {
          //如果下一个字符是（,则sb前向删除
          boolean flag = leftBracketNext(column, i);
          if (flag) {
            sb.delete(sb.length() - 3, sb.length());
          }
          tmp.delete(0, tmp.length());
        }
      } else {
        tmp.delete(0, tmp.length());
      }
    }
    return sb.toString();
  }

  /**
   * @param column
   * @param i
   * @return
   * created by Tianxin on 2015年6月17日 下午5:05:19
   */
  private static boolean leftBracketNext(String column, int i) {
    if (i >= column.length() - 1) {
      return false;
    }
    char c = column.charAt(i + 1);
    return c == '(';
  }

  private static boolean isStatOperator(String s) {
    String[] sArray = new String[] {"min", "max", "sum", "avg"};
    for (String o : sArray) {
      if (o.equals(s)) {
        return true;
      }
    }
    return false;
  }

}
