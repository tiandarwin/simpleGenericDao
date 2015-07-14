/**
 * org.darwin.common.utils.DateUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年7月13日 下午12:44:03
 */
package org.darwin.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 处理日期的方法集
 * <br/>created by Tianxin on 2015年7月13日 下午12:44:03
 */
public class DateUtils {

  private final static long millisOneMinute = 1000 * 60;
  private final static long millisOneHour = millisOneMinute * 60;
  private final static long millisOneDay = millisOneHour * 24;
  
  /**
   * 获取当前的小时戳
   * @return
   * <br/>created by Tianxin on 2015年7月14日 下午2:12:30
   */
  @SuppressWarnings("deprecation")
  public final static int getThisHour(){
    return new Date().getHours();
  }

  /**
   * 以yyyyMMdd格式返回今天的时间数字
   * @return
   * created by Tianxin on 2015年6月4日 下午4:29:33
   */
  public final static int getTodayInt() {
    Date date = new Date();
    return getDateInt(date);
  }

  /**
   * 以yyyyMMdd格式返回昨天的时间数字
   * @return
   * created by Tianxin on 2015年6月4日 下午4:30:04
   */
  public final static int getYesterdayInt() {
    Date date = new Date(System.currentTimeMillis() - millisOneDay);
    return getDateInt(date);
  }

  /**
   * 以yyyyMMdd格式返回当天的时间数字
   * @param date
   * @return
   * created by Tianxin on 2015年6月4日 下午4:30:16
   */
  public final static int getDateInt(Date date) {
    SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
    String sDate = formater.format(date);
    return Integer.parseInt(sDate);
  }

  /**
   * 将参数转为相应的date类型
   * @param yyyyMMdd
   * @return
   * created by Tianxin on 2015年6月4日 下午4:45:49
   */
  public final static Date getDateFromInt(int yyyyMMdd) {
    Calendar calendar = new GregorianCalendar();
    calendar.set(yyyyMMdd / 10000, yyyyMMdd % 10000 / 100 - 1, yyyyMMdd % 100, 0, 0, 0);
    return calendar.getTime();
  }

  /**
   * 将参数转为相应的date类型
   * @param yyyyMMdd
   * @return
   * created by Tianxin on 2015年6月4日 下午4:45:49
   */
  public final static String getDateStringFromInt(int yyyyMMdd) {
    String date = String.valueOf(yyyyMMdd);
    return Utils.connect(date.substring(0, 4), '-',date.substring(4, 6), '-', date.substring(6)); 
  }

  /**
   * 获取date的下一天
   * @param date
   * @return
   * created by Tianxin on 2015年6月4日 下午4:55:49
   */
  public static int getNextDateInt(int date) {
    try {
      SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
      Date oDate = formater.parse(String.valueOf(date));
      Date next = new Date(oDate.getTime() + millisOneDay);
      return getDateInt(next);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取date的前一天
   * @param date
   * @return
   * <br/>created by Tianxin on 2015年6月23日 下午1:44:08
   */
  public static int getLastDateInt(int date) {
    try {
      SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
      Date oDate = formater.parse(String.valueOf(date));
      Date next = new Date(oDate.getTime() - millisOneDay);
      return getDateInt(next);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 获取date的下一天
   * @param date
   * @return
   * created by Tianxin on 2015年6月4日 下午4:55:49
   */
  public static Date getNextDate(int date) {
    try {
      SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
      Date oDate = formater.parse(String.valueOf(date));
      return new Date(oDate.getTime() + millisOneDay);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 获取date的前一天
   * @param date
   * @return
   * <br/>created by Tianxin on 2015年6月23日 下午1:44:08
   */
  public static Date getLastDate(int date) {
    try {
      SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
      Date oDate = formater.parse(String.valueOf(date));
      return new Date(oDate.getTime() - millisOneDay);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 获取hours个小时之前的时间
   * @param hours
   * @return
   * <br/>created by Tianxin on 2015年6月23日 下午1:44:08
   */
  public static Date getHoursAgo(int hours) {
    try {
      return new Date(System.currentTimeMillis() - millisOneHour * hours);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Date getMinutesAgo(int minutes){
    try {
      return new Date(System.currentTimeMillis() - millisOneMinute * minutes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 获取上一个小时的时间
   * @return
   * <br/>created by Tianxin on 2015年6月23日 下午1:44:08
   */
  public static Date getLastHour() {
    try {
      return new Date(System.currentTimeMillis() - millisOneHour);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  

  /**
   * 返回标准的时间格式
   * @param date
   * @return
   * created by Tianxin on 2015年6月16日 上午10:35:12
   */
  public static Object getStringDate(Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(date);
  }
}
