/**
 * org.darwin.genericDao.annotations.stat.StatType.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:05:23
 */
package org.darwin.genericDao.annotations.enums;

/**
 * 默认的几种统计类型
 * created by Tianxin on 2015年6月3日 下午1:55:45
 */
public enum Type {
	SUM(1),AVG(2),KEY(3),DATE(4),EXTEND(5);
	
	private int value = 1;
	private Type(int value) {
		this.value = value;
	}
	public int value(){
		return this.value;
	}
	
}
