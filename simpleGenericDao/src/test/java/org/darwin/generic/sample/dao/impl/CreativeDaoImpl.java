/**
 * org.darwin.generic.sample.dao.impl.CreativeDaoImpl.java
 * created by Tianxin(tianjige@163.com) on 2015年6月9日 下午2:33:21
 */
package org.darwin.generic.sample.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.darwin.generic.sample.bo.Creative;
import org.darwin.generic.sample.dao.CreativeDao;
import org.darwin.genericDao.dao.impl.GenericDao;
import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Modifies;
import org.darwin.genericDao.param.SQLParams;

public class CreativeDaoImpl extends GenericDao<Long, Creative> implements CreativeDao {

	public List<Creative> findByTitle(String title) {
		return find("title", SQLParams.not(Arrays.asList("", "")));
	}
	public int updateTitle(String title) {
		return update(Modifies.init().modify("title", title).modify("planid", 1).modify("count", SQLParams.increase(1)), 
				Matches.one("groupid", 1));
	}
	
	
	//planid,groupid,unitid
	//groupid=1 and planid=2
	//planid=1 and unitid=3
	//groupid=22 and unitid=-44
	
}
