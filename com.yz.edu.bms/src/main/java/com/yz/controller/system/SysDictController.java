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
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;
import com.yz.util.Assert;
import com.yz.util.ValidationUtil;

@RequestMapping("/sysDict")
@Controller
public class SysDictController {
	private static final Logger log = LoggerFactory.getLogger(SysDictController.class);

	@Autowired
	private SysDictService sysDictService;

	@RequestMapping("/list")
	public String showList(HttpServletRequest request) {
		return "system/sysDict-list";
	}

	/**
	 * Description: 新增字典
	 * 
	 * @param sysDict
	 *            SysDict对象
	 * @param bindingResult
	 *            错误对象
	 * @return 返回string boolean型
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/addSysDict")
	@ResponseBody
	public Object addSysDict(@Valid SysDict sysDict, BindingResult bindingResult) {
		String errors = "";
		// 是否存在错误
		if (bindingResult.hasErrors()) {
			// 得到结果集
			List<ObjectError> errorList = bindingResult.getAllErrors();
			// 取第一个就行，无需给出所有，让用户进行排查
			errors = errorList.get(0).getDefaultMessage();
			// 返回错误码和信息
			return errors;
		} else {
			// 新增
			sysDictService.addDict(sysDict);
			log.debug("-------------addSysDict插入成功！--------------" + sysDict.toString());
			return true;
		}
	}

	@RequestMapping("/validate")
	@ResponseBody
	public Object valiedate(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "dictId", required = true) String dictId, HttpServletResponse resp)
			throws IOException {
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			return true;
		} else {
			SysDict dictInfo = sysDictService.getDict(dictId);

			if (dictInfo == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		SysDict dictInfo = new SysDict();

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String dictId = RequestUtil.getString("dictId");
			Assert.hasText(dictId, "参数名称不能为空");
			dictInfo = sysDictService.getDict(dictId);
		}
		model.addAttribute("exType", exType);
		model.addAttribute("dictInfo", dictInfo);
		return "system/sysDict-edit";
	}

	/**
	 * Description: 修改字典
	 * 
	 * @param sysDict
	 *            SysDict对象
	 * @return 返回boolean型
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/editSysDict")
	@ResponseBody
	public Object editSysDict(SysDict sysDict) {

		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(sysDict.getDictId());

		// 根据唯一标识进行修改
		sysDictService.updateDict(sysDict);

		return "success";
	}

	/**
	 * Description: 新增字典
	 * 
	 * @param sysDict
	 *            SysDict对象
	 * @return 返回boolean型
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/insertSysDict")
	@ResponseBody
	public Object insertSysDict(SysDict sysDict) {

		sysDictService.addDict(sysDict);
		return "success";
	}

	/**
	 * Description: 获取所有父级元素
	 * 
	 * @return List<SysDict>
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 *//*
	@RequestMapping("/getParents")
	@ResponseBody
	public Object getParents() {
		return sysDictService.getParents();
	}*/
	
	/**
	 * Description: 获取所有父级元素
	 * 
	 * @return List<SysDict>
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/getParents")
	@ResponseBody
	public Object getParents(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page)sysDictService.getParents(sName));
	}

	/**
	 * Description: 删除字典记录根据id
	 * 
	 * @param id
	 *            数据id
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/deleteSysDict")
	@ResponseBody
	public Object deleteSysDict(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		sysDictService.deleteDict(id);
		return "success";
	}

	/**
	 * Description: 删除字典记录根据id集合批量
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/deleteAllSysDict")
	@ResponseBody
	public Object deleteAllSysDict(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		sysDictService.deleteAllSysDict(idArray);
		return "success";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param sysDict
	 *            SysDict对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/findAllSysDict")
	@ResponseBody
	public Object findAllSysDict(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, SysDict sysDict) {
		PageHelper.offsetPage(start, length);
		List<SysDict> resultList = sysDictService.selectAll(sysDict);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 根据id查询单条数据
	 * 
	 * @param id
	 * @return SysDict
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/findSysDictOne")
	@ResponseBody
	public Object findSysDictOne(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		SysDict result = sysDictService.getDict(id);
		if (null == result) {
			return "不存在!";
		}
		return result;
	}
}
