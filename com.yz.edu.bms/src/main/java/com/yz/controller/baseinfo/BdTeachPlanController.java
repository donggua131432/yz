package com.yz.controller.baseinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.PSource.PSpecified;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.yz.model.baseinfo.BdPlanTextbookKey;
import com.yz.model.baseinfo.BdTeachPlan;
import com.yz.model.baseinfo.BdTeachPlanMap;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.baseinfo.TUPDemo;
import com.yz.model.common.IPageInfo;
import com.yz.service.baseinfo.BdTeachPlanExcelServiceImpl;
import com.yz.service.baseinfo.BdTeachPlanServiceImpl;
import com.yz.service.baseinfo.BdUnvsProfessionServiceImpl;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import com.yz.util.ValidationUtil;

@Controller
@RequestMapping("/teachPlan")
public class BdTeachPlanController {
	private static final Logger log = LoggerFactory.getLogger(BdTeachPlanController.class);
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdTeachPlanServiceImpl teachPlanService;
	
	@Autowired
	private BdUnvsProfessionServiceImpl professionService;
	
	@Autowired
	private BdTeachPlanExcelServiceImpl teachPlanExcelService;

	@RequestMapping("/list")
	@Rule("teachPlan:find")
	public String showList(HttpServletRequest request) {
		return "baseinfo/teachplan/teachplan-list";
	}

	@RequestMapping("/teachPlanImport")
	@Rule("teachPlan:upload")
	public String teachPlanImport(HttpServletRequest request) {
		return "baseinfo/teachplan/teachplan-import";
	}

	@RequestMapping("/textBookImport")
	@Rule("teachPlan:upload")
	public String textBookImport(HttpServletRequest request) {
		return "baseinfo/teachplan/teachplan-textbook-import";
	}
	
	@RequestMapping("/testSubjectImport")
	@Rule("teachPlan:testsubjectupload")
	public String testSubjectImport(HttpServletRequest request) {
		return "baseinfo/teachplan/teachplan-testsubject-import";
	}

	@RequestMapping("/uploadTextBook")
	@ResponseBody
	@Rule("teachPlan:upload")
	public Object uploadTextBook(
			@RequestParam(value = "excelTeachPlan", required = false) MultipartFile excelTeachPlan) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdTeachPlan> testExcelCofing = new ExcelUtil.IExcelConfig<BdTeachPlan>();
		testExcelCofing.setSheetName("index").setType(BdTeachPlan.class)
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "ext2"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnId"))
				.addTitle(new ExcelUtil.IExcelTitle("专业编码", "pfsnCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "ext3"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划名称", "thpName"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("教材编码", "ext1"))
				.addTitle(new ExcelUtil.IExcelTitle("教材名称", "ext4"));
		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdTeachPlan> list = ExcelUtil.importWorkbook(excelTeachPlan.getInputStream(), testExcelCofing,
					excelTeachPlan.getOriginalFilename());
			String grade = null;
			String semester = null;
			String pfsnLevel = null;
			boolean isTeachPlanExit = false;
			BdPlanTextbookKey planTextbookKey = null;
			// 遍历插入
			for (BdTeachPlan teachPlan : list) {
				// 年级转换
				grade = dictExchangeUtil.getParamValue("grade", teachPlan.getGrade().trim());
				if (null == grade) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					teachPlan.setGrade(grade);
				}

				// 学期转换
				semester = dictExchangeUtil.getParamValue("semester", teachPlan.getSemester().trim());
				if (null == semester) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					teachPlan.setSemester(semester);
				}
				//获取专业id 2017-11-20 add
				pfsnLevel = dictExchangeUtil.getParamValue("pfsnLevel",teachPlan.getExt3().trim());
				if(null == pfsnLevel){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业层次不能为空");
				}
				if(!StringUtil.hasValue(teachPlan.getPfsnId())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业不能为空");
				}
				if(!StringUtil.hasValue(teachPlan.getExt2())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！大学也院校不能为空");
				}
				if(!StringUtil.hasValue(teachPlan.getPfsnCode())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业编码不能为空");
				}
				//由于后续教学要处理相关专业的数据,此处不做是否禁用限制2018-01-27
				String pfsnId = teachPlanService.selectPfsnIdByCon(grade,teachPlan.getPfsnId(),teachPlan.getExt2(),pfsnLevel,teachPlan.getPfsnCode());
				if(!StringUtil.hasValue(pfsnId)){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！找不到对应的专业");
				}
				teachPlan.setPfsnId(pfsnId);
				
				isTeachPlanExit = teachPlanService.isTeachPlanExit(teachPlan);
				if (!isTeachPlanExit) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！,教学计划不存在");
				}
				BdTeachPlan tePlan = teachPlanService.selectTeachPlan(teachPlan);
				planTextbookKey = new BdPlanTextbookKey();
				planTextbookKey.setTextbookId(teachPlan.getExt1());
				planTextbookKey.setThpId(tePlan.getThpId());
				teachPlanService.insertPlanTextbook(planTextbookKey);
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
		return "SUCCESS";
	}

	@RequestMapping("/upload")
	@ResponseBody
	@Rule("teachPlan:upload")
	public Object upload(@RequestParam(value = "excelTeachPlan", required = false) MultipartFile excelTeachPlan) {
		
		teachPlanService.uploadTeachPlan(excelTeachPlan);
		
		
		return "SUCCESS";
	}
	
	
	@RequestMapping("/uploadTestSubject")
	@ResponseBody
	@Rule("teachPlan:testsubjectupload")
	public Object uploadTestSubject(@RequestParam(value = "excelTeachPlan", required = false) MultipartFile excelTeachPlan) {
		teachPlanExcelService.uploadTestSubject(excelTeachPlan);
		return "SUCCESS";
	}
	

	@RequestMapping("/edit")
	@Rule("teachPlan:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {

		String[] words = { "pfsnLevel", "grade", "unvsId", "unvsName", "thpId", "pfsnName", "pfsnId", "thpName","pfsnCode",
				"semester", "credit", "thpType", "testSubject","totalHours", "educatedHour", "selfHours", "practiceHours", "remark" };
		Map<String, Object> bdTeachPlan = new HashMap<String, Object>();
		for (String word : words) {
			bdTeachPlan.put(word, null);
		}

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String teachPlanId = RequestUtil.getString("thpId");
			Assert.hasText(teachPlanId, "参数名称不能为空");
			bdTeachPlan = teachPlanService.selectOne(teachPlanId);
		}
		model.addAttribute("exType", exType);
		model.addAttribute("teachPlanInfo", bdTeachPlan);
		return "baseinfo/teachplan/teachplan-edit";
	}

	/**
	 * Description: 修改教学计划
	 * 
	 * @param teachPlan
	 *            BdTeachPlan对象
	 * @return 返回boolean型
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/editTeachPlan")
	@ResponseBody
	@Rule("teachPlan:insert")
	public Object editTeachPlan(BdTeachPlan teachPlan) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(teachPlan.getThpId());
		// 根据唯一标识进行修改
		teachPlanService.updateTeachPlan(teachPlan);
		return "success";
	}

	/**
	 * Description: 添加教学计划
	 * 
	 * @param teachPlan
	 *            BdTeachPlan对象
	 * @return 返回boolean型字符串
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/insertTeachPlan")
	@ResponseBody
	@Rule("teachPlan:insert")
	public Object insertTeachPlan(BdTeachPlan teachPlan) {
		// 由专业编码_专业ID_学期_序号组成
		String thpId = "";

		// 拼接专业编号+专业ID
		BdUnvsProfession profession = professionService.getUnvsProfession(teachPlan.getPfsnId());
		thpId += profession.getPfsnCode();
		log.debug("专业编号--------> [" + thpId + "]");

		thpId += "_" + profession.getGrade();
		log.debug("专业编号+年级--------> [" + thpId + "]");
		// 缓存取字典拼接学期名
		thpId += "_" + teachPlan.getSemester();
		log.debug("专业编号+年级+学期--------> [" + thpId + "]");

		// 获取序号拼接序号
		int order = teachPlanService.getCount(teachPlan.getPfsnId(), teachPlan.getSemester());
		thpId += "_" + (order + 1);
		log.debug("专业编号+年级+学期+序号--------> [" + thpId + "]");

		teachPlan.setThpId(thpId);
		teachPlanService.addTeachPlan(teachPlan);
		return "success";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/findAllBdTeachPlan")
	@ResponseBody
	@Rule("teachPlan:find")
	public Object findAllBdTeachPlan(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, TUPDemo postValue) {
		PageHelper.offsetPage(start, length);
		List<BdTeachPlanMap> resultList = teachPlanService.selectAll(postValue);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 根据条件查询所有符合数据并导出
	 * 
	 * @param
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/export")
	@Rule("teachPlan:export")
	public void export(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, TUPDemo postValue,
			HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdTeachPlanMap> testExcelCofing = new ExcelUtil.IExcelConfig<BdTeachPlanMap>();
		testExcelCofing.setSheetName("index").setType(BdTeachPlanMap.class)
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业编码", "pfsnCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划名称", "thpName"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划编码", "thpId"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("学分", "credit"))
				.addTitle(new ExcelUtil.IExcelTitle("类型", "thpType"))
				.addTitle(new ExcelUtil.IExcelTitle("考试科目", "testSubject"))
				.addTitle(new ExcelUtil.IExcelTitle("总学时", "totalHours"))
				.addTitle(new ExcelUtil.IExcelTitle("面授学时", "educatedHour"))
				.addTitle(new ExcelUtil.IExcelTitle("自学学时", "selfHours"))
				.addTitle(new ExcelUtil.IExcelTitle("实践学时", "practiceHours"));

		// 查询
		//PageHelper.offsetPage(start, length);
		List<BdTeachPlanMap> resultList = teachPlanService.selectAll(postValue);

		for (BdTeachPlanMap bdTeachPlanMap : resultList) {
			// 转换专业层次
			String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", bdTeachPlanMap.getPfsnLevel());
			bdTeachPlanMap.setPfsnLevel(pfsnLevel);
			// 转换年度
			String year = dictExchangeUtil.getParamKey("year", bdTeachPlanMap.getYear());
			bdTeachPlanMap.setYear(year);
			
			// 转换年级
			String grade = dictExchangeUtil.getParamKey("grade", bdTeachPlanMap.getGrade());
			bdTeachPlanMap.setGrade(grade);
			// 学期转换
			String semester = dictExchangeUtil.getParamKey("semester", bdTeachPlanMap.getSemester());
			bdTeachPlanMap.setSemester(semester);
			// 教学计划类型
			String thpType = dictExchangeUtil
					
					.getParamKey("thpType", bdTeachPlanMap.getThpType());
			bdTeachPlanMap.setThpType(thpType);
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=TeachPlan.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Description: 删除错误信息记录根据id集合批量
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/deleteTeachPlan")
	@ResponseBody
	@Rule("teachPlan:delete")
	public Object deleteTeachPlan(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		teachPlanService.deleteTeachPlan(idArray);
		return "success";
	}

	/**
	 * Description: 删除错误信息记录根据id
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.baseinfo.BdTeachPlanController Note: Nothing much.
	 */
	@RequestMapping("/deleteTeachPlanOne")
	@ResponseBody
	@Rule("teachPlan:delete")
	public Object deleteTeachPlanOne(String id) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(id);
		teachPlanService.deleteTeachPlanOne(id);
		return "success";
	}
}
