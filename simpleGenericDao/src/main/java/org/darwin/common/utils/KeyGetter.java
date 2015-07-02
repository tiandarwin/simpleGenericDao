/**
 * org.darwin.common.utils.PropertyGetter.java
 * created by Tianxin(tianjige@163.com) on 2015年7月1日 下午2:07:38
 */
package org.darwin.common.utils;


/**
 * 
 * <br/>created by Tianxin on 2015年7月1日 下午2:07:38
 */
public interface KeyGetter<KEY, ENTITY> {

  KEY getKey(ENTITY entity);
}
