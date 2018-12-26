package com.yz.dao;


import java.util.List;

import com.yz.model.BdPayee;

public interface BdPayeeMapper {

    int insertSelective(BdPayee record);

    BdPayee selectByPrimaryKey(String payeeId);

    int updateByPrimaryKeySelective(BdPayee record);
    
    List<BdPayee> selectAll();
}