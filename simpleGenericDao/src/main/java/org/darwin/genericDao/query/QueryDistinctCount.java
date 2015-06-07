/**
 * org.darwin.genericDao.dao.impl.QueryDistinctCount.java
 * created by Tianxin(tianjige@163.com) on 2015年6月7日 下午2:06:29
 */
package org.darwin.genericDao.query;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.operate.Matches;


/**
 * 做一个distinctCount的query
 * created by Tianxin on 2015年6月7日 下午2:06:29
 */
public class QueryDistinctCount implements Query {
	
	private Matches matches = null;
	private String table = null;
	private String[] targetColumns = null;

	/**
	 * 构造函数
	 * @param matches
	 * @param targetColumns
	 */
	public QueryDistinctCount(String table, Matches matches, String...targetColumns) {
		this.table = table;
		this.matches = matches;
		this.targetColumns = targetColumns;
	}

	public String getSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct ");
		
		for(String column : targetColumns){
			sb.append(column).append(',');
		}
		sb.setCharAt(sb.length(), ')');
		sb.append(" from ").append(table);
		
		if (matches != null && !matches.isEmpty()) {
			sb.append(" where ").append(matches.getOperate());
		}
		return sb.toString();
	}

	public Object[] getParams() {
		return Utils.trans2Array(matches.getParams());
	}

}
