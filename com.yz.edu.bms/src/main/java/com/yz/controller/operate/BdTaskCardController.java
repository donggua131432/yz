package com.yz.controller.operate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.operate.BdTaskCard;
import com.yz.service.operate.BdTaskCardService;
import com.yz.util.DateUtil;

@Controller
@RequestMapping("/taskCard")
public class BdTaskCardController {

	@Autowired
	private BdTaskCardService tcService;
	
	@RequestMapping("toList")
	@Rule("taskCard:query")
	public String toList() {
		return "operate/taskcard/task-list";
	}
	
	@RequestMapping("list")
	@ResponseBody
	@Rule("taskCard:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdTaskCard taskCard) {
		return tcService.selectTaskCardByPage(start, length, taskCard);
	}
	
	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "taskCard:insert")
	@Rule("taskCard:insert")
	public Object toAdd(Model model) {
		BdTaskCard taskCard = new BdTaskCard();
		model.addAttribute("taskCard", taskCard);
		model.addAttribute("nowDate", DateUtil.getCurrentDate());
		model.addAttribute("exType", "ADD");
		return "operate/taskcard/task-edit";
	}
	
	@RequestMapping("/add")
	@Token(action = Flag.Remove, groupId = "taskCard:insert")
	@Rule("taskCard:insert")
	@Log
	@ResponseBody
	public Object add(HttpServletRequest request, BdTaskCard taskCard) {
		tcService.addTaskCard(taskCard);
		return "SUCCESS";
	}
	
	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "taskCard:update")
	@Rule("taskCard:update")
	public Object toEdit(@RequestParam(name = "taskId", required = true) String taskId, Model model) {
		BdTaskCard taskCard = tcService.selectTaskCard(taskId);
		model.addAttribute("taskCard", taskCard);
		model.addAttribute("exType", "UPDATE");
		return "operate/taskcard/task-edit";
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("taskCard:update")
	@Token(action = Flag.Remove, groupId = "taskCard:update")
	public Object edit(HttpServletRequest request, BdTaskCard taskCard) {
		
		tcService.updateTaskCard(taskCard);
		
		return "success";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("taskCard:delete")
	public Object deleteTask(@RequestParam(name = "taskId", required = true) String taskId) {
		tcService.deleteTask(taskId);
		return "success";
	}
	
	@RequestMapping("/publish")
	@ResponseBody
	@Rule("taskCard:update")
	public Object publish(@RequestParam(name = "taskId", required = true) String taskId) {
		tcService.publishTask(taskId);
		return "success";
	}
	
}
