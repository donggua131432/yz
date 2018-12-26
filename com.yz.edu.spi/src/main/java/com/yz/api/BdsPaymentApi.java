package com.yz.api;


import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsPaymentApi {

	/**
	 * 学员缴费流水
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="studentSerials",methodRemark="缴费记录",needLogin=true)
	public Object studentSerials(Header header, Body body) throws IRpcException;

	/**
	 * 学员可用优惠券
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="myCoupons",methodRemark="我的优惠券",needLogin=true)
	public Object myCoupons(Header header, Body body) throws IRpcException;

	/**
	 * 学员缴费信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="payableInfo",methodRemark="学员待缴信息",needLogin=true)
	public Object payableInfo(Header header, Body body) throws IRpcException;

	
	/**
	 * 学员录取通页缴费信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stuEnrollpayableInfo",methodRemark="学员录取通页缴费信息",needLogin=true)
	public Object stuEnrollpayableInfo(Header header, Body body) throws IRpcException;
	/**
	 * 缴费可用优惠券
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="availableCoupon",methodRemark="支付可用优惠券",needLogin=true)
	public Object availableCoupon(Header header, Body body) throws IRpcException;

	/**
	 * 在线支付回调成功处理
	 * 
	 * @param serialNo
	 *            支付流水号
	 * @param outSerialNo
	 *            第三方订单号
	 * @param payType
	 *            支付方式
	 * @param amount
	 *            支付金额
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="paySuccess",methodRemark="在线支付回调成功处理",timeout=3000*10)
	public boolean paySuccess(Header header, Body body) throws IRpcException;

	/**
	 * 在线支付回调失败处理
	 * 
	 * @param serialNo
	 *            支付流水号
	 * @param outSerialNo
	 *            第三方订单号
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="payFailed",methodRemark="在线支付回调失败处理",timeout=3000*10)
	public boolean payFailed(Header header, Body body) throws IRpcException;

	/**
	 * 学员缴纳学费
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stdPayTuition",methodRemark="学员缴费",needLogin=true)
	public Object stdPayTuition(Header header, Body body) throws IRpcException;

	/**
	 * 提现记录
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="withdrawSerial",methodRemark="学员提现记录",needLogin=true)
	public Object withdrawSerial(Header header, Body body) throws IRpcException;

	/**
	 * 获取微信jsapi掉起参数
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="jsapiSign",methodRemark="获取微信jsapi签名参数",needLogin=true)
	public Object jsapiSign(Header header, Body body) throws IRpcException;
	
	/**
	 * 二维码扫描支付
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stdPayTuitionByQRCode",methodRemark="二维码扫描支付",needLogin=true)
	public Object stdPayTuitionByQRCode(Body body) throws IRpcException;
}
