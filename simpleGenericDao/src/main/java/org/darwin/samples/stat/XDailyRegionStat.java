/**
 * com.wolong.dashboard.bo.DailyRegionStat.java
 * created by Tianxin(tianjige@163.com) on 2015年6月1日 下午3:58:49
 */
package org.darwin.samples.stat;

import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.annotations.enums.Type;
import org.darwin.genericDao.annotations.stat.StatType;
import org.darwin.genericDao.annotations.stat.StatTable;

/**
 * 分地域的统计情况 created by Tianxin on 2015年6月1日 下午3:58:49
 */
@StatTable(db = "wl_ad_stat", name = "daily_region_stat", columnStyle = ColumnStyle.JAVA_TO_MYSQL)
public class XDailyRegionStat extends XAbstractDailyAdStat {

	/**
	 * 省的地域id
	 */
	@StatType(Type.KEY)
	private int province;

	/**
	 * 市的地域id
	 */
	@StatType(Type.KEY)
	private int city;

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}
}
