package com.yz.controller.educational;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.baseinfo.BdCourse;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdClassPlan;
import com.yz.model.educational.BdTimeTableQuery;
import com.yz.model.educational.MakeSchedule;
import com.yz.model.oa.EmployeeSelectInfo;
import com.yz.model.oa.OaCampusInfo;
import com.yz.service.educational.BdCourseExcelService;
import com.yz.service.educational.BdClassPlanService;
import com.yz.service.educational.BdCourseService;
import com.yz.service.oa.OaCampusService;
import com.yz.service.oa.OaEmployeeService;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@RequestMapping("/classPlan")
@Controller
public class BdClassPlanController {

	@Autowired
	private BdClassPlanService classPlanService;
	@Autowired
	private BdCourseService courseService;
	@Autowired
	private OaEmployeeService employeeService;
	@Autowired
	private OaCampusService oaCampusService;
	
	@Autowired
	private BdCourseExcelService classPlanExcelService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@RequestMapping("/list")
	@Rule("classPlan:find")
	public String showList(HttpServletRequest request) {
		return "educational/classPlan/class-plan-list";
	}
	
	@RequestMapping("/makeScheduleShow")
	@Rule("classPlan:find")
	public String makeScheduleShow(HttpServletRequest request,Model model,BdTimeTableQuery query) {
		model.addAttribute("initdata", query);
		return "/educational/classPlan/class-plan_curriculum";
	}
	
	@RequestMapping("/findMakeSchedule")
	@ResponseBody
	@Rule("classPlan:find")
	public Object findMakeSchedule(MakeSchedule makeSchedule) {
		Map<String,Object> makeScheduleMap = classPlanService.findMakeSchedule(makeSchedule);
		return makeScheduleMap;
	}
	
	@RequestMapping("/toDistribution")
	@Rule("classPlan:update")
	public String toDistribution(HttpServletRequest request,@RequestParam(name = "idArray[]", required = true) String[] idArray,Model model) {
		model.addAttribute("idArray",idArray);
		return "educational/classPlan/class-plan-distribution";
	}
	
	@RequestMapping("/edit")
	@Rule("classPlan:find")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {        
		String[] arr = {"cpId","courseType","courseName","courseId","cpType","classTime","otherFee","empName","empId","campusId","campusName","startTime","endTime","cpDate"};
		Map<String, Object> map = new HashMap<String, Object>();
		for (String name : arr) {
			map.put(name, "");
		}
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String cpId = RequestUtil.getString("cpId");
			Assert.hasText(cpId, "参数cpId不能为空");
			map = findClassPlanById(cpId);
		}
		model.addAttribute("exType", exType);
		model.addAttribute("classPlanInfo", map);
		return "educational/classPlan/class-plan-edit";
	}

	@RequestMapping("/deleteClassPlan")
	@ResponseBody
	@Rule("classPlan:delete")
	public Object deleteClassPlan(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		classPlanService.deleteClassPlan(idArray);
		return "success";
	}
	
	@RequestMapping("/excelImport")
	@Rule("classPlan:upload")
	public String excelImport(HttpServletRequest request) {
		return "/educational/classPlan/class-plan-import";
	}
	@RequestMapping("/uploadExcel")
	@ResponseBody
	@Rule("classPlan:upload")
	public Object uploadExcel(@RequestParam(value = "excelClassPlan", required = false) MultipartFile excelClassPlan) {
		
		//对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdClassPlan> testExcelCofing = new ExcelUtil.IExcelConfig<BdClassPlan>();
		testExcelCofing.setSheetName("index").setType(BdClassPlan.class)
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType"))
				.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("分校", "placeId"))
				.addTitle(new ExcelUtil.IExcelTitle("上课方式", "cpType"))
				.addTitle(new ExcelUtil.IExcelTitle("上课日期", "startTime"))
				.addTitle(new ExcelUtil.IExcelTitle("上课时间", "endTime"))
				.addTitle(new ExcelUtil.IExcelTitle("交通补助", "money"))
				.addTitle(new ExcelUtil.IExcelTitle("授课老师", "empName"));

		//行数记录
		int index = 2;
		try {
			//对文件进行分析转对象
			List<BdClassPlan> list = ExcelUtil.importWorkbook(excelClassPlan.getInputStream(), testExcelCofing,excelClassPlan.getOriginalFilename());
			
			for (BdClassPlan classPlan : list) {
				
				if (!StringUtil.hasValue(classPlan.getCourseName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课程名称不能为空");
                }
				
//				if (!StringUtil.hasValue(classPlan.getPlaceId())) {
//                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！分校不能为空");
//                }
				if (!StringUtil.hasValue(classPlan.getStartTime())) {

					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！上课日期不能为空");
                }else {
                		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				         try {
				        	format.setLenient(false);
							format.parse(classPlan.getStartTime());
						} catch (ParseException e) {
							throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！上课日期格式错误，正确格式如:[2018-01-26]");
						}
                }
				if (!StringUtil.hasValue(classPlan.getEndTime())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！上课时间不能为空");
                }
				
				
				//年度转换
				String valueTemple = dictExchangeUtil.getParamValue("year", classPlan.getYear());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel年度数据格式错误，请检查第"+index+"行数据！");
				}else{
					classPlan.setYear(valueTemple);
				}
				
				//课程类型转换
				valueTemple = dictExchangeUtil.getParamValue("courseType", classPlan.getCourseType());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel课程类型数据格式错误，请检查第"+index+"行数据！");
				}else{
					classPlan.setCourseType(valueTemple);
				}
				
				//上课方式类型转换
				valueTemple = dictExchangeUtil.getParamValue("cpType", classPlan.getCpType());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel上课方式数据格式错误，请检查第"+index+"行数据！正确格式如:[面授|网络|自习|青书学堂|面授+网络|直播|面授+直播|自行学习]");
				}else{
					classPlan.setCpType(valueTemple);
				}
				
				try {
					classPlan.setCpDate(classPlan.getStartTime());
					classPlan.setExt1(classPlan.getEndTime().split("-")[0].trim());
					classPlan.setExt2(classPlan.getEndTime().split("-")[1].trim());
					
					int n1 = Integer.parseInt(classPlan.getExt1().split(":")[0]) * 60+Integer.parseInt(classPlan.getExt1().split(":")[1]);
					int n2 = Integer.parseInt(classPlan.getExt2().split(":")[0]) * 60+Integer.parseInt(classPlan.getExt2().split(":")[1]);
					classPlan.setClassTime(String.valueOf(n2-n1));
					classPlan.setStartTime(classPlan.getCpDate() + " " + classPlan.getExt1());
					classPlan.setEndTime(classPlan.getCpDate() + " " + classPlan.getExt2());

				}catch (Exception e) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！上课时间格式错误，正确格式如如:(14:30-16:30)");
				}
				

				index++;
			}//for end
			 //批量查找不存在的课程信息
			List<Map<String, Object>> courseList = classPlanExcelService.getNonExistsCourse(list);
            if (courseList!=null&&courseList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("课程名称不存在：<br/>"); 
                for (Map<String, Object> map : courseList) {
                    sb.append(dictExchangeUtil.getParamKey("courseType",map.get("course_type").toString()) +"-"+map.get("year") +"年-"+map.get("course_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            
            
			//批量查找不存在的分校信息
			List<Map<String, Object>> campusList = classPlanExcelService.getNonExistsCampus(list);
            if (campusList!=null&&campusList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("分校不存在：<br/>"); 
                for (Map<String, Object> map : campusList) {
                    sb.append(map.get("campus_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            
            //批量查找不存在的教师信息
			List<Map<String, Object>> employeeList = classPlanExcelService.getNonExistsEmployee(list);
            if (employeeList!=null&&employeeList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("授课老师不存在：<br/>"); 
                for (Map<String, Object> map : employeeList) {
                    sb.append("分校："+map.get("campus_name") + "-"+map.get("emp_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            
            //批量插入
            BaseUser user=SessionUtil.getUser();
            classPlanExcelService.insertByExcel(list,user);
            
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！");
		}
		
		return "SUCCESS";
	}
	
	@RequestMapping("/uploadExcel1")
	@ResponseBody
	@Rule("classPlan:upload")
	public Object uploadExcel1(@RequestParam(value = "excelClassPlan", required = false) MultipartFile excelClassPlan) {
		
		//对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdClassPlan> testExcelCofing = new ExcelUtil.IExcelConfig<BdClassPlan>();
		testExcelCofing.setSheetName("index").setType(BdClassPlan.class)
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType"))
				.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("分校", "placeId"))
				.addTitle(new ExcelUtil.IExcelTitle("上课方式", "cpType"))
				.addTitle(new ExcelUtil.IExcelTitle("上课日期", "startTime"))
				.addTitle(new ExcelUtil.IExcelTitle("上课时间", "endTime"))
				.addTitle(new ExcelUtil.IExcelTitle("交通补助", "money"))
				.addTitle(new ExcelUtil.IExcelTitle("授课老师", "empName"));

		//行数记录
		int index = 2;
		try {
			//对文件进行分析转对象
			List<BdClassPlan> list = ExcelUtil.importWorkbook(excelClassPlan.getInputStream(), testExcelCofing,excelClassPlan.getOriginalFilename());
			//遍历插入
			BdCourse course = null;
			for (BdClassPlan classPlan : list) {
				course = new BdCourse();
				course.setCourseName(classPlan.getCourseName());
				//年度转换
				String valueTemple = dictExchangeUtil.getParamValue("year", classPlan.getYear());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel年度数据格式错误，请检查第"+index+"行数据！");
				}else{
					classPlan.setYear(valueTemple);
				}
				course.setYear(classPlan.getYear());
				
				//课程类型转换
				valueTemple = dictExchangeUtil.getParamValue("courseType", classPlan.getCourseType());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel课程类型数据格式错误，请检查第"+index+"行数据！");
				}else{
					classPlan.setCourseType(valueTemple);
				}
				course.setCourseType(classPlan.getCourseType());
				
				//上课方式类型转换
				valueTemple = dictExchangeUtil.getParamValue("cpType", classPlan.getCpType());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel上课方式数据格式错误，请检查第"+index+"行数据！");
				}else{
					classPlan.setCpType(valueTemple);
				}
				
				
				//得到courseId
				List<BdCourse> courseList = courseService.selectAll(course);
				if(null != courseList && courseList.size()>0) {
					course = courseList.get(0);
				}	
				else {
					throw new IllegalArgumentException("excel课程名称数据格式错误，请检查第"+index+"行数据！");
				}
				classPlan.setCourseId(course.getCourseId());
				
				
				List<OaCampusInfo> campusList = oaCampusService.findAllKeyValue(classPlan.getPlaceId());
				if(null != campusList&&campusList.size()>0){
					classPlan.setPlaceId(campusList.get(0).getCampusId());
				}else {
					throw new IllegalArgumentException("excel分校在数据库中不存在，请检查第"+index+"行数据！");
				}
				if(StringUtil.hasValue(classPlan.getEmpId())) {
					List<EmployeeSelectInfo> employeeList= employeeService.findEmployeeByName(classPlan.getEmpName(),classPlan.getPlaceId());
					if(null != employeeList&&employeeList.size()>0){
						classPlan.setEmpId(employeeList.get(0).getEmpId());
					}else {
						throw new IllegalArgumentException("excel授课老师在数据库中不存在，请检查第"+index+"行数据！");
					}
					
				}
				
				classPlan.setCpDate(classPlan.getStartTime());
				classPlan.setExt1(classPlan.getEndTime().split("-")[0].trim());
				classPlan.setExt2(classPlan.getEndTime().split("-")[1].trim());
				
				int n1 = Integer.parseInt(classPlan.getExt1().split(":")[0]) * 60+Integer.parseInt(classPlan.getExt1().split(":")[1]);
				int n2 = Integer.parseInt(classPlan.getExt2().split(":")[0]) * 60+Integer.parseInt(classPlan.getExt2().split(":")[1]);
				classPlan.setClassTime(String.valueOf(n2-n1));
				
				classPlanService.importClassPlan(classPlan, classPlan.getMoney());
				index++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！");
		}
		
		return "SUCCESS";
	}
	
	
	
	/*
	 * @Description:新增课程安排
	 */
	@RequestMapping("/insertClassPlan")
	@ResponseBody
	@Rule("classPlan:insert")
	public Object insertClassPlan(BdClassPlan classPlan, String money) {
		classPlanService.insertClassPlan(classPlan, money);
		return "SUCCESS";
	}

	/*
	 * @Description:更新课程安排
	 */
	@RequestMapping("/updateClassPlan")
	@ResponseBody
	@Rule("classPlan:update")
	public Object updateClassPlan(BdClassPlan classPlan, String money) {
		classPlanService.updateClassPlan(classPlan, money);
		return "SUCCESS";
	}
    
	
	@RequestMapping("/distributionTeacher")
	@ResponseBody
	@Rule("classPlan:update")
	public Object distributionTeacher(String placeId,String empId, String[] idArray) {
		classPlanService.distributionTeacher(placeId,empId,idArray);
		return "SUCCESS";
	}
	
	/*
	 * @Description:根据课程安排ID查询
	 */
	@RequestMapping("/findClassPlanById")
	@ResponseBody
	@Rule("classPlan:find")
	public Map<String, Object> findClassPlanById(String cpId) {
		Assert.hasText(cpId, "参数cpId不能为空");
		return classPlanService.findClassPlanById(cpId);
	}

	@RequestMapping("/findAllClassPlan")
	@ResponseBody
	@Rule("classPlan:find")
	public Object findAllClassPlan(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdClassPlan classPlan) {
		PageHelper.offsetPage(start, length);
		// 课程安排集合
		List<Map<String, Object>> courseList = classPlanService.findAllClassPlan(classPlan);
		IPageInfo page = new IPageInfo<>();
		return new IPageInfo((Page) courseList);
	}

	@RequestMapping("/findCourseName")
	@ResponseBody
	@Rule("classPlan:find")
	public Object findCourseName(String sName, String courseType,
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) courseService.findCourseName(sName, courseType));
	}

	@RequestMapping("/findEmployeeByName")
	@ResponseBody
	@Rule("classPlan:find")
	public Object findEmployeeByName(String sName,String campusId, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) employeeService.findEmployeeByName(sName,campusId));
	}

	@RequestMapping("/findCampusByName")
	@ResponseBody
	@Rule("classPlan:find")
	public Object findCampusByName(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) oaCampusService.findAllKeyValue(sName));
	}
	
	@RequestMapping("/uploadQingshu")
	@ResponseBody
	@Rule("classPlan:upload")
	public Object uploadQingshu(@RequestParam(value = "qingshuInfo", required = false) MultipartFile qingshuInfo) {
		classPlanService.importQingshu(qingshuInfo);
		return "SUCCESS";
	}
	
	@RequestMapping("/qingshuImport")
	@Rule("classPlan:upload")
	public String qingshuImport(HttpServletRequest request) {
		return "/educational/classPlan/qingshu-import";
	}
	
	
	/**
	 * 勾选导出Excel
	 * @param start
	 * @param length
	 * @param response
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/checkexport")
	@Rule("classPlan:download")
	public void checkexport(@RequestParam(name = "idArray[]", required = true) String[] idArray,HttpServletResponse response) throws UnsupportedEncodingException {
		BdClassPlan classPlan=new BdClassPlan();
		classPlanService.checkexportClassPlan(idArray,response);
		
	}
	/**
	 * 筛选导出Excel
	 * @param start
	 * @param length
	 * @param response
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/queryexport")
	@Rule("classPlan:download")
	public void queryexport(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length,BdClassPlan classPlan,HttpServletResponse response) throws UnsupportedEncodingException{
		
		classPlanService.queryeexportClassPlan(classPlan,response);
	}
	
	
//	@RequestMapping("/toScheduleExport")
//	@Rule("classPlan:find")
//	public String toScheduleExport(BdTimeTableQuery query,Model model,HttpServletRequest request) {
//		model.addAttribute("BdTimeTableQuery",query);
//		return "/educational/classPlan/class-plan-curriculum-excel";
//	}
//	
}
