package com.yz.dao.educational;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdPlanCourseKey;

public interface BdPlanCourseMapper {
    int deleteByPrimaryKey(BdPlanCourseKey key);
    
    int deleteByCourseId(String courseId);
    
    int insert(BdPlanCourseKey record);

    int insertSelective(BdPlanCourseKey record);

	void insertBatch(@Param("teachPlan") String[] teachPlan, @Param("courseId") String courseId);

}