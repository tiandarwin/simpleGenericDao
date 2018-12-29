package org.darwin.simpleGenericDao.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.darwin.simpleGenericDao.dao.CommentsDao;
import org.darwin.simpleGenericDao.entities.Comments;
import org.darwin.simpleGenericDao.service.CommentsService;
import org.springframework.stereotype.Service;

/**
 * @author hexiufeng
 * @date 2018/12/29下午12:16
 */
@Service
public class CommentsServiceImpl implements CommentsService {

  @Resource
  private CommentsDao commentsDao;
  @Override
  public void save(Comments comments) {
    commentsDao.create(comments);
  }

  @Override
  public List<Comments> findByPoiId(String poiId) {
    return commentsDao.findByPoiId(poiId);
  }


}
