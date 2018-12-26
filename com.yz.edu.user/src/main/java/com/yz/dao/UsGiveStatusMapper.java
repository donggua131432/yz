package com.yz.dao;

import com.yz.model.UsGiveStatus;

public interface UsGiveStatusMapper {

    int insertSelective(UsGiveStatus record);

	int countBy(UsGiveStatus giveStatus);
}