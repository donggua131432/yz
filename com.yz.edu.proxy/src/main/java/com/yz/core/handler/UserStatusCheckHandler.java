package com.yz.core.handler;

import com.yz.constants.BccConstants;
import com.yz.core.constants.AppConstants;
import com.yz.exception.BusinessException;
import com.yz.exception.IRuntimeException; 
import com.yz.model.SessionInfo;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Header;
import com.yz.model.communi.Request; 
import com.yz.session.AppSessionHolder; 

public class UserStatusCheckHandler implements CheckHandler {

	@Override
	public void check(YzServiceInfo interfaceInfo, Request request, Object handler) throws IRuntimeException {
		if (interfaceInfo.isNeedTrans()) {
			Header header = request.getHeader();
			SessionInfo userInfo =  AppSessionHolder.getSessionInfo(header.getUserId(), BccConstants.PROXY_LOCAL_SESSION_OPERATOR) ;
			String userStatus = userInfo.getUserStatus();
			if (AppConstants.USER_STATUS_TRANS_LOCK.equals(userStatus)) {
				throw new BusinessException("E00009");// 交易锁定， 无法交易
			}
		}
	}

}
