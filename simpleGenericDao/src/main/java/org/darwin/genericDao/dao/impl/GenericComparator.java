/**
 * org.darwin.genericDao.dao.impl.GenericComparator.java
 * created by Tianxin(tianjige@163.com) on 2015年6月15日 下午3:24:13
 */
package org.darwin.genericDao.dao.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.mapper.ColumnMapper;
import org.darwin.genericDao.operate.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 比较两个对象的顺序
 * created by Tianxin on 2015年6月15日 下午3:24:13
 */
public class GenericComparator<T> implements Comparator<T> {
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(GenericComparator.class);

  @SuppressWarnings("unchecked")
  public int compare(T o1, T o2) {

    //如果无法排序，则保持原来的顺序
    if (Utils.isEmpty(comparedGetters) || o1 == null || o2 == null) {
      return 1;
    } else {
      for (OrderByMethod m : comparedGetters) {
        try {
          Comparable<T> r1 = (Comparable<T>) (m.getter.invoke(o1));
          Comparable<T> r2 = (Comparable<T>) (m.getter.invoke(o2));
          if (r1.equals(r2)) {
            continue;
          } else if (r1.compareTo(o2) > 0) {
            return m.asc ? 1 : -1;
          } else {
            return m.asc ? -1 : 1;
          }
        } catch (Exception e) {
          throw new RuntimeException("compare error");
        }
      }
    }
    return 0;
  }

  private List<OrderByMethod> comparedGetters = new ArrayList<GenericComparator<T>.OrderByMethod>();

  /**
   * 构造函数
   */
  private GenericComparator() {}

  public GenericComparator(Orders orders, Map<String, ColumnMapper> columnMappers) {
    this();
    if (orders != null && !orders.isEmpty()) {
      initOrders(orders.getOperate(), columnMappers);
    }
  }

  public GenericComparator(String sql, Map<String, ColumnMapper> columnMappers) {
    this();
    String orders = getOrdersFromSQL(sql);
    initOrders(orders, columnMappers);
  }

  /**
   * @param orders
   * @param columnMappers
   * created by Tianxin on 2015年6月15日 下午3:47:06
   */
  private void initOrders(String orders, Map<String, ColumnMapper> columnMappers) {
    if (Utils.isEmpty(orders)) {
      return;
    }
    for (String sOrder : orders.split(",")) {
      sOrder = sOrder.trim().toLowerCase();
      int first = sOrder.indexOf(' ');
      if (first < 0) {
        String column = sOrder;
        addComparedGetter(column, true, columnMappers);
      } else {
        String column = sOrder.substring(0, first);
        int last = sOrder.lastIndexOf(' ');
        String asc = sOrder.substring(last + 1);
        addComparedGetter(column, asc.equals("asc"), columnMappers);
      }
    }
  }

  /**
   * 将排序字段加入到结果集中
   * @param column
   * @param asc
   * created by Tianxin on 2015年6月15日 下午4:04:37
   */
  private void addComparedGetter(String column, boolean asc, Map<String, ColumnMapper> columnMappers) {
    ColumnMapper mapper = columnMappers.get(column);
    if (mapper != null) {
      Method getter = mapper.getGetter();
      comparedGetters.add(new OrderByMethod(getter, asc));
    }
  }

  /**
   * 获取order by那一截字符串
   * @param sql
   * @return
   * created by Tianxin on 2015年6月15日 下午3:46:34
   */
  private static String getOrdersFromSQL(String sql) {

    //SQL格式化
    sql = sql.toLowerCase();
    sql = sql.replaceAll("\\s+", " ");
    sql = sql.replace("\\s+,\\s+", ",");

    //看是否有order by
    String tag = " order by ";
    int index = sql.lastIndexOf(tag);
    if (index == -1) {
      return null;
    }

    //看是否是括号中的order by
    index = index + tag.length();
    int rightBracket = sql.indexOf(")", index);
    if (rightBracket == -1) {
      int end = sql.indexOf(' ', index);
      return end == -1 ? sql.substring(index) : sql.substring(index, end);
    }

    //判断order by后面是否有左括号，如果右边有一整套括号，则肯定不是外层排序
    int leftBracket = sql.indexOf("(", index);
    if (leftBracket != -1) {
      return null;
    }

    //如果不以括号开头，则也说明不是最外层的order by
    boolean startWithBracket = sql.indexOf('(') == 0;
    if (!startWithBracket) {
      return null;
    }

    boolean flag = isMatchedWithStart(sql);
    return flag ? sql.substring(index, rightBracket) : null;
  }

  /**
   * @param sql
   * @return
   * created by Tianxin on 2015年6月15日 下午5:29:24
   */
  private static boolean isMatchedWithStart(String sql) {
    int count = 1;
    for (int i = 1; i < sql.length(); i++) {
      char c = sql.charAt(i);
      if (c == '(') {
        count += 1;
      } else {
        count -= 1;
      }

      if (count == 0 && i < sql.length() - 1) {
        return false;
      }
    }
    return true;
  }

  private class OrderByMethod {
    /**
     * 构造函数
     * @param getter
     * @param asc
     */
    private OrderByMethod(Method getter, boolean asc) {
      super();
      this.getter = getter;
      this.asc = asc;
    }

    private Method getter;
    private boolean asc;
  }
}
