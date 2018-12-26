package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.stdService.StudentCollectMapper;
import com.yz.dao.stdService.StudentQingshuMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.*;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.StringUtil;
import com.yz.util.ValidationUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 学员服务---青书学堂上课跟进
 *
 * @author jyt
 */
@Service
public class StudentQingshuService {
    private static Logger log = LoggerFactory.getLogger(StudentQingshuService.class);

    @Autowired
    private StudentQingshuMapper studentQingshuMapper;

    public Object findAllQingshuList(StudentQingshuQuery query) {
        List<StudentQingshuInfo> list = studentQingshuMapper.findAllQingshuList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public int updateRemark(String qingshuId, String remark) {
        return studentQingshuMapper.updateRemark(qingshuId, remark);
    }

    public int resetScore(String qingshuId, String semester) {
        return studentQingshuMapper.resetScore(qingshuId, semester);
    }

    @SuppressWarnings("unchecked")
    public void importQingshuScore(MultipartFile stuScoreImport) {
        //对导入工具进行字段填充
        IExcelConfig<StudentQingshuExcel> testExcelCofing = new IExcelConfig<StudentQingshuExcel>();
        testExcelCofing.setSheetName("index").setType(StudentQingshuExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
                .addTitle(new ExcelUtil.IExcelTitle("课程名", "courseName"))
                .addTitle(new ExcelUtil.IExcelTitle("平时成绩", "genericScore"))
                .addTitle(new ExcelUtil.IExcelTitle("期末考试成绩", "finalScore"))
                .addTitle(new ExcelUtil.IExcelTitle("总评成绩", "summaryScore"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentQingshuExcel> list = ExcelUtil.importWorkbook(stuScoreImport.getInputStream(), testExcelCofing,
                    stuScoreImport.getOriginalFilename());
            // 遍历插入
            for (StudentQingshuExcel studentQingshuExcel : list) {
                if (!StringUtil.hasValue(studentQingshuExcel.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(studentQingshuExcel.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号码不能为空");
                }
                if (!StringUtil.hasValue(studentQingshuExcel.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(studentQingshuExcel.getSemester())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期不能为空");
                }
                if (!StringUtil.hasValue(studentQingshuExcel.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课程名不能为空");
                }
                if (StringUtil.hasValue(studentQingshuExcel.getSemester())) {
                    if (!isNumber(studentQingshuExcel.getSemester()) ) {
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期填入一个有效的数字");
                    }
                    if((Integer.parseInt(studentQingshuExcel.getSemester()) < 1 || Integer.parseInt(studentQingshuExcel.getSemester()) > 6)){
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期填入数据有误");
                    }
                }
                if (StringUtil.hasValue(studentQingshuExcel.getGenericScore())) {
                    if (!isDouble(studentQingshuExcel.getGenericScore())){
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！平时成绩填入一个有效的数字");
                    }
                }
                if (StringUtil.hasValue(studentQingshuExcel.getFinalScore())) {
                    if (!isDouble(studentQingshuExcel.getFinalScore())){
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！期末考试成绩填入一个有效的数字");
                    }
                }
                if (StringUtil.hasValue(studentQingshuExcel.getSummaryScore())) {
                    if (!isDouble(studentQingshuExcel.getSummaryScore())){
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！总评成绩填入一个有效的数字");
                    }
                }
                index++;
            }
            List<Map<String, Object>> resultList = studentQingshuMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("青书学堂上课跟进以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("id_card") + "-" + map.get("std_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            studentQingshuMapper.initTmpQingshu(list);
            List<Map<String, String>> qingshuCourses = studentQingshuMapper.selectQingshuTmpCourse();
            for (Map<String, String> map : qingshuCourses) {
				map.put("qcId", IDGenerator.generatorId());
			}
            studentQingshuMapper.insert(qingshuCourses);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    private BaseUser getUser() {
        BaseUser user = SessionUtil.getUser();
        if (user.getJtList().contains("FDY")) {
            user.setUserLevel("6");
        }
        if (user.getJtList().contains("400")) {
            user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR") || user.getJtList().contains("CJXJ") || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")) {
            user.setUserLevel("1");
        }

        return user;
    }

    private boolean isNumber(String arg0) {
        try {
            Integer.parseInt(arg0);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isDouble(String arg0) {
        try {
            Double.parseDouble(arg0);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
