package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface BdsUserMapper {
	/**
	 * 员工所有的职称
	 * @param empId
	 * @return
	 */
	List<Map<String, String>> findEmpTitle(String empId);
	
	
	/**
	 * 根据姓名或手机精准查询教师信息
	 * @param empName
	 * @param mobile
	 * @return
	 */
	List<Map<String, Object>> getEmpInfoByNameOrMobile(String keywords);
	
	
	/**
	 * 查询员工信息
	 * @param empId
	 * @return
	 */
	Map<String, String> getEmpInfo(String empId);
	/**
	 * 根据手机号查询学员数量
	 * @param mobile
	 * @return
	 */
	int countByMobile(String mobile);
	/**
	 * 根据手机号查询最近一条报读记录并且招生老师在职的
	 * @param mobile
	 * @return
	 */
	Map<String, String> getCloseAndOnJob(String mobile);
	/**
	 * 根据身份证查询最近一条报读记录并且招生老师在职的
	 * @param mobile
	 * @return
	 */
	Map<String, String> getCloseAndOnJobByIdCard(String idCard);
	/**
	 * 根据身份证查询学员ID
	 * @param idCard
	 * @return
	 */
	String getStdIdByIdCard(String idCard);
	/**
	 * 根据手机号码查询学员ID
	 * @param mobile
	 * @return
	 */
	String getStdIdByMobile(String mobile);
	
	/**
	 * 绑定员工和用户的关系
	 * @param userId
	 * @param empId
	 */
	void bindEmp(@Param("userId") String userId,@Param("empId") String empId);

}
