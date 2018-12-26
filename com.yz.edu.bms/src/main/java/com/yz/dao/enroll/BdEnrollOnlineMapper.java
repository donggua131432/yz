package com.yz.dao.enroll;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.enroll.BdEnrollOnline;

public interface BdEnrollOnlineMapper {
	
    int insertSelective(BdEnrollOnline record);

    BdEnrollOnline selectByPrimaryKey(String learnId);

    int updateByPrimaryKeySelective(BdEnrollOnline record);

	List<Map<String, String>> selectList();

	List<BdEnrollOnline> selectEnrollList();

	int insertBatch(@Param("params") List<Map<String, String>> params);
}