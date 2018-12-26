package com.yz.job.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsRechargeApi;
import com.yz.api.BdsApplyApi;
import com.yz.api.BdsPaymentApi;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants; 
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.YzBadPaymentMapper;
import com.yz.job.model.YzBadPayment;
import com.yz.model.UserPaySuccessEvent;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;

@Service(value = "userPaySuccesService")
public class UserPaySuccesService {

	private Logger logger = LoggerFactory.getLogger(UserPaySuccesService.class);

	@Autowired
	private YzBadPaymentMapper badPaymentMapper;

	@Reference(version = "1.0")
	private BdsPaymentApi bdsPaymentApi;

	@Reference(version = "1.0")
	private AtsRechargeApi atsRechargeApi;

	@Reference(version = "1.0")
	private BdsApplyApi bdsApplyApi;

	/**
	 * 
	 * @desc 支付成功 处理
	 * @param event
	 */
	public void paySuccess(UserPaySuccessEvent event) {
		try {
			String dealType = event.getDealType();
			Header header = new Header();
			Body body = this.initPayBody(event);
			switch (dealType) {
			case FinanceConstants.PAYMENT_DEAL_TYPE_TUITION:
				bdsPaymentApi.paySuccess(header, body);
				YzTaskContext.getTaskContext().addEventDetail(event.getSerialNo(), "缴费成功");
				break;
			case FinanceConstants.PAYMENT_DEAL_TYPE_ZM_RECHARGE:
				atsRechargeApi.rechargeCallBack(header, body);
				YzTaskContext.getTaskContext().addEventDetail(event.getSerialNo(), "充值成功");
				break;
			case FinanceConstants.PAYMENT_DEAL_TYPE_REPT:
				bdsApplyApi.reptExpressPaymentCallBack(header, body);
				YzTaskContext.getTaskContext().addEventDetail(event.getSerialNo(), "支付快递费成功");
				break;
			default:
				logger.error("dealType is vaild!!微信交易失败，参数:{}", JsonUtil.object2String(event));
				break;
			}
		} catch (Exception ex) {
			YzTaskContext.getTaskContext().addEventDetail(event.getSerialNo(), "交易失败;msg【" + ex.getMessage() + "】");
			logger.error("回调失败，参数:{}，异常信息：{}", JsonUtil.object2String(event), ExceptionUtil.getStackTrace(ex));
			YzBadPayment badPayment = new YzBadPayment();
			badPayment.setAmount(new BigDecimal(event.getAmount()));
			badPayment.setDealType(event.getDealType());
			badPayment.setPayNo(event.getSerialNo());
			badPayment.setTransNo(event.getOutOrderNo());
			badPayment.setPayType(event.getPayType());
			badPayment.setErrorMsg(ExceptionUtil.getStackTrace(ex));
			badPayment.setDealAddr(OSUtil.getIp());
			this.badPaymentMapper.saveBadPayment(badPayment);
		}
	}

	/**
	 * ?????? 组装body参数？？？？
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Body initPayBody(UserPaySuccessEvent event) {
		Body body = new Body();
		body.put("serialNo", event.getSerialNo());
		body.put("outOrderNo", event.getOutOrderNo());
		body.put("amount", event.getAmount());
		body.put("payType", event.getPayType());
		body.put("isSuccess", GlobalConstants.TRUE);
		return body;
	}
}
