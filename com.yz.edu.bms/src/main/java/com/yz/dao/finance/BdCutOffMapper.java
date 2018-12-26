package com.yz.dao.finance;

import com.yz.model.finance.BdCutOff;

public interface BdCutOffMapper {

    int insertSelective(BdCutOff record);

    BdCutOff selectByPrimaryKey(String ctNo);

    int updateByPrimaryKey(BdCutOff record);
    
}