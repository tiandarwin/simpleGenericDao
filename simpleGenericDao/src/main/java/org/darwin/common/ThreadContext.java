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
  
  public static void ensureInited(){
    if(CTX.get() == null){
      init();
    }
  }

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
   * 线程上下文的初始化。init方法必须和clean方法配合使用。在线程的最外层调用中，先init，然后在finally中clean。
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
    try{
    CTX.get().put(key, value);
    }catch(NullPointerException e){
      System.out.println("调用ThreadContext时，必须要先进行ThreadContext的init，线程退出前再进行clean，避免被其他线程使用到本线程的数据，发生线程安全问题。");
      throw e;
    }
  }

  /**
   * 从上下文中获取到key对应的值
   * @param key
   * @return
   * created by Tianxin on 2015年6月8日 上午10:37:28
   */
  @SuppressWarnings("unchecked")
  public final static <V> V get(String key) {
    Map<String, Object> map = CTX.get();
    return map == null ? null : (V) CTX.get().get(key);
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
