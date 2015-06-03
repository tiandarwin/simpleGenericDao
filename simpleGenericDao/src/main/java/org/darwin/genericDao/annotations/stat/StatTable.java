/**
 * org.darwin.genericDao.annotations.stat.StatTable.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午1:49:42
 */
package org.darwin.genericDao.annotations.stat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.annotations.enums.TimeType;

/**
 * 标记一个统计表
 * created by Tianxin on 2015年5月26日 下午8:28:45
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StatTable {

	/**
	 * 表名
	 * @return
	 * created by Tianxin on 2015年6月3日 下午1:50:50
	 */
	String name();
	
	/**
	 * 所在的database名
	 * @return
	 * created by Tianxin on 2015年6月3日 下午1:50:56
	 */
	String db();
	
	/**
	 * 该表是否做了水平拆表
	 * @return
	 * created by Tianxin on 2015年6月3日 下午1:51:04
	 */
	int shardCount() default 0;
	
	/**
	 * 水平拆表以哪个key字段来做shard
	 * @return
	 * created by Tianxin on 2015年6月3日 下午1:51:19
	 */
	String shardKey() default "";
    
    /**
     * 对象属性到数据库列的映射规则，默认为 {@link ColumnStyle#LOWER_CASE}向前兼容
     * 
     * @return 映射规则 @see ColumnStyle
     */
    ColumnStyle columnStyle() default ColumnStyle.JAVA_TO_MYSQL;
    
    /**
     * 该统计表的时间粒度，默认为天
     * @return
     * created by Tianxin on 2015年6月3日 下午2:10:19
     */
    TimeType timeUnit() default TimeType.DAY;
}
