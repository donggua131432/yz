package com.yz.controller.statistics;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.stdService.StudentStudyingQuery;
import com.yz.service.statistics.StdStudyFeeStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @描述: 在读学员应缴费用统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 11:54
 * @版本号: V1.0
 */
@RequestMapping("/stdStudyFeeStat")
@Controller
public class StdStudyFeeStatController {
    private static final Logger log = LoggerFactory.getLogger(StdStudyFeeStatController.class);

    @Autowired
    StdStudyFeeStatService stdStudyFeeStatService;

    @RequestMapping("/toStdStudyFeeStat")
    @Rule("stdStudyFeeStat")
    public String toListPage() {
        return "statistics/student_study_fee_stat";
    }

    /**
     * 分页获取统计数据
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "/stdStudyFeeStatList", method = RequestMethod.POST)
    @ResponseBody
    @Rule("stdStudyFeeStat:query")
    public Object studentStatList(@RequestParam(name = "start", defaultValue = "0") int start,
                                  @RequestParam(name = "length", defaultValue = "10") int length, StudentStudyingQuery studentStudyingQuery) {
        return stdStudyFeeStatService.queryStdStudyFeeStatPage(start,length,studentStudyingQuery);
    }



}
