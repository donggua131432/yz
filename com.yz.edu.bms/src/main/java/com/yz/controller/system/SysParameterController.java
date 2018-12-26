package com.yz.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yz.conf.YzSysConfig;

@RequestMapping("/sysParameter")
@Controller
public class SysParameterController {
	
	@Autowired
	private YzSysConfig yzSysConfig;

	@RequestMapping("/list")
	public String showList(HttpServletRequest request) {
		return "system/sysParam-list";
	}
	
//	@RequestMapping("/listData")
//	@ResponseBody
//	public Object listData(@RequestParam(name="start", defaultValue="0") int start, @RequestParam(name="length", defaultValue="10") int length,String paramName,String paramValue, HttpServletRequest request) {
//		return sysParameterService.selectByPage(start, length,paramName,paramValue);
//	}
//	
//	@RequestMapping("/edit")
//	public String edit(@RequestParam(name="exType", required=true) String exType, HttpServletRequest request, Model model) {
//		SysParameter paramInfo =  new SysParameter();
//		
//		if("UPDATE".equals(exType.trim().toUpperCase())) {
//			String paramName = RequestUtil.getString("paramName");
//			Assert.hasText(paramName, "参数名称不能为空");
//			paramInfo = sysParameterService.getParam(paramName.trim());
//		}
//		
//		model.addAttribute("paramInfo", paramInfo);
//		model.addAttribute("exType", exType);
//		return "system/sysParam-edit";
//	}
//	
//	@RequestMapping("/delete")
//	@ResponseBody
//	public String delete(@RequestParam(name="paramNames[]", required=true) String[] paramNames, HttpServletRequest request) {
//		sysParameterService.deleteAll(paramNames);
//		return "success";
//	}
//	
//	@RequestMapping("/delOne")
//	@ResponseBody
//	public String delOne(@RequestParam(name="paramName", required=true) String paramName) {
//		sysParameterService.deleteParam(paramName);
//		return "success";
//	}
//	
//	@RequestMapping("/validate")
//	@ResponseBody
//	public boolean valiedate(@RequestParam(name="exType", required=true) String exType,
//			@RequestParam(name="paramName", required=true) String paramName,
//			HttpServletResponse resp) throws IOException {
//		if("UPDATE".equals(exType.trim().toUpperCase())) {
//			return true;
//		} else {
//			SysParameter paramInfo = sysParameterService.getParam(paramName);
//			
//			if(paramInfo == null) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
//	
//	@RequestMapping("/update")
//	@ResponseBody
//	public String update(SysParameter paramInfo,
//			HttpServletRequest request) {
//			sysParameterService.updateParam(paramInfo);
//		return "success";
//	}
//
//	
//	@RequestMapping("/insert")
//	@ResponseBody
//	public String insert(SysParameter paramInfo,
//			HttpServletRequest request) {
//		
//			sysParameterService.addParam(paramInfo);
//			
//		    return "success";
//	}
	
	
//	@RequestMapping("/selectAll")
//	@ResponseBody
//	public Object selectAll(HttpServletRequest request) {
//		return sysParameterService.sellectAll();
//	}
}
