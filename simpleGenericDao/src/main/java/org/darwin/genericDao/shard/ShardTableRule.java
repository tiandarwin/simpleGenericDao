package org.darwin.genericDao.shard;

/**
 * 分表规则接口
 *
 * @author hexiufeng
 * @date 2018/12/29上午11:58
 */
public interface ShardTableRule {

  /**
   * 分表名称
   *
   * @param shardKey
   * @param count
   * @param tableNamePrefix
   * @return
   */
  String shardTable(Object shardKey, int count,String tableNamePrefix);
}
