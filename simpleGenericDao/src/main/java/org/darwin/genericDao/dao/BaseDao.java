/**
 * org.darwin.genericDao.dao.BaseDao.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:02:58
 */
package org.darwin.genericDao.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.darwin.genericDao.bo.BaseObject;

/**
 * DAO接口的一个通用定义，任何一个DAO都需要继承于这个接口
 * created by Tianxin on 2015年5月26日 下午9:02:58
 */
public interface BaseDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> {

	/**
	 * 新建一条记录
	 * created by Tianxin on 2015年5月26日 下午9:16:24
	 */
	boolean create(ENTITY entity);
	
	/**
	 * 新建多条记录，默认采用快速方式进行新建
	 * created by Tianxin on 2015年5月26日 下午9:16:37
	 */
	int create(Collection<ENTITY> entities);
	
	/**
	 * 如果存在ID相同的数据则update，如果不存在则创建
	 * created by Tianxin on 2015年5月26日 下午9:16:51
	 */
	boolean replace(ENTITY entity);
	
	/**
	 * 更新一条记录
	 * created by Tianxin on 2015年5月26日 下午9:17:47
	 */
	boolean update(ENTITY entity);
	
	/**
	 * 批量更新一些数据
	 * created by Tianxin on 2015年5月26日 下午9:17:57
	 */
	int update(Collection<ENTITY> entities);
	
	/**
	 * 删除某id的数据
	 * created by Tianxin on 2015年5月26日 下午9:18:09
	 */
	boolean delete(KEY id);
	
	/**
	 * 删除某条数据
	 * created by Tianxin on 2015年5月26日 下午9:18:26
	 */
	boolean delete(ENTITY entity);
	
	/**
	 * 删除多条数据
	 * created by Tianxin on 2015年5月26日 下午9:18:33
	 */
	int delete(Collection<ENTITY> entities);
	
	/**
	 * 删除多条数据
	 * created by Tianxin on 2015年5月26日 下午9:18:33
	 */
	int deleteByKeys(Collection<KEY> keys);
	
	/**
	 * 删除多条记录
	 * @param keys
	 * @return
	 * created by Tianxin on 2015年6月2日 上午11:28:08
	 */
	int deleteByKeys(KEY...keys);
	
	/**
	 * 根据ID查询一个对象
	 * created by Tianxin on 2015年5月26日 下午9:18:40
	 */
	ENTITY get(KEY id);
	
	/**
	 * 根据多个ID进行批量查询
	 * created by Tianxin on 2015年5月26日 下午9:18:51
	 */
	List<ENTITY> get(Collection<KEY> ids);
	
	/**
	 * 将所有数据查询出来
	 * created by Tianxin on 2015年5月26日 下午9:19:02
	 */
	List<ENTITY> findAll();
	
	/**
	 * 查询该表的所有记录数
	 * created by Tianxin on 2015年5月26日 下午9:19:16
	 */
	int countAll();
	
}
