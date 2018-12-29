/**
 * org.darwin.genericDao.dao.BaseMappers.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午5:35:37
 */
package org.darwin.genericDao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.darwin.common.GenericDaoUtils;
import org.darwin.common.utils.Utils;
import org.darwin.genericDao.mapper.jdbc.FetcherCache;
import org.darwin.genericDao.mapper.jdbc.TypeFetcher;
import org.springframework.jdbc.core.RowMapper;

/**
 * 一些基础类的映射对象 created by Tianxin on 2015年5月28日 下午5:35:37
 */
public class BasicMappers {

  /**
   * 将N个类各自的映射关系做一个map
   */
  private static ConcurrentMap<Class<?>, Map<String, ColumnMapper>> columnMappersMap = new ConcurrentHashMap<Class<?>, Map<String, ColumnMapper>>(64 * 4 / 3);

  private static ConcurrentMap<Class<?>, Class<?>> entityKeyClassMap = new ConcurrentHashMap<Class<?>, Class<?>>(16 * 4/3);

  public static <ENTITY> List<ColumnMapper> getMappers(Class<?> entityClass) {

    //object截止
    if (entityClass.equals(Object.class)) {
      return null;
    }

    Map<String, ColumnMapper> mappers = columnMappersMap.get(entityClass);
    if (mappers == null) {
      entityClass = entityClass.getSuperclass();
      return getMappers(entityClass);
    } else {
      return new ArrayList<ColumnMapper>(mappers.values());
    }
  }

  /**
   * 获取一个实体类的主键类型
   * @param entityClass
   * @return
   * created by Tianxin on 2015年6月3日 下午12:16:03
   */
  public static Class<?> getKeyClass(Class<?> entityClass) {
    return entityKeyClassMap.get(entityClass);
  }

  /**
   * 将一个columnMappers注册到基础映射集中
   * @param entityClass
   * @param columnMappers
   * created by Tianxin on 2015年6月1日 下午2:13:29
   */
  public static void register(Class<?> keyClass, Class<?> entityClass, Map<String, ColumnMapper> columnMappers) {
    entityKeyClassMap.put(entityClass, keyClass);
    columnMappersMap.put(entityClass, columnMappers);
  }

  /**
   * 获取某个clazz的字段转载关系
   * 
   * @param clazz
   * @return created by Tianxin on 2015年5月28日 下午5:56:17
   */
  private static Map<String, ColumnMapper> getColumnMappers(Class<?> clazz) {
    Map<String, ColumnMapper> map = columnMappersMap.get(clazz);
    if (map != null) {
      return map;
    }

    map = GenericDaoUtils.generateColumnMappers(clazz, null);
    columnMappersMap.put(clazz, map);
    return map;
  }

  /**
   * 根据SQL和目标类，构造一个唯一的装载器
   * 
   * @param eClass
   * @param sql
   * @return created by Tianxin on 2015年5月28日 下午5:48:08
   */
  public static <ENTITY extends Object> RowMapper<ENTITY> getEntityMapper(Class<ENTITY> eClass, String sql) {

    //看是否是基本类型
    RowMapper<ENTITY> mapper = getMapper(eClass, true);
    if (mapper != null) {
      return mapper;
    }

    List<String> columns = GenericDaoUtils.getColumnsFromSQL(sql);
    Map<String, ColumnMapper> columnMappers = getColumnMappers(eClass);
    return new EntityMapper<ENTITY>(columns, columnMappers, eClass);
  }

  /**
   * 获取一个基础类的mapper
   * 
   * @param rClass
   * @param nullable    是否允许为空
   * @return created by Tianxin on 2015年6月1日 下午1:49:42
   */
  private static <R> RowMapper<R> getMapper(Class<R> rClass, boolean nullable) {

    if (nullable && !FetcherCache.contains(rClass)) {
      return null;
    }

    final TypeFetcher fetcher = FetcherCache.getFetcher(rClass);
    return new RowMapper<R>() {

      @SuppressWarnings("unchecked")
      public R mapRow(ResultSet rs, int rowNum) throws SQLException {
        return (R) (fetcher.getFromResultSet(rs, 1));
      }
    };
  }

  /**
   * 获取一个基础类的mapper
   * 
   * @param rClass
   * @return created by Tianxin on 2015年6月1日 下午1:49:42
   */
  public static <R> RowMapper<R> getMapper(Class<R> rClass) {
    return getMapper(rClass, false);
  }
}
