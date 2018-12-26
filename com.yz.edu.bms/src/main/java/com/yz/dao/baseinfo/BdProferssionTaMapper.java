package com.yz.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdProferssionTaKey;


public interface BdProferssionTaMapper {
    int deleteByPrimaryKey(BdProferssionTaKey key);

    int insert(BdProferssionTaKey record);

    int insertSelective(BdProferssionTaKey record);
    /**
     * 根据专业ID 获取考试区县信息
     * @param pfsnId
     * @return
     */
	List<Map<String, String>> getTestArea(String pfsnId);

	void deleteBdProfessionTa(String pfsnId);
	/**
	 * 批量插入专业与考区关系
	 * @param pfsnId
	 * @param testAreaId
	 * @return
	 */
	int insertBatch(@Param("pfsnId") String pfsnId, @Param("taIds") String[] taIds);
}