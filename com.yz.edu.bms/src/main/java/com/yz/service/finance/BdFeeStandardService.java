package com.yz.service.finance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GlobalConstants;
import com.yz.dao.finance.BdFeeMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.fee.BdFeeQuery;
import com.yz.model.finance.fee.BdFeeEdit;
import com.yz.model.finance.fee.BdFeeResponse;
import com.yz.model.finance.fee.FeeItemForm;
import com.yz.util.AmountUtil;

/**
 * Description:收费标准service
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年6月8日.
 *
 */
@Service
@Transactional
public class BdFeeStandardService {

	@Autowired
	private BdFeeMapper feeMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectStandardByPage(int start, int length, BdFeeQuery fee) {
		PageHelper.offsetPage(start, length).setRmGroup(false);
		List<BdFeeResponse> standards = feeMapper.selectStandardByPage(fee);
		return new IPageInfo((Page) standards);
	}

	public Object selectStandardById(String feeId) {
		return feeMapper.selectStandardById(feeId);
	}

	public void insertBdFee(BdFeeEdit fee) {
		for (FeeItemForm item : fee.getItems()) {
			BigDecimal amount = AmountUtil.str2Amount(item.getAmount());
			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000049"); // 输入金额不能为负数
			} else {
				item.setAmount(AmountUtil.format(amount, 2));
			}
			item.setFdId(IDGenerator.generatorId());
		}

		List<Map<String, String>> count = feeMapper.selectExistFee(fee.getPfsns(), fee.getTestAreas(),
				fee.getScholarship());

		if (count.size() > 0) {
			String pfsnName = count.get(0).get("pfsnName");
			String taName = count.get(0).get("taName");
			String[] values = { pfsnName, taName };
			throw new BusinessException("E000050", values); // 收费标准已存在
		}
		fee.setFeeId(IDGenerator.generatorId());
		feeMapper.insertBdFee(fee);
		List<Map<String, String>> list = feeMapper.selectTmpAddPtf(fee);
		for (Map<String, String> map : list) {
			map.put("ptf_id", IDGenerator.generatorId());
		}
		feeMapper.insertUpdatePtf(list);
	}

	public void deleteBdFee(String feeId) {
		feeMapper.deleteBdFee(feeId);
	}

	public void deleteBdFees(String[] feeIds) {
		feeMapper.deleteBdFees(feeIds);
	}

	public void updateBdFee(BdFeeEdit fee) {
		for (FeeItemForm item : fee.getItems()) {
			BigDecimal amount = AmountUtil.str2Amount(item.getAmount());
			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000049"); // 输入金额不能为负数
			} else {
				item.setAmount(AmountUtil.format(amount, 2));
			}
			item.setFdId(IDGenerator.generatorId());
		}
		
		feeMapper.deleteBdFeePtf(fee.getFeeId());
		
		List<Map<String, String>> count = feeMapper.selectExistFee(fee.getPfsns(), fee.getTestAreas(),
				fee.getScholarship());

		if (count.size() > 0) {
			String pfsnName = count.get(0).get("pfsnName");
			String taName = count.get(0).get("taName");
			String[] values = { pfsnName, taName };
			throw new BusinessException("E000050", values); // 收费标准已存在
		}
		List<Map<String, String>> list = feeMapper.selectTmpAddPtf(fee);
		for (Map<String, String> map : list) {
			map.put("ptf_id", IDGenerator.generatorId());
		}
		feeMapper.insertUpdatePtf(list);
		feeMapper.updateBdFee(fee);
	}

	public void blockFee(String feeId, String status) {

		// 如果为启用，判断是否有启用的相同收费标准
		if (GlobalConstants.STATUS_START.equals(status)) {

			String[] pfsnIds = feeMapper.selectFeePfsnIds(feeId);
			String[] taIds = feeMapper.selectFeeTaIds(feeId);
			String scholarship = feeMapper.selectFeeScholarship(feeId);

			// 查找已启用的对应的收费标准
			List<Map<String, String>> count = feeMapper.selectExistFee(pfsnIds, taIds, scholarship);

			if (count.size() > 0) {
				String pfsnName = count.get(0).get("pfsnName");
				String taName = count.get(0).get("taName");
				String[] values = { pfsnName, taName };
				throw new BusinessException("E000050", values); // 收费标准已存在
			}
		}
		feeMapper.blockFee(feeId, status);
	}

}
