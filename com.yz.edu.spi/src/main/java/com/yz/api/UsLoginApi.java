package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface UsLoginApi {
	/**
	 * 【PUB】签到
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="sign",methodRemark="签到",needLogin=true)
	Object sign(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】当天是否已签到
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="isSign",methodRemark="判断当天是否签到",needLogin=true)
	Object isSign(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】邀约人登录
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="login",methodRemark="登录接口")
	Object login(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】获取短信验证码
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	
	@YzService(sysBelong="us",methodName="authCode",methodRemark="发送短信验证码")
	Object getValicode(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】注册邀约人
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="register",methodRemark="注册、绑定手机号")
	Object register(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】用户授权
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="auth",methodRemark="用户授权")
	Object auth(Header header, Body body) throws IRpcException;
	/**
	 * 绑定手机号
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="bindMobile",methodRemark="绑定手机号码",needLogin=true)
	Object bindMobile(Header header, Body body) throws IRpcException;
	/**
	 * 是否绑定手机号
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="isBindMobile",methodRemark="是否绑定手机号码",needLogin=true)
	Object isBindMobile(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取用户类型
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="userTypes",methodRemark="获取用户类型信息",needLogin=true)
	Object userTypes(Header header, Body body) throws IRpcException;
	
	/**
	 * 用户退出接口
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="loginOut",methodRemark="APP学员退出",needLogin=true)
	Object loginOut(Header header, Body body) throws IRpcException;
	
	/**
	 * 修改手机号获取验证码
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="getValicode",methodRemark="修改手机发送短信验证码")
	Object getValicodeForUpdateMobile(Header header, Body body) throws IRpcException;
	
	/**
	 * 修改手机号码
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="updateMobile",methodRemark="修改手机发送短信验证码", needLogin = true)
	Object updateMobile(Header header, Body body) throws IRpcException;
}
