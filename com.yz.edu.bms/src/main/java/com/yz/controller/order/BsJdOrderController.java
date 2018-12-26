package com.yz.controller.order;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.ResponseBody;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil; 
import com.yz.model.order.BsOrderQuery;
import com.yz.service.order.BsJdOrderSerivce;
import com.yz.service.order.BsOrderSerivce;
import com.yz.util.Assert;  
/**
 * 运营管理-订单管理
 * @author lx
 * @date 2017年8月18日 下午2:11:17
 */
@Controller
@RequestMapping("/jdorder")
public class BsJdOrderController
{

	@Autowired
	private BsJdOrderSerivce bsJdOrderService;
	
	@Autowired
	private BsOrderSerivce bsOrderService;
//	/**
//	 * 跳转到京东对账订单页面
//	 * @return
//	 */
//	@RequestMapping("/jdorderList")
//	@Rule("jdorder:query")
//	public String jdorderList() 
//	{
//		return "/order/jd_goods_order";	
//	}
//	/**
//	 * 普通商品订单数据
//	 * @param start
//	 * @param length
//	 * @param orderQuery
//	 * @return
//	 */
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	@Rule("jdorder:query")
//	@ResponseBody
//	public Object jdGoodsOrderList(@RequestParam(name = "start", defaultValue = "0") int start,
//			@RequestParam(name = "length", defaultValue = "10") int length,BsOrderQuery orderQuery) {
//		return bsJdOrderService.getBsJdOrderList(start, length,orderQuery);
//	}
//	
	/**
	 * 普通商品订单数据
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/listcount")
	@Rule("order:query")
	@ResponseBody
	public Object getOrderlistCount(BsOrderQuery orderQuery) {
		return bsJdOrderService.getOrderlistCount(orderQuery);
	}
//	/***
//	 * 订单编辑
//	 * @param exType
//	 * @param request
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="/tojdOrderEdit")
//	@Rule("jdorder:detail")
//	public String toOrderEdit(HttpServletRequest request,
//			Model model) {
//		BsOrder order =new BsOrder();
//		String orderNo = RequestUtil.getString("orderNo");
//		Assert.hasText(orderNo, "参数名称不能为空");
//		//获取信息
//		order =bsOrderService.getBsOrderDetail(orderNo);
//		model.addAttribute("orderInfo", order);
//		return "/order/common_order_detail";
//	}
//	
//	/***
//	 * 订单导出
//	 * @param exType
//	 * @param request
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="/jdOrderexport")
//	@Rule("jdorder:export")
//	public void jdOrderexport(BsOrderQuery orderQuery, HttpServletResponse response) {
//		bsJdOrderService.exportJdOrderInfo(orderQuery,response);
//	}
//	
	/**
	 * 同步京东订单
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/synchronousJdOrder")
	@Rule("jdorder:synchronous")
	@ResponseBody
	public Object synchronousJdOrder() {
		bsJdOrderService.synchronousJdOrder();
		return "success";
	}
	
	/**
	 * 获取订单配送信息
	 * @param header
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/toJdOrderLogistics")
	@Rule("order:query")
	public Object orderTrackByNo(HttpServletRequest request,
			Model model){
		String orderNo = RequestUtil.getString("orderNo");
		Assert.hasText(orderNo, "参数名称不能为空");
		//获取信息
		String ordertrack= bsJdOrderService.orderTrackByNo(orderNo);
		model.addAttribute("track", ordertrack);
		model.addAttribute("orderNo", orderNo);
		return "/order/common_order_jdlogistics";
	}

}
