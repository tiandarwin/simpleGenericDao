/**
 * org.darwin.genericDao.query.Modifies.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:08:05
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * created by Tianxin on 2015年5月27日 下午12:08:05
 */
public class Modifies implements Operate {

  /**
   * 返回一个修改操作集对象 created by Tianxin on 2015年5月27日 下午2:44:50
   */
  public static Modifies init() {
    return new Modifies();
  }

  private static Set<String> toSet(String... sArray) {
    Set<String> cSet = new HashSet<String>(sArray.length * 4 / 3 + 1);
    for (String s : sArray) {
      cSet.add(s);
    }
    return cSet;
  }

  /**
   * 构造一个modifies列表，只更改columns中的字段
   * @param entity
   * @param columns
   * @return
   * <br/>created by Tianxin on 2015年12月9日 下午8:42:13
   */
  public static <ENTITY> Modifies includeColumns(ENTITY entity, String... columns) {
    Set<String> cSet = toSet(columns);
    List<ColumnMapper> mappers = BasicMappers.getMappers(entity.getClass());

    try {
      Modifies modifies = Modifies.init();
      for (ColumnMapper mapper : mappers) {
        String column = mapper.getColumn();
        if (!cSet.contains(column)) {
          continue;
        }
        Object value = mapper.getGetter().invoke(entity);
        modifies.modify(column, value);
      }
      return modifies;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 构造一个modifies列表，不更改columns中的字段
   * @param entity
   * @param columns
   * @return
   * <br/>created by Tianxin on 2015年12月9日 下午8:42:41
   */
  public static <ENTITY> Modifies excludeColumns(ENTITY entity, String... columns) {
    Set<String> cSet = toSet(columns);
    List<ColumnMapper> mappers = BasicMappers.getMappers(entity.getClass());

    try {
      Modifies modifies = Modifies.init();
      for (ColumnMapper mapper : mappers) {
        String column = mapper.getColumn();
        if (cSet.contains(column)) {
          continue;
        }
        Object value = mapper.getGetter().invoke(entity);
        modifies.modify(column, value);
      }
      return modifies;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 初始化一个修改动作对象
   * @param column
   * @param value
   * @return
   * created by Tianxin on 2015年6月15日 下午2:29:00
   */
  public static Modifies one(String column, Object value) {
    return new Modifies().modify(column, value);
  }

  private Modifies() {}

  /**
   * 向修改操作集中增加一个修改操作
   * 
   * @param column
   *            要修改的字段
   * @param value
   *            目标值
   * @return 对象自身 created by Tianxin on 2015年5月27日 下午2:47:55
   */
  public Modifies modify(String column, Object value) {
    modifies.add(new Modify(column, value));
    return this;
  }

  /**
   * 存储修改操作的一个集合
   */
  private List<Modify> modifies = new ArrayList<Modify>();

  public String getOperate() {
    StringBuilder sb = new StringBuilder(modifies.size() * 10);
    for (Modify modify : modifies) {
      sb.append(modify.getOperate()).append(',');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public List<Object> getParams() {
    List<Object> params = new ArrayList<Object>(modifies.size() * 2);
    for (Modify modify : modifies) {
      params.addAll(modify.getParams());
    }
    return params;
  }

  public boolean isEmpty() {
    return modifies.size() == 0;
  }

}
