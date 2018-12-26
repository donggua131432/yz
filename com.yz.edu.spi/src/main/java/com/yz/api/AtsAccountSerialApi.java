package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface AtsAccountSerialApi {

	/**
	 * 【PUB】账户流水查询
	 * @param header
	 * @param body
	 * @return:1.0
	 */
	@YzService(sysBelong="ats",methodName="accountSerial",methodRemark="账户流水查询",needLogin =true)
	public Object getAccountSerials(Header header, Body body) throws IRpcException;

}
