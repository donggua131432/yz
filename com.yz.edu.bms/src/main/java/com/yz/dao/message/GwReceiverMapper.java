package com.yz.dao.message;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.core.datasource.DB;
import com.yz.core.datasource.Database;
import com.yz.model.message.GwReceiver;

public interface GwReceiverMapper {
	int deleteByPrimaryKey(String receiverId);

	int insert(GwReceiver record);

	int insertSelective(GwReceiver record);

	GwReceiver selectByPrimaryKey(String receiverId);

	int updateByPrimaryKeySelective(GwReceiver record);

	int updateByPrimaryKey(GwReceiver record);

	List<String> selectByLearnId(String learnId);

	int insertMsgReceiver(@Param("receiverId") String receiverId, @Param("mtpId") String mtpId);

	List<GwReceiver> selectRecerverByMtpId(String mtpId);

	String selectReceiveMsgId(@Param("receiverId") String receiverId, @Param("mtpId") String mtpId);

	int deleteMtpReceiverByLearnId(@Param("learnIds") String[] learnIds, @Param("mtpId") String mtpId);

	int selectCountByLearnId(@Param("learnId") String learnId, @Param("mtpId") String mtpId);

}