/**
 * org.darwin.genericDao.param.GtParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:03:51
 */
package org.darwin.genericDao.param;

import java.util.Arrays;
import java.util.List;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年5月27日 下午2:03:51
 */
public class GtParam implements Param {
	
	private Object floor;
	private boolean withEqual;

	/**
	 * @param floor
	 * @param b
	 */
	public GtParam(Object floor, boolean withEqual) {
		this.floor = floor;
		this.withEqual = withEqual;
	}

	public List<Object> getParams() {
		return Arrays.asList(floor);
	}

	public String buildOperate(String column) {
		return Utils.connect(column, withEqual ? " >= ?" : " > ?");
	}

}
