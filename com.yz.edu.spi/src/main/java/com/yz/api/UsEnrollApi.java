package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface UsEnrollApi {
	/**
	 * 在线报读
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="enroll",methodRemark="在线报名",needLogin=true)
	Object enroll(Header header, Body body) throws IRpcException;
}
