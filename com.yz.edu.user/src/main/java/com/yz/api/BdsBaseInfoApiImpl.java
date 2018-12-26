package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BaseInfoService;
@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsBaseInfoApiImpl implements BdsBaseInfoApi {
	
	@Autowired
	private BaseInfoService baseInfoService;

	@Override
	public Object enrollInfo(Header header, Body body) throws IRpcException {
		return baseInfoService.getEnrollInfo(body);
	}

}
