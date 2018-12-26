package com.yz.core.datasource;

public enum Database {
	BDS("bds"),
	BCC("bcc"),
	BS("bs"),
	GS("gs"),
	US("us"),
	GW("gw");

	private String name;
	
	private Database(String name) {
		this.name = name;
	}
	
	public String get() {
		return this.name;
	}
}
