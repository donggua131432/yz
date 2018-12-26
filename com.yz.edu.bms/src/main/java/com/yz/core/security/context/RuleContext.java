package com.yz.core.security.context;

import com.yz.model.admin.BmsFunc;
/**
 * 权限上下文
 * @author Administrator
 *
 */
public class RuleContext {

	private static final ThreadLocal<BmsFunc> CURRENT_CONTEXT = new ThreadLocal<BmsFunc>();
	
	public static BmsFunc getFunctionInfo() {
		return CURRENT_CONTEXT.get();
	}
	
	public static void setFunctionInfo(BmsFunc functionInfo) {
		CURRENT_CONTEXT.set(functionInfo);
	}
	
	public static void clear() {
		CURRENT_CONTEXT.remove();
	}
}
