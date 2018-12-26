package com.yz.controller.stdService;

import com.yz.edu.paging.common.PageHelper; 
import com.yz.core.security.annotation.Rule; 
import com.yz.model.stdService.StudentCollectInfo;
import com.yz.model.stdService.StudentCollectQuery; 
import com.yz.service.stdService.StudentCollectService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

/**
     * 学服任务--新生学籍资料收集
 *
 * @author jyt
 */
@Controller
@RequestMapping("/collect")
public class StudentCollectController {

    @Autowired
    private StudentCollectService studentCollectService;

    @RequestMapping("/toList")
    @Rule("collect:query")
    public String toList(Model model, HttpServletRequest request) {
        return "/stdservice/collect/student_collect_list";
    }

    @RequestMapping("/toFeedback")
    @Rule("collect:feedback")
    public String toFeedback(Model model,@RequestParam(name = "id") String ctId) {
        model.addAttribute("collects",studentCollectService.getById(ctId));
        return "/stdservice/collect/student_collect_feedback";
    }

    @RequestMapping("/findAllCollectList")
    @Rule("collect:query")
    @ResponseBody
    public Object findAllCollectList(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "length", defaultValue = "10") int length, StudentCollectQuery query) {
        PageHelper.offsetPage(start, length);
        return studentCollectService.findAllCollectList(query);
    }

    @RequestMapping("/exportCollectInfo")
    @Rule("collect:export")
    public void exportCollectInfo(StudentCollectQuery query, HttpServletResponse response) {
        studentCollectService.exportCollectInfo(query, response);
    }

    @RequestMapping("/addRemark")
    @Rule("collect:addRemark")
    @ResponseBody
    public Object addRemark(@RequestParam(name = "addRemark", required = true) String addRemark,
                            @RequestParam(name = "ctId", required = true) String ctId) {
        studentCollectService.updateRemark(ctId, addRemark);
        return "success";
    }

    @RequestMapping("/feedback")
    @Rule("collect:feedback")
    @ResponseBody
    public Object feedback(StudentCollectInfo studentCollectInfo) {
        studentCollectService.updateCollect(studentCollectInfo);
        return "success";
    }
}
