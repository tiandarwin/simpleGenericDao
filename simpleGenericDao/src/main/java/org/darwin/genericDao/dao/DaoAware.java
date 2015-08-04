/**
 * org.darwin.genericDao.dao.DaoAware.java
 * created by Tianxin(tianjige@163.com) on 2015年7月8日 上午11:35:53
 */
package org.darwin.genericDao.dao;

import java.util.Collection;
import java.util.List;


/**
 * 
 * <br/>created by Tianxin on 2015年7月8日 上午11:35:53
 */
public interface DaoAware<ENTITY>{
  

  /**
   * 新建一条记录
   * created by Tianxin on 2015年5月26日 下午9:16:24
   */
  boolean create(ENTITY entity);
  
  /**
   * 新建多条记录，默认采用快速方式进行新建
   * created by Tianxin on 2015年5月26日 下午9:16:37
   */
  int create(Collection<ENTITY> entities);
  
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
  
  /**
   * 删除该表的所有记录
   * @return
   * <br/>created by Tianxin on 2015年8月4日 上午11:12:35
   */
  int deleteAll();
  
  /**
   * 清空该表
   * 
   * <br/>created by Tianxin on 2015年8月4日 上午11:12:49
   */
  void truncate();

}
