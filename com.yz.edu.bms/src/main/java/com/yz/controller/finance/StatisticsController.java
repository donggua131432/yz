package com.yz.controller.finance;

import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.finance.statistics.BdExcel;
import com.yz.model.finance.statistics.BdFeeStatisticsQuery;
import com.yz.service.finance.BdFeeStatisticsService;

@Controller
@RequestMapping("/feeStatistics")
public class StatisticsController {

	@Autowired
	private BdFeeStatisticsService statisticsService;

	@RequestMapping("/toStatistics")
	@Rule("feeStatistics:query")
	public String toList() {

		return "finance/statistics/statistics-list";
	}

	@RequestMapping("/export")
	@Rule("feeStatistics:export")
	@ResponseBody
	public Object export(BdFeeStatisticsQuery query, BdExcel excel, HttpServletResponse response)
			throws ParseException {

		String excelId = statisticsService.insertExcelInfo(excel);
		excel.setExcelId(excelId);
		statisticsService.exportFinance(query, response, excel);
		return "SUCCESS";
	}

	@RequestMapping("/sCampusJson")
	@ResponseBody
	@Rule("feeStatistics:query")
	public Object sCampusJson() {

		return statisticsService.sCampusJson();
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("feeStatistics:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length) {

		return statisticsService.selectExcelByPage(start, length);
	}

}
