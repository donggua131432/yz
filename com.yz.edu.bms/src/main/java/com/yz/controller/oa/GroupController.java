package com.yz.controller.oa;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.oa.OaGroupInfo;
import com.yz.service.oa.OaGroupService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;
/**
 * 部门招生组
 * @author lx
 * @date 2017年6月30日 上午9:24:49
 */
@Controller
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private OaGroupService groupService;
	
	/**
	 * 跳转到 部门招生组列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("group:query")
	public String toList() {
		return "oa/group/group-list";
	}
	
	/**
	 * 分页获取部门招生组数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@Rule("group:query")
	public Object groupList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaGroupInfo groupInfo) {
		return groupService.queryGroupByPage(start, length, groupInfo);
	}
	
	/**
	 * 新增或者修改部门招生组 跳转
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	@Token(action = Flag.Save, groupId = "depEdit")
	@Rule("group:insert")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		OaGroupInfo groupInfo = new OaGroupInfo();
	
		
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String groupId = RequestUtil.getString("groupId");
			Assert.hasText(groupId, "参数名称不能为空");
			groupInfo = groupService.getOaGroupInfo(groupId);	
		}
		model.addAttribute("exType", exType);
		model.addAttribute("groupInfo", groupInfo);
		return "oa/group/group-edit";
	}
	
	/**
	 * 新增或者修改 操作
	 * @param exType
	 * @param role
	 * @param permissions
	 * @return
	 */
	@RequestMapping("/groupUpdate")
	@ResponseBody
	@Log
	@Token(action = Flag.Remove, groupId = "depEdit")
	@Rule("group:insert")
	public Object groupUpdate(@RequestParam(name = "exType", required = true) String exType, OaGroupInfo groupInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		
		if ("UPDATE".equalsIgnoreCase(deal)) {
			groupInfo.setUpdateUserId(user.getUserId());
			groupInfo.setUpdateUser(user.getRealName());
			groupService.updateOaGroupInfo(groupInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			groupInfo.setCreateUserId(user.getUserId());
			groupInfo.setCreateUser(user.getRealName());
			groupService.insertGroupInfo(groupInfo);
		}

		return "success";
	}
	/**
	 *  验证部门招生组是否存在
	 * @param exType
	 * @param groupName
	 * @param dpId
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateGroupName")
	@ResponseBody
	public Object validate(@RequestParam(name = "exType", required = true) String exType,
			String groupName, String dpId, String oldGroupName, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			if(StringUtil.hasValue(groupName) && StringUtil.hasValue(oldGroupName) && groupName.trim().equals(oldGroupName.trim())){
				return true;
			}
			return validGroupName(groupName,dpId);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			return validGroupName(groupName,dpId);
		}

		return false;
	}
	
	private Object validGroupName(String groupName,String dpId) {
		if (StringUtil.hasValue(groupName) && groupService.isGroupNameExist(groupName, dpId) <1) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 启用或者停用部门招生管理组
	 * @return
	 */
	@RequestMapping("/groupBlock")
	@Log(needLog = true)
	@ResponseBody
	@Rule("group:stop")
	public Object groupBlock() {

	    OaGroupInfo groupInfo = new OaGroupInfo();

		BaseUser principal = SessionUtil.getUser();
		String groupId = RequestUtil.getString("groupId");
		String exType = RequestUtil.getString("exType");
		groupInfo.setUpdateUserId(principal.getUserId());
		groupInfo.setUpdateUser(principal.getRealName());
		groupInfo.setGroupId(groupId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			groupInfo.setIsStop("1");
		} else if ("START".equalsIgnoreCase(exType)) {
			groupInfo.setIsStop("0");

		}
		groupService.groupBlock(groupInfo);
	
		return "SUCCESS";
	}

	/**
	 * 某个部门下的所有招生组信息
	 * @param dpId
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public Object selectAllList(String dpId) {
		List<Map<String, String>> resultList = groupService.findAllListByDpId(dpId);
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}
}
