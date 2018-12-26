package com.yz.controller.baseinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.baseinfo.BdUniversity;
import com.yz.model.common.IPageInfo;
import com.yz.service.baseinfo.BdUniversityServiceImpl;
import com.yz.util.Assert;
import com.yz.util.ValidationUtil;

@RequestMapping("/bdUniversity")
@Controller
public class BdUniversityController {
	private static final Logger log = LoggerFactory.getLogger(BdUniversityController.class);

	@Autowired
	private BdUniversityServiceImpl bdUniversityService;

	@RequestMapping("/list")
	@Rule("bdUniversity:find")
	public String showList(HttpServletRequest request) {
		return "baseinfo/universities/universities-list";
	}

	@RequestMapping("/findAllKeyValue")
	@ResponseBody
	public Object findAllKeyValue(String sName, @RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = bdUniversityService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/edit")
	@Rule("bdUniversity:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BdUniversity bdUniversity = new BdUniversity();

		Map<String, Map<String, String>> taMap = new HashMap<String, Map<String, String>>();

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String unvsId = RequestUtil.getString("unvsId");
			// 校验唯一标识不能为空
			Assert.hasText(unvsId, "参数名称不能为空");
			// 获取考试地区
			taMap = bdUniversityService.getTaMap(unvsId);
			bdUniversity = bdUniversityService.getBdUniversity(unvsId);
		}

		model.addAttribute("exType", exType);
		model.addAttribute("taMap", taMap);
		model.addAttribute("bdUniversityInfo", bdUniversity);
		return "baseinfo/universities/universities-edit";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param bdUniversity
	 *            BdUniversity对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/findAllBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:find")
	public Object findAllSysErrorMessage(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdUniversity bdUniversity) {
		PageHelper.offsetPage(start, length);
		List<BdUniversity> resultList = bdUniversityService.selectAll(bdUniversity);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 新增大学院校
	 * 
	 * @param bdUniversity
	 *            BdUniversity对象
	 * @param bindingResult
	 *            错误对象
	 * @return 返回string boolean型
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/addBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:insert")
	public Object addBdUniversity(@Valid BdUniversity bdUniversity, BindingResult bindingResult) {
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
			bdUniversityService.addBdUniversity(bdUniversity);
			log.debug("-------------addBdUniversity插入成功！--------------" + bdUniversity.toString());
			return true;
		}
	}

	/**
	 * Description: 修改大学院校
	 * 
	 * @param bdUniversity
	 *            BdUniversity对象字段
	 * @return 返回boolean型
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/editBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:update")
	public Object editBdUniversity(BdUniversity bdUniversity, String testAreaId) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(bdUniversity.getUnvsId());
		// 根据唯一标识进行修改
		bdUniversityService.updateBdUniversity(bdUniversity);

		if (null != testAreaId && "" != testAreaId) {

			String[] taIds = testAreaId.split("#");
			
			bdUniversityService.batchAddBdUniversityTest(bdUniversity.getUnvsId(), taIds);

		}
		return "success";
	}

	/**
	 * Description: 新增大学院校
	 * 
	 * @param bdUniversity
	 *            BdUniversity对象字段
	 * @return 返回boolean型
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/insertBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:insert")
	public Object insertBdUniversity(BdUniversity bdUniversity, String testAreaId) {
		bdUniversityService.addBdUniversity(bdUniversity);

		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(testAreaId);

		String[] AreaId = testAreaId.split("#");
		for (String id : AreaId) {
			if ("" != id) {
				// 插入院校与考区的对应关系
				bdUniversityService.addBdUniversityTest(bdUniversity.getUnvsId(), id);
			}
		}
		return "success";
	}

	/**
	 * Description: 删除错误信息记录根据id集合批量
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/deleteAllBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:delete")
	public Object deleteAllBdUniversity(@RequestParam(name = "idArray[]", required = true) String[] idArray) {

		bdUniversityService.deleteAllBdUniversity(idArray);
		// 删除对应考区关系表
		/*
		 * for (String id : idArray) { bdUniversityService.deleteBdUniversityTest(id); }
		 */
		return "success";
	}

	/**
	 * Description: 删除错误信息记录根据id
	 * 
	 * @param id
	 *            数据id
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/deleteBdUniversity")
	@ResponseBody
	@Rule("bdUniversity:delete")
	public Object deleteBdUniversity(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		bdUniversityService.deleteBdUniversity(id);
		// 删除对应考区关系表
		/* bdUniversityService.deleteBdUniversityTest(id); */
		return "success";
	}

	/**
	 * Description: 根据院校名称模糊搜索
	 * 
	 * @param id
	 * @return map
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdUniversityController Note: Nothing much.
	 */
	@RequestMapping("/searchUniversity")
	@ResponseBody
	@Rule("bdUniversity:find")
	public Object searchUniversity(String sName, @RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		// 校验唯一标识不能为空
		List<Map<String, String>> unvsUniversityList = bdUniversityService.searchUniversity(sName);
		if (null == unvsUniversityList) {
			return "不存在!";
		}
		return new IPageInfo((Page) unvsUniversityList);
	}
}
