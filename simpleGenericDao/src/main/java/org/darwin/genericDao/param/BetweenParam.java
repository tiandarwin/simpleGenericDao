/**
 * org.darwin.genericDao.param.BetweenParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:05:20
 */
package org.darwin.genericDao.param;

import java.util.ArrayList;
import java.util.List;

import org.darwin.common.utils.Utils;

/**
 * mysql的between是包含边界值的
 * created by Tianxin on 2015年5月27日 下午2:05:20
 */
public class BetweenParam implements Param {
	
	private Object start;
	private Object end;

	/**
	 * @param start
	 * @param end
	 */
	public BetweenParam(Object start, Object end) {
		this.start = start;
		this.end = end;
	}

	public List<Object> getParams() {
		List<Object> os = new ArrayList<Object>();
		if(start != null){
			os.add(start);
		}
		if(end != null){
			os.add(end);
		}
		return os;
	}

	public String buildOperate(String column) {
		if(start == null && end == null){
			throw new RuntimeException("your code error!! start and end can't be null together");
		}else if(end == null){
			return Utils.connect(column, ">=?");
		}else if(start == null){
			return Utils.connect(column, "<=?");
		}else {
			return Utils.connect(column, " between ? and ?");
		}
	}

}
