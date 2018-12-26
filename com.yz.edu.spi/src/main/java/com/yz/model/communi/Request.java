package com.yz.model.communi;

import java.io.Serializable;

public class Request implements Serializable, Communication {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5192294213113594623L;
	
	private Header header;
	private Body body;
	
	
	public Request() {}
	
	public Request(Header header, Body body) {
		this.header = header;
		this.body = body;
	}
	
	
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
}
