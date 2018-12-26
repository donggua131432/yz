package com.yz.service.statistics;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.statistics.BdExamCourseStatMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.statistics.BdExamCourseStatisticsInfo;
import com.yz.model.statistics.ExamCourseStatQuery;
import com.yz.model.statistics.PaperPrintStatInfo;
import com.yz.model.statistics.PaperPrintStatQuery;
import com.yz.util.ExcelUtil;
/**
 * 考试课程统计
 * @author lx
 * @date 2017年11月16日 下午5:03:30
 */
@Service
@Transactional
public class ExamCourseStatService
{
	private static Logger log = LoggerFactory.getLogger(ExamCourseStatService.class);
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdExamCourseStatMapper courseStatMapper;
	
	public IPageInfo<BdExamCourseStatisticsInfo> queryExamCourseStatPage(int start, int length,ExamCourseStatQuery statQuery)
	{
		PageHelper.offsetPage(start, length);
		List<BdExamCourseStatisticsInfo> courseS = courseStatMapper.queryExamCourseStat(statQuery);
		return new IPageInfo<BdExamCourseStatisticsInfo>((Page<BdExamCourseStatisticsInfo>) courseS);
	}
	
	@SuppressWarnings("unchecked")
	public void exportCourseStat(ExamCourseStatQuery statQuery,HttpServletResponse response){
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdExamCourseStatisticsInfo> testExcelCofing = new ExcelUtil.IExcelConfig<BdExamCourseStatisticsInfo>();
		testExcelCofing.setSheetName("index").setType(BdExamCourseStatisticsInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("考试年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("人数", "peopleNum"))
				.addTitle(new ExcelUtil.IExcelTitle("考试科目", "testSubjectText"))
				.addTitle(new ExcelUtil.IExcelTitle("开课课程", "courseText"));

		List<BdExamCourseStatisticsInfo> list = courseStatMapper.queryExamCourseStat(statQuery);

		for (BdExamCourseStatisticsInfo course : list) {

			StringBuffer sb = new StringBuffer();
			List<Map<String, String>> testSubjects = course.getTestSubjectMap();
			if (null != testSubjects && testSubjects.size() > 0) {
				for (Map<String, String> map : testSubjects) {
					sb.append(map.get("testSubject")).append("、");
				}
			}
			course.setTestSubjectText(sb.toString());
			
			sb = new StringBuffer();
			List<Map<String, String>> books = course.getCourseMap();
			if (null != books && books.size() > 0) {
				for (Map<String, String> map : books) {
					sb.append(map.get("courseName")).append("、");
				}
			}
			course.setCourseText(sb.toString());
			
			String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", course.getPfsnLevel());
			course.setPfsnLevel(pfsnLevel);
			
			String semester = dictExchangeUtil.getParamKey("semester", course.getSemester());
			course.setSemester(semester);
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=examCourseStat.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	public IPageInfo<PaperPrintStatInfo> queryPaperPrintStatPage(int start, int length,PaperPrintStatQuery statQuery)
	{
		PageHelper.offsetPage(start, length);
		List<PaperPrintStatInfo> courseS = courseStatMapper.queryPaperPrintStat(statQuery);
		return new IPageInfo<PaperPrintStatInfo>((Page<PaperPrintStatInfo>) courseS);
	}
	
	@SuppressWarnings("unchecked")
	public void exportPaperPrintStat(PaperPrintStatQuery statQuery,HttpServletResponse response){
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<PaperPrintStatInfo> testExcelCofing = new ExcelUtil.IExcelConfig<PaperPrintStatInfo>();
		testExcelCofing.setSheetName("index").setType(PaperPrintStatInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("考试科目", "testSubject"))
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
				.addTitle(new ExcelUtil.IExcelTitle("试卷名称", "testName"))
				.addTitle(new ExcelUtil.IExcelTitle("涉及的人数（需印试卷）", "peopleNum"));

		List<PaperPrintStatInfo> list = courseStatMapper.queryPaperPrintStat(statQuery);

		for (PaperPrintStatInfo course : list) {
			course.setTestName("["+course.getYear()+"]"+course.getCourseName());
		}
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=paperPrintStat.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
}
