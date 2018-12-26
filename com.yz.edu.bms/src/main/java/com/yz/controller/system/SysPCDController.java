package com.yz.controller.system;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.model.system.SysArea;
import com.yz.model.system.SysProvince;
import com.yz.service.system.SysPCDService;
import com.yz.util.ValidationUtil;

@RequestMapping("/sysPCD")
@Controller
public class SysPCDController {

	private static final Logger log = LoggerFactory.getLogger(SysPCDController.class);

	@Autowired
	private SysPCDService sysPCDService;
	

	/**
	 * Description: 获取所有省
	 * 
	 * @return 返回json数据
	 * @see com.yz.controller.system.SysPCDController Note: Nothing much.
	 */
	@RequestMapping("/getProvice")
	@ResponseBody
	public Object getProvice() {
		return sysPCDService.getProvice();
	}

	/**
	 * Description: 获取省下的市级
	 * 
	 * @return 返回json数据
	 * @see com.yz.controller.system.SysPCDController Note: Nothing much.
	 */
	@RequestMapping("/getCity")
	@ResponseBody
	public Object getCity(String proviceId) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(proviceId);
		return sysPCDService.getCity(proviceId);
	}

	/**
	 * Description: 获取市下的县级地区
	 * 
	 * @return 返回json数据
	 * @see com.yz.controller.system.SysPCDController Note: Nothing much.
	 */
	@RequestMapping("/getDistrict")
	@ResponseBody
	public Object getDistrict(String cityId) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(cityId);
		return sysPCDService.getDistrict(cityId);
	}

	/**
	 * Description: 获取省市区json
	 * 
	 * @return 返回json数据
	 * @see com.yz.controller.system.SysPCDController Note: Nothing much.
	 */
	@RequestMapping("/getAreaList")
	@ResponseBody
	public Object getAreaList() {
		// 获取所有市级
		List<SysProvince> provinceList = sysPCDService.getProvice();

		// 根据市级获取所有层级数据
		List<SysArea> resultList = sysPCDService.getList(provinceList);

		return resultList;
	}
	
	/**
	 * Description: 将省市区进行json文件缓存
	 * 
	 * @return 返回PageInfo对象json
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysPCDController Note: Nothing much.
	 */
	@RequestMapping("/pcdToJson")
	@ResponseBody
	public Object pcdToJson() {
		//缓存获取省市区
		List<Map<String, Object>> resultList = sysPCDService.queryArea();
		//4、转json格式写入json文件
		sysPCDService.setPCDMap(resultList);
		return "success";
	}
}
