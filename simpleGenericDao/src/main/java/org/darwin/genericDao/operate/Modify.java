/**
 * org.darwin.genericDao.query.Modify.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:07:53
 */
package org.darwin.genericDao.operate;

import java.util.List;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.param.Param;
import org.darwin.genericDao.param.SQLParams;

/**
 * created by Tianxin on 2015年5月27日 下午12:07:53
 */
public class Modify implements Operate {

	/**
	 * 构造一个Modify对象
	 * @param column
	 * @param value
	 */
	Modify(String column, Object value) {
		this.column = column;
		if(value instanceof Param){
			this.param = (Param)value;
		}else{
			this.param = SQLParams.normal(value);
		}
	}
	
	private String column;
	private Param param;
	
	public String getOperate() {
		return Utils.connect(column, "=?");
	}
	public List<Object> getParams() {
		return param.getParams();
	}
	public boolean isEmpty() {
		return false;
	}
}
