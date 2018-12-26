package com.yz.controller.message;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.constants.MessageConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.message.GwMessageQuery;
import com.yz.model.message.GwMsgStudentQuery;
import com.yz.model.message.GwMsgTemplate;
import com.yz.model.message.GwRecordQuery;
import com.yz.service.message.MsgPublishService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/msgPub")
public class MsgPublishController {

	@Autowired
	private MsgPublishService msgService;

	@RequestMapping("toRegList")
	@Rule("msgReg:query")
	public String toRegList() {

		return "message/regist/regist-list";
	}

	@RequestMapping("toRecordList")
	@Rule("msgRecord:query")
	public String toRecordList() {

		return "message/record/record-list";
	}

	@RequestMapping("toMsgView")
	@Rule("msgPub:query")
	public String toMsgView(@RequestParam(name = "mtpId", required = true) String mtpId, Model model) {
		model.addAttribute("msg", msgService.selectMtpById(mtpId));
		model.addAttribute("exType", "UPDATE");
		return "message/send/msg-view";
	}

	/**
	 * 分配学员-加载学员的信息
	 * 
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping(value = "/stuList", method = RequestMethod.GET)
	@ResponseBody
	@Rule("msgPub:update")
	public Object stuList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwMsgStudentQuery studentQuery) {
		/*
		 * if (null != stdStages && stdStages.length > 0) {
		 * studentQuery.setStdStages(stdStages); }
		 */
		return msgService.queryMsgStudentInfo(start, length, studentQuery);
	}

	@RequestMapping("/addAllMtpStu")
	@ResponseBody
	@Rule("msgPub:update")
	public Object addAllMtpStu(GwMsgStudentQuery studentQuery, String mtpId) {
		msgService.addAllReceiver(studentQuery, mtpId);
		return "SUCCESS";
	}
	

	@RequestMapping("/delAllMtpStu")
	@ResponseBody
	@Rule("msgPub:update")
	public Object delAllMtpStu(GwMsgStudentQuery studentQuery, String mtpId) {
		msgService.delAllReceiver(studentQuery, mtpId);
		return "SUCCESS";
	}

	@RequestMapping("/addMtpStu")
	@ResponseBody
	@Rule("msgPub:update")
	public Object addMtpStu(@RequestParam(name = "learnIds[]", required = true) String[] learnIds, String mtpId) {
		msgService.addReceiver(learnIds, mtpId);
		return "SUCCESS";
	}

	@RequestMapping("/delStu")
	@ResponseBody
	@Rule("msgPub:update")
	public Object delStu(@RequestParam(name = "learnIds[]", required = true) String[] learnIds, String mtpId) {
		msgService.delMtpReceiver(learnIds, mtpId);
		return "SUCCESS";
	}

	@RequestMapping("toCheckList")
	@Rule("msgCheck:query")
	public String toCheckList() {

		return "message/check/check-list";
	}

	@RequestMapping("toSendList")
	@Rule("msgSend:query")
	public String toSendList() {

		return "message/send/send-list";
	}

	@RequestMapping("toStdList")
	@Rule("msgPub:query")
	public String toStdList(String mtpId, String exType, Model model) {
		model.addAttribute("mtpId", mtpId);
		model.addAttribute("exType", exType);
		return "message/regist/student-list";
	}

	@RequestMapping("recordList")
	@ResponseBody
	@Rule("msgRecord:query")
	public Object recordList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwRecordQuery record) {
		return msgService.getMsgRecordByPage(start, length, record);
	}

	@RequestMapping("stdList")
	@ResponseBody
	@Rule("msgPub:query")
	public Object stdList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, String mtpId) {
		return msgService.getMsgStdByPage(start, length, mtpId);
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("msgPub:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwMessageQuery msg, String status) {
		List<String> statuses = new ArrayList<String>();
		if (MessageConstants.MESSAGE_STATUS_UNSUBMIT.equals(status)) {
			statuses.add(MessageConstants.MESSAGE_STATUS_UNSUBMIT);
			statuses.add(MessageConstants.MESSAGE_STATUS_REJECT);
		} else if (MessageConstants.MESSAGE_STATUS_UNCHECK.equals(status)) {
			statuses.add(MessageConstants.MESSAGE_STATUS_UNCHECK);
		} else if (MessageConstants.MESSAGE_STATUS_UNSEND.equals(status)) {
			statuses.add(MessageConstants.MESSAGE_STATUS_UNSEND);
			statuses.add(MessageConstants.MESSAGE_STATUS_SENDED);
		}
		msg.setStatuses(statuses);
		return msgService.getMsgByPage(start, length, msg);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "msgPub:insert")
	@Rule("msgPub:insert")
	public Object toAdd(Model model) {
		GwMsgTemplate msg = new GwMsgTemplate();
		model.addAttribute("msg", msg);
		model.addAttribute("exType", "ADD");
		return "message/regist/msg-edit";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "msgPub:update")
	@Rule("msgPub:update")
	public Object toEdit(@RequestParam(name = "mtpId", required = true) String mtpId, Model model) {
		model.addAttribute("msg", msgService.selectMtpById(mtpId));
		model.addAttribute("exType", "UPDATE");
		return "message/regist/msg-edit";
	}

	@RequestMapping("/toCheck")
	@Token(action = Flag.Save, groupId = "msgPub:update")
	@Rule("msgPub:update")
	public Object toCheck(@RequestParam(name = "mtpId", required = true) String mtpId, Model model) {
		model.addAttribute("msg", msgService.selectMtpById(mtpId));
		model.addAttribute("exType", "UPDATE");
		return "message/check/msg-edit";
	}

	@RequestMapping("/check")
	@ResponseBody
	@Log
	@Rule("msgPub:update")
	@Token(action = Flag.Remove, groupId = "msgPub:update")
	public Object check(String mtpId) {

		msgService.checkMsg(mtpId);
		return "success";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("msgPub:insert")
	@Token(action = Flag.Remove, groupId = "msgPub:insert")
	public Object add(GwMsgTemplate msg) {

		msgService.insertMsgPublish(msg);
		return "success";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("msgPub:update")
	@Token(action = Flag.Remove, groupId = "msgPub:update")
	public Object edit(GwMsgTemplate msg) {

		msgService.updateMsgPublish(msg);

		return "success";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("msgPub:delete")
	public Object delete(@RequestParam(name = "mtpId", required = true) String mtpId) {
		msgService.deleteMsg(mtpId);
		return "success";
	}

	@RequestMapping("/submit")
	@ResponseBody
	@Log
	@Rule("msgPub:submit")
	public Object submit(@RequestParam(name = "mtpId", required = true) String mtpId) {
		msgService.submitMsg(mtpId);
		return "success";
	}

	@RequestMapping("/reject")
	@ResponseBody
	@Log
	@Rule("msgPub:update")
	public Object reject(@RequestParam(name = "mtpId", required = true) String mtpId,
			@RequestParam(name = "remark", required = true) String remark) {
		msgService.rejectMsg(mtpId,remark);
		return "success";
	}

	@RequestMapping("/submits")
	@ResponseBody
	@Log
	@Rule("msgPub:submit")
	public Object submits(@RequestParam(name = "mtpIds[]", required = true) String[] mtpIds) {
		msgService.submitMsgs(mtpIds);
		return "success";
	}

	@RequestMapping("/addStd")
	@ResponseBody
	@Log
	@Rule("msgPub:update")
	public Object submits(MultipartFile excelRegist, String mtpId) {
		Assert.hasText(mtpId, "消息ID不能为空");
		msgService.importStudent(excelRegist, mtpId);
		return "success";
	}

	@RequestMapping("/toImport")
	@Rule("msgPub:update")
	public Object toImport(@RequestParam(name = "mtpId", required = true) String mtpId, Model model) {
		model.addAttribute("mtpId", mtpId);
		return "message/regist/std-import";
	}

	@RequestMapping("/excuteTask")
	@ResponseBody
	@Log
	@Rule("msgPub:update")
	public Object excuteTask(String mtpId) {
		Assert.hasText(mtpId, "消息ID不能为空");
		//msgService.excuteTask(mtpId);
		return "success";
	}

	/**
	 * 导出
	 * @param record
	 * @param response
	 */
	@RequestMapping("export")
	@Rule("msgRecord:export")
	public void export(GwRecordQuery record,HttpServletResponse response) {
		msgService.getMsgRecordForExport(record,response);
	}
}
