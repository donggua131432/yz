package com.yz.model;

import java.util.Date;
import java.util.HashMap;

@SuppressWarnings({"rawtypes"})
public class CommunicationMap extends HashMap {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5212362966530136106L;

	public int getInt(String paramName) {
		Object value = getValue(paramName);
		return value == null ? 0 : Integer.valueOf(value.toString());
	}
	
	public int getInt(String paramName, int defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : Integer.valueOf(value.toString());
	}

	public boolean getBoolean(String paramName) {
		Object value = getValue(paramName);
		return value == null ? false : Boolean.valueOf(value.toString());
	}
	
	public boolean getBoolean(String paramName, boolean defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : Boolean.valueOf(value.toString());
	}

	public String getString(String paramName) {
		Object value = getValue(paramName);
		return value == null ? null : value.toString();
	}
	
	public String getString(String paramName, String defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : value.toString();
	}

	public Object getValue(String paramName) {
		return get(paramName);
	}

	public double getDouble(String paramName) {
		Object value = getValue(paramName);
		return value == null ? 0 : Double.valueOf(value.toString());
	}
	
	public double getDouble(String paramName, double defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : Double.valueOf(value.toString());
	}

	public Byte getByte(String paramName) {
		Object value = getValue(paramName);
		return value == null ? null : Byte.valueOf(value.toString());
	}
	
	public Byte getByte(String paramName, byte defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : Byte.valueOf(value.toString());
	}
	
	public Date getDate(String paramName) {
		Object value = getValue(paramName);
		return value == null ? null : (Date)value;
	}
	
	public Date getDate(String paramName, Date defaultValue) {
		Object value = getValue(paramName);
		return value == null ? defaultValue : (Date)value;
	}

	public long getLong(String paramName) {
		Object value = getValue(paramName);
		return value == null ? 0 : Long.valueOf(value.toString());
	}
}
