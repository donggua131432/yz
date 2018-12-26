package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BsOrderApi {
	
	/**
	 * 我的订单列表
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bs",methodName="getOrderList",methodRemark="用户的订单列表",needLogin = true)
	Object getBsMyOrderInfo(Header header,Body body) throws IRpcException;
	
	/**
	 * 某个订单的详细
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bs",methodName="getOrderDetail",methodRemark="订单详细接口",needLogin = true)
	Object getBsOrderDetailInfo(Header header,Body body) throws IRpcException;
	
	/**
	 * 活动订单，添加队员信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bs",methodName="addActionMember",methodRemark="针对活动订单，添加队员信息",needLogin = true)
	Object insertBsActionMember(Header header,Body body) throws IRpcException;
	
	/**
	 * 针对抽奖,竞拍 中拍后 完善收货地址
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bs",methodName="fillOrderAddress",methodRemark="针对抽奖,竞拍 中拍后 完善收货地址", needLogin = true)
	Object completeOrderAddress(Header header,Body body) throws IRpcException;
	
}
