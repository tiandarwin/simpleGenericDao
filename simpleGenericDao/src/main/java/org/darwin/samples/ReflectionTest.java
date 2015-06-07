/**
 * org.darwin.samples.ReflectionTest.java
 * created by Tianxin(tianjige@163.com) on 2015年6月4日 下午9:07:50
 */
package org.darwin.samples;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * created by Tianxin on 2015年6月4日 下午9:07:50
 */
public class ReflectionTest {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		long times = 10000000l;
		run(times);
		runReflection(times);
	}

	/**
	 * @param times
	 *            created by Tianxin on 2015年6月4日 下午9:09:32
	 */
	private static void run(long times) {
		long start = System.currentTimeMillis();
		for (long i = 0; i < times; i++) {
			new ReflectionTest().setId(10);
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * @param times
	 *            created by Tianxin on 2015年6月4日 下午9:09:32
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private static void runReflection(long times) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		
		Method m = ReflectionTest.class.getDeclaredMethod("setId", Integer.TYPE);
		long start = System.currentTimeMillis();
		for (long i = 0; i < times; i++) {
			m.invoke(ReflectionTest.class.newInstance(), 10);
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;
}
