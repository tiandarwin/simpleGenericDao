/**
 * org.darwin.genericDao.query.QueryHandler.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午6:46:45
 */
package org.darwin.genericDao.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.dao.TableAware;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 *
 *
 * created by Tianxin on 2015年5月27日 下午6:46:45
 * updated by xiufeng，精简代码
 */
public class WriteHandler<ENTITY> extends AbstractHandler<ENTITY> {
  private static final String[] INSERT_OPERATES =  {"insert", "replace", "insert ignore"};


  /**
   *
   * @param columnMappers
   * @param tableAware
   */
  public WriteHandler(Map<String, ColumnMapper> columnMappers, TableAware tableAware) {

    super(columnMappers,tableAware);

  }


  /**
   * 获取insert时的参数列表
   * 
   * @return created by Tianxin on 2015年5月27日 下午7:42:28
   */
  public Object[] generateInsertParams(Collection<ENTITY> entities) {

    ArrayList<Object> params = new ArrayList<Object>(insertColumns.size() * entities.size());
    for (ENTITY entity : entities) {

      if (entity == null) {
        continue;
      }

      params.addAll(getParamsByColumns(insertColumns, entity));
    }
    return params.toArray();
  }



  /**
   * 生成insert语句
   * 
   * @return created by Tianxin on 2015年5月27日 下午7:44:14
   */
  public String generateInsertSQL(Collection<ENTITY> entities) {
    return generateInsertSQL(entities, 0);
  }

  /**
   * 生成insert语句
   * @param entities
   * @param type 0为普通,1为replace,2为insert ignore
   * 
   * @return created by Tianxin on 2015年5月27日 下午7:44:14
   */
  public String generateInsertSQL(Collection<ENTITY> entities, int type) {


    if (type >= INSERT_OPERATES.length || type < 0) {
      throw new RuntimeException("不是合法的type!");
    }

    StringBuilder sb = new StringBuilder(512);
    sb.append(INSERT_OPERATES[type]).append(" into ").append(tableAware.table());
    sb.append(' ').append(sInsertColumns).append(" values ");
    for (ENTITY entity : entities) {
      if (entity != null) {
        sb.append(sInsertPlaceHolder).append(',');
      }
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * 生成update的参数列表
   * 
   * @return created by Tianxin on 2015年5月27日 下午7:42:28
   */
  public Object[] generateUpdateParams(ENTITY entity) {
    if (entity instanceof BaseObject<?>) {
      List<Object> params = getParamsByColumns(updateColumns, entity);
      params.add(((BaseObject<?>) entity).getId());
      return params.toArray();
    }

    throw new RuntimeException(Utils.connect(entity.getClass().getSimpleName(), " 不是BaseObject的子类!"));
  }



  /**
   * @return
   * created by Tianxin on 2015年5月28日 下午5:22:32
   */
  public List<String> allColumns() {
    return this.allColumns;
  }
}
