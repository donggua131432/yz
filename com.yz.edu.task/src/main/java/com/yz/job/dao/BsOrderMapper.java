package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.BsOrderInfo;

public interface BsOrderMapper {
	
	/**
	 * 查询京东需要同步的订单信息
	 * @return
	 */
	public List<BsOrderInfo> querySynchronousJdOrderList();
	
	/**
	 * 同步京东订单状态
	 * @param resultList
	 */
	public void updateOrderStatus(@Param(value="orderlist")List<BsOrderInfo> resultList);
	
	public void updateOrderCompletTime(@Param(value="jdOrderId") String jdOrderId,@Param(value="orderTime") String orderTime);
}
