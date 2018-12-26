package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SYZTemplateExcel extends BaseTemplateExcel{

    public SYZTemplateExcel (){
        super.setExcelPath("static/excel/SZYTemplateExcel.xls");
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

                sb.append(getRowDetail(map, rnd, mapCourse,excel));

                if ((i + 1) % 33 == 0) {
                    sb.append(getRowFooter());
                }
                i++;
                if (size == i) {
                    int span = 33 - (size % 33);
                    if (span < 33) {
                        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"18\" ss:Span=\"" + (span - 1) + "\"/>\r\n");
                        sb.append(getRowFooter());
                    }
                }
            }
            sb.append(getSheetFooter());
            sb.append("$rowdetail");
            text = text.replace("$rowdetail", sb.toString());
            text = text.replace("$courseName", String.valueOf(mapCourse.get("thp_name")));
            text = text.replace("$year", excel.getYear());
            text = text.replace("$grade", excel.getGrade());
            text = text.replace("$semester", semesterConvert(excel.getSemester()));
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
                "  <Table ss:ExpandedColumnCount=\"7\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:DefaultColumnWidth=\"54\" ss:DefaultRowHeight=\"14.25\">\n" +
                "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"99.75\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"58.5\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"62.25\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"63\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"61.5\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"63.75\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"84\"/>\n");
        return sb.toString();
    }

    private String getSheetFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</Table>\n" +
                "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "   <PageSetup>\n" +
                "    <Layout x:CenterHorizontal=\"1\"/>\n" +
                "    <Header x:Margin=\"0.12\"/>\n" +
                "    <Footer x:Margin=\"0.12\" x:Data=\"&amp;C第 &amp;P 页，共 &amp;N 页\"/>\n" +
                "    <PageMargins x:Bottom=\"0.2\" x:Left=\"0.39\" x:Right=\"0.39\" x:Top=\"0.39\"/>\n" +
                "   </PageSetup>\n" +
                "   <Print>\n" +
                "    <ValidPrinterInfo/>\n" +
                "    <PaperSizeIndex>9</PaperSizeIndex>\n" +
                "    <HorizontalResolution>600</HorizontalResolution>\n" +
                "    <VerticalResolution>180</VerticalResolution>\n" +
                "   </Print>\n" +
                "   <PageBreakZoom>60</PageBreakZoom>\n" +
                "   <Panes>\n" +
                "    <Pane>\n" +
                "     <Number>3</Number>\n" +
                "     <ActiveRow>10</ActiveRow>\n" +
                "     <ActiveCol>3</ActiveCol>\n" +
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
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"30\">\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s66\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\"><B>$unvsName成人教育<U>$year</U><Font>学年第$semester学期末总评成绩登记表</Font></B></ss:Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"11.25\">\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "    <Cell ss:StyleID=\"s67\"/>\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "    <Cell ss:StyleID=\"s66\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"21\">\n" +
                "    <Cell ss:StyleID=\"s68\"><Data ss:Type=\"String\">教学点：远智教育</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s94\"><Data ss:Type=\"String\">班级名称：$grade级$pfsnLevel$pfsnName    班</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s95\"><Data ss:Type=\"String\">课程名称:$courseName</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"21\">\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m41595368\"><Data ss:Type=\"String\">学  号</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m41595408\"><Data ss:Type=\"String\">姓名</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m41594940\"><Data ss:Type=\"String\">平时成绩</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m41594960\"><Data ss:Type=\"String\">期末考试成绩</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m41595612\"><Data ss:Type=\"String\">期末总评成绩</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"21\">\n" +
                "    <Cell ss:Index=\"3\" ss:MergeDown=\"1\" ss:StyleID=\"m41595448\"><Data\n" +
                "      ss:Type=\"String\">考勤占总评20%</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"m41595488\"><Data ss:Type=\"String\">作业占总评20%</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"m41595000\"><Data ss:Type=\"String\">考试成绩</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"m41595572\"><Data ss:Type=\"String\">60%得分</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"21\"/>\n");
        return sb.toString();
    }

    private String getRowDetail(Map<String, Object> map, Random rnd, Map<String, Object> courseMap, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(6) + 13);
        int score2 = (rnd.nextInt(6) + 13);
        int score3 = (rnd.nextInt(12) + 75);
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s81\"><Data ss:Type=\"String\">"+ map.get("school_roll")+"</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s82\"><Data ss:Type=\"String\">"+map.get("std_name")+"</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s83\"><Data ss:Type=\"Number\">"+score1+"</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s83\"><Data ss:Type=\"Number\">"+score2+"</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s84\"><Data ss:Type=\"Number\">"+score3+"</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s85\" ss:Formula=\"=RC[-1]*60%\"><Data ss:Type=\"Number\"></Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s86\" ss:Formula=\"=RC[-4]+RC[-3]+RC[-1]\"><Data\n" +
                "      ss:Type=\"Number\"></Data></Cell>\n" +
                "   </Row>\n");
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        if(StringUtil.hasValue(excel.getExamTime())) {
            scoreExcel.setExamTime(excel.getExamTime());
        }
        scoreExcel.setScore(String.valueOf(score1 + score2 + score3 * 0.6));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }

    private String getRowFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <Row ss:Height=\"18.75\">\n" +
                "    <Cell ss:StyleID=\"s87\"/>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s98\"><Data ss:Type=\"String\">阅卷教师签名:</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:Height=\"18.75\">\n" +
                "    <Cell ss:StyleID=\"s87\"/>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s98\"><Data ss:Type=\"String\">年  月   日</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s88\"/>\n" +
                "   </Row>\n");
        return sb.toString();
    }
}
