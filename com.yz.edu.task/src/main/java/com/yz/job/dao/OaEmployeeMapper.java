package com.yz.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.OaEmployeeJobInfo;
import com.yz.job.model.OaEmployeeLearnInfo;
import com.yz.job.model.OaEmployeeModifyInfo;

public interface OaEmployeeMapper {

	/**
	 * 某个员工的职位信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeJobInfo getEmployeeJobInfo(String empId);

	/**
	 * 员工所有的职称
	 * 
	 * @param empId
	 * @return
	 */
	List<Map<String, String>> findEmpTitle(String empId);

	/**
	 * 招生老师最新的一条变更记录id
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeModifyInfo getEmpModifyInfoId(@Param("empId") String empId);

	/**
	 * 老的部门总共的学员
	 * 
	 * @param dpId
	 * @return
	 */
	public int totalLearnCount(@Param("dpId") String dpId, @Param("empId") String empId);

	/**
	 * 老的部门已分配的学员
	 * 
	 * @param dpId
	 * @return
	 */
	public int finishLearnCount(@Param("dpId") String dpId, @Param("empId") String empId);

	/**
	 * 获取上一条变更记录的截止时间
	 * 
	 * @param empId
	 */
	public String getEmpModifyEndTime(@Param("modifyId") String modifyId);

	/**
	 * 某个招生老师,某个部门 在某个时间段的学员信息
	 * 
	 * @param recruit
	 * @param dpId
	 * @return
	 */
	public List<OaEmployeeLearnInfo> getEmpLearnInfos(@Param("recruit") String recruit, @Param("dpId") String dpId);

	/**
	 * 插入招生老师学员记录
	 */
	public void insertEmpLearnInfos(List<OaEmployeeLearnInfo> learnInfos);

	/**
	 * 删除员工职称
	 * 
	 * @param dpId
	 */
	public void deleteEmpTitle(String empId);

	/**
	 * 新增员工职称
	 * 
	 * @param empId
	 * @param jdIds
	 */
	public void insertEmpJdIds(@Param("empId") String empId, @Param("ids") String[] jdIds);

	/**
	 * 修改职位信息
	 * 
	 * @param jobInfo
	 */
	public void empJobUpdate(OaEmployeeJobInfo jobInfo);

	/**
	 * 清楚跟进关系
	 * 
	 * @param empId
	 * @param empStatus
	 * @return
	 */
	int clearFollow(@Param("empId") String empId, @Param("empStatus") String empStatus);

	/**
	 * 根据生效时间查找变更记录
	 * 
	 * @param effectTime
	 * @param string
	 * @return
	 */
	public String selectModifyIdByEffectTime(@Param("empId") String empId, @Param("effectTime") String effectTime);

	/**
	 * 修改变更记录状态
	 * 
	 * @param mofidyId
	 * @param string
	 */
	public void updateModifyStatus(@Param("modifyId") String modifyId, @Param("status") String status);
	
	
	/**
	 * 置空员工的userId
	 */
	void updateEmpUserIdByEmpId(@Param("empId") String empId);
	
	
	/**
	 * 根据userId改变用户的身份
	 * @param userId
	 */
	void updateUserRelationByUserId(@Param("userId") String userId);

}
