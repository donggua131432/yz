package com.yz.service.educational;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.educational.BdStudentTScoreMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.educational.BdStudentTScoreExcel;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
public class BdStudentTScoreExcelService {
	
	private static final Logger log = LoggerFactory.getLogger(BdStudentTScoreExcelService.class);

	@Autowired
	private BdStudentSendMapper studentSendMapper;
	@Autowired
	private BdStudentTScoreMapper studentTScoreMapper;

	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	public void uploadStudentTScore(MultipartFile excelTScore,String type) {
		ExcelUtil.IExcelConfig<BdStudentTScoreExcel> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentTScoreExcel>();
		// 对导入工具进行字段填充
		if(type!=null&&type.equals("idCard")) {
			testExcelCofing.setSheetName("index").setType(BdStudentTScoreExcel.class)
			.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
			.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
			.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
			.addTitle(new ExcelUtil.IExcelTitle("在读院校", "unvsName"))
			.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
			.addTitle(new ExcelUtil.IExcelTitle("在读专业", "pfsnName"))
			.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
			.addTitle(new ExcelUtil.IExcelTitle("课程编号", "courseId"))
			.addTitle(new ExcelUtil.IExcelTitle("课程", "courseName"))
			.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
			.addTitle(new ExcelUtil.IExcelTitle("期末分数", "score"))
			.addTitle(new ExcelUtil.IExcelTitle("总评分数", "totalmark"));
		}else if(type!=null&&type.equals("schoolRoll")) {
			testExcelCofing.setSheetName("index").setType(BdStudentTScoreExcel.class)
			.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
			.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
			.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
			.addTitle(new ExcelUtil.IExcelTitle("在读院校", "unvsName"))
			.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
			.addTitle(new ExcelUtil.IExcelTitle("在读专业", "pfsnName"))
			.addTitle(new ExcelUtil.IExcelTitle("课程年度", "year"))
			.addTitle(new ExcelUtil.IExcelTitle("课程编号", "courseId"))
			.addTitle(new ExcelUtil.IExcelTitle("课程", "courseName"))
			.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
			.addTitle(new ExcelUtil.IExcelTitle("期末分数", "score"))
			.addTitle(new ExcelUtil.IExcelTitle("总评分数", "totalmark"));
		}
		
		
		
		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdStudentTScoreExcel> list = ExcelUtil.importWorkbook(excelTScore.getInputStream(), testExcelCofing,
					excelTScore.getOriginalFilename());

			// 遍历插入
			for (BdStudentTScoreExcel tscoreExcel : list) {
				
				//id = [院校类型]院校名称:专业编码与名称[专业层次]
				if(type!=null&&type.equals("idCard")) {
					if (!StringUtil.hasValue(tscoreExcel.getIdCard())) {
	                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
	                }
				}
				if(type!=null&&type.equals("schoolRoll")) {
					if (!StringUtil.hasValue(tscoreExcel.getSchoolRoll())) {
	                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学号不能为空");
	                }
				}
				if (!StringUtil.hasValue(tscoreExcel.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学员姓名不能为空");
                }
                if (!StringUtil.hasValue(tscoreExcel.getCourseId())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课程编号不能为空");
                }
                if (!StringUtil.hasValue(tscoreExcel.getSemester())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期不能为空");
                }
                
//                // 专业层次转换
//				String valueTemple = DictExchangeUtil.getParamValue("pfsnLevel", tscoreExcel.getPfsnLevel().trim());
//				if (null == valueTemple) {
//					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业层次错误");
//				} else {
//					tscoreExcel.setPfsnLevel(valueTemple);
//				}
//
//				// 年级转换
//				valueTemple = DictExchangeUtil.getParamValue("grade", tscoreExcel.getGrade().trim());
//				if (null == valueTemple) {
//					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级错误");
//				} else {
//					tscoreExcel.setGrade(valueTemple);
//				}
				
				if(StringUtil.hasValue(tscoreExcel.getScore())&&Double.parseDouble(tscoreExcel.getScore().trim())>=60) {
					tscoreExcel.setIsPass("1");
				}else {
					tscoreExcel.setIsPass("2");
				}
					
 
				index++;
			}
			//end for
			
			//批量查找不存在的学员信息
			List<Map<String, Object>> resultList = studentTScoreMapper.getNonExistsStudent(list,type);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("学员非在读学员或学员不存在：<br/>");
                if(type!=null&&type.equals("idCard")) {
	                for (Map<String, Object> map : resultList) {
	                    sb.append(map.get("id_card")+"-"+ map.get("std_name") + "-"+map.get("grade") + "-"  + map.get("unvs_name").toString() +"-"+ map.get("pfsn_name").toString() + "<br/>");
	                }
                }else {
                	for (Map<String, Object> map : resultList) {
	                    sb.append(map.get("school_roll")+"-"+ map.get("std_name") + "-"+map.get("grade") + "-"  + map.get("unvs_name").toString() +"-"+ map.get("pfsn_name").toString() + "<br/>");
	                }
                }

                throw new IllegalArgumentException(sb.toString());
            }
            
            //批量查找不存在的课程信息
			List<Map<String, Object>> courseList = studentTScoreMapper.getNonExistsCourse(list);
            if (courseList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("课程编号不存在：<br/>"); 
                for (Map<String, Object> map : courseList) {
                    sb.append(map.get("course_id")+"-"+ map.get("course_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user=SessionUtil.getUser();
            
            studentTScoreMapper.initTmpExcelTable(list);
            List<Map<String, String>> tmpList = studentTScoreMapper.selectTmpStudentTScore(user);
            for (Map<String, String> map : tmpList) {
				map.put("tscoreId", IDGenerator.generatorId());
			}
            //批量插入
            studentTScoreMapper.insertByExcel(tmpList);
            
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}
	
	
	
	public void importTextBookPfsnCode(MultipartFile textbookpfscode) {
		ExcelUtil.IExcelConfig<BdUnvsProfession> testExcelCofing = new ExcelUtil.IExcelConfig<BdUnvsProfession>();
		// 对导入工具进行字段填充
		testExcelCofing.setSheetName("index").setType(BdUnvsProfession.class)
		
		.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
		.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
		.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
		.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
		.addTitle(new ExcelUtil.IExcelTitle("教材专业编码", "textbookPfsncode"));
		
		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdUnvsProfession> list = ExcelUtil.importWorkbook(textbookpfscode.getInputStream(), testExcelCofing,
					textbookpfscode.getOriginalFilename());

			// 遍历插入
			for (BdUnvsProfession codeExcel : list) {
				
                // 年级转换
                String valueTemple = dictExchangeUtil.getParamValue("grade", codeExcel.getGrade().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级格式错误,正确格式如:[2016级]");
				} else {
					codeExcel.setGrade(valueTemple);
				}
                // 专业层次转换
				valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", codeExcel.getPfsnLevel().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException(
							"excel数据格式错误，请检查第" + index + "行数据！在读层次格式错误，正确格式如:[1>专科升本科类,5>高中起点高职高专]");
				} else {
					codeExcel.setPfsnLevel(valueTemple);
				}
				if (!StringUtil.hasValue(codeExcel.getUnvsName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！院校不能为空");
                }
               
                if (!StringUtil.hasValue(codeExcel.getPfsnName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业名称不能为空");
                }
                if (!StringUtil.hasValue(codeExcel.getTextbookPfsncode())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！教材专业编码不能为空");
                }
                
				index++;
			}
			//end for
			
			//第一步：初始化临时表
			studentSendMapper.initTmpUnvsPsfnInfoTable(list);
			
			//第二步：检查必录项的院校与专业
			List<Map<String, Object>> LearnresultList = studentSendMapper.getNonExistsUnvsNamePsfnName();
			if (LearnresultList != null && LearnresultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("核对以下年级,院校,层次,专业不存在：<br/>");
				for (Map<String, Object> map : LearnresultList) {
					sb.append("年级：["+map.get("grade")+"]-院校["+map.get("unvs_name") + "]-层次[" + map.get("pfsn_level") +"]-专业["+ map.get("pfsn_name") + "]<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}
            //第二步：批量更新
            studentSendMapper.insertByExcel();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}
	
}
