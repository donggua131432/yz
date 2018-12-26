package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsEnrollService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsEnrollApiImpl implements UsEnrollApi {
	
	@Autowired
	private UsEnrollService enrollService;

	@SuppressWarnings("unchecked")
	@Override
	public Object enroll(Header header, Body body) throws IRpcException {
		body.put("userId", header.getUserId());
		body.put("stdId", header.getStdId());
		return enrollService.enroll(body);
	}

}
