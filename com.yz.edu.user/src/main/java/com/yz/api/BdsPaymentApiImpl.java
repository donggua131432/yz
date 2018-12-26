package com.yz.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.coupon.BdCoupon;
import com.yz.model.payment.BdPayInfo;
import com.yz.service.BdsPaymentCallBackService;
import com.yz.service.BdsPaymentService;
import com.yz.service.BdsTuitionService;
import com.yz.util.JsonUtil;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsPaymentApiImpl implements BdsPaymentApi {

	@Autowired
	private BdsPaymentService paymentService;

	@Autowired
	private BdsPaymentCallBackService callBackService;

	@Autowired
	private BdsTuitionService tuitionService;

	@Override
	public Object studentSerials(Header header, Body body) throws BusinessException {

		return paymentService.selectStudentSerial(body.getString("learnId"), body.getInt("pageNum"),
				body.getInt("pageSize"));
	}

	@Override
	public Object myCoupons(Header header, Body body) throws BusinessException {
		return paymentService.selectCoupon(header.getUserId());
	}

	@Override
	public Object payableInfo(Header header, Body body) throws IRpcException {
		return paymentService.selectPayableInfoByLearnId(body.getString("learnId"), header.getUserId());
	}
	
	@Override
	public Object stuEnrollpayableInfo(Header header, Body body) throws IRpcException {
		return paymentService.selectEnrollPayableInfoByLearnId(body.getString("learnId"), header.getUserId());
	}

	@Override
	public Object availableCoupon(Header header, Body body) throws IRpcException {
		return paymentService.selectAbleCoupon(body.getString("learnId"));
	}

	@Override
	public boolean paySuccess(Header header, Body body) throws IRpcException {
		String serialNo= body.getString("serialNo");
		String outSerialNo= body.getString("outOrderNo"); 
		String payType= body.getString("payType");
		String amount= body.getString("amount");
		return callBackService.paySuccess(serialNo, outSerialNo, payType, amount);
	}

	@Override
	public boolean payFailed(Header header, Body body) throws IRpcException {
		String serialNo= body.getString("serialNo");
		String outSerialNo= body.getString("outOrderNo");
		return callBackService.payFailed(serialNo, outSerialNo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object stdPayTuition(Header header, Body body) throws IRpcException {
		BdPayInfo payInfo = new BdPayInfo();
		payInfo.setLearnId(body.getString("learnId"));
		List<BdCoupon> coupons = JsonUtil.str2List(body.getString("coupons"),BdCoupon.class);
		payInfo.setCoupons(coupons);
		payInfo.setItemCodes((ArrayList<String>) body.get("itemCodes"));
		payInfo.setPaymentType(body.getString("paymentType"));
		payInfo.setYears((ArrayList<String>) body.get("years"));
		payInfo.setZmDeduction(body.getString("zmDeduction"));
		payInfo.setAccDeduction(body.getString("accDeduction"));
		payInfo.setTradeType(body.getString("tradeType"));
		payInfo.setOpenId(header.getOpenId());
		payInfo.setUserId(header.getUserId());
		return tuitionService.payTuition(payInfo, header.getUserId());
	}

	@Override
	public Object withdrawSerial(Header header, Body body) throws IRpcException {
		return paymentService.selectWithdrawSerial(header.getStdId());
	}

	@Override
	public Object jsapiSign(Header header, Body body) throws IRpcException {
		return paymentService.jsapiSign(body.getString("url"));
	}

	@Override
	public Object stdPayTuitionByQRCode(Body body) throws IRpcException {
		BdPayInfo payInfo = new BdPayInfo();
		payInfo.setLearnId(body.getString("learnId"));
		String couponStr = body.getString("coupons");
		List<BdCoupon> coupons =  JsonUtil.str2List(couponStr, BdCoupon.class);
		payInfo.setCoupons(coupons);
		String[] itemsCodes = (String[]) body.get("itemCodes");
		ArrayList<String> itemCodesList = new ArrayList<>();
		for (int i = 0; i < itemsCodes.length; i++) {
			itemCodesList.add(itemsCodes[i]);
		}
		payInfo.setItemCodes(itemCodesList);
		payInfo.setPaymentType(body.getString("paymentType"));

		String[] years = (String[]) body.get("years");
		ArrayList<String> yearsList = new ArrayList<>();
		for (int i = 0; i < years.length; i++) {
			yearsList.add(years[i]);
		}
		payInfo.setYears(yearsList);
		payInfo.setZmDeduction(body.getString("zmDeduction"));
		payInfo.setAccDeduction(body.getString("accDeduction"));
		payInfo.setTradeType(body.getString("tradeType"));
		payInfo.setEmpId(body.getString("empId"));
		return tuitionService.payTuition(payInfo, null);
	}

}
