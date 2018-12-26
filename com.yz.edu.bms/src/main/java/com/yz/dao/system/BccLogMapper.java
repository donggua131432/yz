package com.yz.dao.system;

import java.util.List;

import com.yz.core.datasource.DB;
import com.yz.core.datasource.Database;
import com.yz.model.condition.system.LogQueryInfo;
import com.yz.model.system.BccLogWithBLOBs;

@DB(db=Database.BCC)
public interface BccLogMapper {

    int insertSelective(BccLogWithBLOBs record);

    BccLogWithBLOBs selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(BccLogWithBLOBs record);

	List<BccLogWithBLOBs> selectAll(LogQueryInfo queryInfo);
}