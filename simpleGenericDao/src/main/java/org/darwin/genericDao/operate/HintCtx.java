/**
 * org.darwin.genericDao.operate.HintCtx.java
 * created by Tianxin(tianjige@163.com) on 2016年6月14日 下午6:46:03
 */
package org.darwin.genericDao.operate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 强制索引的上下文
 * <br/>created by Tianxin on 2016年6月14日 下午6:46:03
 */
public class HintCtx {
  
  private final static ThreadLocal<String> hintSetting = new ThreadLocal<String>();
  
  /**
   * 设置强制命索引的名字
   * @param indexName
   * @param 
   * <br/>created by Tianxin on 2016年6月14日 下午6:48:35
   */
  public static void setForceIndex(String indexName){
    hintSetting.set(indexName);
  }
  
  /**
   * 将强制索引的设置移除
   * 
   * <br/>created by Tianxin on 2016年6月14日 下午6:49:17
   */
  public static void cleanForceIndex(){
    hintSetting.remove();
  }
  
  /**
   * 获取强制命中的索引
   * @return
   * <br/>created by Tianxin on 2016年6月14日 下午6:58:49
   */
  public static String getForceIndex(){
    return hintSetting.get();
  }
  
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(HintCtx.class);
}
