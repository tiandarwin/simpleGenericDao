/**
 * org.darwin.genericDao.param.LikeParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:59:59
 */
package org.darwin.genericDao.param;

import java.util.Arrays;
import java.util.List;

import org.darwin.common.utils.GenericDaoUtils;

/**
 * created by Tianxin on 2015年5月27日 下午1:59:59
 */
public class LikeParam implements Param {

	private boolean leftStrict = false;
	private boolean rightStrict = false;
	private Object value;

	/**
	 * @param word
	 */
	public LikeParam(String word) {
		this.value = word;
	}

	/**
	 * @param word
	 * @param leftStrict
	 * @param rightStrict
	 */
	public LikeParam(String word, boolean leftStrict, boolean rightStrict) {
		this.value = word;
		this.leftStrict = leftStrict;
		this.rightStrict = rightStrict;
	}

	public List<Object> getParams() {
		return Arrays.asList((Object)(GenericDaoUtils.connect(leftStrict ? "" : "%", value, rightStrict ? "" : "%")));
	}

	public String buildOperate(String column) {
		if (leftStrict && rightStrict) {
			return GenericDaoUtils.connect(column, " = ?");
		} else {
			return GenericDaoUtils.connect(column, " like ?");
		}
	}

}
