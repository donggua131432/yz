package com.yz.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdUniversity;

public interface BdUniversityMapper {
    int deleteByPrimaryKey(String unvsId);

    int insert(BdUniversity record);

    void insertSelective(BdUniversity record);

    BdUniversity selectByPrimaryKey(String unvsId);

    int updateByPrimaryKeySelective(BdUniversity record);

    int updateByPrimaryKey(BdUniversity record);

	List<BdUniversity> selectAll(BdUniversity bdUniversity);

	void deleteAllBdUniversity(@Param("ids") String[] ids);

	List<Map<String, String>> findAllKeyValue(@Param("sName")String sName);

	String getBdUniversityByName(String unvsId);

	List<Map<String, String>> searchUniversity(@Param("unvsName")String unvsName);

}