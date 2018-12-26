package com.yz.dao.educational;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdCourseTextbookKey;

public interface BdCourseTextbookMapper {
    int deleteByPrimaryKey(String courseId);

    int insert(BdCourseTextbookKey record);

    int insertSelective(BdCourseTextbookKey record);

	int insertBatch(@Param("textBooks") String[] textBooks, @Param("courseId") String courseId);

}