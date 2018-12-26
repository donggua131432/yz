package com.yz.dao.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.message.GwMessageQuery;
import com.yz.model.message.GwMsgTemplate;

public interface GwMsgTemplateMapper {
	int deleteByPrimaryKey(String mtpId);

	int insert(GwMsgTemplate record);

	int insertSelective(GwMsgTemplate record);

	GwMsgTemplate selectByPrimaryKey(String mtpId);

	int updateByPrimaryKeySelective(GwMsgTemplate record);

	int updateByPrimaryKey(GwMsgTemplate record);

	int deleteMsgReceiver(String mtpId);

	int updateMtpStatus(@Param("mtpId") String mtpId, @Param("status") String status, @Param("remark") String remark);

	List<GwMsgTemplate> selectMsgByPage(GwMessageQuery msg);

	List<Map<String, String>> selectMsgReceiver(String mtpId);

	int updateMtpStatusBatch(@Param("mtpIds") String[] mtpIds, @Param("status") String status);

	String selectWechatTemplate(String templateId);

	ArrayList<String> selectMtpIdByLearnId(String learnId);

	String[] selectLearnIdsByMtpId(String mtpId);

}