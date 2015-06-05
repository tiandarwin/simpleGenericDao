/**
 * org.darwin.genericDao.mapper.stat.StatAnnotationKeeper.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午2:30:28
 */
package org.darwin.genericDao.mapper.stat;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.annotations.stat.StatTable;

/**
 * created by Tianxin on 2015年6月3日 下午2:30:28
 */
public class StatAnnotationKeeper {
	
	/**
	 * 统计表的annotation
	 */
	private StatTable table;


	//无参构造函数私有化
	private StatAnnotationKeeper() {
	}

	/**
	 * 构造一个注解持有者
	 * @param table
	 */
	public StatAnnotationKeeper(StatTable table) {
		this();
		this.table = table;
	}
	

	public String table(){
		//TODO表名生成逻辑，这里的生成逻辑需要外部能够注入
		return Utils.connect(table.db(), '.', table.name());
	}

}
