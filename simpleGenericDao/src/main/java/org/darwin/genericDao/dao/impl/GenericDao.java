/**
 * org.darwin.genericDao.dao.GenericDao.java created by Tianxin(tianjige@163.com) on 2015年5月26日
 * 下午9:20:41
 */
package org.darwin.genericDao.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.darwin.common.BaseObjectUtils;
import org.darwin.common.GenericDaoUtils;
import org.darwin.common.utils.Utils;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 通用的带有主键的DAO
 * created by Tianxin on 2015年5月26日 下午9:20:41
 */
public class GenericDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> extends AbstractGenericDao<ENTITY> implements BaseDao<KEY, ENTITY> {
  
  /**
   * static slf4j logger instance
   */
  protected final static Logger LOG = LoggerFactory.getLogger(GenericDao.class);

  /**
   * 构造函数，在这里对最主要的几个属性进行初始化
   */
  public GenericDao() {
    super();
    this.keyClass = GenericDaoUtils.getGenericEntityClass(entityClass, BaseObject.class, 0);
  }

  /**
   * 主键的类型
   */
  private Class<KEY> keyClass;

  /**
   * 该方法会对ENTITY逐条插入，每插入一条时会获取一下数据库中生成的key
   * 
   * @param entities
   * @return
   */
  public int createAndFetchKey(Collection<ENTITY> entities) {
    if (entities == null || entities.size() == 0) {
      return 0;
    }

    int createdCount = 0;
    for (ENTITY entity : entities) {
      createdCount += create(entity) ? 1 : 0;
    }
    return createdCount;
  }

  /**
   * 新建一条记录
   */
  @SuppressWarnings("unchecked")
  public boolean create(ENTITY entity) {
    if (entity == null) {
      return false;
    }

    // 获取sql以及参数
    List<ENTITY> entities = Utils.toEntities(entity);
    final String sql = writeHandler.generateInsertSQL(entities);
    final Object[] params = writeHandler.generateInsertParams(entities);

    // 如果id不为null，则直接插入即可
    if (entity.getId() != null) {
      LOG.info(Utils.toLogSQL(sql, params));
      return executeBySQL(sql, params) >= 1;
    }

    // ID为null时，执行插入操作同时要获取插入的key
    KeyHolder keyHolder = new GeneratedKeyHolder();
    LOG.info(sql);
    int rowCount = this.jdbcTemplate.update(new PreparedStatementCreator() {
      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int index = 1;
        for (Object param : params) {
          ps.setObject(index++, param);
        }
        return ps;
      }
    }, keyHolder);

    // 如果插入成功则获取keyHolder中的key
    if (rowCount != 0 && keyHolder.getKey() != null) {
      KEY key = null;
      if (keyClass.equals(Integer.class)) {
        key = (KEY) Integer.valueOf(keyHolder.getKey().intValue());
      } else if (keyClass.equals(Long.class)) {
        key = (KEY) Long.valueOf(keyHolder.getKey().longValue());
      } else if (keyClass.equals(Short.class)) {
        key = (KEY) Short.valueOf(keyHolder.getKey().shortValue());
      } else if (keyClass.equals(String.class)) {
        key = (KEY) keyHolder.getKey().toString();
      } else {
        throw new RuntimeException(entityClass.getName() + " is unexcepted key class!");
      }
      entity.setId(key);
    }

    return rowCount >= 1;
  }

  /**
   * replace操作
   */
  public int replace(Collection<ENTITY> entities) {
   return createCore(entities, 1);
  }
  
  public boolean replace(ENTITY entity) {
    List<ENTITY> list = new ArrayList<ENTITY>(1); 
    list.add(entity);
    return createCore(list, 1) == 1;
  }

  /**
   * 根据主键删除对象
   */
  public boolean delete(KEY id) {
    if (id == null) {
      return false;
    }
    return delete(configKeeper.keyColumn(), id) > 0;
  }

  /**
   * 删除单个对象
   */
  public boolean delete(ENTITY entity) {
    if (entity == null) {
      return false;
    }

    return delete(entity.getId());
  }

  /**
   * 删除多个对象
   */
  public int delete(Collection<ENTITY> entities) {
    entities = Utils.trimEntities(entities);
    if (entities == null || entities.size() == 0) {
      return 0;
    }

    List<KEY> ids = BaseObjectUtils.extractKeys(entities);
    return delete(configKeeper.keyColumn(), ids);
  }

  /**
   * 根据多个KEY进行删除
   */
  public int deleteByKeys(Collection<KEY> ids) {
    if (ids == null || ids.size() == 0) {
      return 0;
    }
    return delete(configKeeper.keyColumn(), ids);
  }

  /**
   * 根据KEY进行删除
   */
  public int deleteByKeys(KEY... ids) {
    if (ids == null || ids.length == 0) {
      return 0;
    }
    return delete(configKeeper.keyColumn(), ids);
  }

  /**
   * update一条记录
   */
  public boolean update(ENTITY entity) {
    if (entity == null) {
      return false;
    }
    String sql = writeHandler.generateUpdateSQL(entity);
    Object[] params = writeHandler.generateUpdateParams(entity);
    
    LOG.info(Utils.toLogSQL(sql, params));
    return executeBySQL(sql, params) >= 1;
  }

  /**
   * 批量update
   */
  public int update(Collection<ENTITY> entities) {

    // 如果为空或这个集合都是空对象，则不更新
    final List<ENTITY> list = Utils.trimEntities(entities);
    if (list == null || list.size() == 0) {
      return 0;
    }

    String sql = writeHandler.generateUpdateSQL(list.get(0));
    LOG.info(sql);
    int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

      public void setValues(PreparedStatement ps, int index) throws SQLException {
        Object[] params = writeHandler.generateUpdateParams(list.get(index));
        int i = 1;
        for (Object param : params) {
          ps.setObject(i++, param);
        }
      }

      public int getBatchSize() {
        return list.size();
      }
    });

    // 计算影响的数据条数
    int updatedCount = 0;
    for (int i : results) {
      updatedCount += i;
    }
    return updatedCount;
  }

  /**
   * 根据主键进行查找
   */
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

  /**
   * 根据若干个ID进行查找
   */
  public List<ENTITY> get(Collection<KEY> ids) {
    if (ids == null || ids.size() == 0) {
      return new ArrayList<ENTITY>(0);
    }
    return find(configKeeper.keyColumn(), ids);
  }

}
