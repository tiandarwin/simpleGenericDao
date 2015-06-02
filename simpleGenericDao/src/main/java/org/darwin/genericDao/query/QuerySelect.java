/**
 * org.darwin.genericDao.query.Query.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:46:17
 */
package org.darwin.genericDao.query;

import java.util.List;

import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Orders;

/**
 * 一个select类型的Query描述
 * created by Tianxin on 2015年5月27日 下午1:46:17
 */
public class QuerySelect implements Query{
	
	private QuerySelect(){
	}
	
	/**
	 * 构造一个Select的Query对象
	 * @param columns
	 * @param matches
	 * @param orders
	 * @param table
	 */
	public QuerySelect(List<String> columns, Matches matches, Orders orders,
			String table) {
		this();
		this.columns = columns;
		this.matches = matches;
		this.orders = orders;
		this.table = table;
	}
	/**
	 * 构造一个Select的Query对象
	 * @param columns
	 * @param matches
	 * @param orders
	 * @param table
	 * @param offset
	 * @param rows
	 */
	public QuerySelect(List<String> columns, Matches matches, Orders orders,
			String table, int offset, int rows) {
		this(columns, matches, orders, table);
		this.offset = offset;
		this.rows = rows;
	}
	private List<String> columns;
	private Matches matches;
	private Orders orders;
	private String table;
	private int offset = 0;
	private int rows = 0;
	
	public String getSQL() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("select ").append(buildColumns()).append(" from ").append(table);
		if(matches != null && !matches.isEmpty()){
			sb.append(" where ").append(matches.getOperate());
		}
		if(orders != null && !orders.isEmpty()){
			sb.append(" order by ").append(orders.getOperate());
		}
		
		if(rows != 0){
			sb.append(" limit ").append(offset).append(',').append(rows);
		}
		return sb.toString();
	}
	
	public Object[] getParams() {
		if(matches == null){
			return new Object[0];
		}else{
			List<Object> params = matches.getParams();
			return params.toArray();
		}
	}
	
	public List<String> getColumns(){
		return columns;
	}
	
	private String buildColumns(){
		StringBuilder sb = new StringBuilder(columns.size() * 8);
		for(String column : columns){
			sb.append(column).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
