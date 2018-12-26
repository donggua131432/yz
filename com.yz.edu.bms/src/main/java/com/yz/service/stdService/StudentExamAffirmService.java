package com.yz.service.stdService;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.stdService.StudentExamAffirmMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentExamAffirmQuery;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员服务--考场确认跟进
 * @author lx
 * @date 2017年12月7日 下午2:35:15
 */
@Service
@Transactional
public class StudentExamAffirmService
{
	private static Logger log = LoggerFactory.getLogger(StudentExamAffirmService.class);

	@Autowired
	private StudentExamAffirmMapper examAffirmMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object queryStudentAffirmList(int page,int pageSize,StudentExamAffirmQuery query){
		PageHelper.offsetPage(page, pageSize);
		List<StudentExamAffirmInfo> list = examAffirmMapper.queryStudentAffirmList(query,getUser());
		
		return new IPageInfo<>((Page)list);
	}
	
	public StudentExamAffirmInfo getExamAffirmInfoById(String affirmId){
		StudentExamAffirmInfo affirmInfo = examAffirmMapper.getExamAffirmInfoById(affirmId);
		if(affirmInfo == null){
			affirmInfo = new StudentExamAffirmInfo();
		}
		return affirmInfo;
	}
	public Object getExamYear(String status){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = examAffirmMapper.getExamYear(status);
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("eyId"));
				map.put("dictName", resultMap.get("examYear"));
				resultList.add(map);
			}

		}
		return resultList;
	}
	
	public Object getExamReason(String eyId){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = examAffirmMapper.getExamReason(eyId);
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("erId"));
				map.put("dictName", resultMap.get("reason"));
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	public void changeUnconfirmeReason(StudentExamAffirmInfo info){
		BaseUser user = SessionUtil.getUser();
		if(StringUtil.isEmpty(info.getErId())){
			info.setErId(null);
		}
		examAffirmMapper.changeUnconfirmeReason(info,user);
	}
	
	public void resetResult(String affirmId,String taskId,String learnId){
		BaseUser user = SessionUtil.getUser();
		examAffirmMapper.resetResult(affirmId,taskId,learnId,user);
	}

	@SuppressWarnings("unchecked")
	public void examAffirmExport(StudentExamAffirmQuery query, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<StudentExamAffirmInfo> testExcelCofing = new ExcelUtil.IExcelConfig<StudentExamAffirmInfo>();
		testExcelCofing.setSheetName("index").setType(StudentExamAffirmInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("考试年度", "testYear"))
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("是否确认", "pyId"))
				.addTitle(new ExcelUtil.IExcelTitle("确认结果", "epName"))
				.addTitle(new ExcelUtil.IExcelTitle("班主任", "tutorName"))
				.addTitle(new ExcelUtil.IExcelTitle("未确认原因", "unconfirmedReason"));

		List<StudentExamAffirmInfo> list = examAffirmMapper.queryStudentAffirmList(query, getUser());

		for (StudentExamAffirmInfo studentExamAffirmInfo : list) {

			if (StringUtil.hasValue(studentExamAffirmInfo.getPfsnLevel()) && studentExamAffirmInfo.getPfsnLevel().equals("1")) {
				studentExamAffirmInfo.setPfsnLevel("1>专科升本科类");
			} else if (StringUtil.hasValue(studentExamAffirmInfo.getPfsnLevel()) && studentExamAffirmInfo.getPfsnLevel().equals("5")) {
				studentExamAffirmInfo.setPfsnLevel("5>高中起点高职高专");
			} else {
				studentExamAffirmInfo.setPfsnLevel("");
			}

			if(studentExamAffirmInfo.getPyId()==null){
				studentExamAffirmInfo.setPyId("否");
			}else {
				studentExamAffirmInfo.setPyId("是");
			}
			if(studentExamAffirmInfo.getEpName()==null){
				studentExamAffirmInfo.setEpName("");
			}else {
				studentExamAffirmInfo.setEpName(studentExamAffirmInfo.getEpName() + "\n" + studentExamAffirmInfo.getStartTime() + "\n" + studentExamAffirmInfo.getEndTime());
			}
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=stuExamAffirm.xls");
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

	public BaseUser getUser(){
		BaseUser user  =SessionUtil.getUser();
		//TODO 数据权限
		List<String> jtList = user.getJtList();
		if(jtList.contains("BMZR")){//如果是部门主任
			user.setUserLevel("1");//查看所有,成教and国开
		}else if(jtList.contains("GKXJ") && jtList.contains("CJXJ")){ //既有国开又有成教 认为 部门主任
			user.setUserLevel("1");
		}else if(jtList.contains("GKXJ")){ //国开学籍
			user.setUserLevel("2");//只看国开
		}else if(jtList.contains("CJXJ")){ //成教学籍
			user.setUserLevel("3");//只看成教
		}else if(jtList.contains("FDY")){//班主任
			user.setUserLevel("4"); //只看自己
		}
		return user;
	}
}
