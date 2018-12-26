package com.yz.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.IPageInfo;
import com.yz.model.system.SysErrorMessage;
import com.yz.service.SysErrorMessageService;
import com.yz.util.Assert;
import com.yz.util.ValidationUtil;

@RequestMapping("/sysErrorMessage")
@Controller
public class SysErrorMessageController {
	private static final Logger log = LoggerFactory.getLogger(SysErrorMessageController.class);

	@Autowired
	private SysErrorMessageService sysErrorMessageService;

	@RequestMapping("/list")
	public String showList(HttpServletRequest request) {
		return "system/sysErrorMessage-list";
	}

	@RequestMapping("/validate")
	@ResponseBody
	public boolean valiedate(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "errorCode", required = true) String errorCode, HttpServletResponse resp)
			throws IOException {
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			return true;
		} else {
			String sysErrorMessage = sysErrorMessageService.getErrorMsg(errorCode);

			if (sysErrorMessage == null) {
				return true;
			} else {
				return false;
			}
		}
	}

//	/**
//	 * Description: 新增系统错误信息
//	 * 
//	 * @param sysErrorMessage
//	 *            SysErrorMessage对象
//	 * @param bindingResult
//	 *            错误对象
//	 * @return 返回string boolean型
//	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing much.
//	 */
//	@RequestMapping("/addSysErrorMessage")
//	@ResponseBody
//	public String addSysErrorMessage(@Valid SysErrorMessage sysErrorMessage, BindingResult bindingResult) {
//		String errors = "";
//		// 是否存在错误
//		if (bindingResult.hasErrors()) {
//			// 得到结果集
//			List<ObjectError> errorList = bindingResult.getAllErrors();
//			// 取第一个就行，无需给出所有，让用户进行排查
//			errors = errorList.get(0).getDefaultMessage();
//			// 返回错误码和信息
//			return errors;
//		} else {
//			sysErrorMessage.setSysBelong("bms");
//			// 新增
//			sysErrorMessageService.addErrorMsg(sysErrorMessage);
//			log.debug("-------------addSysErrorMessage插入成功！--------------" + sysErrorMessage.toString());
//			return "success";
//		}
//	}
//
//	@RequestMapping("/edit")
//	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
//			Model model) {
//		SysErrorMessage sysErrorMessage = new SysErrorMessage();
//
//		if ("UPDATE".equals(exType.trim().toUpperCase())) {
//			String errorCode = RequestUtil.getString("errorCode");
//			Assert.hasText(errorCode, "参数名称不能为空");
//			sysErrorMessage = sysErrorMessageService.getErrorMsg(errorCode);
//		}
//		model.addAttribute("exType", exType);
//		model.addAttribute("sysErrorMessageInfo", sysErrorMessage);
//		return "system/sysErrorMessage-edit";
//	}
//	
//	/**
//	 * Description: 修改系统错误信息
//	 * 
//	 * @param sysErrorMessage
//	 *            SysErrorMessage对象
//	 * @return 返回boolean型
//	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing much.
//	 */
//	@RequestMapping("/editSysErrorMessage")
//	@ResponseBody
//	public String editSysErrorMessage(SysErrorMessage sysErrorMessage) {
//			// 校验唯一标识不能为空
//			ValidationUtil.getInstance().isNotEmptyIgnoreBlank(sysErrorMessage.getErrorCode());
//			// 根据唯一标识进行修改
//			sysErrorMessageService.updateErrorMsg(sysErrorMessage);
//		    return "success";
//	}
//	
//	/**
//	 * Description: 删除错误信息记录根据id
//	 * 
//	 * @param id
//	 *            数据id
//	 * @return 返回boolean型
//	 * @throws Exception
//	 *             抛出一个异常
//	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing much.
//	 */
//	@RequestMapping("/deleteSysErrorMessage")
//	@ResponseBody
//	public String deleteSysErrorMessage(String id) {
//		// 校验唯一标识不能为空
//		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
//		sysErrorMessageService.deleteErrorMsg(id);
//		return "success";
//	}
//	
//	/**
//	 * Description: 删除错误信息记录根据id集合批量
//	 * 
//	 * @param idArray
//	 *            String[]集合
//	 * @return 返回boolean型
//	 * @throws Exception
//	 *             抛出一个异常
//	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing much.
//	 */
//	@RequestMapping("/deleteAllSysErrorMessage")
//	@ResponseBody
//	public String deleteAllSysErrorMessage(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
//		sysErrorMessageService.deleteAllSysErrorMessage(idArray);
//		return "success";
//	}
//	
//	/**
//	 * Description: 根据id查询单条数据
//	 * @param id
//	 * @return SysErrorMessage
//	 * @throws Exception
//	 *             抛出一个异常
//	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing much.
//	 */
//	@RequestMapping("/findSysErrorMessage")
//	@ResponseBody
//	public Object findSysErrorMessage(String id) {
//		// 校验唯一标识不能为空
//		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
//		SysErrorMessage sysErrorMessage = sysErrorMessageService.getErrorMsg(id);
//		if(null == sysErrorMessage){
//			return "不存在!";
//		}
//		return sysErrorMessage;
//	}
//	
//	/**
//	 * Description: 根据条件查询所有符合数据
//	 * @param sysErrorMessage
//	 * SysErrorMessage对象字段
//	 * @return 返回PageInfo对象json
//	 * @return 返回pageSize对象每页显示长度
//	 * @return 返回page对象页码
//	 * @throws Exception
//	 *             抛出一个异常
//	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
//	 */
//	@RequestMapping("/findAllSysErrorMessage")
//	@ResponseBody
//	public Object findAllSysErrorMessage(@RequestParam(value = "start", defaultValue = "1") int start,
//			@RequestParam(value = "length", defaultValue = "10") int length, SysErrorMessage sysErrorMessage) {
//		PageHelper.offsetPage(start, length);
//		List<SysErrorMessage> resultList = sysErrorMessageService.selectAll(sysErrorMessage);
//		return new IPageInfo((Page) resultList);
//	}
}
