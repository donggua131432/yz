package com.yz.dao;

import com.yz.model.UsSign;

public interface UsSignMapper {

    int insertSelective(UsSign record);

    /**
     * 判断用户当天是否签到
     * @param userId
     * @return
     */
    int countTodayBy(String userId);
    /**
     * 查询当天签到数
     * @return
     */
	int countToday();

}