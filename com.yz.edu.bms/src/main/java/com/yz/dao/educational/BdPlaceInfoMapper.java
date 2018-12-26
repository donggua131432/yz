package com.yz.dao.educational;

import com.yz.model.educational.BdPlaceInfo;

public interface BdPlaceInfoMapper {
    int deleteByPrimaryKey(String placeId);

    int insert(BdPlaceInfo record);

    int insertSelective(BdPlaceInfo record);

    BdPlaceInfo selectByPrimaryKey(String placeId);

    int updateByPrimaryKeySelective(BdPlaceInfo record);

    int updateByPrimaryKey(BdPlaceInfo record);
}