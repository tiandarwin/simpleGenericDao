/**
 * org.darwin.tools.CreateTableHelper.java
 * created by Tianxin(tianjige@163.com) on 2015年8月25日 下午5:52:12
 */
package org.darwin.tools;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.darwin.common.GenericDaoUtils;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.mapper.ColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <br/>created by Tianxin on 2015年8月25日 下午5:52:12
 */
public class CreateTableHelper {

  /**
   * 默认生成的该类的LOG记录器，使用slf4j组件。避免产生编译警告，使用protected修饰符。
   */
  protected final static Logger LOG = LoggerFactory.getLogger(CreateTableHelper.class);

  public static void generateCreateTableSQL(Class<?> entityClass) {

    Table table = GenericDaoUtils.getTable(entityClass);
    System.out.println("use " + table.db() + ";");
    System.out.println("create table " + table.name() + "(");
    Map<String, ColumnMapper> mappers =
        GenericDaoUtils.generateColumnMappers(entityClass, table == null ? ColumnStyle.JAVA_TO_MYSQL : table.columnStyle());
    
    for (Entry<String, ColumnMapper> entry : mappers.entrySet()) {
      String column = entry.getKey();

      if (column.equals(table.keyColumn()) && BaseObject.class.isAssignableFrom(entityClass)) {
        Class<?> type = GenericDaoUtils.getGenericEntityClass(entityClass, BaseObject.class, 0);
        System.out.println("  " + column + " " + getDefaultSQLType(type) + " not null comment '',");
      } else {
        Class<?> type = entry.getValue().getGetter().getReturnType();
        System.out.println("  " + column + " " + getDefaultSQLType(type) + " not null comment '',");
      }
    }
    System.out.println("  primary key(" + table.keyColumn() + ")");
    System.out.println(")ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '';");
  }

  /**
   * @param type
   * @return
   * <br/>created by Tianxin on 2015年8月25日 下午5:59:10
   */
  private static String getDefaultSQLType(Class<?> type) {
    if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
      return "int(10)";
    } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
      return "bigint(20)";
    } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
      return "int(10)";
    } else if (type.equals(String.class)) {
      return "varchar(32)";
    } else if (type.equals(Date.class)) {
      return "timestamp";
    }


    throw new RuntimeException("找不到匹配的类型");
  }
}
