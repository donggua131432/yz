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

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.DepartmentInfo;
import com.yz.model.oa.OaDepQueryInfo;
import com.yz.service.oa.DepartmentService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;
/**
 * 部门管理
 * @author lx
 * @date 2017年6月30日 上午9:24:49
 */
@Controller
@RequestMapping("/dep")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * 跳转到 部门列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("dep:query")
	public String toList() {
		return "oa/dep/dep-list";
	}
	
	/**
	 * 分页获取部门数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@Rule("dep:query")
	public Object depList(HttpServletRequest req, HttpServletResponse resp,OaDepQueryInfo depInfo) {
		return departmentService.queryDepByPage(depInfo);
	}
	
	/**
	 * 新增或者修改部门 跳转
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	@Token(action = Flag.Save, groupId = "depEdit")
	@Rule("dep:insert")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		DepartmentInfo depInfo = new DepartmentInfo();
	
		
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String dpId = RequestUtil.getString("dpId");
			Assert.hasText(dpId, "参数名称不能为空");
			depInfo = departmentService.getDepartmentInfo(dpId);
			if(null != depInfo){
				if(StringUtil.hasValue(depInfo.getRecruitRules())){
					depInfo.setRecruitTypes(depInfo.getRecruitRules().split(";"));
				}
				List<Map<String, String>> jdIdMap =departmentService.findDepTitle(depInfo.getDpId());
				
				if(null != jdIdMap && jdIdMap.size()>0){
					String[] jdIds = new String[jdIdMap.size()];
					for(int i=0;i<jdIdMap.size();i++){
						jdIds[i]=jdIdMap.get(i).get("jtId");
					}
					depInfo.setJdIds(jdIds);
				}
			}
			
		}
		model.addAttribute("exType", exType);
		model.addAttribute("depInfo", depInfo);
		return "oa/dep/dep-edit";
	}
	
	/**
	 * 新增或者修改 操作
	 * @param exType
	 * @param role
	 * @param permissions
	 * @return
	 */
	@RequestMapping("/depUpdate")
	@ResponseBody
	@Log
	@Token(action = Flag.Remove, groupId = "depEdit")
	@Rule("dep:insert")
	public Object depUpdate(@RequestParam(name = "exType", required = true) String exType,
			DepartmentInfo dpInfo,String[] recruitTypes,String[] jdIds) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		StringBuilder sb = new StringBuilder();
		if(null != recruitTypes && recruitTypes.length >0){
			for(String str :recruitTypes){
				sb.append(str + ";");
			}
		}
		if(sb.length() >0){
			dpInfo.setRecruitRules(sb.toString().substring(0,sb.toString().lastIndexOf(";")));
		}
	
		if ("UPDATE".equalsIgnoreCase(deal)) {
			dpInfo.setUpdateUserId(user.getUserId());
			dpInfo.setUpdateUser(user.getRealName());
			departmentService.updateDepartmentInfo(dpInfo,jdIds);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			dpInfo.setCreateUserId(user.getUserId());
			dpInfo.setCreateUser(user.getRealName());
			departmentService.insertDpInfo(dpInfo,jdIds);
		}

		return "success";
	}
	/**
	 *  验证部门是否存在
	 * @param exType
	 * @param dpName
	 * @param campusId
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateDpName")
	@ResponseBody
	public Object validate(@RequestParam(name = "exType", required = true) String exType,
			String dpName, String campusId,String oldDpName, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			if(StringUtil.hasValue(dpName) && StringUtil.hasValue(oldDpName) && dpName.trim().equals(oldDpName.trim())){
				return true;
			}
			return validDpName(dpName,campusId);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			return validDpName(dpName,campusId);
		}

		return false;
	}
	
	private Object validDpName(String dpName,String campusId) {
		if (StringUtil.hasValue(dpName) && departmentService.isDepExist(dpName,campusId) < 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 启用或者停用部门
	 * @return
	 */
	@RequestMapping("/depBlock")
	@Log(needLog = true)
	@ResponseBody
	@Rule("dep:stop")
	public Object depBlock() {

		DepartmentInfo dpInfo = new DepartmentInfo();

		BaseUser principal = SessionUtil.getUser();
		String dpId = RequestUtil.getString("dpId");
		String exType = RequestUtil.getString("exType");
		dpInfo.setUpdateUserId(principal.getUserId());
		dpInfo.setUpdateUser(principal.getRealName());
		dpInfo.setDpId(dpId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			dpInfo.setIsStop("1");
		} else if ("START".equalsIgnoreCase(exType)) {
			dpInfo.setIsStop("0");

		}
		departmentService.depBlock(dpInfo);
	
		return "SUCCESS";
	}
	/**
	 * 下拉列表
	 * @param sName
	 * @param rows
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/selectList")
	@ResponseBody
	public Object findAllKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1")int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = departmentService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}
	
	/**
	 * 某个校区下的所有部门
	 * @param campusId
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public Object selectAllList(String campusId) {
		List<Map<String, String>> resultList = departmentService.findAllListByCampusId(campusId);
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}
	
	@RequestMapping("/findDepTitle")
	@ResponseBody
	public Object findDepTitle(String dpId){
		List<Map<String, String>> jdIdMap =departmentService.findDepTitle(dpId);
		
		return jdIdMap;
	}
}
