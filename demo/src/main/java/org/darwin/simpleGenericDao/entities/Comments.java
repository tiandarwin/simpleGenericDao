package org.darwin.simpleGenericDao.entities;

import java.util.Date;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.simpleGenericDao.shard.FarmerShardTable;

/**
 * @author hexiufeng
 * @date 2018/12/29上午11:40
 */
@Table(db="demo",name = "comments",shardCount = 4,shardTableRuleClass = FarmerShardTable.class)
public class Comments extends BaseObject<Long> {
  private String groupId;
  private String comments;
  private Long userId;
  private Date createTime;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
