/**
 * org.darwin.common.utils.ValueUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年7月13日 下午12:46:03
 */
package org.darwin.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象取值的方法集
 * <br/>created by Tianxin on 2015年7月13日 下午12:46:03
 */
public class ValueUtils {
  
  /**
   * 确保target不为空，如果为空，则返回其他默认值
   * @param value
   * @param defaultValue
   * @return
   * <br/>created by Tianxin on 2015年7月13日 下午12:47:57
   */
  public final static <V> V ensureValueNotNull(V value, V defaultValue){
    if(value == null){
      return defaultValue;
    }
    return value;
  }
  
  /**
   * 确保能进行正确的取值。如果target为空，则取默认值。如果getter获取出来的字段为空，则取默认值。
   * @param target
   * @param getter
   * @param defaultValue
   * @return
   * <br/>created by Tianxin on 2015年7月13日 下午12:54:12
   */
  public final static <V, E> V ensureNotNull(E target, KeyGetter<V, E> getter, V defaultValue){
    if(target == null){
      return defaultValue;
    }
    V v = getter.getKey(target);
    return ensureValueNotNull(v, defaultValue);
  }
  
  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(ValueUtils.class);
}
