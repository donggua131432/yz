package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.JStudentStudyingQuery;
import com.yz.model.educational.JStudentStudyingExcel;
import com.yz.model.educational.JStudentStudyingGKExcel;
import com.yz.model.educational.JStudentStudyingImportExcel;
import com.yz.model.educational.JStudentStudyingListInfo;

public interface JStudentStudyingMapper {

	/**
	 * 查询学员列表
	 * 
	 * @param queryInfo
	 * @param user
	 * @return
	 */
	List<JStudentStudyingListInfo> getStudyingList(JStudentStudyingQuery queryInfo);

	List<JStudentStudyingExcel> exportStudying(JStudentStudyingQuery query);

	List<JStudentStudyingGKExcel> exportStudyingGK(JStudentStudyingQuery query);

	List<Map<String, Object>> getNonExistsAdmitUnvsNamePsfnName(
			@Param("studentlist") List<JStudentStudyingImportExcel> studentlist);

	List<Map<String, Object>> getNonExistsLearnUnvsNamePsfnName(
			@Param("studentlist") List<JStudentStudyingImportExcel> studentlist);

	List<Map<String, Object>> getNonExistsHomeCampus(
			@Param("studentlist") List<JStudentStudyingImportExcel> studentlist);

	List<Map<String, Object>> getNonExistsRecruitCampus(
			@Param("studentlist") List<JStudentStudyingImportExcel> studentlist);

	Map<String, Object> getUnvsIdPfsnIdByNames(@Param("pfsnName") String pfsnName, @Param("unvsName") String unvsName,
			@Param("grade") String grade, @Param("pfsnLevel") String pfsnLevel);

	Map<String, Object> getRecruitCampusIdByName(@Param("campusName") String campusName);

	Map<String, Object> getHomeCampusIdByName(@Param("campusName") String campusName);

	int insertByExcel(@Param("studentlist") List<JStudentStudyingImportExcel> studentlist, @Param("user") BaseUser user,
			@Param("stdId") String stdId, @Param("empId") String empId, @Param("learnId") String learnId,
			@Param("annexId") String annexId);

	void initTmpWbStudentInfoTable(@Param("studentlist") List<JStudentStudyingImportExcel> list);

	List<Map<String, String>> selectStudentInfoInsert(@Param("user") BaseUser user);

	void insertStudentInfoExcel(@Param("list") List<Map<String, String>> stdList);

	List<Map<String, String>> selectTmpLearnInfoInsert(@Param("user") BaseUser user);

	void insertTmpLearnInfo(@Param("list") List<Map<String, String>> learnList);

	List<Map<String, String>> selectTmpEmpInsert(@Param("user") BaseUser user);

	void insertTmpEmp(@Param("list") List<Map<String, String>> empList);

	List<Map<String, String>> selectTmpLearnAnnex();

	void insertTmpLearnAnnex(@Param("list")List<Map<String, String>> annexList);

}
