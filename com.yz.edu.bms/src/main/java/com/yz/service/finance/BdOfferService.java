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
import com.yz.constants.StudentConstants;
import com.yz.dao.finance.BdOfferMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.fee.FeeItemForm;
import com.yz.model.finance.offer.BdOffer;
import com.yz.model.finance.offer.BdOfferQuery;
import com.yz.model.finance.offer.BdOfferResponse;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdOfferService {

	@Autowired
	private BdOfferMapper offerMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectOfferByPage(int start, int length, BdOfferQuery offer) {
		PageHelper.offsetPage(start, length).setRmGroup(false);
		List<BdOfferResponse> offers = offerMapper.selectOfferByPage(offer);
		return new IPageInfo((Page) offers);
	}

	public void insertOffer(BdOffer offer) {
		for (FeeItemForm item : offer.getItems()) {
			BigDecimal amount = AmountUtil.str2Amount(item.getAmount());
			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000049"); // 输入金额不能为负数
			} else {
				item.setAmount(AmountUtil.format(amount, 2));
			}
			item.setOdId(IDGenerator.generatorId());
		}

		List<Map<String, String>> count = offerMapper.selectExistOffer(offer.getPfsnIds(), offer.getTaIds(),
				offer.getScholarship(), offer.getInclusionStatus());

		if (count.size() > 0) {
			String pfsnName = count.get(0).get("pfsnName");
			String taName = count.get(0).get("taName");
			String[] values = { pfsnName, taName };
			throw new BusinessException("E000051", values); // 优惠政策已存在
		}
		if (!StringUtil.hasValue(offer.getInclusionStatus())) {
			offer.setInclusionStatus(StudentConstants.INCLUSION_STATUS_HOLD);
		}

		offer.setOfferId(IDGenerator.generatorId());
		offerMapper.insertOffer(offer);
		List<Map<String, String>> list = offerMapper.selectTmpAddPto(offer);
		for (Map<String, String> map : list) {
			map.put("pto_id", IDGenerator.generatorId());
		}
		offerMapper.insertPtoUpdate(list);
	}

	public void updateOffer(BdOffer offer) {
		for (FeeItemForm item : offer.getItems()) {
			BigDecimal amount = AmountUtil.str2Amount(item.getAmount());
			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				throw new BusinessException("E000049"); // 输入金额不能为负数
			} else {
				item.setAmount(AmountUtil.format(amount, 2));
			}
			item.setOdId(IDGenerator.generatorId());
		}

		offerMapper.deletePtoByOfferId(offer.getOfferId());

		List<Map<String, String>> count = offerMapper.selectExistOffer(offer.getPfsnIds(), offer.getTaIds(),
				offer.getScholarship(), offer.getInclusionStatus());

		if (count.size() > 0) {
			String pfsnName = count.get(0).get("pfsnName");
			String taName = count.get(0).get("taName");
			String[] values = { pfsnName, taName };
			throw new BusinessException("E000051", values); // 优惠政策已存在
		}

		if (!StringUtil.hasValue(offer.getInclusionStatus())) {
			offer.setInclusionStatus(StudentConstants.INCLUSION_STATUS_HOLD);
		}
		
		offerMapper.updateOffer(offer);

		List<Map<String, String>> list = offerMapper.selectTmpAddPto(offer);
		for (Map<String, String> map : list) {
			map.put("pto_id", IDGenerator.generatorId());
		}
		offerMapper.insertPtoUpdate(list);
	}

	public Object selectOfferById(String offerId) {
		return offerMapper.selectOfferById(offerId);
	}

	public void deleteOffer(String offerId) {
		offerMapper.deleteOfferById(offerId);
	}

	public void deleteOffers(String[] offerIds) {
		offerMapper.deleteOffersByIds(offerIds);
	}

	public void blockOffer(String offerId, String status) {
		// 如果为启用，判断是否有启用的相同收费标准
		if (GlobalConstants.STATUS_START.equals(status)) {

			String[] pfsnIds = offerMapper.selectOfferPfsnIds(offerId);
			String[] taIds = offerMapper.selectOfferTaIds(offerId);
			String scholarship = offerMapper.selectOfferScholarship(offerId);
			String inclusionStatus = offerMapper.selectInclusionStatus(offerId);

			// 查找已启用的对应的收费标准
			List<Map<String, String>> count = offerMapper.selectExistOffer(pfsnIds, taIds, scholarship,
					inclusionStatus);

			if (count.size() > 0) {
				String pfsnName = count.get(0).get("pfsnName");
				String taName = count.get(0).get("taName");
				String[] values = { pfsnName, taName };
				throw new BusinessException("E000050", values); // 收费标准已存在
			}
		}
		offerMapper.blockOffer(offerId, status);
	}

	public String selectSgByScholarship(String scholarship) {
		return offerMapper.selectSgByScholarship(scholarship);
	}

}
