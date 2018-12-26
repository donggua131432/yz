package com.yz.dao.oa;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.oa.OaEmpLearnInfoPerfInfo;
import com.yz.model.oa.OaEmployeePerfQuery;

/**
 * 分配招生老师绩效
 * @author lx
 * @date 2017年11月8日 上午10:32:43
 */
public interface OaEmployeePerfMapper
{
	
	//所有的待分配学员信息
	public List<OaEmpLearnInfoPerfInfo> getOaEmpLearnInfoPerfInfo(OaEmployeePerfQuery perfQuery);
	
	//部分归属到原部门
	public void partChangeToOld(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("userName") String userName ,@Param("userId") String userId,
			@Param("empId") String empId,@Param("dpId") String dpId,@Param("campusId") String campusId);
	
	//部分归属到新部门
	public void partChangeToNew(@Param("list") List<OaEmpLearnInfoPerfInfo> list,
			@Param("userName") String userName ,@Param("userId") String userId,@Param("empId") String empId,
			@Param("dpId") String dpId,@Param("campusId") String campusId);
	//全部归属到原部门
	public void allChangeToOld(@Param("modifyId") String modifyId,@Param("userName") String userName ,@Param("userId") String userId,
			@Param("empId") String empId,@Param("dpId") String dpId,@Param("campusId") String campusId);
	
	//全部归属到原部门
	public void allChangeToNew(@Param("modifyId") String modifyId,@Param("userName") String userName ,@Param("userId") String userId,@Param("empId") String empId,
			@Param("dpId") String dpId,@Param("campusId") String campusId);
	
	//学员学业对应的用户信息(部分)
	public List<String> getUserIdByLearnId(@Param("list") List<OaEmpLearnInfoPerfInfo> list);
	
	//改变招生关系(部分)
	public void updateBdLearnRules(@Param("list") List<OaEmpLearnInfoPerfInfo> list,@Param("dpId") String dpId,@Param("campusId") String campusId);
	
	//改变招生关系(全部)
	public void updateAllBdLearnRules(@Param("empId") String empId,@Param("dpId") String dpId
			,@Param("campusId") String campusId,@Param("oldDpId") String oldDpId);
	
	///学员学业对应的用户信息(全部)
	public List<String> getAllUserIdByLearnId(@Param("empId") String empId,@Param("dpId") String dpId);
}
