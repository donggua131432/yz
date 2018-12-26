package com.yz.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.order.BadPaymentInfo;
import com.yz.service.order.BadPaymentOrderService;

/**
 * 回调失败订单列表
 * 
 * @ClassName: InviteQrCodeController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月18日
 *
 */
@RequestMapping("/bad_payment_order")
@Controller
public class BadPaymentOrderController {

	
	@Autowired 
	private BadPaymentOrderService badPaymentOrderService;

	@RequestMapping("/toList")
	@Rule("bad_payment_order")
	public String toList() {
		return "/order/bad_payment_order";
	}

	/**
	 * 获取数据
	 * 
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping("/getList")
	@Rule("bad_payment_order")
	@ResponseBody
	public Object getList(@RequestParam(value = "rows", defaultValue = "10000") int rows,
            @RequestParam(value = "page", defaultValue = "1")int page, String payNo) {
		PageHelper.startPage(page, rows);
        List<BadPaymentInfo> resultList = badPaymentOrderService.getBsOrderList(payNo);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList); 
	}
	
	/**
	 * 批量重试
	 * 
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping("/batchExecute")
	@Rule("bad_payment_order")
	@ResponseBody
	public Object batchExecute(@RequestParam(name = "badPaymentIds[]", required = true) String[]  badPaymentIds) {
		//根据ids获取到数据 然后发送到redis队列
		badPaymentOrderService.getBadPaymentOrderByIds(badPaymentIds);
		return "success";
	}
}
