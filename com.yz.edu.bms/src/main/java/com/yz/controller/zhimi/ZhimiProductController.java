package com.yz.controller.zhimi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.zhimi.ZhimiProductQuery;
import com.yz.model.condition.zhimi.ZhimiProductRecordsQuery;
import com.yz.model.zhimi.ZhimiProductInfo;
import com.yz.service.zhimi.ZhimiProductService;

@Controller
@RequestMapping("/product")
public class ZhimiProductController {
	
	@Autowired
	private ZhimiProductService productService;
	
	@RequestMapping("/toList")
	@Rule("recharge_product")
	public String toProductList(Model model) {
		return "zhimi/product/product-list";
	}
	
	@RequestMapping("/getList")
	@Rule("recharge_product")
	@ResponseBody
	public Object getProductList(ZhimiProductQuery queryInfo) {
		return productService.getProductList(queryInfo);
	}
	
	@RequestMapping("/toAdd")
	@Rule("recharge_product:add")
	public String toAdd(Model model) {
		int count = productService.getListCount();
		model.addAttribute("count", count + 1);
		model.addAttribute("type", "ADD");
		return "zhimi/product/product-edit";
	}
	
	@RequestMapping("/toUpdate")
	@Rule("recharge_product:update")
	public String toUpdate(Model model, @RequestParam(value="productId", required=true) String productId) {
		ZhimiProductInfo productInfo = productService.getProductInfo(productId);
		int count = productService.getListCount();
		model.addAttribute("count", count);
		model.addAttribute("type", "UPDATE");
		model.addAttribute("productInfo", productInfo);
		return "zhimi/product/product-edit";
	}
	
	@RequestMapping("/add")
	@Rule("recharge_product:add")
	@ResponseBody
	public Object add(ZhimiProductInfo productInfo) {
		productService.add(productInfo);
		return null;
	}
	
	@RequestMapping("/update")
	@Rule("recharge_product:update")
	@ResponseBody
	public Object update(ZhimiProductInfo productInfo) {
		productService.update(productInfo);
		return null;
	}
	
	@RequestMapping("/toggle")
	@Rule("recharge_product:update")
	@ResponseBody
	public Object toggle(ZhimiProductInfo productInfo) {
		productService.update(productInfo);
		return null;
	}
	
	@RequestMapping("/validate")
	@Rule(value={"recharge_product:add", "recharge_product:update"})
	@ResponseBody
	public Object validateRuleCode(ZhimiProductInfo productInfo) {
		return productService.isExsit(productInfo);
	}
	
	@RequestMapping("/toProductRecords")
	@Rule("recharge_records")
	public String toProductRecords(Model model) {
		return "zhimi/product/product-records";
	}
	
	@RequestMapping("/getProductRecords")
	@Rule("recharge_records")
	@ResponseBody
	public Object getProductRecords(ZhimiProductRecordsQuery queryInfo) {
		return productService.getProductRecords(queryInfo);
	}
	
	
}
