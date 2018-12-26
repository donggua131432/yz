package com.yz.service.educational.excel;

import com.yz.dao.educational.BdStudentTScoreMapper;
import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class HSTemplateExcel extends BaseTemplateExcel {

    public HSTemplateExcel() {
        super.setExcelPath("static/excel/HSTemplateExcel.xls");
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
                if (i % 31 == 0) {
                    sb.append(getRowHeader(j,excel.getSemester()));
                }

                sb.append(getRowDetail(map, rnd, mapCourse, excel));

                if ((i + 1) % 31 == 0) {
                    sb.append(getRowFooter());
                }
                i++;
                if (size == i) {
                    int span = 31 - (size % 31);
                    if (span < 31) {
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
                "  <Table ss:ExpandedColumnCount=\"22\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:StyleID=\"s69\" ss:DefaultColumnWidth=\"54\"\n" +
                "   ss:DefaultRowHeight=\"14.25\">\n" +
                "   <Column ss:StyleID=\"s69\" ss:AutoFitWidth=\"0\" ss:Width=\"80.25\"/>\n" +
                "   <Column ss:StyleID=\"s69\" ss:AutoFitWidth=\"0\" ss:Width=\"46.5\"/>\n" +
                "   <Column ss:StyleID=\"s69\" ss:AutoFitWidth=\"0\" ss:Width=\"29.25\" ss:Span=\"9\"/>\n" +
                "   <Column ss:Index=\"13\" ss:StyleID=\"s69\" ss:AutoFitWidth=\"0\" ss:Width=\"21.75\"\n" +
                "    ss:Span=\"2\"/>\n" +
                "   <Column ss:Index=\"16\" ss:StyleID=\"s70\" ss:AutoFitWidth=\"0\" ss:Width=\"30.75\"/>\n" +
                "   <Column ss:StyleID=\"s69\" ss:AutoFitWidth=\"0\" ss:Width=\"51.75\"/>\n");
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
                "    <PageMargins x:Bottom=\"0.39\" x:Left=\"0.15\" x:Right=\"0.15\" x:Top=\"0.19\"/>\n" +
                "   </PageSetup>\n" +
                "   <Print>\n" +
                "    <ValidPrinterInfo/>\n" +
                "    <PaperSizeIndex>9</PaperSizeIndex>\n" +
                "    <HorizontalResolution>300</HorizontalResolution>\n" +
                "    <VerticalResolution>0</VerticalResolution>\n" +
                "   </Print>\n" +
                "   <PageBreakZoom>60</PageBreakZoom>\n" +
                "   <Panes>\n" +
                "    <Pane>\n" +
                "     <Number>3</Number>\n" +
                "     <RangeSelection>R1C1:R1C17</RangeSelection>\n" +
                "    </Pane>\n" +
                "   </Panes>\n" +
                "   <ProtectObjects>False</ProtectObjects>\n" +
                "   <ProtectScenarios>False</ProtectScenarios>\n" +
                "  </WorksheetOptions>\n" +
                " </Worksheet>\n");
        return sb.toString();
    }

    private String getRowHeader(int mod, String semester) {

        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"39.9375\" ss:StyleID=\"s66\">\n" +
                "    <Cell ss:MergeAcross=\"16\"><Data ss:Type=\"String\">$unvsName成人教育学生考勤和成绩登记表（$year年第$semester学期）</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"23.0625\">\n" +
                "    <Cell><Data ss:Type=\"String\">教学点：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s107\"><Data ss:Type=\"String\">远智教育</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\"><Data ss:Type=\"String\">专业：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s109\"><Data ss:Type=\"String\">$pfsnName</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\"><Data ss:Type=\"String\">层次：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s107\"><Data ss:Type=\"String\">$pfsnLevel</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">年级：</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s68\"><Data ss:Type=\"String\">$grade级</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"24.9375\">\n" +
                "    <Cell><Data ss:Type=\"String\">课程名称：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s107\"><Data ss:Type=\"String\">$courseName</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\"><Data ss:Type=\"String\">考试时间：</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s110\"><Data ss:Type=\"String\">$examTime</Data></Cell>\n" +
                "    <Cell ss:Index=\"14\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">任课教师<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">(</Font><Font>签名</Font><Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">)</Font><Font>：</Font></ss:Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.625\">\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"s111\"><Data ss:Type=\"String\">学号</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"s111\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">姓<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\"> </Font><Font>名</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"9\" ss:StyleID=\"s111\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">考<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">   </Font><Font>勤</Font><Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">    </Font><Font>情</Font><Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">   </Font><Font>况</Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m65647488\"><Data ss:Type=\"String\">考勤总评</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m65647548\"><Data ss:Type=\"String\">作业成绩</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m65647712\"><Data ss:Type=\"String\">考试成绩</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m65647772\"><Data ss:Type=\"String\">总评成绩</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"2\" ss:StyleID=\"m65647936\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">备<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\"> </Font><Font>注</Font></ss:Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"24.9375\">\n" +
                "    <Cell ss:Index=\"3\" ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(0, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(1, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(2, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(3, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(4, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(5, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(6, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(7, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(8, mod, semester) + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s74\"><Data ss:Type=\"String\">" + getDate(9, mod, semester) + "</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"20.0625\">\n" +
                "    <Cell ss:Index=\"3\" ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">晚上</Data><NamedCell\n" +
                "      ss:Name=\"_FilterDatabase\"/></Cell>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    private String getRowDetail(Map<String, Object> map, Random rnd, Map<String, Object> courseMap, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(8) + 20);
        int score2 = (rnd.nextInt(5) + 14);
        int score3 = (rnd.nextInt(12) + 75);
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s78\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s80\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s80\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s79\">" + getRandData(rnd) + "</Cell>\n" +
                "    <Cell ss:StyleID=\"s99\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s100\"><Data ss:Type=\"Number\">" + score2 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s101\"><Data ss:Type=\"Number\">" + score3 + "</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s102\" ss:Formula=\"=RC[-3]+RC[-2]+RC[-1]*0.5\"><Data\n" +
                "      ss:Type=\"Number\"></Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s104\"/>\n" +
                "   </Row>\n");
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        scoreExcel.setExamTime(excel.getExamTime());
        scoreExcel.setScore(String.valueOf(score1 + score2 + score3 * 0.5));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }

    private String getRowFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"23.0625\" ss:StyleID=\"s68\">\n" +
                "    <Cell ss:StyleID=\"s69\"/>\n" +
                "    <Cell ss:StyleID=\"s83\"><Data ss:Type=\"String\">     旷课“×”   请假“＃”   迟到“О”</Data></Cell>\n" +
                "    <Cell ss:Index=\"16\" ss:StyleID=\"s91\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\">\n" +
                "    <Cell ss:MergeAcross=\"16\" ss:MergeDown=\"2\" ss:StyleID=\"s118\"><ss:Data\n" +
                "      ss:Type=\"String\" xmlns=\"http://www.w3.org/TR/REC-html40\">注：１．各院系应将本表发给任课老师，任课老师面授时应带上本表并登记考勤；&#10;<Font\n" +
                "       html:Face=\"Times New Roman\" x:Family=\"Roman\">        </Font><Font>２．本表一式二份，一份由任课老师于课程考试结束后半月内（寒暑假可于教师集中时）连同学员考试试卷直接送交所在院系；一份由我院送各有关教学点，由教学点安排班主任登记考勤。&#10;</Font></ss:Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Span=\"1\"/>\n");
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
        SimpleDateFormat sdf = new SimpleDateFormat("M月 d日");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 10 * mod + amount);
        date = calendar.getTime();
        return sdf.format(date);
    }

}
