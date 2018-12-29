package org.darwin.genericDao.shard;

/**
 * 无效的分表策略，用于兼容之前dao层实现分表策略的逻辑
 *
 * @author hexiufeng
 * @date 2018/12/29上午11:59
 */
public class UnknownShardTableRule implements ShardTableRule {

  @Override
  public String shardTable(Object shardKey, int count,String tableNamePrefix) {
    throw new RuntimeException("can't use this implement.");
  }
}
