/**
 * org.darwin.common.utils.Utils.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午1:25:52
 */
package org.darwin.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	public final static <K,V> Map<K,V> newMap(){
		return newMap(10);
	}
	
	
	/**
	 * 生成一个新的map，按加载因子计算一个合适的长度
	 * @param size
	 * @return
	 * created by Tianxin on 2015年6月4日 下午2:05:37
	 */
	public final static <K,V> Map<K,V> newMap(int size){
		return new HashMap<K, V>((size * 4 + 2 )/ 3);
	}
	
	/**
	 * 生成一个新的set，按加载因子计算一个合适的长度
	 * @return
	 * created by Tianxin on 2015年6月4日 下午2:05:37
	 */
	public final static <K> Set<K> newSet(){
		return newSet(10);
	}
	
	/**
	 * 生成一个新的set，按加载因子计算一个合适的长度
	 * @param size
	 * @return
	 * created by Tianxin on 2015年6月4日 下午2:05:37
	 */
	public final static <K> Set<K> newSet(int size){
		return new HashSet<K>((size * 4 + 2 )/ 3);
	}
	
	/**
	 * 判断一个集合是否为空
	 * @param cs
	 * @return
	 * created by Tianxin on 2015年6月4日 下午1:34:00
	 */
	public final static boolean isEmpty(Collection<?> cs){
		return cs == null || cs.isEmpty();
	}
	
	/**
	 * 判断一个字符串是否为空
	 * @param cs
	 * @return
	 * created by Tianxin on 2015年6月4日 下午1:34:00
	 */
	public final static boolean isEmpty(String s){
		return s == null || s.length() == 0;
	}
	
	/**
	 * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
	 * @param entities
	 * @return
	 * created by Tianxin on 2015年6月4日 下午1:35:07
	 */
	public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<KEY> extractKeys(Collection<ENTITY> entities){
		if(isEmpty(entities)){
			return Collections.emptyList();
		}
		List<KEY> keys = new ArrayList<KEY>(entities.size());
		for(ENTITY entity : entities){
			if(entity != null){
				keys.add(entity.getId());
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
	public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Map<KEY, ENTITY> trans2Map(Collection<ENTITY> entities){
		if(isEmpty(entities)){
			return Collections.emptyMap();
		}
		Map<KEY, ENTITY> map = newMap(entities.size());
		for(ENTITY entity : entities){
			if(entity != null){
				map.put(entity.getId(), entity);
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
	public final static <KEY> Set<KEY> trans2Set(Collection<KEY> keys){
		if(isEmpty(keys)){
			return Collections.emptySet();
		}
		Set<KEY> set = newSet(keys.size());
		for(KEY key : keys){
			if(key != null){
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
			return Collections.emptyList();
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
			return Collections.emptyList();
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
		if(isEmpty(coll)){
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
	public static Object[] trans2Array(List<Object> oList) {
		if(isEmpty(oList)){
			return new Object[0];
		}
		
		return oList.toArray(new Object[oList.size()]);
	}


}
