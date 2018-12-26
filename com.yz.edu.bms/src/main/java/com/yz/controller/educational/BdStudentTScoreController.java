package com.yz.controller.educational;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yz.model.educational.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.common.IPageInfo;
import com.yz.service.educational.BdStudentScoreService;
import com.yz.service.educational.BdStudentTScoreExcelService;
import com.yz.util.Assert;

@RequestMapping("/studentTScore")
@Controller
public class BdStudentTScoreController {

    @Autowired
    private BdStudentScoreService studentScoreService;

    @Autowired
    private BdStudentTScoreExcelService studentScoreExcelService;

    /**
     * 转向期未考试成功页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/tolist")
    public String showTScoreList(HttpServletRequest request) {
        return "/educational/studentScore/studentTScore-list";
    }

    @RequestMapping("/toTemplateExcel")
    public String toTemplateExcel(HttpServletRequest request) {
        return "/educational/studentScore/studentTScore-template-excel";
    }

    @RequestMapping("/toCourseDoc")
    public String toCourseDoc(HttpServletRequest request) {
        return "/educational/studentScore/studentTScore-course-doc";
    }

    /**
     * 查询所有在读学员期未考试成绩
     *
     * @param BdStudentScoreMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/findAllStudentTScore")
    @ResponseBody
    @Rule("studentTScore:query")
    public Object findAllStudentTScore(@RequestParam(value = "start", defaultValue = "0") int start,
                                       @RequestParam(value = "length", defaultValue = "10") int length,
                                       BdStudentTScoreMap studentScoreMap) {
        IPageInfo result = studentScoreService.findAllStudentTScorePage(start, length, studentScoreMap);
        return result;

    }

    /**
     * 编辑期末成绩
     *
     * @param request
     * @param model
     * @param learnId
     * @param idCard
     * @param stdName
     * @return
     */
    @RequestMapping("/edit")
    @Rule("studentTScore:update")
    @Token(action = Flag.Save, groupId = "studentTScore:update")
    public String edit(HttpServletRequest request, Model model, @RequestParam(name = "learnId") String learnId) {
        Assert.hasText(learnId, "参数learnId不能为空");
        model.addAttribute("learninfo", studentScoreService.getBdStudentTScoreLearnInfo(learnId));
        //model.addAttribute("course", studentScoreService.findStudentExamCourse(learnId));
        return "/educational/studentScore/studentTScore-edit";
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/findStudentTScoreBySemester")
    @ResponseBody
    @Rule("studentTScore:update")
    public Object findStudentTScoreBySemester(@RequestParam(name = "learnId") String learnId,
                                              @RequestParam(name = "semester") String semester) {
        List<BdStudentTScore> result = studentScoreService.findStudentTScoreBySemester(learnId, semester);
        return result;

    }

    /**
     * Description: 根据escoreId修改分数
     *
     * @param studentEScore List<BdStudentEScore>集合
     * @throws Exception 抛出一个异常
     * @see com.yz.controller.educational.updateStudentScore Note: Nothing much.
     */
    @RequestMapping("/updateStudentTScore")
    @ResponseBody
    @Rule("studentTScore:update")
    @Token(action = Flag.Remove, groupId = "studentTScore:update")
    public Object updateStudentTScore(BdStudentTScoreReceive scores) {
        studentScoreService.insertStudentTScore(scores);
        return "SUCCESS";
    }

    @RequestMapping("/excelImportByIdCard")
    @Rule("studentTScore:upload")
    public String excelImportByIdCard(HttpServletRequest request) {
        return "/educational/studentScore/studentTScoreIdCard-import";
    }

    @RequestMapping("/excelImportByStdNo")
    @Rule("studentTScore:upload")
    public String excelImportByStdNo(HttpServletRequest request) {
        return "/educational/studentScore/studentTScoreStdno-import";
    }

    @RequestMapping("/uploadByIdCard")
    @ResponseBody
    @Rule("studentTScore:upload")
    public Object uploadByIdCard(@RequestParam(value = "excelTScore", required = false) MultipartFile excelTScore) {
        if (null != excelTScore) {
            studentScoreExcelService.uploadStudentTScore(excelTScore, "idCard");
        }
        return "SUCCESS";
    }

    @RequestMapping("/uploadByStdNo")
    @ResponseBody
    @Rule("studentTScore:upload")
    public Object uploadByStdNo(@RequestParam(value = "excelTScore", required = false) MultipartFile excelTScore) {
        if (null != excelTScore) {
            studentScoreExcelService.uploadStudentTScore(excelTScore, "schoolRoll");
        }
        return "SUCCESS";
    }


    /**
     * 筛选导出Excel
     *
     * @param start
     * @param length
     * @param response
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/queryexport")
    @Rule("studentTScore:export")
    public void queryexport(@RequestParam(value = "start", defaultValue = "1") int start,
                            @RequestParam(value = "length", defaultValue = "10") int length, BdStudentTScoreMap studentScoreMap, HttpServletResponse response) throws UnsupportedEncodingException {
        studentScoreMap.setStdName(new String(studentScoreMap.getStdName().getBytes("ISO-8859-1"), "utf-8"));
        studentScoreService.exportStudentTScore(studentScoreMap, "queryExport", response);

    }

    /**
     * 勾选导出Excel
     *
     * @param start
     * @param length
     * @param response
     */
    @RequestMapping("/checkexport")
    @Rule("studentTScore:export")
    public void checkexport(BdStudentTScoreMap studentScoreMap, HttpServletResponse response) {
        studentScoreService.exportStudentTScore(studentScoreMap, "checkExport", response);

    }

    @RequestMapping("/templateExcelExport")
    @Rule("studentTScore:templateExcelExport")
    public void templateExcelExport(TemplateExcel excel, HttpServletResponse response) {
        studentScoreService.templateExcelExport(excel, response);
    }

    @RequestMapping("/getCourseName")
    @ResponseBody
    public Object getCourseName(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "rows", defaultValue = "10") int rows, String grade, String semester, String pfsnId) {
        return studentScoreService.getCourseName(page, rows, grade, semester, pfsnId);
    }

    @RequestMapping("/templateDocExport")
    @Rule("studentTScore:templateDocExport")
    public void templateDocExport(TemplateDocQuery docQuery, HttpServletResponse response) {
        studentScoreService.templateDocExport(docQuery, response);
    }

    @RequestMapping("/existsScore")
    @ResponseBody
    public Object existsScore(TemplateExcel excel) {
        return studentScoreService.existsScore(excel);
    }
}
