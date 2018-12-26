package com.yz.pay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.constants.FinanceConstants; 
import com.yz.exception.IRpcException;
import com.yz.pay.constants.PaymentConstants;

@Service
public class GwPaymentService  {

	@Autowired
	private WechatService wechatService;

	@Autowired
	private AllinPayService allinpayService;
	
	@Autowired
	private AllinPayRouteService routeService;
	
	@Value(value="${payment.wechat.invoke.type:2}")
	private String wechatInvokeType;

	public Map<String, Object> payment(Map<String, String> postData) throws IRpcException {
		if (FinanceConstants.PAYMENT_TYPE_WECHAT.equals(postData.get("payType"))) {
			if (PaymentConstants.WECHAT_PAYMENT_INVOKE_TYPE_ALLINPAY.equals(wechatInvokeType)) {
				return allinpayService.addOrder(postData);
			} else if (PaymentConstants.WECHAT_PAYMENT_INVOKE_TYPE_WECHAT.equals(wechatInvokeType)) {
				return wechatService.addOrder(postData);
			}
		}
		return null;
	}

	public Map<String, String> sendRedPacket(Map<String, String> postData) throws IRpcException {
		return wechatService.sendRedPacket(postData);
	}

	public Map<String, String> cutOff(Map<String, String> postData) throws IRpcException {
		return routeService.addOrder(postData);
	}

}
