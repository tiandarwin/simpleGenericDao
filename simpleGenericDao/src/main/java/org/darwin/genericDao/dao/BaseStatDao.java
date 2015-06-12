/**
 * org.darwin.genericDao.dao.BaseStatDao.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午11:20:23
 */
package org.darwin.genericDao.dao;

import java.util.List;

/**
 * 统计SQL的DAO
 * created by Tianxin on 2015年5月28日 下午11:20:23
 */
public interface BaseStatDao<ENTITY> {
  
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
