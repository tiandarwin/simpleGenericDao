/**
 * org.darwin.generic.sample.dao.CreativeDao.java
 * created by Tianxin(tianjige@163.com) on 2015年6月9日 下午2:32:37
 */
package org.darwin.generic.sample.dao;

import java.util.List;

import org.darwin.generic.sample.bo.Creative;
import org.darwin.genericDao.dao.BaseDao;

/**
 * created by Tianxin on 2015年6月9日 下午2:32:37
 */
public interface CreativeDao extends BaseDao<Long, Creative> {

	List<Creative> findByTitle(String title);
}
