package com.yz.controller.goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.goods.GsGoodsPurchasingInfo;
import com.yz.model.goods.GsGoodsPurchasingQuery;
import com.yz.service.goods.GsGoodsPurchasingService;
import com.yz.util.Assert;

/**
 * 运营管理--采购列表
 * @author lx
 * @date 2018年5月15日 上午10:10:57
 */
@Controller
@RequestMapping("/purchasing")
public class GsGoodsPurchasingController {

	@Autowired
	private GsGoodsPurchasingService gsGoodsPurchasingService;
	
	@RequestMapping("/toList")
	@Rule("purchasing:query")
	public String toList() {
		return "/goods/purchasing_list";
	}
	
	/**
	 * 采购列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("purchasing:query")
	@ResponseBody
	public Object goodsPurchasingList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GsGoodsPurchasingQuery query) {
		return gsGoodsPurchasingService.getGsGoodsPurchasing(start, length, query);
	}
	
	@RequestMapping("/toApply")
	@Rule("purchasing:buy")
	public String toApply() {
		return "/goods/purchasing_apply";
	}
	
	/**
	 * 获取京东的省
	 * @return
	 */
	@RequestMapping("/getJDProvince")
	@ResponseBody
	public Object getJDProvince(){
		return gsGoodsPurchasingService.getJDProvince();
	}
	
	/**
	 * 获取京东的市
	 * @return
	 */
	@RequestMapping("/getJDCity")
	@ResponseBody
	public Object getJDCity(@RequestParam(name = "pId") String pId){
		return gsGoodsPurchasingService.getJDCity(pId);
	}
	
	/**
	 * 获取京东的区
	 * @param cId
	 * @return
	 */
	@RequestMapping("/getJDCounty")
	@ResponseBody
	public Object getJDCounty(@RequestParam(name = "cId") String cId){
		return gsGoodsPurchasingService.getJDCounty(cId);
	}
	/**
	 * 获取京东的街道
	 * @param tId
	 * @return
	 */
	@RequestMapping("/getJDTown")
	@ResponseBody
	public Object getJDTown(@RequestParam(name = "tId") String tId){
		return gsGoodsPurchasingService.getJDTown(tId);
	}
	
	/**
	 * 采购下单
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping(value = "/buyGoods")
	@Rule("purchasing:buy")
	@ResponseBody
	public Object buyGoods(GsGoodsPurchasingInfo goodsInfo) {
		return gsGoodsPurchasingService.buyGoods(goodsInfo);
	}
	
	/**
	 * 查看详细
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/lookDetail")
	@Rule("purchasing:query")
	public String lookDetail(@RequestParam(name = "id") String id, HttpServletRequest request,
			Model model) {
		Assert.hasText(id, "参数名称不能为空");
		model.addAttribute("goodsInfo", gsGoodsPurchasingService.getPurchasingDetail(id));
		return "/goods/purchasing_detail";
	}
}
