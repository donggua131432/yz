package com.yz.controller.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.statistics.BdExamCourseStatisticsInfo;
import com.yz.model.statistics.ExamCourseStatQuery;
import com.yz.model.statistics.PaperPrintStatInfo;
import com.yz.model.statistics.PaperPrintStatQuery;
import com.yz.service.statistics.ExamCourseStatService;

/**
 * 考试科目统计
 * @author lx
 * @date 2017年11月16日 下午5:18:09
 */
@RequestMapping("/courseStat")
@Controller
public class ExamCourseStatController
{
	@Autowired
	private ExamCourseStatService courseStatService;
	
	/**
	 * 跳转到统计页面
	 * @return
	 */
	@RequestMapping("/toCourseStat")
	@Rule("courseStat:query")
	public String toListPage() {
		return "statistics/exam_course_stat";
	}

	/**
	 * 分页获取 考试科目统计信息
	 * @param req
	 * @param resp
	 * @param start
	 * @param length
	 * @param statQuery
	 * @return
	 */
	@RequestMapping("/courseStatList")
	@ResponseBody
	@Rule("courseStat:query")
	public Object courseStatList(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,ExamCourseStatQuery statQuery) {
		IPageInfo<BdExamCourseStatisticsInfo> result = courseStatService.queryExamCourseStatPage(start, length,statQuery);
		return result;
	}
	/**
	 * 导出科目统计信息
	 * @param statQuery
	 * @param response
	 */
	@RequestMapping("/export")
	@Rule("courseStat:export")
	public void export(ExamCourseStatQuery statQuery,HttpServletResponse response) {
		courseStatService.exportCourseStat(statQuery,response);
	}
	
	/**
	 * 试卷印刷统计
	 * @param req
	 * @param resp
	 * @param start
	 * @param length
	 * @param statQuery
	 * @return
	 */
	@RequestMapping("/examStatList")
	@ResponseBody
	@Rule("courseStat:query")
	public Object examStatList(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,PaperPrintStatQuery statQuery) {
		IPageInfo<PaperPrintStatInfo> result = courseStatService.queryPaperPrintStatPage(start, length,statQuery);
		return result;
	}
	/**
	 * 试卷印刷统计导出
	 * @param statQuery
	 * @param response
	 */
	@RequestMapping("/exportExam")
	@Rule("courseStat:export")
	public void exportExam(PaperPrintStatQuery statQuery,HttpServletResponse response) {
		courseStatService.exportPaperPrintStat(statQuery,response);
	}
}
