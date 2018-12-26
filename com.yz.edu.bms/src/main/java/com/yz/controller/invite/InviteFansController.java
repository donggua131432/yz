package com.yz.controller.invite;

import java.util.List;

import com.yz.core.util.RequestUtil;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.EmpQueryInfo;
import com.yz.model.condition.invite.InviteFansQuery;
import com.yz.model.invite.InviteAssignInfo;
import com.yz.model.invite.InviteUserInfo;
import com.yz.service.common.BaseInfoService;
import com.yz.service.invite.InviteFansService;
import com.yz.util.Assert;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/fans")
@Controller
public class InviteFansController {

	@Autowired
	private InviteFansService fansService;

	@RequestMapping("/toAssignedList")
	@Rule("invite_fans:assigned")
	public String toAssignedList(Model model) {
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		//XJ ZSZG
		if(jtList.contains("XJ") || jtList.contains("ZSZG")){
			model.addAttribute("isShow",1);
		}else{
			model.addAttribute("isShow",0);
		}
		return "/invite/fans/fans_assigned_list";
	}

	@RequestMapping("/getAssignedList")
	@Rule("invite_fans:assigned")
	@ResponseBody
	public Object getAssignedList(InviteUserQuery queryInfo) {
		return fansService.getAssignedList(queryInfo);
	}

	@RequestMapping("/toUndistributedList")
	@Rule("invite_fans:undistributed")
	public String toUndistributedList() {
		return "/invite/fans/fans_undistributed_list";
	}

	@RequestMapping("/getUndistributedList")
	@Rule("invite_fans:undistributed")
	@ResponseBody
	public Object getUndistributedList(InviteFansQuery queryInfo) {
		return fansService.getUndistributedList(queryInfo);
	}

	@Autowired
	private BaseInfoService baseInfoService;

	@RequestMapping("/getEmpList")
	@ResponseBody
	@Rule(value = {"invite_user:assign","invite_fans:schoolSup:assign","invite_fans:admin:assign","invite_fans:search:assign"})
	public Object getEmpList(EmpQueryInfo queryInfo) {
		return baseInfoService.getEmpList(queryInfo);
	}


	/**
	 * 分配校监
	 * @param model
	 * @param userIds
	 * @return
	 */
	@RequestMapping("/toADistributionPageXJ")
	@Rule(value = {"invite_user:assignXJ","invite_fans:admin:assignXJ","invite_user:search:assignXJ"})
	public String toADistributionPage2(Model model, @RequestParam("userIds") String userIds) {
		Assert.hasText(userIds, "待分配的用户ID不能为空");
		String[] s = userIds.split(",");
		List<InviteUserInfo> list = fansService.getUser(s);
		model.addAttribute("fansList", list);
		model.addAttribute("jtIds", "XJ");
		model.addAttribute("url", "fans/aDistributionXJ.do");
		return "invite/fans/fans_assign_page";
	}

	@RequestMapping("/aDistributionXJ")
	@ResponseBody
	@Rule(value = {"invite_user:assignXJ","invite_fans:admin:assignXJ","invite_user:search:assignXJ"})
	public Object aDistributionXJ(InviteAssignInfo assignInfo) {
		String[] userIds = assignInfo.getUserIds();
		if (userIds == null) return null;
		fansService.addAssignXJ(assignInfo);
		return null;
	}


	/**
	 * 分配跟进人
	 * @param model
	 * @param userIds
	 * @return
	 */
	@RequestMapping("/toADistributionPage")
	@Rule(value = {"invite_user:assign","invite_fans:schoolSup:assign","invite_fans:admin:assign","invite_fans:search:assign"})
	public String toADistributionPage(Model model, @RequestParam("userIds") String userIds, HttpServletRequest request) {
		Assert.hasText(userIds, "待分配的用户ID不能为空");
		String[] s = userIds.split(",");
		List<InviteUserInfo> list = fansService.getUser(s);
		model.addAttribute("fansList", list);
		model.addAttribute("jtIds", "ZSLS");
		String assignType = request.getParameter("assignType");
		if(StringUtil.hasValue(assignType)){
			model.addAttribute("assignType", assignType);
		}
		model.addAttribute("url", "fans/aDistribution.do");
		return "invite/fans/fans_assign_page";
	}

	@RequestMapping("/aDistribution")
	@ResponseBody
	@Rule(value = {"invite_user:assign","invite_fans:schoolSup:assign","invite_fans:admin:assign","invite_fans:search:assign"})
	public Object aDistribution(InviteAssignInfo assignInfo) {
		String[] userIds = assignInfo.getUserIds();
		if (userIds == null) return null;
		fansService.addAssign(assignInfo);
		return null;
	}

}
