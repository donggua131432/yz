package com.yz.dao.transfer;

import com.yz.model.transfer.BdCheckRecord;

public interface BdCheckRecordMapper {
    int deleteByPrimaryKey(String crId);

    int insert(BdCheckRecord record);

    int insertSelective(BdCheckRecord record);

    BdCheckRecord selectByPrimaryKey(String crId);

    int updateByPrimaryKeySelective(BdCheckRecord record);

    int updateByPrimaryKey(BdCheckRecord record);

	void updateBdCheckRecord(BdCheckRecord bcr);
}