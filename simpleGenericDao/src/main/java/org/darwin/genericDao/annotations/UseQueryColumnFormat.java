package org.darwin.genericDao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.darwin.genericDao.annotations.enums.QueryColumnFormat;

/**
 * 用在dao层,用于声明当前dao中拼接sql时(使用Match/Matches)使用数据库表字段名还是使用Pojo filed名称来拼接。
 *
 * <p>
 *   使用Match进行条件设置时需要传入字段名称，之前版本的设计是需要传入数据库表字段的名称，这可能有两个问题:<br>
 *     <ul>
 *       <li>这需要暴露数据库信息到dao层</li>
 *       <li>容易拼错，因为java习惯于驼峰</li>
 *     </ul>
 *    这个版本支持自由的配置成你喜欢的方式: 数据库字段名称，Pojo field名称，及二者混合，seee {@link QueryColumnFormat}.
 * </p>
 *
 * 当Dao没有配置该注解时，使用数据库字段名称，这是为了兼容之前版本
 *
 * @author hexiufeng
 * @date 2018/12/28下午6:53
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseQueryColumnFormat {
  QueryColumnFormat value();
}
