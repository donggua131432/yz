package com.yz.service.educational;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.educational.JStudentStudyingExcel;
import com.yz.model.educational.JStudentStudyingGKExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.dao.educational.JStudentStudyingMapper;
import com.yz.dao.finance.BdSubOrderMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.educational.JStudentStudyingQuery;
import com.yz.model.educational.JStudentStudyingListInfo;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.util.AmountUtil;

@Service
@Transactional
public class JStudentStudyingService {
	
	@Autowired
	private JStudentStudyingMapper studyingMapper;

	@Autowired
	private BdSubOrderMapper subOrderMapper;

	@Autowired
	private StudentRecruitMapper recruitMapper;

	public Object getStudyingList(JStudentStudyingQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<JStudentStudyingListInfo> list = studyingMapper.getStudyingList(queryInfo);

		String[] remarkTypes = { StudentConstants.REMARK_TYPE_WECHAT, StudentConstants.REMARK_TYPE_TELEPHONE,
				StudentConstants.REMARK_TYPE_QQ };

		for (JStudentStudyingListInfo studyingInfo : list) {
			List<BdSubOrder> subOrderInfos = subOrderMapper.getSubOrders(studyingInfo.getLearnId());
			BigDecimal paid = BigDecimal.ZERO;
			BigDecimal debt = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			for (BdSubOrder subOrder : subOrderInfos) {
				String subOrderStatus = subOrder.getSubOrderStatus();
				
				BigDecimal feeAmount = AmountUtil.str2Amount(subOrder.getPayable());
				
				if (FinanceConstants.ORDER_STATUS_UNPAID.equals(subOrderStatus)) {
					debt = debt.add(feeAmount);
				} else if(FinanceConstants.ORDER_STATUS_PAID.equals(subOrderStatus)) {
					paid = paid.add(feeAmount);
				}
				
				amount = amount.add(feeAmount);
			}

			studyingInfo.setPaid(paid.setScale(2, BigDecimal.ROUND_DOWN).toString());
			studyingInfo.setDebt(debt.setScale(2, BigDecimal.ROUND_DOWN).toString());
			studyingInfo.setAmount(amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
			// 查询备注
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("learnId", studyingInfo.getLearnId());
//			param.put("remarkTypes", remarkTypes);
//			List<BdLearnRemark> remarks = recruitMapper.findLearnRemark(param);
//			studyingInfo.setRemarkList(remarks);
		}

		return new IPageInfo<JStudentStudyingListInfo>((Page<JStudentStudyingListInfo>) list);
	}

	public List<JStudentStudyingExcel> exportStudying(JStudentStudyingQuery query) {
			return  studyingMapper.exportStudying(query);
	}

	public List<JStudentStudyingGKExcel> exportStudyingGK(JStudentStudyingQuery query) {
		return  studyingMapper.exportStudyingGK(query);
	}
}
