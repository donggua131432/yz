package com.yz.service;

import org.springframework.stereotype.Service;

 
import com.yz.core.constants.AppConstants;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;

@Service
public class BccTokenService {
	
	@YzService(sysBelong="bcc",methodName="getCommitToken",methodRemark="获取防重复提交TOKEN",needLogin=true)
	public String getToken(Header header, Body body) {
		String userId = header.getUserId();
		String interfaceName = body.getString("itName");
		String commitToken = StringUtil.UUID();
		RedisService.getRedisService().set(userId+AppConstants.COMMIT_TOKEN_PREFIX + interfaceName, commitToken);
		return commitToken;
	}
}
