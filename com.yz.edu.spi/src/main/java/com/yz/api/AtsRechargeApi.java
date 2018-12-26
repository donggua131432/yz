package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface AtsRechargeApi {
	/**
	 * 【PUB】获取充值产品列表
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="ats",methodName="productList",methodRemark="智米充值产品列表",needLogin =true)
	public Object getProductList(Header header, Body body) throws IRpcException;
	
	/**
	 * 【PUB】充值
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="ats",methodName="recharge",methodRemark="智米充值",needLogin =true)
	public Object recharge(Header header, Body body) throws IRpcException;
	
	/**
	 * 【PRI】充值
	 * @param recordsNo 充值记录编号
	 * @param isSuccess 是否成功
	 * @param outSerialNo 第三方流水号
	 * @return
	 */
	@YzService(sysBelong="ats",methodName="rechargeCallBack",methodRemark="【PRI】充值回调")
	public boolean rechargeCallBack(Header header, Body body) throws IRpcException;
}
