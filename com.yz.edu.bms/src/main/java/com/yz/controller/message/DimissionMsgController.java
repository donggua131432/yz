package com.yz.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.message.GwDimissionMsg;
import com.yz.service.message.DimissionMsgService;

@Controller
@RequestMapping("/dimissionMsg")
public class DimissionMsgController {

	@Autowired
	private DimissionMsgService msgService;

	@RequestMapping("/toDimissionMsgList")
	@Rule("dimissionMsg:query")
	public String toDimissionMsgList() {

		return "message/dimission/dimission-msg-list";
	}
	
	@RequestMapping("/toSendMsg")
	@Rule("dimissionMsg:insert")
	@Token(groupId = "dimissionMsg:insert", action = Flag.Save)
	public String toSendMsg() {

		return "message/dimission/dimission-msg-add";
	}

	@RequestMapping("/dimissionMsgList")
	@ResponseBody
	@Rule("dimissionMsg:query")
	public Object recordList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwDimissionMsg query) {
		return msgService.getDimissionMsgByPage(start, length, query);
	}

	@RequestMapping("/dimissionEmp")
	@ResponseBody
	@Rule("dimissionMsg:insert")
	public Object dimissionEmp(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "rows", defaultValue = "10") int rows, String sName) {
		return msgService.getDimissionEmpByPage(page, rows, sName);
	}

	@RequestMapping("/empStudents")
	@ResponseBody
	@Rule("dimissionMsg:insert")
	public Object empStudents(@RequestParam(name = "empId", required = true) String empId) {
		return msgService.getEmpStudents(empId);
	}

	@RequestMapping("/dimissionMsg")
	@ResponseBody
	@Rule("dimissionMsg:insert")
	@Token(groupId = "dimissionMsg:insert", action = Flag.Remove)
	public Object dimissionMsg(@RequestParam(name = "empId", required = true) String empId,
			@RequestParam(name = "msgType", required = true) String msgType) {
		msgService.sendDimissionMsg(empId, msgType);
		return "SUCCESS";
	}

}
