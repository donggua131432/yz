package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.StudentHistory;

public interface BdsStudentMapper {

	/**
	 * 获取报读学业信息
	 * 
	 * @param stdId
	 * @param type 
	 * @return
	 */
	List<Map<String, String>> getEnrollList(String stdId);

	/**
	 * 查询学员历史学历信息
	 * 
	 * @param learnId
	 * @return
	 */
	Map<String, String> getHistoryInfo(String learnId);

	/**
	 * 更新附件信息
	 * 
	 * @param stdId
	 * @param annexType
	 * @param url
	 */
	int updateAnnex(@Param("stdId") String stdId, @Param("annexType") String annexType, @Param("url") String url);
	/**
	 * 更新学员附件状态
	 * @param stdId
	 * @return
	 */
	int updateAnnexStatus(String stdId);
	/**
	 * 更新学历信息
	 * @param history
	 * @return
	 */
	int updateHistory(StudentHistory history);
	/**
	 * 更新考试资料完善信息
	 * @param learnId
	 * @return
	 */
	int testCompleted(String learnId);

}
