package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.AtsRechargeService;

@Service(version="1.0", timeout=30000, retries=0)
public class AtsRechargeApiImpl implements AtsRechargeApi {
	
	@Autowired
	private AtsRechargeService rechargeService;

	@Override
	public Object getProductList(Header header, Body body) throws IRpcException {
		return rechargeService.getProductList();
	}

	@Override
	public Object recharge(Header header, Body body) throws IRpcException {
		String productId = body.getString("productId");
		String userId = header.getUserId();
		String openId = header.getOpenId();
		
		return rechargeService.recharge(productId, userId, openId);
	}

	@Override
	public boolean rechargeCallBack(Header header, Body body) throws IRpcException {
		// TODO Auto-generated method stub
		String recordsNo = body.getString("serialNo"); 
		String isSuccess = body.getString("isSuccess");
		String outSerialNo = body.getString("outOrderNo");
		return rechargeService.rechargeCallBack(recordsNo, isSuccess, outSerialNo);
	}

}
