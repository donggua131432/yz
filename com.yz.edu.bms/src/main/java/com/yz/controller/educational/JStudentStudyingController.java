package com.yz.controller.educational;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yz.core.util.DictExchangeUtil;
import com.yz.model.educational.BdClassPlan;
import com.yz.model.educational.JStudentStudyingExcel;
import com.yz.model.educational.JStudentStudyingGKExcel;
import com.yz.model.educational.JStudentStudyingImportExcel;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
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

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.condition.educational.JStudentStudyingQuery;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.stdService.StudyingContacts;
import com.yz.service.educational.JStudentStudyingImportService;
import com.yz.service.educational.JStudentStudyingService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.stdService.StudentStudyingService;

@RequestMapping("/jStudying")
@Controller
public class JStudentStudyingController {

	private static final Logger log = LoggerFactory.getLogger(JStudentStudyingController.class);
	
	@Autowired
	private JStudentStudyingService jStudyingService;
	
	@Autowired
	private StudentStudyingService studyingService;
	
	@Autowired
	private StudentRecruitService studentRecruitService;
	
	@Autowired
	private JStudentStudyingImportService jStudyingImportService;

	@RequestMapping("/toList")
	@Rule("jStudying")
	public String toList(Model model) {
		return "/educational/studying/student_studying_list";
	}
	
	@RequestMapping("/getStudyingList")
	@Rule("jStudying")
	@ResponseBody
	public Object getStudyingList(JStudentStudyingQuery queryInfo) {
		return jStudyingService.getStudyingList(queryInfo);
	}
	
	@Log
	@RequestMapping("/changeRemark")
	@ResponseBody
	@Rule("jStudying:changeReamrk")
	public Object changeRemark(BdLearnRemark remarkInfo) {
		studentRecruitService.changeRemark(remarkInfo);
		return null;
	}
	
	@RequestMapping("/showPaymentDetail")
	@Rule("jStudying:paymentDetail")
	public String showPaymentDetail(Model model, @RequestParam(value="learnId", required=true) String learnId, @RequestParam(value="stdId", required=true) String stdId) {
		Map<String, Object> resultMap = studyingService.getPaymentDetail(learnId, stdId);
		
		Set<Entry<String, Object>> vSet = resultMap.entrySet();
		
		Iterator<Entry<String, Object>> it = vSet.iterator();
		
		while(it.hasNext()) {
			Entry<String, Object> e = it.next();
			model.addAttribute(e.getKey(), e.getValue());
		}
		
		return "/educational/studying/studying_paymentDetail";
	}
	
	@RequestMapping("/showContacts")
	@Rule("jStudying:queryContact")
	@Token(groupId="jStudying:updateContact", action=Flag.Save)
	public String showConcats(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("studentConcats", studyingService.getStudyingContacts(learnId));
		return "/educational/studying/studying_contacts";
	}
	
	@Log
	@RequestMapping("/updateContacts")
	@Rule("jStudying:updateContact")
	@Token(groupId="jStudying:updateContact", action=Flag.Remove)
	@ResponseBody
	public Object updateContacts(HttpServletRequest requset, StudyingContacts contacts) {
		studyingService.updateContacts(contacts);
		return null;
	}
	
	@RequestMapping("/showServices")
	@Rule("jStudying:queryServices")
	public String showServices(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		return "/educational/studying/studying_service";
	}
	
	@RequestMapping("/showServicesData")
	@Rule("studying:queryServices")
	@ResponseBody
	public Object showServicesData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, @RequestParam(value="learnId", required=true) String learnId) {
		return studyingService.getStudentServiceLog(start,length,learnId);
	}

	
	@RequestMapping("/toEditStdNo")
	@Rule("jStudying:editStudyingNo")
	public String toEditStdNo(Model model,@RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("info", studyingService.getStudyingInfoByLearnId(learnId));
		return "/educational/studying/student_stdNoEdit";
	}
	
	@RequestMapping("/editStdNo")
	@Rule("jStudying:editStudyingNo")
	@ResponseBody
	public Object EditStdNo(String learnId,String stdNo) {
		studyingService.updateStudentNo(learnId, stdNo, "");
		return null;
	}
	
	@RequestMapping("/toEditSchoolRoll")
	@Rule("jStudying:editStudyingNo")
	public String toEditSchoolRoll(Model model,@RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("info", studyingService.getStudyingInfoByLearnId(learnId));
		return "/educational/studying/student_schoolRollEdit";
	}
	
	@RequestMapping("/editSchoolRoll")
	@Rule("jStudying:editStudyingNo")
	@ResponseBody
	public Object EditSchoolRoll(String learnId,String schoolRoll) {
		studyingService.updateStudentNo(learnId, "", schoolRoll);
		return null;
	}
	
	@RequestMapping("/exportStudying")
	@Rule("jStudying:exportStudying")
	public void exportStudying(JStudentStudyingQuery query, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<JStudentStudyingExcel> testExcelCofing = new ExcelUtil.IExcelConfig<JStudentStudyingExcel>();
		testExcelCofing.setSheetName("index").setType(JStudentStudyingExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("报考层次", "enrollPfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("第一志愿院校", "enrollUnvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("第一志愿专业", "enrollPfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("录取院校", "admitUnvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("录取专业", "adminPfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("在读层次", "learnPfsnLevel"))

				.addTitle(new ExcelUtil.IExcelTitle("在读院校", "learnUnvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("在读专业", "learnPfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("是否欠费", "debt"))
				.addTitle(new ExcelUtil.IExcelTitle("是否外校", "stdType"))
				.addTitle(new ExcelUtil.IExcelTitle("分校", "campusName"))
				.addTitle(new ExcelUtil.IExcelTitle("学员状态", "stdStage"))
				.addTitle(new ExcelUtil.IExcelTitle("招生老师", "empName"))
				.addTitle(new ExcelUtil.IExcelTitle("招生部门", "dpName"))
				.addTitle(new ExcelUtil.IExcelTitle("归属校区", "homeCampusName"))
				.addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))

				.addTitle(new ExcelUtil.IExcelTitle("生日", "birthday"))
				.addTitle(new ExcelUtil.IExcelTitle("政治面貌", "politicalStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("户口", "rprProvinceCode"))
				.addTitle(new ExcelUtil.IExcelTitle("文化程度", "edcsType"))
				.addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
				.addTitle(new ExcelUtil.IExcelTitle("户口类型", "rprType"))
				.addTitle(new ExcelUtil.IExcelTitle("证件类型", "idType"))
				.addTitle(new ExcelUtil.IExcelTitle("职业", "jobType"))
				.addTitle(new ExcelUtil.IExcelTitle("工作单位", "workPlace"))
				.addTitle(new ExcelUtil.IExcelTitle("电话", "telephone"))

				.addTitle(new ExcelUtil.IExcelTitle("手机", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("省", "provinceName"))
				.addTitle(new ExcelUtil.IExcelTitle("市", "cityName"))
				.addTitle(new ExcelUtil.IExcelTitle("区", "districtName"))
				.addTitle(new ExcelUtil.IExcelTitle("地址", "wpAddress"))
				.addTitle(new ExcelUtil.IExcelTitle("入学年度", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("招生日期", "createTime"))
				.addTitle(new ExcelUtil.IExcelTitle("照片", "annexUrl"))
				.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
				.addTitle(new ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"))

				.addTitle(new ExcelUtil.IExcelTitle("辅导员", "tutor"))
				.addTitle(new ExcelUtil.IExcelTitle("收件地址", "address"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业院校", "unvsNameOld"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业时间", "graduateTime"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业专业", "profession"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业证编号", "diploma"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业院校所在省市", "proCity"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业入学时间", "adminssionTime"))
				.addTitle(new ExcelUtil.IExcelTitle("考试区县", "taName"))
				.addTitle(new ExcelUtil.IExcelTitle("考生号", "examNo"))

				.addTitle(new ExcelUtil.IExcelTitle("成考分", "sumScore"))
				.addTitle(new ExcelUtil.IExcelTitle("加分", "points")
				);

		// 查询
		List<JStudentStudyingExcel> resultList = jStudyingService.exportStudying(query);
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=jStudying.xls");
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


	@RequestMapping("/exportStudyingGK")
	@Rule("jStudying:exportStudyingGK")
	public void exportStudyingGK(JStudentStudyingQuery query, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<JStudentStudyingGKExcel> testExcelCofing = new ExcelUtil.IExcelConfig<JStudentStudyingGKExcel>();
		testExcelCofing.setSheetName("index").setType(JStudentStudyingGKExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
				.addTitle(new ExcelUtil.IExcelTitle("政治面貌", "politicalStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("证件类型", "idType"))
				.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("出生日期", "birthday"))
				.addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
				.addTitle(new ExcelUtil.IExcelTitle("在职状况", "jobStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("分布情况", "assignArea"))
				.addTitle(new ExcelUtil.IExcelTitle("婚姻状况", "maritalStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("学费来源", "feeSource"))

				.addTitle(new ExcelUtil.IExcelTitle("户口性质", "rprType"))
				.addTitle(new ExcelUtil.IExcelTitle("籍贯（省）", "rprProvinceName"))
				.addTitle(new ExcelUtil.IExcelTitle("户口所在地", "rprAddress"))
				.addTitle(new ExcelUtil.IExcelTitle("手机", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("固定电话", "telephone"))
				.addTitle(new ExcelUtil.IExcelTitle("邮箱", "email"))
				.addTitle(new ExcelUtil.IExcelTitle("通讯地址", "address"))
				.addTitle(new ExcelUtil.IExcelTitle("邮编", "zipCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "learnPfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "learnPfsnName"))

				.addTitle(new ExcelUtil.IExcelTitle("是否电大毕业", "isOpenUnvs"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历层次", "edcsType"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业院校", "unvsNameOld"))
				.addTitle(new ExcelUtil.IExcelTitle("原毕业时间", "graduateTime"))
				.addTitle(new ExcelUtil.IExcelTitle("原学科", "subject"))
				.addTitle(new ExcelUtil.IExcelTitle("原学科门类", "subjectCategory"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历学习类型", "studyType"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历所学专业", "profession"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历毕业证编号", "diploma"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历证明材料", "materialType"))
				.addTitle(new ExcelUtil.IExcelTitle("证明材料编号", "materialCode"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历证件类型", "idType"))
				.addTitle(new ExcelUtil.IExcelTitle("原学历证件号码", "idCard"));

		// 查询
		List<JStudentStudyingGKExcel> resultList = jStudyingService.exportStudyingGK(query);
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=jStudyingGK.xls");
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
	
	@RequestMapping("/excelImport")
	@Rule("jStudying:upload")
	public String excelImport(HttpServletRequest request) {
		return "/educational/studying/student_studying_import";
	}
	
	@RequestMapping("/uploadExcel")
	@ResponseBody
	@Rule("jStudying:upload")
	public Object uploadExcel(@RequestParam(value = "excelStudentInfo", required = false) MultipartFile excelStudentInfo) {
		jStudyingImportService.importWbStudentInfo(excelStudentInfo);
   	    return "SUCCESS";
	}
}