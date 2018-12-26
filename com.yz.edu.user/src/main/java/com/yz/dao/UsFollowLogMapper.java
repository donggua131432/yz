package com.yz.dao;

import com.yz.model.UsFollowLog;

public interface UsFollowLogMapper {

    int insertSelective(UsFollowLog record);

    UsFollowLog selectByPrimaryKey(String recrodsNo);

    int updateByPrimaryKeySelective(UsFollowLog record);

}