package com.yz.dao.educational;

import com.yz.model.educational.BdPlaceTime;

public interface BdPlaceTimeMapper {
    int deleteByPrimaryKey(String timeId);

    int insert(BdPlaceTime record);

    int insertSelective(BdPlaceTime record);

    BdPlaceTime selectByPrimaryKey(String timeId);

    int updateByPrimaryKeySelective(BdPlaceTime record);

    int updateByPrimaryKey(BdPlaceTime record);
}