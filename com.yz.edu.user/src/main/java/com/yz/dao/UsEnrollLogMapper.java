package com.yz.dao;

import com.yz.model.UsEnrollLog;

public interface UsEnrollLogMapper {
	/**
	 * 新增报读记录
	 * @param record
	 * @return
	 */
    int insertSelective(UsEnrollLog record);

}