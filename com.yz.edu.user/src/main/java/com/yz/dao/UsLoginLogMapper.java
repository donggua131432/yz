package com.yz.dao;

import com.yz.model.UsLoginLog;

public interface UsLoginLogMapper {

    int insertSelective(UsLoginLog record);

    UsLoginLog selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(UsLoginLog record);

}