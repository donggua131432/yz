package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * 抽奖活动api Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年3月7日.
 *
 */
public interface GsLotteryApi {

	/**
	 * 获取一次抽奖机会
	 * @param userId
	 * @throws IRpcException
	 */
	void gainLoterryTicket(String userId) throws IRpcException;

	/**
	 * 用户注册时赠送抽奖机会
	 * @param userId
	 * @throws IRpcException
	 */
	void gainLoterryTicket(String userId, String pId, String realName) throws IRpcException;

	/**
	 * 获取抽奖首页信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getLotteryInfo", methodRemark = "获取抽奖首页信息", needLogin = true)
	Object getLotteryInfo(Header header, Body body) throws IRpcException;

	/**
	 * 抽奖接口
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "lottery", methodRemark = "抽奖接口", needLogin = true)
	Object lottery(Header header, Body body) throws IRpcException;

	/**
	 * 首页轮播中奖信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="getWinningInfo",methodRemark="首页轮播中奖信息")
	Object getWinningInfo(Header header, Body body) throws IRpcException;

	/**
	 * 插入奖品收货地址
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "setPrizeAddress", methodRemark = "插入奖品收货地址", needLogin = true)
	void setPrizeAddress(Header header, Body body) throws IRpcException;

	/**
	 * 获取所有抽奖记录
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getAllWinningInfo", methodRemark = "获取所有抽奖记录")
	Object getAllWinningInfo(Header header, Body body) throws IRpcException;
}