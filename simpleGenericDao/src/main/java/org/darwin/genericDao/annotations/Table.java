/**
 * org.darwin.genericDao.annotations.Table.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午8:28:45
 */
package org.darwin.genericDao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.darwin.genericDao.annotations.enums.ColumnStyle;


/**
 * 标记一个bo对应着数据库中哪个table的注解
 * created by Tianxin on 2015年5月26日 下午8:28:45
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

  /**
   * 在哪个DB中
   * 
   * @return
   */
  String db();

  /**
   * 表名 添加默认值方便<code>@Inherited</code>后零注解
   * 
   * @return
   */
  String name();

  /**
   * 该表做了多少个切片拆分，0-1为无分片
   * 
   * @return 表的分片数
   */
  int shardCount() default 0;

  /**
   * 主键字段的名字，默认为<code>id</code>，复合主键时，则用逗号“,”分割
   * 
   * @return 主键字段的名字
   */
  String keyColumn() default "id";

  /**
   * 这里感觉意义不大
   * 该表的主键是否是自动生成
   * @return
   * created by Tianxin on 2015年6月1日 下午2:57:20
  boolean audoIncrementKey() default false;
   */

  /**
   * 对象属性到数据库列的映射规则，默认为 {@link ColumnStyle#LOWER_CASE}向前兼容
   * 
   * @return 映射规则 @see ColumnStyle
   */
  ColumnStyle columnStyle() default ColumnStyle.JAVA_TO_MYSQL;
}
