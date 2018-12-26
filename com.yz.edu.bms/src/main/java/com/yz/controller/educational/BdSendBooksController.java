package com.yz.controller.educational;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.open.api.sdk.JdException;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdSendBooksQuery;
import com.yz.service.educational.BdSendBooksService;

/**
 * 外包发书
 * 
 * @author lx
 * @date 2017年9月19日 下午2:51:55
 */
@RequestMapping("/sendBooks")
@Controller
public class BdSendBooksController {

	@Autowired
	private BdSendBooksService sendBooksService;

	/**
	 * 发书列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	@Rule("sendBooks:query")
	public String showList(HttpServletRequest request, Model model) {
		String fsdentity="sfwbfs";
		BaseUser user=SessionUtil.getUser();
		if(user.getRoleCodeList().contains("wbfs")) {
			fsdentity="sfwbfs";
		}else if(user.getRoleCodeList().contains("jdwbfs")) {
			fsdentity="jdwbfs";
		}else {
			fsdentity="all";
		}
		model.addAttribute("fsdentity", fsdentity);
		return "educational/sendBook/send_book_list";
	}

	/**
	 * 加载需要发书的数据
	 * 
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/findNeedSendBooks")
	@ResponseBody
	@Rule("sendBooks:query")
	public Object findNeedSendBooks(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdSendBooksQuery bookQuery) {
		return sendBooksService.selectSendBooksByPage(start, length, bookQuery);
	}

	/**
	 * 顺丰下单
	 * 
	 * @param sendIds
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/sfOrders")
	@ResponseBody
	@Log
	@Rule("sendBooks:sfOrder")
	public Object reptOrders(@RequestParam(name = "sendIds[]", required = true) String[] sendIds) throws IOException {
		sendBooksService.sfOrders(sendIds);
		return "SUCCESS";
	}

	/**
	 * 打印顺丰快递单(批量)
	 * 
	 * @param sendIds
	 * @param model
	 * @return
	 */
	@RequestMapping("/sfPrints")
	@Rule("sendBooks:print")
	public Object sfPrints(@RequestParam(name = "sendIds[]", required = true) String[] sendIds, Model model) {
		model.addAttribute("sfs", sendBooksService.sfPrintInfos(sendIds));
		return "/educational/sendBook/sf_print";
	}

	/**
	 * 打印顺丰快递单(单个)
	 * 
	 * @param sendId
	 * @param model
	 * @return
	 */
	@RequestMapping("/sfPrint")
	@Rule("sendBooks:print")
	public Object sfPrint(@RequestParam(name = "sendId", required = true) String sendId, Model model) {
		model.addAttribute("sfs", sendBooksService.sfPrintInfo(sendId));
		return "/educational/sendBook/sf_print";
	}

	
	/**
	 * 打印京东快递单(批量)
	 * 
	 * @param sendIds
	 * @param model
	 * @return
	 */
	@RequestMapping("/jdPrints")
	@Rule("sendBooks:jdprint")
	public Object jdPrints(@RequestParam(name = "sendIds[]", required = true) String[] sendIds, Model model) {
		model.addAttribute("sfs", sendBooksService.jdPrintInfos(sendIds));
		return "/educational/sendBook/jd_print";
	}

	/**
	 * 打印京东快递单(单个)
	 * 
	 * @param sendId
	 * @param model
	 * @return
	 */
	@RequestMapping("/jdPrint")
	@Rule("sendBooks:jdprint")
	public Object jdPrint(@RequestParam(name = "sendId", required = true) String sendId, Model model) {
		model.addAttribute("sfs", sendBooksService.jdPrintInfo(sendId));
		return "/educational/sendBook/jd_print";
	}

	
	
	@RequestMapping("/export")
	@Rule("sendBooks:export")
	public void export(BdSendBooksQuery bookQuery, HttpServletResponse response) {
		sendBooksService.exportSendInfo(bookQuery, response);
	}
	
	/**
	 * 批量筛选下单
	 * @param bookQuery
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/querySfOrders")
	@ResponseBody
	@Log
	@Rule("sendBooks:sfOrder")
	public Object querySfOrders(BdSendBooksQuery bookQuery) throws IOException {
		sendBooksService.querySfOrders(bookQuery);
	    return "SUCCESS";
	}
	
	/**
	 * 京东下单
	 * 
	 * @param sendIds
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/jdOrders")
	@ResponseBody
	@Log
	@Rule("sendBooks:jdOrder")
	public Object reptJdOrders(@RequestParam(name = "sendIds[]", required = true) String[] sendIds) throws JdException, IOException {
		sendBooksService.jdOrders(sendIds);
		return "SUCCESS";
	}
	
	/**
	 * 京东批量筛选下单
	 * @param bookQuery
	 * @return
	 * @throws IOException
	 * @throws JdException 
	 */
	@RequestMapping("/queryJdOrders")
	@ResponseBody
	@Log
	@Rule("sendBooks:jdOrder")
	public Object queryJdOrders(BdSendBooksQuery bookQuery) throws IOException, JdException {
		sendBooksService.queryJdOrders(bookQuery);
	    return "SUCCESS";
	}
	
}
