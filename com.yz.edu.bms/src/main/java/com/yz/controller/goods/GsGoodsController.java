package com.yz.controller.goods;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsQueryInfo;
import com.yz.service.goods.GsGoodsService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

/**
 * 运营管理-商品管理
 * @author lx
 * @date 2017年8月18日 下午12:05:55
 */
@Controller
@RequestMapping("goods")
public class GsGoodsController {
	
	@Autowired
	private GsGoodsService gsGoodsService;
	/**
	 * 跳转到商品管理
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("goods:query")
	public String toList() {
		return "/goods/goods_list";
	}
	
	/**
	 * 普通商品列表
	 * @return
	 */
	@RequestMapping("/toCommon")
	@Rule("goods:query")
	public String toCommon() {
		return "/goods/common_goods_list";
	}
	/**
	 * 分页普通商品数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("goods:query")
	@ResponseBody
	public Object goodsList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GsGoodsQueryInfo queryInfo) {
		return gsGoodsService.queryGsGoodsShowInfoByPage(start,length,queryInfo);
	}
	/**
	 * 普通列表编辑页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCommonEdit")
	@Rule("goods:insert")
	public String toCommonEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String goodsId = RequestUtil.getString("goodsId");
			Assert.hasText(goodsId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getGoodsDetailInfo(goodsId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/common_goods_edit";
	}
	/**
	 * 普通商品的修改和新增
	 * @param exType
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/goodsUpdate")
	@ResponseBody
	@Rule("goods:insert")
	public Object goodsUpdate(@RequestParam(name = "exType", required = true) String exType,GsGoodsInsertInfo goodsInfo,
			@RequestParam(name = "coverUrls", required = true) String coverUrls) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if(!StringUtil.isEmpty(goodsInfo.getGoodsUse())){
			goodsInfo.setGoodsUse(goodsInfo.getGoodsUse().replace(",", ";"));
		}
		if ("UPDATE".equalsIgnoreCase(deal)) {
			goodsInfo.setUpdateUserId(user.getUserId());
			goodsInfo.setUpdateUser(user.getRealName());
			gsGoodsService.updateGoodsInfo(goodsInfo,coverUrls);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			goodsInfo.setUpdateUserId(user.getUserId());
			goodsInfo.setUpdateUser(user.getRealName());
			return gsGoodsService.insertGoodsInfo(goodsInfo,coverUrls);
			
		}

		return "success";
	}
	
	/**
	 * 跳转到线下活动页面
	 * @return
	 */
	@RequestMapping("/toActivity")
	@Rule("goods:activityQuery")
	public String toActivityList() {
		return "/goods/activities_goods_list";
	}

	/**
	 * 分页线下活动数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/toActivityEdit")
	@Rule("goods:insert")
	public String toActivityEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String goodsId = RequestUtil.getString("goodsId");
			Assert.hasText(goodsId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getActivityGoodsDetailInfo(goodsId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/activities_goods_edit";
	}
	
	/**
	 * 跳转到培训课程
	 * @return
	 */
	@RequestMapping("/toCourseList")
	@Rule("goods:courseQuery")
	public String toCourseList() {
		return "/goods/course_goods_list";
	}
	/**
	 * 跳转到课程编辑页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCourseEdit")
	@Rule("goods:insert")
	public String toCourseEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String goodsId = RequestUtil.getString("goodsId");
			Assert.hasText(goodsId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getCourseGoodsDetailInfo(goodsId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/course_goods_edit";
	}
	
	/**
	 * 教材商品页面
	 * @return
	 */
	@RequestMapping("/toTextBookList")
	@Rule("goods:textBookQuery")
	public String toTextBookList() {
		return "/goods/textbook_goods_list";
	}
	/**
	 * 教材商品编辑页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toTextBookEdit")
	@Rule("goods:insert")
	public String toTextBookEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String goodsId = RequestUtil.getString("goodsId");
			Assert.hasText(goodsId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getGoodsDetailInfo(goodsId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/textbook_goods_edit";
	}
	
	/**
	 * 批量禁用
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/batchStopGoods")
	@Rule("goods:batchStop")
	@ResponseBody
	public Object batchStopGoods(@RequestParam(name = "goodsIds[]", required = true) String[] goodsIds) {
		gsGoodsService.batchStopGoods(goodsIds);
		return "success";
	}
	/**
	 * 批量启用
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/batchStartGoods")
	@Rule("goods:batchStart")
	@ResponseBody
	public Object batchStartGoods(@RequestParam(name = "goodsIds[]", required = true) String[] goodsIds) {
		gsGoodsService.batchStartGoods(goodsIds);
		return "success";
	}
	/**
	 * 商品数量
	 * @param goodsId
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getGoodsCount")
	@ResponseBody
	public Object getGoodsCount(String goodsId, HttpServletResponse resp) throws IOException {
		
		return gsGoodsService.getGoodsCountById(goodsId);
	}
}
