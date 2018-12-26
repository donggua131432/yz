package com.yz.service.educational.excel;

import com.yz.dao.educational.BdStudentTScoreMapper;
import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JYTemplateExcel extends BaseTemplateExcel {

    public JYTemplateExcel() {
        super.setExcelPath("static/excel/JYTemplateExcel.xls");
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
                if (i % 30 == 0) {
                    sb.append(getRowHeader(j, excel.getSemester()));
                }

                sb.append(getRowDetail(map, rnd, mapCourse, excel));

                if ((i + 1) % 30 == 0) {
                    sb.append(getRowFooter());
                }
                i++;
                if (size == i) {
                    int span = 30 - (size % 30);
                    if (span < 30) {
                        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"19.5\" ss:Span=\"" + (span - 1) + "\"/>\r\n");
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
                "  <Table ss:ExpandedColumnCount=\"23\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:DefaultColumnWidth=\"54\" ss:DefaultRowHeight=\"14.25\">\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"73.5\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"39\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"18\" ss:Span=\"13\"/>\n" +
                "   <Column ss:Index=\"17\" ss:AutoFitWidth=\"0\" ss:Width=\"40.5\"/>\n" +
                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"19.5\" ss:Span=\"1\"/>\n" +
                "   <Column ss:Index=\"20\" ss:AutoFitWidth=\"0\" ss:Width=\"30\"/>\n" +
                "   <Column ss:StyleID=\"s62\" ss:AutoFitWidth=\"0\" ss:Width=\"30\" ss:Span=\"1\"/>\n" +
                "   <Column ss:Index=\"23\" ss:AutoFitWidth=\"0\" ss:Width=\"28.5\"/>\r\n");
        return sb.toString();
    }

    private String getSheetFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</Table>\n" +
                "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "   <PageSetup>\n" +
                "    <Header x:Margin=\"0.11\"/>\n" +
                "    <Footer x:Margin=\"0.11\"/>\n" +
                "    <PageMargins x:Bottom=\"0.39\" x:Left=\"0.15\" x:Right=\"0.15\" x:Top=\"0.39\"/>\n" +
                "   </PageSetup>\n" +
                "   <Unsynced/>\n" +
                "   <Print>\n" +
                "    <ValidPrinterInfo/>\n" +
                "    <PaperSizeIndex>9</PaperSizeIndex>\n" +
                "    <HorizontalResolution>600</HorizontalResolution>\n" +
                "    <VerticalResolution>600</VerticalResolution>\n" +
                "   </Print>\n" +
                "   <PageBreakZoom>60</PageBreakZoom>\n" +
                "   <Panes>\n" +
                "    <Pane>\n" +
                "     <Number>3</Number>\n" +
                "     <ActiveRow>27</ActiveRow>\n" +
                "     <ActiveCol>24</ActiveCol>\n" +
                "    </Pane>\n" +
                "   </Panes>\n" +
                "   <ProtectObjects>False</ProtectObjects>\n" +
                "   <ProtectScenarios>False</ProtectScenarios>\n" +
                "  </WorksheetOptions>\n" +
                " </Worksheet>\r\n");
        return sb.toString();
    }

    private String getRowHeader(int mod, String semester) {

        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"26.0625\">\n" +
                "    <Cell ss:MergeAcross=\"22\" ss:StyleID=\"s64\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">$unvsName成人高等教育<U> $year  </U><Font>学年</Font><U>  $semester  </U><Font>学期考勤与成绩表</Font></ss:Data></Cell>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"19.5\">\n" +
                "    <Cell ss:MergeAcross=\"22\" ss:StyleID=\"s65\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">院（系、部）：<U> 继续教育学院  </U><Font>级别：</Font><U> $grade级 </U><Font>专业：</Font><U>$pfsnName                   </U></ss:Data></Cell>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"17.0625\">\n" +
                "    <Cell ss:MergeAcross=\"22\" ss:StyleID=\"s68\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">办学层次：<U> $pfsnLevel    </U><Font>办学地点：</Font><U> 远智教育  </U><Font>  课程：</Font><U>  $courseName        </U><Font>学分：</Font><U>    </U></ss:Data></Cell>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696540\"><Data ss:Type=\"String\">学号</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696560\"><Data ss:Type=\"String\">姓名</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"13\" ss:StyleID=\"m8696580\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">考<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">          </Font><Font>勤</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696600\"><Data ss:Type=\"String\">考勤分</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8696620\"><Data ss:Type=\"String\">作业</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696640\"><Data ss:Type=\"String\">期考</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696660\"><Data ss:Type=\"String\">总评</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696928\"><Data ss:Type=\"String\">平时</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m8696948\"><Data ss:Type=\"String\">补考</Data></Cell>\n" +
                "   </Row>\r\n");
        sb.append(" <Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:Index=\"3\" ss:MergeAcross=\"1\" ss:StyleID=\"m8696968\"><Data\n" +
                "      ss:Type=\"String\">" + getDate(0, mod, semester) + "    日</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8696988\"><Data ss:Type=\"String\">" + getDate(1, mod, semester) + "    日</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8697008\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">" + getDate(2, mod, semester) + "<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">    </Font><Font>日</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8697028\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">" + getDate(3, mod, semester) + "<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">    </Font><Font>日</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8697048\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">" + getDate(4, mod, semester) + "<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">    </Font><Font>日</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8697068\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">" + getDate(5, mod, semester) + "<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">    </Font><Font>日</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m8697088\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">" + getDate(6, mod, semester) + "<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">   </Font><Font> 日</Font></ss:Data></Cell>\n" +
                "    <Cell ss:Index=\"18\" ss:MergeDown=\"1\" ss:StyleID=\"m8697108\"><Data\n" +
                "      ss:Type=\"Number\">1</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"m8697152\"><Data ss:Type=\"Number\">2</Data></Cell>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:Index=\"3\" ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s118\"><Data ss:Type=\"String\">晚上</Data></Cell>\n" +
                "   </Row>\r\n");
        return sb.toString();
    }

    private String getRowDetail(Map<String, Object> map, Random rnd, Map<String, Object> courseMap, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(12) + 75);
        int score2 = (rnd.nextInt(12) + 75);
        int score3 = (rnd.nextInt(12) + 75);
        int score4 = (rnd.nextInt(12) + 75);
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"19.5\">\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s126\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s123\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s123\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s123\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s122\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s121\">" + getRandData(rnd) + "</Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"Number\">" + score2 + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"Number\">" + score3 + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s119\"><Data ss:Type=\"Number\">" + score4 + "</Data></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s124\"\n" +
                "     ss:Formula=\"=RC[-4]*0.3+RC[-3]*0.1+RC[-2]*0.1+RC[-1]*0.5\"></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s124\" ss:Formula=\"=RC[-1]*2-RC[-2]\"></Cell>\r\n");
        sb.append("<Cell ss:StyleID=\"s125\"/>\r\n");
        sb.append("</Row>\r\n");
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        if (StringUtil.hasValue(excel.getExamTime())) {
            scoreExcel.setExamTime(excel.getExamTime());
        }
        scoreExcel.setScore(String.valueOf(score1 * 0.3 + score2 * 0.1 + score3 * 0.1 + score4 * 0.5));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }

    private String getRowFooter() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "    <Cell ss:StyleID=\"s135\"/>\n" +
                    "    <Cell ss:StyleID=\"s135\"/>\n" +
                    "    <Cell ss:StyleID=\"s132\"/>\n" +
                    "   </Row>\r\n");
        }
        sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s133\"><Data ss:Type=\"String\">     旷课“×”   请假“＃”   迟到“О”</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s134\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s65\"><Data ss:Type=\"String\"> 科任教师签名：</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "   </Row>\r\n");
        sb.append("<Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "    <Cell ss:StyleID=\"s136\"><Data ss:Type=\"String\">  年    月    日</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s135\"/>\n" +
                "    <Cell ss:StyleID=\"s132\"/>\n" +
                "   </Row>");

        return sb.toString();
    }

    private String getRandData(Random rnd) {
        String data = "";
        int i = (rnd.nextInt(50) + 1);
        switch (i) {
            case 1:
                data = "<Data ss:Type=\"String\">＃</Data>";
                break;
            case 2:
                data = "<Data ss:Type=\"String\">О</Data>";
                break;
        }
        return data;
    }

    private String getDate(int amount, int mod, String semester) {
        Date date = null;
        if (semester.equals("1")  || semester.equals("3")  || semester.equals("5")) {
            date = DateUtil.convertDateStrToDate("03-05", "MM-dd");
        } else {
            date = DateUtil.convertDateStrToDate("09-15", "MM-dd");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("M月d");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7 * mod + amount);
        date = calendar.getTime();
        return sdf.format(date);
    }

}
