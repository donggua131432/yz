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
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsLotteryGoodsSalesDetail;
import com.yz.model.goods.GsLotteryPart;
import com.yz.service.goods.GsGoodsLotteryService;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;

/**
 * 运营管理-抽奖
 * @author lx
 * @date 2017年8月1日 下午7:49:14
 */
@Controller
@RequestMapping("/lottery")
public class GsGoodsLotteryController {

	@Autowired
	private GsGoodsLotteryService gsGoodsService;
	
	/**
	 * 跳转到抽奖活动页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("lottery:query")
	public String toList() {
		return "/goods/goods_lottery_list";
	}
	/**
	 * 获取抽奖活动的数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("lottery:query")
	@ResponseBody
	public Object goodsLotteryList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GsGoodsSalesQuery salesQuery) {
		return gsGoodsService.getZmcLotteryGoodsSales(start, length,salesQuery);
	}
	
	/**
	 * 跳转到抽奖活动编辑或者新增的页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toLotteryEdit")
	@Rule("lottery:insert")
	public String toLotteryEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsLotteryGoodsSalesDetail goodsInfo = new GsLotteryGoodsSalesDetail();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String salesId = RequestUtil.getString("salesId");
			Assert.hasText(salesId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsService.getZmcLotteryGoodsSalesDetail(salesId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/goods_lottery_edit";
	}
	
	/**
	 * 新增或者修改抽奖活动
	 * @param exType
	 * @param salesDetail
	 * @return
	 */
	@RequestMapping("/lotteryUpdate")
	@Rule("lottery:insert")
	@Log
	@ResponseBody
	public Object lotteryUpdate(@RequestParam(name = "exType", required = true) String exType,GsLotteryGoodsSalesDetail salesDetail) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if ("UPDATE".equalsIgnoreCase(deal)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			gsGoodsService.updateLotteryGoodsSales(salesDetail);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			return gsGoodsService.insertLotteryGoodsSales(salesDetail);
		}

		return "success";
	}
	/**
	 * 批量删除
	 * @param salesIds
	 * @return
	 */
	@RequestMapping("/batchDeleteLottery")
	@Rule("lottery:batchDel")
	@ResponseBody
	public Object batchDeleteLottery(@RequestParam(name = "salesIds[]", required = true) String[] salesIds) {
		gsGoodsService.batchDeleteLottery(salesIds);
		return "success";
	}
	/**
	 * 往期中奖记录
	 * @param salesId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/showLotteryResult")
	public String showLotteryResult(@RequestParam(name = "salesId", required = true) String salesId, HttpServletRequest request,
			Model model){
		List<GsLotteryPart> parstList = gsGoodsService.getGsLotteryParts(salesId);
		model.addAttribute("result", JsonUtil.object2String(parstList));
		return "/goods/look_lottery_result";
	}
}
