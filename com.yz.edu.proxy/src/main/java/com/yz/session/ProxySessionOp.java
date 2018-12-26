package com.yz.session;

import com.yz.constants.BccConstants;
import com.yz.core.util.RequestUtil;
import com.yz.model.SessionInfo;

/**
 * 
 * @author Administrator
 *
 */ 
public class ProxySessionOp implements AppSessionHolder.SessionOperator {

	public void setSession(SessionInfo info)// 存储sessionInfo信息
	{
		RequestUtil.setAttribute(BccConstants.CURENT_USER_SESSION_KEY, info);
	}

	public SessionInfo getSessionLocal() {
		return (SessionInfo) RequestUtil.getAttribute(BccConstants.CURENT_USER_SESSION_KEY);
	}
}
