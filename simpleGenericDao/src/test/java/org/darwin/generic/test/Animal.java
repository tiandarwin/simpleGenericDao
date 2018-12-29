package org.darwin.generic.test;

import org.darwin.genericDao.annotations.UseQueryColumnFormat;
import org.darwin.genericDao.annotations.enums.QueryColumnFormat;

/**
 * @author hexiufeng
 * @date 2018/12/28下午7:16
 */
public abstract class Animal {
  private final QueryColumnFormat sqlColumnStyle;

  protected Animal(){
    this.sqlColumnStyle = extractSQLColumnStyle();
  }

  private QueryColumnFormat extractSQLColumnStyle() {
    Class<?> clazz = this.getClass();

    while (!clazz.equals(Animal.class)) {
      UseQueryColumnFormat usingSQLColumnStyle =  clazz.getAnnotation(UseQueryColumnFormat.class);
      if(usingSQLColumnStyle != null){
        return usingSQLColumnStyle.value();
      }
      clazz = clazz.getSuperclass();
    }

    return null;
  }

  public void show() {
    System.out.println(this.sqlColumnStyle);
  }
}
