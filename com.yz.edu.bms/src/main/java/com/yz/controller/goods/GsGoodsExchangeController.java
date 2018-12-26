package com.yz.controller.goods;

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
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsExchangeGoodsSalesDetail;
import com.yz.service.goods.GsGoodsExchangeService;
import com.yz.service.goods.GsGoodsService;
import com.yz.util.Assert;
/**
 * 运营管理-- 兑换
 * @author lx
 * @date 2017年8月1日 下午7:49:03
 */
@Controller
@RequestMapping("/exchange")
public class GsGoodsExchangeController {

	@Autowired
	private GsGoodsExchangeService gsGoodsExchangeService;
	
	@Autowired
	private GsGoodsService gsGoodsService;
	
	/**
	 * 跳转到兑换活动列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("exchange:query")
	public String toList() {
		return "/goods/goods_exchange_list";
	}
	/**
	 * 获取兑换活动数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("exchange:query")
	@ResponseBody
	public Object goodsExchangeList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GsGoodsSalesQuery salesQuery) {
		return gsGoodsExchangeService.getZmcExchangeGoodsSales(start, length,salesQuery);
	}
	/**
	 * 跳转到兑换活动新增或者修改页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toExchangeEdit")
	@Rule("exchange:insert")
	public String toExchangeEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsExchangeGoodsSalesDetail goodsInfo = new GsExchangeGoodsSalesDetail();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String salesId = RequestUtil.getString("salesId");
			Assert.hasText(salesId, "参数名称不能为空");
			//获取信息
		    goodsInfo =gsGoodsExchangeService.getZmcExchangeGoodsSalesDetail(salesId);
		}
		model.addAttribute("goodsInfo", goodsInfo);
		model.addAttribute("exType", exType);
		return "/goods/goods_exchange_edit";
	}
	/**
	 * 获取某个类型下的所有商品信息
	 * @param goodsType
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public Object selectAllList(String goodsType) {
		List<Map<String, String>> resultList = gsGoodsExchangeService.findAllGoodsByType(goodsType);
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}
	
	/**
	 * 修改或者信息兑换活动
	 * @param exType
	 * @param salesDetail
	 * @return
	 */
	@RequestMapping("/exchangeUpdate")
	@Rule("exchange:insert")
	@Log
	@ResponseBody
	public Object exchangeUpdate(@RequestParam(name = "exType", required = true) String exType,GsExchangeGoodsSalesDetail salesDetail) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if ("UPDATE".equalsIgnoreCase(deal)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			gsGoodsExchangeService.updateExchangeGoodsSales(salesDetail);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			salesDetail.setUpdateUserId(user.getUserId());
			salesDetail.setUpdateUser(user.getRealName());
			return gsGoodsExchangeService.insertExchangeGoodsSales(salesDetail);
		}

		return "success";
	}
	/**
	 * 获取某个商品的具体信息
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/getGoodsDetail")
	@ResponseBody
	public Object goodsDetail(String goodsId) {
		return gsGoodsService.getGoodsDetailInfo(goodsId);
	}
	/**
	 * 批量删除
	 * @param salesIds
	 * @return
	 */
	@RequestMapping("/batchDeleteExchange")
	@Rule("exchange:batchDel")
	@ResponseBody
	public Object batchDeleteExchange(@RequestParam(name = "salesIds[]", required = true) String[] salesIds,@RequestParam(name = "goodsIds[]", required = true) String[] goodsIds) {
		gsGoodsExchangeService.batchDeleteExchange(salesIds,goodsIds);
		return "success";
	}
	/**
	 * 得到京东商品池
	 * @return
	 */
	@RequestMapping("/getJdPageNum")
	@Rule("exchange:query")
	@ResponseBody
	public Object getJdPageNum() {
		return gsGoodsExchangeService.getJdPageNumList();
	}
	
	/**
	 * 得到京东商品池
	 * @return
	 */
	@RequestMapping("/getJdGoodDetail")
	@Rule("exchange:query")
	@ResponseBody
	public Object getJdGoodDetail(String skuId) {
		return gsGoodsExchangeService.getJdGoodDetail(skuId);
	}
	
	/**
	 * 得到京东商品价格信息
	 * @return
	 */
	@RequestMapping("/getJdGoodPrice")
	@Rule("exchange:query")
	@ResponseBody
	public Object getJdGoodPrice(String skuId) {
		return gsGoodsExchangeService.getJdGoodPrice(skuId);
	}
	
	/**
	 * 根据京东商品编码得到池子商品信息
	 * @param pageNum 商品池编号
	 * @param pageNo 页码，默认取第一页
	 * @return
	 */
	@RequestMapping("/getJdSkuByPage")
	@Rule("exchange:query")
	@ResponseBody
	public Object getJdSkuByPage(@RequestParam(name = "pageNum", required = true) String pageNum,@RequestParam(name = "pageNo", required = true) String pageNo) {
		return gsGoodsExchangeService.getJdSkuByPage(pageNum, pageNo);
	}
	
	@RequestMapping("/exportExchenageInfo")
	@Rule("exchange:export")
	public void exportExchenageInfo(GsGoodsSalesQuery query, HttpServletResponse response) {
		gsGoodsExchangeService.exportExchenageInfo(query, response);
	}
	
}
