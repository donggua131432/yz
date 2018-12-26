package com.yz.dao;

import com.yz.model.GsSalesNotify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GsSalesNotifyMapper {

	List<GsSalesNotify> getGsSalesNotify();
	
	void singleDeleteSalesNotify(String notifyId);
	
	void batchDeleteSalesNotify(@Param("ids") String[] ids);

	List<GsSalesNotify> getAllNotifyBySalesId(@Param("salesId") String salesId, @Param("planCount") String planCount);
	
	void updateNotifyStatus(@Param("notifyId") String notifyId);
}
