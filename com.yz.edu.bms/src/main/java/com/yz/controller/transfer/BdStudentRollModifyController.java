package com.yz.controller.transfer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yz.model.transfer.*;
import com.yz.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.CheckConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.IPageInfo;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.service.transfer.BdStudentRollModifyService;
import com.yz.util.Assert;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/studentRoll")
public class BdStudentRollModifyController {
	@Autowired
	private BdStudentModifyService studentModifyService;
	@Autowired
	private BdStudentRollModifyService studentRollModifyService;

	@RequestMapping("/list")
	@Rule("studentRoll:find")
	public String showList(HttpServletRequest request) {
		return "transfer/roll/student-roll-list";
	}

	@RequestMapping("/listNew")
	@Rule("studentRollNew:find")
	public String showListNew(HttpServletRequest request) {
		return "transfer/roll/student-roll-list-new";
	}

	@RequestMapping("/listToAudit")
	@Rule("studentRoll:findAudit")
	public String listToAudit(HttpServletRequest request) {
		return "transfer/roll/student-roll-audit-list";
	}

	@RequestMapping("/listToAuditNew")
	@Rule("studentRollNew:findAudit")
	public String listToAuditNew(HttpServletRequest request) {
		return "transfer/roll/student-roll-audit-list-new";
	}

	@RequestMapping("/add")
	@Rule("studentRoll:insert")
	public String edit(HttpServletRequest request) {
		return "transfer/roll/student-roll-add";
	}

	@RequestMapping("/addNew")
	@Rule("studentRollNew:insert")
	public String insert(HttpServletRequest request) {
		return "transfer/roll/student-roll-add-new";
	}

	/*
	 * @Description:查询学员（针对【在读学员】阶段学员）
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("studentRoll:find")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		String[] stauts = { StudentConstants.STD_STAGE_STUDYING };
		List<Map<String, String>> studentInfoMap = studentRollModifyService.findStudentInfo(sName, stauts, page, rows);
		if (null == studentInfoMap) {
			return "不存在!";
		}
		return new IPageInfo((Page) studentInfoMap);
	}

	@RequestMapping("/findStudentInfoNew")
	@ResponseBody
	@Rule("studentRollNew:find")
	public Object findStudentInfoMew(String sName, @RequestParam(value = "rows", defaultValue = "5") int rows,
								  @RequestParam(value = "page", defaultValue = "1") int page) {
		List<Map<String, String>> studentInfoMap = studentRollModifyService.findStudentInfoNew(sName, page, rows);
		if (null == studentInfoMap) {
			return "不存在!";
		}
		return new IPageInfo((Page) studentInfoMap);
	}

	/*
	 * @Description:新增异动申请
	 */
	@RequestMapping("/insertStudentRollModify")
	@ResponseBody
	@Rule("studentRoll:insert")
	public Object insertStudentRollModify(BdStudentModify studentModify) {
		// 审核类型为异动修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY);
		// 变更类型为异动修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_ROLL_2);
		studentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
		// 插入
		studentRollModifyService.insertStudentRollModify(studentModify);
		return "SUCCESS";
	}

	/*
	 * @Description:新增异动申请
	 */
	@RequestMapping("/insertStudentRollModifyNew")
	@ResponseBody
	@Rule("studentRollNew:insert")
	public Object insertStudentRollModifyNew(BdStudentModify studentModify) {
		// 审核类型为异动修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY_NEW);
		// 变更类型为异动修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_ROLL_NEW);
		studentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
		// 插入
		studentRollModifyService.insertStudentRollModifyNew(studentModify);
		return "SUCCESS";
	}

	/*
	 * @Description:根据学员ID和年级查询 学员报读信息
	 */
	@RequestMapping("/findStudentEnrollInfo")
	@ResponseBody
	@Rule("studentRoll:find")
	public Object findStudentEnrollInfo(String learnId) {
		Map<String, String> studentEnrollInfoMap = studentModifyService.findStudentEnrollInfo(learnId);
		return studentEnrollInfoMap;
	}

	/**
	 * Description: 根据条件查询所有信息异动数据
	 * 
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentRollModify")
	@ResponseBody
	@Rule("studentRoll:find")
	public Object findStudentRollModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		PageHelper.offsetPage(start, length);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentRollModify(studentModifyMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/findStudentRollModifyNew")
	@ResponseBody
	@Rule("studentRollNew:find")
	public Object findStudentRollModifyNew(@RequestParam(value = "start", defaultValue = "1") int start,
										@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyQuery query) {
		PageHelper.offsetPage(start, length);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentRollModifyNew(query);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 根据条件和角色查询异动数据
	 * 
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentAuditRollModify")
	@ResponseBody
	@Rule("studentRoll:findAudit")
	public Object findStudentAuditRollModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		PageHelper.offsetPage(start, length);
		/*
		 * BaseUser user = SessionUtil.getUser(); String checkOrder =
		 * studentOutService.getCheckOrder(TransferConstants.
		 * CHECK_TYPE_SCHOOLROLL_MODIFY,user.getJtId());
		 */
		String checkOrder = TransferConstants.CHECK_RECORD_ORDER_THIRD;
		studentModifyMap.setCheckOrder(checkOrder);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentAuditRollModify(studentModifyMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/findStudentAuditRollModifyNew")
	@ResponseBody
	@Rule("studentRollNew:findAudit")
	public Object findStudentAuditRollModifyNew(@RequestParam(value = "start", defaultValue = "1") int start,
											 @RequestParam(value = "length", defaultValue = "10") int length, StudentModifyQuery query) {
		PageHelper.offsetPage(start, length);
		String checkOrder = TransferConstants.CHECK_RECORD_ORDER_THIRD;
		query.setCheckOrder(checkOrder);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentRollModifyNew(query);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 根据条件和角色查询异动数据
	 * 
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentAccpetRollModify")
	@ResponseBody
	@Rule("studentRoll:findAudit")
	public Object findStudentAccpetRollModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		PageHelper.offsetPage(start, length);
		/*
		 * BaseUser user = SessionUtil.getUser(); String checkOrder =
		 * studentOutService.getCheckOrder(TransferConstants.
		 * CHECK_TYPE_SCHOOLROLL_MODIFY,user.getJtId());
		 */
		String checkOrder = TransferConstants.CHECK_RECORD_ORDER_SECOND;
		studentModifyMap.setCheckOrder(checkOrder);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentAuditRollModify(studentModifyMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/findStudentAccpetRollModifyNew")
	@ResponseBody
	@Rule("studentRollNew:findAccpet")
	public Object findStudentAccpetRollModifyNew(@RequestParam(value = "start", defaultValue = "1") int start,
											  @RequestParam(value = "length", defaultValue = "10") int length, StudentModifyQuery query) {
		PageHelper.offsetPage(start, length);
		String checkOrder = TransferConstants.CHECK_RECORD_ORDER_SECOND;
		query.setCheckOrder(checkOrder);
		List<StudentRollModifyMap> resultList = studentRollModifyService.findStudentRollModifyNew(query);
		return new IPageInfo((Page) resultList);
	}

	/*
	 * @Description:根据修改记录id查询修改信息
	 */
	@RequestMapping("/edit")
	@Rule("studentRoll:findAudit")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentRollModify studentModify = studentRollModifyService.findStudentRollModifyById(modifyId);
		model.addAttribute("exType", exType);
		model.addAttribute("studentRollModifyInfo", studentModify);
		return "transfer/roll/student-roll-edit";
	}

	@RequestMapping("/show")
	@Rule("studentRollNew:show")
	public String show(HttpServletRequest request, Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentRollModify studentModify = studentRollModifyService.findStudentRollModifyNewById(modifyId);
		for(BdRollCheckRecord check:studentModify.getCheck()){
			if(StringUtil.hasValue(check.getReason())) {
			check.setReason(check.getReason().replace("\n",""));
			}
		}
		studentModify.setCreateTime(studentModify.getCreateTime().replace(".0",""));
		model.addAttribute("studentRollModifyInfo", studentModify);
		return "transfer/roll/student-roll-show";
	}

	/*
	 * @Description:审核
	 */
	@RequestMapping("/editToAudit")
	@Rule("studentRoll:passApproval")
	public String editToAudit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentRollModify studentModify = studentRollModifyService.findStudentRollModifyById(modifyId);
		model.addAttribute("exType", exType);
		model.addAttribute("studentRollModifyInfo", studentModify);
		return "transfer/roll/student-roll-audit-edit";
	}

	@RequestMapping("/audit")
	@Rule("studentRollNew:audit")
	public String audit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
							  Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentRollModify studentModify = studentRollModifyService.findStudentRollModifyNewById(modifyId);
		for(BdRollCheckRecord check:studentModify.getCheck()){
			if(StringUtil.hasValue(check.getReason())) {
				check.setReason(check.getReason().replace("\n",""));
			}
		}
		studentModify.setCreateTime(studentModify.getCreateTime().replace(".0",""));
		model.addAttribute("exType", exType);
		model.addAttribute("studentRollModifyInfo", studentModify);
		return "transfer/roll/student-roll-audit";
	}

	/*
	 * @Description:执行异动
	 */
	@RequestMapping("/passApproval")
	@ResponseBody
	@Rule("studentRoll:passApproval")
	public Object passApproval(String checkStatus, String reason, String modifyId) {
		studentRollModifyService.passApproval(checkStatus, reason, modifyId,
				TransferConstants.CHECK_RECORD_ORDER_THIRD);
		return "SUCCESS";
	}

	@RequestMapping("/passApprovalNew")
	@ResponseBody
	@Rule("studentRollNew:audit")
	public Object passApprovalNew(String checkStatus, String reason, String modifyId,String checkOrder) {
		studentRollModifyService.passApprovalNew(checkStatus, reason, modifyId, checkOrder);
		return "SUCCESS";
	}

	@RequestMapping("/batchPassApprovalNew")
	@ResponseBody
	@Rule("studentRollNew:audit")
	public Object batchPassApprovalNew(@RequestParam(name = "idArray[]", required = true) String[] idArray,String checkStatus,String checkOrder) {
		studentRollModifyService.batchPassApprovalNew(idArray, checkStatus, checkOrder);
		return "SUCCESS";
	}
}
