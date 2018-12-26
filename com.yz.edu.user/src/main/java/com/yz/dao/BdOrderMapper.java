package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.communi.Body;
import com.yz.model.payment.BdSubOrder;

public interface BdOrderMapper {

	/**
	 * 创建订单
	 * 
	 * @param orderInfo
	 */
	public int createOrder(Body body);
	

	/**
	 * 查询阶段所需科目
	 * 
	 * @param learnInfo
	 * @return
	 */
	public List<Map<String, String>> getRequireItemCodes(Body body);

	/**
	 * 获取收费标准与优惠规则
	 * 
	 * @param feeId
	 * @param offerId
	 * @param itemCode
	 * @return
	 */
	public Map<String, String> getFeeInfo(@Param("feeId") String feeId, @Param("offerId") String offerId,
			@Param("itemCode") String itemCode);
	

	/**
	 * 根据学业ID查询订单号
	 * @param learnId
	 * @return
	 */
	public String selectOrderNoByLearnId(String learnId);

	/**
	 * 根据学业ID查询子订单数据
	 * @param learnId
	 * @return
	 */
	public List<BdSubOrder> selectSubOrderByLearnId(String learnId);
	
	/**
	 * 新增子订单信息
	 * @param subOrderList
	 * @return
	 */
	public int createSubOrders(@Param("list") List<Map<String, String>> subOrderList);
	
	/**
	 * 更新订单信息
	 * @param body
	 * @return
	 */
	public int updateOrder(Body body);

	/**
	 * 根据招生老师查询 招生老师所属校区的财务编码
	 * @param recruitEmpId
	 * @return
	 */
	public String selectFinanceCodeByEmp(String recruitEmpId);

}
