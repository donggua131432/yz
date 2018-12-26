package com.yz.controller.finance;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.model.finance.review.BdFeeReviewQuery;
import com.yz.model.finance.statistics.BdExcel;
import com.yz.service.finance.BdFeeReviewService;
import com.yz.service.finance.BdFeeStatisticsService;

@Controller
@RequestMapping("/feeReview")
public class FeeReviewController {

	@Autowired
	private BdFeeReviewService reviewService;
	
	@Autowired
	private BdFeeStatisticsService statisticsService;

	@RequestMapping("/toList")
	@Rule("feeReview:query")
	public String toList() {

		return "finance/feeReview/feeReview-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("feeReview:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdFeeReviewQuery feeReview) {

		return reviewService.queryFeeReviewByPage(start, length, feeReview);
	}

	@RequestMapping("/count")
	@ResponseBody
	@Rule("feeReview:query")
	public Object count(BdFeeReviewQuery feeReview) {

		return reviewService.countAmount(feeReview);
	}

	@RequestMapping("/reviewFee")
	@ResponseBody
	@Rule("feeReview:update")
	@Log
	public Object reviewFee(@RequestParam(name = "serialNo", required = true) String serialNo) {
		String[] serialNos = { serialNo };
		return reviewService.reviewSerial(serialNos);
	}

	@RequestMapping("/reviewFees")
	@ResponseBody
	@Rule("feeReview:update")
	@Log
	public Object reviewFees(@RequestParam(name = "serialNos[]", required = true) String[] serialNos) {
		return reviewService.reviewSerial(serialNos);
	}

	@RequestMapping("/rollBackReviews")
	@ResponseBody
	@Rule("feeReview:rollback")
	@Log
	public Object rollBackReviews(@RequestParam(name = "serialNos[]", required = true) String[] serialNos) {
		return reviewService.rollBackSerialReviews(serialNos);
	}

	@RequestMapping("/rollBackReview")
	@ResponseBody
	@Rule("feeReview:rollback")
	@Log
	public Object rollBackReview(@RequestParam(name = "serialNo", required = true) String serialNo) {
		String[] serialNos = { serialNo };
		return reviewService.rollBackSerialReviews(serialNos);
	}

	@RequestMapping("/export")
	@Rule("feeReview:export")
	@ResponseBody
	public Object export(BdFeeReviewQuery feeReview, BdExcel excel, HttpServletResponse response) {

		String excelId = statisticsService.insertExcelInfo(excel);
		excel.setExcelId(excelId);
		reviewService.exportSerial(feeReview, response,excel);
		return "SUCCESS";
	}

}
