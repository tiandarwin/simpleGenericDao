/**
 * org.darwin.genericDao.annotations.StatColumn.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午1:48:02
 */
package org.darwin.genericDao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.darwin.genericDao.annotations.enums.Type;

/**
 * 统计数据表中的列类型
 * created by Tianxin on 2015年6月3日 下午1:48:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface StatType{
	
	/**
	 * 该字段对应的统计类型，默认是sum类型
	 * @return
	 * created by Tianxin on 2015年6月3日 下午1:54:15
	 */
	Type value() default Type.SUM;
	
}
