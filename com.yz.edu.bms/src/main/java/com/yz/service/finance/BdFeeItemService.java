package com.yz.service.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FeeConstants;
import com.yz.constants.StudentConstants;
import com.yz.dao.finance.BdFeeItemMapper;
import com.yz.exception.BusinessException;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.fee.BdRtItem;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdFeeItemService {

	@Autowired
	private BdFeeItemMapper itemMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryFeeItemByPage(int start, int length, BdFeeItem item) {
		PageHelper.offsetPage(start, length);
		List<HashMap<String, Object>> items = itemMapper.selectFeeItemByPage(item);
		return new IPageInfo((Page) items);
	}

	public void updateFeeItem(BdFeeItem item, String[] recruitTypes) {
		itemMapper.updateByPrimaryKeySelective(item);
		itemMapper.updateItemCodeRecruitType(item.getItemCode(), recruitTypes, checkStdStage(item, recruitTypes));
	}

	public void insertFeeItem(BdFeeItem item, String[] recruitTypes) {

		itemMapper.insertSelective(item, recruitTypes, checkStdStage(item, recruitTypes));
	}

	/**
	 * 根据科目类型返回对应学员状态
	 * 
	 * @param itemType
	 * @return
	 */
	private List<BdRtItem> checkStdStage(BdFeeItem item, String[] recruitTypes) {

		List<BdRtItem> rts = new ArrayList<BdRtItem>();

		if (null != recruitTypes && recruitTypes.length <= 0) {
			throw new BusinessException("E000075");// 招生类型不能为空
		}

		for (String recruitType : recruitTypes) {
			String stdStage = null;
			BdRtItem rt = new BdRtItem();
			rt.setItemCode(item.getItemCode());
			rt.setRecruitType(recruitType);

			if (StringUtil.hasValue(item.getItemType())) {
				if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
					if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						stdStage = StudentConstants.STD_STAGE_PURPOSE;
					}
				} else if (FeeConstants.FEE_ITEM_TYPE_TUITION.equals(item.getItemType())
						|| FeeConstants.FEE_ITEM_TYPE_BOOK.equals(item.getItemType())
						|| FeeConstants.FEE_ITEM_TYPE_NET.equals(item.getItemType())) {
					if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						stdStage = StudentConstants.STD_STAGE_ENROLLED;
					} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
						stdStage = StudentConstants.STD_STAGE_PURPOSE;
					}
				}
				rt.setStdStage(stdStage);
				rts.add(rt);

			} else {
				throw new BusinessException("E000046");// 科目类型不能为空
			}

		}
		return rts;
	}

	public void deleteFeeItems(String[] itemCodes) {
		itemMapper.deleteFeeItems(itemCodes);
	}

	public void deleteFeeItem(String itemCode) {
		itemMapper.deleteByPrimaryKey(itemCode);
	}

	public BdFeeItem selectFeeItemByItemCode(String itemCode) {
		return itemMapper.selectItemInfoById(itemCode);
	}

	public void blockFeeItem(BdFeeItem item) {
		itemMapper.updateByPrimaryKeySelective(item);
	}

	public List<BdFeeItem> selectFeeItemByRecruitType(String recruitType) {

		return itemMapper.selectFeeItemByRecruitType(recruitType);
	}

}
