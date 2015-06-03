/**
 * org.darwin.genericDao.operate.Groups.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午3:28:48
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by Tianxin on 2015年6月3日 下午3:28:48
 */
public class Groups implements Operate {

	private Groups() {
	}

	public String getOperate() {
		StringBuilder sb = new StringBuilder(groups.size() * 10);
		for (String group : groups) {
			sb.append(group).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public List<Object> getParams() {
		return Collections.emptyList();
	}

	public boolean isEmpty() {
		return groups == null || groups.size() == 0;
	}

	/**
	 * 初始化一个GroupBy的字段集
	 * @return
	 * created by Tianxin on 2015年6月3日 下午3:37:16
	 */
	public static Groups init() {
		return new Groups();
	}

	/**
	 * 生产一个GroupBy的字段集
	 * @param column
	 * @return
	 * created by Tianxin on 2015年6月3日 下午3:37:00
	 */
	public static Groups one(String column) {
		return init().groupBy(column);
	}

	/**
	 * 添加groupBy的字段
	 * @param column
	 *            created by Tianxin on 2015年6月3日 下午3:35:42
	 */
	public Groups groupBy(String column) {
		groups.add(column);
		return this;

	}

	private List<String> groups = new ArrayList<String>();

}
