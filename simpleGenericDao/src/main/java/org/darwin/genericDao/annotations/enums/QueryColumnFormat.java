package org.darwin.genericDao.annotations.enums;

/**
 * 在使用sql拼接器拼接sql时,用于判断传入的字段名是原生数据库表字段名还是POJO的field名称
 *
 * @author hexiufeng
 * @date 2018/12/28下午3:19
 */
public enum QueryColumnFormat {
  DB_COLUMN_NAME,POJO_FIELD_NAME,MIX;
}
