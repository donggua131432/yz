package com.yz.controller.gk;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.IPageInfo;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentCityAffrimGkQuery;
import com.yz.service.gk.BdStudentCityAffirmGKService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author juliet
 * @version 1.0
 */
@Controller
@RequestMapping("/studentCityAffirmGK")
public class BdStudentCityAffirmGKController {
    @Autowired
    BdStudentCityAffirmGKService cityAffirmGKService;

    @RequestMapping("/tolist")
    @Rule("cityAffirmGK:query")
    public String showList(HttpServletRequest request) {
        return "gk/exam/exam_city_affirm_list";
    }

    @RequestMapping("/findStudentCityAffirmGK")
	@Rule("cityAffirmGK:query")
	@ResponseBody
	public Object findStudentCityAffirmGKList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentCityAffrimGkQuery query) {
		PageHelper.offsetPage(start, length);
		return cityAffirmGKService.findStudentCityAffirmGKList(query);
	}
    
    @RequestMapping("/getExamCityForGK")
	@ResponseBody
	public Object getExamCityForGK(@RequestParam(name = "status") String status, @RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
    	PageHelper.startPage(page, rows);
    	List<Map<String, String>> resultList = cityAffirmGKService.getExamCityForGK(status);
    	if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}
    
    @RequestMapping("/toEdit")
	@Rule("cityAffirmGK:edit")
	public String toEdit(HttpServletRequest request,
			Model model) {
    	StudentCityAffirmGKInfo cityAffrimInfo  = new StudentCityAffirmGKInfo();
		String id = RequestUtil.getString("id");
		Assert.hasText(id, "参数名称不能为空");
		cityAffrimInfo = cityAffirmGKService.getGkStuCityAffirmById(id);
		model.addAttribute("dataInfo", cityAffrimInfo);
		return "gk/exam/exam_city_affirm_edit";
	}
    
    @RequestMapping("/exportStuCityAffirmInfo")
	@Rule("cityAffirmGK:export")
	public void exportStuCityAffirmInfo(StudentCityAffrimGkQuery query, HttpServletResponse response) {
    	cityAffirmGKService.exportStuCityAffirmInfo(query, response);
	}

	@RequestMapping("/editStuCityAffirmReason")
	@Rule("cityAffirmGK:edit")
	@ResponseBody
	public Object updateReason(String affirmId,String reason, String isExam,HttpServletResponse response) {
		cityAffirmGKService.updateReason(affirmId, reason,isExam);
		return "SUCCESS";
	}

	@RequestMapping("/resetTask")
	@Rule("cityAffirmGK:reset")
	@ResponseBody
	public Object resetTask(String affirmId, String taskId, String learnId, HttpServletResponse response) {
		cityAffirmGKService.resetTask(affirmId,taskId,learnId);
		return "SUCCESS";
	}
	
	
	@RequestMapping("/toStatistics")
    @Rule("cityAffirmGK:statistics")
    public String toStatistics(Model model,String eyId) {
		model.addAttribute("eyId", eyId);
        return "gk/exam/exam_city_affirm_statistics";
    }
	
	@RequestMapping("/statistics")
    @Rule("cityAffirmGK:statistics")
	@ResponseBody
	public Object statistics(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,String eyId,String statGroup) {
		PageHelper.offsetPage(start, length);
		return cityAffirmGKService.getGkCityStatisticsInfo(eyId,statGroup);
	}
	
}
