package com.yz.dao.enroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.communi.Body;
import com.yz.model.condition.recruit.StudentInfoQuery;
import com.yz.model.enroll.regist.BdStudentRegist;
import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;

public interface BdStdRegisterMapper {

	List<BdStudentRegist> selectRegisterInfoByPage(BdStdEnrollQuery bdStdEnrollQuery);

	int stdRegisterBatchs(@Param("learnIds") List<String> learnIds, @Param("grade") String grade,
			@Param("updateUser") String updateUser, @Param("updateUserId") String updateUserId);

	int stdRegisterBatch(@Param("learnId") String learnId, @Param("grade") String grade,
			@Param("updateUser") String updateUser, @Param("updateUserId") String updateUserId);

	BdStudentRegist selectRegisterInfoById(@Param("learnId") String learnId, @Param("grade") String grade);

	String selectStdStatus(@Param("learnId") String learnId);

	List<BdStudentRegist> selectFirstRegisterInfoByPage(StudentInfoQuery student);

	List<BdStudentRegist> selectSecondRegisterInfoByPage(StudentInfoQuery student);

	List<BdStudentRegist> selectThirdRegisterInfoByPage(StudentInfoQuery student);

	int insertNextRegistInfo(@Param("list") List<Map<String, String>> list);

	int insertFirstRegist(@Param("list") List<Map<String, String>> list);

	String selectSchoolRoll(String learnId);

	String[] selectUnPaidItemCodes(@Param("learnId") String learnId, @Param("grade") String grade);

	ArrayList<String> selectLearnId(@Param("stdName") String stdName, @Param("idCard") String idCard,
			@Param("pfsnName") String pfsnName, @Param("unvsName") String unvsName, @Param("grade") String grade);

	int updateSchoolRollStdNo(@Param("learnIds") ArrayList<String> learnIds, @Param("schoolRoll") String schoolRoll,@Param("stdNo")String stdNo);

	/**
	 * 查询赠送规则校验条件
	 * 
	 * @param learnId
	 * @return
	 */
	Body getMpCondition(String learnId);

	int insertNextRegistInfos(@Param("list") List<Map<String, String>> list);

	/**
	 * 查询赠送规则校验条件
	 * 
	 * @param learnId
	 * @return
	 */
	List<Body> getMpConditions(@Param("learnIds") String[] learnIds);

	String selectRecruitTypeByLearnId(String learnId);

	ArrayList<String> selectLearnIdByCond(@Param("idCard") String idCard, @Param("grade") String grade);

	List<Map<String, String>> selectTmpAddRecord(@Param("learnId") String learnId,
			@Param("updateUser") String updateUser, @Param("updateUserId") String updateUserId);

	List<Map<String, String>> selectNextRegistInsertInfo(@Param("learnId") String learnId, @Param("grade") String grade,
			@Param("updateUser") String updateUser, @Param("updateUserId") String updateUserId);

	List<Map<String, String>> selectNextRegistInfoBatch(@Param("learnIds") List<String> ableLearnIds,
			@Param("grade") String grade, @Param("updateUser") String updateUser,
			@Param("updateUserId") String updateUserId);
}