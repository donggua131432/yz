package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaStudentTask;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStatisticsInfo;
import com.yz.model.educational.OaTaskStudentInfo;

/**
 * 所有的 教务任务
 * @author lx
 * @date 2017年7月19日 下午3:19:27
 */
public interface OaTaskInfoMapper {

	//所有的教务任务
	public List<OaTaskInfo> selectOaTaskInfo(OaTaskInfoQuery infoQuery);
	
	public OaTaskInfo getTaskInfoById(@Param("taskId") String taskId);
	
	public void insertTask(OaTaskInfo taskInfo);
	
	public void updateTask(OaTaskInfo taskInfo);
	
	public List<OaTaskStudentInfo> queryOaTaskStudentInfo(OaTaskTargetStudentQuery studentQuery);
	
	public void addStu(@Param("list") List<OaTaskStudentInfo> list,@Param("taskId") String taskId);
	
	public void delStu(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	public OaTaskStudentInfo getOaTaskStudentInfoByIdCard(@Param("idCard") String idCard,@Param("grade") String grade);
	
	public void batchImport(@Param("strs") List<String> strs,@Param("taskId") String taskId);
	
	public List<OaTaskStatisticsInfo> getOaTaskStatisticsInfo(@Param("taskId") String taskId);
	
	public List<OaStudentTask> getOaStudentTask(@Param("taskId") String taskId);
	
	public void updateStudentTaskIsNotify(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public String getUserIdByLearnId(@Param("learnId") String learnId);
	
	public List<OaStudentTask> getTaskUnfinishedStudent(@Param("taskId") String taskId);
	
	public List<OaStudentTask> getTaskUnfinishedStudentReport(@Param("taskId") String taskId);
	
	public void singleImport(@Param("learnId") String learnId,@Param("taskId") String taskId,@Param("tutorId") String tutorId);
	
	List<OaStudentTask> getOaStudentTaskForPage(@Param("start") int _start, @Param("size") int _size,@Param("taskId") String taskId);
	
	List<OaStudentTask> getOaStudentTaskForUnfinished(@Param("start") int _start, @Param("size") int _size,@Param("taskId") String taskId);
	
	public int getCountByLearnIdAndTaskId(@Param("learnId") String learnId,@Param("taskId") String taskId);
	
	public int affirmCount(@Param("taskId") String taskId,@Param("eyId") String eyId);
	
	List<Map<String, String>> findAllTaskInfo(@Param("sName")String sName);
	
	public void aloneDelStu(@Param("learnId") String learnId,@Param("taskId") String taskId);
	
	public void singleAddStu(OaTaskStudentInfo studentInfo);
	
	public List<Map<String, String>> getGKExamYear();

	void deleteTask(@Param("taskIds")String[] taskIds);
	
	
	List<Map<String, String>> findGraduateDataTaskInfo(@Param("sName")String sName);

	List<Map<String, String>> findTaskInfoByType(@Param("sName")String sName,@Param("taskType") String taskType);
	
	/**
	 * 国开考试城市确认考试年度
	 * @param status
	 * @return
	 */
	List<Map<String, String>> getExamYearForGK(@Param("status")String status);
	
	/**
	 * 国开统考设置
	 * @param status
	 * @return
	 */
	List<Map<String, String>> getGkUnifiedExam(@Param("status")String status);
	
}
