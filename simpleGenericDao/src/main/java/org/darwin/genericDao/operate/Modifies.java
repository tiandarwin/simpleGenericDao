/**
 * org.darwin.genericDao.query.Modifies.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:08:05
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Tianxin on 2015年5月27日 下午12:08:05
 */
public class Modifies implements Operate {

	/**
	 * 返回一个修改操作集对象 created by Tianxin on 2015年5月27日 下午2:44:50
	 */
	public static Modifies init() {
		return new Modifies();
	}
	
	/**
	 * 初始化一个修改动作对象
	 * @param column
	 * @param value
	 * @return
	 * created by Tianxin on 2015年6月15日 下午2:29:00
	 */
	public static Modifies one(String column, Object value){
	  return new Modifies().modify(column, value);
	}

	private Modifies() {
	}

	/**
	 * 向修改操作集中增加一个修改操作
	 * 
	 * @param column
	 *            要修改的字段
	 * @param value
	 *            目标值
	 * @return 对象自身 created by Tianxin on 2015年5月27日 下午2:47:55
	 */
	public Modifies modify(String column, Object value) {
		modifies.add(new Modify(column, value));
		return this;
	}

	/**
	 * 存储修改操作的一个集合
	 */
	private List<Modify> modifies = new ArrayList<Modify>();

	public String getOperate() {
		StringBuilder sb = new StringBuilder(modifies.size() * 10);
		for (Modify modify : modifies) {
			sb.append(modify.getOperate()).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public List<Object> getParams() {
		List<Object> params = new ArrayList<Object>(modifies.size() * 2);
		for (Modify modify : modifies) {
			params.addAll(modify.getParams());
		}
		return params;
	}

	public boolean isEmpty() {
		return modifies.size() == 0;
	}

}
