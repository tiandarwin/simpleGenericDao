package org.darwin.genericDao.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.darwin.genericDao.dao.TableAware;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * 抽象Handler实现，多个Handler可复用的功能实现，之前有QueryHandler实现，现在已经删除
 *
 * @author hexiufeng
 * @date 2018/12/29下午11:37
 */
abstract class AbstractHandler<ENTITY> {
  protected List<String> allColumns;
  protected List<String> updateColumns;

  /**
   * insert的SQL中参数的展位符——(?,?,?,?)
   */
  protected String sInsertPlaceHolder;
  protected List<String> insertColumns;
  protected String sInsertColumns;

  protected Map<String, ColumnMapper> columnMappers;
  protected TableAware tableAware;

  protected AbstractHandler(Map<String, ColumnMapper> columnMappers, TableAware tableAware) {
    this.tableAware = tableAware;
    this.columnMappers = columnMappers;
    int mapperCount = columnMappers.size();
    List<String> insertColumns = new ArrayList<String>(mapperCount);
    List<String> updateColumns = new ArrayList<String>(mapperCount);
    List<String> allColumns = new ArrayList<String>(mapperCount);

    Collection<ColumnMapper> mappers = columnMappers.values();
    for (ColumnMapper mapper : mappers) {
      allColumns.add(mapper.getColumn());
      insertColumns.add(mapper.getColumn());
      if (mapper.getAnnotation() == null || mapper.getAnnotation().modifiable()) {
        updateColumns.add(mapper.getColumn());
      }
    }

    initColumns(insertColumns, updateColumns, allColumns);
  }

  /**
   * 生成update的SQL语句
   *
   * @return created by Tianxin on 2015年5月27日 下午7:44:14
   */
  public  String generateUpdateSQL(ENTITY entity) {
    StringBuilder sb = new StringBuilder(512);
    sb.append("update ").append(tableAware.table()).append(" set ");
    for (String column : updateColumns) {
      sb.append(column).append("=?,");
    }
    sb.setCharAt(sb.length() - 1, ' ');
    sb.append(" where ").append(tableAware.keyColumn()).append("=?");
    return sb.toString();
  }

  /**
   * 初始化字段列表的设置
   * @param insertColumns
   * @param updateColumns
   * @param allColumns
   * created by Tianxin on 2015年6月1日 上午6:56:20
   */
  private void initColumns(List<String> insertColumns, List<String> updateColumns, List<String> allColumns) {

    //普通的字段保存
    this.allColumns = allColumns;
    this.insertColumns = insertColumns;
    this.updateColumns = updateColumns;

    //构造insert语句的字段列表与展位符
    StringBuilder sInsertColumnBuilder = new StringBuilder(insertColumns.size() * 9);
    StringBuilder sInsertPHolderBuilder = new StringBuilder(insertColumns.size() * 2 + 2);
    for (String column : insertColumns) {
      sInsertColumnBuilder.append(',').append(column);
      sInsertPHolderBuilder.append(",?");
    }
    sInsertColumnBuilder.append(')').setCharAt(0, '(');
    sInsertPHolderBuilder.append(')').setCharAt(0, '(');
    this.sInsertColumns = sInsertColumnBuilder.toString();
    this.sInsertPlaceHolder = sInsertPHolderBuilder.toString();
  }

  /**
   * @param columns
   * @param entity
   *            created by Tianxin on 2015年5月27日 下午8:22:04
   */
  protected List<Object> getParamsByColumns(List<String> columns, ENTITY entity) {
    ArrayList<Object> params = new ArrayList<Object>(columns.size() + 1);
    for (String column : columns) {
      try {
        ColumnMapper cMapper = columnMappers.get(column);
        Object value = cMapper.getGetter().invoke(entity);
        params.add(value);
      } catch (Exception e) {
        throw new RuntimeException(column + "getter invoke error!", e);
      }
    }
    return params;
  }
}
