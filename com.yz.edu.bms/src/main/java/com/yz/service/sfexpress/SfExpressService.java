package com.yz.service.sfexpress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.constants.ReptConstants;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.sf.SFExpressRequest;
import com.yz.util.SFUtil;
import com.yz.util.XmlUtil;

@Service
public class SfExpressService {

	@Value("${sf.orderUrl}")
	private String orderUrl;

	@Value("${sf.checkword}")
	private String checkword;

	@Value("${sf.head}")
	private String head;

	@Value("${sf.finance_custid}")
	private String financeCustid;

	@Value("${sf.zhimi_custid}")
	private String zhimiCustid;

	@Value("${sf.sendbook_custid}")
	private String sendBookCustid;

	/**
	 * 顺丰下单接口
	 * 
	 * @param sendPart
	 *            寄件部门 1-智米中心 2-财务 常量：ReptConstants.SF_ORDER_SENDER_ZHIMI
	 * 
	 */
	public Map<String, String> sfOrder(SFExpressRequest order, String sendPart) {
		order.setHead(head);

		String custid = null;

		if (ReptConstants.SF_ORDER_SENDER_ZHIMI.equals(sendPart)) {
			custid = zhimiCustid;
		} else if (ReptConstants.SF_ORDER_SENDER_FINANCE.equals(sendPart)) {
			custid = financeCustid;
		} else if (ReptConstants.SF_ORDER_SENDER_SENDBOOK.equals(sendPart)) {
			custid = sendBookCustid;
		}

		order.setCustid(custid);
		String param = SFUtil.setSFXml2String(order);

		String verifyCode = SFUtil.md5EncryptAndBase64(param + checkword);

		/* String result = HttpUtil.sendPost(orderUrl, xml); */

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", param));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		String stringParam = URLEncodedUtils.format(nvps, Consts.UTF_8);
		String result = HttpUtil.sendPost(orderUrl, stringParam, HttpTraceInterceptor.TRACE_INTERCEPTOR);
		Map<String, String> resp = XmlUtil.readSFXml(result);
		resp.put("custid", custid);
		return resp;

	}

}
