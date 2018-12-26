package com.yz.api;

import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsOaEmployeeApi {
	/**
	 * 教师查询接口
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="queryEmployeeInfo",methodRemark="根据姓名或手机查询教师信息",needLogin=false)
	public Object queryEmployeeInfo(Header header, Body body) throws IRpcException;
	
	
	
	
}
