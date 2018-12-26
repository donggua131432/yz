package com.yz.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.service.UsCertService;
import com.yz.util.Assert;

@Service(version = "1.0", timeout = 30000, retries = 0,async=true)
public class UsCertApiImpl implements UsCertApi {
	
	@Autowired
	private UsCertService certService;
/*
	@Override
	public Object bindCert(Header header, Body body) throws IRuntimeException {
		String userId = header.getUserId();
		String certNo = body.getString("certNo");
		String certType = body.getString("certType");
		String name = body.getString("name");
		
		Assert.hasText(userId, "用户Id不能为空");
		Assert.hasText(certNo, "证件号码不能为空");
		Assert.hasText(certType, "证件类型不能为空");
		
		return certService.bindCert(userId, name, certNo, certType, false);
	}
	
	

	@Override
	public Object getBindStatus(Header header, Body body) throws IRuntimeException {
		String userId = header.getUserId();
		String certType = body.getString("certType");
		
		Assert.hasText(userId, "用户Id不能为空");
		Assert.hasText(certType, "证件类型不能为空");
		return certService.getBindStatus(userId, certType);
	}



	@Override
	public Object isBindCert(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		String certType = body.getString("certType");
		
		Assert.hasText(userId, "用户Id不能为空");
		Assert.hasText(certType, "证件类型不能为空");
		
		return certService.isBindCert(userId, certType);
	}*/



	@Override
	public void createRelation(Body body) {
		
		String type = body.getString("type");
		
		Assert.hasText(type, "绑定类型不能为空 STD-学员身份绑定，EMP-员工身份绑定");
		type = type.toUpperCase();
		
		if("STD".equals(type)) {
			
			String stdId = body.getString("stdId");
			String idCard = body.getString("idCard");
			String userId = body.getString("userId");
			Map<String, String> recruitMap = (Map<String, String>) body.get("recruitMap");
			
			Assert.hasText(stdId, "学员ID不能为空");
			Assert.hasText(idCard, "身份证不能为空");
			Assert.hasText(userId, "用户ID不能为空");
			
			certService.createStdRelation(stdId, idCard, userId, recruitMap);
		} else if("EMP".equals(type)) {
			String yzCode = body.getString("yzCode");
			String empId = body.getString("empId");
			
			Assert.hasText(yzCode, "远智编号不能为空");
			Assert.hasText(empId, "员工ID不能为空");
			
			certService.createEmpRelation(yzCode, empId);
		}
	}

	@Override
	public Map<String, String> refreshFollow(Body body) throws IRpcException {
		return certService.refreshFollow(body);
		
	}

	@Override
	public void clearFollow(String empId) {
		certService.clearFollow(empId);
	}

}
