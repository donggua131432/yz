package com.yz.service.educational.excel;

import com.yz.model.educational.BdStudentScoreExcel;
import com.yz.model.educational.TemplateExcel;
import com.yz.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ZKTemplateExcel extends BaseTemplateExcel {

    public ZKTemplateExcel() {
        super.setExcelPath("static/excel/ZKTemplateExcel.xls");
    }

    //90分以上
    private int count1 = 0;
    //80—90分
    private int count2 = 0;
    //70—79分
    private int count3 = 0;
    //60—69分
    private int count4 = 0;
    //不及格
    private int count5 = 0;

    @Override
    public String excelRender(List<Map<String, Object>> listStudent, List<Map<String, Object>> listCourse, TemplateExcel excel, String text) {
        int size = listStudent.size();
        int j = 0;
        for (Map<String, Object> mapCourse : listCourse) {
            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();
            sb.append(getSheetHeader());
            int i = 0;
            count1 = 0;
            count2 = 0;
            count3 = 0;
            count4 = 0;
            count5 = 0;
            for (Map<String, Object> map : listStudent) {
                if (i % 40 == 0) {
                    sb.append(getRowHeader(j));
                }

                sb.append(getRowDetail(map, rnd, mapCourse, i, size, excel));

                if ((i + 1) % 40 == 0) {
                    sb.append(getRowFooter());
                }
                i++;
                if (size == i) {
                    int span = 40 - (size % 40);
                    if (span < 40) {
                        sb.append(getRowFooter());
                    }
                }
            }
            sb.append(getSheetFooter());
            sb.append("$rowdetail");
            text = text.replace("$rowdetail", sb.toString());
            text = text.replace("$courseName", String.valueOf(mapCourse.get("thp_name")));
            text = text.replace("$hours", String.valueOf(mapCourse.get("total_hours")));
            text = text.replace("$empName", String.valueOf(mapCourse.get("emp_name")));
            text = text.replace("$year", excel.getYear());
            text = text.replace("$grade", excel.getGrade());
            text = text.replace("$semester", semesterConvert(excel.getSemester()));
            text = text.replace("$unvsName", excel.getUnvsName());
            text = text.replace("$pfsnName", pfsnNameConvert(excel.getPfsnName()));
            text = text.replace("$pfsnLevel", pfsnLevelConvertSimple(excel.getPfsnLevel()));
            text = text.replace("$size", String.valueOf(size));
            text = text.replace("$count1", String.valueOf(count1));
            text = text.replace("$percent1", doubleFormat(((double) count1 / size) * 100));
            text = text.replace("$count2", String.valueOf(count2));
            text = text.replace("$percent2", doubleFormat(((double) count2 / size) * 100));
            text = text.replace("$count3", String.valueOf(count3));
            text = text.replace("$percent3", doubleFormat(((double) count3 / size) * 100));
            text = text.replace("$count4", String.valueOf(count4));
            text = text.replace("$percent4", doubleFormat(((double) count4 / size) * 100));
            text = text.replace("$count5", String.valueOf(count5));
            text = text.replace("$percent5", doubleFormat(((double) count5 / size) * 100));
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
                "  <Table ss:ExpandedColumnCount=\"12\" x:FullColumns=\"1\"\n" +
                "   x:FullRows=\"1\" ss:StyleID=\"s64\" ss:DefaultColumnWidth=\"54\"\n" +
                "   ss:DefaultRowHeight=\"13.5\">\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"120\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"52.5\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"42.75\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"39\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"38.25\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"123.75\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"36.75\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"42.75\"/>\n" +
                "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"39.75\"/>\n" +
                "   <Column ss:Index=\"12\" ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"86.25\"/>\n");
        return sb.toString();
    }

    private String getSheetFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</Table>\n" +
                "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "   <PageSetup>\n" +
                "    <Header x:Margin=\"0.31388888888888899\"/>\n" +
                "    <Footer x:Margin=\"0.31388888888888899\" x:Data=\"&amp;R第 &amp;P 页，共 &amp;N 页\"/>\n" +
                "    <PageMargins x:Bottom=\"0.35416666666666702\" x:Left=\"0.196527777777778\"\n" +
                "     x:Right=\"0.196527777777778\" x:Top=\"0.35416666666666702\"/>\n" +
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
                "     <ActiveRow>12</ActiveRow>\n" +
                "     <ActiveCol>6</ActiveCol>\n" +
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
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"33\">\n" +
                "    <Cell ss:MergeAcross=\"9\" ss:StyleID=\"s91\"><Data ss:Type=\"String\">$unvsName继续教育学院&#10;考试、考查成绩登记表</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"15.9375\">\n" +
                "    <Cell ss:MergeAcross=\"9\" ss:StyleID=\"s93\"><Data ss:Type=\"String\">（$year 学年第  $semester  学期）</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"32.0625\">\n" +
                "    <Cell ss:MergeAcross=\"9\" ss:StyleID=\"s95\"><Data ss:Type=\"String\">科目名称：$courseName   教学点：远智教育  年级专业：$grade级$pfsnLevel$pfsnName    &#10;任课教师：$empName   学时：$hours</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">学  号</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">姓名 </Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">成绩记录</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">学  号</Data></Cell>\n" +
                "    <Cell ss:MergeDown=\"1\" ss:StyleID=\"m51597812\"><Data ss:Type=\"String\">姓名</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s97\"><Data ss:Type=\"String\">成绩记录</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"15\">\n" +
                "    <Cell ss:Index=\"3\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">平时</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">期末</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">总评</Data></Cell>\n" +
                "    <Cell ss:Index=\"8\" ss:StyleID=\"s71\"><Data ss:Type=\"String\">平时</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">期末</Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s71\"><Data ss:Type=\"String\">总评</Data></Cell>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    private String getRowDetail(Map<String, Object> map, Random rnd, Map<String, Object> courseMap, int index, int size, TemplateExcel excel) {
        StringBuilder sb = new StringBuilder();
        int score1 = (rnd.nextInt(12) + 75);
        int score2 = (rnd.nextInt(12) + 75);
        if (index % 2 == 0) {
            sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"24\">\n" +
                    "<Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"Number\">" + score2 + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\" ss:Formula=\"=RC[-2]*0.3+RC[-1]*0.7\"><Data\n" +
                    "      ss:Type=\"Number\"></Data></Cell>\n");
        }
        if (index % 2 == 1) {
            sb.append(" <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">" + map.get("school_roll") + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">" + map.get("std_name") + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"Number\">" + score1 + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"Number\">" + score2 + "</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\" ss:Formula=\"=RC[-2]*0.3+RC[-1]*0.7\"><Data\n" +
                    "      ss:Type=\"Number\"></Data></Cell></Row>\n");
        }
        if ((size - 1) == index && index % 2 == 0) {
            sb.append(" <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\"></Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\"></Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\"></Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\"></Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\"></Data></Cell></Row>\n");
        }
        //保存分数
        BdStudentScoreExcel scoreExcel = new BdStudentScoreExcel();
        scoreExcel.setLearnId(map.get("learn_id").toString());
        scoreExcel.setThpId(courseMap.get("thp_id").toString());
        scoreExcel.setUnvsId(map.get("unvs_id").toString());
        scoreExcel.setEmpId(courseMap.get("emp_id").toString());
        if (StringUtil.hasValue(excel.getExamTime())) {
            scoreExcel.setExamTime(excel.getExamTime());
        }
        long score = Math.round(score1 * 0.3 + score2 * 0.7);
        if (score > 90) {
            count1++;
        } else if (score >= 80 && score <= 90) {
            count2++;
        } else if (score >= 70 && score <= 79) {
            count3++;
        } else if (score >= 60 && score <= 69) {
            count4++;
        } else {
            count5++;
        }
        scoreExcel.setScore(String.valueOf(score1 * 0.3 + score2 * 0.7));
        super.AddStudentScoreExcels(scoreExcel);
        return sb.toString();
    }

    private String getRowFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"20.0625\">\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">全班人数</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"m50232188\"><Data ss:Type=\"String\">$size人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"m50232208\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"20.0625\" ss:StyleID=\"Default\">\n" +
                "    <Cell ss:StyleID=\"s75\"><Data ss:Type=\"String\">90分以上</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s101\"><Data ss:Type=\"String\">$count1人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s102\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">占   <Font html:Size=\"12\">$percent1% </Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s79\"/>\n" +
                "    <Cell ss:StyleID=\"s80\"/>\n" +
                "    <Cell ss:StyleID=\"s80\"/>\n" +
                "    <Cell ss:StyleID=\"s80\"/>\n" +
                "    <Cell ss:StyleID=\"s81\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s84\"><Data ss:Type=\"String\">80—90分</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s101\"><Data ss:Type=\"String\">$count2人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s102\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">占   <Font html:Size=\"12\">$percent2% </Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s103\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s84\"><Data ss:Type=\"String\">70—79分</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s101\"><Data ss:Type=\"String\">$count3人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s102\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">占   <Font html:Size=\"12\">$percent3% </Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s104\"/>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s84\"><Data ss:Type=\"String\">60—69分</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s101\"><Data ss:Type=\"String\">$count4人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s102\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">占   <Font html:Size=\"12\">$percent4% </Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s84\"><Data ss:Type=\"String\">教师签名：</Data></Cell>\n" +
                "   </Row>\n" +
                "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"18\">\n" +
                "    <Cell ss:StyleID=\"s84\"><Data ss:Type=\"String\">不及格</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s101\"><Data ss:Type=\"String\">$count5人</Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s102\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">占   <Font html:Size=\"12\">$percent5% </Font></ss:Data></Cell>\n" +
                "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s106\"/>\n" +
                "   </Row>\n" +
                "   <Row>\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s107\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">注：<Font html:Face=\"Times New Roman\"\n" +
                "       x:Family=\"Roman\">1</Font><Font>、本表由各教学点填写学生姓名后送任课教师。</Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "   </Row>\n" +
                "   <Row>\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s108\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">2<Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">、本表在考试结束后两周内由任课教师记表成绩并送交继续教育学院。</Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "   </Row>\n" +
                "   <Row>\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s108\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">3<Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">、考试总评成绩的计算标准平时占</Font><Font>30%</Font><Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">，期末占</Font><Font>70%</Font><Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">。</Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "   </Row>\n" +
                "   <Row>\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s108\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">4<Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">、由教学点向学生发出成绩通知。</Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "   </Row>\n" +
                "   <Row>\n" +
                "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"s108\"><ss:Data ss:Type=\"String\"\n" +
                "      xmlns=\"http://www.w3.org/TR/REC-html40\">5<Font html:Face=\"宋体\"\n" +
                "       x:CharSet=\"134\">、本表作各种统计的原始依据，教学点要长期保存。</Font></ss:Data></Cell>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "    <Cell ss:StyleID=\"s89\"/>\n" +
                "   </Row>\n");
        return sb.toString();
    }

    /**
     * 使用NumberFormat,保留小数点后两位
     */
    public static String doubleFormat(double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        /*
         * setMinimumFractionDigits设置成2
         *
         * 如果不这么做，那么当value的值是100.00的时候返回100
         *
         * 而不是100.00
         */
        //nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        /*
         * 如果想输出的格式用逗号隔开，可以设置成true
         */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }
}
