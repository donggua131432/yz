package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.constants.StudentConstants;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsLoginService;
import com.yz.service.UserRegisterService;
import com.yz.session.AppSessionHolder;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsLoginApiImpl implements UsLoginApi {

	@Autowired
	private UsLoginService loginService;
	
	@Autowired
	private UserRegisterService userRegisterService;

	@Override
	public Object sign(Header header, Body body) throws IRuntimeException {
		String userId = header.getUserId();
		return loginService.sign(userId);
	}

	@Override
	public Object login(Header header, Body body) throws IRuntimeException {
		String mobile = body.getString("mobile");
		String valicode = body.getString("valicode");
		
		String ip = body.getString("ip");
		String mac = body.getString("mac");
		String coordinate = body.getString("coordinate");
		
		return loginService.login(mobile, valicode, ip, mac, coordinate);
	}
	
	@Override
	public Object getValicode(Header header, Body body) {
		String mobile = body.getString("mobile");
		String authCode = body.getString("code");
		return loginService.valicode(mobile, authCode);
	}

	@Override
	public Object register(Header header, Body body) throws IRuntimeException {
		return userRegisterService.register(body);
	}

	@Override
	public Object auth(Header header, Body body) throws IRuntimeException {
		String code = body.getString("code");
		String scope = body.getString("scope");
		
		String ip = body.getString("ip");
		String mac = body.getString("mac");
		String coordinate = body.getString("coordinate");
		
		return loginService.auth(code, scope, ip, mac, coordinate);
	}

	@Override
	public Object isSign(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		return loginService.isSign(userId);
	}

	@Override
	public Object bindMobile(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		String mobile = body.getString("mobile");
		String valicode = body.getString("valicode");
		String inviteToken = body.getString("inviteToken");
		String realName = body.getString("realName");
		String scholarship = body.getString("scholarship", StudentConstants.SCHOLARSHIP_DEFAULT);
		String idCard = body.getString("idCard");
		
		String ip = body.getString("ip");
		String mac = body.getString("mac");
		String coordinate = body.getString("coordinate");
		
		return loginService.bindMobile(userId, mobile, valicode, inviteToken, realName, scholarship, ip, mac, coordinate, idCard);
	}

	@Override
	public Object isBindMobile(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		return loginService.isBindMobile(userId);
	}

	@Override
	public Object userTypes(Header header, Body body) throws IRpcException
	{
		String userId = header.getUserId();
		return loginService.userTypes(userId);
	}

	@Override
	public Object loginOut(Header header, Body body) throws IRpcException {
		AppSessionHolder.delSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
		return null;
	}

	@Override
	public Object getValicodeForUpdateMobile(Header header, Body body) throws IRpcException {
		return loginService.getValicodeForUpdateMobile(header,body);
	}

	@Override
	public Object updateMobile(Header header, Body body) throws IRpcException {
		return loginService.updateMobile(header,body);
	}

}
