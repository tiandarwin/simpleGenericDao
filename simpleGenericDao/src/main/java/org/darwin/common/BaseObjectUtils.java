/**
 * org.darwin.common.BaseObjectUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年7月15日 上午10:24:11
 */
package org.darwin.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.darwin.common.utils.KeyGetter;
import org.darwin.common.utils.Utils;
import org.darwin.genericDao.bo.BaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <br/>created by Tianxin on 2015年7月15日 上午10:24:11
 */
public class BaseObjectUtils {

  /**
   * 获取两个list的交集，是否相等使用id来进行判断
   * @param list1
   * @param list2
   * @return
   * created by Tianxin on 2015年6月11日 下午4:53:13
   */
  public final static <K extends Serializable, E extends BaseObject<K>> List<E> getCrossedList(Collection<E> list1, Collection<E> list2) {

    if (Utils.isEmpty(list1) || Utils.isEmpty(list2)) {
      return new ArrayList<E>(0);
    }

    Set<K> ks = extractKeySet(list1);
    List<E> list = new ArrayList<E>(Math.min(list1.size(), list2.size()));
    for (E e : list2) {
      if (e != null && ks.contains(e.getId())) {
        list.add(e);
      }
    }
    return list;
  }

  /**
   * 获取两个list的并集，是否相等使用id来进行判断
   * @param list1
   * @param list2
   * @return
   * created by Tianxin on 2015年6月11日 下午4:53:13
   */
  public final static <K extends Serializable, E extends BaseObject<K>> List<E> getUnionList(Collection<E> list1, Collection<E> list2) {
    boolean empty1 = Utils.isEmpty(list1);
    boolean empty2 = Utils.isEmpty(list2);

    if (empty1 && empty2) {
      return new ArrayList<E>(0);
    } else if (empty1) {
      return new ArrayList<E>(list2);
    } else if (empty2) {
      return new ArrayList<E>(list1);
    } else {

      Set<K> ks = Utils.newSet(list1.size() + list2.size());
      List<E> list = new ArrayList<E>(list1.size() + list2.size());
      for (E e : list1) {
        if (e != null && !ks.contains(e.getId())) {
          list.add(e);
        }
      }
      for (E e : list2) {
        if (e != null && !ks.contains(e.getId())) {
          list.add(e);
        }
      }
      return list;
    }

  }

  /**
   * 将实体列表转化为一个map，key为实体的id，value为实体本身
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Map<KEY, ENTITY> trans2Map(Collection<ENTITY> entities) {
    return Utils.trans2Map(entities, new KeyGetter<KEY, ENTITY>() {
      public KEY getKey(ENTITY entity) {
        return entity.getId();
      }
    });
  }

  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<KEY> extractKeys(Collection<ENTITY> entities) {
    return Utils.extractKeys(entities, new KeyGetter<KEY, ENTITY>() {
      public KEY getKey(ENTITY entity) {
        return entity.getId();
      }
    });
  }


  /**
   * 抽取实体列表的key作为一个新的列表,null的对象会被跳过
   * @param entities
   * @return
   * created by Tianxin on 2015年6月4日 下午1:35:07
   */
  public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Set<KEY> extractKeySet(Collection<ENTITY> entities) {
    return Utils.extractKeySet(entities, new KeyGetter<KEY, ENTITY>() {
      public KEY getKey(ENTITY entity) {
        return entity.getId();
      }
    });
  }

  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(BaseObjectUtils.class);
}
