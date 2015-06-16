/**
 * org.darwin.generic.sample.bo.Creative.java
 * created by Tianxin(tianjige@163.com) on 2015年6月9日 下午2:28:30
 */
package org.darwin.generic.sample.bo;

import java.util.Date;

import org.darwin.common.utils.GenericDaoUtils;
import org.darwin.generic.sample.dao.impl.CreativeDaoImpl;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.dao.impl.GenericDao;

/**
 * created by Tianxin on 2015年6月9日 下午2:28:30
 */
@Table(db = "", name = "tb_creative join x on ", keyColumn = "creative_id")
public class Creative extends UnitAware<Long> {
	
	private String title;
	
	private String desc;
	
	private int status;
	
	private int addUser;
	
	private Date addTime;
	
	private int modUser;
	
	private Date modTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAddUser() {
		return addUser;
	}

	public void setAddUser(int addUser) {
		this.addUser = addUser;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getModUser() {
		return modUser;
	}

	public void setModUser(int modUser) {
		this.modUser = modUser;
	}

	public Date getModTime() {
		return modTime;
	}

	public void setModTime(Date modTime) {
		this.modTime = modTime;
	}
	
	public static void main(String[] args) {
      System.out.println(GenericDaoUtils.getGenericEntityClass(CreativeDaoImpl.class, GenericDao.class, 1));
    }
}
