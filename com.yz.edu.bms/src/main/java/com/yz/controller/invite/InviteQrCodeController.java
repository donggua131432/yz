package com.yz.controller.invite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.invite.InviteQrCodeQuery;
import com.yz.model.invite.InviteQrCodeInfo;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.service.invite.InviteQrCodeService;
import com.yz.service.oa.OaEmployeeService;
import com.yz.util.TokenUtil;

/**
 * 二维码推广管理
 * 
 * @ClassName: InviteQrCodeController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月18日
 *
 */
@RequestMapping("/invite_qrcode")
@Controller
public class InviteQrCodeController {

	@Autowired
	private OaEmployeeService employeeService;

	@Autowired
	private InviteQrCodeService inviteQrCodeService;

	@RequestMapping("/toList")
	@Rule("invite_qrcode")
	public String toList() {
		return "/invite/qrcode/qrcode_list";
	}

	@RequestMapping("/addInviteQrCode")
	@Rule("invite_qrcode")
	public String addInviteQrCode(Model model) {
		InviteQrCodeInfo InviteQrCodeInfo = new InviteQrCodeInfo();
		model.addAttribute("exType", "ADD");
		model.addAttribute("inviteQrCodeInfo", InviteQrCodeInfo);
		return "/invite/qrcode/qrcode_add";
	}

	@RequestMapping("/lookInviteQrCode")
	@Rule("invite_qrcode")
	public String lookInviteQrCode(@RequestParam(name = "channelId", required = true) String channelId, Model model) {
		InviteQrCodeInfo InviteQrCodeInfo = inviteQrCodeService.selectByChancelId(channelId);
		model.addAttribute("exType", "look");
		model.addAttribute("inviteQrCodeInfo", InviteQrCodeInfo);
		return "/invite/qrcode/qrcode_add";
	}

	/**
	 * 获取数据
	 * 
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping("/getList")
	@Rule("invite_qrcode")
	@ResponseBody
	public Object getList(InviteQrCodeQuery queryInfo) {
		return inviteQrCodeService.getList(queryInfo);
	}

	/**
	 * 获取邀约人
	 * 
	 * @param sName
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/getEmpList")
	@Rule("invite_qrcode")
	@ResponseBody
	public Object getEmpList(String sName, @RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);//获取在职的员工信息
		List<Map<String, String>> resultList = employeeService.findKeyValueByStatus(sName, "1");
		if (sName.equals("智哥")) {
			Map<String, String> map = new HashMap<>();
			map.put("empName", "智哥");
			map.put("empId", "0000");
			resultList.add(map);
		}
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}

	/**
	 * 生成渠道编号，二维码，和连接地址
	 * 
	 * @param empUser
	 * @param inviteUrl
	 * @return
	 */
	@RequestMapping("/generateQrcode")
	@Rule("invite_qrcode")
	@ResponseBody
	public Object generateQrcode(@RequestParam(name = "empUser", required = true) String empUser,
			@RequestParam(name = "inviteUrl", required = true) String inviteUrl) {
		String channal_id = IDGenerator.generatorId();
		String url = inviteUrl += "?channelId=" + channal_id;
		if (!empUser.equals("0000")) {// 智哥
			OaEmployeeBaseInfo info = employeeService.getEmpBaseInfo(empUser);
			if(TextUtils.isEmpty(info.getUserId())){
				return "生成渠道ID失败!";
			}
			url += "&inviteId=" + TokenUtil.createToken(info.getUserId(), UUID.randomUUID().toString());
		}
		// 生成二维码
		String imgUrl = inviteQrCodeService.generateQrcode(empUser, url);
		Map<String, String> map = Maps.newHashMap();
		map.put("imgUrl", imgUrl);// 二维码地址
		map.put("url", url);// 生成的推广地址
		map.put("channelId", channal_id);// 渠道编号
		return map;
	}

	/**
	 * 新增二维码
	 * 
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping("/inviteQrCodeAdd")
	@Rule("invite_qrcode")
	@ResponseBody
	public Object inviteQrCodeAdd(InviteQrCodeInfo info) {
		if(TextUtils.isEmpty(info.getChannelId())){
			return "生成渠道ID失败!";
		}
		BaseUser baseUser = SessionUtil.getUser();
		info.setCreateUser(baseUser.getUserId());
		info.setCreateUserName(baseUser.getRealName());
		inviteQrCodeService.add(info);
		return "success";
	}

	/**
	 * 批量删除二维码
	 * 
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping("/inviteQrCodeDel")
	@Rule("invite_qrcode")
	@ResponseBody
	public Object inviteQrCodeDel(@RequestParam(name = "channelIds[]", required = true) String[] channelIds) {
		inviteQrCodeService.del(channelIds);
		return "success";
	}
}
