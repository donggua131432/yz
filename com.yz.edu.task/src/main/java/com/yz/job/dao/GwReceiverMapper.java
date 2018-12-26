package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.job.disruptor.CollectMsgSendRespCmd;
import com.yz.job.model.GwReceiver;


public interface GwReceiverMapper {
	
	/**
	 * 获取消息的全部接受对象
	 * @param mtpId
	 * @return
	 */
	List<GwReceiver> selectRecerverByMtpId(@Param("mtpId") String mtpId);
	
	/**
	 * 更改状态为已同步
	 * @param mtpId
	 * @param status
	 * @return
	 */
	int updateMtpStatus(@Param("mtpId") String mtpId, @Param("status") String status);
	
	/**
	 * 更改记录的接受状态
	 * @param srId
	 * @return
	 */
	int insertSendRecords(CollectMsgSendRespCmd send);

}