package com.yz.controller.statistics;

import com.yz.core.security.annotation.Rule;
import com.yz.model.oa.OaCampusInfo;
import com.yz.model.statistics.StudentStatQuery;
import com.yz.service.statistics.StudentStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @描述: 学员统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 11:54
 * @版本号: V1.0
 */
@RequestMapping("/studentStat")
@Controller
public class StudentStatController {
    private static final Logger log = LoggerFactory.getLogger(StudentStatController.class);

    @Autowired
    StudentStatService studentStatService;

    @RequestMapping("/toStudentStat")
    @Rule("studentStat")
    public String toListPage() {
        return "/statistics/student_stat";
    }

    /**
     * 分页获取统计数据
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "/studentStatList", method = RequestMethod.POST)
    @ResponseBody
    @Rule("studentStat:query")
    public Object studentStatList(@RequestParam(name = "start", defaultValue = "0") int start,
                             @RequestParam(name = "length", defaultValue = "10") int length, StudentStatQuery studentStatQuery) {
        String[] stdStageArray =  studentStatQuery.getStdStage().split(",");
        studentStatQuery.setStdStageArray(stdStageArray);
        return studentStatService.queryStudentStatPage(start,length,studentStatQuery);
    }


    @RequestMapping(value = "/studentStatTotal", method = RequestMethod.POST)
    @ResponseBody
    @Rule("studentStat:query")
    public Object queryStudentStatTotal(StudentStatQuery studentStatQuery) {
        String[] stdStageArray =  studentStatQuery.getStdStage().split(",");
        studentStatQuery.setStdStageArray(stdStageArray);
        return studentStatService.queryStudentStatTotal(studentStatQuery);
    }



}
