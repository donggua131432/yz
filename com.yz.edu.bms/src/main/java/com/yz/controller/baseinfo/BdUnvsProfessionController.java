package com.yz.controller.baseinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.common.IPageInfo;
import com.yz.model.system.SysArea;
import com.yz.service.baseinfo.BdUniversityServiceImpl;
import com.yz.service.baseinfo.BdUnvsProfessionServiceImpl;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
import com.yz.util.ValidationUtil;

@Controller
@RequestMapping("/unvsProfession")
public class BdUnvsProfessionController {
	private static final Logger log = LoggerFactory.getLogger(BdUnvsProfessionController.class);

	@Autowired
	private BdUnvsProfessionServiceImpl unvsProfessionService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdUniversityServiceImpl bdUniversityService;

	@RequestMapping("/list")
	@Rule("unvsProfession:find")
	public String showList(HttpServletRequest request) {
		return "baseinfo/unvsProfession/unvsProfession-list";
	}

	@RequestMapping("/excelImport")
	@Rule("unvsProfession:upload")
	public String excelImport(HttpServletRequest request) {
		return "baseinfo/unvsProfession/unvsProfession-import";
	}

	@RequestMapping("/validate")
	@ResponseBody
	@Rule("unvsProfession:insert")
	public Object valiedate(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "paramName", required = true) String paramName, HttpServletResponse resp)
			throws IOException {
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			return true;
		} else {
			BdUnvsProfession unvsProfession = unvsProfessionService.getParamByPfsnCode(paramName);

			if (unvsProfession == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	@RequestMapping(value = "/upload.do")
	@ResponseBody
	@Rule("unvsProfession:upload")
	public Object upload(@RequestParam(value = "excelProfessional", required = false) MultipartFile excelProfessional) {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdUnvsProfession> testExcelCofing = new ExcelUtil.IExcelConfig<BdUnvsProfession>();
		testExcelCofing.setSheetName("index").setType(BdUnvsProfession.class)
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsId"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业编码", "pfsnCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业大类", "pfsnCata"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("授课方式", "teachMethod"))
				.addTitle(new ExcelUtil.IExcelTitle("最低分数线", "passScore"))
				.addTitle(new ExcelUtil.IExcelTitle("入学考试科目", "testSubject"))
				.addTitle(new ExcelUtil.IExcelTitle("是否可选", "isAllow"))
				.addTitle(new ExcelUtil.IExcelTitle("年度", "year"));

		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdUnvsProfession> list = ExcelUtil.importWorkbook(excelProfessional.getInputStream(), testExcelCofing,
					excelProfessional.getOriginalFilename());

			// 遍历插入
			for (BdUnvsProfession bdUnvsProfession : list) {

				// 判断是否有重复数据
				BdUnvsProfession unvsProfession = unvsProfessionService
						.getParamByPfsnCode(bdUnvsProfession.getPfsnCode());

				if (null != unvsProfession) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				}

				// 年级转换
				String valueTemple = dictExchangeUtil.getParamValue("grade", bdUnvsProfession.getGrade());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setGrade(valueTemple);
				}

				// 年度转换
				valueTemple = dictExchangeUtil.getParamValue("year", bdUnvsProfession.getYear());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setYear(valueTemple);
				}

				// 专业大类转换
				valueTemple = dictExchangeUtil.getParamValue("pfsnCata", bdUnvsProfession.getPfsnCata());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setPfsnCata(valueTemple);
				}

				// 专业层次转换
				valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", bdUnvsProfession.getPfsnLevel());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setPfsnLevel(valueTemple);
				}

				// 授课方式转换
				valueTemple = dictExchangeUtil.getParamValue("teachMethod", bdUnvsProfession.getTeachMethod());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setTeachMethod(valueTemple);
				}

				// 院校转换
				valueTemple = bdUniversityService.getBdUniversityByName(bdUnvsProfession.getUnvsId());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setUnvsId(valueTemple);
				}

				// 是否可选转换
				String isAllow = bdUnvsProfession.getIsAllow();
				if (null == isAllow || isAllow.length() < 1) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdUnvsProfession.setIsAllow("是".equals(isAllow) ? "1" : "0");
				}

				unvsProfessionService.addBdProfession(bdUnvsProfession, null);

				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
		return "SUCCESS";
	}

	@RequestMapping("/edit")
	@Rule("unvsProfession:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BdUnvsProfession unvsProfession = new BdUnvsProfession();
		Map<String, Map<String, String>> taMap = new HashMap<String, Map<String, String>>();

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String pfsnId = RequestUtil.getString("pfsnId");
			Assert.hasText(pfsnId, "参数名称不能为空");
			// 获取考试地区
			taMap = unvsProfessionService.getTestArea(pfsnId);
			unvsProfession = unvsProfessionService.getUnvsProfession(pfsnId);
		}

		unvsProfession.setShowTestSubject(unvsProfessionService.findTestSubjectByGroupId(unvsProfession.getGroupId()));

		model.addAttribute("exType", exType);
		model.addAttribute("taMap", taMap);
		model.addAttribute("UnvsProfession", unvsProfession);
		return "baseinfo/unvsProfession/unvsProfession-edit";
	}

	/**
	 * 修改大学院校
	 * @param unvsProfession
	 * @param testAreaId
	 * @param testSubjects
	 * @return
	 */
	@RequestMapping("/editBdUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:update")
	public Object editBdUnvsProfession(BdUnvsProfession unvsProfession, String[] testAreaId, String[] testSubjects) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(unvsProfession.getPfsnId());
		StringBuilder strBuilder = new StringBuilder();
		// 根据唯一标识进行修改
		if (null != testSubjects) {
			for (int i = 0, len = testSubjects.length; i < len; i++) {
				if (i < (len - 1))
					strBuilder.append(testSubjects[i] + ",");
				else
					strBuilder.append(testSubjects[i]);
			}
		}
		unvsProfession.setTestSubject(strBuilder.toString());
		unvsProfessionService.updateBdUnvsProfession(unvsProfession, testAreaId);

		return "success";
	}

	/**
	 * 新增大学院校专业
	 * @param unvsProfession
	 * @param testAreaId
	 * @param testSubjects
	 * @return
	 */
	@RequestMapping("/insertBdUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:insert")
	public Object insertBdUnvsProfession(BdUnvsProfession unvsProfession, String[] testAreaId, String[] testSubjects) {
		StringBuilder strBuilder = new StringBuilder();
		if (null != testSubjects) {
			for (int i = 0, len = testSubjects.length; i < len; i++) {
				if (i < (len - 1))
					strBuilder.append(testSubjects[i] + ",");
				else
					strBuilder.append(testSubjects[i]);
			}
		}
		unvsProfession.setTestSubject(strBuilder.toString());
		unvsProfessionService.addBdProfession(unvsProfession, testAreaId);

		return "success";
	}

	/**
	 * 删除错误信息记录根据id集合批量
	 * @param idArray
	 * @return
	 */
	@RequestMapping("/deleteAllUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:delete")
	public Object deleteAllUnvsProfession(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		unvsProfessionService.deleteAllUnvsProfession(idArray);
		return "success";
	}

	/**
	 * 删除单条错误信息记录根据id
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:delete")
	public Object deleteUnvsProfession(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		unvsProfessionService.deleteUnvsProfession(id);
		return "success";
	}

	/**
	 * 根据id集合批量禁用或启用
	 * @param idArray
	 * @param exType
	 * @return
	 */
	@RequestMapping("/opendownAllUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:open")
	public Object opendownAllUnvsProfession(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(name = "exType", required = true) String exType) {
		unvsProfessionService.opendownAllUnvsProfession(idArray, exType);
		return "success";
	}

	/**
	 * 根据id查询单条数据
	 * @param id
	 * @return
	 */
	@RequestMapping("/findUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:find")
	public Object findUnvsProfession(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		BdUnvsProfession unvsProfession = unvsProfessionService.getUnvsProfession(id);
		if (null == unvsProfession) {
			return "不存在!";
		}

		return unvsProfession;
	}

	/**
	 * 根据专业名称模糊搜索
	 * @param sName
	 * @param pfsnName
	 * @param unvsId
	 * @param pfsnLevel
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/searchGradeJson")
	@ResponseBody
	@Rule("unvsProfession:find")
	public Object searchGradeJson(String sName, String pfsnName, String unvsId, String pfsnLevel,
			@RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> unvsProfessionList = unvsProfessionService.searchGradeJson(sName, pfsnName, unvsId,
				pfsnLevel);
		if (null == unvsProfessionList) {
			return "不存在!";
		}
		return new IPageInfo((Page) unvsProfessionList);
	}

	/**
	 * 根据专业名称模糊搜索
	 * @param sName
	 * @param unvsId
	 * @param pfsnLevel
	 * @param grade
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/searchProfessionJson")
	@ResponseBody
	@Rule({ "unvsProfession:find", "studentModify:insert" })
	public Object searchProfessionJson(String sName, String unvsId, String pfsnLevel, String grade,
			@RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> unvsProfessionList = unvsProfessionService.searchProfessionJson(sName, unvsId,
				pfsnLevel, grade);
		if (null == unvsProfessionList) {
			return "不存在!";
		}
		return new IPageInfo((Page) unvsProfessionList);
	}

	/**
	 * 根据条件查询所有符合数据
	 * @param start
	 * @param length
	 * @param unvsProfession
	 * @return
	 */
	@RequestMapping("/findAllUnvsProfession")
	@ResponseBody
	@Rule("unvsProfession:find")
	public Object findAllUnvsProfession(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdUnvsProfession unvsProfession) {
		PageHelper.offsetPage(start, length);
		List<BdUnvsProfession> resultList = unvsProfessionService.selectAll(unvsProfession);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/findAllKeyValue")
	@ResponseBody
	@Rule("unvsProfession:find")
	public Object findAllKeyValue(String sName, @RequestParam(value = "rows", defaultValue = "10000") int rows,
								  @RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = unvsProfessionService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}

	/**
	 * 考试科目组
	 * @param pfsnLevel
	 * @return
	 */
	@RequestMapping("/findTestGroup")
	@ResponseBody
	public Object findTestGroup(String pfsnLevel) {
		List<Map<String, String>> resultList = unvsProfessionService.findTestGroupByPfsnLevel(pfsnLevel);

		if (null == resultList) {
			return "不存在!";
		}

		IPageInfo<Map<String, String>> pageInfo = new IPageInfo<>(resultList, resultList.size());

		return pageInfo;
	}

	/**
	 * 考试科目
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/findTestSubject")
	@ResponseBody
	public Object findTestSubject(String groupId) {
		String resultList = unvsProfessionService.findTestSubjectByGroupId(groupId);
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}

	@RequestMapping("/getAreaList")
	@ResponseBody
	@Rule({ "unvsProfession:insert", "unvsProfession:update" })
	public Object getAreaList(String pfsnId, String unvsId) {
		// 获取所有市级
		List<SysArea> resultList = unvsProfessionService.getTestAreaProvice(pfsnId, unvsId);
		return resultList;
	}

	@RequestMapping("/searchAllowProfessionJson")
	@ResponseBody
	@Rule({ "unvsProfession:find", "studentModify:insert" })
	public Object searchAllowProfessionJson(String sName, String unvsId, String pfsnLevel, String grade,
									   @RequestParam(value = "rows", defaultValue = "5") int rows,
									   @RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> unvsProfessionList = unvsProfessionService.searchAllowProfessionJson(sName, unvsId,
				pfsnLevel, grade);
		if (null == unvsProfessionList) {
			return "不存在!";
		}
		return new IPageInfo((Page) unvsProfessionList);
	}

}
