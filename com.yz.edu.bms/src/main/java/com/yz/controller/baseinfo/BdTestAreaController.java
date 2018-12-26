package com.yz.controller.baseinfo;

import java.io.IOException;
import java.util.List;

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
import com.yz.model.baseinfo.BdTestArea;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.common.IPageInfo;
import com.yz.model.system.SysArea;
import com.yz.model.system.SysProvince;
import com.yz.service.baseinfo.BdTestAreaServiceImpl;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
import com.yz.util.ValidationUtil;

@RequestMapping("/testArea")
@Controller
public class BdTestAreaController {
	private static final Logger log = LoggerFactory.getLogger(BdTestAreaController.class);

	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdTestAreaServiceImpl bdTestAreaService;

	@RequestMapping("/list")
	@Rule("testArea:find")
	public String showList(HttpServletRequest request) {
		return "baseinfo/testArea/testArea-list";
	}

	@RequestMapping("/upload")
	@ResponseBody
	@Rule("testArea:upload")
	public Object upload(@RequestParam(value = "excelTestArea", required = false) MultipartFile excelTestArea) {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdTestArea> testExcelCofing = new ExcelUtil.IExcelConfig<BdTestArea>();
		testExcelCofing.setSheetName("index").setType(BdTestArea.class)
				.addTitle(new ExcelUtil.IExcelTitle("省", "provinceId"))
				.addTitle(new ExcelUtil.IExcelTitle("市", "cityId")).addTitle(new ExcelUtil.IExcelTitle("区县", "areaId"))
				.addTitle(new ExcelUtil.IExcelTitle("考区编号", "taCode"))
				.addTitle(new ExcelUtil.IExcelTitle("考取类型", "taType"))
				.addTitle(new ExcelUtil.IExcelTitle("所属校区", "ext1"))
				.addTitle(new ExcelUtil.IExcelTitle("是否可选", "isAllow"));

		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdTestArea> list = ExcelUtil.importWorkbook(excelTestArea.getInputStream(), testExcelCofing,
					excelTestArea.getOriginalFilename());

			// 遍历插入
			for (BdTestArea bdTestArea : list) {

				// 判断是否有重复数据
				boolean exitTaCode = bdTestAreaService.isExitTaCode("ADD", bdTestArea.getTaCode(), null);

				if (exitTaCode) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				}

				// 省转换
				String valueTemple = dictExchangeUtil.getAreaValue(bdTestArea.getProvinceId().trim());
				bdTestArea.setProvinceId(valueTemple);

				// 考区名称生成
				valueTemple = bdTestArea.getCityId().trim().replace("市", "")
						+ bdTestArea.getAreaId().trim().replace("县", "").replace("区", "").replace("市", "");
				bdTestArea.setTaName(valueTemple);

				// 市转换
				valueTemple = dictExchangeUtil.getAreaValue(bdTestArea.getCityId().trim());
				bdTestArea.setCityId(valueTemple);

				// 县区转换
				valueTemple = dictExchangeUtil.getAreaValue(bdTestArea.getAreaId().trim());
				bdTestArea.setAreaId(valueTemple);

				// 考区类型转换
				valueTemple = dictExchangeUtil.getParamValue("taType", bdTestArea.getTaType().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					bdTestArea.setTaType(valueTemple);
				}

				// 是否可选转换
				valueTemple = "是".equals(bdTestArea.getIsAllow().trim()) ? "1" : "0";
				bdTestArea.setIsAllow(valueTemple);

				bdTestAreaService.addTestArea(bdTestArea);

				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}

		return "SUCCESS";
	}

	@RequestMapping("/validate")
	@ResponseBody
	@Rule("testArea:insert")
	public Object valiedate(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "taCode", required = true) String taCode, String oldTaCode, HttpServletResponse resp)
			throws IOException {
		exType = exType.toUpperCase();
		return !bdTestAreaService.isExitTaCode(exType, taCode, oldTaCode);
	}

	@RequestMapping("/excelImport")
	@Rule("testArea:upload")
	public String excelImport(HttpServletRequest request) {
		return "baseinfo/testArea/testArea-import";
	}

	@RequestMapping("/edit")
	@Rule("testArea:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BdTestArea testArea = new BdTestArea();
		// 默认值：广东省，惠州市，市辖区
		/*
		 * testArea.setProvinceId("440000"); testArea.setCityId("441300");
		 * testArea.setAreaId("441301"); testArea.setTaName("惠州市辖");
		 */

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String taId = RequestUtil.getString("taId");
			Assert.hasText(taId, "参数名称不能为空");
			testArea = bdTestAreaService.getTestArea(taId);
		}
		model.addAttribute("exType", exType);
		model.addAttribute("testAreaInfo", testArea);
		return "baseinfo/testArea/testArea-edit";
	}

	/**
	 * Description: 修改考区
	 * 
	 * @param testArea
	 *            BdTestArea对象
	 * @return 返回boolean型
	 * @see com.yz.controller.baseinfo.BdTestAreaController Note: Nothing much.
	 */
	@RequestMapping("/editTestArea")
	@ResponseBody
	@Rule("testArea:update")
	public Object editTestArea(BdTestArea testArea) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(testArea.getTaId());
		// 根据唯一标识进行修改
		bdTestAreaService.updateTestArea(testArea);
		return "success";
	}

	/**
	 * Description: 新增考区
	 * 
	 * @param testArea
	 *            BdTestArea对象
	 * @return 返回boolean型
	 * @see com.yz.controller.baseinfo.BdTestAreaController Note: Nothing much.
	 */
	@RequestMapping("/insertTestArea")
	@ResponseBody
	@Rule("testArea:insert")
	public Object insertTestArea(BdTestArea testArea) {
		bdTestAreaService.addTestArea(testArea);
		return "success";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param sysErrorMessage
	 *            SysErrorMessage对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTestAreaController Note: Nothing much.
	 */
	@RequestMapping("/findAllTestArea")
	@ResponseBody
	@Rule("testArea:find")
	public Object findAllTestArea(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdTestArea testArea) {
		PageHelper.offsetPage(start, length);
		List<BdTestArea> resultList = bdTestAreaService.selectAll(testArea);
		return new IPageInfo((Page) resultList);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findAllKeyValue")
	@ResponseBody
	public Object findAllKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
								  @RequestParam(value = "page", defaultValue = "1")int page) {
		PageHelper.startPage(page, rows);
		List<BdTestArea> resultList = bdTestAreaService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 删除根据id
	 * 
	 * @param id
	 *            数据id
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTestAreaController Note: Nothing much.
	 */
	@RequestMapping("/deleteTestArea")
	@ResponseBody
	@Rule("testArea:delete")
	public Object deleteTestArea(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		bdTestAreaService.deleteTestArea(id);
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
	 * @see com.yz.controller.system.SysErrorMessageController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/deleteAllTestArea")
	@ResponseBody
	@Rule("testArea:delete")
	public Object deleteAllTestArea(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		bdTestAreaService.deleteAllTestArea(idArray);
		return "success";
	}

	/**
	 * Description: 根据省市区查询所有符合条件的考区
	 * 
	 * @param testArea
	 *            省市区以#拼接字符串
	 * @return 返回PageInfo对象json
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTestAreaController Note: Nothing much.
	 */
	@RequestMapping("/findBdTestArea")
	@ResponseBody
	@Rule("testArea:find")
	public Object findBdTestArea(String testArea) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(testArea);

		List<BdTestArea> resultList = bdTestAreaService.findBdTestArea(testArea);

		return resultList;
	}

	@RequestMapping("/findBdTestAreaByPfsnId")
	@ResponseBody
	@Rule({ "testArea:find", "studentModify:insert" })
	public Object findBdTestAreaByPfsnId(String pfsnId, String sName,
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(pfsnId);
		PageHelper.startPage(page, rows);
		List<BdTestArea> resultList = bdTestAreaService.findBdTestAreaByPfsnId(pfsnId);

		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/getAreaList")
	@ResponseBody
	@Rule({ "testArea:find", "bdUniversity:insert", "bdUniversity:update", "unvsProfession:insert",
			"unvsProfession:update" })
	public Object getAreaList(String mappingId, String type) {
		// 获取所有市级
		List<SysProvince> provinceList = bdTestAreaService.getTestAreaProvice();

		// 根据市级获取所有层级数据
		List<SysArea> resultList = bdTestAreaService.getCityList(provinceList, mappingId, type);

		return resultList;
	}
}
