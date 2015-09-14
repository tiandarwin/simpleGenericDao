/**
 * org.darwin.genericDao.operate.Replace.java
 * created by Tianxin(tianjige@163.com) on 2015年7月16日 下午12:52:22
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.darwin.common.utils.DateUtils;
import org.darwin.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定义了一个字符串替换
 * <br/>created by Tianxin on 2015年7月16日 下午12:52:22
 */
public class Replaces {
  
  private List<Replace> list = new ArrayList<Replace>(5);
  
  /**
   * 一个替换对象
   * 
   * <br/>created by Tianxin on 2015年7月16日 下午1:00:22
   */
  private static class Replace{

    /**
     * 占位符
     */
    private String label;
    
    /**
     * 替换为的值
     */
    private Object value;
    /**
     * 构造函数
     * @param label
     * @param value
     */
    private Replace(String label, Object value) {
      this.label = label;
      this.value = value;
    }
    /**
     * 获取当前value的数据库值
     * @return
     * <br/>created by Tianxin on 2015年8月26日 下午4:04:38
     */
    public String value() {
      if(value == null){
        return String.valueOf(value);
      }
      
      if(value instanceof Date){
        return Utils.connect('\'', DateUtils.format((Date)value), '\'');
      }
      
      if(value instanceof String){
        if(label.equals("#table")){
          return String.valueOf(value);
        }else{
          return Utils.connect('\'', value, '\'');
        }
      }
      
      if(value instanceof Boolean){
        return ((Boolean)value) ? "1" : "0";
      }
      
      return String.valueOf(value);
    }
  }
  
  public static Logger getLog() {
    return LOG;
  }

  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(Replaces.class);

  /**
   * 做字符串的替换
   * @param sql
   * @return
   * <br/>created by Tianxin on 2015年7月16日 下午12:56:26
   */
  public String execute(String sql) {
    for(Replace r : list){
      sql = sql.replaceAll(r.label, r.value());
    }
    return sql;
  }
  
  public static Replaces one(String label, Object value){
    return new Replaces().replace(label, value);
  }

  /**
   * 增加一个替换操作
   * @param label   占位符
   * @param value   替换后的值
   * @return
   * <br/>created by Tianxin on 2015年7月16日 下午1:03:11
   */
  public Replaces replace(String label, Object value) {
    list.add(new Replace(label, value));
    return this;
  }
}
