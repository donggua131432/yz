package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.communi.Body;
import com.yz.model.finance.BdStudentOrder;
import com.yz.model.finance.order.BdOrderRefund;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdLearnInfo;

public interface BdOrderMapper {

	/**
	 * 创建订单
	 * 
	 * @param orderInfo
	 */
	public int createOrder(BdStudentOrder orderInfo);

	/**
	 * 更新订单信息
	 * 
	 * @param orderInfo
	 */
	public int updateOrder(BdStudentOrder orderInfo);

	/**
	 * 查询阶段所需科目
	 * 
	 * @param learnInfo
	 * @return
	 */
	public List<BdFeeInfo> getRequireItemCodes(BdLearnInfo learnInfo);

	/**
	 * 获取收费标准与优惠规则
	 * 
	 * @param feeId
	 * @param offerId
	 * @param itemCode
	 * @return
	 */
	public BdFeeInfo getFeeInfo(@Param("feeId") String feeId, @Param("offerId") String offerId,
			@Param("itemCode") String itemCode);

	/**
	 * 学员退学订单退款处理中
	 * 
	 * @param learnId
	 */
	public int refundingOrder(String learnId);

	/**
	 * 插入退款信息
	 * 
	 * @param refund
	 * @return
	 */
	public int insertRefundInfo(BdOrderRefund refund);

	/**
	 * 修改学员退费金额
	 * 
	 * @param learnId
	 * @param items
	 * @param amount
	 */
	public int updateOrderRefundAmount(@Param("learnId") String learnId,
			@Param("items") List<BdStdPayInfoResponse> items, @Param("amount") String amount);

	/**
	 * 退款拒绝，撤回状态
	 * 
	 * @param learnId
	 * @return
	 */
	public int rejectRefund(@Param("learnId") String learnId);

	/**
	 * 完成退款
	 * 
	 * @param learnId
	 * @return
	 */
	public int finishiRefund(@Param("learnId") String learnId);

	/**
	 * 查询退款信息
	 * 
	 * @param learnId
	 * @return
	 */
	public List<BdOrderRefund> selectRefundInfo(String learnId);

	public String selectOrderNoByLearnId(String learnId);

	void finishStudentRefund(String refundId);

	/**
	 * 2017 学员缴费老数据处理
	 * 
	 * @return
	 */
	public List<Map<String, String>> getStudentPayOrder();

	/**
	 * 查询是否已有订单退款记录
	 * 
	 * @param learnId
	 * @param checkType
	 * @return
	 */
	public int selectCountOrderRefunde(@Param("learnId") String learnId, @Param("checkType") String checkType);

	/**
	 * 查询正在使用的缴费科目
	 * 
	 * @param learnId
	 * @return
	 */
	public String[] selectItemCodeByLearnId(String learnId);

	/**
	 * 查询科目应缴额
	 * 
	 * @param learnId
	 * @param itemCode
	 * @return
	 */
	public String selectItemCodePayable(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	public BdStudentOrder selectOrderByOrderNo(String orderNo);

	public String selectAccAmount(@Param("accType")String accType, @Param("stdId")String stdId);

	public void updateAccAmount(@Param("accType")String accType, @Param("stdId")String stdId, @Param("balance")String balance);

	public void insertAccSerial(Body body);
}
