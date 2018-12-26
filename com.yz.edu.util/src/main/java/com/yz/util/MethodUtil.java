package com.yz.util;

import java.lang.reflect.Method;

import org.apache.commons.lang.reflect.MethodUtils;
@SuppressWarnings({"unchecked", "rawtypes"})
public class MethodUtil extends MethodUtils{
	/**
	 * 获取对象 成员 get方法
	 * 
	 * @param clz
	 * @param columnName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	
	public static Method getterMethod(Class clz, String columnName) throws NoSuchMethodException, SecurityException {
		String s = "get" + columnName.substring(0, 1).toUpperCase();
		s += columnName.substring(1);
		return clz.getMethod(s, null);
	}

	/**
	 * 查找方法
	 * 
	 * @param clz
	 * @param interfaceMethod
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method findBy(Class clz, String interfaceMethod, Class... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		return clz.getMethod(interfaceMethod, parameterTypes);
	}

	/**
	 * 根据get方法名获取参数名
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getterFieldName(String methodName) {
		String paramName = null;
		if (StringUtil.hasValue(methodName)) {
			paramName = methodName.substring(3);

			String start = paramName.substring(0, 1).toLowerCase();

			paramName = start + paramName.substring(1);

			return paramName;
		}
		return null;
	}

	/**
	 * 获取对象成员set方法
	 * 
	 * @param clz
	 * @param columnName
	 * @param paramClz
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method setterMethod(Class clz, String columnName, Class paramClz)
			throws NoSuchMethodException, SecurityException {
		String s = "set" + columnName.substring(0, 1).toUpperCase();
		s += columnName.substring(1);
		Method[] methods = clz.getMethods();
		for (Method m : methods) {
			if (m.getName().equals(s))
				return m;
		}
		return null;
	}
}
