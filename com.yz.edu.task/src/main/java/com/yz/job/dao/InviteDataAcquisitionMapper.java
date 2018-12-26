package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.InviteDataAcquisitionEvent;
 

public interface InviteDataAcquisitionMapper {

	/**
	 * @desc 保存采集到的数据
	 * @param record
	 */
	public void saveInviteDataAcquisition(@Param(value = "record") List<InviteDataAcquisitionEvent> record);
	
	public void addLookCount(@Param(value = "channel_id") String channel_id,@Param(value = "count") String count);
	
	public String getLookCount(String channel_id);
}
