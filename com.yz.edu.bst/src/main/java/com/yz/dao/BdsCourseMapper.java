package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.course.BdCourse;
import com.yz.model.course.BdCourseResource;

public interface BdsCourseMapper {

	List<BdCourseResource> selectCourseResourceReading(String learnId);

	String selectStdStage(String learnId);

	List<BdCourseResource> selectCourseResourceUnEnroll(String learnId);

	String[] selectTermCpId(@Param("learnId") String learnId, @Param("term") String term);

	Map<String, String> selectCourseDate(@Param("cpIds") String[] cpIds);

	Map<String, String> selectPfsnInfoByLearnId(String learnId);

	List<Map<String, String>> selectCourseNames(@Param("cpIds") String[] cpIds);

	String[] selectCpTypes(@Param("cpIds") String[] cpIds);

	String[] selectCourseAddress(@Param("cpIds") String[] cpIds);

	String[] selectCourseDates(@Param("cpIds") String[] cpIds);

	List<Map<String, String>> selectDetails(@Param("cpIds") String[] cpIds, @Param("date") String date);

	String selectGradeByLearnId(String learnId);

	Map<String, String> selectQingshuInfiByLearnId(String learnId);

	List<Map<String, String>> selectCourse(@Param("courseType") String courseType,
                                           @Param("startTimePre") String startTimePre, @Param("endTimePre") String endTimePre);

	List<String> selectUserIdByCourseId(@Param("courseId") String courseId, @Param("stdStage") String stdStage);
	
	BdCourseResource selectResourceByPrimaryKey(String learnId);
	BdCourse selectByPrimaryKey(String courseId);

	List<Map<String,String>> getUnvsAllCourseLive(@Param("unvsId") String unvsId,@Param("date") String date);

	int selectStdAllLive(@Param("stdId") String stdId);
}
