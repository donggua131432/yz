package com.yz.model;

import java.lang.reflect.Method;

public class InterfaceBean {
	
	private Method method;
	
	private Object invokeObj;
	
	public InterfaceBean(){}
	
	public InterfaceBean(Object invokeObj, Method method) {
		this.setInvokeObj(invokeObj);
		this.setMethod(method);
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getInvokeObj() {
		return invokeObj;
	}

	public void setInvokeObj(Object invokeObj) {
		this.invokeObj = invokeObj;
	}
}
