package com.yz.service;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GwConstants;
import com.yz.exception.BusinessException;
import com.yz.model.UserPaySuccessEvent;
import com.yz.pay.allinpay.AllinpayCertUtil;
import com.yz.pay.cert.Cert;
import com.yz.pay.constants.PayConstants;
import com.yz.pay.wechat.WechatCertUtil;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @author cyf
 *
 */
@Service
public class TransCallBackService {

	private static final Logger log = LoggerFactory.getLogger(TransCallBackService.class);

	private WechatCertUtil wechatCertUtil = new WechatCertUtil();

	private AllinpayCertUtil allinpayCertUtil = new AllinpayCertUtil();

	@Autowired
	private BsCertInfoService bsCertInfoService;

	/**
	 * 
	 * @param payType
	 *            支付类型
	 * @param excuteType
	 * @param m
	 * @throws BusinessException
	 * @throws InterruptedException
	 */
	public void process(String payType, String excuteType, Map<String, String> m)
			throws BusinessException, InterruptedException {
		sendPayEvent(m, payType, excuteType);
	}

	/**
	 * 在线支付回调函数
	 * 
	 * @param postData
	 * @param payType
	 * @param dealType
	 * @return
	 * @throws InterruptedException
	 * @throws BusinessException
	 */
	public void sendPayEvent(Map<String, String> postData, String payType, String dealType) throws BusinessException {
		TransCallBackInvoker invoker = null;
		UserPaySuccessEvent event = new UserPaySuccessEvent();
		if (GwConstants.PAY_TYPE_WECHAT.equals(payType)) {
			// 第三方订单号
			String outOrderNo = postData.get("transaction_id");
			// 交易流水号
			String serialNo = postData.get("out_trade_no");
			String amount = postData.get("total_fee");
			event.setPayType(FinanceConstants.PAYMENT_TYPE_WECHAT);
			event.setAmount(amount);
			event.setOutOrderNo(outOrderNo);
			event.setSerialNo(serialNo);
			event.setDealType(dealType);
			invoker = new TransCallBackInvoker(event) {
				@Override
				public boolean validate() {
					if (StringUtil.equalsIgnoreCase("SUCCESS", postData.get("return_code"))) {
						Cert cert = bsCertInfoService.getCert(PayConstants.WECHAT_CERTS);
						return wechatCertUtil.validate(cert, postData);
					}
					return false;
				}
			};
		} else if (GwConstants.PAY_TYPE_ALLINPAY.equals(payType)) {
			// 第三方订单号
			String outOrderNo = postData.get("chnltrxid");
			log.debug("----------------第三方订单号:" + outOrderNo);
			// 交易流水号
			String serialNo = postData.get("cusorderid");
			String amount = postData.get("trxamt");

			event.setPayType(GwConstants.PAY_TYPE_ALLINPAY);
			event.setAmount(amount);
			event.setOutOrderNo(outOrderNo);
			event.setSerialNo(serialNo);
			event.setDealType(dealType);
			invoker = new TransCallBackInvoker(event) {
				@Override
				public boolean validate() {
					if (StringUtil.equalsIgnoreCase("0000", postData.get("trxstatus"))) {
						TreeMap<String, String> tmap = Maps.newTreeMap();
						tmap.putAll(postData);
						Cert cert = null;
						if (FinanceConstants.PAYMENT_DEAL_TYPE_TUITION.equals(dealType)) {
							cert = bsCertInfoService.getCert(PayConstants.ALLINPAY_JSAPI_CERTS);
						} else if (FinanceConstants.PAYMENT_DEAL_TYPE_ZM_RECHARGE.equals(dealType)) {
							cert = bsCertInfoService.getCert(PayConstants.ALLINPAY_NATIVE_CERTS);
						} else if (FinanceConstants.PAYMENT_DEAL_TYPE_REPT.equals(dealType)) {
							cert = bsCertInfoService.getCert(PayConstants.ALLINPAY_NATIVE_CERTS);
						}
						return allinpayCertUtil.validSign(tmap, cert.getCertId());
					}
					return false;
				}
			};

		}
		invoker.action();
		log.info("paymentCallBack.invoke:{},payType:{},dealType:" + dealType, JsonUtil.object2String(postData),
				payType);
	}

	/**
	 * @desc 支付回调执行器
	 * @author Administrator
	 *
	 */
	private abstract class TransCallBackInvoker {

		private UserPaySuccessEvent event; // 支付方式

		public TransCallBackInvoker(UserPaySuccessEvent event) {
			this.event = event;
		}

		public abstract boolean validate();

		@SuppressWarnings("unchecked")
		public void action() {
			log.info("TransCallBackInvoker.action.event:{}", JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
			if (RedisService.getRedisService().incr(event.getSerialNo()) == 1) {
				if (validate()) {
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT,
							JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
					return;
				}
			 log.error("TransCallBackInvoker.validate.error:param:{}", JsonUtil.object2String(event));
			}
		}
	}
}
