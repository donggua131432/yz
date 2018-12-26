package com.yz.controller.goods;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.goods.GsAuctionGoodsSalesDetail;
import com.yz.model.goods.GsAuctionPart;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.service.goods.GsGoodsAuctionService;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;

/**
 * 运营管理-竞拍
 * @author lx
 * @date 2017年8月1日 下午7:49:14
 */
@Controller
@RequestMapping("/auction")
public class GsGoodsAuctionController {

	@Autowired
	private GsGoodsAuctionService gsGoodsService;
	
	/**
	 * 跳转到竞拍列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("auction:query")
	public String toList() {
		return "/goods/goods_auction_list";
	}
	/**
	 * 获取竞拍信息
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("auction:query")
	@ResponseBody
	public Object goodsAuctionList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GsGoodsSalesQuery salesQuery) {
		return gsGoodsService.getGsAuctionGoodsSales(start, length,salesQuery);
	}
	/**
	 * 跳转到竞拍编辑或者信息页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAuctionEdit")
	@Rule("auction:insert")
	public String toLotteryEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsAuctionGoodsSalesDetail goodsInfo = new GsAuctionGoodsSalesDetail();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String salesId = RequestUtil.getString("salesId");
			Assert.hasText(salesId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getGsAuctionGoodsSalesDetail(salesId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/goods_auction_edit";
	}
	/**
	 * 修改或者新增竞拍
	 * @param exType
	 * @param salesDetail
	 * @return
	 */
	@RequestMapping("/auctionUpdate")
	@Rule("auction:insert")
	@Log
	@ResponseBody
	public Object auctionUpdate(@RequestParam(name = "exType", required = true) String exType,GsAuctionGoodsSalesDetail salesDetail) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if ("UPDATE".equalsIgnoreCase(deal)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			gsGoodsService.updateAuctionGoodsSales(salesDetail);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			return gsGoodsService.insertAuctionGoodsSales(salesDetail);
		}

		return "success";
	}
	/**
	 * 批量删除竞拍信息
	 * @param salesIds
	 * @return
	 */
	@RequestMapping("/batchDeleteAuction")
	@Rule("auction:batchDel")
	@ResponseBody
	public Object batchDeleteAuction(@RequestParam(name = "salesIds[]", required = true) String[] salesIds) {
		gsGoodsService.batchDeleteAuction(salesIds);
		return "success";
	}
	
	/**
	 * 往期中拍记录
	 * @param salesId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/showAuctionResult")
	public String showAuctionResult(@RequestParam(name = "salesId", required = true) String salesId, HttpServletRequest request,
			Model model){
		List<GsAuctionPart> parstList = gsGoodsService.getGsAuctionParts(salesId);
		model.addAttribute("result", JsonUtil.object2String(parstList));
		return "/goods/look_lottery_result";
	}
}
