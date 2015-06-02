/**
 * org.darwin.genericDao.annotations.Sequence.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:29:32
 */
package org.darwin.genericDao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记着一个bo使用Sequence序列的注解
 * created by Tianxin on 2015年5月26日 下午8:29:32
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sequence {

    /**
     * sequence的名字
     * 
     * @return 下午1:22:37 created by Darwin(Tianxin)
     */
    String name();

    /**
     * 该Sequence每次缓存多少个ID
     * 
     * @return 下午1:22:49 created by Darwin(Tianxin)
     */
    int size() default 1000;
}
