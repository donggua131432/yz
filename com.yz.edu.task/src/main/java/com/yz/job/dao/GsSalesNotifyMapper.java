package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.GsSalesNotify;


public interface GsSalesNotifyMapper {
	
	/**
	 * 获取所有待提醒的用户信息
	 * @param salesId
	 * @param planCount
	 * @return
	 */
	public List<GsSalesNotify> getAllNotifyBySalesId(@Param("salesId") String salesId,@Param("planCount") String planCount);
	
}
