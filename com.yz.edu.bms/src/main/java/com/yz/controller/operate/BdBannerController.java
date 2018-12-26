package com.yz.controller.operate;

import javax.servlet.http.HttpServletRequest;

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
import com.yz.core.security.manager.SessionUtil;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.model.operate.banner.BdBanner;
import com.yz.model.operate.banner.BdBannerQuery;
import com.yz.service.operate.BdBannerService;

@Controller
@RequestMapping("/banner")
public class BdBannerController {

	@Autowired
	private BdBannerService bannerService;

	@RequestMapping("toList")
	@Rule("banner:query")
	public String toList() {
		return "operate/banner/banner-list";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("banner:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdBannerQuery banner) {
		return bannerService.selectBannerByPage(start, length, banner);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "banner:insert")
	@Rule("banner:insert")
	public Object toAdd(Model model) {
		BdBanner banner = new BdBanner();
		model.addAttribute("banner", banner);
		model.addAttribute("exType", "ADD");
		return "operate/banner/banner-edit";
	}

	@RequestMapping("/add")
	@Token(action = Flag.Remove, groupId = "banner:insert")
	@Rule("banner:insert")
	@Log
	@ResponseBody
	public Object add(HttpServletRequest request, BdBanner banner) {
		bannerService.addBanner(banner);
		return "SUCCESS";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "banner:update")
	@Rule("banner:update")
	public Object toEdit(@RequestParam(name = "bannerId", required = true) String bannerId, Model model) {
		BdBanner banner = bannerService.selectBannerById(bannerId);
		model.addAttribute("banner", banner);
		model.addAttribute("exType", "UPDATE");
		return "operate/banner/banner-edit";
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("banner:update")
	@Token(action = Flag.Remove, groupId = "banner:update")
	public Object edit(HttpServletRequest request, BdBanner banner) {
		
		bannerService.updateBanner(banner);
		
		return "success";
	}
	
	@RequestMapping("/block")
	@ResponseBody
	@Log
	@Rule("banner:update")
	public Object block(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "bannerId", required = true) String bannerId) {
		BdBanner banner = new BdBanner();
		banner.setBannerId(bannerId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			banner.setIsAllow(GlobalConstants.STATUS_NOT_ALLOW);
		} else if ("START".equalsIgnoreCase(exType)) {
			int allowCount = bannerService.selectBannerAllowCount();
			if(allowCount >= 5){
				throw new BusinessException("E000067");	// Banner启用数量已超5个
			}
			banner.setIsAllow(GlobalConstants.STATUS_ALLOW);
		}
		bannerService.updateBannerAllow(banner);
		return "SUCCESS";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("charges:delete")
	public Object deleteBanner(@RequestParam(name = "bannerId", required = true) String bannerId) {
		bannerService.deleteBanner(bannerId);
		return "success";
	}
	
	@RequestMapping("/deleteBanners")
	@ResponseBody
	@Log
	@Rule("charges:delete")
	public Object deleteBanners(@RequestParam(name = "bannerIds[]", required = true) String[] bannerIds) {
		bannerService.deleteBanners(bannerIds);
		return "success";
	}

}
