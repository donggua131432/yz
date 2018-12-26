package com.yz.dao.stdService;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduateDataQuery;

/**
 * 学员服务 - 毕业资料提交
 * @author lx
 * @date 2018年1月26日 上午11:53:34
 */
public interface StudentGraduateDataMapper
{
	
	public void addStuGraduateData(@Param("list") List<StudentGraduateDataInfo> list);
	
	public void singleAddStuGraduateData(StudentGraduateDataInfo graduateData);
	
	public void aloneDelStuGraduateData(@Param("learnId") String learnId,@Param("taskId") String taskId);
	
	public void delStuGraduateData(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	public List<StudentGraduateDataInfo> queryStuGraduateDataList(@Param("queryInfo") StudentGraduateDataQuery query,@Param("user") BaseUser user);
	
	public StudentGraduateDataInfo getGraduateDataById(String id);
	
	public void editStuGraduateData(StudentGraduateDataInfo info);
	
	List<Map<String, Object>> getNonExistsStudent(@Param("stuGraduateList") List<StudentGraduateDataInfo> stuGraduateList);
	
	public void updateStuGraduateData(StudentGraduateDataInfo info);
}
