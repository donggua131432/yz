package com.yz.dao.us;

import java.util.List;

import com.yz.model.us.UsFollowLog;

public interface UsFollowLogMapper {

    int insertSelective(UsFollowLog record);

    UsFollowLog selectByPrimaryKey(String recrodsNo);

    int updateByPrimaryKeySelective(UsFollowLog record);

	List<UsFollowLog> selectByUserId(String userId);


    String selectCountByUserId(String userId);

}