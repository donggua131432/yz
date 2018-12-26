package com.yz.controller.gk;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdExamYearProfession;
import com.yz.model.educational.ExamRoomSeatsQuery;
import com.yz.model.gk.BdStudentExamGKExcel;
import com.yz.model.gk.BdStudentExamGKQuery;
import com.yz.service.gk.BdStudentExamGKService;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Controller
@RequestMapping("/studentExamGK")
public class BdStudentExamGKController {
    @Autowired
    BdStudentExamGKService studentExamGKService;

    @RequestMapping("/list")
    @Rule("studentExamGK")
    public String showList(HttpServletRequest request) {
        return "gk/exam/exam-room-list";
    }

    @RequestMapping("/studentExamGKImport")
    public String professionImport(HttpServletRequest request) {
        return "gk/exam/exam-room-import";
    }

    @RequestMapping("/findAllStudentExamGK")
    @Rule("studentExamGK:findAll")
    @ResponseBody
    public Object findAllStudentExamGK(@RequestParam(value = "start", defaultValue = "1") int start,
                                       @RequestParam(value = "length", defaultValue = "10") int length, BdStudentExamGKQuery query) {
        PageHelper.offsetPage(start, length);
        List<Map<String, Object>> resultList = studentExamGKService.findAllStudentExamGK(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/uploadStudentExamGK")
    @Rule("studentExamGK:uploadStudentExamGK")
    @ResponseBody
    public Object uploadStudentExamGK(
            @RequestParam(value = "excelStudentExamGK", required = false) MultipartFile excelStudentExamGK) {
        // 对导入工具进行字段填充
        ExcelUtil.IExcelConfig<BdStudentExamGKExcel> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentExamGKExcel>();
        testExcelCofing.setSheetName("index").setType(BdStudentExamGKExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("国开考试年度", "examYear"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("考试科目", "examCourse"))
                .addTitle(new ExcelUtil.IExcelTitle("考试方式", "examType"))
                .addTitle(new ExcelUtil.IExcelTitle("考场名称", "examPlace"))
                .addTitle(new ExcelUtil.IExcelTitle("考场地址", "examAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("考试时间", "examTime"))
                .addTitle(new ExcelUtil.IExcelTitle("课室号", "placeName"))
                .addTitle(new ExcelUtil.IExcelTitle("座位号", "seatNum"))
                .addTitle(new ExcelUtil.IExcelTitle("温馨提示", "tips"));
        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<BdStudentExamGKExcel> list = ExcelUtil.importWorkbook(excelStudentExamGK.getInputStream(), testExcelCofing,
                    excelStudentExamGK.getOriginalFilename());
            // 遍历插入
            for (BdStudentExamGKExcel studentExamGKExcel : list) {
                if (!StringUtil.hasValue(studentExamGKExcel.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(studentExamGKExcel.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学员姓名不能为空");
                }
                if (!StringUtil.hasValue(studentExamGKExcel.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(studentExamGKExcel.getStdNo())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学号不能为空");
                }
                if (!StringUtil.hasValue(studentExamGKExcel.getExamCourse())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考试科目不能为空");
                }
                if (StringUtil.hasValue(studentExamGKExcel.getExamTime())) {
                    try {
                        String arrs[] = studentExamGKExcel.getExamTime().split("-");
                        Date date = DateUtil.convertDateStrToDate(arrs[0], "yyyy年MM月dd日HH:mm");
                        String endTime = DateUtil.formatDate(date, "yyyy-MM-dd") +arrs[1];
                        studentExamGKExcel.setExamStartTime(DateUtil.convertDateStrToDate(arrs[0], "yyyy年MM月dd日HH:mm"));
                        studentExamGKExcel.setExamEndTime(DateUtil.convertDateStrToDate(endTime, "yyyy-MM-ddHH:mm"));
                    } catch (Exception e) {
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考试时间格式有误");
                    }
                }
                if (StringUtil.hasValue(studentExamGKExcel.getTips())) {
                    studentExamGKExcel.setTips(studentExamGKExcel.getTips().replace("\n","<br/>"));
                }
                index++;

            }
            List<Map<String, Object>> resultList = studentExamGKService.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("学员非在读学员或学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("grade") + "-" + map.get("std_name") + "-" + map.get("id_card").toString() + "<br/>");
                }

                throw new IllegalArgumentException(sb.toString());
            }
            studentExamGKService.insert(list);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
        return "SUCCESS";
    }

    @RequestMapping("/updateJoinStatus")
    @Rule("studentExamGK:updateJoinStatus")
    @ResponseBody
    public Object updateJoinStatus(@RequestParam(name = "eigIds[]") String[] eigIds, @RequestParam(name = "status") String status) {
        studentExamGKService.updateJoinStatus(eigIds, status);
        return "SUCCESS";
    }

    @RequestMapping("/findExamYear")
    @ResponseBody
    public Object findExamYear() {
        return studentExamGKService.findExamYear();
    }

    @RequestMapping("/findExamCourse")
    @ResponseBody
    public Object findExamCourse() {
        return studentExamGKService.findExamCourse();
    }

    @RequestMapping("/findExamType")
    @ResponseBody
    public Object findExamType() {
        return studentExamGKService.findExamType();
    }

    @RequestMapping("/deletes")
    @Rule("studentExamGK:deletes")
    @ResponseBody
    public Object deletes(@RequestParam(name = "eigIds[]") String[] eigIds) throws Exception {
        studentExamGKService.deleteStudentExamGK(eigIds);
        return "SUCCESS";
    }

    @RequestMapping("/findExamTime")
    @ResponseBody
    public Object findExamTime(String date) {
        return studentExamGKService.findExamTime(date);
    }
}
