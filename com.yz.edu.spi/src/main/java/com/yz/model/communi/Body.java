package com.yz.model.communi;

import java.util.Map;

import com.yz.model.CommunicationMap;


@SuppressWarnings({"rawtypes", "unchecked"})
public class Body extends CommunicationMap {

	private static final long serialVersionUID = 3901447912252291997L;

	public void cover(Map o) {
		this.clear();
		putAll(o);
	}
	
	public void cover(Object key, Object o) {
		this.clear();
		put(key, o);
	}
}
