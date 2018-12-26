package com.yz.api;

import java.util.List;

import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface UsInfoApi {
	/**
	 * 【PUB】完善资料
	 * @param header
	 * @param body
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="completeInfo",methodRemark="完善资料",needLogin=true)
	void completeInfo(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】查询用户信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="userInfo",methodRemark="我的资料",needLogin=true)
	Object getOtherInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 【PUB】查询我的粉丝信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="myFans",methodRemark="查询我的粉丝",needLogin=true)
	Object myFans(Header header, Body body) throws IRpcException;
	
	/**
	 * 【PRI】通过user获取openId
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	Object getOpenIdByUserId(Body body) throws IRpcException;
	
	/**
	 * 批量获取openIds
	 * @return
	 * @throws IRpcException
	 */
	List<String> getOpenIdsByUserIds(List<String> userIds) throws IRpcException;


	/**
	 * 获取最新注册学员信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="getNewRegList",methodRemark="获取最新注册学员信息")
	public Object getNewRegList(Header header, Body body) throws IRpcException;
	
}
