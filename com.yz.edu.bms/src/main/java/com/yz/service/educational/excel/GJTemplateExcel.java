package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class GJTemplateExcel extends BaseTemplateExcel {

    public GJTemplateExcel() {
        super.setExcelPath("static/excel/GJTemplateExcel.xls");
    }

    @Override
    public String excelRender(List<Map<String, Object>> listStudent, List<Map<String, Object>> listCourse, TemplateExcel excel, String text) {
        int size = listStudent.size();
        int j = 0;
        for (Map<String, Object> mapCourse : listCourse) {
            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();
            sb.append(getSheetHeader());
            sb.append(getRowHeader(j));
            for (Map<String, Object> map : listStudent) {
                sb.append(getRowDetail(map, rnd, mapCourse, excel));
            }
            sb.append(getSheetFooter());
            sb.append("$rowdetail");
            text = text.replace("$rowdetail", sb.toString());
            text = text.replace("$courseName", String.valueOf(mapCourse.get("thp_name")));
            text = text.replace("$year", excel.getYear());
            text = text.replace("$grade", excel.getGrade());
            text = text.replace("$semester", excel.getSemester());
            text = text.replace("$examTime", excel.getExamTime());
            text = text.replace("$unvsName", excel.getUnvsName());
            text = text.replace("$pfsnName", pfsnNameConvert(excel.getPfsnName()));
            text = text.replace("$pfsnLevel", pfsnLevelConvert(excel.getPfsnLevel()));
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
                "  <Table ss:ExpandedColumnCount=\"6\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:StyleID=\"s68\" ss:DefaultColumnWidth=\"54\"\n" +
                "   ss:DefaultRowHeight=\"14.25\">\n" +
                "   <Column ss:StyleID=\"s67\" ss:AutoFitWidth=\"0\" ss:Width=\"108\"/>\n" +
                "   <Column ss:StyleID=\"s67\" ss:AutoFitWidth=\"0\" ss:Width=\"82.5\"/>\n" +
                "   <Column ss:StyleID=\"s68\" ss:AutoFitWidth=\"0\" ss:Width=\"75.75\"/>\n" +
                "   <Column ss:StyleID=\"s68\" ss:AutoFitWidth=\"0\" ss:Width=\"71.25\"/>\n" +
                "   <Column ss:StyleID=\"s68\" ss:AutoFitWidth=\"0\" ss:Width=\"130.5\"/>\n" +
                "   <Column ss:StyleID=\"s68\" ss:AutoFitWidth=\"0\" ss:Width=\"222\"/>\n");
        return sb.toString();
    }

    private String getSheetFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</Table>\n" +
                "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "   <PageSetup>\n" +
                "    <Header x:Margin=\"0.3\"/>\n" +
                "    <Footer x:Margin=\"0.3\"/>\n" +
                "    <PageMargins x:Bottom=\"0.75\" x:Left=\"0.69930555555555596\"\n" +
                "     x:Right=\"0.69930555555555596\" x:Top=\"0.75\"/>\n" +
                "   </PageSetup>\n" +
                "   <Print>\n" +
                "    <ValidPrinterInfo/>\n" +
                "    <PaperSizeIndex>9</PaperSizeIndex>\n" +
                "    <HorizontalResolution>600</HorizontalResolution>\n" +
                "    <VerticalResolution>600</VerticalResolution>\n" +
                "   </Print>\n" +
                "   <Panes>\n" +
                "    <Pane>\n" +
                "     <Number>3</Number>\n" +
                "     <ActiveRow>15</ActiveRow>\n" +
                "     <ActiveCol>4</ActiveCol>\n" +
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
        sb.append(" <Row ss:AutoFitHeight=\"0\" ss:Height=\"42\" ss:StyleID=\"s65\">\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s84\"><Data ss:Type=\"String\">$unvsName继续教育学院成绩登记表（$year年第$semester学期）</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s65\">\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">班级ID</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">课程名</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">课程号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">教学点代号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s70\"><Data ss:Type=\"String\">任课老师账户</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s66\">\n" +
                "    <Cell ss:StyleID=\"s72\"/>\n" +
                "    <Cell ss:StyleID=\"s73\"><Data ss:Type=\"String\">$courseName</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"/>\n" +
                "    <Cell ss:StyleID=\"s72\"><Data ss:Type=\"String\">036</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s65\">\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">学号</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">姓名</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">平时成绩</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">期末成绩</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">考情(缺考/作弊)</Data></Cell>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    private String getRowDetail(Map<String, Object> map, Random rnd, Map<String, Object> courseMap, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(12) + 75);
        int score2 = (rnd.nextInt(12) + 75);
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s65\">\n" +
                "    <Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s80\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s81\"><Data ss:Type=\"Number\">" + score2 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s82\"/>\n" +
                "   </Row>\n");
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        if(StringUtil.hasValue(excel.getExamTime())) {
            scoreExcel.setExamTime(excel.getExamTime());
        }
        scoreExcel.setScore(String.valueOf(score2));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }
}
