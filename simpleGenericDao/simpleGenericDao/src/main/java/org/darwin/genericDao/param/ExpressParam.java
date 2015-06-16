/**
 * org.darwin.genericDao.param.ExpressParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:07:49
 */
package org.darwin.genericDao.param;

import java.util.Arrays;
import java.util.List;

/**
 * 表达式参数，一些复杂的需求很难提前对象化，用这个来表达。
 * 例如 expression 设置为 style=(status*2+style-?)
 * created by Tianxin on 2015年5月27日 下午2:07:49
 */
public class ExpressParam implements Param {
	
	private String expression;
	private Object[] params;

	/**
	 * @param value
	 */
	public ExpressParam(String expression, Object...params) {
		this.expression = expression;
		this.params = params;
	}

	public List<Object> getParams() {
		return Arrays.asList(params);
	}

	public String buildOperate(String column) {
		return expression;
	}

}
