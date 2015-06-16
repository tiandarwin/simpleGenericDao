/**
 * org.darwin.genericDao.query.Query.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:48:40
 */
package org.darwin.genericDao.query;


/**
 * created by Tianxin on 2015年5月27日 下午1:48:40
 */
public interface Query {

	/**
	 * 获取一个query的SQL
	 * @return
	 * created by Tianxin on 2015年5月27日 下午3:05:41
	 */
	String getSQL();
	
	/**
	 * 获取一个query的参数
	 * @return
	 * created by Tianxin on 2015年5月27日 下午3:05:59
	 */
	Object[] getParams();
	
}
