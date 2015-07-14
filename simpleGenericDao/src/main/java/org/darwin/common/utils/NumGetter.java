/**
 * org.darwin.common.utils.NumGetter.java
 * created by Tianxin(tianjige@163.com) on 2015年7月14日 下午3:26:29
 */
package org.darwin.common.utils;


/**
 * 
 * <br/>created by Tianxin on 2015年7月14日 下午3:26:29
 */
public interface NumGetter<ENTITY, KEY, NUM> extends KeyGetter<KEY, ENTITY>{
  
  NUM getNum(ENTITY entity);
  
  NUM sum(NUM n1, NUM n2);
}
