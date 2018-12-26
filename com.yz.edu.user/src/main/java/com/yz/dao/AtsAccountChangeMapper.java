package com.yz.dao;

import com.yz.model.AtsAccountChange;

public interface AtsAccountChangeMapper {

    int insertSelective(AtsAccountChange record);

    AtsAccountChange selectByPrimaryKey(String changeId);

    int updateByPrimaryKeySelective(AtsAccountChange record);
}