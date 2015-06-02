/**
 * org.darwin.samples.Main.java
 * created by Tianxin(tianjige@163.com) on 2015年6月1日 下午2:19:12
 */
package org.darwin.samples;

import java.util.Arrays;


/**
 * created by Tianxin on 2015年6月1日 下午2:19:12
 */
public class Main {

	public static void main(String[] args) {
		PlanDao dao = new PlanDaoImpl();
		setJdbcTemplate(dao);
//		dao.findAll();
//		dao.findByUserIdAndStatus(1, 2);
		dao.deleteByKeys(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
	}

	/**
	 * 
	 * created by Tianxin on 2015年6月1日 下午2:20:34
	 */
	private static void setJdbcTemplate(PlanDao dao) {
		PlanDaoImpl impl = (PlanDaoImpl)dao;
		impl.setJdbcTemplate(new MyJdbcTemplate());
	}
}
