package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.BdTeacherRecommendQuery;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeacherTemplateExcel {

    private String excelPath;

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public TeacherTemplateExcel() {
        setExcelPath("static/excel/teachRecommendTemplate.xls");
    }

    public String excelRender(List<Map<String, Object>> listStudent, BdTeacherRecommendQuery recommendQuery, String text) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Map<String, Object> map : listStudent) {
            sb.append(getRowDetail(map, i));
            i++;
        }
        text = text.replace("$rowdetail", sb.toString());
        text = text.replace("$year", recommendQuery.getYear());
        text = text.replace("$grade", recommendQuery.getGrade());
        text = text.replace("$semester", recommendQuery.getSemester());
        text = text.replace("$unvsName", recommendQuery.getUnvsName());
        return text;
    }

    private String getRowDetail(Map<String, Object> map, int i) {
        StringBuilder sb = new StringBuilder();
        String proInfo = String.valueOf(map.get("grade")) + "级" + String.valueOf(map.get("pfsn_level")) + String.valueOf(map.get("pfsn_name"));
        sb.append(" <Row ss:Height=\"40.5\">\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"Number\">" + i + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + map.get("emp_name") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + map.get("sex") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + (map.get("birthday") == null ? "" : map.get("birthday")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"Number\">" + (map.get("age") == null ? "" : map.get("age")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("teachEducation") == null ? "" : map.get("teachEducation")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("degree") == null ? "" : map.get("degree")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("finish_school") == null ? "" : map.get("finish_school")) + "   &#10;</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("finish_major") == null ? "" : map.get("finish_major")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + (map.get("finish_time") == null ? "" : map.get("finish_time")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("job_title") == null ? "" : map.get("job_title")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">" + (map.get("professional_time") == null ? "" : map.get("professional_time")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("teach_cert_type") == null ? "" : map.get("teach_cert_type")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s73\"><Data ss:Type=\"String\">" + proInfo + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">" + (map.get("course_name") == null ? "" : map.get("course_name")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">" + (map.get("campus_name") == null ? "" : map.get("campus_name")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">" + (map.get("address") == null ? "" : map.get("address")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">" + (map.get("plan_date") == null ? "" : String.valueOf(map.get("plan_date")).replace(",", "&#10;")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"Number\">" + (map.get("mobile") == null ? "" : map.get("mobile")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">" + (map.get("work_place") == null ? "" : map.get("work_place")) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"/>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    /**
     * 专业名称替换
     *
     * @param pfsnName
     * @return
     */
    public String pfsnNameConvert(String pfsnName) {
        Pattern r = Pattern.compile("(\\[([^\\]]*)\\])|(\\(([^\\)]*)\\))");
        Matcher m = r.matcher(pfsnName);
        return m.replaceAll("");
    }

    /**
     * 专业层次转换
     *
     * @param pfsnLevel
     * @return
     */
    public String pfsnLevelConvertSimple(String pfsnLevel) {
        if (pfsnLevel.equals("1")) {
            return "本科";
        } else if (pfsnLevel.equals("5")) {
            return "专科";
        }
        return "";
    }

}
