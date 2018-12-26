package com.yz.controller.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.service.goods.GsSalesNotifyService;
/**
 * 运营管理-消息提醒
 * @author lx
 * @date 2017年8月18日 下午12:26:11
 */
@Controller
@RequestMapping("/notify")
public class GsSalesNotifyController {

	@Autowired
	private GsSalesNotifyService gsSalesNotifyService;
	
	/**
	 * 跳转到消息提醒页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("notify:query")
	public String toList() {
		return "/goods/goods_notify_list";
	}
	/**
	 * 分页通知信息数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("notify:query")
	@ResponseBody
	public Object goodsNotifyList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length) {
		return gsSalesNotifyService.getGsSalesNotify(start, length);
	}
	/**
	 * 单个删除通知信息
	 * @param notifyId
	 * @return
	 */
	@RequestMapping("/singleDeleteSalesNotify")
	@Rule("notify:singleDel")
	@ResponseBody
	public Object singleDeleteSalesNotify(@RequestParam(name = "notifyId", required = true) String notifyId) {
		gsSalesNotifyService.singleDeleteSalesNotify(notifyId);
		return "success";
	}
	/**
	 * 批量删除通知信息
	 * @param notifyIds
	 * @return
	 */
	@RequestMapping("/batchDeleteSalesNotify")
	@Rule("notify:batchDel")
	@ResponseBody
	public Object batchDeleteSalesNotify(@RequestParam(name = "notifyIds[]", required = true) String[] notifyIds) {
		gsSalesNotifyService.batchDeleteSalesNotify(notifyIds);
		return "success";
	}
}
