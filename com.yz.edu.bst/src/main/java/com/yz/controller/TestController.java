package com.yz.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.enums.ReturnStatus;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentTScoreYZ;
import com.yz.service.BdsCourseService;
import com.yz.service.BdsLearnInfoService;
import com.yz.service.BdsStudentTScoreService;
import com.yz.vo.ReturnModel;


@Controller
public class TestController {
	
	@Autowired
	private BdsLearnInfoService learnService;
	@Autowired
	private BdsCourseService courseService;
	
	@Autowired
	private BdsStudentTScoreService scoreService;
	
	
	@ResponseBody
	@RequestMapping("/testresource")
	public Object getMyResource(@RequestParam(name = "learnId", required = true) String learnId) {
		ReturnModel rqModel=new ReturnModel();
		Object object= courseService.selectCourseResource(learnId);
		rqModel.setBasemodle(object);
		rqModel.setCode(ReturnStatus.Success);
		rqModel.setMsg("成功！");
		return rqModel;
	}
	
	
	@ResponseBody
	@RequestMapping("/testweb")
	public Object getMyResource() {
		ReturnModel rqModel=new ReturnModel();
		IPageInfo<BdStudentTScoreYZ> object= scoreService.GetPreScoringResaulByPage( "", 100, 1);
		rqModel.setBasemodle(object);
		rqModel.setCode(ReturnStatus.Success);
		rqModel.setMsg("成功！");
		return rqModel;
	}
	
	
}
