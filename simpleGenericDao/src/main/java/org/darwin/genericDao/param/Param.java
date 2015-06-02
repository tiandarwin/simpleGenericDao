/**
 * org.darwin.genericDao.param.Param.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:18:03
 */
package org.darwin.genericDao.param;

import java.util.List;

/**
 * created by Tianxin on 2015年5月27日 下午12:18:03
 */
public interface Param {

	List<Object> getParams();
	String buildOperate(String column);
}
