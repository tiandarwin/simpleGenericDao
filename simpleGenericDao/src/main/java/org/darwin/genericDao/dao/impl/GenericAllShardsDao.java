/**
 * org.darwin.genericDao.dao.impl.GenericAllShardsDao.java
 * created by Tianxin(tianjige@163.com) on 2015年6月15日 下午2:36:48
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.GenericDaoUtils;
import org.darwin.common.utils.Utils;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.dao.BaseAllShardsDao;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;
import org.darwin.genericDao.mapper.EntityMapper;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Orders;
import org.darwin.genericDao.query.Query;
import org.darwin.genericDao.query.QueryDistinctCount;
import org.darwin.genericDao.query.QuerySelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 通用的扫描全多个数据库切片的DAO
 * created by Tianxin on 2015年6月15日 下午2:36:48
 */
public class GenericAllShardsDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> implements BaseAllShardsDao<KEY, ENTITY> {
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(GenericAllShardsDao.class);

  /**
   * 构造函数，在这里对最主要的几个属性进行初始化
   */
  public GenericAllShardsDao() {
    //GenericDao的第二个泛型是entityClass
    Class<ENTITY> entityClass = GenericDaoUtils.getGenericEntityClass(this.getClass(), GenericAllShardsDao.class, 1);

    Table table = GenericDaoUtils.getTable(entityClass);
    Sequence sequence = GenericDaoUtils.getSequence(entityClass);

    this.entityClass = entityClass;
    this.configKeeper = new AnnotationConfigKeeper(table, sequence);
    this.columnMappers = GenericDaoUtils.generateColumnMappers(entityClass, table.columnStyle());
    this.allColumns = new ArrayList<String>(columnMappers.keySet());
  }

  /**
   * 获取表名
   * 
   * @return created by Tianxin on 2015年6月4日 下午2:29:52
   */
  protected String table() {
    return configKeeper.table();
  }

  private List<String> allColumns;
  private Class<ENTITY> entityClass;
  protected JdbcTemplate jdbcTemplate;
  private AnnotationConfigKeeper configKeeper;
  private Map<String, ColumnMapper> columnMappers;

  public ENTITY get(KEY id) {
    if (id == null) {
      return null;
    }
    List<ENTITY> entities = find(configKeeper.keyColumn(), id);
    if (entities == null || entities.size() == 0) {
      return null;
    }
    return entities.get(0);
  }

  public List<ENTITY> get(Collection<KEY> ids) {
    if (ids == null || ids.size() == 0) {
      return null;
    }
    return find(configKeeper.keyColumn(), ids);
  }

  /**
   * 获取所有的数据
   */
  public List<ENTITY> findAll() {
    return find(Matches.empty());
  }

  /**
   * 获取符合条件的第一条记录
   * 
   * @param column 字段名
   * @param value 字段取值
   * @return created by Tianxin on 2015年6月3日 下午8:34:36
   */
  protected ENTITY findOne(String column, Object value) {
    return findOne(Matches.one(column, value));
  }

  /**
   * 获取符合条件的数据
   * 
   * @param column 字段名
   * @param value 字段取值
   * @return created by Tianxin on 2015年6月3日 下午8:43:57
   */
  protected List<ENTITY> find(String column, Object value) {
    return find(Matches.one(column, value));
  }

  /**
   * 获取符合匹配条件的数据
   * 
   * @param matches 匹配条件，可为null
   * @return created by Tianxin on 2015年6月3日 下午8:44:48
   */
  protected List<ENTITY> find(Matches matches) {
    return find(matches, null);
  }

  /**
   * 获取符合匹配条件的数据，并按orders排序
   * 
   * @param matches 匹配条件，可为null
   * @param orders 排序规则，可以为null
   * @return created by Tianxin on 2015年6月3日 下午8:45:12
   */
  protected List<ENTITY> find(Matches matches, Orders orders) {
    return page(matches, orders, 0, 0);
  }

  /**
   * 获取符合匹配条件的第一条记录
   * 
   * @param matches 匹配条件，可为null
   * @return created by Tianxin on 2015年6月3日 下午8:46:04
   */
  protected ENTITY findOne(Matches matches) {
    return findOne(matches, null);
  }

  /**
   * 获取符合匹配条件的按orders排序后的第一调数据
   * 
   * @param matches 匹配条件，可为null
   * @param orders 排序规则，可为null
   * @return created by Tianxin on 2015年6月3日 下午8:46:29
   */
  protected ENTITY findOne(Matches matches, Orders orders) {
    List<ENTITY> entities = page(matches, orders, 0, 1);
    if (entities == null || entities.size() == 0) {
      return null;
    }
    return entities.get(0);
  }

  /**
   * 分页查询
   * 
   * @param matches 匹配条件，可为null
   * @param orders 匹配条件，可为null
   * @param offset 起始位置
   * @param rows 获取条数
   * @return created by Tianxin on 2015年6月3日 下午8:47:09
   */
  protected List<ENTITY> page(Matches matches, Orders orders, int offset, int rows) {
    return pageColumns(matches, orders, offset, rows, allColumns.toArray(new String[allColumns.size()]));
  }

  /**
   * 查询简单对象，只获取其中几列
   * 
   * @param matches 匹配条件，可为null
   * @param columns 字段名
   * @return created by Tianxin on 2015年6月3日 下午8:49:03
   */
  protected List<ENTITY> findSimple(Matches matches, String... columns) {
    return findSimple(matches, null, columns);
  }

  /**
   * 查询简单对象，只获取几列
   * 
   * @param matches 匹配条件，可为null
   * @param orders 排序规则，可为null
   * @param columns 字段集合
   * @return created by Tianxin on 2015年6月3日 下午8:49:49
   */
  protected List<ENTITY> findSimple(Matches matches, Orders orders, String... columns) {
    return pageColumns(matches, orders, 0, 0, columns);
  }

  /**
   * 分页查询简单对象
   * 
   * @param matches 匹配条件，可为null
   * @param orders 排序规则，可为null
   * @param offset 起始位置
   * @param rows 获取条数
   * @param columns 字段集合
   * @return created by Tianxin on 2015年6月3日 下午8:50:57
   */
  protected List<ENTITY> pageColumns(Matches matches, Orders orders, int offset, int rows, String... columns) {
    List<String> choozenColumns = Arrays.asList(columns);
    QuerySelect query = new QuerySelect(choozenColumns, matches, orders, configKeeper.table(), offset, rows);
    String sql = query.getSQL();
    Object[] args = query.getParams();
    LOG.info(Utils.toLogSQL(sql, args));
    List<String> columnList = GenericDaoUtils.getColumnsFromSQL(sql);
    List<ENTITY> entities = jdbcTemplate.query(sql, args, new EntityMapper<ENTITY>(columnList, columnMappers, entityClass));
    Collections.sort(entities, new GenericComparator<ENTITY>(orders, columnMappers));
    return entities;
  }

  /**
   * 查询某一列
   * 
   * @param rClass 结果类型
   * @param matches 匹配条件，可为null
   * @param column 获取哪一列
   * @return created by Tianxin on 2015年6月3日 下午8:47:59
   */
  protected <R extends Serializable> List<R> findOneColumn(Class<R> rClass, Matches matches, String column) {
    return pageOneColumn(rClass, matches, null, column, 0, 0);
  }

  /**
   * 分页查询某一列
   * 
   * @param rClass 结果类型
   * @param matches 匹配条件
   * @param column 字段名
   * @param offset 起始位置
   * @param rows 获取条数
   * @return created by Tianxin on 2015年6月3日 下午8:48:26
   */
  protected <R extends Serializable> List<R> pageOneColumn(Class<R> rClass, Matches matches, Orders orders, String column, int offset, int rows) {
    List<String> columns = Arrays.asList(column);
    QuerySelect query = new QuerySelect(columns, matches, orders, configKeeper.table(), offset, rows);
    String sql = query.getSQL();
    Object[] params = query.getParams();
    LOG.info(Utils.toLogSQL(sql, params));
    List<R> entities = jdbcTemplate.query(sql, params, BasicMappers.getMapper(rClass));
    Collections.sort(entities, new GenericComparator<R>(orders, columnMappers));
    return entities;

  }

  /**
   * 根据SQL查询结果
   * 
   * @param eClass 结果映射到的对象
   * @param sql 查询SQL
   * @param params 参数
   * @return created by Tianxin on 2015年6月3日 下午8:50:26
   */
  protected <E extends BaseObject<?>> List<E> findBySQL(Class<E> eClass, String sql, Object... params) {
    LOG.info(Utils.toLogSQL(sql, params));
    List<E> entities = jdbcTemplate.query(sql, params, BasicMappers.getEntityMapper(eClass, sql));
    Collections.sort(entities, new GenericComparator<E>(sql, columnMappers));
    return entities;
  }

  /**
   * 查询记录条数
   */
  public int countAll() {
    return count(Matches.empty());
  }

  /**
   * 查询符合条件的记录条数
   * 
   * @param column 字段名字
   * @param value 字段匹配值
   * @return created by Tianxin on 2015年6月3日 下午8:51:43
   */
  protected int count(String column, Object value) {
    return count(Matches.one(column, value));
  }

  /**
   * 查询符合条件的结果条数
   * 
   * @param matches 匹配条件，可为null
   * @return created by Tianxin on 2015年6月3日 下午8:52:01
   */
  protected int count(Matches matches) {
    List<String> columns = Arrays.asList("count(1)");
    QuerySelect query = new QuerySelect(columns, matches, null, configKeeper.table());
    String sql = query.getSQL();
    Object[] params = query.getParams();
    return countBySQL(sql, params);
  }

  /**
   * 查询符合条件的记录条数
   * 
   * @param column 字段名字
   * @param value 字段匹配值
   * @param targetColumns 要做distinct count的字段
   * @return created by Tianxin on 2015年6月3日 下午8:51:43
   */
  protected int countDistinct(String column, Object value, String... targetColumns) {
    return countDistinct(Matches.one(column, value), targetColumns);
  }

  /**
   * 查询符合条件的结果条数
   * 
   * @param matches 匹配条件，可为null
   * @param targetColumns 要做distinct count的字段，可以是"*"
   * @return created by Tianxin on 2015年6月3日 下午8:52:01
   */
  protected int countDistinct(Matches matches, String... targetColumns) {
    Query query = new QueryDistinctCount(configKeeper.table(), matches, targetColumns);

    String sql = query.getSQL();
    Object[] params = query.getParams();
    return countBySQL(sql, params);
  }

  /**
   * 暴露SQL接口对外
   * 
   * @param sql
   * @param params
   * @return
   */
  @SuppressWarnings("deprecation")
  protected int countBySQL(String sql, Object[] params) {
    LOG.info(Utils.toLogSQL(sql, params));
    return jdbcTemplate.queryForInt(sql, params);
  }

  public void setShardsJdbcTemplate(ScanShardsJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
}
