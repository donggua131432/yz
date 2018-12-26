package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.common.UnvsSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.condition.fee.BdFeeQuery;
import com.yz.model.finance.fee.BdFee;
import com.yz.model.finance.fee.BdFeeEdit;
import com.yz.model.finance.fee.BdFeeResponse;
import com.yz.model.finance.fee.FeeInfoResponse;
import com.yz.model.finance.fee.ZTreeResponse;

public interface BdFeeMapper {

	int insert(BdFee record);

	int insertSelective(BdFee record);

	BdFee selectByPrimaryKey(String feeId);

	int updateByPrimaryKeySelective(BdFee record);

	int updateByPrimaryKey(BdFee record);

	List<BdFeeResponse> selectStandardByPage(@Param("fee") BdFeeQuery fee);

	FeeInfoResponse selectStandardById(String feeId);

	int insertBdFee(BdFeeEdit fee);

	int deleteBdFee(String feeId);

	int deleteBdFees(@Param("feeIds") String[] feeIds);

	int updateBdFee(BdFeeEdit fee);

	List<ZTreeResponse> searchPfsn(SelectQueryInfo pfsn);

	UnvsSelectInfo searchUnvs(String unvsId);

	List<ZTreeResponse> searchTa(@Param("pfsnIds") String[] pfsnIds);

	int blockFee(@Param("feeId") String feeId, @Param("status") String status);

	List<Map<String, String>> selectExistFee(@Param("pfsns") String[] pfsns, @Param("testAreas") String[] testAreas,
			@Param("scholarship") String scholarship);

	String[] selectFeePfsnIds(String feeId);

	String[] selectFeeTaIds(String feeId);

	String selectFeeScholarship(String feeId);

	int deleteBdFeePtf(String feeId);

	List<Map<String, String>> selectTmpAddPtf(BdFeeEdit fee);

	void insertUpdatePtf(@Param("list") List<Map<String, String>> list);

}