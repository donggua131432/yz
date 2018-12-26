package com.yz.dao.baseinfo;

import com.yz.model.baseinfo.BdPlanTextbookKey;

public interface BdPlanTextbookMapper {
    int deleteByPrimaryKey(BdPlanTextbookKey key);

    int insert(BdPlanTextbookKey record);

    int insertSelective(BdPlanTextbookKey record);

	boolean selectPlanTextbook(BdPlanTextbookKey planTextbookKey);
}