package com.yz.api;

import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsApplyApi {
	
	/**
	 * 查询学员申请信息
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="myApplies",methodRemark="我的申请",needLogin=true)
	public Object stduentApplyInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 学员提现申请
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="studentWithdrawApply",methodRemark="学员提现",needLogin=true)
	public void studentWithdrawApply(Header header, Body body) throws IRpcException;
	
	/**
	 * 学员收据申请
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="studentReptApply",methodRemark="学员收据申请",needLogin=true)
	public String studentReptApply(Header header, Body body) throws IRpcException;
	
	/**
	 * 校区地址查询
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getCampusInfo",methodRemark="获取所有校区地址",needLogin=true)
	public Object getCampusInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 收据申请快递费缴纳
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="reptExpressPay",methodRemark="收据快递费微信支付",needLogin=true)
	public Object reptExpressPay(Header header, Body body) throws IRpcException;
	
	/**
	 * 收据申请快递费支付回调
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="applyApi",methodRemark="收据申请快递费支付回调",needLogin=true)
	public boolean reptExpressPaymentCallBack(Header header, Body body) throws IRpcException;

	/**
	 * 学员证明申请
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="studentCertificateApply",methodRemark="学员证明申请",needLogin=true)
	public void studentCertificateApply(Header header, Body body) throws IRpcException;

	/**
	 * 获取证明申请
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getCertificateApply",methodRemark="获取证明申请",needLogin=true)
	public Object getCertificateApply(Header header, Body body) throws IRpcException;
}
