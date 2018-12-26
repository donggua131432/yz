package com.yz.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

  
import com.yz.constants.GlobalConstants;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.pay.cert.Cert;
import com.yz.pay.cert.CertUtil;
import com.yz.pay.constants.PayConstants; 
import com.yz.pay.model.WechatHtmlPayInfo;
import com.yz.pay.model.WechatOrderInfo;
import com.yz.pay.model.WechatRedPacketInfo;
import com.yz.pay.model.WechatTradeInfo;
import com.yz.pay.wechat.WeChatConstants;
import com.yz.pay.wechat.WechatCertUtil;
import com.yz.util.BigDecimalUtil;  
import com.yz.util.OSUtil;
import com.yz.util.XmlUtil;

@Service
@Transactional
public class WechatService extends AbstractTranService {

	private static final String CALL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	private static final String RED_POCKET_CALL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

	private static final Logger log = LoggerFactory.getLogger(WechatService.class);
	
	@Value(value="${payment.wechat.notify.url:''}")
	private String url; 
	
	@Autowired
	private YzPayCertService payCertService;
	

	@Override
	public Map<String, Object> addOrder(Map<String, String> postData) {
		// 微信支付日志
		Cert cert = payCertService.getCert(PayConstants.WECHAT_CERTS);
		WechatOrderInfo orderInfo = new WechatOrderInfo();
		orderInfo.setAppId(cert.getAppId());
		orderInfo.setMchId(cert.getMerchantId());
		orderInfo.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
		String amount = BigDecimalUtil.multiply(postData.get("orderAmount"), "100");
		orderInfo.setBody("远智教育"); // TODO
		orderInfo.setOutTradeNo(postData.get("serialNo")); // 流水交易号
		orderInfo.setTotalFee(amount.substring(0, amount.indexOf(".")));
		log.info("------------------------微信支付金额为：" + amount.substring(0, amount.indexOf(".")));
		orderInfo.setSpbillCreateIp(OSUtil.getIp());

		orderInfo.setNotifyUrl(url + "/" + postData.get("dealType") + ".do");
		String tradeType = postData.get("tradeType");
		if (null != tradeType && tradeType.equalsIgnoreCase(WeChatConstants.TRADE_TYPE)) {
			orderInfo.setTradeType(tradeType);
			orderInfo.setOpenId(postData.get("openId"));
		} else {
			orderInfo.setTradeType(tradeType);
			orderInfo.setProductId(postData.get("productId"));
		}
		log.info("-------------------------- 字符集：" + System.getProperty("file.encoding"));

		Map<String, String> signData = certUtil.signData(cert, orderInfo);

		log.info("---------- sign data : " + signData);

		String xmlString = XmlUtil.map2Xml(signData, "xml");

		log.info("---------- xmlString : " + xmlString);

		String resultStr = HttpUtil.sendPost(CALL, xmlString, "text/xml; charset=UTF-8",HttpTraceInterceptor.TRACE_INTERCEPTOR);

		log.info("------ 微信创建订单  : " + resultStr);

		Map<String, String> resultMap = XmlUtil.xml2Map(resultStr);
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String resultCode = resultMap.get(WeChatConstants.resp_result_code);

		if (GlobalConstants.SUCCESS.equals(resultCode)) {
			if (certUtil.validate(cert, resultMap)) {
				if (null != tradeType && tradeType.equals(WeChatConstants.TRADE_TYPE)) {
					WechatHtmlPayInfo payInfo = new WechatHtmlPayInfo();

					payInfo.setAppId(cert.getAppId());
					payInfo.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
					payInfo.setTimeStamp(System.currentTimeMillis() / 1000 + "");
					payInfo.setPackage(
							WeChatConstants.resp_prepay_id + "=" + resultMap.get(WeChatConstants.resp_prepay_id));
					payInfo.setSignType("MD5");
					Map<String, String> rt = certUtil.signData(cert, payInfo);
					log.debug("---------------------------微信下单成功返回参数：" + rt);
					String sign = rt.get("sign");
					rt.remove("sign");
					rt.put("paySign", sign);
					
					responseMap.put("payInfo", rt);
					return responseMap;
				} else {
					// 二维码支付
					Map<String, String> rt = new HashMap<>();
					rt.put("codeUrl", resultMap.get(WeChatConstants.param_code_url));
					responseMap.put("payInfo", rt);
					return responseMap;
				}

			} else {
				resultMap.put("resCode", GlobalConstants.FAILED);
				resultMap.put("errorMsg", resultMap.get("err_code_des"));
				responseMap.put("payInfo", resultMap);
				return responseMap;
			}
		} else {
			resultMap.put("resCode", GlobalConstants.FAILED);
			resultMap.put("errorMsg", resultMap.get("return_msg"));
			responseMap.put("payInfo", resultMap);
			return responseMap;
		}
	}

	/**
	 * 微信发送红包
	 * 
	 * @param postData
	 * @return
	 */
	public Map<String, String> sendRedPacket(Map<String, String> postData) {
		Cert cert = payCertService.getCert(PayConstants.WECHAT_RED_POCKET_CERTS);
		WechatRedPacketInfo orderInfo = new WechatRedPacketInfo();
		orderInfo.setWxAppId(cert.getAppId());
		orderInfo.setMchId(cert.getMerchantId());
		orderInfo.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
		String amount = BigDecimalUtil.multiply(postData.get("orderAmount"), "100");
		orderInfo.setTotalAmount(amount.substring(0, amount.indexOf(".")));
		log.info("------------------------微信支付金额为：" + amount.substring(0, amount.indexOf(".")));
		orderInfo.setClientIp(OSUtil.getIp());
		orderInfo.setActName("学员提现");
		orderInfo.setMchBillNo(postData.get("billNo"));
		orderInfo.setReOpenid(postData.get("openId"));
		orderInfo.setSceneId("PRODUCT_3");
		orderInfo.setSendName("远智教育");
		orderInfo.setTotalNum("1");
		orderInfo.setWishing("学员提现");

		 

		Map<String, String> signData = certUtil.signData(cert, orderInfo);

		log.debug("---------- sign data : " + signData);

		String xmlString = XmlUtil.map2Xml(signData, "xml");

		log.debug("---------- xmlString : " + xmlString);

		String resultStr = HttpUtil.sendPost(RED_POCKET_CALL, xmlString, "text/xml; charset=UTF-8",HttpTraceInterceptor.TRACE_INTERCEPTOR);

		log.debug("------ 微信创建订单  : " + resultStr);

		Map<String, String> resultMap = XmlUtil.xml2Map(resultStr);

		String resultCode = resultMap.get(WeChatConstants.resp_result_code);

		if (GlobalConstants.SUCCESS.equals(resultCode)) {
			if (certUtil.validate(cert, resultMap)) {

				WechatTradeInfo payInfo = new WechatTradeInfo();

				payInfo.setAppid(cert.getAppId());
				payInfo.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
				payInfo.setPartnerid(cert.getMerchantId());
				// payInfo.setPrepayid(resultMap.get(WeChatConstants.resp_prepay_id));
				payInfo.setTimestamp(System.currentTimeMillis() / 1000 + "");
				Map<String, String> rt = certUtil.signData(cert, payInfo);
				rt.put("resCode", WeChatConstants.SUCCESS);
				return rt;
				// return XmlUtil.map2Xml(rt, "xml");
			} else {
				resultMap.put("resCode", GlobalConstants.FAILED);
				resultMap.put("errorMsg", resultMap.get("err_code_des"));
				return resultMap;
			}
		} else {
			resultMap.put("resCode", GlobalConstants.FAILED);
			resultMap.put("errorMsg", resultMap.get("return_msg"));
			return resultMap;
		}
	}

	@Override
	public CertUtil getCertUtil() {
		return new WechatCertUtil();
	}

}
