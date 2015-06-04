/**
 * org.darwin.samples.stat.DailyRegionStatDaoImpl.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午4:28:41
 */
package org.darwin.samples.stat;

import org.darwin.genericDao.dao.impl.GenericStatDao;

/**
 * created by Tianxin on 2015年6月3日 下午4:28:41
 */
public class XDailyRegionStatDaoImpl extends GenericStatDao<XDailyRegionStat> implements XDailyRegionStatDao {

	
	public static void main(String[] args) {
		new XDailyRegionStatDaoImpl().statByRange(20150501, 20150505, true);
	}
}
