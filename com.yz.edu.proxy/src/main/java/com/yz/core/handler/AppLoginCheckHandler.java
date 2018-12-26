package com.yz.core.handler;

import org.springframework.util.StringUtils;

import com.yz.exception.BusinessException;
import com.yz.exception.IRuntimeException;
import com.yz.model.SessionInfo;

public class AppLoginCheckHandler extends LoginCheckHandler {

	@Override
	public void loginCheck(SessionInfo userInfo, String newToken) throws IRuntimeException {
		if (userInfo == null) {
			throw new BusinessException("E10000");// 您尚未登录，请先登录
		}

		String oldToken = userInfo.getJwtToken();

		if (!StringUtils.hasLength(oldToken)) {
			throw new BusinessException("E10000");// 您尚未登录，请先登录
		}

		if (!oldToken.equals(newToken)) {
			throw new BusinessException("E20000");// 异地登录
		}
	}

}
