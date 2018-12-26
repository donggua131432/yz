package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdCourseResource;

public interface BdCourseResourceMapper {
    int deleteByPrimaryKey(String resourceId);

    int insert(BdCourseResource record);

    int insertSelective(BdCourseResource record);

    BdCourseResource selectByPrimaryKey(String resourceId);

    int updateByPrimaryKeySelective(BdCourseResource record);

    int updateByPrimaryKey(BdCourseResource record);

	List<BdCourseResource> getCourseResource(String courseId);

	void deleteByCourseId(String courseId);

	int deleteBatch(@Param("delResourceIds") String[] delResourceIds);

	List<Map<String,Object>> getCourseByPfsnId(@Param("pfsnId") String pfsnId, @Param("semester") String semester);

	Map<String,Object> getTeacherInfo(String courseId);
}