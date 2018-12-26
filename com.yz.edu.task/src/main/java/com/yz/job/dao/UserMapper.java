package com.yz.job.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.job.model.UsEnrollLog;
import com.yz.job.model.UsFollow;

/**
 * 
 * @author Administrator
 *
 */
public interface UserMapper {

	
	/**
	 * @desc 根据身份证/电话号码查询招生老师 
	 * @param idCard
	 * @param phone
	 * @return
	 */
	public Map<String,String> getRecruit(@Param(value="idCard")String idCard,@Param(value="mobile")String phone);
	
	
	/**
	 * 根据用户Id查询员工信息
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, String> getEmpInfo(@Param(value = "userId") String userId);
	
	
	/**
	 * 根据用户Id查询员工信息
	 * 
	 * @param userId
	 * @return
	 */
	void saveUsFollow(UsFollow follow);
	

	 /**
	  * @desc 根据推荐人建立跟进记录
	  * @param pId
	  * @param userId
	  */
	public void createUsFollowByPId(@Param(value = "pId")String pId,@Param(value = "userId")String userId);
	
	
	 /**
	  * @desc 添加报名记录 ？？？
	  * @param log 
	  */
	public void saveUsEnrollLog(@Param(value = "log")UsEnrollLog log);
	
	
	 /**
	  * @desc 用户信息和学员信息相互绑定？？？
	  * @param userId 用户ID
	  * @param stdId 学员ID
	  */
	public void bindUserStd(@Param(value = "userId")String userId,@Param(value = "stdId")String stdId);
	
	/**
	 * @desc 根据StdId写入UserId
	 * @param stdId
	 * @param userId
	 */
	public void updateAccountUserIdByStdId(@Param(value = "stdId") String stdId ,@Param(value = "userId") String userId);
	
	/**
	 * 根据员工id获取对应的用户信息
	 * @param empId
	 * @return
	 */
	public Map<String, String> getUserInfoByEmpId(@Param(value = "empId")String empId);
	
	/**
	 * 获取用户的信息
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUserInfoByUserId(@Param(value = "userId")String userId);
	
	/**
	 * 根据邀约人获取招生老师id
	 * @param pId
	 * @return
	 */
	public String getUsFollowByPId(@Param(value = "pId") String pId);
}
