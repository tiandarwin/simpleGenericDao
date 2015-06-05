/**
 * org.darwin.samples.MyJdbcTemplate.java
 * created by Tianxin(tianjige@163.com) on 2015年6月1日 下午2:22:13
 */
package org.darwin.samples;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * created by Tianxin on 2015年6月1日 下午2:22:13
 */
public class MockJdbcTemplate extends JdbcTemplate {

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		System.out.println(sql);
		return super.update(sql, args);
	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
		System.out.println(sql);
		return super.batchUpdate(sql, pss);
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		System.out.println(sql);
		return super.query(sql, args, rowMapper);
	}

	@Override
	public int queryForInt(String sql, Object... args) throws DataAccessException {
		System.out.println(sql);
		return super.queryForInt(sql, args);
	}
}
