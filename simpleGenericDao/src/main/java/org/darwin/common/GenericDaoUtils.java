/**
 * org.darwin.common.utils.ReflectionUtils.java created by Tianxin(tianjige@163.com) on 2015年5月28日
 * 下午10:00:11
 */
package org.darwin.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.Utils;
import org.darwin.genericDao.annotations.Column;
import org.darwin.genericDao.annotations.Sequence;
import org.darwin.genericDao.annotations.StatType;
import org.darwin.genericDao.annotations.Table;
import org.darwin.genericDao.annotations.enums.ColumnStyle;
import org.darwin.genericDao.bo.BaseObject;
import org.darwin.genericDao.mapper.BasicMappers;
import org.darwin.genericDao.mapper.ColumnMapper;

/**
 * 反射的方法工具集 created by Tianxin on 2015年5月28日 下午10:00:11
 */
public class GenericDaoUtils {

  /**
   * clazz是topGenericClass的子类，这个方法可以找到topGenericClass中的泛型到clazz中以后，对应的是哪个实体类
   * 
   * @param clazz
   * @param topGenericClass
   * @param genericTypeIndex 要获取的泛型的index created by Tianxin on 2015年6月10日 下午8:49:08
   */
  @SuppressWarnings("unchecked")
  public static <ENTITY> Class<ENTITY> getGenericEntityClass(Class<?> clazz, Class<?> topGenericClass, int genericTypeIndex) {

    // 获取class的继承链
    ArrayList<Class<?>> classChain = getInheritClassChain(clazz, topGenericClass);

    // 由指定的父类向下依次寻找其指定的泛型
    int count = classChain.size();
    for (int i = 0; i < count; i++) {
      Class<?> currClass = classChain.get(count - i - 1);

      // 获取当前类的泛型列表
      Type genericSuperClass = currClass.getGenericSuperclass();
      ParameterizedType paramType = (ParameterizedType) genericSuperClass;
      Type[] types = paramType.getActualTypeArguments();

      // 根据父类所设定的泛型的index看是否在本类中指定了实现类
      Type genericType = types[genericTypeIndex];

      // 如果是一个class则证明是实现类
      if (genericType instanceof Class) {
        return (Class<ENTITY>) genericType;

        // 如果不是实现类，则判断追踪的泛型在此类中被放到了第几个位置
      } else {
        String genericTypeName = ((TypeVariable<?>) genericType).getName();
        TypeVariable<?>[] vars = currClass.getTypeParameters();
        int index = 0;
        for (TypeVariable<?> var : vars) {
          if (var.getName().equals(genericTypeName)) {
            genericTypeIndex = index;
          }
          index++;
        }
      }
    }
    throw new RuntimeException("没有找到泛型类!");
  }

  /**
   * 获取class关系的继承链
   * 
   * @param clazz
   * @param topGenericClass
   * @return created by Tianxin on 2015年6月10日 下午8:50:29
   */
  private static ArrayList<Class<?>> getInheritClassChain(Class<?> clazz, Class<?> topGenericClass) {
    ArrayList<Class<?>> inheritClassChain = new ArrayList<Class<?>>(5);
    while (!clazz.equals(topGenericClass)) {
      inheritClassChain.add(clazz);
      clazz = clazz.getSuperclass();
    }
    return inheritClassChain;
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


  /**
   * @param sql
   * @return created by Tianxin on 2015年5月28日 下午5:49:10
   */
  public static List<String> getColumnsFromSQL(String sql) {
    if (sql == null || sql.length() == 0) {
      return new ArrayList<String>(0);
    }

    // 获取字段列表部分
    sql = sql.trim().replaceAll("\\s+", " ");
    int start = sql.toLowerCase().indexOf("select ") + "select ".length();
    int end = sql.toLowerCase().indexOf(" from ");
    String sColumns = sql.substring(start, end).trim();

    // 解析字段列表部分
    List<String> columns = splitColumns(sColumns);
    List<String> labels = new ArrayList<String>(columns.size());
    for(String column : columns){
      int index = column.lastIndexOf(' ');
      if(index == -1){
        labels.add(column);
      }else{
        labels.add(column.substring(index + 1));
      }
    }
    return labels;
  }

  /**
   * @param sColumns
   * @return
   * created by Tianxin on 2015年6月15日 下午8:15:54
   */
  private static List<String> splitColumns(String sColumns) {
    List<String> columns = new ArrayList<String>();
    sColumns = sColumns + ",";
    int leftBracketCount = 0;
    int start = 0;
    for(int i = 0 ; i < sColumns.length() ; i ++){
      char c = sColumns.charAt(i);
      if(c == '('){
        leftBracketCount += 1;
      }else if(c == ')'){
        leftBracketCount -= 1;
      }else if(c == ','){
        if(leftBracketCount == 0){
          columns.add(sColumns.substring(start, i));
          start = i + 1;
        }
      }
    }
    return columns;
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
   * 获取实体的columnStyle
   * @param entityClass
   * @return
   * created by Tianxin on 2015年6月17日 下午8:17:13
   */
  private static <ENTITY> ColumnStyle getColumnStyleFromClass(Class<ENTITY> entityClass){
    
    //找table标签
    Table table = entityClass.getAnnotation(Table.class);
    if(table != null){
      return table.columnStyle();
    }
    
    //没有任何标签的，默认按照驼峰到mysql的转
    return ColumnStyle.JAVA_TO_MYSQL;
  }
  

  /**
   * 获取entityClass中与DB的所有字段映射关系
   * 
   * @param entityClass
   * @return created by Tianxin on 2015年6月1日 上午10:26:05
   */
  public static <ENTITY> Map<String, ColumnMapper> generateColumnMappers(Class<ENTITY> entityClass, ColumnStyle columnStyle) {
    
    if(columnStyle == null){
      columnStyle = getColumnStyleFromClass(entityClass);
    }

    // 获取get方法、set方法、属性的列表
    List<Field> fields = getAllFields(entityClass);
    List<Method> getters = getAllGetters(entityClass);
    List<Method> setters = getAllSetters(entityClass);

    // 将get方法、set方法转换为映射关系，key为名称.参数类型
    Map<String, Field> fieldMap = trans2FieldMap(fields);
    Map<String, Method> setterMap = trans2SetterMap(setters);

    // 以getter方法做循环，找到配对的方法和属性信息
    Map<String, ColumnMapper> columnMappers = Utils.newMap(16);
    for (Method getter : getters) {

      // 推导对一个的setter名字与field名字
      String getterName = getter.getName() + "." + getter.getReturnType().getName();
      String setterName = generateSetterKeyFromGetter(getterName);
      String fieldName = generateFieldKeyFromSetter(setterName);

      // 获取setter方法与field
      Method setter = setterMap.get(setterName);
      Field field = fieldMap.get(fieldName);

      // 如果没有setter或getter与setter类型不匹配，则跳过
      if (setter == null || !getter.getReturnType().equals(setter.getParameterTypes()[0])) {
        continue;
      }

      Class<?> fieldType = getter.getReturnType();
      if (setter.getName().equals("setId") && BaseObject.class.isAssignableFrom(entityClass)) {
        fieldType = getGenericEntityClass(entityClass, BaseObject.class, 0);
      }
      Column column = fetchColumn(field, getter, setter, entityClass);
      StatType type = fetchColumnType(field, getter, setter, entityClass);
      ColumnMapper columnMapper = new ColumnMapper(getter, setter, fieldType, column, columnStyle, type);
      columnMappers.put(columnMapper.getColumn(), columnMapper);
    }

    // 注册到BasicMapper中
    Class<?> keyClass = null;
    if (BaseObject.class.isAssignableFrom(entityClass)) {
      keyClass = getGenericEntityClass(entityClass, BaseObject.class, 0);
    }
    BasicMappers.register(keyClass, entityClass, columnMappers);
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
  static Column fetchColumn(Field field, Method getter, Method setter, Class<?> entityClass) {

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

      Method superGetter = superClass.getMethod(getter.getName(), getter.getParameterTypes());
      getter = superGetter == null ? getter : superGetter;

      Method superSetter = superClass.getMethod(setter.getName(), setter.getParameterTypes());
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
  static String generateFieldKeyFromSetter(String setterName) {
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
  static String generateSetterKeyFromGetter(String getterName) {

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
  static Map<String, Method> trans2SetterMap(List<Method> methods) {

    if (methods == null || methods.size() == 0) {
      return Collections.emptyMap();
    }

    Map<String, Method> methodMap = Utils.newMap(methods.size());
    for (Method m : methods) {
      String name = m.getName() + "." + m.getParameterTypes()[0].getName();
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
  static Map<String, Field> trans2FieldMap(List<Field> fields) {

    if (fields == null || fields.size() == 0) {
      return Collections.emptyMap();
    }

    Map<String, Field> fieldMap = Utils.newMap(fields.size());
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
  static List<Method> getAllGetters(Class<?> clazz) {

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
      if (returnType.equals(Boolean.class) || returnType.equals(Boolean.TYPE)) {
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
   * @param xetterPrefix 可能是get、set、is
   * @return created by Tianxin on 2015年6月1日 上午11:30:31
   */
  private static boolean isLegalXetterName(String methodName, String xetterPrefix) {
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
  static List<Method> getAllSetters(Class<?> clazz) {

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
   * 判断一个类型，是否是java提供的原生类型
   * 
   * @param type
   * @return created by Tianxin on 2015年6月1日 上午10:53:56
   */
  private static boolean isTypeFromJdk(Class<?> type) {
    if (type.isPrimitive()) {
    } else if (type.getPackage().getName().startsWith("java")) {
    } else if (Date.class.isAssignableFrom(type)) {
    } else if (BigDecimal.class.isAssignableFrom(type)) {
    } else if (URL.class.isAssignableFrom(type)) {
      // 以上几种都不是，则证明非原生类型
    } else {
      return false;
    }
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
  static List<Field> getAllFields(Class<?> clazz) {
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
}
