package com.yz.enums;

public enum ReturnStatus {
	/**
	 * 操作成功
	 */
	Success(1),
	/*-----------------------------------登陆错误--------------------------------------------*/
	/**
	 * 登陆过期（需要重新登陆）
	 */
	LoginError(-1),
	/**
	 * 登陆 密码错误
	 */
	LoginError_1(401),
	/**
	 * 手机号未注册
	 */
	LoginError_2(402),
	/**
	 * accessToken过期
	 */
	LoginError_3(403),
	/*-----------------------------------参数错误--------------------------------------------*/
	/**
	 * 参数错误	
	 */
	ParamError(200),
	/**
	 * 参数不全、参数为空
	 */
	ParamError_1(201),
	/**
	 * 危险参数
	 */
	ParamError_2(202),
	/*-----------------------------------系统错误--------------------------------------------*/
	/**
	 * 系统错误
	 */
	SystemError(300),
	/**
	 * 非法用户（用户不存在）
	 */
	SystemError_1(301),
	/**
	 * 账户余额不足
	 */
	CashError(302),
	
	/*-----------------------------------验证码错误--------------------------------------------*/
	/**
	 * 验证码失效
	 */
	VcodeError_1(101),
	/**
	 * 验证码错误
	 */
	VcodeError_2(102),
	/**
	 * 手机号已经注册（绑定手机号时）
	 */
	VcodeError_3(103),
	/**
	 * 手机号未注册（密码找回时用）
	 */
	VcodeError_4(104),
	/*------------------------------------------------------------------------------------------*/
	/**
	 * 订单参数不全/有误
	 */
	OrderError(500),
	/**
	 * 订单拆单后有误（用户订单列表为null）
	 */
	OrderError_1(501),
	/**
	 * 
	 */
	OrderError_2(502),
	/*----------------------------协同编辑错误------------------------------------------------*/
	/**
	 * 协同编辑错误
	 */
	InvitError_1(601)
	
	;

	private final int step;

	private ReturnStatus(int step) {

		this.step = step;
	}

	public int getValue() {
		return step;
	}

	@Override
	public String toString() {
		return String.valueOf(this.step);
	}
}
