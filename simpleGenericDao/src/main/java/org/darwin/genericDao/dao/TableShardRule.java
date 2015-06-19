/**
 * org.darwin.genericDao.dao.TableShardRule.java
 * created by Tianxin(tianjige@163.com) on 2015年6月18日 下午5:27:16
 */
package org.darwin.genericDao.dao;


/**
 * 分表的表名生成规则
 * <br/>created by Tianxin on 2015年6月18日 下午5:27:16
 */
public interface TableShardRule {

  /**
   * 生成逻辑表名
   * @param logicTableName
   * @param shardKey
   * @return
   * <br/>created by Tianxin on 2015年6月18日 下午5:28:24
   */
  String generateTableName(String logicTableName, Object shardKey);
  
  /**
   * 生成逻辑库名
   * @param logicDBName
   * @param shardKey
   * @return
   * <br/>created by Tianxin on 2015年6月18日 下午5:28:36
   */
  String generateDBName(String logicDBName, Object shardKey);
}
