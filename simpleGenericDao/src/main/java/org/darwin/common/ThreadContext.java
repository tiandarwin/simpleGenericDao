/**
 * org.darwin.common.ThreadContext.java
 * created by Tianxin(tianjige@163.com) on 2015年6月8日 上午10:30:23
 */
package org.darwin.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程上下文存储对象
 * created by Tianxin on 2015年6月8日 上午10:30:23
 */
public class ThreadContext {

  /**
   * 全局的线程上下文数据存储
   */
  private final static ThreadLocal<Map<String, Object>> CTX = new ThreadLocal<Map<String, Object>>();

  /**
   * 清空ThreadContext中的内容
   * 
   * created by Tianxin on 2015年6月8日 上午10:32:31
   */
  public final static void clean() {
    CTX.get().clear();
    CTX.set(null);
  }

  /**
   * 线程上下文的初始化
   * 
   * created by Tianxin on 2015年6月8日 上午10:33:31
   */
  public final static void init() {
    Map<String, Object> currentThreadCtx = new HashMap<String, Object>(16);
    CTX.set(currentThreadCtx);

  }

  /**
   * 向线程上下文中存储一个字段
   * @param key
   * @param value
   * created by Tianxin on 2015年6月8日 上午10:35:53
   */
  public final static <V> void put(String key, V value) {
    CTX.get().put(key, value);
  }

  /**
   * 从上下文中获取到key对应的值
   * @param key
   * @return
   * created by Tianxin on 2015年6月8日 上午10:37:28
   */
  @SuppressWarnings("unchecked")
  public final static <V> V get(String key) {
    return (V) CTX.get().get(key);
  }

  /**
   * @param key
   * created by Tianxin on 2015年6月15日 下午2:58:12
   */
  public static void putShardingKey(Object key) {
    put(shardingKey, key);
  }

  @SuppressWarnings("unchecked")
  public static <KEY> KEY getShardingKey() {
    return (KEY) get(shardingKey);
  }
  
  private final static String shardingKey = "shardingKey";
  //TODO 可能需要将ThreadContext的存储域进行细分，避免不同应用或不同层次的程序的key冲突
}
