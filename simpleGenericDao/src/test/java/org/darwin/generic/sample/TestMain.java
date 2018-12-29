/**
 * org.darwin.generic.sample.TestMain.java
 * created by Tianxin(tianjige@163.com) on 2015年6月9日 下午2:33:49
 */
package org.darwin.generic.sample;

import java.util.List;

import org.darwin.generic.sample.dao.CreativeDao;
import org.darwin.generic.sample.dao.impl.CreativeDaoImpl;
import org.darwin.genericDao.dao.impl.GenericDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * created by Tianxin on 2015年6月9日 下午2:33:49
 */
public class TestMain {

	public static void main(String[] args) {
		CreativeDao dao = getDao(CreativeDaoImpl.class);
		
	}
	
	public static <T extends GenericDao> T getDao(Class<T> daoClass){
		try {
			T dao = daoClass.newInstance();
			dao.setJdbcTemplate(mockJdbcTemplate());
			return dao;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 * created by Tianxin on 2015年6月9日 下午2:37:31
	 */
	private static JdbcTemplate mockJdbcTemplate() {
		return new JdbcTemplate(){
			
			@Override
			public int update(String sql, Object... args) throws DataAccessException {
				System.out.println(sql);
				return 0;
			}

			@Override
			public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
				System.out.println(sql);
				return new int[0];
			}

			@Override
			public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
				System.out.println(sql);
				return null;
			}

			@Override
			public int queryForInt(String sql, Object... args) throws DataAccessException {
				System.out.println(sql);
				return 0;
			}
		};
	}
	
	
}
