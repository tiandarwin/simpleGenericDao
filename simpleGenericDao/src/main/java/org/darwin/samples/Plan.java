/**
 * org.darwin.samples.Plan.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:47:02
 */
package org.darwin.samples;

import java.util.Date;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.bo.UserObject;

/**
 * 一个叫做计划的对象
 * created by Tianxin on 2015年5月26日 下午8:47:02
 */
@Table(name = "plan", keyColumn = "id", db = "database", audoIncrementKey = true)
public class Plan extends UserObject<Integer> {
	
	static class Columns{
		public final static String planName = "plan_name";
		public final static String modifyTime = "modify_time";
	}

	/**
	 * 计划的名字
	 */
	@Column(Columns.planName)
	private String name;
	
	/**
	 * 计划的状态
	 */
	private int status;
	
	/**
	 * 计划的类型
	 */
	private int type;
	
	/**
	 * 计划的创建时间
	 */
	private Date createTime;
	
	/**
	 * 计划的最后修改时间
	 */
	@Column(value = Columns.modifyTime, modifiable = false)
	private Date modifyTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
