/**
 * org.darwin.common.utils.Utils.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午1:25:52
 */
package org.darwin.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.darwin.genericDao.bo.BaseObject;

/**
 * 一些通用的方法集
 * created by Tianxin on 2015年6月4日 下午1:25:52
 */
public class Utils {

  /**
   * 生成一个新的map，按加载因子计算一个合适的长度
   * @return
   * created by Tianxin on 2015年6月4日 下午2:05:37
   */
  public final static <K, V> Map<K, V> newMap() {
    return newMap(10);
  }


  /**
   * 生成一个新的map，按加载因子计算一个合适的长度
   * @param size
   * @return
   * created by Tianxin on 2015年6月4日 下午2:05:37
   */
  public final static <K, V> Map<K, V> newMap(int size) {
    return new HashMap<K, V>((size * 4 + 2) / 3);
  }

  /**
   * 生成一个新的set，按加载因子计算一个合适的长度
   * @return
   * created by Tianxin on 2015年6月4日 下午2:05:37
   */
  public final static <K> Set<K> newSet() {
    return newSet(10);
  }

  /**
   * 生成一个新的set，按加载因子计算一个合适的长度
   * @param size
   * @return
   * created by Tianxin on 2015年6月4日 下午2:05:37
   */
  public final static <K> Set<K> newSet(int size) {
    return new HashSet<K>((size * 4 + 2) / 3);
  }

  /**
   * 生成一个新的set，按加载因子计算一个合适的长度
   * @param size
   * @return
   * created by Tianxin on 2015年6月4日 下午2:05:37
   */
  public final static <K> Set<K> newSet(K... ks) {
    if (ks == null || ks.length == 0) {
      return new HashSet<K>(0);
    }
    Set<K> kSet = new HashSet<K>((ks.length * 4 + 2) / 3);
    for (K k : ks) {
      kSet.add(k);
    }
    return kSet;
  }

  /**
   * 判断一个集合是否为空
   * @param cs
   * @return
   * created by Tianxin on 2015年6月4日 下午1:34:00
   */
  public final static boolean isEmpty(Collection<?> cs) {
    return cs == null || cs.isEmpty();
  }

  /**
   * 判断一个字符串是否为空
   * @param cs
   * @return
   * created by Tianxin on 2015年6月4日 下午1:34:00
   */
  public final static boolean isEmpty(String s) {
    return s == null || s.length() == 0;
  }

  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<KEY> extractKeys(Collection<ENTITY> entities) {
    if (isEmpty(entities)) {
      return new ArrayList<KEY>(0);
    }
    List<KEY> keys = new ArrayList<KEY>(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        keys.add(entity.getId());
      }
    }
    return keys;
  }
  
  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY> List<KEY> extractKeys(Collection<? extends ENTITY> entities, KeyGetter<KEY, ENTITY> keyGetter) {
    if (isEmpty(entities)) {
      return new ArrayList<KEY>(0);
    }
    List<KEY> keys = new ArrayList<KEY>(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        keys.add(keyGetter.getKey(entity));
      }
    }
    return keys;
  }
  
  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Set<KEY> extractKeySet(Collection<ENTITY> entities) {
    if (isEmpty(entities)) {
      return newSet(0);
    }
    Set<KEY> keys = newSet(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        keys.add(entity.getId());
      }
    }
    return keys;
  }
  
  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY> Set<KEY> extractKeySet(Collection<? extends ENTITY> entities, KeyGetter<KEY, ENTITY> keyGetter) {
    if (isEmpty(entities)) {
      return newSet(0);
    }
    Set<KEY> keys = newSet(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        keys.add(keyGetter.getKey(entity));
      }
    }
    return keys;
  }

  /**
   * 将实体列表转化为一个map，key为实体的id，value为实体本身
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Map<KEY, ENTITY> trans2Map(Collection<ENTITY> entities) {
    if (isEmpty(entities)) {
      return newMap(0);
    }
    Map<KEY, ENTITY> map = newMap(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        map.put(entity.getId(), entity);
      }
    }
    return map;
  }
  
  /**
   * 将entities中的每个对象提取出相应的key与value组成一个map
   * @param entities
   * @param entryGetter
   * @return
   * <br/>created by Tianxin on 2015年7月1日 下午5:46:26
   */
  public final static <K,V,ENTITY> Map<K, List<V>> extract2KeyListMap(Collection<ENTITY> entities, EntryGetter<ENTITY,K,V> entryGetter){
    if(isEmpty(entities)){
      return newMap(0);
    }
    Map<K, List<V>> map = newMap(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        
        K key = entryGetter.getKey(entity);
        V value = entryGetter.getValue(entity);
        List<V> list = map.get(key);
        if(list == null){
          list = new ArrayList<V>();
          map.put(key, list);
        }
        list.add(value);
      }
    }
    return map;
  }
  
  /**
   * 将entities中的每个对象提取出相应的key与value组成一个map
   * @param entities
   * @param entryGetter
   * @return
   * <br/>created by Tianxin on 2015年7月1日 下午5:46:26
   */
  public final static <K,V,ENTITY> Map<K, V> extract2Map(Collection<ENTITY> entities, EntryGetter<ENTITY,K,V> entryGetter){
    if(isEmpty(entities)){
      return newMap(0);
    }
    Map<K,V> map = newMap(entities.size());
    for(ENTITY entity : entities){
      map.put(entryGetter.getKey(entity), entryGetter.getValue(entity));
    }
    return map;
  }
  
  /**
   * 将实体列表转化为一个map，key为keyGetter获取到的key，value为实体本身
   * @param entities
   * @param keyGetter 从entity中获取放到hashMap里的key
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY> Map<KEY, ENTITY> trans2Map(Collection<ENTITY> entities, KeyGetter<KEY, ? super ENTITY> keyGetter) {
    return trans2Map(entities, keyGetter, false);
  }
  /**
   * 将实体列表转化为一个map，key为keyGetter获取到的key，value为实体本身
   * @param entities
   * @param keyGetter 从entity中获取放到hashMap里的key
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY> Map<KEY, ENTITY> trans2Map(Collection<ENTITY> entities, KeyGetter<KEY, ? super ENTITY> keyGetter, boolean checkDuplicate) {
    if (isEmpty(entities)) {
      return newMap(0);
    }
    Map<KEY, ENTITY> map = newMap(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        
        KEY key = keyGetter.getKey(entity);
        ENTITY o = map.get(key);
        
        if(o != null && checkDuplicate){
          throw new RuntimeException(connect(key, " 有两个重复的值在列表中！"));
        }
        map.put(key, entity);
      }
    }
    return map;
  }
  
  /**
   * 将实体列表转化为一个map，key为keyGetter获取到的key，value为实体本身
   * @param entities
   * @param keyGetter 从entity中获取放到hashMap里的key
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY> Map<KEY, List<ENTITY>> trans2KeyListMap(Collection<ENTITY> entities, KeyGetter<KEY, ? super ENTITY> keyGetter) {
    if (isEmpty(entities)) {
      return newMap(0);
    }
    Map<KEY, List<ENTITY>> map = newMap(entities.size());
    for (ENTITY entity : entities) {
      if (entity != null) {
        
        KEY key = keyGetter.getKey(entity);
        List<ENTITY> list = map.get(key);
        if(list == null){
          list = new ArrayList<ENTITY>();
          map.put(key, list);
        }
        list.add(entity);
      }
    }
    return map;
  }

  /**
   * 将一个集合转为set
   * @param keys
   * @return
   * created by Tianxin on 2015年6月4日 下午2:01:45
   */
  public final static <KEY> Set<KEY> trans2Set(Collection<KEY> keys) {
    if (isEmpty(keys)) {
      return newSet(0);
    }
    Set<KEY> set = newSet(keys.size());
    for (KEY key : keys) {
      if (key != null) {
        set.add(key);
      }
    }
    return set;
  }

  /**
   * 将若干个对象转换成一个对象list
   * @param os
   * @return
   * created by Tianxin on 2015年6月4日 下午1:26:43
   */
  public final static List<Object> toObjectList(Object... os) {
    if (os == null || os.length == 0) {
      return new ArrayList<Object>(0);
    }

    List<Object> list = new ArrayList<Object>(os.length);
    for (Object o : os) {
      list.add(o);
    }
    return list;
  }

  /**
   * 将若干个对象转换成一个list
   * @param es
   * @return
   * created by Tianxin on 2015年6月4日 下午1:26:43
   */
  public final static <E> List<E> toList(E... es) {
    if (es == null || es.length == 0) {
      return new ArrayList<E>(0);
    }

    List<E> list = new ArrayList<E>(es.length);
    for (E o : es) {
      list.add(o);
    }
    return list;
  }

  /**
   * 将对象转换成一个list
   * @param es
   * @return
   * created by Tianxin on 2015年6月4日 下午1:26:43
   */
  public final static <E> List<E> one2List(E e) {
    if (e == null) {
      return new ArrayList<E>(0);
    }

    List<E> list = new ArrayList<E>(1);
    list.add(e);
    return list;
  }

  /**
   * 字符串连接
   * 
   * @param os
   * @return
   */
  public final static String connect(Object... os) {
    if (os == null || os.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder(os.length * 5);
    for (Object o : os) {
      sb.append(o);
    }
    return sb.toString();
  }
  /**
   * 字符串连接
   * 
   * @param os
   * @return
   */
  public final static String connectBySplit(String split, Object... os) {
    if (os == null || os.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder(os.length * 5);
    for (Object o : os) {
      sb.append(o).append(split);
    }
    sb.delete(sb.length() - split.length(), sb.length());
    return sb.toString();
  }

  /**
   * 去除不合法的entity
   * 
   * @param entities
   * @return created by Tianxin on 2015年5月28日 下午4:29:02
   */
  public final static <ENTITY> List<ENTITY> trimEntities(Collection<ENTITY> entities) {
    if (entities == null || entities.size() == 0) {
      return null;
    }

    ArrayList<ENTITY> list = new ArrayList<ENTITY>(entities.size());
    for (ENTITY e : entities) {
      if (e != null) {
        list.add(e);
      }
    }
    return list;
  }

  /**
   * 将数组转换为list
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:31:56
   */
  public final static <ENTITY> List<ENTITY> toEntities(ENTITY... entities) {
    List<ENTITY> entityList = new ArrayList<ENTITY>(1);
    for (ENTITY entity : entities) {
      entityList.add(entity);
    }
    return entityList;
  }


  /**
   * 获取集合的第一个元素，如果为空则返回null
   * @param coll
   * @return
   * created by Tianxin on 2015年6月4日 下午2:24:48
   */
  public static <E> E getFirst(Collection<E> coll) {
    if (isEmpty(coll)) {
      return null;
    }
    return coll.iterator().next();
  }


  /**
   * 将对象列表转为对象数组
   * @param oList
   * @return
   * created by Tianxin on 2015年6月7日 下午2:11:04
   */
  public static Object[] trans2Array(Collection<?> oList) {
    if (isEmpty(oList)) {
      return new Object[0];
    }

    return oList.toArray(new Object[oList.size()]);
  }
  
  /**
   * 获取两个list的并集，是否相等使用id来进行判断
   * @param list1
   * @param list2
   * @return
   * created by Tianxin on 2015年6月11日 下午4:53:13
   */
  public final static <K extends Serializable, E extends BaseObject<K>> List<E> getUnionList(Collection<E> list1, Collection<E> list2){
    boolean empty1 = isEmpty(list1);
    boolean empty2 = isEmpty(list2);
    
    if(empty1 && empty2){
      return new ArrayList<E>(0);
    }else if(empty1){
      return new ArrayList<E>(list2);
    }else if(empty2){
      return new ArrayList<E>(list1);
    }else{
      
      Set<K> ks = newSet(list1.size() + list2.size());
      List<E> list = new ArrayList<E>(list1.size() + list2.size());
      for(E e : list1){
        if(e != null && !ks.contains(e.getId())){
          list.add(e);
        }
      }
      for(E e : list2){
        if(e != null && !ks.contains(e.getId())){
          list.add(e);
        }
      }
      return list;
    }
    
  }

  /**
   * 获取两个list的交集，是否相等使用id来进行判断
   * @param list1
   * @param list2
   * @return
   * created by Tianxin on 2015年6月11日 下午4:53:13
   */
  public final static <K extends Serializable, E extends BaseObject<K>> List<E> getCrossedList(Collection<E> list1, Collection<E> list2){
    
    if(isEmpty(list1) || isEmpty(list2)){
      return new ArrayList<E>(0);
    }
    
    Set<K> ks = extractKeySet(list1);
    List<E> list = new ArrayList<E>(Math.min(list1.size(), list2.size()));
    for(E e : list2){
      if(e != null && ks.contains(e.getId())){
        list.add(e);
      }
    }
    return list;
  }
  
  /**
   * 构造日志中的SQL
   * @param sql
   * @param args
   * @return
   * created by Tianxin on 2015年6月16日 上午10:28:13
   */
  public final static String toLogSQL(String sql, Object...args){
    
    if(sql == null || sql.length() == 0){
      return sql;
    }
    
    if(args == null || args.length == 0){
      return sql;
    }
    
    int count = countChar(sql, '?');
    if(count == args.length){
      return putArgsIn2SQL(sql, args);
    }else {
      return putArgsAfterSQL(sql, args);
    }
  }


  /**
   * 将参数放到SQL的结尾。当问号与其参数个数不匹配时才会这样
   * @param sql
   * @param args
   * @return
   * created by Tianxin on 2015年6月16日 上午10:42:35
   */
  private static String putArgsAfterSQL(String sql, Object[] args) {
    StringBuilder sb = new StringBuilder(sql.length() * 2);
    sb.append(" with args [");
    for(Object arg : args){
      sb.append(getSQLParam(arg)).append(',');
    }
    sb.setCharAt(sb.length() - 1, ']');
    return sb.toString();
  }


  /**
   * 将参数放到sql中
   * @param sql
   * @param args
   * @return
   * created by Tianxin on 2015年6月16日 上午10:41:28
   */
  private static String putArgsIn2SQL(String sql, Object... args) {
    StringBuilder sb = new StringBuilder(sql.length() * 2);
    int argIndex = 0;
    for(int i = 0 ; i < sql.length() ; i ++){
      char c = sql.charAt(i);
      if(c == '?'){
        Object param = args[argIndex];
        sb.append(getSQLParam(param));
        argIndex += 1;
      }else{
        sb.append(c);
      }
    }
    return sb.toString();
  }


  /**
   * 看字符串中有多少个char出现
   * @param s
   * @param c
   * @return
   * created by Tianxin on 2015年6月16日 上午10:39:01
   */
  public static int countChar(String s, char c) {
    if(isEmpty(s)){
      return 0;
    }
    
    int count = 0;
    for (int i = 0; i < s.length(); i++) {
      if(s.charAt(i) == c){
        count += 1;
      }
    }
    return count;
  }


  /**
   * @param param
   * @return
   * created by Tianxin on 2015年6月16日 上午10:32:27
   */
  public static Object getSQLParam(Object param) {
    if(param == null){
      return "null";
    }
    if(param instanceof String){
      return connect('\'', param, '\'');
    }
    if(param instanceof Date){
      return connect('\'', StatUtils.getStringDate((Date)param), '\'');
    }
    return param;
  }
  
}
