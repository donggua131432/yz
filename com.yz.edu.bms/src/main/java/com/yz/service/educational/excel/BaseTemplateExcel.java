package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseTemplateExcel {


    public abstract String excelRender(List<Map<String, Object>> listStudent ,List<Map<String, Object>> listCourse ,TemplateExcel excel, String text);

    private String excelPath;
    private List<BdStudentScoreExcel> studentScoreExcels;

    public BaseTemplateExcel(){
        studentScoreExcels = new ArrayList<BdStudentScoreExcel>();
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public List<BdStudentScoreExcel> getStudentScoreExcels() {
        return studentScoreExcels;
    }

    public void AddStudentScoreExcels(BdStudentScoreExcel studentScoreExcel) {
        studentScoreExcels.add(studentScoreExcel);
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
    public String pfsnLevelConvert(String pfsnLevel) {
        if (pfsnLevel.equals("1")) {
            return "专升本";
        } else if (pfsnLevel.equals("5")) {
            return "高起专";
        }
        return "";
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

    /**
     * 学期转换
     *
     * @param semester
     * @return
     */
    public String semesterConvert(String semester) {
        String ret = "";
        switch (semester) {
            case "1":
                ret = "一";
                break;
            case "2":
                ret = "二";
                break;
            case "3":
                ret = "三";
                break;
            case "4":
                ret = "四";
                break;
            case "5":
                ret = "五";
                break;
            case "6":
                ret = "六";
                break;
        }
        return ret;
    }
}
