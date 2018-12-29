package org.darwin.simpleGenericDao.command;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.darwin.common.ThreadContext;
import org.darwin.simpleGenericDao.entities.Comments;
import org.darwin.simpleGenericDao.service.CommentsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author hexiufeng
 * @date 2018/12/29下午1:31
 */
@Component
public class RumCommand implements CommandLineRunner {

  @Resource
  private CommentsService commentsService;

  @Override
  public void run(String... strings) throws Exception {
    ThreadContext.init();
    Comments comments = new Comments();
    comments.setUserId(101L);
    comments.setComments("haha");
    comments.setGroupId("2390037");
    comments.setCreateTime(new Date());
    ThreadContext.putShardingKey(comments.getGroupId());
    commentsService.save(comments);
    List<Comments> list = commentsService.findByPoiId(comments.getGroupId());
    System.out.println(list.get(list.size()-1).getCreateTime()+"---"+list.get(list.size()-1).getId());
    System.out.println("====================");
  }
}
