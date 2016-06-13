/**
 * org.darwin.genericDao.dao.TableAware.java
 * created by Tianxin(tianjige@163.com) on 2016年6月13日 上午10:55:28
 */
package org.darwin.genericDao.dao;


/**
 * 对应一张表的一个对象
 * <br/>created by Tianxin on 2016年6月13日 上午10:55:28
 */
public interface TableAware {

  /**
   * 获取表的名字
   * @return
   * <br/>created by Tianxin on 2016年6月13日 上午10:56:08
   */
  String table();
  
  /**
   * 获取主键的字段名
   * @return
   * <br/>created by Tianxin on 2016年6月13日 上午10:56:22
   */
  String keyColumn();
}
