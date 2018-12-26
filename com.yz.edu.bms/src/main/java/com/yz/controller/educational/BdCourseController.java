package com.yz.controller.educational;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yz.model.educational.TeachTaskBookQuery;
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
import com.yz.constants.OrderConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.RequestUtil;
import com.yz.dao.baseinfo.BdUnvsProfessionMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.baseinfo.BdCourse;
import com.yz.model.baseinfo.BdCourseEditInfo;
import com.yz.model.baseinfo.BdCourseMap;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.TbListInfo;
import com.yz.model.common.TbQueryInfo;
import com.yz.model.common.ThpListInfo;
import com.yz.model.common.ThpQueryInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.course.CourseExport;
import com.yz.model.course.CourseExportExcel;
import com.yz.model.educational.BdCourseResource;
import com.yz.model.educational.BdTimeTableQuery;
import com.yz.model.educational.CourseExcel;
import com.yz.model.educational.CourseExcel.InformationExcel;
import com.yz.service.baseinfo.BdTeachPlanServiceImpl;
import com.yz.service.common.BaseInfoService;
import com.yz.service.educational.BdCourseExcelService;
import com.yz.service.educational.BdCourseService;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@RequestMapping("/course")
@Controller
public class BdCourseController {
	
	private static final Logger log = LoggerFactory.getLogger(BdCourseController.class);
	
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdCourseService courseService;
	
	@Autowired
	private BdTeachPlanServiceImpl teachPlanService;
	
	@Autowired
	private BdUnvsProfessionMapper upMapper;
	
	@Autowired
	private BdCourseExcelService classPlanExcelService;

	@RequestMapping("/list")
	@Rule("course:find")
	public String showList(HttpServletRequest request) {
		return "/educational/course/course-list";
	}

	@RequestMapping("/excelImport")
	@Rule("course:upload")
	public String excelImport(HttpServletRequest request) {
		return "/educational/course/course-import";
	}

	@RequestMapping("/timetable")
	@ResponseBody
	@Rule("course:find")
	public Object timetable(BdTimeTableQuery query) throws ParseException {
		return courseService.getTimetable(query);
	}
	
	@RequestMapping("/sPfsn")
	@Rule("course:find")
	@ResponseBody
	public Object getPfsnList(SelectQueryInfo queryInfo) {
		return courseService.getPfsnSelectList(queryInfo);
	}
	@RequestMapping("/uploadExcel")
	@ResponseBody
	@Rule("course:upload")
	public Object uploadExcel(@RequestParam(value = "excelCourse", required = false) MultipartFile excelCourse) throws IOException {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<CourseExcel> testExcelCofing = new ExcelUtil.IExcelConfig<CourseExcel>();
		testExcelCofing.setSheetName("index").setType(CourseExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName", false))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType", false))
				.addTitle(new ExcelUtil.IExcelTitle("年度", "year", false))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel", false))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName", false))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName", false))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade", false))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester", false))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划", "mappingName", false))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark", false));
		//行数记录
		int index = 2;
		try {
			
			// 对文件进行分析转对象
			List<CourseExcel> list = ExcelUtil.importWorkbook(excelCourse.getInputStream(), testExcelCofing,
					excelCourse.getOriginalFilename());
			
			
			for (CourseExcel courseExcel : list) {
				if (!StringUtil.hasValue(courseExcel.getCourseName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课程名称不能为空");
                }
				if (!StringUtil.hasValue(courseExcel.getCourseType())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课程类型不能为空");
                }
				if (!StringUtil.hasValue(courseExcel.getUnvsName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！院校不能为空");
                }
				if (!StringUtil.hasValue(courseExcel.getPfsnName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业不能为空");
                }
				if (!StringUtil.hasValue(courseExcel.getMappingName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！教学计划不能为空");
                }
				
				//年度转换
				String valueTemple = dictExchangeUtil.getParamValue("year", courseExcel.getYear());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！年度格式错误");
				}else{
					courseExcel.setYear(valueTemple);
				}
				
				//课程类型转换
				valueTemple = dictExchangeUtil.getParamValue("courseType", courseExcel.getCourseType());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！课程类型格式错误");
				}else{
					courseExcel.setCourseType(valueTemple);
				}
				
				// 转换专业层次
				valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", courseExcel.getPfsnLevel());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！层次格式错误,格式如：[1>专科升本科类,5>高中起点高职高专]");
				}else{
					courseExcel.setPfsnLevel(valueTemple);
				}
				// 转换年级
				valueTemple = dictExchangeUtil.getParamValue("grade", courseExcel.getGrade());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！年级格式错误");
				}else{
					courseExcel.setGrade(valueTemple);
				}
				// 学期转换
				valueTemple = dictExchangeUtil.getParamKey("semester", courseExcel.getSemester());
				if(null == valueTemple){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！学期格式错误,格式如：[1-6]之间的数字");
				}
				
				
				
			}//for end
			//检查教材计划
			List<Map<String, Object>> courseList = classPlanExcelService.getNonExistsTeachPlan(list);
            if (courseList!=null&&courseList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("教材计划不存在：<br/>"); 
                for (Map<String, Object> map : courseList) {
                    sb.append(map.get("grade") +"级-第"+map.get("semester") +"学期-"+map.get("unvs_name") +"-"+dictExchangeUtil.getParamKey("pfsnLevel", map.get("pfsn_level").toString()) +"-"+"-"+map.get("pfsn_name") +"-"+map.get("thp_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            //检查教材名称
			List<Map<String, Object>> textList = classPlanExcelService.getNonExistsTextBook(list);
            if (textList!=null&&textList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("教材名称不存在：<br/>"); 
                for (Map<String, Object> map : textList) {
                    sb.append(map.get("textbook_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
			
			
            //批量插入
            BaseUser user=SessionUtil.getUser();
            classPlanExcelService.insertCourseByExcel(list,user);
			List<CourseExcel> exsitList = new ArrayList<CourseExcel>();
			
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		return "SUCCESS";
	}
	@RequestMapping("/uploadExcel1")
	@ResponseBody
	@Rule("course:upload")
	public Object uploadExcel1(@RequestParam(value = "excelCourse", required = false) MultipartFile excelCourse) throws IOException {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<CourseExcel> testExcelCofing = new ExcelUtil.IExcelConfig<CourseExcel>();
		testExcelCofing.setSheetName("index").setType(CourseExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName", false))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType", false))
				.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year", false))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划/教材编码", "mappingId", false))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划/教材名称", "mappingName", false));


		try {
			List<CourseExcel> exsitList = new ArrayList<CourseExcel>();
			// 对文件进行分析转对象
			List<CourseExcel> list = ExcelUtil.importWorkbook(excelCourse.getInputStream(), testExcelCofing,
					excelCourse.getOriginalFilename());

			for (int index = 1; index <= list.size(); index++) {
				// 遍历插入
				CourseExcel course = list.get(index - 1);
				InformationExcel otherInfo = new InformationExcel();
				otherInfo.setMappingId(course.getMappingId());
				otherInfo.setMappingName(course.getMappingName());

				// 教材类型转换
				String valueTemple = dictExchangeUtil.getParamValue("courseType", course.getCourseType());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					course.setCourseType(valueTemple);
				}

				// 年度转换
				valueTemple = dictExchangeUtil.getParamValue("year", course.getYear());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					course.setYear(valueTemple);
				}

				boolean isExsit = false;

				for (CourseExcel exsit : exsitList) {
					if (exsit.equals(course)) {
						isExsit = true;
						List<InformationExcel> tpList = exsit.getInfoList();
						if (!tpList.contains(otherInfo))
							tpList.add(otherInfo);
					}
				}

				if (!isExsit) {
					course.getInfoList().add(otherInfo);
					exsitList.add(course);
				}
			}
			
			courseService.insertCourseExcel(exsitList);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		return "SUCCESS";
	}

	@RequestMapping("/edit")
	@Rule("course:find")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		BdCourse course = new BdCourse();
		List<ThpListInfo> thpList = new ArrayList<ThpListInfo>();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String courseId = RequestUtil.getString("courseId");
			Assert.hasText(courseId, "课程ID不能为空");
			course = courseService.findCourseById(courseId);
			// 教学计划
			ThpQueryInfo queryInfo = new ThpQueryInfo();
			queryInfo.setCourseId(courseId);
			thpList = baseInfoService.getThpSelectedList(queryInfo);

			List<BdCourseResource> courseResourceList = courseService.getCourseResource(courseId);
			model.addAttribute("courseResource", courseResourceList);
			// 是否为辅导教材
			List<TbListInfo> textBookList = null;
			if (OrderConstants.TEXTBOOK_TYPE_FD.equals(course.getCourseType())) {
				TbQueryInfo tbQueryInfo = new TbQueryInfo();
				tbQueryInfo.setCourseId(courseId);
				textBookList = baseInfoService.getTbSelectedList(tbQueryInfo);
				model.addAttribute("textBooks", textBookList);
			}
		}
		model.addAttribute("exType", exType);
		model.addAttribute("courseInfo", course);
		model.addAttribute("thpList", thpList);
		model.addAttribute("subjects", upMapper.selectAllSubject());
		return "educational/course/course-edit";
	}

	@RequestMapping("/show")
	@Rule("course:find")
	public String show(HttpServletRequest request, Model model) {
		String courseId = RequestUtil.getString("courseId");
		Assert.hasText(courseId, "参数名称不能为空");
		List<BdCourseResource> courseResourceList = courseService.getCourseResource(courseId);
		model.addAttribute("courseResource", courseResourceList);
		return "educational/course/course-show";
	}

	@RequestMapping("/upload")
	@ResponseBody
	@Rule("course:upload")
	public Object upload(@RequestParam(value = "file", required = true) MultipartFile myfile) {
		String realFilePath = courseService.tempUpload(myfile);
		return realFilePath;
	}

	/**
	 * @Description: 下载文件
	 * @param 资源文件id
	 */
	@RequestMapping("/down")
	@Rule("course:down")
	public void down(String resourceId, HttpServletResponse res) {
		courseService.down(resourceId, res);
	}

	/**
	 * @Description: 根据条件查询所有符合数据
	 * 
	 * @param course
	 *            BdCourse对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常 much.
	 */
	@RequestMapping("/findAllCourse")
	@ResponseBody
	@Rule("course:find")
	public Object findAllCourse(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdCourse course) {
		Object o = courseService.exchangePage(start, length, course);
		return o;
	}

	/*
	 * @Description:新增课程
	 */
	@RequestMapping("/insertCourse")
	@ResponseBody
	@Rule("course:insert")
	public Object insertCourse(BdCourse course, BdCourseEditInfo editInfo) {
		courseService.insertCourse(course, editInfo);
		return "SUCCESS";
	}

	/*
	 * @Description:修改课程
	 */
	@RequestMapping("/updateCourse")
	@ResponseBody
	@Rule("course:update")
	public Object updateCourse(BdCourse course, BdCourseEditInfo editInfo) {
		courseService.updateCourse(course, editInfo);
		return "SUCCESS";
	}

	/*
	 * @Description:根据课程ID查询
	 */
	@RequestMapping("/findCourseById")
	@ResponseBody
	@Rule("course:find")
	public BdCourse findCourseById(String courseId) {
		Assert.hasText(courseId, "参数courseId不能为空");
		return courseService.findCourseById(courseId);
	}

	@RequestMapping("/findTeachPlan")
	@ResponseBody
	@Rule("course:find")
	public Object findTeachPlan(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) teachPlanService.findTeachPlan(sName));
	}

	/**
	 * Description: 根据条件查询所有符合数据并导出
	 * 
	 * @param
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 *             抛出一个异常
	 */
	@RequestMapping("/export")
	@Rule("course:export")
	public void export(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdCourse course,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdCourseMap> testExcelCofing = new ExcelUtil.IExcelConfig<BdCourseMap>();
		testExcelCofing.setSheetName("index").setType(BdCourseMap.class)
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
				.addTitle(new ExcelUtil.IExcelTitle("课程编码", "courseId"))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType"))
				.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfnsType"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("人数", "studentNumber"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划编码", "thpId"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划名称", "thpName"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));
		List<BdCourseMap> courseList = courseService.selectAllToExport(course);
		for (BdCourseMap bdCourse : courseList) {
			
			bdCourse.setCourseType(dictExchangeUtil.getParamKey("courseType", bdCourse.getCourseType()));
			bdCourse.setYear(dictExchangeUtil.getParamKey("year",bdCourse.getYear()));
			String tempValue=dictExchangeUtil.getParamKey("pfsnLevel",bdCourse.getPfnsType());	
			bdCourse.setPfnsType(tempValue);
			bdCourse.setGrade(dictExchangeUtil.getParamKey("grade",bdCourse.getGrade()));
			int stdCount=0;
			 // 人数
            if (bdCourse.getCourseType().equals("FD")) {
            	stdCount = courseService.stdFdCount(bdCourse.getCourseId());
            	 bdCourse.setStudentNumber(String.valueOf(stdCount));
            }
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(courseList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=Course.xls");
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

	@Autowired
	private BaseInfoService baseInfoService;

	@RequestMapping("/getTpSelectInfo")
	@ResponseBody
	public Object getTpSelectInfo(ThpQueryInfo queryInfo) {
		return baseInfoService.getThpUnSelectedListByPage(queryInfo);
	}

	@RequestMapping("/toTpSelectInfo")
	public String toTpSelectInfo(@RequestParam(name = "thpIds") String thpIds, Model model) {

		if (StringUtil.isEmpty(thpIds)) {
			model.addAttribute("selectedList", new ArrayList<ThpListInfo>());
		} else {
			ThpQueryInfo queryInfo = new ThpQueryInfo();
			queryInfo.setSelectedThpId(thpIds.split(","));
			List<ThpListInfo> list = baseInfoService.getThpSelectedList(queryInfo);
			model.addAttribute("selectedList", list);
		}

		return "educational/course/course_tp_select";
	}

	@RequestMapping("/getTbSelectInfo")
	@ResponseBody
	public Object getTbSelectInfo(TbQueryInfo queryInfo) {
		return baseInfoService.getTbUnSelectedListByPage(queryInfo);
	}

	@RequestMapping("/toTbSelectInfo")
	public String toTbSelectInfo(@RequestParam(name = "textbookIds") String textbookIds, Model model) {

		if (StringUtil.isEmpty(textbookIds) || textbookIds.indexOf(",") < 0) {
			model.addAttribute("selectedList", new ArrayList<ThpListInfo>());
		} else {
			TbQueryInfo queryInfo = new TbQueryInfo();
			queryInfo.setSelectedTbIds(textbookIds.split(","));
			List<TbListInfo> list = baseInfoService.getTbSelectedList(queryInfo);
			model.addAttribute("selectedList", list);
		}

		return "educational/course/course_tb_select";
	}

	@RequestMapping("/exportList")
	@Rule("course:findExportCourse")
	public String exportlist(HttpServletRequest request) {
		return "/educational/course/course-export-list";
	}

	@RequestMapping("/findExportCourse")
	@Rule("course:findExportCourse")
	@ResponseBody
	public Object findExportCourse(@RequestParam(value = "start", defaultValue = "0") int start,
								@RequestParam(value = "length", defaultValue = "10") int length, CourseExport course) {

		String courseId = RequestUtil.getString("courseType");
		Assert.hasText(courseId, "课程类型不能为空");
		String grade = RequestUtil.getString("grade");
		Assert.hasText(grade, "年级不能为空");

		PageHelper.offsetPage(start, length);
		List<Map<String,Object>> resultList = courseService.findExportCourse(course);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/exportCourse")
	@Rule("course:exportCourse")
	public void exportCourse(CourseExport course, HttpServletResponse response) {
		String courseType = RequestUtil.getString("courseType");
		Assert.hasText(courseType, "课程类型不能为空");
		String grade = RequestUtil.getString("grade");
		Assert.hasText(grade, "年级不能为空");

		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<CourseExportExcel> testExcelCofing = new ExcelUtil.IExcelConfig<CourseExportExcel>();
		testExcelCofing.setSheetName("index").setType(CourseExportExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("用户名称", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("密码", "stdPassword"))
				.addTitle(new ExcelUtil.IExcelTitle("昵称", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
				.addTitle(new ExcelUtil.IExcelTitle("电话", "tel"))
				.addTitle(new ExcelUtil.IExcelTitle("手机", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("电子邮件", "email"))
				.addTitle(new ExcelUtil.IExcelTitle("所属部门", "department"));

		// 查询
		List<Map<String, Object>> resultList = courseService.findExportCourse(course);
		List<CourseExportExcel> excelList = new ArrayList<>();
		for (Map<String, Object> map : resultList) {
			CourseExportExcel courseExportExcel = new CourseExportExcel();
			courseExportExcel.setIdCard(map.get("idCard").toString());
			courseExportExcel.setStdPassword(map.get("stdPassword").toString());
			courseExportExcel.setStdName(map.get("stdName").toString());
			courseExportExcel.setSex(map.get("sex").toString());
			courseExportExcel.setTel("");
			courseExportExcel.setMobile("");
			courseExportExcel.setEmail("");
			String department = courseType.equals("FD") ? map.get("grade") + "级" + map.get("pfsnLevel") : map.get("grade") + "级" + map.get("pfsnLevel") + map.get("unvsName") + map.get("pfsnName");
			courseExportExcel.setDepartment(department);
			excelList.add(courseExportExcel);
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(excelList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=Course.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("/deleteCourse")
	@ResponseBody
	@Rule("course:delete")
	public Object deleteCourse(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		courseService.deleteCourse(idArray);
		return "success";
	}

	@RequestMapping("/exportTeachTaskBook")
	public String exportTeachTaskBook(HttpServletRequest request) {
		return "/educational/course/course_teach_task_book-doc";
	}

	@RequestMapping("/getCourseByPfsnId")
	@ResponseBody
	public Object getCourseByPfsnId(@RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "rows", defaultValue = "10") int rows, String pfsnId,String semester) {
		Object o = courseService.getCourseByPfsnId(page, rows, pfsnId,semester);
		return o;
	}

	@RequestMapping("/teachTaskBookExport")
	@Rule("course:teachTaskBookExport")
	public void teachTaskBookExport(TeachTaskBookQuery bookQuery, HttpServletResponse response) {
		courseService.teachTaskBookExport(bookQuery, response);
	}
	
	@RequestMapping("/updeteRecourceStatus")
	@ResponseBody
	@Rule("course:updeteRecourceStatus")
	public Object updeteRecourceStatus(String resourceId,String status) {
		courseService.updeteRecourceStatus(resourceId, status);
		return "success";
	}
	
}
