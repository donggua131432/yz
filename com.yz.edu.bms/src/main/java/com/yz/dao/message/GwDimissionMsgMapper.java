package com.yz.dao.message;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.core.datasource.DB;
import com.yz.core.datasource.Database;
import com.yz.model.message.GwDimissionMsg;

public interface GwDimissionMsgMapper {

	int deleteByPrimaryKey(String msgId);

	int insert(GwDimissionMsg record);

	int insertSelective(GwDimissionMsg record);

	GwDimissionMsg selectByPrimaryKey(String msgId);

	int updateByPrimaryKeySelective(GwDimissionMsg record);

	int updateByPrimaryKey(GwDimissionMsg record);

	List<GwDimissionMsg> selectDimissionMsgList(GwDimissionMsg query);

	/**
	 * 
	 * @param empId
	 * @param msgType
	 * @return
	 */
	int selectDimissionCount(@Param("empId") String empId, @Param("msgType") String msgType);
	
	void batchInsertSelective(@Param("list") List<GwDimissionMsg> msgList);

}
