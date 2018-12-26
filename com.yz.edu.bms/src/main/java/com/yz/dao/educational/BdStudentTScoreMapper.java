package com.yz.dao.educational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.educational.*;
import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;

public interface BdStudentTScoreMapper {
	
	List<BdStudentTScore> findStudentScore(@Param("learnId") String learnId);
	
	/**
	 * 
	 */
	
	HashMap<String, String> getTeacherByCourseId(@Param("courseId") String courseId);
	/**
	 * 用于期末成绩编辑页面得到学员学业信息
	 * @param learnId
	 * @return
	 */
	Map<String, Object> getBdStudentTScoreLearnInfo(@Param("learnId") String learnId);
	/**
	 * 获取学员考试成绩
	 * @param learnId
	 * @return
	 */
	List<BdStudentTScore> findStudentTScoreBySemester(@Param("learnId") String learnId,@Param("semester") String semester);
	
	/**
	 * 根据学业编号，课程编号，学期查询唯一记录
	 * @param learnId
	 * @param courseId
	 * @param semester
	 * @return
	 */
	BdStudentTScore findStudentScoreByUnionKey(@Param("learnId") String learnId,@Param("courseId") String courseId,@Param("semester") String semester);
	
	/**
	 * 查询在读学员期未考试成绩
	 * @param queryInfo
	 * @param user
	 * @return
	 */
	List<Map<String, Object>> findAllStudentTScorePage(@Param("queryInfo") BdStudentTScoreMap queryInfo, @Param("user") BaseUser user);
	
	
	/**
	 * 查询在读学员期未考试成绩——用于导出的excel结果
	 * @param queryInfo
	 * @param user
	 * @return
	 */
	List<BdStudentTScoreExcel> findAllStudentTScoreList(@Param("queryInfo") BdStudentTScoreMap queryInfo);
	
	/**
	 * 查询在读学员期未考试成绩——用于勾选导出的excel结果
	 * @param queryInfo
	 * @return
	 */
	List<BdStudentTScoreExcel> findAllStudentTScoreListByLearnIds(@Param("learnids") String[] learnids);
	
	/**
	 * 根据身份证或学号查询学员学业信息
	 * @param scores
	 * @return
	 */
	List<HashMap <String, String>> selectLearninfoByIdCardOrSchoolRoll(BdStudentTScoreExcel scores);
	
	/**
	 * 根据在读院校、专业层次、在读专业来确定具体的学业信息
	 * @param scores
	 * @return
	 */
	HashMap <String, String> selectLearninfoByUnvsId(BdStudentTScoreExcel scores);
	
	/**
	 * excel导入批量查询不存在的学员
	 * @param studentExamGKExcelList
	 * @return
	 */
	List<Map<String, Object>> getNonExistsStudent(@Param("studentExcelList")List<BdStudentTScoreExcel> studentExcelList,@Param("type") String type);
	
	/**
	 * excel导入批量查询不存在的课程信息
	 * @param studentExamGKExcelList
	 * @return
	 */
	List<Map<String, Object>> getNonExistsCourse(@Param("studentExcelList")List<BdStudentTScoreExcel> studentExcelList);
	
	/**
	 * 批量插入
	 * @param studentExcelList
	 * @return
	 */
	int insertByExcel(@Param("studentExcelList")List<Map<String, String>> studentExcelList);
	
	int deleteByPrimaryKey(String tscoreId);

    int insert(BdStudentTScore record);

    int insertSelective(BdStudentTScore record);

    BdStudentTScore selectByPrimaryKey(String tscoreId);

    int updateByPrimaryKeySelective(BdStudentTScore record);

    int updateByPrimaryKey(BdStudentTScore record);
    
	int insertStudentTScore(BdStudentTScoreReceive scores);

	List<Map<String, Object>> getStudentInfo(@Param("grade") String grade, @Param("unvsId") String unvsId, @Param("pfsnId") String pfsnId);

	List<Map<String, Object>> getCourseName(@Param("grade") String grade, @Param("semester") String semester, @Param("pfsnId") String pfsnId);

	int insertScoreExcel(@Param("studentScoreExcels") List<BdStudentScoreExcel> studentScoreExcels);

	int insertTemplateExcel(BdStudentTemplateExcel bdStudentTemplateExcel);

	String getTemplateExcelUrl(TemplateExcel templateExcel);

	Map<String, Object> getCourseScore(@Param("unvsId") String unvsId, @Param("thpId") String thpId);

	void initTmpExcelTable(@Param("studentExcelList")List<BdStudentTScoreExcel> list);

	List<Map<String, String>> selectTmpStudentTScore(@Param("user")BaseUser user);
}
