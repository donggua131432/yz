package com.yz.service.gk;

import com.yz.edu.paging.bean.Page;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.gk.StudentCityAffirmGKMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentCityAffrimGkQuery;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
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
import java.util.List;
import java.util.Map;

/**
 * 国开考试---考场城市确认
 *
 * @author zlp
 */
@Service
@Transactional
public class BdStudentCityAffirmGKService {
    private static Logger log = LoggerFactory.getLogger(BdStudentCityAffirmGKService.class);


    @Autowired
    private StudentCityAffirmGKMapper studentCityAffirmGKMapper;
    
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

    public Object findStudentCityAffirmGKList(StudentCityAffrimGkQuery query) {
        List<StudentCityAffirmGKInfo> list = studentCityAffirmGKMapper.findStudentCityAffirmGKList(query, getUser());
        return new IPageInfo<>((Page) list);
    }
    
    public List<Map<String, String>> getExamCityForGK(String status){
    	List<Map<String, String>> list = studentCityAffirmGKMapper.getExamCityForGK(status);
    	return list;
    }
   
    public StudentCityAffirmGKInfo getGkStuCityAffirmById(String affirmId) {
    	StudentCityAffrimGkQuery query = new StudentCityAffrimGkQuery();
        query.setAffirmId(affirmId);
        List<StudentCityAffirmGKInfo> list = studentCityAffirmGKMapper.findStudentCityAffirmGKList(query, getUser());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    public int updateReason(String affirmId, String reason,String isExam) {
    	if(StringUtil.isEmpty(isExam)) {
    		isExam="0";
    	}
        return studentCityAffirmGKMapper.updateReason(affirmId, reason,isExam);
    }

    public int resetTask(String affirmId, String taskId, String learnId) {
        return studentCityAffirmGKMapper.resetTask(affirmId, taskId, learnId);
    }

    @SuppressWarnings("unchecked")
    public void exportStuCityAffirmInfo(StudentCityAffrimGkQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentCityAffirmGKInfo> testExcelCofing = new IExcelConfig<StudentCityAffirmGKInfo>();
        testExcelCofing.setSheetName("index").setType(StudentCityAffirmGKInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("国开考试年度", "examYear"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("考试城市", "ecName"))
                .addTitle(new ExcelUtil.IExcelTitle("未确认原因", "reason"))
                .addTitle(new ExcelUtil.IExcelTitle("是否确认", "isAffirm"))
                .addTitle(new ExcelUtil.IExcelTitle("是否考试", "isExam"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"));

        List<StudentCityAffirmGKInfo> list = studentCityAffirmGKMapper.findStudentCityAffirmGKList(query, getUser());

        for (StudentCityAffirmGKInfo cityAffirmInfo : list) {
        	//现场确认结果
            if (cityAffirmInfo.getIsAffirm().equals("0")) {
            	cityAffirmInfo.setIsAffirm("未确认");
            } else {
            	cityAffirmInfo.setIsAffirm("已确认");
            } 
            
            //是否考试
            if (cityAffirmInfo.getIsExam().equals("1")) {
            	cityAffirmInfo.setIsExam("是");
            } else {
            	cityAffirmInfo.setIsExam("否");
            } 
           

            if (StringUtil.hasValue(cityAffirmInfo.getPfsnLevel()) && cityAffirmInfo.getPfsnLevel().equals("1")) {
            	cityAffirmInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(cityAffirmInfo.getPfsnLevel()) && cityAffirmInfo.getPfsnLevel().equals("5")) {
            	cityAffirmInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
            	cityAffirmInfo.setPfsnLevel("");
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=gkStuCityAffirm.xls");
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
    
    public Object getGkCityStatisticsInfo(String eyId,String statGroup) {
        List<StudentCityAffirmGKInfo> list = studentCityAffirmGKMapper.getGkCityStatisticsInfo(eyId,statGroup);
        return new IPageInfo<>((Page) list);
    }
    
    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        //没有职称看所有数据
        if(user.getJtList()==null||user.getJtList().size()==0) {
        	user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR")  || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")||user.getJtList().contains("400")) {
            user.setUserLevel("1");
        }else if(user.getJtList().contains("CJZFDY") || user.getJtList().contains("GKZFDY")) {
        	user.setUserLevel("6");
        }
        return user;
    }
}
