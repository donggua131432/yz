package com.yz.dao.stdService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.stdService.StudentTaskFollowUp;
import com.yz.model.stdService.StudentTaskFollowUpQuery;

public interface StudentTaskFollowUpMapper
{
	public List<StudentTaskFollowUp> getStudentTaskFollowUp(StudentTaskFollowUpQuery query);
	
	public void finishTask(@Param("taskId") String taskId,@Param("learnId") String learnId);
	
	public List<StudentTaskFollowUp> getStudentServiceLog(@Param("learnId") String learnId);
}
