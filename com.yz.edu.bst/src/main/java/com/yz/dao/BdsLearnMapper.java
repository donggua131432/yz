package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdLearnRemark;
import com.yz.model.communi.Body;

public interface BdsLearnMapper {

	Map<String, Object> selectStudentInfo(String stdId);
	/**
	 * 通过学员Id得到学员信息及报读信息
	 * @param stdId
	 * @return
	 */
	Map<String, Object> selectStudentInfoByStdId(String stdId);
	
	List<Map<String, Object>> selectLearnInfo(String stdId);

	int selectPaidCount(String learnId);

	int selectUnpaidCount(String leanrId);
	/**
	 * （报读检测）- 查询学员报读信息
	 * @param stdId
	 * @return
	 */
	List<Map<String, String>> getLearnList(String stdId);
	/**
	 * （报读检测）查询该学员是否在白名单之内
	 * @param stdId
	 * @param scholarship
	 * @return
	 */
	int countWhiteListBy(@Param("stdId") String stdId, @Param("scholarship") String scholarship);
	/**
	 * 根据证件查询学员信息
	 * @param certNo
	 * @param certType
	 * @return
	 */
	Map<String, String> getStudentInfo(@Param("certNo") String certNo, @Param("certType") String certType);
	/**
	 * 根据学员ID查询学员信息
	 * @param certNo
	 * @param certType
	 * @return
	 */
	Map<String, Object> getStudentInfoById(String stdId);
	/**
	 * 新增学员信息
	 * @param body
	 */
	int addStudentInfo(Body body);
	/**
	 * 新增学员附属信息
	 * @param body
	 */
	int addStudentOther(Body body);
	/**
	 * 新增学员附件信息
	 * @param annexInfos
	 * @return
	 */
	int addStudentAnnex(List<Map<String, String>> annexInfos);
	/**
	 * 查询学员缴费信息
	 * @param pfsnId
	 * @param taId
	 * @param scholarship
	 * @return
	 */
	Map<String, String> selectFeeInfo(@Param("pfsnId") String pfsnId, @Param("taId") String taId, @Param("scholarship") String scholarship);
	/**
	 * 新增学员学业信息
	 * @param body
	 * @return
	 */
	int addStudentLearnInfo(Body body);
	/**
	 * 新增学员原学历信息
	 * @param body
	 * @return
	 */
	int addStudentHistory(Body body);
	/**
	 * 新增学员报读信息
	 * @param body
	 * @return
	 */
	int addStudentEnroll(Body body);

	/**
	 * 根据年级查询学员学业
	 * @param string
	 * @param object
	 * @return
	 */
	int countGrade(@Param("stdId") String string, @Param("grade") String grade);

	/**
	 * 插入学员备注
	 * @param remarkList
	 */
	int insertLearnRemarks(@Param("remarkList") List<BdLearnRemark> remarkList);

	/**
	 * 插入考前审核记录
	 * @param record
	 * @return
	 */
	int insertCheckRecord(Map<String, String> record);

	/**
	 * 获取员工信息
	 * @param recruitId
	 * @return
	 */
	Map<String, String> selectEmpInfo(String recruitId);

	/**
	 * 插入学员数据权限信息
	 * @param empInfo
	 * @return
	 */
	int insertLearnRule(Map<String, String> empInfo);
	
	/**
	 * 查找招生、班主任用户ID
	 * @param learnId
	 * @return
	 */
	Map<String, String> selectTutorAndRecruitUserId(String learnId);


	String selectUserIdByLearnId(String learnId);
	
}
