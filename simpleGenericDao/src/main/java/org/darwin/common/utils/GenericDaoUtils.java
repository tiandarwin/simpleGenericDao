/**
 * org.darwin.common.utils.ReflectionUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午10:00:11
 */
package org.darwin.common.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.annotations.Table.ColumnStyle;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * 反射的方法工具集 created by Tianxin on 2015年5月28日 下午10:00:11
 */
public class GenericDaoUtils {

	/**
	 * 获取GenericDao的子类所操作的Entity的class
	 * 
	 * @param subGenericDaoClass
	 * @return created by Tianxin on 2015年5月31日 下午10:51:37
	 */
	@SuppressWarnings("unchecked")
	public static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Class<ENTITY> getEntityClass(
			Class<?> subGenericDaoClass) {

		// TODO 这个方法里面有一些漏洞，应该从GenericDao类向下追溯，根据语法规则找到准确的语法指定的泛型，而不是底向上回溯

		// 如果是objec则直接返回null
		if (subGenericDaoClass.equals(Object.class)) {
			return null;
		}

		// 如果泛型父类为空，则返回null
		Type superClass = subGenericDaoClass.getGenericSuperclass();
		if (superClass != null && superClass instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) superClass;
			Type[] types = paramType.getActualTypeArguments();
			for (Type type : types) {

				// 如果是占位泛型，则跳过
				if (!(type instanceof Class)) {
					continue;
				}

				// 如果是BaseObject的子类，则直接返回即可
				Class<?> typeClass = (Class<?>) type;
				boolean isBaseObjectClass = BaseObject.class
						.isAssignableFrom(typeClass);
				if (isBaseObjectClass) {
					return (Class<ENTITY>) typeClass;
				}
			}
		}

		// 如果本类没有，则获取其泛型父类
		return getEntityClass(subGenericDaoClass.getSuperclass());
	}

	/**
	 * 获取entityClass的key的class
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年5月31日 下午10:51:37
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <KEY extends Serializable> Class<KEY> getKeyClass(
			Class entityClass) {

		// TODO 这个方法里面有一些漏洞，应该从GenericDao类向下追溯，根据语法规则找到准确的语法指定的泛型，而不是底向上回溯

		// 如果是objec则直接返回null
		if (entityClass.equals(Object.class)) {
			return null;
		}

		// 如果泛型父类为空，则返回null
		Type superClass = entityClass.getGenericSuperclass();
		if (superClass != null && superClass instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) superClass;
			Type[] types = paramType.getActualTypeArguments();
			for (Type type : types) {

				// 如果是占位泛型，则跳过
				if (!(type instanceof Class)) {
					continue;
				}

				// 如果是BaseObject的子类，则直接返回即可
				Class<?> typeClass = (Class<?>) type;
				boolean isKeyClass = Serializable.class
						.isAssignableFrom(typeClass);
				if (isKeyClass) {
					return (Class<KEY>) typeClass;
				}
			}
		}

		// 如果本类没有，则获取其泛型父类
		return getKeyClass(entityClass.getSuperclass());
	}

	/**
	 * 获取实体类中的Table的注解
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午7:07:12
	 */
	public static Table getTable(Class<?> entityClass) {
		if (entityClass.equals(Object.class)) {
			return null;
		}
		Table table = entityClass.getAnnotation(Table.class);
		if (table == null) {
			return getTable(entityClass.getSuperclass());
		} else {
			return table;
		}
	}

	/**
	 * 获取实体类中的Sequence注解
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午7:07:34
	 */
	public static Sequence getSequence(Class<?> entityClass) {
		if (entityClass.equals(Object.class)) {
			return null;
		}
		Sequence sequence = entityClass.getAnnotation(Sequence.class);
		if (sequence == null) {
			return getSequence(entityClass.getSuperclass());
		} else {
			return sequence;
		}
	}

	/**
	 * 获取entityClass中与DB的所有字段映射关系
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午10:26:05
	 */
	public static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> Map<String, ColumnMapper> generateColumnMappers(
			Class<ENTITY> entityClass, ColumnStyle columnStyle) {

		// 获取get方法、set方法、属性的列表
		List<Field> fields = getAllFields(entityClass);
		List<Method> getters = getAllGetters(entityClass);
		List<Method> setters = getAllSetters(entityClass);

		// 将get方法、set方法转换为映射关系，key为名称.参数类型
		Map<String, Field> fieldMap = trans2FieldMap(fields);
		Map<String, Method> setterMap = trans2SetterMap(setters);

		// 以getter方法做循环，找到配对的方法和属性信息
		Map<String, ColumnMapper> columnMappers = new HashMap<String, ColumnMapper>(
				50);
		for (Method getter : getters) {

			// 推导对一个的setter名字与field名字
			String getterName = getter.getName() + "."
					+ getter.getReturnType().getName();
			String setterName = generateSetterKeyFromGetter(getterName);
			String fieldName = generateFieldKeyFromSetter(setterName);

			// 获取setter方法与field
			Method setter = setterMap.get(setterName);
			Field field = fieldMap.get(fieldName);

			// 如果没有setter或getter与setter类型不匹配，则跳过
			if (setter == null
					|| !getter.getReturnType().equals(
							setter.getParameterTypes()[0])) {
				continue;
			}

			Column column = fetchColumn(field, getter, setter, entityClass);
			ColumnMapper columnMapper = new ColumnMapper(getter, setter,
					column, columnStyle);
			columnMappers.put(columnMapper.getColumn(), columnMapper);
		}

		// 注册到BasicMapper中
		BasicMappers.register(entityClass, columnMappers);
		return columnMappers;
	}

	/**
	 * 从field、getter、setter中获取annotation。如果不存在，则向上回溯获取。
	 * 
	 * @param field
	 * @param getter
	 * @param setter
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午11:44:30
	 */
	private static Column fetchColumn(Field field, Method getter,
			Method setter, Class<?> entityClass) {

		// 如果field在当前类中声明，且field中有Column的注解，则返回该Column注解
		if (field != null && field.getDeclaringClass().equals(entityClass)) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				return column;
			}
		}

		// 如果getter在当前类中声明，且getter中有Column的注解，则返回该Column注解
		if (getter != null && getter.getDeclaringClass().equals(entityClass)) {
			Column column = getter.getAnnotation(Column.class);
			if (column != null) {
				return column;
			}
		}

		// 如果setter在当前类中声明，且setter中有Column的注解，则返回该Column注解
		if (setter != null && setter.getDeclaringClass().equals(entityClass)) {
			Column column = setter.getAnnotation(Column.class);
			if (column != null) {
				return column;
			}
		}

		// 如果当前类中不存在，则向上追溯
		Class<?> superClass = entityClass.getSuperclass();
		if (superClass.equals(Object.class)) {
			return null;
		}

		// 追溯父类中的信息
		try {
			Field superField = superClass.getField(field.getName());
			field = superField == null ? field : superField;

			Method superGetter = superClass.getMethod(getter.getName(),
					getter.getParameterTypes());
			getter = superGetter == null ? getter : superGetter;

			Method superSetter = superClass.getMethod(setter.getName(),
					setter.getParameterTypes());
			setter = superSetter == null ? setter : superSetter;
		} catch (Exception e) {
		}

		return fetchColumn(field, getter, setter, superClass);
	}

	/**
	 * 根据getter名字推导field的名字
	 * 
	 * @param setterName
	 * @return created by Tianxin on 2015年6月1日 上午11:26:30
	 */
	private static String generateFieldKeyFromSetter(String setterName) {
		StringBuilder sb = new StringBuilder(setterName.substring(3));
		sb.setCharAt(0, (char) (sb.charAt(0) + 32));
		return sb.toString();
	}

	/**
	 * 根据getter名字推导setter的名字
	 * 
	 * @param getterName
	 * @return created by Tianxin on 2015年6月1日 上午11:25:45
	 */
	private static String generateSetterKeyFromGetter(String getterName) {

		if (getterName.startsWith("is")) {
			return "set" + getterName.substring(2);
		}

		StringBuilder sb = new StringBuilder(getterName);
		sb.setCharAt(0, 's');
		return sb.toString();
	}

	/**
	 * 转换成名字与对象的映射，子类优先
	 * 
	 * @param methods
	 * @return created by Tianxin on 2015年6月1日 上午11:13:56
	 */
	private static Map<String, Method> trans2SetterMap(List<Method> methods) {

		if (methods == null || methods.size() == 0) {
			return Collections.emptyMap();
		}

		Map<String, Method> methodMap = new HashMap<String, Method>(
				methods.size() * 2);
		for (Method m : methods) {
			String name = m.getName() + "."
					+ m.getParameterTypes()[0].getName();
			if (!methodMap.containsKey(name)) {
				methodMap.put(name, m);
			}
		}
		return methodMap;
	}

	/**
	 * 转换成名字与对象的映射，子类优先
	 * 
	 * @param fields
	 * @return created by Tianxin on 2015年6月1日 上午11:13:56
	 */
	private static Map<String, Field> trans2FieldMap(List<Field> fields) {

		if (fields == null || fields.size() == 0) {
			return Collections.emptyMap();
		}

		Map<String, Field> fieldMap = new HashMap<String, Field>(
				fields.size() * 2);
		for (Field f : fields) {
			String name = f.getName() + "." + f.getType().getName();
			if (!fieldMap.containsKey(name)) {
				fieldMap.put(name, f);
			}
		}
		return fieldMap;
	}

	/**
	 * 获取clazz中的所有getter，子类优先
	 * 
	 * @param clazz
	 * @return created by Tianxin on 2015年6月1日 上午10:27:22
	 */
	private static List<Method> getAllGetters(Class<?> clazz) {

		if (clazz == null) {
			return new ArrayList<Method>(0);
		}

		// 获取当前clazz中所有的getter，public、单个返回结果、无参数
		List<Method> methods = new ArrayList<Method>(32);
		Method[] ms = clazz.getDeclaredMethods();
		for (Method m : ms) {

			// static方法的过滤掉
			if (!isPublicObjectMethod(m)) {
				continue;
			}

			// 有参数的过滤掉
			if (m.getParameterTypes().length != 0) {
				continue;
			}

			// void的方法过滤掉
			Class<?> returnType = m.getReturnType();
			if (returnType.toString().equals("void")) {
				continue;
			}

			// 排除嵌套对象
			if (!isTypeFromJdk(m.getReturnType())) {
				continue;
			}

			String name = m.getName();
			if (returnType.equals(Boolean.class)
					|| returnType.equals(Boolean.TYPE)) {
				if (isLegalXetterName(name, "is")) {
					methods.add(m);
				}
			} else if (isLegalXetterName(name, "get")) {
				methods.add(m);
			}
		}

		// 获取clazz的父类的所有的非static的field
		Class<?> superClass = clazz.getSuperclass();
		if (!superClass.equals(Object.class)) {
			methods.addAll(getAllGetters(superClass));
		}

		return methods;
	}

	/**
	 * 是否是一个合法的getter或setter方法名
	 * 
	 * @param methodName
	 * @param xetterPrefix
	 *            可能是get、set、is
	 * @return created by Tianxin on 2015年6月1日 上午11:30:31
	 */
	private static boolean isLegalXetterName(String methodName,
			String xetterPrefix) {
		int length = methodName.length();
		if (length <= 3) {
			return false;
		}
		char c = methodName.charAt(xetterPrefix.length());
		if (c < 65 || c > 90) {
			return false;
		}

		return methodName.startsWith(xetterPrefix);
	}

	/**
	 * 获取clazz中所有的setter，子类优先
	 * 
	 * @param clazz
	 * @return created by Tianxin on 2015年6月1日 上午10:27:36
	 */
	private static List<Method> getAllSetters(Class<?> clazz) {

		if (clazz == null) {
			return new ArrayList<Method>(0);
		}

		// 获取当前clazz中所有的getter，public、单个返回结果、无参数
		List<Method> methods = new ArrayList<Method>(32);
		Method[] ms = clazz.getDeclaredMethods();
		for (Method m : ms) {

			// static方法的过滤掉
			if (!isPublicObjectMethod(m)) {
				continue;
			}

			// 有参数的过滤掉
			if (m.getParameterTypes().length != 1) {
				continue;
			}

			// 非void的方法过滤掉
			if (!m.getReturnType().toString().equals("void")) {
				continue;
			}

			// 排除嵌套对象
			if (!isTypeFromJdk(m.getParameterTypes()[0])) {
				continue;
			}

			// 保留get开头的方法
			String name = m.getName();
			if (isLegalXetterName(name, "set")) {
				methods.add(m);
			}
		}

		// 获取clazz的父类的所有的非static的field
		Class<?> superClass = clazz.getSuperclass();
		if (!superClass.equals(Object.class)) {
			methods.addAll(getAllSetters(superClass));
		}

		return methods;
	}

	/**
	 * 判断一个类型，是否是java提供的类型
	 * 
	 * @param type
	 * @return created by Tianxin on 2015年6月1日 上午10:53:56
	 */
	private static boolean isTypeFromJdk(Class<?> type) {
		// TODO 这里暂时不实现
		return true;
	}

	/**
	 * 判断一个方法是否是public的成员方法
	 * 
	 * @param m
	 * @return created by Tianxin on 2015年6月1日 上午10:54:08
	 */
	private static boolean isPublicObjectMethod(Method m) {
		int modifier = m.getModifiers();
		if (Modifier.isStatic(modifier)) {
			return false;
		}
		return Modifier.isPublic(modifier);
	}

	/**
	 * 获取clazz中所有的非static的Field，子类优先
	 * 
	 * @param clazz
	 * @return created by Tianxin on 2015年6月1日 上午10:27:52
	 */
	private static List<Field> getAllFields(Class<?> clazz) {
		if (clazz == null) {
			return new ArrayList<Field>(0);
		}

		// 获取当前clazz中所有的非static的field
		List<Field> fields = new ArrayList<Field>(32);
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			if (!Modifier.isStatic(f.getModifiers())) {
				fields.add(f);
			}
		}

		// 获取clazz的父类的所有的非static的field
		Class<?> superClass = clazz.getSuperclass();
		if (!superClass.equals(Object.class)) {
			fields.addAll(getAllFields(superClass));
		}

		return fields;
	}

	/**
	 * 字符串连接
	 * 
	 * @param os
	 * @return
	 */
	public final static String connect(Object... os) {
		if (os == null || os.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(os.length * 5);
		for (Object o : os) {
			sb.append(o);
		}
		return sb.toString();
	}

	/**
	 * 提取key的集合
	 * 
	 * @param entities
	 * @return created by Tianxin on 2015年5月28日 下午6:03:43
	 */
	public static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<KEY> extractKeys(
			Collection<ENTITY> entities) {

		List<KEY> keys = new ArrayList<KEY>(entities.size());
		for (ENTITY entity : entities) {
			keys.add(entity.getId());
		}
		return keys;
	}

	/**
	 * 去除不合法的entity
	 * 
	 * @param entities
	 * @return created by Tianxin on 2015年5月28日 下午4:29:02
	 */
	public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<ENTITY> trimEntities(
			Collection<ENTITY> entities) {
		if (entities == null || entities.size() == 0) {
			return null;
		}

		ArrayList<ENTITY> list = new ArrayList<ENTITY>(entities.size());
		for (ENTITY e : entities) {
			if (e != null) {
				list.add(e);
			}
		}
		return list;
	}

	public final static <KEY extends Serializable, ENTITY extends BaseObject<KEY>> List<ENTITY> toEntities(
			ENTITY... entities) {
		List<ENTITY> entityList = new ArrayList<ENTITY>(1);
		for (ENTITY entity : entities) {
			entityList.add(entity);
		}
		return entityList;
	}
}
