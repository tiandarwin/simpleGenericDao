/**
 * org.darwin.genericDao.param.NotParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:07:42
 */
package org.darwin.genericDao.param;

import java.util.Collection;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年5月27日 下午2:07:42
 */
public class NotParam extends NormalParam implements Param {

	/**
	 * @param value
	 */
	public NotParam(Object value) {
		super(value);
	}

	public String buildOperate(String column) {
		if (value == null) {
			return Utils.connect(column, " is not null");
		} else if (value instanceof Collection) {
			return buildCollectionOperate(column);
		} else if (value.getClass().isArray()) {
			return buildArrayOperate(column);
		} else {
			return Utils.connect(column, " != ?");
		}
	}

	private String buildCollectionOperate(String column) {
		Collection<?> coll = (Collection<?>) value;
		if (coll.size() == 1) {
			return Utils.connect(column, " != ?");
		} else {
			return Utils.connect(column, " not in ", buildInOperate(coll.size()));
		}
	}

	private String buildArrayOperate(String column) {
		Object[] array = (Object[]) value;
		if (array.length == 1) {
			return Utils.connect(column, " != ?");
		} else {
			return Utils.connect(column, " not in ", buildInOperate(array.length));
		}
	}
}
