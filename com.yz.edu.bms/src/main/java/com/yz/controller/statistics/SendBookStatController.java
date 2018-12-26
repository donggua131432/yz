package com.yz.controller.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.statistics.SendBookStatQuery;
import com.yz.service.statistics.SendBookStatService;

/**
 * 学员订书统计
 * @author lx
 * @date 2017年11月23日 下午5:34:52
 */
@RequestMapping("/bookStat")
@Controller
public class SendBookStatController
{
	@Autowired
	private SendBookStatService bookStatService;
	
	@RequestMapping("/toBookStat")
	@Rule("bookStat:query")
	public String toListPage() {
		return "statistics/send_book_stat";
	}
	@RequestMapping("/bookStatList")
	@ResponseBody
	@Rule("bookStat:query")
	public Object bookStatList(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,
            SendBookStatQuery statQuery) {
		String[] stdStageArray = statQuery.getStdStage().split(",");
		statQuery.setStdStageArray(stdStageArray);
		return bookStatService.querySendBookStatByPage(start, length, statQuery);
	}
	
	@RequestMapping("/export")
	@Rule("bookStat:export")
	public void export(SendBookStatQuery statQuery,HttpServletResponse response) {
		String[] stdStageArray = statQuery.getStdStage().split(",");
		statQuery.setStdStageArray(stdStageArray);
		bookStatService.exportBookStat(statQuery,response);
	}
	
	@RequestMapping("/okOrder")
	@ResponseBody
	@Rule("bookStat:okOrder")
	public Object okOrder(@RequestParam(name = "statGroup", required = true) String statGroup) {
		bookStatService.okOrder(statGroup);
		return "success";
	}
}
