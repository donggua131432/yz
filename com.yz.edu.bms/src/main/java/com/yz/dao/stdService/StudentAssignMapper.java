package com.yz.dao.stdService;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.stdService.StudentAssignQuery;
import com.yz.model.stdService.StudentAssignInfo;
import com.yz.model.stdService.StudentAssignListInfo;

public interface StudentAssignMapper {
	/**
	 * 查询注册学员列表
	 * @param queryInfo
	 * @return
	 */
	List<StudentAssignListInfo> findAssignList(StudentAssignQuery queryInfo);

	/**
	 * 查询学员信息
	 * @param learnId
	 * @return
	 */
	StudentAssignInfo getStudentInfo(String learnId);

	/**
	 * 批量更新学员阶段
	 * @param assignInfo
	 */
	int updateStudentsStage(StudentAssignInfo assignInfo);
    /**
     * 批量分配辅导员
     * @param assignInfo
     */
	int addAssignInfos(StudentAssignInfo assignInfo);
	/**
	 * 更新学员阶段
	 * @param assignInfo
	 */
	int updateStudentStage(StudentAssignInfo assignInfo);
	/**
	 * 分配辅导员
	 * @param assignInfo
	 */
	int addAssignInfo(StudentAssignInfo assignInfo);
	/**
	 * 查询学员信息
	 * @param learnIds
	 * @return
	 */
	List<Map<String, String>> getStudentList(@Param("learnIds") String[] learnIds);
	/**
	 * 查询学员信息
	 * @param learnIds
	 * @return
	 */
	List<Map<String, String>> getStudentListByquery(StudentAssignQuery queryInfo);

}
