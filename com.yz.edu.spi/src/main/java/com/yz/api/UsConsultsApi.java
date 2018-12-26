package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface UsConsultsApi {
	/**
	 * 【PUB】查询我的报考问答信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="myConsults",methodRemark="查询我的报考问答",needLogin=true)
	Object myConsults(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】添加报考问答
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="addConsults",methodRemark="添加报考问答",needLogin=true)
	Object addConsults(Header header, Body body) throws IRpcException;
}
