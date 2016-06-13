/**
 * org.darwin.genericDao.bo.UserObject.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:20:38
 */
package org.darwin.genericDao.bo;

import java.io.Serializable;

/**
 * 从属于某一个用户的业务模型
 * created by Tianxin on 2015年5月26日 下午8:20:38
 */
public class UserObject<KEY extends Serializable> extends BaseObject<KEY> {

  public static class Columns {
    public final static String userId = "userid";
  }

  /**
   * 对象所属的用户的ID
   */
  private int userId;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

}
