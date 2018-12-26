package com.yz.dao;

import com.yz.model.paper.StudentAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentGraduatePaperMapper {

    void insertAttachment(StudentAttachment attachment);

    List<StudentAttachment> selectUserStudentAttachment(String learnId);

    Map<String,String> getLearnInfo(String learnId);

    void updatePaperTitle(@Param("learnId") String learnId, @Param("title") String title);

    Map<String,String> getPaperInfoByLearnId(String learnId);
}
