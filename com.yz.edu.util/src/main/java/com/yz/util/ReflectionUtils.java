package com.yz.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author lingdian 2016年8月11日
 * @desc
 */
public class ReflectionUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		try { 
			field.setAccessible(true);
			if(ClassUtil.isBaseDataType(field.getType()))
			{
				field.set(object, adapter(field.getType(),value));
			}
			else
			{
				
			}
			
		} catch (IllegalAccessException e) {
			logger.error("field.set.error",ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 
	 * 
	 * @param cls
	 * @param val
	 * @return
	 */
	private static Object adapter(Class<?>cls,Object val)
	{
		if(ClassUtil.isBaseDataType(cls))
		{
			if(cls ==int.class || cls==Integer.class)
			{
				return NumberUtils.toInt(String.valueOf(val));
			}
			if(cls ==Long.class || cls==long.class)
			{
				return NumberUtils.toLong(String.valueOf(val));
			}
			if(cls ==Double.class || cls==double.class)
			{
				return NumberUtils.toDouble(String.valueOf(val));
			}
			if(cls ==BigDecimal.class )
			{
				return new BigDecimal(String.valueOf(val));
			}
		}else if(ClassUtil.isAssignable(cls, Map.class))
		{
			
		}
		
		return String.valueOf(val);
	}
	
	

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("field.getFieldValue.error{}",e);
		}
		return result;
	}

	/**
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
			throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			logger.error("field.invokeMethod.error:{}",e);
		}
		return null;
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	protected static Field getDeclaredField(Object object, String fieldName) {
		for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		return null;
	}

	/**
	 * 
	 * @param field
	 */
	protected static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()))
				|| (!Modifier.isPublic(field.getDeclaringClass().getModifiers())))
			field.setAccessible(true);
	}

	/**
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				logger.error("field.getDeclaredMethod.error:{}",e);
			}
		return null;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Class<T> getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if ((index >= params.length) || (index < 0)) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);

			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/***
	 * 
	 * @param e
	 * @return
	 */
	public static IllegalArgumentException convertToUncheckedException(Exception e) {
		if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException))
				|| ((e instanceof NoSuchMethodException))) {
			return new IllegalArgumentException("Refelction Exception.", e);
		}
		return new IllegalArgumentException(e);
	}
}