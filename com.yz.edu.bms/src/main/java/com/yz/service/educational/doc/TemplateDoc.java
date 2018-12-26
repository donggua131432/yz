package com.yz.service.educational.doc;

import com.yz.model.educational.TemplateDocQuery;

import java.util.Map;

public class TemplateDoc extends BaseDoc {

    public TemplateDoc() {
        super.setDocPath("static/doc/courseTemplate.doc");
    }

    public String docRender(Map<String, Object> courseScore, TemplateDocQuery docQuery, String text) {
        text = text.replace("$courseName", docQuery.getThpName());
        text = text.replace("$year", docQuery.getYear());
        text = text.replace("$grade", docQuery.getGrade());
        text = text.replace("$semester", docQuery.getSemester());
        text = text.replace("$unvsName", docQuery.getUnvsName());
        text = text.replace("$pfsnName", pfsnNameConvert(docQuery.getPfsnName()));
        text = text.replace("$pfsnLevel", pfsnLevelConvertSimple(docQuery.getPfsnLevel()));
        text = text.replace("$avgScore", courseScore.get("avg_score") == null ? "" : String.valueOf(courseScore.get("avg_score")));
        text = text.replace("$maxScore", courseScore.get("max_score") == null ? "" : String.valueOf(courseScore.get("max_score")));
        text = text.replace("$minScore", courseScore.get("min_score") == null ? "" : String.valueOf(courseScore.get("min_score")));
        int count1 = courseScore.get("count1") == null ? 0 : Integer.valueOf(courseScore.get("count1").toString());
        int count2 = courseScore.get("count2") == null ? 0 : Integer.valueOf(courseScore.get("count2").toString());
        int count = courseScore.get("count") == null ? 0 : Integer.valueOf(courseScore.get("count").toString());
        text = text.replace("$count1", courseScore.get("count1") == null ? "" : String.valueOf(courseScore.get("count1")));
        text = text.replace("$count2", courseScore.get("count1") == null ? "" : String.valueOf(courseScore.get("count2")));
        text = text.replace("$percent1", count == 0 ? "" : doubleFormat(((double) count1 / count) * 100));
        text = text.replace("$percent2", count == 0 ? "" : doubleFormat(((double) count2 / count) * 100));
        text = text.replace("$hours", courseScore.get("total_hours") == null ? "" : String.valueOf(courseScore.get("total_hours")));
        text = text.replace("$size", courseScore.get("count") == null ? "" : String.valueOf(courseScore.get("count")));
        text = text.replace("$empName", courseScore.get("emp_name") == null ? "" : String.valueOf(courseScore.get("emp_name")));
        text = text.replace("$examTime", courseScore.get("exam_time") == null ? "" : String.valueOf(courseScore.get("exam_time")));
        return text;
    }

}
