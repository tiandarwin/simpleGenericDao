/**
 * org.darwin.genericDao.param.LessThanParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:01:06
 */
package org.darwin.genericDao.param;

import java.util.Arrays;
import java.util.List;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年5月27日 下午2:01:06
 */
public class LtParam implements Param {
	
	private Object top;
	private boolean withEqual;

	/**
	 * @param top
	 * @param withEqual 
	 */
	public LtParam(Object top, boolean withEqual) {
		this.top = top;
		this.withEqual = withEqual;
	}

	public List<Object> getParams() {
		return Arrays.asList(top);
	}

	public String buildOperate(String column) {
		return Utils.connect(column, withEqual ? " <= ?" : " < ?");
	}

}
