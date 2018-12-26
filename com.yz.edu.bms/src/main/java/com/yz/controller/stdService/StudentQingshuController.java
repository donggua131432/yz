package com.yz.controller.stdService;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.stdService.StudentCollectInfo;
import com.yz.model.stdService.StudentCollectQuery;
import com.yz.model.stdService.StudentQingshuQuery;
import com.yz.service.stdService.StudentCollectService;
import com.yz.service.stdService.StudentQingshuService;
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
     * 学服任务--青书学堂上课跟进
 *
 * @author jyt
 */
@Controller
@RequestMapping("/qingshu")
public class StudentQingshuController {

    @Autowired
    private StudentQingshuService studentQingshuService;

    @RequestMapping("/toList")
    @Rule("qingshu:query")
    public String toList(Model model, HttpServletRequest request) {
        return "/stdservice/qingshu/student_qingshu_list";
    }

    @RequestMapping("/toReset")
    @Rule("qingshu:resetScore")
    public String toReset(Model model, HttpServletRequest request) {
        return "/stdservice/qingshu/student_qingshu_reset";
    }

    @RequestMapping("/findAllQingshuList")
    @Rule("qingshu:query")
    @ResponseBody
    public Object findAllCollectList(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "length", defaultValue = "10") int length, StudentQingshuQuery query) {
        PageHelper.offsetPage(start, length);
        return studentQingshuService.findAllQingshuList(query);
    }


    @RequestMapping("/addRemark")
    @Rule("qingshu:addRemark")
    @ResponseBody
    public Object addRemark(@RequestParam(name = "addRemark", required = true) String addRemark,
                            @RequestParam(name = "qingshuId", required = true) String qingshuId) {
        studentQingshuService.updateRemark(qingshuId, addRemark);
        return "success";
    }

    @RequestMapping("/resetScore")
    @Rule("qingshu:resetScore")
    @ResponseBody
    public Object reset(@RequestParam(name = "semester", required = true) String semester,
                            @RequestParam(name = "qingshuId", required = true) String qingshuId) {
        studentQingshuService.resetScore(qingshuId, semester);
        return "success";
    }

    @RequestMapping("/scoreImport")
    @Rule("qingshu:import")
    public String paperImport(HttpServletRequest request) {
        return "stdservice/qingshu/student_qingshu-import";
    }

    @RequestMapping("/uploadScore")
    @Rule("qingshu:import")
    @ResponseBody
    public Object uploadPaper(
            @RequestParam(value = "scoreImport", required = false) MultipartFile stuScoreImport) {
        studentQingshuService.importQingshuScore(stuScoreImport);
        return "SUCCESS";
    }

}
