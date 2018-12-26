package com.yz.dao.recruit;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.recruit.BdLearnAnnex;

public interface BdLearnAnnexMapper {
    int deleteByPrimaryKey(String annexId);

    int insertSelective(BdLearnAnnex record);

    BdLearnAnnex selectByPrimaryKey(String annexId);

    int updateByPrimaryKeySelective(BdLearnAnnex record);
    /**
     * 批量插入
     * @param annexList
     */
	int batchInsert(@Param("annexList") List<BdLearnAnnex> annexList);
	/**
	 * 查询学员附件信息
	 * @param learnId
	 * @return
	 */
	List<BdLearnAnnex> getAnnexList(String learnId);
	
	/**
	 * 查询学业对应附件状态的文件数量
	 * @param stdId
	 * @param annexStatus
	 */
	int countBy(@Param("learnId") String learnId, @Param("annexStatus") String annexStatus);

	/**
	 * 更新学业附件状态
	 * @param learnId
	 * @param annexStatus
	 */
	int updateLearnAnnexStatus(@Param("learnId") String learnId, @Param("annexStatus") String annexStatus);
	/**
	 * 查询必传附件
	 * @param learnId
	 * @return
	 */
	List<BdLearnAnnex> selectRequiredAnnex(String learnId);
	/**
	 * 临时
	 * @return
	 */
	int countAll();
	/**
	 * 临时
	 * @return
	 */
	List<String> selectAll(@Param("start") int start, @Param("size") int size);

	String selectGradeByLearnId(String learnId);
}