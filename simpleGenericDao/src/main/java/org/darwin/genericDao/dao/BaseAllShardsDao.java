/**
 * org.darwin.genericDao.dao.BaseAllShardsDao.java
 * created by Tianxin(tianjige@163.com) on 2015年5月29日 下午9:23:14
 */
package org.darwin.genericDao.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.darwin.genericDao.bo.BaseObject;

/**
 * 查询
 * created by Tianxin on 2015年5月29日 下午9:23:14
 */
public interface BaseAllShardsDao <KEY extends Serializable, ENTITY extends BaseObject<KEY>> {
  
  /**
   * 根据ID查询一个对象
   * created by Tianxin on 2015年5月26日 下午9:18:40
   */
  ENTITY get(KEY id);
  
  /**
   * 根据多个ID进行批量查询
   * created by Tianxin on 2015年5月26日 下午9:18:51
   */
  List<ENTITY> get(Collection<KEY> ids);
  
  /**
   * 将所有数据查询出来
   * created by Tianxin on 2015年5月26日 下午9:19:02
   */
  List<ENTITY> findAll();
  
  /**
   * 查询该表的所有记录数
   * created by Tianxin on 2015年5月26日 下午9:19:16
   */
  int countAll();
  
}
