package com.yz.dao.finance;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.fee.BdRtItem;
import com.yz.model.finance.feeitem.BdFeeItem;

public interface BdFeeItemMapper {
	int deleteByPrimaryKey(String itemCode);

	int insert(BdFeeItem record);

	int insertSelective(@Param("item") BdFeeItem item, @Param("recruitTypes") String[] recruitTypes,
			@Param("rts") List<BdRtItem> list);

	BdFeeItem selectByPrimaryKey(String itemCode);

	int updateByPrimaryKeySelective(BdFeeItem record);

	int updateByPrimaryKey(BdFeeItem record);

	List<HashMap<String, Object>> selectFeeItemByPage(@Param("item") BdFeeItem item);

	int deleteFeeItems(@Param("itemCodes") String[] itemCodes);

	BdFeeItem selectItemInfoById(String itemCode);

	int updateItemCodeRecruitType(@Param("itemCode") String itemCode, @Param("recruitTypes") String[] recruitTypes,
			@Param("rts") List<BdRtItem> list);

	List<BdFeeItem> selectFeeItemByRecruitType(@Param("recruitType") String recruitType);
}