package com.yz.dao.stdService;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.educational.MakeSchedule;

public interface StudentClassPlanMapper {
	
	/**
	 * 按上课时间段得到课程安排列表
	 * @param makeSchedule
	 * @return
	 */
	List<Map<String, Object>> findClassPlan(@Param("queryinfo") MakeSchedule queryinfo,@Param("user")BaseUser user);
	
	/**
	 * 得到学员数量
	 * @param thpIds
	 * @return
	 */
	int stdtCount(@Param("courseId") String courseId,@Param("user")BaseUser user);
	
	/**
	 * 得到课程专业信息
	 * @param courseId
	 * @return
	 */
	List<Map<String, Object>> findUnvsPfsn(@Param("courseId")String courseId);
	
	/**
	 * 得到课程专业学员数量
	 * @param thpIds
	 * @return
	 */
	int stdtWithPfsnCount(@Param("pfsnId") String pfsnId,@Param("grade") String grade,@Param("user")BaseUser user);
	
	/**
	 * 查看上课学员
	 * @param pfsnId
	 * @param unvsId
	 * @param grade
	 * @return
	 */
	List<Map<String, Object>> findStudentClassPlan(@Param("pfsnId")String pfsnId,@Param("unvsId")String unvsId,@Param("grade")String grade,@Param("user")BaseUser user);
}
