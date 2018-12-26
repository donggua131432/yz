package com.yz.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.service.BdsUserInfoService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsUserInfoApiImpl implements BdsUserInfoApi {
	
	@Autowired
	private BdsUserInfoService userInfoService;

	@Override
	public Map<String, String> getUserInfo(String userId) {
		return userInfoService.getUserInfo(userId);
	}

	@Override
	public Map<String, String> getRecruit(String mobile, String idCard) {
		return userInfoService.getRecruit(mobile, idCard);
	}

	@Override
	public void createRelation2(String pUserId, String pIsMb, String userId, String stdId, String userType, String pType) {
		userInfoService.createRelation2(pUserId, pIsMb, userId, stdId, userType, pType);
	}
	
}
