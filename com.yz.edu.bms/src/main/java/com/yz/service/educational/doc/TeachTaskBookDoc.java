package com.yz.service.educational.doc;

import com.yz.model.educational.TeachTaskBookQuery;
import com.yz.util.DateUtil;

import java.util.Map;

public class TeachTaskBookDoc extends BaseDoc {

    public TeachTaskBookDoc() {
        super.setDocPath("static/doc/teachTaskBookTemplate.doc");
    }

    public String docRender(Map<String, Object> courseScore, TeachTaskBookQuery bookQuery, String text) {
        text = text.replace("$courseName", bookQuery.getCourseName());
        text = text.replace("$year", bookQuery.getYear());
        text = text.replace("$grade", bookQuery.getGrade());
        text = text.replace("$semester", bookQuery.getSemester());
        text = text.replace("$unvsName", bookQuery.getUnvsName());
        text = text.replace("$pfsnName", pfsnNameConvert(bookQuery.getPfsnName()));
        text = text.replace("$pfsnLevel", pfsnLevelConvertSimple(bookQuery.getPfsnLevel()));
        text = text.replace("$empName", courseScore.get("emp_name") == null ? "" : String.valueOf(courseScore.get("emp_name")));
        text = text.replace("$sex", courseScore.get("sex") == null ? "" : String.valueOf(courseScore.get("sex")));
        text = text.replace("$jobTitle", courseScore.get("job_title") == null ? "" : String.valueOf(courseScore.get("job_title")));
        text = text.replace("$yyyy", DateUtil.getCurrentDate("yyyy"));
        text = text.replace("$M", DateUtil.getCurrentDate("M"));
        text = text.replace("$dd", DateUtil.getCurrentDate("dd"));
        return text;
    }


}
