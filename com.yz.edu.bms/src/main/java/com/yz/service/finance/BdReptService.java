package com.yz.service.finance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.UsInfoApi;
import com.yz.constants.ReptConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.SfCode128CUtil;
import com.yz.core.util.WechatSendServiceMsgUtil;
import com.yz.dao.finance.BdReptMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.finance.receipt.BdPayItem;
import com.yz.model.finance.receipt.BdReceipt;
import com.yz.model.finance.receipt.BdReceiptPrint;
import com.yz.model.finance.receipt.BdReceiptQuery;
import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.sf.SFExpressRequest;
import com.yz.service.sfexpress.SfExpressService;
import com.yz.util.AmountToCNUtil;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdReptService {

	private static Logger log = LoggerFactory.getLogger(BdReptService.class);

	// 单张收据打印科目数量
	private final static int itemCount = 3;

	@Autowired
	private BdReptMapper reptMapper;

	@Autowired
	private SfExpressService sfService;

	@Autowired
	private SfCode128CUtil sfCode128CUtil;

	public void sfOrder(String reptId) throws IOException {

		SFExpressRequest order = reptMapper.selectSfInfoByLearnId(reptId);
		order.setjCompany("惠州远智教育");
		order.setjContact("财务部");
		order.setjTel("07522686013");
		order.setjProvince("广东省");
		order.setjCity("惠州");
		order.setjCountry("惠城区");
		order.setjAddress("江北三新南路22号润宇豪庭3楼");
		// 顺丰下单接口
		Map<String, String> resp = sfService.sfOrder(order, ReptConstants.SF_ORDER_SENDER_FINANCE);
		String headResp = resp.get(ReptConstants.SF_RESPONSE_HEAD);
		if (!StringUtil.hasValue(headResp)) {
			reptMapper.updateReptRemark(reptId, ReptConstants.SF_RESPONSE_ERRMSG_NULL);
			return;
		} else if (ReptConstants.SF_RESPONSE_ERR.equalsIgnoreCase(headResp)) {
			log.error("-------------顺丰下单错误：" + resp.toString());
			String errMsg = "下单失败：(" + resp.get("errorCode") + ")" + resp.get("errorMsg");
			reptMapper.updateReptRemark(reptId, errMsg);
			return;
		}
		String destCode = resp.get(ReptConstants.SF_EXPRESS_DESTCODE); // 寄件地区编码
		String mailno = resp.get(ReptConstants.SF_EXPRESS_MAILNO); // 顺丰订单号
		String custid = resp.get("custid");

		BdReceipt rept = new BdReceipt();
		rept.setDestCode(destCode);
		rept.setSfMailno(mailno);
		rept.setReptStatus(ReptConstants.REPT_SEND_STATUS_UNSEND);
		rept.setReptId(reptId);
		rept.setRemark(ReptConstants.SF_RESPONSE_SUCCESS_MSG);
		rept.setCustid(custid);
		reptMapper.updateRept(rept);
		if (StringUtil.hasValue(mailno)) { // 生成条形码并上传至OSS文件服务器 文件地址：sf/$mailno
			sfCode128CUtil.uploadBarcodeImg(mailno);
		}

	}

	/**
	 * 收据打印信息
	 * 
	 * @param reptId
	 * @return
	 */
	/*
	 * public Object reptPrintInfo(String reptId) { List<BdReceiptPrint> repts =
	 * new ArrayList<BdReceiptPrint>(); BdReceiptPrint rept =
	 * reptMapper.selectReceiptPrint(reptId); if
	 * ("201803".equals(rept.getGrade())) { for (BdPayItem item :
	 * rept.getItems()) { String itemName = item.getItemName().replace("年",
	 * "学期"); item.setItemName(itemName); } }
	 * 
	 * BigDecimal a = new BigDecimal(rept.getAmount()); String cnAmount =
	 * AmountToCNUtil.number2CNMontrayUnit(a); rept.setCnAmount(cnAmount);
	 * 
	 * repts.add(rept);
	 * 
	 * return repts; }
	 */

	/**
	 * 顺丰电子单打印信息
	 * 
	 * @param reptId
	 * @return
	 */
	public Object sfPrintInfo(String reptId) {
		List<BdSfPrint> sfs = new ArrayList<BdSfPrint>();
		BdSfPrint sf = reptMapper.selectSfPrint(reptId);
		String mailno = StringUtil.addSpace(sf.getMailno(), 3);
		sf.setMailnoSpace(mailno);
		sfs.add(sf);
		return sfs;
	}

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectReptByPage(int start, int length, BdReceiptQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdReceipt> repts = reptMapper.selectReptByPage(query);
		for (BdReceipt rept : repts) {
			if ("201803".equals(rept.getGrade())) {
				for (BdPayItem item : rept.getItems()) {
					String itemName = item.getItemName().replace("年", "学期");
					item.setItemName(itemName);
				}
			}
		}
		return new IPageInfo<>((Page) repts);
	}

	public void sfOrders(String[] reptIds) throws IOException {
		for (String reptId : reptIds) {
			sfOrder(reptId);
		}

	}

	public Object reptPrintInfos(String[] reptIds) {
		List<BdReceiptPrint> repts = reptMapper.selectReceiptPrints(reptIds);

		List<BdReceiptPrint> result = new ArrayList<BdReceiptPrint>();
		for (BdReceiptPrint rept : repts) {

			List<BdPayItem> items = reptMapper.selectItemsByReptId(rept.getReptId());
			int pageCount = (items.size() + itemCount - 1) / itemCount;

			for (int i = 0; i < pageCount; i++) {

				int start = (i * itemCount) < 0 ? 0 : (i * itemCount);

				int end = i * itemCount + itemCount;

				List<BdPayItem> item = new ArrayList<BdPayItem>();
				if (end >= items.size()) {
					item = items.subList(start, items.size());
				} else {

					item = items.subList(start, end);
				}

				String amount = "0.00";
				for (BdPayItem it : item) {
					amount = BigDecimalUtil.add(it.getAmount(), amount);
					if ("201809".equals(rept.getGrade()) || "201803".equals(rept.getGrade())
							|| "201703".equals(rept.getGrade())) {
						String itemName = it.getItemName().replace("年", "学期");
						it.setItemName(itemName);
					}
				}

				BdReceiptPrint reptTemp = new BdReceiptPrint();
				reptTemp.setDeduction(rept.getDeduction());
				reptTemp.setGrade(rept.getGrade());
				reptTemp.setPayee(rept.getPayee());
				reptTemp.setPaymentType(rept.getPaymentType());
				reptTemp.setPaySite(rept.getPaySite());
				reptTemp.setPayTime(rept.getPayTime());
				reptTemp.setPfsnLevel(rept.getPfsnLevel());
				reptTemp.setPfsnName(rept.getPfsnName());
				reptTemp.setReptId(rept.getReptId());
				reptTemp.setSerialMark(rept.getSerialMark());
				reptTemp.setSerialNo(rept.getSerialNo());
				reptTemp.setStdName(rept.getStdName());
				reptTemp.setUnvsName(rept.getUnvsName());
				reptTemp.setZmDeduction(rept.getZmDeduction());
				BigDecimal a = new BigDecimal(amount);
				String cnAmount = AmountToCNUtil.number2CNMontrayUnit(a);
				reptTemp.setCnAmount(cnAmount);
				reptTemp.setAmount(amount);

				reptTemp.setItems(item);
				result.add(reptTemp);
				log.debug("---------------------------------- 收据打印信息:" + i + "===" + JsonUtil.object2String(result));
			}

		}
		log.debug("---------------------------------- 收据打印信息:" + JsonUtil.object2String(result));
		return result;
	}

	public static void main(String[] args) {
		List<String> s = new ArrayList<String>();
		s.add("1");
		s.add("2");
		s.add("3");
		s.add("4");
		/*
		 * s.add("5"); s.add("6"); s.add("7"); s.add("8"); s.add("9");
		 * s.add("10"); s.add("11"); s.add("12");
		 */

		int pageCount = (s.size() + itemCount - 1) / itemCount;

		for (int i = 0; i < pageCount; i++) {

			int start = (i * itemCount) < 0 ? 0 : (i * itemCount);

			int end = i * itemCount + itemCount;
			List<String> item = new ArrayList<String>();

			if (end >= s.size()) {
				item = s.subList(start, s.size());
			} else {

				item = s.subList(start, end);
			}

			System.err.println(item.toString());
		}

	}

	public Object sfPrintInfos(String[] reptIds) {
		List<BdSfPrint> sfs = reptMapper.selectSfPrints(reptIds);
		for (BdSfPrint sf : sfs) {
			String mailno = StringUtil.addSpace(sf.getMailno(), 3);
			sf.setMailnoSpace(mailno);
		}
		return sfs;
	}

	public void reptMailed(String[] reptIds) {
		reptMapper.updateReptMailed(reptIds);
	}

	public String insertStudentRept(Map<String, String> result) {
		int count = reptMapper.selectReptCountBySerialMark(result.get("serialMark"));
		if (count > 0) {
			return reptMapper.selectReptIdBySerialMark(result.get("serialMark"));
		}
		BdReceipt rept = new BdReceipt();
		BaseUser user = SessionUtil.getUser();

		rept.setCreateUser(user.getRealName());
		rept.setCreateUserId(user.getUserId());
		rept.setUpdateUser(user.getRealName());
		rept.setUpdateUserId(user.getUserId());
		rept.setReptStatus(ReptConstants.REPT_SEND_STATUS_SITE);
		rept.setLearnId(result.get("learnId"));
		rept.setStdId(result.get("stdId"));
		rept.setStdName(result.get("stdName"));
		rept.setReptId(IDGenerator.generatorId());
		reptMapper.insertSelective(rept);
		reptMapper.insertReptSerial(IDGenerator.generatorId(), rept.getReptId(), result.get("serialMark"));

		return rept.getReptId();
	}

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	@SuppressWarnings("unchecked")
	public void sendReptInform(String reptId) {

		String hasInform = reptMapper.selectHasInformByReptId(reptId);

		if ("1".equals(hasInform)) {
			throw new BusinessException("E000115"); // 不能重复发送
		}

		String reptType = reptMapper.selectReptType(reptId);
		String content = "";
		String userId = reptMapper.selectUserIdByReptId(reptId);
		Body body = new Body();
		body.put("userId", userId);
		String openId = (String) usApi.getOpenIdByUserId(body);

		if (!StringUtil.hasValue(openId)) {
			throw new BusinessException("E000114"); // 学员openId不存在
		}

		if (ReptConstants.REPT_TYPE_EXPRESS.equals(reptType)) {
			content = "收据已寄出，请注意查收。顺丰单号为：";
			String mailno = reptMapper.selectMailNoByReptId(reptId);
			if (!StringUtil.hasValue(mailno)) {
				throw new BusinessException("E000116"); // 无快递单号
			}
			content += mailno;
		} /*
			 * else if (ReptConstants.REPT_TYPE_SELF_PICK.equals(reptType)) {
			 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 * 
			 * String applyTime = reptMapper.selectApplyTimeByReptId(reptId);
			 * String pickDay = null; try { Date date = sdf.parse(applyTime);
			 * Calendar cal = Calendar.getInstance(); cal.setTime(date); int day
			 * = cal.get(Calendar.DAY_OF_MONTH); if (day >= 1 && day <= 10) {
			 * pickDay = "当月15号"; } else if (day >= 11 && day <= 20) { pickDay =
			 * "当月25号"; } else if (day >= 21 && day <= 31) { pickDay = "次月5号"; }
			 * } catch (ParseException e) {
			 * log.debug("---------------------------日期转换失败"); }
			 * 
			 * String campus = reptMapper.selectReptCampusAddress(reptId);
			 * 
			 * content = "请于" + pickDay + "前往" + campus + "领取收据，或致电400确认后再前往。";
			 * }
			 */ else {
			return;
		}

		// 推送客服微信公众号信息
		WechatSendServiceMsgUtil.wechatSendServiceMsg(openId, content);

		reptMapper.updateSendInform(reptId);
	}

	public Object applySiteRept(String serialMark) {
		Map<String, String> result = reptMapper.selectStdInfoBySerialMark(serialMark);
		result.put("serialMark", serialMark);
		return insertStudentRept(result);
	}

	public String countAmount(BdReceiptQuery query) {
		String amount = reptMapper.selectReptAmount(query);
		BigDecimal count = BigDecimal.ZERO;
		if (StringUtil.hasValue(amount)) {
			count = AmountUtil.str2Amount(amount);
		}

		return count.toString();
	}


	public void reptProcessed(String[] reptIds) {
		for (String reptId : reptIds) {

			BdReceipt rept = reptMapper.selectReptById(reptId);
			if (ReptConstants.REPT_TYPE_SELF_PICK.equals(rept.getReptType())
					&& ReptConstants.REPT_SEND_STATUS_SELF_PICK.equals(rept.getReptStatus())) {
				reptMapper.updateReptStatus(reptId, ReptConstants.REPT_SEND_STATUS_SELF_PICKED);
			}

		}
	}
}
