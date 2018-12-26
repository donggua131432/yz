package com.yz.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdUniversityTest;

public interface BdUniversityTestMapper {
    int insert(BdUniversityTest record);

    int insertSelective(BdUniversityTest record);

	List<BdUniversityTest> getTestArea(String unvsId);

	void updateBdUniversityTest(@Param("unvsId")String unvsId, @Param("testAreaId")String testAreaId);

	void deleteBdUniversityTest(String unvsId);
	/**
	 * 获取院校与考试区县关系
	 * @param unvsId
	 * @return
	 */
	List<Map<String, String>> getTaMap(String unvsId);

	/**
	 * 批量插入
	 * @param unvsId
	 * @param taIds
	 * @return
	 */
	int insertBatch(@Param("unvsId") String unvsId, @Param("taIds") String[] taIds);
}