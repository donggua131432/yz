package com.yz.dao;

import com.yz.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BsOrderMapper {

	void addNewBsOrder(BsOrderInfo orderInfo);
	
	void addNewBsSerial(BsSerialInfo serialInfo);
	
	void addNewBsSalesOrder(BsSalesOrderInfo orderInfo);
	
	List<Map<String, String>> getBsMyOrderInfo(@Param("userId") String userId);
	
	Map<String, String> getBsOrderDetailInfo(@Param("orderNo") String orderNo);
	
	void addNewBsGoodsOrder(BsGoodsOrder orderInfo);
	
	void insertBsActionMember(List<BsActionMember> list);
	
	void insertBsLogistics(BsLogistics logistics);
	
	/**获取收货地址*/
	Map<String, String> getAddress(String saId);
	
	public void updateJdOrderStatus(@Param("orderStatus") String orderStatus,@Param("orderNo") String orderNo);
	
}
