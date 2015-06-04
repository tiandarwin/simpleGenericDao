/**
 * org.darwin.genericDao.annotations.ColumnStyle.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午2:11:11
 */
package org.darwin.genericDao.annotations.enums;

/**
 * 格式转换，对象属性到数据库列的映射规则
 * 
 * create by Tianxin on 2014-11-30 下午4:20:28
 */
public enum ColumnStyle {

    /**
     * 转变为小写，即userId到数据库中映射为userid
     */
    LOWER_CASE {
        @Override
        public String convert(String field) {
            return field.toLowerCase();
        }
    },
    
    /**
     * 驼峰转为mysql规范，即userId到数据库中映射为user_id
     */
    JAVA_TO_MYSQL {
        @Override
        public String convert(String field) {
        	StringBuilder sb = new StringBuilder(field.length() * 2);
        	for(int i = 0 ; i < field.length(); i ++){
        		char c = field.charAt(i);
        		if(c >= 'A' && c <= 'Z'){
        			sb.append('_').append((char)(c + 32));
        		} else {
					sb.append(c);
				}
        	}
            return sb.toString();
        }
    };

    /**
     * 将<code>field</code>格式进行转换
     * 
     * @param field JAVA对象属性
     * @return 数据库表的列
     */
    public abstract String convert(String field);

}
