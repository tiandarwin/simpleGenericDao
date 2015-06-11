/**
 * org.darwin.generic.sample.bo.UnitAware.java
 * created by Tianxin(tianjige@163.com) on 2015年6月9日 下午2:26:56
 */
package org.darwin.generic.sample.bo;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.bo.UserObject;

/**
 * 关联到unit的对象的基础实现
 * created by Tianxin on 2015年6月9日 下午2:26:56
 */
public abstract class UnitAware<KEY extends Number> extends UserObject<KEY> {

	/**
	 * 推广计划的ID
	 */
	@Column("planid")
	private long planId;
	
	/**
	 * 推广单元的ID
	 */
	@Column("unitid")
	private long unitId;
	
	
	public long getPlanId() {
		return planId;
	}
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	public long getUnitId() {
		return unitId;
	}
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	
	
}
