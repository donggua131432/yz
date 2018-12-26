package com.yz.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.dao.AtsRechargeMapper;
import com.yz.model.AtsAccountSerial;
import com.yz.model.AtsRechargeRecords;
import com.yz.model.AtsZhimiProduct;
import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.pay.GwPaymentService;

@Service
@Transactional
public class AtsRechargeService {
	
	private static final Logger log = LoggerFactory.getLogger(AtsRechargeService.class);

	@Autowired
	private AtsRechargeMapper rechargeMapper;

	@Autowired
	private GwPaymentService paymentApi;
 

	public List<Map<String, String>> getProductList() {
		int size = 6;// 取前6个充值产品
		return rechargeMapper.getProductList(size);
	}

	public Object recharge(String productId, String userId, String openId) {
		AtsZhimiProduct productInfo = rechargeMapper.getProductById(productId);

		String price = productInfo.getPrice();
		String zhimi = productInfo.getZhimi();

		AtsRechargeRecords records = new AtsRechargeRecords();

		records.setAmount(price);
		records.setZhimi(zhimi);
		records.setUserId(userId);
		records.setCreateTime(new Date());
		records.setPaymentType(FinanceConstants.PAYMENT_TYPE_WECHAT);
		records.setPaymentStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);
		records.setRemark("用户[" + userId + "]充值" + zhimi + "智米");
		records.setWechatOpenId(openId);
		records.setProductId(productId);

		rechargeMapper.insertRecords(records);

		String serialNo = records.getRecordsNo();

		Map<String, String> postData = new HashMap<String, String>();
		postData.put("dealType", FinanceConstants.PAYMENT_DEAL_TYPE_ZM_RECHARGE);
		postData.put("orderAmount", price);
		postData.put("serialNo", serialNo);
		postData.put("payType", FinanceConstants.PAYMENT_TYPE_WECHAT);
		postData.put("openId", openId);
		postData.put("tradeType", "JSAPI");

		Map<String, Object> result = paymentApi.payment(postData);
		
		return result;
	}

	@Autowired
	private AccountService accountService;;

	public boolean rechargeCallBack(String recordsNo, String isSuccess, String outSerialNo) {
		AtsRechargeRecords records = rechargeMapper.getRecordsInfo(recordsNo);

		if (records == null) {
			records = new AtsRechargeRecords();
			records.setRecordsNo(recordsNo);
			records.setPaymentStatus(FinanceConstants.ACC_SERAIL_STATUS_FAILED);
			records.setRemark("异常流水记录");
			records.setCreateTime(new Date());
			rechargeMapper.insertRecordsNoG(records);
			return true;
		}
		
		if(!FinanceConstants.ACC_SERAIL_STATUS_PROCESSING.equals(records.getPaymentStatus())) {
			log.error(" -------------------------- 充值记录[" + recordsNo + "]已处理完成，请勿重复回调");
			return true;
		}

		if (GlobalConstants.FALSE.equals(isSuccess)) {
			records.setPaymentStatus(FinanceConstants.ACC_SERAIL_STATUS_FAILED);
			records.setOutSerialNo(outSerialNo);
			records.setCompleteTime(new Date());
			rechargeMapper.updateRecords(records);
		} else {

			String zhimi = records.getZhimi();

			AtsAccountSerial serial = new AtsAccountSerial();
			serial.setAccId(records.getAccId());
			serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
			serial.setAction(FinanceConstants.ACC_ACTION_IN);
			serial.setAmount(zhimi);
			serial.setUserId(records.getUserId());
			serial.setEmpId(records.getEmpId());
			serial.setStdId(records.getStdId());
			serial.setExcDesc("用户充值" + zhimi + "智米");
			serial.setMappingId(records.getRecordsNo());
			serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);

			serial = accountService.trans(serial);//金额操作

			records.setAccId(serial.getAccId());
			records.setOutSerialNo(outSerialNo);
			records.setStdId(serial.getStdId());
			records.setEmpId(serial.getEmpId());
			records.setPaymentStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
			records.setCompleteTime(new Date());
			
			rechargeMapper.updateRecords(records);
			
			//推送微信公众号信息
			sendRechargeMsg(records.getWechatOpenId(), zhimi, records.getAmount());
		}
		return true;
	}
	/**
	 * 智米充值成功-推送微信公众号信息
	 * @param openId
	 * @param rechargeAmount
	 * @param rmbAmount
	 */
	public void sendRechargeMsg(String openId, String rechargeAmount, String rmbAmount){
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_RECHARGE);
		msgVo.setTouser(openId);
		msgVo.addData("rechargeAmount", rechargeAmount);
		msgVo.addData("rmbAmount", rmbAmount);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	}
}
