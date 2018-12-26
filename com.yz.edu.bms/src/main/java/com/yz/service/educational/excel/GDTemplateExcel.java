package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class GDTemplateExcel extends BaseTemplateExcel {

    public GDTemplateExcel() {
        super.setExcelPath("static/excel/GDTemplateExcel.xls");
    }

    @Override
    public String excelRender(List<Map<String, Object>> listStudent, List<Map<String, Object>> listCourse, TemplateExcel excel, String text) {
        int size = listStudent.size();
        int j = 0;
        for (Map<String, Object> mapCourse : listCourse) {
            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();
            sb.append(getSheetHeader());
            int i = 0;
            for (Map<String, Object> map : listStudent) {
                if (i % 33 == 0) {
                    sb.append(getRowHeader(j));
                }

                sb.append(getRowDetail(i, map, rnd, mapCourse, excel));
                if ((i + 1) % 33 == 0) {
                    sb.append(getRowFooter());
                }
                i++;
            }
            sb.append(getSheetFooter());
            sb.append("$rowdetail");
            text = text.replace("$rowdetail", sb.toString());
            text = text.replace("$courseName", String.valueOf(mapCourse.get("thp_name")));
            text = text.replace("$year", excel.getYear());
            text = text.replace("$grade", excel.getGrade());
            text = text.replace("$semester", semesterConvert(excel.getSemester()));
            text = text.replace("$examTime", excel.getExamTime());
            text = text.replace("$unvsName", excel.getUnvsName());
            text = text.replace("$pfsnName", pfsnNameConvert(excel.getPfsnName()));
            text = text.replace("$pfsnLevel", pfsnLevelConvertSimple(excel.getPfsnLevel()));
            j++;
        }
        if (listCourse.size() == 0) {
            text = text.replace("$rowdetail", getSheetHeader() + getSheetFooter());
        }
        text = text.replace("$rowdetail", "");
        return text;
    }

    private String getSheetHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Worksheet ss:Name=\"$courseName\">\n" +
                "  <Table ss:ExpandedColumnCount=\"11\" ss:ExpandedRowCount=\"150\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:DefaultColumnWidth=\"54\" ss:DefaultRowHeight=\"14.25\">\n" +
                "   <Column ss:Index=\"4\" ss:AutoFitWidth=\"0\" ss:Width=\"77.25\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"51.75\" ss:Span=\"5\"/>\n");
        return sb.toString();
    }

    private String getSheetFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</Table>\n" +
                "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "   <PageSetup>\n" +
                "    <Header x:Margin=\"0.12\"/>\n" +
                "    <Footer x:Margin=\"0.12\" x:Data=\"&amp;C第 &amp;P 页，共 &amp;N 页\"/>\n" +
                "    <PageMargins x:Bottom=\"0.21\" x:Left=\"0.2\" x:Right=\"0.2\" x:Top=\"0.21\"/>\n" +
                "   </PageSetup>\n" +
                "   <Print>\n" +
                "    <ValidPrinterInfo/>\n" +
                "    <PaperSizeIndex>9</PaperSizeIndex>\n" +
                "    <HorizontalResolution>600</HorizontalResolution>\n" +
                "    <VerticalResolution>0</VerticalResolution>\n" +
                "   </Print>\n" +
                "   <PageBreakZoom>100</PageBreakZoom>\n" +
                "   <Panes>\n" +
                "    <Pane>\n" +
                "     <Number>3</Number>\n" +
                "     <RangeSelection>R1C1:R1C10</RangeSelection>\n" +
                "    </Pane>\n" +
                "   </Panes>\n" +
                "   <ProtectObjects>False</ProtectObjects>\n" +
                "   <ProtectScenarios>False</ProtectScenarios>\n" +
                "  </WorksheetOptions>\n" +
                " </Worksheet>\n");
        return sb.toString();
    }

    private String getRowHeader(int mod) {

        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"30\" ss:StyleID=\"s68\">\n" +
                "    <Cell ss:MergeAcross=\"9\" ss:StyleID=\"s84\"><Data ss:Type=\"String\">$unvsName成人教育$year学年第$semester学期末成绩登记表</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"30\" ss:StyleID=\"s68\">\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">教学点：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m46903316\"><Data ss:Type=\"String\">远智教育</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">班级名称：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"m46903336\"><Data ss:Type=\"String\">$grade级$pfsnLevel$pfsnName</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">课程名称</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m46903356\"><Data ss:Type=\"String\">$courseName</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"36.9375\" ss:StyleID=\"s69\">\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">序号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">学号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">姓名</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">科目名称</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">考试成绩</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">形考成绩</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">备注</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">颁发机构</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">证书名称</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">证书编号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s83\"/>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    private String getRowFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append(" <Row ss:AutoFitHeight=\"0\" ss:Span=\"1\"/>\n");
        return sb.toString();
    }

    private String getRowDetail(int i, Map<String, Object> map, Random rnd, Map<String, Object> courseMap, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(12) + 75);
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"20.0625\" ss:StyleID=\"s70\">\n" +
                "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"Number\">" + (i + 1) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">" + courseMap.get("thp_name") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s80\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s81\"/>\n" +
                "    <Cell ss:StyleID=\"s78\"/>\n" +
                "    <Cell ss:StyleID=\"s78\"/>\n" +
                "    <Cell ss:StyleID=\"s78\"/>\n" +
                "    <Cell ss:StyleID=\"s78\"/>\n" +
                "   </Row>\n");
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        if (StringUtil.hasValue(excel.getExamTime())) {
            scoreExcel.setExamTime(excel.getExamTime());
        }
        scoreExcel.setScore(String.valueOf(score1));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }
}
