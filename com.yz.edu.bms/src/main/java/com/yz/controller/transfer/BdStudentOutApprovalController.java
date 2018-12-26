package com.yz.controller.transfer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.ApprovalMap;
import com.yz.model.transfer.BdStudentOut;
import com.yz.model.transfer.BdStudentOutMap;
import com.yz.redis.RedisService;
import com.yz.service.finance.BdStdPayFeeService;
import com.yz.service.transfer.BdStudentOutService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/studentOutApproval")
public class BdStudentOutApprovalController {
	private static final Logger log = LoggerFactory.getLogger(BdStudentOutApprovalController.class);
	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdStdPayFeeService payService;

	@RequestMapping("/list")
	@Rule("studentOutApproval:find")
	public String showList(HttpServletRequest request, Model model) {

		BaseUser user = SessionUtil.getUser();

		if (GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
			model.addAttribute("isSuper", GlobalConstants.TRUE);
		} else {
			List<BmsFunc> fncList = user.getFuncs();

			if (fncList != null) {
				for (BmsFunc func : fncList) {
					String code = func.getFuncCode();

					switch (code) {
					case "studentOutApproval:findDirector":
						model.addAttribute("isXJ", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findFinancial":
						model.addAttribute("isCW", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findSchoolManaged":
						model.addAttribute("isXB", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findSenate":
						model.addAttribute("isJW", GlobalConstants.TRUE);
						break;
					}

				}
			}
		}

		return "transfer/check/out-check-list";
	}

	@RequestMapping("/editToDirector")
	@Rule("studentOutApproval:checkDirector")
	@Token(action = Flag.Save, groupId = "studentOutApproval:checkDirector")
	public String editToDirector(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "undo", required = true) String undo, HttpServletRequest request, Model model) {
		String outId = RequestUtil.getString("outId");
		Assert.hasText(outId, "参数名称不能为空");
		String[] arr = outId.split("@");
		// 查询提交人
		BdStudentOut oldBso = studentOutService.selectBdStudentOutBuId(arr[2]);
		model.addAttribute("exType", exType);
		model.addAttribute("grade", arr[1]);
		model.addAttribute("learnId", arr[0]);
		model.addAttribute("outId", arr[2]);
		model.addAttribute("stdName", arr[3]);
		model.addAttribute("reason", arr[4]);
		Map<String, String> stdInfo = studentOutService.selectStdInfo(arr[0]);
		model.addAttribute("stdId", stdInfo.get("stdId"));
		model.addAttribute("idCard", stdInfo.get("idCard"));
		model.addAttribute("scholarship", stdInfo.get("scholarship"));
		model.addAttribute("inclusionStatus", stdInfo.get("inclusionStatus"));
		model.addAttribute("postUser", oldBso.getCreateUser());
		model.addAttribute("postTime", oldBso.getCreateTime());
		model.addAttribute("remark", oldBso.getRemark());
		model.addAttribute("fileUrl", oldBso.getFileUrl());
		model.addAttribute("fileName", oldBso.getFileName());
		if ("true".equals(undo)) {
			return "transfer/check/out-check-undo";
		}
		return "transfer/check/out-check-edit";
	}

	@RequestMapping("/editToFinancial")
	@Rule("studentOutApproval:checkFinancial")
	@Token(action = Flag.Save, groupId = "studentOutApproval:checkFinancial")
	public String editToFinancial(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "undo", required = true) String undo, HttpServletRequest request, Model model) {
		String outId = RequestUtil.getString("outId");
		Assert.hasText(outId, "参数名称不能为空");
		String[] arr = outId.split("@");
		// 查询提交人
		BdStudentOut oldBso = studentOutService.selectBdStudentOutBuId(arr[2]);
		Object o = payService.selectPayableInfo(arr[0], FinanceConstants.ORDER_STATUS_REFUNDING);
		model.addAttribute("stdFee", o);
		model.addAttribute("exType", exType);
		model.addAttribute("grade", arr[1]);
		model.addAttribute("learnId", arr[0]);
		model.addAttribute("outId", arr[2]);
		model.addAttribute("stdName", arr[3]);
		model.addAttribute("reason", arr[4]);
		Map<String, String> stdInfo = studentOutService.selectStdInfo(arr[0]);
		model.addAttribute("stdId", stdInfo.get("stdId"));
		model.addAttribute("idCard", stdInfo.get("idCard"));
		model.addAttribute("scholarship", stdInfo.get("scholarship"));
		model.addAttribute("inclusionStatus", stdInfo.get("inclusionStatus"));
		model.addAttribute("postUser", oldBso.getCreateUser());
		model.addAttribute("postTime", oldBso.getCreateTime());
		model.addAttribute("remark", oldBso.getRemark());
		model.addAttribute("fileUrl", oldBso.getFileUrl());
		model.addAttribute("fileName", oldBso.getFileName());
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(arr[2], "2");
		if (opareRecord != null && opareRecord.get(0) != null) {
			model.addAttribute("directorUser", opareRecord.get(0).get("empName"));
			model.addAttribute("directorTime", opareRecord.get(0).get("updateTime"));
		}
		if ("true".equals(undo)) {
			return "transfer/check/out-check-financial-undo";
		}
		return "transfer/check/out-check-financial-edit";
	}

	@RequestMapping("/editToSchoolManaged")
	@Rule("studentOutApproval:checkSchoolManaged")
	@Token(action = Flag.Save, groupId = "studentOutApproval:checkSchoolManaged")
	public String editToSchoolManaged(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "undo", required = true) String undo, HttpServletRequest request, Model model) {
		String outId = RequestUtil.getString("outId");
		Assert.hasText(outId, "参数名称不能为空");
		String[] arr = outId.split("@");
		// 查询提交人
		BdStudentOut oldBso = studentOutService.selectBdStudentOutBuId(arr[2]);
		Object o = payService.selectPayableInfo(arr[0], FinanceConstants.ORDER_STATUS_REFUNDING);
		model.addAttribute("stdFee", o);
		model.addAttribute("exType", exType);
		model.addAttribute("grade", arr[1]);
		model.addAttribute("learnId", arr[0]);
		model.addAttribute("outId", arr[2]);
		model.addAttribute("stdName", arr[3]);
		model.addAttribute("reason", arr[4]);
		Map<String, String> stdInfo = studentOutService.selectStdInfo(arr[0]);
		model.addAttribute("stdId", stdInfo.get("stdId"));
		model.addAttribute("idCard", stdInfo.get("idCard"));
		model.addAttribute("scholarship", stdInfo.get("scholarship"));
		model.addAttribute("inclusionStatus", stdInfo.get("inclusionStatus"));
		model.addAttribute("postUser", oldBso.getCreateUser());
		model.addAttribute("postTime", oldBso.getCreateTime());
		model.addAttribute("remark", oldBso.getRemark());
		model.addAttribute("fileUrl", oldBso.getFileUrl());
		model.addAttribute("fileName", oldBso.getFileName());
		model.addAttribute("financialRemark", oldBso.getFinancial_remark());
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(arr[2], "3");
		if (null != opareRecord && opareRecord.size() > 1) {
			if (opareRecord.get(0) != null) {
				model.addAttribute("directorUser", opareRecord.get(0).get("empName"));
				model.addAttribute("directorTime", opareRecord.get(0).get("updateTime"));
			}
			if (opareRecord.get(1) != null) {
				model.addAttribute("financialUser", opareRecord.get(1).get("empName"));
				model.addAttribute("financialTime", opareRecord.get(1).get("updateTime"));
			}
		}
		if ("true".equals(undo)) {
			return "transfer/check/out-check-schmanage-undo";
		}
		return "transfer/check/out-check-schmanage-edit";
	}

	/**
	 * 退学学员-->查看审批状态
	 * 
	 * @param exType
	 * @param undo
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/lookToSenate")
	@Rule("studentOut:find")
	public String lookToSenate(HttpServletRequest request, Model model) {
		String outId = RequestUtil.getString("outId");
		Assert.hasText(outId, "参数名称不能为空");
		String[] arr = outId.split("@");
		// 查询提交人
		BdStudentOut oldBso = studentOutService.selectBdStudentOutBuId(arr[2]);
		Object o = payService.selectPayableInfo(arr[0], null);
		model.addAttribute("stdFee", o);
		model.addAttribute("exType", "look");
		model.addAttribute("grade", arr[1]);
		model.addAttribute("learnId", arr[0]);
		model.addAttribute("outId", arr[2]);
		model.addAttribute("stdName", arr[3]);
		model.addAttribute("reason", arr[4]);
		Map<String, String> stdInfo = studentOutService.selectStdInfo(arr[0]);
		model.addAttribute("stdId", stdInfo.get("stdId"));
		model.addAttribute("idCard", stdInfo.get("idCard"));
		model.addAttribute("scholarship", stdInfo.get("scholarship"));
		model.addAttribute("inclusionStatus", stdInfo.get("inclusionStatus"));

		model.addAttribute("postUser", oldBso.getCreateUser());
		model.addAttribute("postTime", oldBso.getCreateTime());
		model.addAttribute("remark", oldBso.getRemark());
		model.addAttribute("fileUrl", oldBso.getFileUrl());
		model.addAttribute("fileName", oldBso.getFileName());
		model.addAttribute("financialRemark", oldBso.getFinancial_remark());
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(arr[2], "4");
		if (null != opareRecord && opareRecord.size() > 0) {
			model.addAttribute("directorUser", opareRecord.get(0) == null ? "" : opareRecord.get(0).get("empName"));
			model.addAttribute("directorTime", opareRecord.get(0) == null ? "" : opareRecord.get(0).get("updateTime"));
			if (opareRecord.get(1) != null) {
				model.addAttribute("financialUser", opareRecord.get(1) == null ? "" : opareRecord.get(1).get("empName"));
				model.addAttribute("financialTime", opareRecord.get(1) == null ? "" : opareRecord.get(1).get("updateTime"));
			}
			if (opareRecord.get(2) != null) {
				model.addAttribute("schoolManagedUser",
						opareRecord.get(2) == null ? "" : opareRecord.get(2).get("empName"));
				model.addAttribute("schoolManagedTime",
						opareRecord.get(2) == null ? "" : opareRecord.get(2).get("updateTime"));
			}
		}
		return "transfer/check/out-check-approsenate-look";
	}

	@RequestMapping("/editToSenate")
	@Rule("studentOutApproval:checkSenate")
	@Token(action = Flag.Save, groupId = "studentOutApproval:checkSenate")
	public String editToSenate(@RequestParam(name = "exType", required = true) String exType,
			HttpServletRequest request, Model model) {
		//加入到redis，防止重复提交
		RedisService.getRedisService().sadd("studentOutApproval", SessionUtil.getUser().getUserId());
		String outId = RequestUtil.getString("outId");
		Assert.hasText(outId, "参数名称不能为空");
		String[] arr = outId.split("@");
		// 查询提交人
		BdStudentOut oldBso = studentOutService.selectBdStudentOutBuId(arr[2]);
		Object o = payService.selectPayableInfo(arr[0], FinanceConstants.ORDER_STATUS_REFUNDING);
		model.addAttribute("stdFee", o);
		model.addAttribute("exType", exType);
		model.addAttribute("grade", arr[1]);
		model.addAttribute("learnId", arr[0]);
		model.addAttribute("outId", arr[2]);
		model.addAttribute("stdName", arr[3]);
		model.addAttribute("reason", arr[4]);
		Map<String, String> stdInfo = studentOutService.selectStdInfo(arr[0]);
		model.addAttribute("stdId", stdInfo.get("stdId"));
		model.addAttribute("idCard", stdInfo.get("idCard"));
		model.addAttribute("scholarship", stdInfo.get("scholarship"));
		model.addAttribute("inclusionStatus", stdInfo.get("inclusionStatus"));

		model.addAttribute("postUser", oldBso.getCreateUser());
		model.addAttribute("postTime", oldBso.getCreateTime());
		model.addAttribute("remark", oldBso.getRemark());
		model.addAttribute("fileUrl", oldBso.getFileUrl());
		model.addAttribute("fileName", oldBso.getFileName());
		model.addAttribute("financialRemark", oldBso.getFinancial_remark());
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(arr[2], "4");
		if (null != opareRecord && opareRecord.size() > 2) {
			model.addAttribute("directorUser", opareRecord.get(0).get("empName"));
			model.addAttribute("directorTime", opareRecord.get(0).get("updateTime"));
			model.addAttribute("FinancialUser", opareRecord.get(1).get("empName"));
			model.addAttribute("FinancialTime", opareRecord.get(1).get("updateTime"));
			model.addAttribute("schoolManagedUser", opareRecord.get(2).get("empName"));
			model.addAttribute("schoolManagedTime", opareRecord.get(2).get("updateTime"));
		}
		return "transfer/check/out-check-approsenate-edit";
	}

	@RequestMapping("/findStudentOutInfo")
	@ResponseBody
	@Rule("studentOutApproval:find")
	public Object findStudentOutInfo(String learnId) {
		return studentOutService.findStudentInfoById(learnId);
	}

	/**
	 * Description: 根据当前审批权限返回需要审批的数据（校监/主任审批 阶段：1;审核类型：2,4）
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see BdStudentOutApprovalController Note: Nothing much.
	 */
	@RequestMapping("/findDirectorApproval")
	@ResponseBody
	@Rule("studentOutApproval:findDirector")
	public Object findDirectorApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentOutMap studentOutMap) {
		PageHelper.offsetPage(start, length);
		List<BdStudentOutMap> resultList = studentOutService.findDirectorApproval(studentOutMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/passDirectorApproval")
	@ResponseBody
	@Rule("studentOutApproval:passDirector")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkDirector")
	public Object passDirectorApproval(String outId, String checkStatus, String reason, String learnId) {
		studentOutService.passDirectorApproval(outId, checkStatus, reason, learnId);
		return "SUCCESS";
	}

	@RequestMapping("/undoDirectorApproval")
	@ResponseBody
	@Rule("studentOutApproval:passDirector")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkDirector")
	public Object undoDirectorApproval(String outId, String reason) {
		studentOutService.undoDirectorApproval(outId, reason);
		return "SUCCESS";
	}

	/**
	 * Description: 根据当前审批权限返回需要审批的数据（财务 阶段：2;审核类型：2,4）
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see BdStudentOutApprovalController Note: Nothing much.
	 */
	@RequestMapping("/findFinancialApproval")
	@ResponseBody
	@Rule("studentOutApproval:findFinancial")
	public Object findFinancialApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentOutMap studentOutMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> resultList = studentOutService.findFinancialApproval(studentOutMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/passFinancialApproval")
	@ResponseBody
	@Rule("studentOutApproval:passFinancial")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkFinancial")
	public Object passFinancialApproval(ApprovalMap approvalMap) {
		studentOutService.passFinancialApproval(approvalMap);
		return "SUCCESS";
	}

	@RequestMapping("/undoFinancialApproval")
	@ResponseBody
	@Rule("studentOutApproval:passFinancial")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkFinancial")
	public Object undoFinancialApproval(String outId, String reason) {
		studentOutService.undoFinancialApproval(outId, reason);
		return "SUCCESS";
	}

	/**
	 * Description: 根据当前审批权限返回需要审批的数据（校办 阶段：3;审核类型：2,4）
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see BdStudentOutApprovalController Note: Nothing much.
	 */
	@RequestMapping("/findSchoolManagedApproval")
	@ResponseBody
	@Rule("studentOutApproval:findSchoolManaged")
	public Object findSchoolManagedApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentOutMap studentOutMap) {
		PageHelper.offsetPage(start, length);
		List<BdStudentOutMap> resultList = studentOutService.findSchoolManagedApproval(studentOutMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/passSchoolManagedApproval")
	@ResponseBody
	@Rule("studentOutApproval:passSchoolManaged")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkSchoolManaged")
	public Object passSchoolManagedApproval(ApprovalMap approvalMap) {
		studentOutService.passSchoolManagedApproval(approvalMap);
		return "SUCCESS";
	}

	@RequestMapping("/undoSchoolManagedApproval")
	@ResponseBody
	@Rule("studentOutApproval:passSchoolManaged")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkSchoolManaged")
	public Object undoSchoolManagedApproval(String outId, String reason) {
		studentOutService.undoSchoolManagedApproval(outId, reason);
		return "SUCCESS";
	}

	/**
	 * Description: 根据当前审批权限返回需要审批的数据（教务 阶段：4;审核类型：4）
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see BdStudentOutApprovalController Note: Nothing much.
	 */
	@RequestMapping("/findSenateApproval")
	@ResponseBody
	@Rule("studentOutApproval:findSenate")
	public Object findSenateApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentOutMap studentOutMap) {
		PageHelper.offsetPage(start, length);
		List<BdStudentOutMap> resultList = studentOutService.findSenateApproval(studentOutMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/passSenateApproval")
	@ResponseBody
	@Rule("studentOutApproval:passSenate")
	@Token(action = Flag.Remove, groupId = "studentOutApproval:checkSenate")
	public Object passSenateApproval(String outId, String checkStatus, String reason, String learnId) {
		long count= RedisService.getRedisService().srem("studentOutApproval", SessionUtil.getUser().getUserId());
		if(count==0){
			return "已提交审核，请勿重复提交!";
		}
		studentOutService.passSenateApproval(outId, checkStatus, reason, learnId);
		return "SUCCESS";
	}
}
