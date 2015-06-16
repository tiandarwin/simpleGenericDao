/**
 * org.darwin.genericDao.annotations.Column.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:28:31
 */
package org.darwin.genericDao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个Field所对应的数据库的字段名的注解
 * created by Tianxin on 2015年5月26日 下午8:28:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Column {

    /**
     * 数据库对应的column
     * 
     * @return 数据库对应的column
     */
    String value();

    /**
     * 该字段是否被修改
     * 
     * @return 上午10:15:15 created by Darwin(Tianxin)
     */
    boolean modifiable() default true;
}
