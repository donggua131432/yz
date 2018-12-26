package com.yz.core.handler;


import com.yz.exception.BusinessException;
import com.yz.exception.IRuntimeException;
import com.yz.model.SessionInfo;

public class WechatLoginCheckHandler extends LoginCheckHandler {

	@Override
	public void loginCheck(SessionInfo userInfo, String newToken) throws IRuntimeException {
		if (userInfo == null) {
			throw new BusinessException("E00001");// 尚未关注公众号
		}
		
		if(!newToken.equals(userInfo.getJwtToken())) {
			throw new BusinessException("E00010");// 异地登录
		}
		
		/*if(StringUtil.isEmpty(userInfo.getMobile())) {
			throw new BusinessException("E00011");//未绑定手机号
		}*/
	}

}
