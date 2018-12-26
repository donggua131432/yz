package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service; 
import com.yz.constants.GlobalConstants;
import com.yz.exception.IRpcException;
import com.yz.model.SessionInfo;
import com.yz.model.UsConsults;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsConsultsService;
import com.yz.session.AppSessionHolder;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsConsultsApiImpl implements UsConsultsApi {
	
	@Autowired
	private UsConsultsService consultsService;

	@Override
	public Object myConsults(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		int pageNum = body.getInt("pageNum", 1);
		int pageSize = body.getInt("pageSize", 10);
		return consultsService.myConsults(userId, pageNum, pageSize);
	}

	@Override
	public Object addConsults(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		SessionInfo session =AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
		String realName = session.getRealName();
		String nickName = session.getNickName();
		
		String userName = StringUtil.hasValue(realName) ? realName : nickName;
		
		String infoTitle = "[" + DateUtil.getCurrentDate(DateUtil.YYYYMMDDHHMMSS_SPLIT) + "]_" + userName + "的提问";
		
		String infoContent = body.getString("content");
		
		String mobile = session.getMobile();
		
		UsConsults consults = new UsConsults();
		
		consults.setInfoTitle(infoTitle);
		consults.setInfoContent(infoContent);
		consults.setUserId(userId);
		consults.setUserName(userName);
		consults.setMobile(mobile);
		consults.setIsReply(GlobalConstants.FALSE);
		
		consultsService.addConsults(consults);
		
		return null;
	}

}
