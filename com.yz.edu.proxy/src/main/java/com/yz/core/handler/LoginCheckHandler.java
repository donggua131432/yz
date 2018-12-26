package com.yz.core.handler;

import javax.servlet.http.HttpServletRequest;

import com.yz.constants.BccConstants;
import com.yz.core.constants.AppConstants; 
import com.yz.exception.BusinessException;
import com.yz.exception.IRuntimeException; 
import com.yz.model.SessionInfo;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Request; 
import com.yz.session.AppSessionHolder; 
import com.yz.util.StringUtil;
import com.yz.util.TokenUtil;

/**
 * 登录校验
 * 
 * @author Administrator
 *
 */
public abstract class LoginCheckHandler implements CheckHandler {

	@Override
	public void check(YzServiceInfo interfaceInfo, Request request, Object handler) throws IRuntimeException {
		if (interfaceInfo.isNeedLogin()) {// 是否需要登录
			HttpServletRequest httpRequest = (HttpServletRequest) handler;

			String authToken = httpRequest.getHeader(AppConstants.AUTH_TOKEN);
			
			if(StringUtil.isEmpty(authToken)) {
				authToken = httpRequest.getHeader(AppConstants.AUTH_TOKEN_NEW);
			}
			
			if(StringUtil.isEmpty(authToken)) {
				throw new BusinessException("E00001");
			}

			final TokenUtil.Secure secure = TokenUtil.convert(authToken);
			
			if (secure == null) {
				throw new BusinessException("E00001");
			}

			String userId = secure.getUserId();
			String newToken = secure.getToken();
			 
			SessionInfo userInfo = AppSessionHolder.getSessionInfo(userId, BccConstants.PROXY_LOCAL_SESSION_OPERATOR) ;
					
			loginCheck(userInfo, newToken);  

			String userStatus = userInfo.getUserStatus();

			if (AppConstants.USER_STATUS_FROZEN.equals(userStatus)) {
				throw new BusinessException("E00003");// 用户被冻结 无法操作
			} else if (AppConstants.USER_STATUS_LOCK.equals(userStatus)) {
				throw new BusinessException("E00004");// 用户已被系统锁定，请联系管理员
			}
			
			request.getHeader().setUserId(userId);
			request.getHeader().setStdId(userInfo.getStdId());
			request.getHeader().setEmpId(userInfo.getEmpId());
			request.getHeader().setOpenId(userInfo.getOpenId());
		}
	}

	public abstract void loginCheck(SessionInfo userInfo, String newToken) throws IRuntimeException; 

}
