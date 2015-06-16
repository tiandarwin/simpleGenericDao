/**
 * org.darwin.genericDao.query.Matches.java
 * created by Tianxin(tianjige@163.com) on 2015年5月26日 下午9:27:00
 */
package org.darwin.genericDao.operate;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Tianxin on 2015年5月26日 下午9:27:00
 */
public class Matches implements Operate {

	/**
	 * 返回一个条件匹配集，与and()返回的结果相同
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午2:55:34
	 */
	public static Matches init() {
		return and();
	}

	/**
	 * 返回一个以and为连接符的匹配集
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午2:54:16
	 */
	public static Matches and() {
		return new Matches();
	}

	/**
	 * 返回一个以or为连接符的匹配集
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午2:54:40
	 */
	public static Matches or() {
		return new Matches(OR);
	}

	/**
	 * 返回一个空的匹配集合
	 * 
	 * @return created by Tianxin on 2015年5月27日 下午2:57:22
	 */
	public static Matches empty() {
		return init();
	}

	public static Matches one(String column, Object value) {
		return Matches.and().match(column, value);
	}

	private final static String AND = " and ";
	private final static String OR = " or ";

	private String conj = AND;
	private List<Operate> matches = new ArrayList<Operate>(2);

	private Matches() {
	}

	private Matches(String conj) {
		this.conj = conj;
	}

	/**
	 * 向匹配集中增加一个匹配关系
	 * 
	 * @param column
	 * @param value
	 * @return created by Tianxin on 2015年5月27日 下午3:09:27
	 */
	public Matches match(String column, Object value) {
		matches.add(new Match(column, value));
		return this;
	}

	/**
	 * 向匹配集中增加另一个匹配集
	 * 
	 * @param instance
	 * @return created by Tianxin on 2015年5月27日 下午3:09:40
	 */
	public Matches addMatches(Matches instance) {
		if (instance.matches.size() == 1) {
			this.matches.add(instance.matches.get(0));
		} else {
			this.matches.add(instance);
		}
		return this;
	}

	public String getOperate() {
		if (matches.size() == 1) {
			return matches.get(0).getOperate();
		}

		StringBuilder sb = new StringBuilder(matches.size() * 12);
		for (Operate operate : matches) {
			if (operate instanceof Matches) {
				sb.append('(').append(operate.getOperate()).append(')').append(conj);
			} else {
				sb.append(operate.getOperate()).append(conj);
			}
		}
		sb.delete(sb.length() - conj.length(), sb.length());
		return sb.toString();
	}

	public List<Object> getParams() {
		List<Object> params = new ArrayList<Object>(matches.size() * 2);
		for (Operate match : matches) {
			params.addAll(match.getParams());
		}
		return params;
	}

	public boolean isEmpty() {
		return matches.size() == 0;
	}
}
