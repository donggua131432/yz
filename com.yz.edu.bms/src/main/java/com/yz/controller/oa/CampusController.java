package com.yz.controller.oa;

import java.io.IOException;
import java.util.List;

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
import com.yz.model.oa.OaCampusInfo;
import com.yz.service.oa.OaCampusService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

/**
 * 校区管理
 * @author lx
 * @date 2017年6月28日 下午12:01:31
 */
@Controller
@RequestMapping("/campus")
public class CampusController {

	@Autowired
	private OaCampusService campusService;
	
	/**
	 * 跳转到 校区列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("campus:query")
	public String toList() {

		return "oa/campus/campus-list";
	}
	
	/**
	 * 分页获取校区数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@Rule("campus:query")
	public Object campusList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaCampusInfo campusInfo) {
		return campusService.queryCampusByPage(start, length,campusInfo);
	}
	
	/**
	 * 新增或者修改校区 跳转
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	@Token(action = Flag.Save, groupId = "campusEdit")
	@Rule("campus:insert")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		OaCampusInfo campusInfo = new OaCampusInfo();
		//默认值：广东省，惠州市，市辖区
		campusInfo.setProvinceId("440000");
		campusInfo.setCityId("441300");
		campusInfo.setAreaId("441301");
		
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String campusId = RequestUtil.getString("campusId");
			Assert.hasText(campusId, "参数名称不能为空");
			campusInfo = campusService.getOaCampusInfo(campusId);
			
		}
		model.addAttribute("exType", exType);
		model.addAttribute("campusInfo", campusInfo);
		return "oa/campus/campus-edit";
	}
	
	/**
	 * 新增或者修改 操作
	 * @param exType
	 * @param role
	 * @param permissions
	 * @return
	 */
	@RequestMapping("/campusUpdate")
	@ResponseBody
	@Log
	@Token(action = Flag.Remove, groupId = "campusEdit")
	@Rule("campus:insert")
	public Object campusUpdate(@RequestParam(name = "exType", required = true) String exType, OaCampusInfo campusInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();

		if ("UPDATE".equalsIgnoreCase(deal)) {
			campusInfo.setUpdateUserId(user.getUserId());
			campusInfo.setUpdateUser(user.getRealName());
			campusService.updateCampus(campusInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			campusInfo.setCreateUserId(user.getUserId());
			campusInfo.setCreateUser(user.getRealName());
			campusService.insertCampus(campusInfo);
		}

		return "success";
	}
	/**
	 * 验证校区是否存在
	 * @param exType
	 * @param roleName
	 * @param roleCode
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateCampus")
	@ResponseBody
	public Object validate(@RequestParam(name = "exType", required = true) String exType,
			String campusName,String oldCampusName, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			if(StringUtil.hasValue(campusName) && StringUtil.hasValue(oldCampusName) && campusName.trim().equals(oldCampusName.trim())){
				return true;
			}
			return validCampus(campusName);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			return validCampus(campusName);
		}

		return false;
	}
	
	private Object validCampus(String campusName) {
		if (StringUtil.hasValue(campusName) && campusService.isCampusExist(campusName) < 1){
			return true;
		}
		return false;
	}
	
	/**
	 * 启用或者停用校区
	 * @param exType
	 * @param campusId
	 * @param model
	 * @return
	 */
	@RequestMapping("/campusBlock")
	@Log(needLog = true)
	@ResponseBody
	@Rule("campus:stop")
	public Object campusBlock() {

		OaCampusInfo campusInfo = new OaCampusInfo();

		BaseUser principal = SessionUtil.getUser();
		String campusId = RequestUtil.getString("campusId");
		String exType = RequestUtil.getString("exType");
		campusInfo.setUpdateUserId(principal.getUserId());
		campusInfo.setUpdateUser(principal.getRealName());
		campusInfo.setCampusId(campusId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			campusInfo.setIsStop("1");
		} else if ("START".equalsIgnoreCase(exType)) {
			campusInfo.setIsStop("0");

		}
		campusService.campusBlock(campusInfo);
	
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
		List<OaCampusInfo> resultList = campusService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}
	
	/**
	 * 下拉联动 校区信息
	 * @param sName
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public Object selectAllList() {
		List<OaCampusInfo> resultList = campusService.findAllList();
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}
	
	/**
	 * 验证校区财务代码是否存在
	 * @param financeNo
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateFinanceNo")
	@ResponseBody
	public Object validateFinanceNo(@RequestParam(name = "exType", required = true) String exType,
			String financeNo,String oldFinanceNo, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			if(StringUtil.hasValue(financeNo) && StringUtil.hasValue(oldFinanceNo) && financeNo.trim().equals(oldFinanceNo.trim())){
				return true;
			}
			return validFinanceNo(financeNo);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			return validFinanceNo(financeNo);
		}

		return false;
	}
	private Object validFinanceNo(String financeNo) {
		if (StringUtil.hasValue(financeNo) && campusService.isFinanceNoExist(financeNo) < 1) {
			return true;
		}
		return false;
	}
}
