package com.yz.dao.gk;

import com.yz.model.admin.BaseUser;
import com.yz.model.gk.BdStudentExamGK;
import com.yz.model.gk.BdStudentExamGKExcel;
import com.yz.model.gk.BdStudentExamGKQuery;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface BdStudentExamGKMapper {
	List<Map<String, Object>> findAllStudentExamGK(@Param("query") BdStudentExamGKQuery query,
			@Param("user") BaseUser user);

	void updateJoinStatus(@Param("eigIds") String[] eigIds, @Param("status") String status);

	List<Map<String, Object>> findExamYear();

	List<Map<String, Object>> findExamCourse();

	List<Map<String, Object>> findExamType();

	List<Map<String, Object>> getNonExistsStudent(
			@Param("studentExamGKExcelList") List<BdStudentExamGKExcel> studentExamGKExcelList);

	void insert(@Param("list") List<Map<String, String>> list);

	int deleteStudentExamGK(@Param("eigIds") String[] eigIds);

	void initTmpExamGk(@Param("studentExamGKExcelList") List<BdStudentExamGKExcel> studentExamGKExcelList);

	List<Map<String, String>> selectTmpEaxamGkInsert(@Param("user") BaseUser user);

	List<Map<String, Object>> findExamTime(String date);
}
