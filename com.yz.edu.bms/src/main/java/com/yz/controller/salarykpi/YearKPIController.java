package com.yz.controller.salarykpi;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.model.salarykpi.StudentDetailQuery;
import com.yz.model.salarykpi.YearKPIQuery;
import com.yz.service.pubquery.TutorshipBookSendService;
import com.yz.service.salarykpi.YearKPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@RequestMapping("/yearkpi")
@Controller
public class YearKPIController {

    @Autowired
    YearKPIService yearKPIService;

    @RequestMapping("/list")
    @Rule("yearkpi:query")
    public String showList(HttpServletRequest request) {
        return "salarykpi/yearkpi/year-kpi-list";
    }

    @RequestMapping("/detail")
    @Rule("yearkpi:detail")
    public String detail(HttpServletRequest request) {
        return "salarykpi/yearkpi/year-kpi-detail";
    }

    @RequestMapping("/studentDetail")
    @Rule("yearkpi:studentDetail")
    public String studentDetail(HttpServletRequest request) {
        return "salarykpi/yearkpi/year-kpi-student-list";
    }

    @RequestMapping("/findAllYearKPI")
    @Rule("yearkpi:query")
    @ResponseBody
    public Object findAllYearKPI(@RequestParam(value = "start", defaultValue = "1") int start,
                                          @RequestParam(value = "length", defaultValue = "10") int length, YearKPIQuery query) {
        PageHelper.offsetPage(start, length).setRmGroup(false);
        List<Map<String,Object>> resultList = yearKPIService.findAllYearKPI(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/findAllStudentDetail")
    @Rule("yearkpi:studentDetail")
    @ResponseBody
    public Object findAllStudentDetail(@RequestParam(value = "start", defaultValue = "1") int start,
                                 @RequestParam(value = "length", defaultValue = "10") int length, StudentDetailQuery query) {
        PageHelper.offsetPage(start, length);
        List<Map<String,Object>> resultList = yearKPIService.findAllStudentDetail(query);
        return new IPageInfo((Page) resultList);
    }
}
