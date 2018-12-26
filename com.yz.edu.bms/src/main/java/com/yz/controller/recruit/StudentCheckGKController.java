package com.yz.controller.recruit;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.service.recruit.StudentCheckGKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @描述: 国开资料审核
 * @作者: DuKai
 * @创建时间: 2018/5/8 19:03
 * @版本号: V1.0
 */
@RequestMapping("/gkCheckInfo")
@Controller
public class StudentCheckGKController {


    @Autowired
    private StudentCheckGKService studentCheckGKService;

    @RequestMapping("/toGkStudentList")
    @Rule("gkCheckInfo")
    public String toGkStudentList(Model model) {
        BaseUser user = SessionUtil.getUser();
        List<String> jtList = user.getJtList();
        model.addAttribute("jtList",jtList);
        return "/recruit/student/student-check-gk-list";
    }

    @RequestMapping("/findAllGkStudentList")
    @Rule("gkCheckInfo")
    @ResponseBody
    public Object findAllGkStudentList(StudentAnnexCheckQuery queryInfo) {
        return studentCheckGKService.findAllStudentGKList(queryInfo);
    }

    @RequestMapping("/batchChecks")
    @ResponseBody
    public Object batchChecks(@RequestParam(name = "learnIds[]") String[] learnIds) {
        boolean flag = studentCheckGKService.batchChecks(learnIds);
        if(flag == true){
            return "SUCCESS";
        }else{
            return "FAIL";
        }
    }

    @RequestMapping("/initGkCheckRecord")
    @ResponseBody
    public Object initGkCheckRecord(String learnId) {
        return studentCheckGKService.initGkCheckRecord(learnId);
    }
}
