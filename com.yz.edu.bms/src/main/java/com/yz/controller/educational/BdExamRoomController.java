package com.yz.controller.educational;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.educational.BdExamPlace;
import com.yz.service.educational.BdExamRoomService;

@Controller
@RequestMapping("/examRoom")
public class BdExamRoomController {

	@Autowired
	private BdExamRoomService examService;

	@RequestMapping("/toList")
	@Rule("examRoom:query")
	public Object toList() {
		return "educational/examRoom/room-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("examRoom:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdExamPlace query) {
		return examService.selectExamRoomByPage(start, length, query);
	}
	
	@RequestMapping("/sEpName")
	@ResponseBody
	public Object sEpName(SelectQueryInfo queryInfo) {
		return examService.getEmName(queryInfo);
	}

	@RequestMapping("/toAdd")
	@Rule("examRoom:insert")
	@Token(groupId = "examRoom:insert", action = Flag.Save)
	public String toAdd(Model model) {
		BdExamPlace p = new BdExamPlace();
		model.addAttribute("exType", "ADD");
		model.addAttribute("room", p);
		return "educational/examRoom/room-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Rule("examRoom:insert")
	@Log
	@Token(groupId = "examRoom:insert", action = Flag.Remove)
	public Object add(BdExamPlace exam) {
		examService.insertExamPlace(exam);
		return "SUCCESS";
	}

	@RequestMapping("/toUpdate")
	@Rule("examRoom:update")
	@Token(groupId = "examRoom:update", action = Flag.Save)
	public String toUpdate(Model model, @RequestParam(name = "epId") String epId) {
		BdExamPlace p = examService.selectExamRoomById(epId);
		model.addAttribute("room", p);
		model.addAttribute("exType", "UPDATE");
		return "educational/examRoom/room-edit";
	}

	@RequestMapping("/update")
	@Rule("examRoom:update")
	@ResponseBody
	@Token(groupId = "examRoom:update", action = Flag.Remove)
	@Log
	public Object update(BdExamPlace examPlace) {
		examService.updateBdExamPlace(examPlace);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@Rule("examRoom:update")
	@ResponseBody
	public Object block(@RequestParam(name = "epId") String epId) {
		examService.updateStatus(epId, GlobalConstants.STATUS_BLOCK);
		return "SUCCESS";
	}

	@RequestMapping("/start")
	@Rule("examRoom:update")
	@ResponseBody
	public Object start(@RequestParam(name = "epId") String epId) {
		examService.updateStatus(epId, GlobalConstants.STATUS_START);
		return "SUCCESS";
	}

	@RequestMapping("/blocks")
	@Rule("examRoom:update")
	@ResponseBody
	public Object blocks(@RequestParam(name = "epIds[]") String[] epIds) {
		examService.updateStatus(epIds, GlobalConstants.STATUS_BLOCK);
		return "SUCCESS";
	}

	@RequestMapping("/starts")
	@Rule("examRoom:update")
	@ResponseBody
	public Object starts(@RequestParam(name = "epIds[]") String[] epIds) {
		examService.updateStatus(epIds, GlobalConstants.STATUS_START);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@Rule("examRoom:delete")
	@ResponseBody
	public Object delete(@RequestParam(name = "epId") String epId) {
		examService.deleteExamRoom(epId);
		return "SUCCESS";
	}

	@RequestMapping("/deletes")
	@Rule("examRoom:delete")
	@ResponseBody
	public Object deletes(@RequestParam(name = "epIds[]") String[] epIds) {
		for (String epId : epIds) {
			examService.deleteExamRoom(epId);
		}
		return "SUCCESS";
	}

	@RequestMapping("/validateEpCode")
	@ResponseBody
	@Log
	@Rule("examRoom:update")
	public Object validateItemCode(@RequestParam(name = "epCode", required = true) String epCode,
			@RequestParam(name = "exType", required = true) String exType) {
		if ("ADD".equals(exType)) {
			if (null != examService.selectExamRoomByCode(epCode)) {
				return false;
			}
		} else if ("UPDATE".equals(exType)) {
			BdExamPlace p = examService.selectExamRoomByCode(epCode);
			if (null == p) {
				return true;
			}
			if (epCode.equals(p.getEpCode())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

}
