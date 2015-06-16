/**
 * org.darwin.genericDao.param.NormalParam.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午2:06:01
 */
package org.darwin.genericDao.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.darwin.common.utils.Utils;

/**
 * created by Tianxin on 2015年5月27日 下午2:06:01
 */
public class NormalParam implements Param {

	/**
	 * 一个普通的参数匹配
	 */
	protected Object value = null;

	protected NormalParam() {
	}
	/**
	 * @param value
	 */
	public NormalParam(Object value) {
		if (value == null) {
		} else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			if (coll.size() == 1) {
				this.value = coll.iterator().next();
				return;
			}
		} else if (value.getClass().isArray()) {
			Object[] array = trans2ObjectArray(value);
			if (array.length == 1) {
				this.value = array[0];
			} else {
				this.value = array;
			}
			return;
		}
		this.value = value;
	}

	/**
	 * 将src转换为一个对象数组
	 * 
	 * @param src
	 * @return created by Tianxin on 2015年5月27日 下午5:07:15
	 */
	private static Object[] trans2ObjectArray(Object src) {
		Object[] os = null;
		Class<?> type = src.getClass().getComponentType();

		if (!type.isPrimitive()) {
			os = (Object[]) src;
		} else if (type.equals(Integer.TYPE)) {
			int[] array = (int[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Long.TYPE)) {
			long[] array = (long[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Float.TYPE)) {
			float[] array = (float[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Short.TYPE)) {
			short[] array = (short[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Double.TYPE)) {
			double[] array = (double[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Character.TYPE)) {
			char[] array = (char[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Boolean.TYPE)) {
			boolean[] array = (boolean[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else if (type.equals(Byte.TYPE)) {
			byte[] array = (byte[]) src;
			os = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				os[i] = array[i];
			}
		} else {
			throw new RuntimeException("unknown array type, please report to the author");
		}
		return os;
	}

	public List<Object> getParams() {
		if (value == null) {
			return Collections.emptyList();
		} else if (value instanceof Collection) {
			return getCollectionParams();
		} else if (value.getClass().isArray()) {
			return getArrayParams();
		} else {
			return Arrays.asList(value);
		}
	}

	/**
	 * @return created by Tianxin on 2015年5月27日 下午4:03:14
	 */
	protected List<Object> getArrayParams() {
		Object[] array = (Object[]) value;
		if (array.length == 0) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(array);
		}
	}

	/**
	 * @return created by Tianxin on 2015年5月27日 下午3:58:52
	 */
	protected List<Object> getCollectionParams() {
		Collection<?> coll = (Collection<?>) value;
		if (coll.size() == 0) {
			return Collections.emptyList();
		} else {
			return new ArrayList<Object>(coll);
		}
	}

	public String buildOperate(String column) {
		if (value == null) {
			return Utils.connect(column, " is null");
		} else if (value instanceof Collection) {
			return buildCollectionOperate(column);
		} else if (value.getClass().isArray()) {
			return buildArrayOperate(column);
		} else {
			return Utils.connect(column, "=?");
		}
	}

	/**
	 * @param column
	 * @return created by Tianxin on 2015年5月27日 下午4:01:00
	 */
	private String buildArrayOperate(String column) {
		Object[] array = (Object[]) value;
		if (array.length == 0) {
			return "1 != 1";
		} else if (array.length == 1) {
			return Utils.connect(column, "=?");
		} else {
			return Utils.connect(column, " in ", buildInOperate(array.length));
		}
	}

	/**
	 * value是集合类如何处理
	 * 
	 * @param column
	 * @return created by Tianxin on 2015年5月27日 下午3:57:43
	 */
	private String buildCollectionOperate(String column) {
		Collection<?> coll = (Collection<?>) value;
		if (coll.size() == 0) {
			return "1 != 1";
		} else if (coll.size() == 1) {
			return Utils.connect(column, "=?");
		} else {
			return Utils.connect(column, " in ", buildInOperate(coll.size()));
		}
	}

	/**
	 * @param size
	 * @return created by Tianxin on 2015年5月27日 下午3:20:08
	 */
	protected String buildInOperate(int size) {
		StringBuilder sb = new StringBuilder(size * 3);
		sb.append('(');
		for (int i = 0; i < size; i++) {
			sb.append("?,");
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}
}
