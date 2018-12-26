package com.yz.controller.refund;

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

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.IPageInfo;
import com.yz.model.refund.BdRefundQuery;
import com.yz.model.refund.BdRefundReponse;
import com.yz.model.transfer.ApprovalMap;
import com.yz.model.transfer.BdStudentOut;
import com.yz.model.transfer.BdStudentOutMap;
import com.yz.service.finance.BdStdPayFeeService;
import com.yz.service.refund.StdRefundService;
import com.yz.service.transfer.BdStudentOutService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/refundCheck")
public class StdRefundCheckController {
	private static final Logger log = LoggerFactory.getLogger(StdRefundCheckController.class);
	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdStdPayFeeService payService;
	@Autowired
	private StdRefundService refundService;

	@RequestMapping("/toList")
	@Rule("refundCheck:query")
	public String showList(HttpServletRequest request,Model model) {
		BaseUser user = SessionUtil.getUser();

		if (GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
			model.addAttribute("isSuper", GlobalConstants.TRUE);
		} else {
			List<BmsFunc> fncList = user.getFuncs();

			if (fncList != null) {
				for (BmsFunc func : fncList) {
					String code = func.getFuncCode();

					switch (code) {
					case "refundCheck:findDirector":
						model.addAttribute("isXJ", GlobalConstants.TRUE);
						break;
					case "refundCheck:findFinancial":
						model.addAttribute("isCW", GlobalConstants.TRUE);
						break;
					case "refundCheck:findPrincipal":
						model.addAttribute("isXB", GlobalConstants.TRUE);
						break;
					}

				}
			}
		}
		
		return "refund/check/refund-check-list";
	}

	@RequestMapping("/editToDirector")
	@Rule("refundCheck:checkDirector")
	@Token(action = Flag.Save, groupId = "refundCheck:check")
	public String editToDirector(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "refundId", required = true) String refundId, Model model) {
		// 查询提交人
		BdRefundReponse refund = refundService.selectRefundInfo(refundId);
		model.addAttribute("exType", exType);
		model.addAttribute("dealType", "DIRECTOR");
		model.addAttribute("refund", refund);
		return "refund/check/refund-check-edit";
	}

	@RequestMapping("/editToFinancial")
	@Rule("refundCheck:checkFinancial")
	@Token(action = Flag.Save, groupId = "refundCheck:check")
	public String editToFinancial(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "refundId", required = true) String refundId, Model model) {
		Assert.hasText(refundId, "参数名称不能为空");

		BdRefundReponse refund = refundService.selectRefundInfo(refundId);

		model.addAttribute("exType", exType);
		model.addAttribute("dealType", "FINANCIAL");
		model.addAttribute("refund", refund);
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(refundId,
				TransferConstants.CHECK_RECORD_ORDER_SECOND);
		if (null != opareRecord) {
			model.addAttribute("directorUser", opareRecord.get(0).get("empName"));
			model.addAttribute("directorTime", opareRecord.get(0).get("updateTime"));
		}
		return "refund/check/refund-check-edit";
	}

	@RequestMapping("/editToPrincipal")
	@Rule("refundCheck:checkPrincipal")
	@Token(action = Flag.Save, groupId = "refundCheck:check")
	public String editToPrincipal(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "refundId", required = true) String refundId, Model model) {
		Assert.hasText(refundId, "参数名称不能为空");
		// 查询提交人
		BdRefundReponse refund = refundService.selectRefundInfo(refundId);

		model.addAttribute("exType", exType);
		model.addAttribute("dealType", "PRINCIPAL");
		model.addAttribute("refund", refund);
		List<Map<String, String>> opareRecord = studentOutService.selectOpareRecord(refundId,
				TransferConstants.CHECK_RECORD_ORDER_THIRD);
		if (null != opareRecord && opareRecord.size() > 1) {
			model.addAttribute("directorUser", opareRecord.get(0).get("empName"));
			model.addAttribute("directorTime", opareRecord.get(0).get("updateTime"));
			model.addAttribute("financialUser", opareRecord.get(1).get("empName"));
			model.addAttribute("financialTime", opareRecord.get(1).get("updateTime"));
		}
		return "refund/check/refund-check-edit";
	}

	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("refundCheck:query")
	public Object findStudentInfo(String learnId) {
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
	 * @see com.yz.controller.transfer.BdStudentOutApprovalController Note:
	 *      Nothing much.
	 */
	@RequestMapping("/findDirectorApproval")
	@ResponseBody
	@Rule("refundCheck:findDirector")
	public Object findDirectorApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdRefundQuery query) {
		Object o = refundService.findDirectorApproval(start, length, query);
		return o;
	}

	@RequestMapping("/passDirectorApproval")
	@ResponseBody
	@Rule("refundCheck:checkDirector")
	@Token(action = Flag.Remove, groupId = "refundCheck:check")
	public Object passDirectorApproval(@RequestParam(name = "refundId", required = true) String refundId,
			@RequestParam(name = "checkStatus", required = true) String checkStatus, String reason) {
		if (TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(checkStatus)) {
			refundService.checkDirector(refundId);
		} else if (TransferConstants.CHECK_RECORD_STATUS_REFUND.equals(checkStatus)) {
			refundService.checkReject(refundId, reason, TransferConstants.CHECK_RECORD_ORDER_FIRST);
		}
		return "SUCCESS";
	}

	@RequestMapping("/undoDirectorApproval")
	@ResponseBody
	@Rule("refundCheck:checkDirector")
	@Token(action = Flag.Remove, groupId = "refundCheck:check")
	public Object undoDirectorApproval(@RequestParam(name = "refundId", required = true) String refundId,
			String reason) {
		refundService.rollBackCheck(refundId, TransferConstants.CHECK_RECORD_ORDER_FIRST);
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
	 * 
	 */
	@RequestMapping("/findFinancialApproval")
	@ResponseBody
	@Rule("refundCheck:findFinancial")
	public Object findFinancialApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdRefundQuery query) {
		Object o = refundService.findFinancialApproval(start, length, query);
		return o;
	}

	@RequestMapping("/passFinancialApproval")
	@ResponseBody
	@Rule("refundCheck:checkFinancial")
	@Token(action = Flag.Remove, groupId = "refundCheck:check")
	public Object passFinancialApproval(@RequestParam(name = "refundId", required = true) String refundId,
			@RequestParam(name = "checkStatus", required = true) String checkStatus, String reason) {

		if (TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(checkStatus)) {
			refundService.checkFinancial(refundId);
		} else if (TransferConstants.CHECK_RECORD_STATUS_REFUND.equals(checkStatus)) {
			refundService.checkReject(refundId, reason, TransferConstants.CHECK_RECORD_ORDER_SECOND);
		}
		return "SUCCESS";
	}

	@RequestMapping("/undoFinancialApproval")
	@ResponseBody
	@Rule("refundCheck:checkFinancial")
	@Token(action = Flag.Remove, groupId = "refundCheck:check")
	public Object undoFinancialApproval(@RequestParam(name = "refundId", required = true) String refundId,
			String reason) {
		refundService.rollBackCheck(refundId, TransferConstants.CHECK_RECORD_ORDER_SECOND);
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
	 * @see com.yz.controller.transfer.BdrefundCheckController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findPrincipalApproval")
	@ResponseBody
	@Rule("refundCheck:findPrincipal")
	public Object findPrincipalApproval(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdRefundQuery query) {
		Object o = refundService.findPrincipalApproval(start, length, query);
		return o;
	}

	@RequestMapping("/passPrincipalApproval")
	@ResponseBody
	@Rule("refundCheck:checkPrincipal")
	@Token(action = Flag.Remove, groupId = "refundCheck:check")
	public Object passPrincipalApproval(@RequestParam(name = "refundId", required = true) String refundId,
			@RequestParam(name = "checkStatus", required = true) String checkStatus, String reason) {

		if (TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(checkStatus)) {
			refundService.checkPricipal(refundId);
		} else if (TransferConstants.CHECK_RECORD_STATUS_REFUND.equals(checkStatus)) {
			refundService.checkReject(refundId, reason, TransferConstants.CHECK_RECORD_ORDER_SECOND);
		}
		return "SUCCESS";
	}

}
