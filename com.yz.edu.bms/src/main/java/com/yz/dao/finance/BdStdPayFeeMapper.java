package com.yz.dao.finance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.communi.Body;
import com.yz.model.finance.BdOrder;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.fee.FeeItemForm;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.finance.stdfee.BdPayableQuery;
import com.yz.model.finance.stdfee.BdQRCodePayableInfoResponse;
import com.yz.model.finance.stdfee.BdSerialSurplus;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.finance.stdfee.BdStudentSerial;
import com.yz.model.finance.stdfee.BdSubSerial;
import com.yz.model.oa.OaCampusInfo;
import com.yz.model.recruit.BdLearnInfo;

public interface BdStdPayFeeMapper {

	List<BdPayableInfoResponse> selectStdPayFeeByPage(BdPayableQuery pay);

	BdPayableInfoResponse selectPayableInfoByLearnId(@Param("learnId") String learnId,
			@Param("subOrderStatus") String subOrderStatus);
	
	BdPayableInfoResponse selectPayableInfoByStdId(@Param("stdId") String stdId,
			@Param("subOrderStatus") String subOrderStatus);

	int updateSubOrderPaid(@Param("learnId") String learnId, @Param("itemCodes") String[] itemCodes);

	List<FeeItemForm> selectAmountByItems(@Param("learnId") String learnId, @Param("itemCodes") String[] itemCodes);

	int insertOrderSerial(@Param("serial") BdStudentSerial serial, @Param("subOrderNos") String[] subOrderNos);

	BdOrder selectOrder(@Param("learnId") String learnId);

	List<BdStudentSerial> selectPayDetailByLearnId(String learnId);

	String[] selectPaySubOrderNos(@Param("learnId") String learnId, @Param("itemCodes") String[] itemCodes);

	List<HashMap<String, String>> findStudentInfo(@Param("sName") String sName);

	int addPayableItem(BdSubOrder subOrder);

	List<BdStdPayInfoResponse> findItemCodeHaveNot(@Param("sName") String sName, @Param("learnId") String learnId);

	List<Body> selectStudentSerialByLearnId(@Param("learnId") String learnId);
	
	List<BdStdPayInfoResponse> selectPayableInfo(@Param("learnId") String learnId,
			@Param("subOrderStatus") String subOrderStatus);

	String selectSubOrderStatusByItemCode(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	int updateStdStage(@Param("learnId") String learnId, @Param("stdStage") String stdStage);

	List<BdCoupon> selectCouponByLearnId(@Param("stdId") String stdId, @Param("userId") String userId,
			@Param("learnId") String learnId);

	BdCoupon selectCouponAmount(String couponId);

	int updateCouponUsed(@Param("scId") String scId, @Param("stdId") String stdId);

	int insertSubSerial(BdSubSerial delay);

	String selectScId(@Param("couponId") String couponId, @Param("stdId") String stdId);

	String selectUserId(String stdId);

	BdCoupon selectCouponAmountToPay(@Param("couponId") String couponId, @Param("scId") String scId,
			@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("userId") String userId);

	OaCampusInfo selectCampusInfoByEmpId(String empId);

	String selectStdStageByLearnId(String learnId);

	String selectStdIdByLearnId(String learnId);

	String selectPaidTutorAmount(String learnId);

	int updateAfterAmount(BdStudentSerial serial);

	Map<String, String> selectTutorAndRecruitUserId(String learnId);

	String[] selectItemNameBySerialNo(String serialNo);

	String selectStdNameBySerialNo(String serialNo);

	String selectRecruitType(String learnId);
	
	
	/**
	 * 二维码支付
	 */
	List<BdQRCodePayableInfoResponse> selectStdPayFeeByCondition(@Param("condition") String condition,@Param("empId") String empId);
	
	BdPayableInfoResponse selectPayableInfoByLearnIdForQRCode(@Param("learnId") String learnId,
			@Param("subOrderStatus") String subOrderStatus);
	/**
	 * 通过学员id获取绑定userId
	 * @param stdId
	 * @return
	 */
	String getUserId(String stdId);
	
	int insertSerialSurplus(BdSerialSurplus surplus);
	
	int updateSerialSurplus(BdSerialSurplus surplus);
	
	String[] selectItemCodeByStdid(@Param("stdId") String stdId);
	
	String[] selectItemNameBySerialMark(String serialMark);
	
	int insertSerialBatch(BdStudentSerial serial);
	
	String selectPaySubOrderNo(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	String selectGradeByLearnId(String learnId);
	
	List<BdCoupon> selectAbleCouponByLearnId(@Param("stdId") String stdId, @Param("userId") String userId,
			@Param("learnId") String learnId);
	
	BdLearnInfo selectLearnInfoByLearnId(String learnId);
	
	List<String> getStdLearndIdsByCond(BdPayableQuery pay);

	String selectFeeNameByLearnId(String learnId);

	String selectOfferNameByLearnId(String learnId);

	String selectStdIdByUserId(String userId);

	Map<String, String> selectStdInfoByStdId(String stdId);

	List<BdStudentSerial> selectPayDetailByStdId(String stdId);

	String selectPaidSumByStdId(String stdId);

	String selectWithdrawByStdId(String stdId);

	int updateTutionTime(String learnId);

	Map<String, String> selectStdUserIdByLearnId(String learnId);
	
	/**
	 * 根据流水获取缴费科目
	 * @param serialMark
	 * @return
	 */
	List<String> selectItemCodesBySerialMark(@Param("serialMark") String serialMark);
}