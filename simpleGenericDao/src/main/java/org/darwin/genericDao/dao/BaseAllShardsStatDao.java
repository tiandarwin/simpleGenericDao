/**
 * org.darwin.genericDao.dao.BaseAllShardsStatDao.java
 * created by Tianxin(tianjige@163.com) on 2015年6月18日 下午2:46:21
 */
package org.darwin.genericDao.dao;

import java.util.List;

/**
 * 多库扫描的dao
 * <br/>created by Tianxin on 2015年6月18日 下午2:46:21
 */
public interface BaseAllShardsStatDao<ENTITY> {

  /**
   * 获取全部数据
   * @return
   * created by Tianxin on 2015年6月12日 上午11:45:06
   */
  List<ENTITY> findAll();

  /**
   * 查询出全部的结果
   * @param aggregationByDate 是否按时间聚合
   * @return
   * created by Tianxin on 2015年6月3日 下午3:59:32
   */
  List<ENTITY> statAll(boolean aggregationByDate);

  /**
   * 查询某一个时间范围的接口时间范围包括两端的时间点
   * @param startDate
   * @param endDate
   * @param aggregationByDate
   * @return
   * created by Tianxin on 2015年6月3日 下午3:59:40
   */
  List<ENTITY> statByRange(int startDate, int endDate, boolean aggregationByDate);


}
