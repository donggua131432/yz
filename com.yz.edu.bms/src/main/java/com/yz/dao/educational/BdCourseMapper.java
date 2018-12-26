package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.baseinfo.BdCourse;
import com.yz.model.baseinfo.BdCourseMap;
import com.yz.model.common.PfsnSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.course.CourseExport;
import com.yz.model.educational.BdTimeTableQuery;
import com.yz.model.educational.CourseExcel;

public interface BdCourseMapper {

	int insertSelective(BdCourse record);

	BdCourse selectByPrimaryKey(String courseId);

	int updateByPrimaryKeySelective(BdCourse record);

	List<Map<String, Object>> selectAllCourse(BdCourse course);
	
	int getSelectAllCourseCount(BdCourse course);

	List<BdCourse> selectAll(BdCourse course);

	int stdtCount(@Param("thpIds") String[] thpIds);
	
	int stdFdCount (@Param("courseId") String courseId); 
	
	int stdXkCountByThpId(@Param("thpId") String thpId);

	String[] getThpIds(@Param("courseId") String courseId,@Param("course") BdCourse course);

	List<BdCourse> findCourseName(@Param("sName") String sName, @Param("courseType") String courseType);

	String[] selectCpIds(BdTimeTableQuery query);
	
	String[] selectFDTermCpId(BdTimeTableQuery query);

	Map<String, String> selectCourseDate(@Param("cpIds") String[] cpIds);

	Map<String, String> selectPfsnInfo(String pfsnId);

	List<Map<String, String>> selectCourseNames(@Param("cpIds") String[] cpIds);

	List<String> selectCpTypes(@Param("cpIds") String[] cpIds);

	List<String> selectCourseAddress(@Param("cpIds") String[] cpIds);

	List<String> selectCourseDates(@Param("cpIds") String[] cpIds);

	List<String> selectAmCourse(@Param("date") String date, @Param("cpIds") String[] cpIds,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	int selectStdCountByPfsnId(String pfsnId);
	/**
	 * 导入课程 判断课程是否存在
	 * @param courseExcel
	 * @return
	 */
	BdCourse selectBy(CourseExcel courseExcel);

	/**
	 * 查询专业信息
	 * 
	 * @param sqInfo
	 * @return
	 */
	List<PfsnSelectInfo> getPfsnSelectList(SelectQueryInfo sqInfo);

	List<Map<String, Object>> findExportCourseXK(CourseExport course);

	List<Map<String, Object>> findExportCourseFD(CourseExport course);
	
	/**
	 * 同一门课程不能绑定相同年级、院校、层次、专业的2条教学计划
	 */
	List<Map<String, Object>> findRepeatTeachPlan(@Param("courseId") String courseId,@Param("thpIds") String[] thpIds);
	
	List<BdCourseMap> selectAllToExport(BdCourse course);
	/**
	 * Excel导入查找不存在教材计划
	 * @param classplanList
	 * @return
	 */
	List<Map<String, Object>>  getNonExistsTeachPlan(@Param("courseList")List<CourseExcel> courseList);
	/**
	 * Excel导入查找不存在教材名称
	 * @param classplanList
	 * @return
	 */
	List<Map<String, Object>>  getNonExistsTextBook(@Param("courseList")List<CourseExcel> courseList);
	
	int insertByExcel(@Param("courseList")List<CourseExcel> courseList,@Param("user") BaseUser user);

	void deleteCourse(@Param("ids") String[] ids);
	
	void updeteRecourceStatus(@Param("resourceId") String resourceId,@Param("status") String status);
}