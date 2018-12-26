package com.yz.dao;

import com.yz.model.AtsAwardRecord;

public interface AtsAwardRecordMapper {

    int insertSelective(AtsAwardRecord record);

	int countBy(AtsAwardRecord record);

	int newCountBy(AtsAwardRecord record);
}