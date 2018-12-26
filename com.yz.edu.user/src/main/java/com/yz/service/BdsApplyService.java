package com.yz.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.ReptConstants;
import com.yz.constants.TransferConstants;
import com.yz.dao.BdsApplyMapper;
import com.yz.dao.BdsStudentMapper;
import com.yz.exception.BusinessException;
import com.yz.model.AtsAccount;
import com.yz.model.AtsAccountSerial;
import com.yz.model.BdStudentRept;
import com.yz.model.WechatMsgVo;
import com.yz.model.apply.BdStudentWithdraw;
import com.yz.model.apply.BdsStudentCertificateInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.student.BdCheckRecord;
import com.yz.pay.GwPaymentService;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

import net.sf.json.JSONArray;

@Service
@Transactional
public class BdsApplyService {

	private static final Logger log = LoggerFactory.getLogger(BdsApplyService.class);

	@Autowired
	private BdsApplyMapper applyMapper;

	@Autowired
	private BdsStudentMapper studentMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private AccountService accountService;
	

	@Autowired
	private GwPaymentService paymentService;

	public String applyRept(Header header, Body body) {

		// 删除未支付收据申请
		applyMapper.deleteReptUnpaid(body.getString("learnId"));

		// 查询已缴流水但未申请收据或收据申请但未支付的流水
		List<String> serials = applyMapper.selectUnReptSerialInfo(body.getString("learnId"));

		if (null == serials || serials.size() <= 0) {
			throw new BusinessException("E60020"); // 暂无可申请收据
		}

		// 申请收据类型
		String reptType = body.getString("reptType");

		String reptStatus = null;
		String expressAmount = null;
		String content = null;
		
		// 收据状态
		switch (reptType) {
		case ReptConstants.REPT_TYPE_PICTURE: // 电子收据
			reptStatus = ReptConstants.REPT_SEND_STATUS_PICTURE;
			break;
		case ReptConstants.REPT_TYPE_EXPRESS: // 快递
			reptStatus = ReptConstants.REPT_SEND_STATUS_UNPAID;
			expressAmount = yzSysConfig.getReptAmount();
			break;
		case ReptConstants.REPT_TYPE_SELF_PICK: // 自取
			reptStatus = ReptConstants.REPT_SEND_STATUS_SELF_PICK;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String applyTime = DateUtil.getNowDateAndTime();
			String pickDay = null;
			try {
				Date date = sdf.parse(applyTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				if (day >= 1 && day <= 10) {
					pickDay = "当月15号";
				} else if (day >= 11 && day <= 20) {
					pickDay = "当月25号";
				} else if (day >= 21 && day <= 31) {
					pickDay = "次月5号";
				}
			} catch (ParseException e) {
				log.debug("---------------------------日期转换失败");
			}

			String campus = applyMapper.selectReptCampusAddress(body.getString("campusId"));

			content = "请于" + pickDay + "前往" + campus + "领取收据，或致电400确认后再前往。";
			//推送客服微信公众号信息
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(header.getOpenId());
			Map<String, String> contentMap = new HashMap<>();
			contentMap.put("content", content);
			msgVo.setContentData(contentMap);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
			break;
		default:
			return null;
		}

		// 组装参数
		BdStudentRept rept = new BdStudentRept();
		rept.setStdId(applyMapper.selectStdByLearnId(body.getString("learnId")));
		rept.setCampusId(body.getString("campusId"));
		rept.setReptType(body.getString("reptType"));
		rept.setLearnId(body.getString("learnId"));
		rept.setAddress(body.getString("address"));
		rept.setMobile(body.getString("mobile"));
		rept.setUserName(body.getString("userName"));
		rept.setProvince(body.getString("province"));
		rept.setProvinceCode(body.getString("provinceCode"));
		rept.setCity(body.getString("city"));
		rept.setCityCode(body.getString("cityCode"));
		rept.setDistrict(body.getString("district"));
		rept.setDistrictCode(body.getString("districtCode"));
		rept.setReptStatus(reptStatus);
		rept.setExpressAmount(expressAmount);
		log.debug("----------------------------------- 纸质收据申请：" + JsonUtil.object2String(serials));

		// 插入收据申请
		applyMapper.insertReptApply(rept);

		// 插入收据申请与流水批次关联
		applyMapper.insertReptSerialMark(rept.getReptId(), serials);

		return rept.getReptId();
	}

	public void withdrawApply(String stdId, String amount, String bankCard, String bankType, String bankOpen,
			String provinceCode, String cityCode) {
		Assert.hasText(stdId, "学员ID不能为空");

		BigDecimal am = AmountUtil.str2Amount(amount);

		if (am.compareTo(AmountUtil.str2Amount(FinanceConstants.STUDENT_WITHDRAW_MIN_COUNT)) < 0) {
			throw new BusinessException("E60017"); // 金额必须大于最小提现数
		}

		if (am.compareTo(AmountUtil.str2Amount(FinanceConstants.STUDENT_WITHDRAW_MAX_COUNT)) > 0) {
			throw new BusinessException("E60016"); // 金额不许大于最大提现数
		}

		//Map<String, String> balanceInfo = atsApi.getAccount(null, stdId, null, FinanceConstants.ACC_TYPE_NORMAL);
		//使用本地方法
		AtsAccount account = new AtsAccount();
		account.setStdId(stdId);
		account.setUserId(null);
		account.setEmpId(null);
		account.setAccType(FinanceConstants.ACC_TYPE_NORMAL);
		Map<String, String> balanceInfo = accountService.getAccount(account);
		
		

		String canDeposit = balanceInfo.get("canDeposit");
		if (GlobalConstants.FALSE.equals(canDeposit)) {
			throw new BusinessException("E60015"); // 账户不可提现
		}

		String balance = balanceInfo.get("accAmount");

		if (Double.valueOf(balance) < Double.valueOf(amount)) {
			throw new BusinessException("E60018"); // 余额不足
		}

		BdStudentWithdraw withdraw = new BdStudentWithdraw();
		withdraw.setAmount(am.toString());
		withdraw.setStdId(stdId);
		withdraw.setBankCard(bankCard);
		withdraw.setBankType(bankType);
		withdraw.setBankOpen(bankOpen);
		withdraw.setProvinceCode(provinceCode);
		withdraw.setCityCode(cityCode);
		applyMapper.insertWithdraw(withdraw);

		// 设置转账对象
//		Body body = new Body();
//		body.put("accType", FinanceConstants.ACC_TYPE_NORMAL);
//		body.put("stdId", stdId);
//		body.put("amount", am);
//		body.put("action", FinanceConstants.ACC_ACTION_OUT);
//		body.put("excDesc", "学员提现");
//		body.put("mappingId", withdraw.getWithdrawId());
//		atsApi.trans(body);
		
		
		//edit at 20180411 调用本地方法
		Assert.hasText(amount, "动账金额不能为空");
		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccId(null);
		serial.setAccType(FinanceConstants.ACC_TYPE_NORMAL);
		serial.setAction(FinanceConstants.ACC_ACTION_OUT);
		serial.setAmount(am.toString());
		serial.setUserId(null);
		serial.setEmpId(null);
		serial.setStdId(stdId);
		serial.setExcDesc("学员提现");
		serial.setMappingId(withdraw.getWithdrawId());
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);
		accountService.trans(serial);
	}

	/**
	 * 用户申请信息
	 * 
	 * @param learnId
	 * @return
	 */
	public Object selectApplyInfo(String learnId) {
		Object obj = JSONArray.fromObject(applyMapper.selectApplyInfoByLearnId(learnId));
		return obj;
	}

	
	public List<Map<String, String>> getCampusInfo() {
		List<Map<String, String>> campusInfo = applyMapper.selectCampusInfo();
		return campusInfo;
	}

	public Map<String, Object> reptExpressPayment(Header header, Body body) {

		String reptId = body.getString("reptId");

		String amount = applyMapper.selectExpressAmountByReptId(reptId);

		if (FinanceConstants.PAYMENT_TYPE_WECHAT.equals(body.getString("paymentType"))) {
			return reptPaymentWechat(reptId, amount, header.getOpenId());
		} else if (FinanceConstants.PAYMENT_TYPE_ZM.equals(body.getString("paymentType"))) {
			reptPaymentZM(reptId, amount, header.getUserId());
			return null;
		} else {
			return null;
		}

	}

	private Map<String, Object> reptPaymentWechat(String reptId, String amount, String openId) {
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("orderAmount", amount);
		postData.put("serialNo", reptId);
		postData.put("payType", FinanceConstants.PAYMENT_TYPE_WECHAT);
		postData.put("dealType", FinanceConstants.PAYMENT_DEAL_TYPE_REPT);
		postData.put("openId", openId);

		postData.put("tradeType", FinanceConstants.TRADE_TYPE_JSAPI);
		postData.put("productId", reptId);

		log.debug("---------------------------------- 丢雷楼某， reptId： " + reptId);

		// 微信支付
		Map<String, Object> result = paymentService.payment(postData);

		log.debug("--------------------------------- 微信下单参数：" + JsonUtil.object2String(result));
		return result;
	}

	/**
	 * 智米支付快递费
	 */
	private void reptPaymentZM(String reptId, String amount, String userId) {
		// 智米抵扣比例
		BigDecimal zmScale = AmountUtil.str2Amount(yzSysConfig.getZhiMiScale());

		// 智米抵扣金额
		BigDecimal zmDeduction = BigDecimalUtil.multiply(AmountUtil.str2Amount(amount), zmScale);

		// 设置转账对象
//		Body body = new Body();
//		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
//		body.put("userId", userId);
//		body.put("amount", zmDeduction);
//		body.put("action", FinanceConstants.ACC_ACTION_OUT);
//		body.put("excDesc", "智米抵扣收据快递费用");
//		body.put("mappingId", reptId);
//		
//		// 扣除智米
//		atsApi.trans(body);
		
		//调用本地方法
		Assert.hasText(zmDeduction.toString(), "动账金额不能为空");
		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccId(null);
		serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		serial.setAction(FinanceConstants.ACC_ACTION_OUT);
		serial.setAmount(zmDeduction.toString());
		serial.setUserId(userId);
		serial.setEmpId(null);
		serial.setStdId(null);
		serial.setExcDesc("智米抵扣收据快递费用");
		serial.setMappingId(reptId);
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);
		accountService.trans(serial);

		

		// 支付成功
		reptPaySuccess(reptId, FinanceConstants.PAYMENT_TYPE_ZM, null);

	}

	/**
	 * 修改收据状态缴费成功
	 * 
	 * @param reptId
	 * @param paymentType
	 * @param outSerialNo
	 */
	public void reptPaySuccess(String reptId, String paymentType, String outSerialNo) {

		applyMapper.updateReptPaySuccess(reptId, paymentType, outSerialNo);
	}

	/**
	 * 收据快递费回调处理
	 * 
	 * @param reptId
	 * @param outSerialNo
	 * @param wechatAmount
	 * @return
	 */
	public boolean wechatCallBack(String reptId, String outSerialNo, String wechatAmount) {

		log.debug("--------------------------快递费支付成功流程开始，收据id为：" + reptId);

		int exist = applyMapper.selectPaidRept(reptId);

		if (1 < exist) {
			log.error("--------------------------在线支付重复回调，收据id：" + reptId);
			return false;
		}

		// 转换为 单位；元
		wechatAmount = BigDecimalUtil.divide(wechatAmount, "100");

		String amount = applyMapper.selectExpressAmountByReptId(reptId);

		// 缴费金额不一致
		if (AmountUtil.str2Amount(wechatAmount).compareTo(AmountUtil.str2Amount(amount)) != 0) {
			log.error("--------------------------在线支付回调金额与付款金额不匹配，收据id：" + reptId);
			return false;
		}

		// 支付成功
		reptPaySuccess(reptId, FinanceConstants.PAYMENT_TYPE_WECHAT, outSerialNo);
		return true;
	}

	public void studentCertificateApply(BdsStudentCertificateInfo studentCertificateInfo){
		applyMapper.insertStudentCertificate(studentCertificateInfo);
		List<Map<String, String>> checkWeights = null;
		checkWeights = studentMapper.getCheckWeight(TransferConstants.CHECK_TYPE_CERTIFICATE);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentCertificateInfo.getCertId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			studentMapper.addBdCheckRecord(checkRecord);
		}
	}

	public Object getCertificateApply(String learnId){
		return applyMapper.getCertificateApply(learnId);
	}
}
