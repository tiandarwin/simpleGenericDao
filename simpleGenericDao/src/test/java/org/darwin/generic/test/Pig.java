package org.darwin.generic.test;

import org.darwin.genericDao.annotations.UseQueryColumnFormat;
import org.darwin.genericDao.annotations.enums.QueryColumnFormat;

/**
 * @author hexiufeng
 * @date 2018/12/28下午7:16
 */
@UseQueryColumnFormat(QueryColumnFormat.POJO_FIELD_NAME)
public class Pig extends Animal {

}
