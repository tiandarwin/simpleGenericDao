/**
 * org.darwin.genericDao.query.QueryUpdate.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午1:51:51
 */
package org.darwin.genericDao.query;

import java.util.List;

import org.darwin.genericDao.operate.Matches;
import org.darwin.genericDao.operate.Modifies;

/**
 * 这里的QueryModify意为修改某几个字段
 * created by Tianxin on 2015年5月27日 下午1:51:51
 */
public class QueryModify implements Query {
	
	private QueryModify() {
	}
	
	/**
	 * 构造一个modify的Query
	 * @param matches
	 * @param table
	 */
	public QueryModify(Modifies modifies, Matches matches, String table) {
		this();
		this.modifies = modifies;
		this.matches = matches;
		this.table = table;
	}
	
	private Modifies modifies;
	private Matches matches;
	private String table;

	public String getSQL() {
		StringBuilder sb = new StringBuilder(512);
		sb.append("update ").append(table).append(" set ");
		sb.append(modifies.getOperate());
		if(matches != null && !matches.isEmpty()){
			sb.append(" where ").append(matches.getOperate());
		}
		return sb.toString();
	}

	public Object[] getParams() {
		List<Object> params = modifies.getParams();
		if(matches != null && !matches.isEmpty()){
			params.addAll(matches.getParams());
		}
		return params.toArray();
	}
}
