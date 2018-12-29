package org.darwin.simpleGenericDao.service;

import java.util.List;
import org.darwin.simpleGenericDao.entities.Comments;

/**
 * @author hexiufeng
 * @date 2018/12/29下午12:16
 */
public interface CommentsService {
  void save(Comments comments);
  List<Comments> findByPoiId(String poiId);
}
