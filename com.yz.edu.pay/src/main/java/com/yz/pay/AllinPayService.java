package com.yz.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.pay.allinpay.AllinpayCertUtil;
import com.yz.pay.cert.Cert;
import com.yz.pay.cert.CertUtil;
import com.yz.pay.constants.PayConstants; 
import com.yz.util.BigDecimalUtil; 
import com.yz.util.JsonUtil; 

@Service
@Transactional
public class AllinPayService extends AbstractTranService {

	private static final Logger log = LoggerFactory.getLogger(AllinPayService.class);

	// 正式环境JSAPI地址
	private static final String JSAPI_CALL = "https://vsp.allinpay.com/apiweb/unitorder/pay";
	// 测试环境JSAPI地址
	// private static final String JSAPI_CALL =
	// "http://113.108.182.3:10080/apiweb/unitorder/pay";

	// 正式环境充值地址
	// private static final String
	// NATIVE_CALL="https://vsp.allinpay.com/apiweb/unitorder/pay";
	// 充值测试环境地址
	private static final String NATIVE_CALL = "https://vsp.allinpay.com/apiweb/unitorder/pay";
	
	@Value(value="${payment.allinpay.notify.url:''}")
	private String notify_url;
	
	@Value(value="${payment.wechat.qrcode.validtime:''}")
	private String validtime;
	
	@Autowired
	private YzPayCertService payCertService;

	@Override
	public Map<String, Object> addOrder(Map<String, String> postData) {

		TreeMap<String, String> params = new TreeMap<String, String>();


		String url = null;
		String tradeType = postData.get("tradeType");
		String payType = null;
		String body = null;

		String dealType = postData.get("dealType");

		Cert cert = new Cert();

		if (FinanceConstants.PAYMENT_DEAL_TYPE_TUITION.equals(dealType)) {
			cert = payCertService.getCert(PayConstants.ALLINPAY_JSAPI_CERTS);
			body = "远智教育学费";
			log.debug("------------------------------- 微信支付，学费缴纳");
		} else if (FinanceConstants.PAYMENT_DEAL_TYPE_ZM_RECHARGE.equals(dealType)) {
			cert = payCertService.getCert(PayConstants.ALLINPAY_NATIVE_CERTS);
			body = "远智教育智米";
			log.debug("------------------------------- 微信支付，智米充值");
		}else if (FinanceConstants.PAYMENT_DEAL_TYPE_REPT.equals(dealType)) {
			cert = payCertService.getCert(PayConstants.ALLINPAY_NATIVE_CERTS);
			body = "远智教育收据快递费用";
			log.debug("------------------------------- 微信支付，收据缴费");
		}

		
		if (FinanceConstants.TRADE_TYPE_JSAPI.equalsIgnoreCase(tradeType)) {
			url = JSAPI_CALL;
			payType = PayConstants.ALLINPAY_PAYMENT_TYPE_WECHAT_JSAPI;
		} else if (FinanceConstants.TRADE_TYPE_NATIVE.equalsIgnoreCase(tradeType)) {
			url = NATIVE_CALL;
			payType = PayConstants.ALLINPAY_PAYMENT_TYPE_WECHAT_NATIVE;
		}
		log.info("-----微信支付，学费缴纳--------------学员信息:openId="+postData.get("openId"));
		// 支付日志
	 

		params.put("cusid", cert.getMerchantId());
		params.put("appid", cert.getAppId());

		String amount = BigDecimalUtil.multiply(postData.get("orderAmount"), "100");

		params.put("trxamt", amount.substring(0, amount.indexOf(".")));
		params.put("reqsn", postData.get("serialNo"));
		params.put("paytype", payType);
		params.put("randomstr", UUID.randomUUID().toString().replace("-", ""));
		params.put("body", body);
		params.put("remark", null);
		if (FinanceConstants.TRADE_TYPE_JSAPI.equalsIgnoreCase(tradeType)) {
			params.put("acct", postData.get("openId"));
		} else if (FinanceConstants.TRADE_TYPE_NATIVE.equalsIgnoreCase(tradeType)) {
			params.put("validtime", validtime);
		}

		params.put("notify_url", notify_url+ "/" + postData.get("dealType") + ".do");
		params.put("limit_pay", "");

		String sign = AllinpayCertUtil.sign(params, cert.getCertId());
		params.put("sign", sign);

	 

		log.info("-------------------------- 字符集：" + System.getProperty("file.encoding"));

		log.info("---------- sign data : " + sign);

		log.info("---------------  通联支付下单参数：" + JsonUtil.object2String(params));

		String resultStr = HttpUtil.sendPost(url, AllinpayCertUtil.spliceParams(params),HttpTraceInterceptor.TRACE_INTERCEPTOR);

		log.info("------ 通联创建订单  : " + resultStr);

		Map<String, Object> resultMap = JsonUtil.str2Object(resultStr, Map.class);

		if (null != resultMap && "SUCCESS".equals(resultMap.get("retcode"))) {

			if (resultMap.get("trxstatus").equals("0000")) {
				TreeMap tmap = new TreeMap();
				tmap.putAll(resultMap);

				if (AllinpayCertUtil.validSign(tmap, cert.getCertId())) {
				 
					Map<String, Object> res = new HashMap<String, Object>();
					Object payinfo = null;
					if (FinanceConstants.TRADE_TYPE_JSAPI.equalsIgnoreCase(tradeType)) {
						payinfo = JsonUtil.str2Object((String) tmap.get("payinfo"), Map.class);
					} else if (FinanceConstants.TRADE_TYPE_NATIVE.equalsIgnoreCase(tradeType)) {
						payinfo = (String) tmap.get("payinfo");
					}
					res.put("payInfo", payinfo);
					return res;
				} else {
					resultMap.put("resCode", GlobalConstants.FAILED);
					resultMap.put("errorMsg", "签名验证失败");
					// 插入支付日志
					return resultMap;
				}

			} else {

				resultMap.put("resCode", resultMap.get("trxstatus"));
				resultMap.put("errorMsg", resultMap.get("签名验证失败"));
				// 插入支付日志
				return resultMap;
			}

		} else {
			resultMap.put("resCode", GlobalConstants.FAILED);
			resultMap.put("errorMsg", resultMap.get("retmsg"));
			// 插入支付日志
			return resultMap;
		}

	}
 

	@Override
	public CertUtil getCertUtil() {
		return null;
	}

}
