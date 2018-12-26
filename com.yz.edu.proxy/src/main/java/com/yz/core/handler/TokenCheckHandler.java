package com.yz.core.handler;

import com.yz.core.constants.AppConstants;
import com.yz.exception.BusinessException;
import com.yz.exception.IRuntimeException; 
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Request;
import com.yz.redis.RedisService;
import com.yz.util.StringUtil;

public class TokenCheckHandler implements CheckHandler {

	@Override
	public void check(YzServiceInfo interfaceInfo, Request request, Object handler) throws IRuntimeException {
		if (interfaceInfo.isNeedLogin() && interfaceInfo.isNeedToken()) {
			String itName = interfaceInfo.getInterfaceName();
			
			String userId = request.getHeader().getUserId();
			
			String commitToken = request.getBody().getString(AppConstants.COMMIT_TOKEN);
			
			String saveToken =  RedisService.getRedisService().get(userId+AppConstants.COMMIT_TOKEN_PREFIX + itName);
			
			if(StringUtil.isEmpty(saveToken) || !saveToken.equals(commitToken)) {
				throw new BusinessException("E00012");//请勿重复提交
			}
		}
	}

}
