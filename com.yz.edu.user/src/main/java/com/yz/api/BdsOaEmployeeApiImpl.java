package com.yz.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsOaEmployeeService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsOaEmployeeApiImpl implements BdsOaEmployeeApi {

	@Autowired
	private BdsOaEmployeeService employeeService;

	@Override
	public Object queryEmployeeInfo(Header header, Body body) throws IRpcException {
		List<Map<String, Object>> map=employeeService.queryEmployInfo(body.getString("keywords"));
		return map;
	}

	

	

}
