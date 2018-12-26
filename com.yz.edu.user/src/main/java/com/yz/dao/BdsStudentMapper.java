package com.yz.dao;

import java.util.List;
import java.util.Map;

import com.yz.model.student.BdCheckRecord;
import com.yz.model.student.BdStudentModify;
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


	Map<String, String> getStudentByMobile(@Param("mobile") String mobile);

	Map<String, String> getStudentModify(@Param("learnId") String learnId);

	Map<String, String> getStudentInfo(@Param("learnId") String learnId);

	void addStudentModify(BdStudentModify studentModify);

	List<Map<String, String>> getCheckWeight(String stdStage);

	int addBdCheckRecord(BdCheckRecord record);

	int countYMStudent(@Param("learnId") String learnId);
	
	/**
	 * 更新学员和用户的关系
	 * @param userId
	 * @param stdId
	 */
	void updateUserIdByStdId(@Param("userId") String userId,@Param("stdId") String stdId);
	
	/**
	 * 根据学员stdId获取对应绑定的userId
	 * @param stdId
	 * @return
	 */
	String getUserIdByStdId(String stdId);
	/**
	 * 修改学员的手机号码
	 * @param mobile
	 * @param stdId
	 */
	void updateStdMobileByStdId(@Param("mobile")String mobile,@Param("stdId") String stdId);
}
