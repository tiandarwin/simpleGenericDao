/**
 * org.darwin.genericDao.mapper.ResultFetcher.java
 * created by Tianxin(tianjige@163.com) on 2015年6月5日 上午11:31:34
 */
package org.darwin.genericDao.mapper.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * created by Tianxin on 2015年6月5日 上午11:31:34
 */
public interface TypeFetcher {

	/**
	 * 从resultSet中获取第index字段的值
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 * created by Tianxin on 2015年6月5日 下午12:09:27
	 */
	Object getFromResultSet(ResultSet rs, int index) throws SQLException;
	
	/**
	 * 从resultSet中获取第label字段的值
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 * created by Tianxin on 2015年6月5日 下午12:09:27
	 */
	Object getFromResultSet(ResultSet rs, String label) throws SQLException;
}
