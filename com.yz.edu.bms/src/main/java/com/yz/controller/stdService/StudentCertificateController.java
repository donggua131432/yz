package com.yz.controller.stdService;

import com.aliyun.oss.model.ObjectListing;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.stdService.StudentCertificateInfo;
import com.yz.model.stdService.StudentCertificateQuery;
import com.yz.model.stdService.StudentQingshuQuery;
import com.yz.service.stdService.StudentCertificateService;
import com.yz.service.stdService.StudentQingshuService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 学服任务--学员证明申请/审核
 *
 * @author jyt
 */
@Controller
@RequestMapping("/certificate")
public class StudentCertificateController {

    @Autowired
    private StudentCertificateService studentCertificateService;

    @RequestMapping("/toList")
    @Rule("certificate:query")
    public String toList(Model model, HttpServletRequest request) {
        return "/stdservice/certificate/student_certificate_list";
    }

    @RequestMapping("/toListZS")
    @Rule("certificateZS:query")
    public String toListZS(Model model, HttpServletRequest request) {
        return "/recruit/certificate/student_certificate_list";
    }

    @RequestMapping("/printView")
    @Rule("certificate:getStudyInfo")
    public String printView(Model model, HttpServletRequest request) {
        return "/stdservice/certificate/student_certificate_print";
    }

    @RequestMapping("/printViewZS")
    @Rule("certificateZS:getStudyInfo")
    public String printViewZS(Model model, HttpServletRequest request) {
        return "/recruit/certificate/student_certificate_print";
    }

    @RequestMapping("/toCheckList")
    @Rule("certificate:queryCheck")
    public String toCheckList(Model model, HttpServletRequest request) {
        return "/stdservice/certificate/student_certificate_check_list";
    }

    @RequestMapping("/toCheckListZS")
    @Rule("certificateZS:queryCheck")
    public String toCheckListZS(Model model, HttpServletRequest request) {
        return "/recruit/certificate/student_certificate_check_list";
    }

    @RequestMapping("/toEdit")
    public String toEdit(String certId, Model model, HttpServletRequest request) {
        Assert.hasText(certId, "参数名称不能为空");
        StudentCertificateInfo studentCertificateInfo = studentCertificateService.getCertificateInfoById(certId);
        model.addAttribute("dataInfo", studentCertificateInfo);
        return "/stdservice/certificate/student_certificate_edit";
    }

    @RequestMapping("/view")
    @Rule("certificate:query")
    public String view(String certId, Model model, HttpServletRequest request) {
        Assert.hasText(certId, "参数名称不能为空");
        StudentCertificateInfo studentCertificateInfo = studentCertificateService.getCertificateInfoById(certId);
        model.addAttribute("dataInfo", studentCertificateInfo);
        return "/stdservice/certificate/student_certificate_view";
    }

    @RequestMapping("/viewZS")
    @Rule("certificateZS:query")
    public String viewZS(String certId, Model model, HttpServletRequest request) {
        Assert.hasText(certId, "参数名称不能为空");
        StudentCertificateInfo studentCertificateInfo = studentCertificateService.getCertificateInfoById(certId);
        model.addAttribute("dataInfo", studentCertificateInfo);
        return "/stdservice/certificate/student_certificate_view";
    }

    @RequestMapping("/findAllList")
    @Rule("certificate:query")
    @ResponseBody
    public Object findAllList(@RequestParam(name = "start", defaultValue = "0") int start,
                              @RequestParam(name = "length", defaultValue = "10") int length, StudentCertificateQuery query) {
        PageHelper.offsetPage(start, length);
        query.setQueryType("xf");
        return studentCertificateService.findAllList(query);
    }

    @RequestMapping("/findAllListZS")
    @Rule("certificateZS:query")
    @ResponseBody
    public Object findAllListZS(@RequestParam(name = "start", defaultValue = "0") int start,
                              @RequestParam(name = "length", defaultValue = "10") int length, StudentCertificateQuery query) {
        PageHelper.offsetPage(start, length);
        query.setQueryType("zs");
        return studentCertificateService.findAllList(query);
    }

    @RequestMapping("/findAllCheckList")
    @Rule("certificate:queryCheck")
    @ResponseBody
    public Object findAllCheckList(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "length", defaultValue = "10") int length, StudentCertificateQuery query) {
        PageHelper.offsetPage(start, length);
        query.setCheckStatus("1");
        query.setQueryType("xf");
        return studentCertificateService.findAllList(query);
    }

    @RequestMapping("/findAllCheckListZS")
    @Rule("certificateZS:queryCheck")
    @ResponseBody
    public Object findAllCheckListZS(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "length", defaultValue = "10") int length, StudentCertificateQuery query) {
        PageHelper.offsetPage(start, length);
        query.setCheckStatus("1");
        query.setQueryType("zs");
        return studentCertificateService.findAllList(query);
    }

    @RequestMapping("/deliver")
    @Rule("certificate:deliver")
    @ResponseBody
    public Object updateExpressNo(@RequestParam(name = "addExpressNo", required = true) String addExpressNo,
                                  @RequestParam(name = "certId", required = true) String certId) {
        studentCertificateService.updateExpressNo(certId, addExpressNo);
        return "success";
    }

    @RequestMapping("/deliverZS")
    @Rule("certificateZS:deliver")
    @ResponseBody
    public Object updateExpressNoZS(@RequestParam(name = "addExpressNo", required = true) String addExpressNo,
                                  @RequestParam(name = "certId", required = true) String certId) {
        studentCertificateService.updateExpressNo(certId, addExpressNo);
        return "success";
    }

    @RequestMapping("/certificateImport")
    @Rule("certificate:import")
    public String certificateImport(HttpServletRequest request,Model model) {
        model.addAttribute("type","xf");
        return "stdservice/certificate/student_certificate-import";
    }

    @RequestMapping("/certificateImportZS")
    @Rule("certificateZS:import")
    public String certificateImportZS(HttpServletRequest request,Model model) {
        model.addAttribute("type","zs");
        return "stdservice/certificate/student_certificate-import";
    }

    @RequestMapping("/upload")
    @Rule("certificate:import")
    @ResponseBody
    public Object uploadCertificate(
            @RequestParam(value = "importFile", required = false) MultipartFile importFile) {
        studentCertificateService.importCertificate(importFile);
        return "SUCCESS";
    }

    @RequestMapping("/uploadZS")
    @Rule("certificateZS:import")
    @ResponseBody
    public Object uploadCertificateZS(
            @RequestParam(value = "importFile", required = false) MultipartFile importFile) {
        studentCertificateService.importCertificate(importFile);
        return "SUCCESS";
    }

    @RequestMapping("/exportCertificateInfo")
    @Rule("certificate:export")
    public void exportCertificateInfo(StudentCertificateQuery query, HttpServletResponse response) {
        query.setQueryType("xf");
        studentCertificateService.exportCertificateInfo(query, response);
    }

    @RequestMapping("/exportCertificateInfoZS")
    @Rule("certificateZS:export")
    public void exportCertificateInfoZS(StudentCertificateQuery query, HttpServletResponse response) {
        query.setQueryType("zs");
        studentCertificateService.exportCertificateInfo(query, response);
    }

    @RequestMapping("/fdyCheck")
    @Rule("certificate:fdyCheck")
    @ResponseBody
    public Object fdyCheck(@RequestParam(name = "checkStatus", required = true) String checkStatus,
                           @RequestParam(name = "certId", required = true) String certId, String reason, String applyType,String learnId) {
        studentCertificateService.check(checkStatus, reason, certId, "1", applyType,learnId);
        return "success";
    }

    @RequestMapping("/fdyCheckZS")
    @Rule("certificateZS:fdyCheck")
    @ResponseBody
    public Object fdyCheckZS(@RequestParam(name = "checkStatus", required = true) String checkStatus,
                           @RequestParam(name = "certId", required = true) String certId, String reason, String applyType,String learnId) {
        studentCertificateService.check(checkStatus, reason, certId, "1", applyType,learnId);
        return "success";
    }

    @RequestMapping("/finalCheck")
    @Rule("certificate:finalCheck")
    @ResponseBody
    public Object finalCheck(@RequestParam(name = "checkStatus", required = true) String checkStatus,
                             @RequestParam(name = "certId", required = true) String certId, String reason, String applyType,String learnId) {
        studentCertificateService.check(checkStatus, reason, certId, "2", applyType,learnId);
        return "success";
    }

    @RequestMapping("/finalCheckZS")
    @Rule("certificateZS:finalCheck")
    @ResponseBody
    public Object finalCheckZS(@RequestParam(name = "checkStatus", required = true) String checkStatus,
                             @RequestParam(name = "certId", required = true) String certId, String reason, String applyType,String learnId) {
        studentCertificateService.check(checkStatus, reason, certId, "2", applyType,learnId);
        return "success";
    }

    @RequestMapping("/getStudyInfo")
    @Rule("certificate:getStudyInfo")
    @ResponseBody
    public Object getStudyInfo(String learnId) {
        return studentCertificateService.getStudyInfo(learnId);
    }

    @RequestMapping("/getStudyInfoZS")
    @Rule("certificateZS:getStudyInfo")
    @ResponseBody
    public Object getStudyInfoZS(String learnId) {
        return studentCertificateService.getStudyInfo(learnId);
    }

}
