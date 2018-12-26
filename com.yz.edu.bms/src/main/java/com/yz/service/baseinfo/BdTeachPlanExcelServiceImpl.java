package com.yz.service.baseinfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.baseinfo.BdTeachPlanMapper;
import com.yz.model.baseinfo.BdTeachPlanMap;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
public class BdTeachPlanExcelServiceImpl {

	@Autowired
	private BdTeachPlanMapper teachPlanMapper;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	public void uploadTestSubject(MultipartFile excelTeachPlan) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdTeachPlanMap> testExcelCofing = new ExcelUtil.IExcelConfig<BdTeachPlanMap>();
		testExcelCofing.setSheetName("index").setType(BdTeachPlanMap.class)
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业编码", "pfsnCode"))
				.addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("教学计划名称", "thpName"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("考试科目", "testSubject"));
		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdTeachPlanMap> list = ExcelUtil.importWorkbook(excelTeachPlan.getInputStream(), testExcelCofing,
					excelTeachPlan.getOriginalFilename());
			String grade = null;
			String semester = null;
			String pfsnLevel = null;	
			// 遍历插入
			for (BdTeachPlanMap teachPlan : list) {
				if(!StringUtil.hasValue(teachPlan.getUnvsName())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！大学院校不能为空");
				}
				if(!StringUtil.hasValue(teachPlan.getPfsnName())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业名称不能为空");
				}
				if(!StringUtil.hasValue(teachPlan.getThpName())){
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！教学计划名称不能为空");
				}			
				// 专业层次
				pfsnLevel = dictExchangeUtil.getParamValue("pfsnLevel", teachPlan.getPfsnLevel().trim());
				if (null == pfsnLevel) {
					throw new IllegalArgumentException(
							"excel数据格式错误，请检查第" + index + "行数据！报考层次格式错误，正确格式如:[1>专科升本科类,5>高中起点高职高专]");
				} else {
					teachPlan.setPfsnLevel(pfsnLevel);
				}
				// 年级转换
				grade = dictExchangeUtil.getParamValue("grade", teachPlan.getGrade().trim());
				if (null == grade) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级格式错误,正确格式如:[2016级]");
				} else {
					teachPlan.setGrade(grade);
				}
				
				// 学期转换
				semester = dictExchangeUtil.getParamValue("semester", teachPlan.getSemester());
				if(null == semester){
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！学期格式错误,格式如：[1-6]之间的数字");
				}else {
					teachPlan.setSemester(semester);
				}
				index++;
			}
			//初始化临时表
			teachPlanMapper.initTeachPlanTable(list);
			//查找不存的教学计划
			List<Map<String, Object>> courseList = teachPlanMapper.getNonExistsTeachPlan(list);
            if (courseList!=null&&courseList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("教学计划不存在：<br/>"); 
                for (Map<String, Object> map : courseList) {
                    sb.append(map.get("grade") +"级-第"+map.get("semester") +"学期-"+map.get("unvs_name") +"层次-"+dictExchangeUtil.getParamKey("pfsnLevel", map.get("pfsn_level").toString()) +"-专业编码["+map.get("pfsn_code")+"]-"+map.get("pfsn_name") +"-"+map.get("thp_name") +"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            
            //更新考试科目
            teachPlanMapper.batchUpdateTestSubjectByExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

}
