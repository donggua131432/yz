package com.yz.dao.finance;

import org.apache.ibatis.annotations.Param;

import com.yz.model.CommunicationMap;
import com.yz.model.finance.BdUnaward;

public interface BdUnawardMapper {

    int insertMap(CommunicationMap map);

    int insertSelective(BdUnaward record);

    BdUnaward selectByPrimaryKey(String recordsNo);

    int updateByPrimaryKeySelective(BdUnaward record);
    /**
     * 是否已存在奖励记录
     * @param stdId
     * @param ruleCode
     * @return
     */
	int isExsit(@Param("stdId") String stdId, @Param("ruleCode") String ruleCode);

}