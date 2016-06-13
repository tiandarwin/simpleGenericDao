/**
 * org.darwin.genericDao.annotations.enums.ColumnType.java
 * created by Tianxin(tianjige@163.com) on 2015年8月11日 上午11:55:04
 */
package org.darwin.genericDao.annotations.enums;

import java.util.ArrayList;
import java.util.List;

import org.darwin.common.utils.Utils;


/**
 * SQL中的column的操作类型
 * <br/>created by Tianxin on 2015年8月11日 上午11:55:04
 */
public enum ColumnBuilder {

  MAX {
    public String build(String field) {
      return Utils.connect("max(", field, ") as ", field);
    }
  },
  MIN {
    public String build(String field) {
      return Utils.connect("min(", field, ") as ", field);
    }
  },
  SUM {
    public String build(String field) {
      return Utils.connect("sum(", field, ") as ", field);
    }
  },
  AVG {
    public String build(String field) {
      return Utils.connect("avg(", field, ") as ", field);
    }
  },
  DISTINCT {
    public String build(String field) {
      return Utils.connect("distinct ", field);
    }
  };

  /**
   * 该type处理一个字段时要如何搞
   * @param field
   * @return
   * <br/>created by Tianxin on 2015年8月11日 下午1:16:27
   */
  public abstract String build(String field);

  /**
   * 用该类型的columnBuilder包装所需要的filed集合
   * @param fields
   * @return
   * <br/>created by Tianxin on 2015年8月11日 下午1:15:33
   */
  public List<String> build(String... fields) {
    //防止为空
    if (fields == null || fields.length == 0) {
      return new ArrayList<String>(0);
    }

    //字段列表转换
    List<String> list = new ArrayList<String>(fields.length);
    for (String field : fields) {
      list.add(build(field));
    }
    return list;
  }
}
