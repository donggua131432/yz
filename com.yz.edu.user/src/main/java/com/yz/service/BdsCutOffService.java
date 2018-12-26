package com.yz.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.dao.BdCutOffMapper;
import com.yz.dao.BdPayeeMapper;
import com.yz.model.BdCutOff;
import com.yz.model.BdPayee;
import com.yz.pay.GwPaymentService;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdsCutOffService {

	private static final Logger log = LoggerFactory.getLogger(BdsCutOffService.class);

	@Autowired
	private BdPayeeMapper payeeMapper;

	@Autowired
	private BdCutOffMapper ctMapper;

	@Autowired
	private GwPaymentService paymentApi;

	public void ctEveryday() {
		boolean fuckOff = true;
		if (!fuckOff) {
			List<BdPayee> list = payeeMapper.selectAll();

			if (list != null && list.size() > 0) {
				for (BdPayee payee : list) {
					String payeeId = payee.getPayeeId();
					String cardNo = payee.getCardNo();
					String cardType = payee.getCardType();
					String belong = payee.getBelong();

					List<Map<String, String>> serialList = ctMapper.selectCtSerials(payeeId);

					if (serialList == null || serialList.isEmpty()) {
						log.debug("---------------------- 账号：" + cardNo + " | 账号所属人：" + belong
								+ " ::::::::::::: 没有待结算流水");
						continue;
					}

					log.debug("---------------------- 账号：" + cardNo + " | 账号所属人：" + belong + " ::::::::::::: 开始结算");

					BigDecimal total = BigDecimal.ZERO;

					for (Map<String, String> serial : serialList) {
						String payable = serial.get("payable");

						if (StringUtil.isEmpty(payable))
							continue;

						total = total.add(new BigDecimal(payable));
					}

					Date _now = new Date();

					BdCutOff ctInfo = new BdCutOff();

					ctInfo.setAmount(total.toString());
					ctInfo.setBelong(belong);
					ctInfo.setCardNo(cardNo);
					ctInfo.setCtTime(_now);
					ctInfo.setPayeeId(payeeId);

					Map<String, String> postData = new HashMap<String, String>();
					postData.put("cardNo", cardNo);
					postData.put("cardType", cardType);
					postData.put("belong", belong);
					postData.put("amount", total.toString());
					postData.put("timeStr", DateUtil.formatDate(_now, DateUtil.YYYYMMDDHHMMSS));

					log.debug("---------------------- 账号：" + cardNo + " | 账号所属人：" + belong + " ::::::::::::: 结算信息："
							+ JsonUtil.object2String(postData));

					try {
						Map<String, String> result = paymentApi.cutOff(postData);

						if (result != null && result.size() > 0) {
							String isOk = result.get("isOk");
							String ctNo = result.get("ctNo");

							if (GlobalConstants.TRUE.equals(isOk)) {
								ctInfo.setCtNo(ctNo);
								ctInfo.setIsSuccess(GlobalConstants.TRUE);
							} else {
								String eMsg = result.get("eMsg");
								ctInfo.setCtNo(StringUtil.UUID());
								ctInfo.setReason(eMsg);
								ctInfo.setIsSuccess(GlobalConstants.FALSE);
							}
						} else {
							ctInfo.setCtNo(StringUtil.UUID());
							ctInfo.setReason("返回结果为空");
							ctInfo.setIsSuccess(GlobalConstants.FALSE);
						}

					} catch (Exception e) {
						log.error(e.getMessage(), e);
						ctInfo.setCtNo(StringUtil.UUID());
						ctInfo.setReason(e.getMessage());
						ctInfo.setIsSuccess(GlobalConstants.FALSE);
					} finally {
						ctMapper.insertSelective(ctInfo);

						if (GlobalConstants.TRUE.equals(ctInfo.getIsSuccess())) {
							ctMapper.updateSerialCtStatus(serialList, FinanceConstants.CT_STATUS_FINISH);
						} else {
							ctMapper.updateSerialCtCount(serialList);
						}
					}
				}
			} else {
				log.error("------------------------- 没有收款账户信息");
			}
		}
	}

}
