/**
 * org.darwin.genericDao.bo.BaseObject.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:18:14
 */
package org.darwin.genericDao.bo;

import java.io.Serializable;

/**
 * GenericDao中处理的业务模型的基础类，它的子类会与数据库中的表进行映射
 * created by Tianxin on 2015年5月26日 下午8:18:14
 */
public class BaseObject<KEY extends Serializable> {

	private KEY id;

	public KEY getId() {
		return id;
	}

	public void setId(KEY id) {
		this.id = id;
	}
}
