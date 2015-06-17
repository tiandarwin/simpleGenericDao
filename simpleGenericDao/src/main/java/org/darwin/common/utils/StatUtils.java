/**
 * org.darwin.common.utils.StatUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午4:24:15
 */
package org.darwin.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 报表系统需要使用的方法集.天全部按照yyyyMMdd的格式，月按照yyyyMM格式，年按照yyyy格式
 * created by Tianxin on 2015年6月4日 下午4:24:15
 */
public class StatUtils {
	
	private final static long millisOneDay = 1000 * 60 * 60 * 24;

	/**
	 * 以yyyyMMdd格式返回今天的时间数字
	 * @return
	 * created by Tianxin on 2015年6月4日 下午4:29:33
	 */
	public final static int getToday(){
		Date date = new Date();
		return getDateInt(date);
	}
	
	/**
	 * 以yyyyMMdd格式返回昨天的时间数字
	 * @return
	 * created by Tianxin on 2015年6月4日 下午4:30:04
	 */
	public final static int getYesterday(){
		Date date = new Date(System.currentTimeMillis() - millisOneDay);
		return getDateInt(date);
	}
	
	/**
	 * 以yyyyMMdd格式返回当天的时间数字
	 * @param date
	 * @return
	 * created by Tianxin on 2015年6月4日 下午4:30:16
	 */
	public final static int getDateInt(Date date){
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
	public final static Date getDateFromInt(int yyyyMMdd){
		Calendar calendar = new GregorianCalendar();
		calendar.set(yyyyMMdd / 10000, yyyyMMdd % 10000 / 100 - 1, yyyyMMdd % 100, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @return
	 * created by Tianxin on 2015年6月4日 下午4:55:49
	 */
	public static int getNextDate(int date) {
		try{
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			Date oDate = formater.parse(String.valueOf(date));
			Date next = new Date(oDate.getTime() + millisOneDay);
			return getDateInt(next);
		}catch(Exception e){
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
    if(date == null){
      return null;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return format.format(date);
  }
}
