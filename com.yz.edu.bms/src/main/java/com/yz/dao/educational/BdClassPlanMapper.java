package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdClassPlan;
import com.yz.model.educational.BdStudentTScoreExcel;
import com.yz.model.educational.MakeSchedule;

public interface BdClassPlanMapper {
	int deleteByPrimaryKey(String cpId);

	int insert(BdClassPlan record);

	int insertSelective(BdClassPlan record);

	BdClassPlan selectByPrimaryKey(String cpId);

	int updateByPrimaryKeySelective(BdClassPlan record);

	int updateByPrimaryKey(BdClassPlan record);

	void updateEmpIdMoney(@Param("empId") String empId, @Param("money") String money);

	List<Map<String, Object>> findAllClassPlan(BdClassPlan classPlan);
	
	List<Map<String, Object>>  findClassPlanByIds(@Param("ids") String[] ids);

	List<Map<String, Object>> findProfessional(@Param("courseId") String courseId, @Param("pfsnName") String pfsnName);

	Map<String, Object> findClassPlanById(String cpId);

	void deleteClassPlan(@Param("ids") String[] ids);

	void distributionTeacher(@Param("placeId") String placeId,@Param("empId") String empId, @Param("ids") String[] ids);

	Map<String, Object> findUnvsPfsn(MakeSchedule makeSchedule);

	List<Map<String, Object>> findMakeSchedule(MakeSchedule makeSchedule);

	String getTphId(MakeSchedule makeSchedule);

	String selectLearnIdBySchoolRoll(@Param("stdName") String stdName, @Param("schoolRoll") String schoolRoll);

	int updateQingshuInfo(@Param("learnId") String learnId, @Param("qingshuId") String qingshuId,
			@Param("qingshuPwd") String qingshuPwd);
	
	/**
	 * excel导入批量查询不存在的课程信息
	 * @param studentExamGKExcelList
	 * @return
	 */
	List<Map<String, Object>> getNonExistsCourse(@Param("classplanList")List<BdClassPlan> classplanList);
	
	/**
	 * excel导入批量查询不存在的分校信息
	 * @param studentExamGKExcelList
	 * @return
	 */
	List<Map<String, Object>> getNonExistsCampus(@Param("classplanList")List<BdClassPlan> classplanList);
	
	/**
	 * excel导入批量查询不存在的授课老师信息
	 * @param studentExamGKExcelList
	 * @return
	 */
	List<Map<String, Object>> getNonExistsEmployee(@Param("classplanList")List<BdClassPlan> classplanList);
	
	/**
	 * 导入excel
	 * @param list
	 * @param user
	 * @return
	 */
	int insertByExcel(@Param("classplanList")List<Map<String, String>> list);

	List<Map<String, String>> selectTmpClassPlan(BaseUser user);

	void initTmpExcelTable(@Param("classplanList")List<BdClassPlan> classplanList);

}