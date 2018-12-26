package com.yz.edu.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.service.TraceSearchService;

@Controller
@RequestMapping("/trace")
public class TraceController {

	private Logger logger = LoggerFactory.getLogger(TraceController.class);

	@Autowired
	private TraceSearchService traceSearchService;

	@RequestMapping("/search")
	@ResponseBody
	public Object info(@RequestParam(name = "traceId") String traceId) {
		logger.info("traceId:{}",traceId);
		return traceSearchService.search(traceId);
	}

}
