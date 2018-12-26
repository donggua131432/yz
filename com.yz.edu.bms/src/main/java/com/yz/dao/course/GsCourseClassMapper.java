package com.yz.dao.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.course.GsCourseClassInfo;

public interface GsCourseClassMapper
{
	public List<GsCourseClassInfo> getGsCourseClassInfo();
	
	public List<Map<String, String>> findAllKeyValue(@Param("sName") String sName);
	
	public GsCourseClassInfo getGsCourseClassDetailInfo(@Param("classId") String classId);
	
	public void updateClass(GsCourseClassInfo classInfo);
	
	public void insertClass(GsCourseClassInfo classInfo);
}
