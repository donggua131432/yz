package com.yz.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapUtil {

	/**
	 * 过滤空参数
	 * 
	 * @param <T>
	 * 
	 * @param postData
	 * @return
	 * @return
	 */
	public static <T extends Map<String, String>> Map<String, String> filterBlank(T orderInfo) {

		Map<String, String> m = null;
		try {
			m = (T) orderInfo.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		Set<String> keyset = orderInfo.keySet();
		for (String key : keyset) {
			String value = orderInfo.get(key);
			if (!StringUtil.hasValue(value)) {
				continue;
			}
			m.put(key, value);
		}

		return m;
	}
	
	/**
	 * 将参数排序并拼接为key=V&key2=v2字符串
	 * 
	 * @param data
	 * @param outParamName
	 *            剔除的参数
	 * @return
	 */
	public static String sortMap2String(Map<String, String> data, String... outParamName) {
		Map<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();

			boolean flag = false;
			if (outParamName != null) {
				for (int i = 0; i < outParamName.length; i++) {
					if (StringUtil.hasValue(outParamName[i]) && outParamName[i].equals(en.getKey().trim())) {
						flag = true;
						break;
					}
				}
			}

			if (flag)
				continue;

			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			// sf.append(en.getKey() + "=" + format(en.getValue()) + "&");
			sf.append(en.getKey() + "=" + en.getValue() + "&");
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 将参数排序并拼接为key=V&key2=v2字符串
	 * 
	 * @param data
	 * @param outParamName
	 *            剔除的参数
	 * @return
	 */
	public static String sortMap2UrlString(Map<String, String> data, String... outParamNames) {
		Map<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();

			boolean flag = false;
			if (outParamNames != null) {
				for (int i = 0; i < outParamNames.length; i++) {
					String outParamName = outParamNames[i];
					String dataKey = en.getKey().trim();
					if (StringUtil.hasValue(outParamName) && outParamName.equals(dataKey)) {
						flag = true;
						break;
					}
				}
			}

			if (flag)
				continue;

			tree.put(en.getKey(), en.getValue());
		}

		List<String> treeKeys = new ArrayList<String>(tree.keySet());

		Collections.sort(treeKeys);

		StringBuffer sf = new StringBuffer();
		for (String key : treeKeys) {
			String value = tree.get(key);

			sf.append(key + "=" + value + "&");
		}

		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 拼接为key=V&key2=v2字符串(不排序)
	 * 
	 * @param data
	 * @param outParamNames
	 *            剔除的参数
	 * @return
	 */
	public static String map2UrlString(Map<String, String> data, String... outParamNames) {
		Map<String, String> tree = new LinkedHashMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();

			if (outParamNames != null) {
				boolean flag = false;
				for (int i = 0; i < outParamNames.length; i++) {
					String outParamName = outParamNames[i];
					String dataKey = en.getKey().trim();
					if (StringUtil.hasValue(outParamName) && outParamName.equals(dataKey)) {
						flag = true;
						break;
					}
				}

				if (flag)
					continue;
			}

			tree.put(en.getKey(), en.getValue());
		}

		Iterator<Entry<String, String>> treeIt = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (treeIt.hasNext()) {
			Entry<String, String> en = treeIt.next();
			// sf.append(en.getKey() + "=" + format(en.getValue()) + "&");
			sf.append(en.getKey() + "=" + en.getValue() + "&");
		}

		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 将参数排序并拼接为key=V&key2=v2字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String sortMap2UrlString(Map<String, String> data) {
		return sortMap2UrlString(data, null);
	}

	public static int getInt(Map m, String paramName) {
		Object value = getValue(m, paramName);
		return value == null ? 0 : Integer.valueOf(value.toString());
	}

	public static boolean getBoolean(Map m, String paramName) {
		Object value = getValue(m, paramName);
		return value == null ? false : Boolean.valueOf(value.toString());
	}

	public static String getString(Map m, String paramName) {
		Object value = getValue(m, paramName);
		return value == null ? null : value.toString();
	}

	public static Object getValue(Map m, String paramName) {
		return m.get(paramName);
	}

	public static double getDouble(Map m, String paramName) {
		Object value = getValue(m, paramName);
		return value == null ? 0 : Double.valueOf(value.toString());
	}

	public static Byte getByte(Map m, String paramName) {
		Object value = getValue(m, paramName);
		if (value == null)
			value = 0;
		return Byte.valueOf(value.toString());
	}

	public static Date getDate(Map m, String paramName) {
		Object value = getValue(m, paramName);
		if (value == null)
			return null;
		return (Date) value;
	}

	public static int getInt(Map m, String paramName, int defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : Integer.valueOf(value.toString());
	}

	public static boolean getBoolean(Map m, String paramName, boolean defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : Boolean.valueOf(value.toString());
	}

	public static String getString(Map m, String paramName, String defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : value.toString();
	}

	public static double getDouble(Map m, String paramName, double defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : Double.valueOf(value.toString());
	}

	public static Byte getByte(Map m, String paramName, byte defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : Byte.valueOf(value.toString());
	}

	public static Date getDate(Map m, String paramName, Date defaultValue) {
		Object value = getValue(m, paramName);
		return value == null ? defaultValue : (Date) value;
	}

}
