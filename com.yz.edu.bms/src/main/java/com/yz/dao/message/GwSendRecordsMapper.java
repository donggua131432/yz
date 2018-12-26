package com.yz.dao.message;

import java.util.List;
import java.util.Map;

import com.yz.model.message.GwRecordExport;
import com.yz.model.message.GwRecordQuery;
import com.yz.model.message.GwSendRecords;


public interface GwSendRecordsMapper {
    int deleteByPrimaryKey(String srId);

    int insert(GwSendRecords record);

    int insertSelective(GwSendRecords record);

    GwSendRecords selectByPrimaryKey(String srId);

    int updateByPrimaryKeySelective(GwSendRecords record);

    int updateByPrimaryKey(GwSendRecords record);

	List<Map<String, String>> selectRecordByPage(GwRecordQuery record);
	
	public List<GwRecordExport> selectRecordForExport(GwRecordQuery record);
}