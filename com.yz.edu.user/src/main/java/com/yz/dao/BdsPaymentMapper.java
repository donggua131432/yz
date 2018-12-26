package com.yz.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdPaymentClearing;
import com.yz.model.BdSerialSurplus;
import com.yz.model.coupon.BdCoupon;
import com.yz.model.payment.BdFeeItem;
import com.yz.model.payment.BdOrder;
import com.yz.model.payment.BdPayInfoDetail;
import com.yz.model.payment.BdPayableInfoResponse;
import com.yz.model.payment.BdStudentSerial;
import com.yz.model.payment.BdSubSerial;
import com.yz.model.payment.FeeItemForm;
import com.yz.model.payment.OaCampusInfo;

public interface BdsPaymentMapper {

	List<HashMap<String, Object>> selectPayDetailByLearnId(String learnId);

	String selectZmCountBySerialNo(String serialNo);

	List<BdCoupon> selectCouponByLearnId(String userId);

	BdPayableInfoResponse selectPayableInfoByLearnId(@Param("learnId") String learnId,
			@Param("subOrderStatus") String subOrderStatus);

	/**
	 * 学员录取通知待缴费科目列表
	 * 
	 * @param learnId
	 * @param subOrderStatus
	 * @return
	 */
	BdPayableInfoResponse selectStuPayableInfoResultMap(@Param("learnId") String learnId,
			@Param("subOrderStatus") String subOrderStatus);

	String selectOrderNoBySerialNo(String serialMark);

	List<BdCoupon> selectAbleCouponByLearnId(@Param("stdId") String stdId, @Param("userId") String userId,
			@Param("learnId") String learnId);

	BdOrder selectOrder(@Param("learnId") String learnId);

	List<FeeItemForm> selectAmountByItems(@Param("learnId") String learnId, @Param("itemCodes") List<String> itemCodes);

	BdCoupon selectCouponAmountToPay(@Param("couponId") String couponId, @Param("scId") String scId,
			@Param("learnId") String learnId, @Param("stdId") String stdId, @Param("userId") String userId);

	int updateCouponUsed(@Param("scId") String scId, @Param("isUse") String isUse);

	String selectUserId(String stdId);

	String[] selectPaySubOrderNos(@Param("learnId") String learnId, @Param("itemCodes") List<String> itemCodes);

	int insertOrderSerial(@Param("serial") BdStudentSerial serial, @Param("subOrderNos") String[] subOrderNos);

	int insertSubSerial(BdSubSerial delay);

	String selectScId(@Param("couponId") String couponId, @Param("stdId") String stdId);

	int updateSubOrderPaid(@Param("learnId") String learnId, @Param("itemCodes") List<String> itemCodes);

	int updateStdStage(@Param("learnId") String learnId, @Param("stdStage") String stdStage);

	int insertFirstRegist(@Param("learnId") String learnId, @Param("updateUser") String updateUser,
			@Param("updateUserId") String updateUserId);

	int updateSubOrderStatus(@Param("learnId") String learnId, @Param("itemCodes") List<String> itemCodes,
			@Param("orderStatus") String orderStatus);

	BdStudentSerial selectSerialBySerialNo(String serialNo);

	int selectExistFinishedSerial(String serialNo);

	int updateSerialStatus(@Param("serialMark") String serialMark, @Param("serialStatus") String serialStatus,
			@Param("outSerialNo") String outSerialNo);

	List<BdStudentSerial> selectSerialDetail(String serialMark);

	List<Map<String, String>> selectWithdrawSerial(String stdId);

	OaCampusInfo selectCampusInfoByLearnId(String learnId);

	int selectCouponUnUsed(@Param("couponId") String couponId, @Param("learnId") String learnId);

	String[] selectItemNameBySerialMark(String serialMark);

	String selectStdNameBySerialNo(String serialNo);

	int updateSerialCodeUrl(@Param("serialMark") String serialMark, @Param("codeUrl") String codeUrl);

	public BdPayInfoDetail getPayDetail(@Param("serialNo") String serialNo);

	String selectPaySubOrderNo(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	int insertSerialBatch(BdStudentSerial serial);

	int insertSerialSurplus(BdSerialSurplus surplus);

	int updateSerialSurplus(BdSerialSurplus surplus);

	List<BdSerialSurplus> selectSurplusBySerialMark(String serialMark);

	List<String> selectItemCodesBySerialMark(String serialMark);

	String selectPayableAmountBySerialMark(String serialMark);

	/**
	 * 根据批次号查询缴费年份
	 * 
	 * @param serialMark
	 * @return
	 */
	String[] selectYearsBySerialMark(String serialMark);

	// 获取员工信息
	Map<String, String> getEmpInfoByEmpId(String empId);

	String selectGradeByLearnId(String learnId);

	/**
	 * 查询科目数组中是否已缴费
	 * 
	 * @param itemCodes
	 * @return
	 */
	int selectPaidCountByItemCodes(@Param("orderNo") String orderNo, @Param("itemCodes") ArrayList<String> itemCodes);

	void insertPaymentClearing(BdPaymentClearing c);

	List<BdPaymentClearing> selectPaymentClearingUncheck();

	void updatePaymentClearingStatus(@Param("clearId") String clearId, @Param("status") String status);

	void updatePaytime(String serialMark);

	int updateTutionTime(String learnId);

	Map<String, String> selectStdUserIdByLearnId(String learnId);

}
