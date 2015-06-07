/**
 * org.darwin.genericDao.mapper.TableGenerator.java
 * created by Tianxin(tianjige@163.com) on 2015年5月27日 下午7:57:31
 */
package org.darwin.genericDao.dao.impl;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.Table;

/**
 * created by Tianxin on 2015年5月27日 下午7:57:31
 */
public class AnnotationConfigKeeper {
	
	//私有化构造函数
	private AnnotationConfigKeeper() {
	}
	
	/**
	 * @param table
	 * @param sequence
	 */
	public AnnotationConfigKeeper(Table table, Sequence sequence) {
		this();
		this.table = table;
		this.sequence = sequence;
	}

	private Table table;
	private Sequence sequence;
	//TODO Sequence逻辑尚未处理

	public String table(){
		//TODO表名生成逻辑，这里的生成逻辑需要外部能够注入
		if(Utils.isEmpty(table.db())){
			return table.name();
		}
		return Utils.connect(table.db(), '.', table.name());
	}

	/**
	 * 获取该ENTITY的主键列名
	 * @return
	 * created by Tianxin on 2015年5月27日 下午8:38:03
	 */
	public String keyColumn() {
		return table.keyColumn();
	}
	
	/**
	 * 判断是否有sequence的配置，如果没有sequence的配置，则认为是autoIncrement获取key
	 * @return
	 * created by Tianxin on 2015年6月1日 下午2:49:55
	 */
	public boolean hasNoSequence(){
		return sequence == null;
	}
}
