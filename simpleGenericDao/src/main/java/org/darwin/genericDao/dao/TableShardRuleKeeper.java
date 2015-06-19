/**
 * org.darwin.genericDao.dao.TableRuleKeeper.java
 * created by Tianxin(tianjige@163.com) on 2015年6月18日 下午5:25:14
 */
package org.darwin.genericDao.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * table的切分规则持有者
 * <br/>created by Tianxin on 2015年6月18日 下午5:25:14
 */
public class TableShardRuleKeeper {
  
  /**
   * 生成shard表名
   * @param logicTable
   * @param shardKey
   * @return
   * <br/>created by Tianxin on 2015年6月18日 下午5:32:59
   */
  public final static String generateTableName(String logicTable, Object shardKey){
    return instance.tableRule.generateTableName(logicTable, shardKey);
  }
  
  /**
   * 生成逻辑库名
   * @param logicDB
   * @param shardKey
   * @return
   * <br/>created by Tianxin on 2015年6月18日 下午5:33:19
   */
  public final static String generateDBName(String logicDB, Object shardKey){
    return instance.tableRule.generateDBName(logicDB, shardKey);
  }
  
  /**
   * 一个切分规则的持有者
   */
  private static TableShardRuleKeeper instance = null;
  
  /**
   * 构造函数
   */
  public TableShardRuleKeeper() {
    instance = this;
  }
  
  private TableShardRule tableRule;
  
  public TableShardRule getTableRule() {
    return tableRule;
  }

  public void setTableRule(TableShardRule tableRule) {
    this.tableRule = tableRule;
  }

  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(TableShardRuleKeeper.class);
}
