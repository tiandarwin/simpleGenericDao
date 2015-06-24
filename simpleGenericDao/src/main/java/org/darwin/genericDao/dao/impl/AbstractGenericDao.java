/**
 * org.darwin.genericDao.dao.impl.AbstractGenericDao.java
 * created by Tianxin(tianjige@163.com) on 2015年6月24日 下午5:33:04
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.GenericDaoUtils;
import org.darwin.common.utils.Utils;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;
import org.darwin.genericDao.mapper.EntityMapper;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Modifies;
import org.darwin.genericDao.operate.Orders;
import org.darwin.genericDao.query.Query;
import org.darwin.genericDao.query.QueryDelete;
import org.darwin.genericDao.query.QueryDistinctCount;
import org.darwin.genericDao.query.QueryModify;
import org.darwin.genericDao.query.QuerySelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 虚拟的通用dao
 * <br/>created by Tianxin on 2015年6月24日 下午5:33:04
 */
public class AbstractGenericDao<ENTITY> {
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(AbstractGenericDao.class);
  
  /**
   * 构造函数
   */
  public AbstractGenericDao() {
    
    Class<ENTITY> entityClass = GenericDaoUtils.getGenericEntityClass(this.getClass(), AbstractGenericDao.class, 0);
    
    Table table = GenericDaoUtils.getTable(entityClass);
    Sequence sequence = GenericDaoUtils.getSequence(entityClass);

    this.entityClass = entityClass;
    this.configKeeper = new TableConfigKeeper(table, sequence);
    this.columnMappers = GenericDaoUtils.generateColumnMappers(entityClass, table.columnStyle());
    this.writeHandler = new WriteSQLHandler<ENTITY>(columnMappers, configKeeper);
  }
  
  protected JdbcTemplate jdbcTemplate;
  protected Class<ENTITY> entityClass;
  protected TableConfigKeeper configKeeper;
  protected WriteSQLHandler<ENTITY> writeHandler;
  protected Map<String, ColumnMapper> columnMappers;
  
  /**
   * 获取表名
   * 
   * @return created by Tianxin on 2015年6月4日 下午2:29:52
   */
  protected String table() {
    return configKeeper.table();
  }
  
  /**
   * 新建一个对象
   * @param entity
   * @return
   * <br/>created by Tianxin on 2015年6月24日 下午5:49:32
   */
  public boolean create(ENTITY entity) {
    return create(Utils.one2List(entity)) >= 1;
  }

  /**
   * 新建一批对象
   * @param entities
   * @return
   * <br/>created by Tianxin on 2015年6月24日 下午5:49:40
   */
  public int create(Collection<ENTITY> entities) {
    String sql = writeHandler.generateInsertSQL(entities);
    Object[] args = writeHandler.generateInsertParams(entities);
    LOG.info(Utils.toLogSQL(sql, args));
    return jdbcTemplate.update(sql, args);
  }
  
  /**
   * 如果column的取值match到了value则进行删除
   * 
   * @param column 字段名
   * @param value 匹配值
   * @return 删除条数 created by Tianxin on 2015年6月3日 下午8:33:29
   */
  protected int delete(String column, Object value) {
    return delete(Matches.one(column, value));
  }

  /**
   * 按照匹配条件删除数据
   * 
   * @param matches 匹配条件，可为null
   * @return 删除条数 created by Tianxin on 2015年6月3日 下午8:34:03
   */
  protected int delete(Matches matches) {
    QueryDelete query = new QueryDelete(matches, writeHandler.table());
    String sql = query.getSQL();
    Object[] params = query.getParams();
    LOG.info(Utils.toLogSQL(sql, params));
    return jdbcTemplate.update(sql, params);
  }

  /**
   * 按照匹配条件进行更新
   * 
   * @param modifies 数据修改定义
   * @param matches 匹配条件
   * @return
   */
  protected int update(Modifies modifies, Matches matches) {
    QueryModify modify = new QueryModify(modifies, matches, configKeeper.table());
    String sql = modify.getSQL();
    Object[] args = modify.getParams();
    LOG.info(Utils.toLogSQL(sql, args));
    return jdbcTemplate.update(sql, args);
  }

  /**
   * 获取所有的数据
   * @return
   * <br/>created by Tianxin on 2015年6月24日 下午5:50:21
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
    List<String> allColumns = writeHandler.allColumns();
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
    Object[] params = query.getParams();
    LOG.info(Utils.toLogSQL(sql, params));
    return jdbcTemplate.query(sql, params, new EntityMapper<ENTITY>(choozenColumns, columnMappers, entityClass));
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
    return jdbcTemplate.query(sql, params, BasicMappers.getMapper(rClass));
  }

  /**
   * 根据SQL查询结果
   * 
   * @param eClass 结果映射到的对象
   * @param sql 查询SQL
   * @param params 参数
   * @return created by Tianxin on 2015年6月3日 下午8:50:26
   */
  protected <E> List<E> findBySQL(Class<E> eClass, String sql, Object... params) {
    LOG.info(Utils.toLogSQL(sql, params));
    return jdbcTemplate.query(sql, params, BasicMappers.getEntityMapper(eClass, sql));
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

  /**
   * 设置jdbcTemplate对象
   * @param jdbcTemplate
   * <br/>created by Tianxin on 2015年6月24日 下午5:50:42
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
}
