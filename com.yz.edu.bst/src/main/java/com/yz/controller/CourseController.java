package com.yz.controller;


import com.yz.core.annotation.Rule;
import com.yz.core.util.SessionUtil;
import com.yz.service.BdsCourseService;

import com.yz.util.DateUtil;
import com.yz.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CourseController {

    @Autowired
    BdsCourseService courseService;

    @RequestMapping("/course")
    @Rule
    public String toLogin() {
        return "myCourse";
    }

    @RequestMapping("/live")
    @Rule
    public String live() {
        return "live";
    }

    @RequestMapping("/allCourseLive")
    @Rule
    public String allCourseLive() {
        return "allCourseLive";
    }

    @RequestMapping("/presentTerm")
    @ResponseBody
    @Rule
    public Object presentTerm(@RequestParam(name = "learnId", required = true) String learnId) throws IOException {
        return courseService.presentTerm(learnId);
    }

    @RequestMapping("/mySyllabus")
    @ResponseBody
    @Rule
    public Object login(@RequestParam(name = "learnId") String learnId, @RequestParam(name = "term") String term) throws IOException {
        return courseService.mySyllabus(learnId, term);
    }

    @RequestMapping("/getCourseLive")
    @ResponseBody
    @Rule
    public Object getCourseLive(@RequestParam(name = "learnId") String learnId, @RequestParam(name = "term") String term) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        LoginUser user = SessionUtil.getUser();
        map.put("userName",user.getUsername());
        map.put("userId",user.getUserId());
        map.put("live",courseService.getCourseLive(learnId, term));
        return map;
    }

    @RequestMapping("/getUnvsAllCourseLive")
    @ResponseBody
    @Rule
    public Object getUnvsAllCourseLive() throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        LoginUser user = SessionUtil.getUser();
        map.put("now", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        map.put("userId",user.getStdId());
        map.put("live",courseService.getUnvsAllCourseLive(user.getStdId()));
        return map;
    }

    @RequestMapping("/allowAllLive")
    @ResponseBody
    @Rule
    public Object allowAllLive() throws IOException {
        return courseService.allowAllLive();
    }

}
