package org.darwin.samples.test;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * .TestGenericTypes.java
 * created by Tianxin(tianjige@163.com) on 2015年5月29日 下午9:42:29
 */

/**
 * created by Tianxin on 2015年5月29日 下午9:42:29
 */
public class TestGenericTypes extends Middle<Integer, Object> {
	
	public static void main(String[] args) {
		paramize(TestGenericTypes.class.getSuperclass());
	}
	
	public static void paramize(Class<?> clazz){
		Type type = clazz.getGenericSuperclass();
		if(type == null){
			System.out.println(clazz.toString() + " null ");
			return;
		}
		
		System.out.println(type instanceof ParameterizedType);
		Type [] types = ((ParameterizedType)type).getActualTypeArguments();
		System.out.println(types[0].toString());
	}

}
