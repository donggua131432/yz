package com.yz.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.goods.GsSalesNotify;
public interface GsSalesNotifyMapper {

	public List<GsSalesNotify> getGsSalesNotify();
	
	public void singleDeleteSalesNotify(String notifyId);
	
	public void batchDeleteSalesNotify(@Param("ids") String[] ids);
	
	public List<GsSalesNotify> getAllNotifyBySalesId(@Param("salesId") String salesId,@Param("planCount") String planCount);
	
	public void updateNotifyStatus(@Param("notifyId") String notifyId);
}
