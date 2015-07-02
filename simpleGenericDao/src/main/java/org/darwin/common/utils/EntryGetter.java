/**
 * org.darwin.common.utils.EntryGetter.java
 * created by Tianxin(tianjige@163.com) on 2015年7月1日 下午5:27:10
 */
package org.darwin.common.utils;

/**
 * 获取一个map中的entry
 * <br/>created by Tianxin on 2015年7月1日 下午5:27:10
 */
public interface EntryGetter<ENTITY,K,V> extends KeyGetter<K, ENTITY> {

  V getValue(ENTITY entity);
}
