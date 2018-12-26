package com.yz.service.educational;

import java.io.IOException;
import java.io.UnsupportedEncodingException; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.BdClassPlanMapper;
import com.yz.exception.CustomException;
import com.yz.generator.IDGenerator;
import com.yz.model.educational.BdClassPlan;
import com.yz.model.educational.BdQingshuImport;
import com.yz.model.educational.MakeSchedule;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdClassPlanService {

	@Autowired
	private BdClassPlanMapper classPlanMapper;
	@Autowired
	private BdCourseService courseService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	public void insertClassPlan(BdClassPlan classPlan, String money) {

		classPlan.setStartTime(classPlan.getCpDate() + " " + classPlan.getStartTime());
		classPlan.setEndTime(classPlan.getCpDate() + " " + classPlan.getEndTime());

		// TODO Auto-generated method stub
		classPlan.setCpId(IDGenerator.generatorId());
		classPlanMapper.insertSelective(classPlan);
		// 更新金额
		classPlanMapper.updateEmpIdMoney(classPlan.getEmpId(), money);
	}

	public void importClassPlan(BdClassPlan classPlan, String money) {

		classPlan.setStartTime(classPlan.getCpDate() + " " + classPlan.getExt1());
		classPlan.setEndTime(classPlan.getCpDate() + " " + classPlan.getExt2());

		// TODO Auto-generated method stub
		classPlan.setCpId(IDGenerator.generatorId());
		classPlanMapper.insertSelective(classPlan);
		// 更新金额
		classPlanMapper.updateEmpIdMoney(classPlan.getEmpId(), money);
	}

	public void updateClassPlan(BdClassPlan classPlan, String money) {

		classPlan.setStartTime(classPlan.getCpDate() + " " + classPlan.getStartTime());
		classPlan.setEndTime(classPlan.getCpDate() + " " + classPlan.getEndTime());

		// TODO Auto-generated method stub
		classPlanMapper.updateByPrimaryKeySelective(classPlan);
		// 更新金额
		classPlanMapper.updateEmpIdMoney(classPlan.getEmpId(), money);
	}

	public List<Map<String, Object>> findAllClassPlan(BdClassPlan classPlan) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMap = classPlanMapper.findAllClassPlan(classPlan);
		if (null != listMap)
			for (Map<String, Object> map : listMap) {
				List<Map<String, Object>> pfsnList = classPlanMapper.findProfessional((String) map.get("courseId"),
						classPlan.getPfsnName());
				map.put("professional", pfsnList);
			}
		return listMap;
	}

	public Map<String, Object> findClassPlanById(String cpId) {
		// TODO Auto-generated method stub
		return classPlanMapper.findClassPlanById(cpId);
	}

	public void deleteClassPlan(String[] idArray) {
		// TODO Auto-generated method stub
		classPlanMapper.deleteClassPlan(idArray);
	}

	public void distributionTeacher(String placeId,String empId,String[] idArray) {
		// TODO Auto-generated method stub
		classPlanMapper.distributionTeacher(placeId,empId, idArray);
	}

	public Map<String, Object> findMakeSchedule(MakeSchedule makeSchedule) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = null;
		// 第一步：根据必要条件筛选出院校 专业
		Map<String, Object> unvsPfsnMap = classPlanMapper.findUnvsPfsn(makeSchedule);
		if (null != unvsPfsnMap) {
			makeSchedule.setPfsnId((String) unvsPfsnMap.get("pfsnId"));
			// 第二步：根据专业id查询出课程安排
			List<Map<String, Object>> makeScheduleMaps = classPlanMapper.findMakeSchedule(makeSchedule);
			if (null != makeScheduleMaps) {
				resultMap = new HashMap<String, Object>();
				resultMap.putAll(unvsPfsnMap);
				resultMap.put("makeScheduleMaps", makeScheduleMaps);
				// 人数
				String tphId = classPlanMapper.getTphId(makeSchedule);
				String[] tphIds = { tphId };
				int stdCount = courseService.stdtCount(tphIds);
				resultMap.put("stdCount", stdCount);
			}
		}
		return resultMap;
	}

	public void importQingshu(MultipartFile qingshuInfo) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdQingshuImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdQingshuImport>();
		testExcelCofing.setSheetName("index").setType(BdQingshuImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("高校学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("青书学堂账号", "qingshuId"))
				.addTitle(new ExcelUtil.IExcelTitle("青书学堂密码", "qingshuPwd"));

		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdQingshuImport> list = ExcelUtil.importWorkbook(qingshuInfo.getInputStream(), testExcelCofing,
					qingshuInfo.getOriginalFilename());
			if (null != list && list.size() > 0) {

				// 遍历插入
				for (BdQingshuImport std : list) {

					String stdName = std.getStdName();
					String schoolRoll = std.getSchoolRoll();
					String qingshuId = std.getQingshuId();
					String qingshuPwd = std.getQingshuPwd();

					if (!StringUtil.hasValue(stdName) || !StringUtil.hasValue(schoolRoll)) {
						throw new CustomException("请检查第" + index + "行：学员姓名、高校学号不能为空");
					}

					String learnId = classPlanMapper.selectLearnIdBySchoolRoll(stdName, schoolRoll);

					if (!StringUtil.hasValue(learnId)) {
						throw new CustomException("请检查学员：" + stdName + "，未查询到报读记录");
					}

					classPlanMapper.updateQingshuInfo(learnId, qingshuId, qingshuPwd);

					index++;
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}
	
	public void exportClassPlan(List<Map<String, Object>> courseList ,HttpServletResponse response) {
		List<BdClassPlan> resultList=new ArrayList<BdClassPlan>();
		if(courseList!=null&&courseList.size()>0) {
			for (Map<String, Object> map : courseList) {
				BdClassPlan bdplan=new BdClassPlan();
				if(map.get("empName")!=null&&StringUtil.hasValue(map.get("empName").toString())) {
					bdplan.setEmpName(map.get("empName").toString());
				}
				
				bdplan.setCourseId(map.get("courseId").toString());
				if(map.get("courseName")!=null&&StringUtil.hasValue(map.get("courseName").toString())) {
					bdplan.setCourseName(map.get("courseName").toString());
				}
				if(map.get("year")!=null&&StringUtil.hasValue(map.get("year").toString())) {
					bdplan.setYear(map.get("year").toString());
				}
				
				// 转换课程类型
				if(map.get("courseType")!=null&&StringUtil.hasValue(map.get("courseType").toString())) {
					String courseType = dictExchangeUtil.getParamKey("courseType", map.get("courseType").toString());				
					bdplan.setCourseType(courseType);
				}
				
				if(map.get("campusName")!=null&&StringUtil.hasValue(map.get("campusName").toString())) {
					bdplan.setPlaceId(map.get("campusName").toString());
				}
				
				// 转换上课方式
				if(map.get("cpType")!=null&&StringUtil.hasValue(map.get("cpType").toString())) {
					String cpType = dictExchangeUtil.getParamKey("cpType", map.get("cpType").toString());				
					bdplan.setCpType(cpType);
				}
				if(map.get("startTime")!=null&&StringUtil.hasValue(map.get("startTime").toString())) {
					String startTime=map.get("startTime").toString().split(" ")[0];
					bdplan.setStartTime(startTime);
				}
				if(map.get("startTime")!=null&&map.get("endTime")!=null&&StringUtil.hasValue(map.get("startTime").toString())&&StringUtil.hasValue(map.get("endTime").toString())) {
					String startTime=map.get("startTime").toString();
					String endTime=map.get("endTime").toString(); 
					String tempStart=startTime.split(" ")[1];
					String startPart1=String.format("%02d", Integer.parseInt(tempStart.split(":")[0]));
					String startPart2=String.format("%02d", Integer.parseInt(tempStart.split(":")[1]));
					startTime=startPart1 + ":"+ startPart2;
					
					String tempEnd=endTime.split(" ")[1];
					String endPart1=String.format("%02d", Integer.parseInt(tempEnd.split(":")[0]));
					String endPart2=String.format("%02d", Integer.parseInt(tempEnd.split(":")[1]));
					endTime=endPart1 + ":"+ endPart2;
					
					endTime=startTime + " 至  "+ endTime;
					
					bdplan.setEndTime(endTime);
				}
				
				resultList.add(bdplan);
			}
		}
		
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdClassPlan> testExcelCofing = new ExcelUtil.IExcelConfig<BdClassPlan>();
		testExcelCofing.setSheetName("index").setType(BdClassPlan.class)
				.addTitle(new ExcelUtil.IExcelTitle("教师", "empName"))
				.addTitle(new ExcelUtil.IExcelTitle("课程编号", "courseId"))
				.addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
				.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("课程类型", "courseType"))
				.addTitle(new ExcelUtil.IExcelTitle("上课分校", "placeId"))
				.addTitle(new ExcelUtil.IExcelTitle("上课方式", "cpType"))
				.addTitle(new ExcelUtil.IExcelTitle("上课日期", "startTime"))
				.addTitle(new ExcelUtil.IExcelTitle("时间", "endTime"));
		
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=ClassPlan.xls");
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
	 * 筛选课程安排导出excel
	 * @param excelTScore
	 * @param type
	 * @throws UnsupportedEncodingException 
	 */
	public void queryeexportClassPlan(BdClassPlan classPlan,HttpServletResponse response) throws UnsupportedEncodingException {
		classPlan.setCourseName(new String(classPlan.getCourseName().getBytes("ISO-8859-1"),"utf-8"));
		classPlan.setEmpName(new String(classPlan.getEmpName().getBytes("ISO-8859-1"),"utf-8"));
		classPlan.setPfsnName(new String(classPlan.getPfsnName().getBytes("ISO-8859-1"),"utf-8"));
		// 课程安排集合
		List<Map<String, Object>> courseList = classPlanMapper.findAllClassPlan(classPlan);
		this.exportClassPlan(courseList, response);	
	}
	
	/**
	 * 勾选导出Excel
	 * @param excelTScore
	 * @param type
	 * @throws UnsupportedEncodingException 
	 */
	public void checkexportClassPlan(String[] idArray,HttpServletResponse response) throws UnsupportedEncodingException {	
		// 课程安排集合
		List<Map<String, Object>> courseList = classPlanMapper.findClassPlanByIds(idArray);
		this.exportClassPlan(courseList, response);	
	}

}
