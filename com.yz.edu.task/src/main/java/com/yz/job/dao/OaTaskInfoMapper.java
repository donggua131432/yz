package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.OaStudentTask;


/**
 * 所有的 教务任务
 * @author lx
 * @date 2017年7月19日 下午3:19:27
 */
public interface OaTaskInfoMapper {

	/**
	 * 所有未完成任务的学员信息
	 * @param taskId
	 * @return
	 */
	public List<OaStudentTask> getTaskUnfinishedStudent(@Param("taskId") String taskId);
	
	/**
	 * 通过学业id获取对应的userId
	 * @param learnId
	 * @return
	 */
	public String getUserIdByLearnId(@Param("learnId") String learnId);
}
