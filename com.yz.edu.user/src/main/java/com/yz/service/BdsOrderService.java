package com.yz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.FinanceConstants;
import com.yz.dao.BdOrderMapper;
import com.yz.dao.BdPfsnPayeeMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.communi.Body;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
public class BdsOrderService {

	private static final Logger log = LoggerFactory.getLogger(BdsOrderService.class);

	@Autowired
	private BdOrderMapper orderMapper;
	
	@Autowired
	private BdPfsnPayeeMapper payeeMapper;

	@SuppressWarnings("unchecked")
	public void initStudentOrder(Body body) {
		String recruitEmpId = body.getString("recruitId");
		String financeCode = null;
		if(StringUtil.hasValue(recruitEmpId)) {
			financeCode = orderMapper.selectFinanceCodeByEmp(recruitEmpId);
			if(StringUtil.isEmpty(financeCode))
				financeCode = FinanceConstants.DEFAULT_FINANCE_CODE;
		} else {
			financeCode = FinanceConstants.DEFAULT_FINANCE_CODE;
		}

		String payeeId = payeeMapper.selectPayeeId(body.getString("pfsnId"));

		body.put("peyeeId", payeeId);
		body.put("financeCode", financeCode);
		body.put("stdName", body.getString("name"));
		
		String orderNo = orderMapper.selectOrderNoByLearnId(body.getString("learnId"));
		if (!StringUtil.hasValue(orderNo)) {
			body.put("orderNo", IDGenerator.generatororderNo(financeCode));
			orderMapper.createOrder(body);
			log.debug("-------------------------- initStudentOrder(1)：订单创建成功");
		} else {
			body.put("orderNo", orderNo);
		}

		// TODO 根据学员阶段 查询需要生成的收费科目订单
		List<Map<String, String>> itemInfos = orderMapper.getRequireItemCodes(body);

		if (itemInfos == null || itemInfos.size() < 1) {
			log.error("-------------------------- initStudentOrder(2)：尚未设置收费科目或者未设置科目与阶段的对应关系，无法创建订单");
			throw new BusinessException("E60022");// 请先配置收费科目
		}

		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal discount = BigDecimal.ZERO;
		BigDecimal payable = BigDecimal.ZERO;

		List<Map<String, String>> subOrderList = new ArrayList<Map<String, String>>();

		for (Map<String, String> feeInfo : itemInfos) {

			String itemCode = feeInfo.get("itemCode");
			String fdId = feeInfo.get("fdId");
			String odId = feeInfo.get("odId");
			String _orderNo = body.getString("orderNo");
			
			
			String stdId = body.getString("stdId");
			String feeId = body.getString("feeId");
			String offerId = body.getString("offerId");

			Map<String, String> amountInfo = orderMapper.getFeeInfo(feeId, offerId, itemCode);
			if (amountInfo == null)
				continue;
			
			String _amount = amountInfo.get("amount");
			String _discount = amountInfo.get("discount");
			
			if(BigDecimal.ZERO.compareTo(AmountUtil.str2Amount(_amount)) == 0) 
					continue;

			if (!StringUtil.hasValue(_discount)) {
				_discount = "0.00";
			} 

			Map<String, String> subOrder = new HashMap<String, String>();

			subOrder.put("fdId", fdId);
			subOrder.put("odId", odId);
			subOrder.put("orderNo", _orderNo);
			subOrder.put("feeAmount", _amount);
			subOrder.put("offerAmount", _discount);
			subOrder.put("stdId", stdId);
			subOrder.put("payeeId", payeeId);

			BigDecimal subPayable = AmountUtil.str2Amount(_amount).subtract(AmountUtil.str2Amount(_discount));
			if (subPayable.compareTo(BigDecimal.ZERO) < 0) {
				subPayable = BigDecimal.ZERO;
			}

			subOrder.put("payable", subPayable.setScale(2, BigDecimal.ROUND_DOWN).toString());
			subOrder.put("subOrderStatus", FinanceConstants.ORDER_STATUS_UNPAID);

			subOrder.put("itemCode", itemCode);
			subOrder.put("stdName", body.getString("name"));
			subOrder.put("fdId", feeId);
			subOrder.put("odId", offerId);
			subOrder.put("mobile", body.getString("mobile"));

			subOrderList.add(subOrder);

			amount = amount.add(AmountUtil.str2Amount(_amount));
			discount = discount.add(AmountUtil.str2Amount(_discount));
			payable = payable.add(subPayable);
		}
		log.debug("-------------------------- initStudentOrder(2)：学员子订单创建成功");
		
		if(subOrderList != null && subOrderList.size() > 0)
			orderMapper.createSubOrders(subOrderList);
		// TODO 更新订单总金额
		body.put("amount", amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
		body.put("discount", discount.setScale(2, BigDecimal.ROUND_DOWN).toString());
		body.put("payable", payable.setScale(2, BigDecimal.ROUND_DOWN).toString());
	
		orderMapper.updateOrder(body);
		log.debug("-------------------------- initStudentOrder(3)：学员订单金额更新成功");
	}
}
