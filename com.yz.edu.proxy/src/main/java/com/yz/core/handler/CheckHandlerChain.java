package com.yz.core.handler;

import java.util.LinkedList;

import com.yz.exception.SystemException;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Request;

/**
 * 检测链
 * @author Administrator
 *
 */
public class CheckHandlerChain implements CheckHandler {

	private static LinkedList<CheckHandler> checkHandlers = new LinkedList<CheckHandler>();

	@Override
	public void check(YzServiceInfo interfaceInfo, Request request, Object handler) throws SystemException {
		for(CheckHandler ch : checkHandlers) {
			ch.check(interfaceInfo, request, handler);
		}
	}

	public void addHandler(CheckHandler handler) {
		checkHandlers.add(handler);
	}
}
