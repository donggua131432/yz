package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsBaseInfoApi {
	
	@YzService(sysBelong="bds",methodName="enrollInfo",methodRemark="院校、专业、考区信息查询",needLogin=true)
	public Object enrollInfo(Header header, Body body) throws IRpcException;
}
