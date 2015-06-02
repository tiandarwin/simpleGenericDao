/**
 * org.darwin.genericDao.query.Orders.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午12:08:30
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by Tianxin on 2015年5月27日 下午12:08:30
 */
public class Orders implements Operate {
	
	/**
	 * 初始化一个排序集对象
	 * created by Tianxin on 2015年5月27日 下午2:43:04
	 */
	public static Orders init(){
		return new Orders();
	}
	
	/**
	 * 初始化一个空的排序集对象
	 * created by Tianxin on 2015年5月27日 下午2:43:04
	 */
	public static Orders empty(){
		return init();
	}
	
	/**
	 * 存储排序集合中的自排序列表
	 */
	private List<Order> orders = new ArrayList<Order>(3);
	
	/**
	 * 在排序集中增加一个column的正向排序
	 * created by Tianxin on 2015年5月27日 下午2:43:46
	 */
	public Orders addAsc(String column){
		orders.add(Order.asc(column));
		return this;
	}
	
	/**
	 * 在排序集中增加一个column的反向排序
	 * created by Tianxin on 2015年5月27日 下午2:44:01
	 */
	public Orders addDesc(String column){
		orders.add(Order.desc(column));
		return this;
	}

	public String getOperate() {
		StringBuilder sb = new StringBuilder(10 * orders.size());
		for(Order order : orders){
			sb.append(order.getOperate()).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public List<Object> getParams() {
		return Collections.emptyList();
	}

	public boolean isEmpty() {
		return orders.size() == 0;
	}

	/**
	 * @param column
	 * @param asc
	 * @return
	 * created by Tianxin on 2015年5月27日 下午6:22:33
	 */
	public static Orders one(String column, boolean asc) {
		return asc ? Orders.init().addAsc(column) :  Orders.init().addDesc(column);
	}
}
