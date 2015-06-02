/**
 * org.darwin.samples.PlanDao.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:41:29
 */
package org.darwin.samples;

import java.util.List;

import org.darwin.genericDao.dao.BaseDao;

/**
 * created by Tianxin on 2015年5月26日 下午9:41:29
 */
public interface PlanDao extends BaseDao<Integer, Plan> {

	int countByName(String name);
	
	List<Plan> findByUserIdAndStatus(int userId, int status);
	
	List<Plan> pageByUserId(int userId, int offset, int limit);
	List<Plan> pageSimpleByUserId(int userId, int offset, int limit);
	List<String> pagePlanNameByUserId(int userId, int offset, int limit);
}
