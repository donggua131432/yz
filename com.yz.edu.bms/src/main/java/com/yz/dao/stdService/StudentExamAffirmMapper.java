package com.yz.dao.stdService;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentExamAffirmQuery;

/**
 * 考场确认跟进
 * @author lx
 * @date 2017年12月7日 下午2:14:53
 */
public interface StudentExamAffirmMapper
{
	public void addStuExamAffirm(@Param("list") List<StudentExamAffirmInfo> list);
	
	public void delStuExamAffirm(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	public List<StudentExamAffirmInfo> queryStudentAffirmList(@Param("queryInfo") StudentExamAffirmQuery query,@Param("user") BaseUser user);
	
	public StudentExamAffirmInfo getExamAffirmInfoById(String affirmId);
	
	public List<Map<String, String>> getExamYear(@Param("status") String status);
	
	public List<Map<String, String>> getExamReason(@Param("eyId") String eyId);
	
	public void changeUnconfirmeReason(@Param("info") StudentExamAffirmInfo info,@Param("user") BaseUser user);
	
	public void singleAddStuExamAffirm(StudentExamAffirmInfo affirmInfo);
	
	public void resetResult(@Param("affirmId") String affirmId,@Param("taskId") String taskId,@Param("learnId") String learnId,@Param("user") BaseUser user);
	
	public void aloneDelStuExamAffirm(@Param("learnId") String learnId,@Param("taskId") String taskId);
}
