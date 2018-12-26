package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface BdsTaskMapper
{
	public List<Map<String, String>> getMyTaskInfo(@Param("learnId") String learnId,@Param("taskStatus") String taskStatus);
	
	public void updateTaskStatus(@Param("taskId") String taskId,@Param("learnId") String learnId);

	public List<String> selectAllEmpNoContains(String year);

	public void insertExpenses(@Param("empIds") List<String> empIds, @Param("year")String year);
	
	public Map<String, String> getStudentInfo(@Param("learnId") String learnId);
	
	public List<Map<String, String>> getProvince(@Param("eyId") String eyId);
	
	public List<Map<String, String>> getCity(@Param("eyId") String eyId);
	
	public List<Map<String, String>> getDistrict(@Param("eyId") String eyId);
	
	public List<Map<String, String>> getExamPlace(@Param("provinceCode") String provinceCode,@Param("cityCode") String cityCode,@Param("districtCode") String districtCode,@Param("eyId") String eyId);
	
	public List<Map<String, String>> getPlaceYear(@Param("placeId") String placeId,@Param("eyId") String eyId);
	
	public String getStudentReasonById(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public int taskIfPast(@Param("taskId") String taskId);
	
	public int getExamAffirmCount(@Param("eyId") String eyId,@Param("pyId") String pyId);
	
	public int getExamSeats(@Param("pyId") String pyId);
	
	public void affirmExamInfo(@Param("taskId") String taskId,@Param("eyId") String eyId,@Param("learnId") String learnId,@Param("pyId") String pyId);
	
	public Map<String, String> getExamAffirmResult(@Param("taskId") String taskId,@Param("eyId") String eyId,@Param("learnId") String learnId);
	
	public List<Map<String, String>> getExamYearSubject(@Param("eyId") String eyId);
	
	public String getStudentTestSubject(@Param("pfsnId") String pfsnId,@Param("grade") String grade,@Param("semester") String semester);
	
	public String getPfsnIdByLearnId(@Param("learnId") String learnId);
	
	public Map<String, Object> getStudentForGkExam(@Param("examYear") String examYear,@Param("learnId") String learnId);
	
	public void updateStudentExamGkIsRead(@Param("examYear") String examYear,@Param("learnId") String learnId);
	
	public String getStudentGraduateTemplate(@Param("learnId") String learnId);
	
	public void updateStudentGraduateAddress(Map<String, String> map);
	
	public String getStudentGraduateAddressById(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public int stuIfLookXueXinInfo(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public void stuLookXueXinNet(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public void stuSubmitXueXinInfo(@Param("taskId") String taskId,@Param("learnId") String learnId,@Param("isError") String isError,@Param("feedback") String feedback);
	
	public void stuDegreeEnglishEnroll(@Param("taskId") String taskId,@Param("learnId") String learnId,@Param("isEnroll") String isEnroll,@Param("enrollNo") String enrollNo);
	
	public String getDegreeScoreByLearnId(@Param("learnId") String learnId); 
	
	public Map<String, String> getStuDegreeEnglishInfo(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public List<Map<String, String>> getTemplateUrlByUnvsIdAndPfsnLevel(@Param("unvsId") String unvsId,@Param("pfsnLevel") String pfsnLevel);
	
	public void updateNoticeTaskViewTime(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public void updateSubmitAffirmInfo(@Param("taskId") String taskId,@Param("learnId") String learnId,@Param("isReceiveBook") String isReceiveBook,@Param("isKnowCourseType") String isKnowCourseType,@Param("isKnowTimetables") String isKnowTimetables);

	public Map<String, String> getQingshuInfo(@Param("learnId") String learnId);

	public void updateQingshuConfirmStatus(@Param("learnId") String learnId,@Param("taskId") String taskId,@Param("confirmStatus")String confirmStatus);
	
	/**
	 * 获取国开考场城市信息
	 * @return
	 */
	public List<Map<String, String>> getExamCityGk();
	
	/**
	 * 获取未确认原因
	 * @param learnId
	 * @param taskId
	 * @return
	 */
	public String getUnconfirmedReason(@Param("taskId") String taskId,@Param("learnId")String learnId);
	
	/**
	 * 确认选择城市
	 * @param taskId
	 * @param learnId
	 * @param ecId
	 */
	public void affirmExamCityGKInfo(@Param("taskId") String taskId,@Param("learnId")String learnId,@Param("ecId")String ecId);
	
	/**
	 * 获取国开考试选择城市信息
	 * @param taskId
	 * @param learnId
	 * @return
	 */
	public Map<String, String> getAffirmExamCityGKInfo(@Param("taskId") String taskId,@Param("learnId")String learnId);

	/**
	 * 获取模板信息
	 * @param unvsId
	 * @param pfsnLevel
	 * @param grade
	 * @return
	 */
	public List<Map<String, String>> getTemplateUrlByUnvsIdAndPfsnLevel(@Param("unvsId") String unvsId,@Param("pfsnLevel") String pfsnLevel,@Param("grade") String grade);



	/**
	 * 更新毕业论文任务为已查看和查看时间
	 * @param taskId
	 * @param learnId
	 */
	public void updatePaperTaskViewTime(@Param("taskId") String taskId,@Param("learnId")String learnId);

	/**
	 * 获取毕业论文任务信息
	 * @param taskId
	 * @param learnId
	 * @return
	 */
	Map<String,Object> getStuGraduatePaperTaskInfo(@Param("taskId") String taskId,@Param("learnId")String learnId);
	
	/**
	 * 获取统考设置的信息
	 * @param eyId
	 * @return
	 */
	Map<String, String> getGkUnifiedExamSet(@Param("eyId") String eyId);
	
	/**
	 * 修改提交的国开统考操作
	 * @param taskId
	 * @param learnId
	 * @param operType
	 */
	void updateGkUnifiedExamInfo(@Param("taskId") String taskId,@Param("learnId")String learnId,@Param("operType")String operType);

}
