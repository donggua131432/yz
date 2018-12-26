package com.yz.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.BdSubOrder;

public interface BdSubOrderMapper {
	/**
	 * 获取子订单信息
	 * @param subOrderNo
	 * @return
	 */
	BdSubOrder getSubOrder(String subOrderNo);
	/**
	 * 创建子订单
	 * @param subOrderInfo
	 * @return
	 */
	int createSubOrder(BdSubOrder subOrderInfo);
	/**
	 * 更新子订单信息
	 * @param subOrderInfo
	 * @return
	 */
	int updateSubOrder(BdSubOrder subOrderInfo);
	/**
	 * 根据订单号获取子订单数据
	 * @param subOrderInfo
	 * @return
	 */
	List<BdSubOrder> getSubOrderForOrderNo(String orderNo);
	/**
	 * 查询学员子订单信息
	 * @param learnId
	 * @return
	 */
	List<BdSubOrder> getSubOrders(@Param("learnId") String learnId);
}
