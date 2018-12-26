package com.yz.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsLearnInfoService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsLearnInfoApiImpl implements BdsLearnInfoApi {

	@Autowired
	private BdsLearnInfoService learnService;

	@Override
	public Object stdLearnInfo(Header header, Body body) throws IRpcException {

		return learnService.selectStudentInfo(header.getUserId());
	}

	@Override
	public Map<String, String> addLearnInfo(Body body) throws IRpcException {
		return learnService.addLearnInfo(body);
	}

	@Override
	public Map<String, String> getLearnStatus(String userId) throws IRpcException {
		return learnService.getLearnStatus(userId);
	}

	@Override
	public Object getLearnInfoByLearnId(Header header, Body body) throws IRpcException
	{
		return learnService.getLearnInfoByLearnId(header,body);
	}

}
