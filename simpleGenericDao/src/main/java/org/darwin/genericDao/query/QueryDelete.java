/**
 * org.darwin.genericDao.query.QueryDelete.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:51:30
 */
package org.darwin.genericDao.query;

import java.util.List;

import org.darwin.genericDao.operate.Matches;

/**
 * created by Tianxin on 2015年5月27日 下午1:51:30
 */
public class QueryDelete implements Query {
	
	private QueryDelete() {
	}
	
	/**
	 * 构造一个delete的Query
	 * @param matches
	 * @param table
	 */
	public QueryDelete(Matches matches, String table) {
		this();
		this.matches = matches;
		this.table = table;
	}

	private Matches matches;
	private String table;

	public String getSQL() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("delete from ").append(table);
		
		if(matches != null && !matches.isEmpty()){
			sb.append(" where ").append(matches.getOperate());
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

}
