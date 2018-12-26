package com.yz.api;

import com.yz.model.apply.BdsStudentCertificateInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsApplyService;
import com.yz.service.BdsStudentSendService;
import com.yz.util.JsonUtil;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsApplyApiImpl implements BdsApplyApi {

	private static final Logger log = LoggerFactory.getLogger(BdsApplyApi.class);

	@Autowired
	private BdsApplyService applyService;

	@Autowired
	private BdsStudentSendService sendService;

	@Override
	public Object stduentApplyInfo(Header header, Body body) throws BusinessException {
		return applyService.selectApplyInfo(body.getString("learnId"));
	}

	@Override
	public void studentWithdrawApply(Header header, Body body) throws IRpcException {
		applyService.withdrawApply(header.getStdId(), body.getString("amount"), body.getString("bankCard"),
				body.getString("bankType"), body.getString("bankOpen"), body.getString("provinceCode"),
				body.getString("cityCode"));
	}

	@Override
	public String studentReptApply(Header header, Body body) throws IRpcException {
		return applyService.applyRept(header,body);
	}

	@Override
	public Object getCampusInfo(Header header, Body body) throws IRpcException {
		return applyService.getCampusInfo();
	}

	@Override
	public Object reptExpressPay(Header header, Body body) throws IRpcException {
		Object o = applyService.reptExpressPayment(header, body);
		log.debug("---------------------- 收据支付下单:" + JsonUtil.object2String(o));
		return o;
	}

	@Override
	public boolean reptExpressPaymentCallBack(Header header, Body body)
			throws IRpcException {
		String reptId=body.getString("serialNo");
		String outSerialNo=body.getString("outOrderNo"); 
		String wechatAmount=body.getString("amount");
		return applyService.wechatCallBack(reptId, outSerialNo, wechatAmount);
	}

	@Override
	public void studentCertificateApply(Header header, Body body) throws IRpcException {
		BdsStudentCertificateInfo studentCertificateInfo = new BdsStudentCertificateInfo();

		studentCertificateInfo.setLearnId(body.getString("learnId"));
		studentCertificateInfo.setApplyType(body.getString("applyType"));
		studentCertificateInfo.setApplyPurpose(body.getString("applyPurpose"));
		studentCertificateInfo.setReceiverName(body.getString("receiverName"));
		studentCertificateInfo.setReceiverMobile(body.getString("receiverMobile"));
		studentCertificateInfo.setReceiverAddress(body.getString("receiverAddress"));
		studentCertificateInfo.setReceiveType(body.getString("receiveType"));
		studentCertificateInfo.setStampDown(body.getString("stampDown"));
		studentCertificateInfo.setMaterialName(body.getString("materialName"));
		studentCertificateInfo.setRemark(body.getString("remark"));
		applyService.studentCertificateApply(studentCertificateInfo);
	}

	@Override
	public Object getCertificateApply(Header header, Body body) throws IRpcException {
		return applyService.getCertificateApply(body.getString("learnId"));
	}
}
