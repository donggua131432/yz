package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

import java.util.Map;

public interface BdsStudentApi {
	/**
	 * 【PUB】获取验证码
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getAuthCode",methodRemark="获取验证码",needLogin=true)
	public Object getAuthCode(Header header, Body body) throws IRpcException;

	/**
	 * 【PUB】验证学员身份
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="authStudent",methodRemark="验证学员身份",needLogin=true)
	public Object authStudent(Header header, Body body) throws IRpcException;

	/**
	 * 【PUB】获取入学信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getEnrolls",methodRemark="获取报读信息列表",needLogin=true)
	public Object getEnrolls(Header header, Body body) throws IRpcException;

	/**
	 * 【PUB】获取原学历信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getHistoryInfo",methodRemark="获取学历信息",needLogin=true)
	public Object getHistory(Header header, Body body) throws IRpcException;

	/**
	 * 【PUB】更新学历信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="updateHistory",methodRemark="更新学历信息",needLogin=true)
	public Object updateHistory(Header header, Body body) throws IRpcException;

	/**
	 * 【PUB】提交身份证和
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="completeEnroll",methodRemark="上传学员身份证与毕业证",needLogin=true)
	public Object completeEnroll(Header header, Body body) throws IRpcException;

	/**
	 * 注册送优惠券
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 * @throws IRpcException
	 */
	public void sendCoupon(String userId) throws IRpcException;

	/**
	 * 根据手机号获取学员信息
	 *
	 */
	Object getStudentByMobile(String mobile) throws IRpcException;

	/**
	 * 获取学籍信息异动信息
	 * 
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getStudentModify",methodRemark="获取学籍信息异动信息",needLogin=true)
	public Map<String, String> getStudentModify(Header header, Body body) throws IRpcException;

	/**
	 * 增加学籍信息异动信息
	 * 
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="addStudentModify",methodRemark="增加学籍信息异动信息",needLogin=true)
	public void addStudentModify(Header header, Body body) throws IRpcException;

	/**
	 * 抽奖优惠券插入
	 * 
	 * @param userId
	 * @param couponId
	 * @throws IRpcException
	 */
	public void sendLotteryCoupon(String userId, String couponId) throws IRpcException;

	/**
	 * 判断是否符合抽奖条件
	 * @param mobile
	 * @return
	 * @throws IRpcException
	 */
	public boolean checkLotteryCondition(String mobile) throws IRpcException;
}
