/**
 * com.wolong.dashboard.bo.AbstractDailyAdStat.java
 * created by Tianxin(tianjige@163.com) on 2015年6月1日 下午4:06:16
 */
package org.darwin.samples.stat;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.enums.Type;
import org.darwin.genericDao.annotations.stat.StatType;
import org.darwin.genericDao.bo.BaseStatObject;

/**
 * 广告统计的父类
 * created by Tianxin on 2015年6月1日 下午4:06:16
 */
public abstract class XAbstractDailyAdStat extends BaseStatObject {
	
	/**
	 * 统计日期 格式为20150504
	 */
	@StatType(Type.DATE)
	private int date;
	
	/**
	 * 展现次数
	 */
	@StatType(Type.SUM)
	private long showNum;
	
	/**
	 * 点击次数
	 */
	@StatType(Type.SUM)
	private long clickNum;
	
	/**
	 * 消费总数，精确到分
	 */
	@StatType(Type.AVG)
	private long consumed;
	
	@Column("click_num/show_num as ctr")
	@StatType(Type.EXTEND)
	private float ctr;
	
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public long getShowNum() {
		return showNum;
	}
	public void setShowNum(long showNum) {
		this.showNum = showNum;
	}
	public long getClickNum() {
		return clickNum;
	}
	public void setClickNum(long clickNum) {
		this.clickNum = clickNum;
	}
	public long getConsumed() {
		return consumed;
	}
	public void setConsumed(long consumed) {
		this.consumed = consumed;
	}
	public float getCtr() {
		return ctr;
	}
	public void setCtr(float ctr) {
		this.ctr = ctr;
	}
	
}
