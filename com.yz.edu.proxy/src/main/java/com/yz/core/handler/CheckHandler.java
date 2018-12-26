package com.yz.core.handler;

import com.yz.exception.IRuntimeException;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Request;

public interface CheckHandler {

	public void check(YzServiceInfo interfaceInfo, Request request, Object handler) throws IRuntimeException;
}
