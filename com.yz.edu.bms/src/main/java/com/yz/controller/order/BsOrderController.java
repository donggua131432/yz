package com.yz.controller.order;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.yz.model.baseinfo.BdCourseMap;
import com.yz.model.order.BsActivityOrder;
import com.yz.model.order.BsCourseOrder;
import com.yz.model.order.BsLogistics;
import com.yz.model.order.BsOrder;
import com.yz.model.order.BsOrderQuery;
import com.yz.service.order.BsOrderSerivce;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
/**
 * 运营管理-订单管理
 * @author lx
 * @date 2017年8月18日 下午2:11:17
 */
@Controller
@RequestMapping("/order")
public class BsOrderController
{

	@Autowired
	private BsOrderSerivce bsOrderService;
	
	/**
	 * 跳转到普通商品订单页面
	 * @return
	 */
	@RequestMapping("/commonList")
	@Rule("order:query")
	public String toCommonList() 
	{
		return "/order/common_goods_order";	
	}
	/**
	 * 普通商品订单数据
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("order:query")
	@ResponseBody
	public Object commonGoodsOrderList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BsOrderQuery orderQuery) {
		return bsOrderService.getBsOrderList(start, length,orderQuery);
	}
	
	/**
	 * 活动订单页面
	 * @return
	 */
	@RequestMapping("/toActivityList")
	@Rule("order:activityQuery")
	public String toActivityList() 
	{
		return "/order/activity_goods_order";	
	}
	/**
	 * 活动订单数据
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/activityList", method = RequestMethod.GET)
	@Rule("order:activityQuery")
	@ResponseBody
	public Object activityGoodsOrderList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BsOrderQuery orderQuery) {
		return bsOrderService.getBsActivityOrderList(start, length,orderQuery);
	}
	/**
	 * 课程订单页面
	 * @return
	 */
	@RequestMapping("/toCourseOrder")
	@Rule("order:courseQuery")
	public String toCourseOrder() 
	{
		return "/order/course_goods_order";	
	}
	/**
	 * 课程订单信息
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/courseList", method = RequestMethod.GET)
	@Rule("order:courseQuery")
	@ResponseBody
	public Object courseGoodsOrderList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BsOrderQuery orderQuery) {
		return bsOrderService.getBsCourseOrderList(start, length,orderQuery);
	}
	/**
	 * 教材订单页面
	 * @return
	 */
	@RequestMapping("/toTextBookOrder")
	@Rule("order:textBookQuery")
	public String toTextBookOrder() 
	{
		return "/order/textbook_goods_order";	
	}
	/**
	 * 教材订单数据
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	@RequestMapping(value = "/textBookList", method = RequestMethod.GET)
	@Rule("order:textBookQuery")
	@ResponseBody
	public Object textBookGoodsOrderList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BsOrderQuery orderQuery) {
		return bsOrderService.getBsTextBookOrderList(start, length,orderQuery);
	}
	
	/***
	 * 订单编辑
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toOrderEdit")
	@Rule("order:insert")
	public String toOrderEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BsOrder order =new BsOrder();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String orderNo = RequestUtil.getString("orderNo");
			Assert.hasText(orderNo, "参数名称不能为空");
			//获取信息
			order =bsOrderService.getBsOrderDetail(orderNo);
			
		}
		model.addAttribute("orderInfo", order);
		model.addAttribute("exType", exType);
		return "/order/common_order_detail";
	}
	/***
	 * 订单查看物流
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toOrderLogistics")
	@Rule("order:insert")
	public String toOrderLogistics(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BsOrder order =new BsOrder();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String orderNo = RequestUtil.getString("orderNo");
			Assert.hasText(orderNo, "参数名称不能为空");
			//获取信息
			order =bsOrderService.getBsOrderDetail(orderNo);
		}
		model.addAttribute("orderInfo", order);
		model.addAttribute("exType", exType);
		return "/order/common_order_logistics";
	}
	/**
	 * 订单物流信息
	 * @return
	 */
	@RequestMapping("/updateOrderLogisticsInfo")
	@Rule("order:insert")
	@Log
	@ResponseBody
	public Object updateOrderLogisticsInfo() {

		BsLogistics logistics = new BsLogistics();
		
		BaseUser principal = SessionUtil.getUser();
		String logisticsId = RequestUtil.getString("logisticsId");
		String transportNo = RequestUtil.getString("transportNo");
		String logisticsName = RequestUtil.getString("logisticsName");
		String orderNo = RequestUtil.getString("orderNo");
		Assert.hasText(logisticsId, "logisticsId参数名称不能为空");
		Assert.hasText(orderNo, "参数名称不能为空");
		logistics.setUpdateUserId(principal.getUserId());
		logistics.setUpdateUser(principal.getRealName());
		logistics.setLogisticsId(logisticsId);
		logistics.setTransportNo(transportNo);
		logistics.setLogisticsName(logisticsName);
		
		bsOrderService.updateOrderLogisticsInfo(logistics);
		//订单状态改为已发货
		bsOrderService.updateOrderStatus(orderNo,"2");
	
		return "SUCCESS";
	}
	/**
	 * 订单状态
	 * @return
	 */
	@RequestMapping("/updateOrderStatus")
	@Rule("order:insert")
	@Log
	@ResponseBody
	public Object updateOrderStatus() {

		String orderNo = RequestUtil.getString("orderNo");
		Assert.hasText(orderNo, "参数名称不能为空");
		
		bsOrderService.updateOrderStatus(orderNo,"2");
	
		return "SUCCESS";
	}
	/**
	 * 课程订单编辑
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toCourseOrderEdit")
	@Rule("order:courseInsert")
	public String toCourseOrderEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BsCourseOrder order =new BsCourseOrder();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String orderNo = RequestUtil.getString("orderNo");
			Assert.hasText(orderNo, "参数名称不能为空");
			//获取信息
			order =bsOrderService.getBsCourseOrder(orderNo);
		}
		model.addAttribute("orderInfo", order);
		model.addAttribute("exType", exType);
		return "/order/course_order_detail";
	}
	/**
	 * 顺丰下单
	 * @param orderNos
	 * @return
	 */
	@RequestMapping("/sfOrders")
	@ResponseBody
	@Rule("order:sfOrder")
	@Log
	public Object sfOrders(@RequestParam(name = "orderNos[]", required = true) String[] orderNos) throws IOException {
		//bsOrderService.sfOrders(orderNos);
		for (String orderNo : orderNos) {
			bsOrderService.sfOrder(orderNo);
		}
		return "SUCCESS";
	}
	
	/**
	 * 活动订单详细页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toActivityOrderEdit")
	@Rule("order:activityInsert")
	public String toActivityOrderEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BsActivityOrder order =new BsActivityOrder();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String orderNo = RequestUtil.getString("orderNo");
			Assert.hasText(orderNo, "参数名称不能为空");
			//获取信息
			order =bsOrderService.getBsActivityOrder(orderNo);
		}
		model.addAttribute("orderInfo", order);
		model.addAttribute("exType", exType);
		return "/order/activity_order_detail";
	}
	/**
	 * 打印快递单
	 * @param logisticsId
	 * @param model
	 * @return
	 */
	@RequestMapping("/sfPrint")
	@Rule("order:print")
	public Object sfPrint(@RequestParam(name = "logisticsId", required = true) String logisticsId, Model model) {
		model.addAttribute("sfs", bsOrderService.sfPrintInfo(logisticsId));
		return "/order/sf-print";
	}
	
	/**
	 * 订单物品邮寄
	 * @param orderNos
	 * @return
	 */
	@RequestMapping("/sended")
	@ResponseBody
	@Log
	@Rule("order:sended")
	public Object sended(@RequestParam(name = "orderNos[]", required = true) String[] orderNos) {
		bsOrderService.goodsOrderMailed(orderNos);
		return "SUCCESS";
	}
	
	/**
	 * 订单结算
	 * @return
	 */
	@RequestMapping("/checkOrderInfo")
	@Rule("order:check")
	@Log
	@ResponseBody
	public Object checkOrderInfo() {

		String orderNo = RequestUtil.getString("orderNo");
		Assert.hasText(orderNo, "参数名称不能为空");
		BaseUser userInfo = SessionUtil.getUser();
		bsOrderService.checkOrderInfo(userInfo.getRealName(),userInfo.getUserId(), orderNo);
	
		return "SUCCESS";
	}
	/**
	 * 批量结算
	 * @param orderNos
	 * @return
	 */
	@RequestMapping("/batchCheck")
	@ResponseBody
	@Log
	@Rule("order:check")
	public Object batchCheck(@RequestParam(name = "orderNos[]", required = true) String[] orderNos) {
		BaseUser userInfo = SessionUtil.getUser();
		bsOrderService.batchCheck(userInfo.getRealName(),userInfo.getUserId(),orderNos);
		return "SUCCESS";
	}
	
	/**
	 * 批量打印快递单
	 * @param orderNos
	 * @param model
	 * @return
	 */
	@RequestMapping("/sfPrints")
	@Rule("order:print")
	public Object sfPrints(@RequestParam(name = "orderNos[]", required = true) String[] orderNos, Model model) {
		model.addAttribute("sfs", bsOrderService.sfPrintInfos(orderNos));
		return "/order/sf-print";
	}
	
	@RequestMapping("/export")
	@Rule("order:export")
	public void export(BsOrderQuery orderQuery,HttpServletResponse response) {
		bsOrderService.exportOrder(orderQuery,response);
	}
}
