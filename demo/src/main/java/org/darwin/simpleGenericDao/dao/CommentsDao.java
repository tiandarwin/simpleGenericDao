package org.darwin.simpleGenericDao.dao;


import java.util.List;
import javax.annotation.Resource;
import org.darwin.genericDao.annotations.UseQueryColumnFormat;
import org.darwin.genericDao.annotations.enums.QueryColumnFormat;
import org.darwin.genericDao.dao.impl.GenericDao;
import org.darwin.simpleGenericDao.entities.Comments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author hexiufeng
 * @date 2018/12/29下午12:15
 */
@Repository
@UseQueryColumnFormat(QueryColumnFormat.POJO_FIELD_NAME)
public class CommentsDao extends GenericDao<Long,Comments> {
  @Resource(name = "jdbcTemplate")
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Comments> findByPoiId(String poiId) {
    return super.find("groupId",poiId);
  }
}
