/**
 * org.darwin.common.utils.StatGenericDaoUtils.java
 * created by Tianxin(tianjige@163.com) on 2015年6月3日 下午2:17:14
 */
package org.darwin.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.annotations.stat.StatTable;
import org.darwin.genericDao.annotations.stat.StatType;
import org.darwin.genericDao.bo.BaseStatObject;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * 统计的通用dao的工具方法集合 created by Tianxin on 2015年6月3日 下午2:17:14
 */
public class StatGenericDaoUtils {

	/**
	 * @param class1
	 * @return created by Tianxin on 2015年6月3日 下午2:18:38
	 */
	@SuppressWarnings("unchecked")
	public static <ENTITY extends BaseStatObject> Class<ENTITY> getEntityClass(Class<?> subGenericStatDaoClass) {

		// 如果是objec则直接返回null
		if (subGenericStatDaoClass.equals(Object.class)) {
			return null;
		}

		// 如果泛型父类为空，则返回null
		Type genericSuperClass = subGenericStatDaoClass.getGenericSuperclass();
		if (genericSuperClass != null && genericSuperClass instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) genericSuperClass;
			Type[] types = paramType.getActualTypeArguments();
			for (Type type : types) {

				// 如果是占位泛型，则跳过
				if (!(type instanceof Class)) {
					continue;
				}

				// 如果是BaseObject的子类，则直接返回即可
				Class<?> typeClass = (Class<?>) type;
				boolean isBaseObjectClass = BaseStatObject.class.isAssignableFrom(typeClass);
				if (isBaseObjectClass) {
					return (Class<ENTITY>) typeClass;
				}
			}
		}

		// 如果本类没有，则获取其泛型父类
		return getEntityClass(subGenericStatDaoClass.getSuperclass());
	}

	
	/**
	 * 获取实体类中的StatTable的注解
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午7:07:12
	 */
	public static StatTable getStatTable(Class<?> entityClass) {
		if (entityClass.equals(Object.class)) {
			return null;
		}
		StatTable table = entityClass.getAnnotation(StatTable.class);
		if (table == null) {
			return getStatTable(entityClass.getSuperclass());
		} else {
			return table;
		}
	}
	
	/**
	 * 获取entityClass中与DB的所有字段映射关系
	 * 
	 * @param entityClass
	 * @return created by Tianxin on 2015年6月1日 上午10:26:05
	 */
	public static <ENTITY extends BaseStatObject> Map<String, ColumnMapper> generateColumnMappers(Class<ENTITY> entityClass,
			ColumnStyle columnStyle) {

		// 获取get方法、set方法、属性的列表
		List<Field> fields = GenericDaoUtils.getAllFields(entityClass);
		List<Method> getters = GenericDaoUtils.getAllGetters(entityClass);
		List<Method> setters = GenericDaoUtils.getAllSetters(entityClass);

		// 将get方法、set方法转换为映射关系，key为名称.参数类型
		Map<String, Field> fieldMap = GenericDaoUtils.trans2FieldMap(fields);
		Map<String, Method> setterMap = GenericDaoUtils.trans2SetterMap(setters);

		// 以getter方法做循环，找到配对的方法和属性信息
		Map<String, ColumnMapper> columnMappers = Utils.newMap(16);
		for (Method getter : getters) {

			// 推导对一个的setter名字与field名字
			String getterName = getter.getName() + "." + getter.getReturnType().getName();
			String setterName = GenericDaoUtils.generateSetterKeyFromGetter(getterName);
			String fieldName = GenericDaoUtils.generateFieldKeyFromSetter(setterName);

			// 获取setter方法与field
			Method setter = setterMap.get(setterName);
			Field field = fieldMap.get(fieldName);

			// 如果没有setter或getter与setter类型不匹配，则跳过
			if (setter == null || !getter.getReturnType().equals(setter.getParameterTypes()[0])) {
				continue;
			}

			Column column = GenericDaoUtils.fetchColumn(field, getter, setter, entityClass);
			StatType type = fetchColumnType(field, getter, setter, entityClass);
			ColumnMapper columnMapper = new ColumnMapper(getter, setter, column, columnStyle, type);
			columnMappers.put(columnMapper.getColumn(), columnMapper);
		}

		// 注册到BasicMapper中
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
	static StatType fetchColumnType(Field field, Method getter, Method setter, Class<?> entityClass) {

		// 如果field在当前类中声明，且field中有Column的注解，则返回该Column注解
		if (field != null && field.getDeclaringClass().equals(entityClass)) {
			StatType column = field.getAnnotation(StatType.class);
			if (column != null) {
				return column;
			}
		}

		// 如果getter在当前类中声明，且getter中有Column的注解，则返回该Column注解
		if (getter != null && getter.getDeclaringClass().equals(entityClass)) {
			StatType column = getter.getAnnotation(StatType.class);
			if (column != null) {
				return column;
			}
		}

		// 如果setter在当前类中声明，且setter中有Column的注解，则返回该Column注解
		if (setter != null && setter.getDeclaringClass().equals(entityClass)) {
			StatType column = setter.getAnnotation(StatType.class);
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

			Method superGetter = superClass.getMethod(getter.getName(), getter.getParameterTypes());
			getter = superGetter == null ? getter : superGetter;

			Method superSetter = superClass.getMethod(setter.getName(), setter.getParameterTypes());
			setter = superSetter == null ? setter : superSetter;
		} catch (Exception e) {
		}

		return fetchColumnType(field, getter, setter, superClass);
	}
}
