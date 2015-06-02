/**
 * org.darwin.samples.PlanDaoImpl.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:42:06
 */
package org.darwin.samples;

import java.util.List;

import org.darwin.genericDao.bo.UserObject;
import org.darwin.genericDao.dao.impl.GenericDao;
import org.darwin.genericDao.operate.Matches;

/**
 * created by Tianxin on 2015年5月26日 下午9:42:06
 */	
public class PlanDaoImpl extends GenericDao<Integer, Plan> implements PlanDao {

	public int countByName(String name) {
		return count(Plan.Columns.planName, name);
	}

	public List<Plan> findByUserIdAndStatus(int userId, int status) {
		return find(Matches.and().match(UserObject.Columns.userId, userId).match("status", status));
	}

	public List<Plan> pageByUserId(int userId, int offset, int limit) {
		return page(Matches.one(UserObject.Columns.userId, userId), offset, limit);
	}

	public List<Plan> pageSimpleByUserId(int userId, int offset, int limit) {
		return findBySQL(Plan.class, "select * from plan where userid=? limit ?, ?", toList(userId, offset, limit));
	}

	public List<String> pagePlanNameByUserId(int userId, int offset, int limit) {
		return pageOneColumn(String.class, Matches.one(UserObject.Columns.userId, userId), Plan.Columns.planName, offset, limit);
	}
	
	public static void main(String[] args) {
		new PlanDaoImpl();
	}
	
	

}
