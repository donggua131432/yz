package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdCutOff;

public interface BdCutOffMapper {
    int insertSelective(BdCutOff record);

    BdCutOff selectByPrimaryKey(String ctNo);

    int updateByPrimaryKeySelective(BdCutOff record);

	List<Map<String, String>> selectCtSerials(String payeeId);

	int updateSerialCtStatus(@Param("serialList") List<Map<String, String>> serialList, @Param("ctStatus") String ctStatus);

	int updateSerialCtCount(@Param("serialList") List<Map<String, String>> serialList);
	/**
	 * 设置待结算状态
	 * @param serialNo
	 * @param ctStatusNeed 
	 */
	int setNeedCutOff(@Param("serialNo") String serialNo, @Param("ctStatus") String ctStatus);
}