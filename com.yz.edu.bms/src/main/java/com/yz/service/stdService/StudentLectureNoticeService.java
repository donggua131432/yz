package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.stdService.StudentLectureNoticeMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentLectureNoticeInfo;
import com.yz.util.DateUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 学员服务---开课通知确认跟进
 *
 * @author zlp
 */
@Service
@Transactional
public class StudentLectureNoticeService {
    private static Logger log = LoggerFactory.getLogger(StudentLectureNoticeService.class);


    @Autowired
    private StudentLectureNoticeMapper studentLectureNoticeMapper;

    public Object findAllLectureNoticeList(StudentLectureNoticeInfo query) {
        List<StudentLectureNoticeInfo> list = studentLectureNoticeMapper.findAllLectureNoticeList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public StudentLectureNoticeInfo getLectureNoticeInfoById(String lectureId) {
    	StudentLectureNoticeInfo query = new StudentLectureNoticeInfo();
        query.setLectureId(lectureId);
        List<StudentLectureNoticeInfo> list = studentLectureNoticeMapper.findAllLectureNoticeList(query, getUser());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    public int updateRemark(String lectureId, String remark) {
    	
        return studentLectureNoticeMapper.updateRemark(lectureId, remark);
    }

    public int resetTask(String lectureId, String taskId, String learnId) {
        return studentLectureNoticeMapper.resetTask(lectureId, taskId, learnId);
    }
    
    @SuppressWarnings("unchecked")
    public void exportLectureNoticeInfo(StudentLectureNoticeInfo query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentLectureNoticeInfo> testExcelCofing = new IExcelConfig<StudentLectureNoticeInfo>();
        testExcelCofing.setSheetName("index").setType(StudentLectureNoticeInfo.class) 
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否收到教材", "isReceiveBook"))
                .addTitle(new ExcelUtil.IExcelTitle("是否知道课程表查看方式", "isKnowTimetables"))
                .addTitle(new ExcelUtil.IExcelTitle("是否知道上课方式", "isKnowCourseType"))
                .addTitle(new ExcelUtil.IExcelTitle("提交时间", "submitTime"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        List<StudentLectureNoticeInfo> list = studentLectureNoticeMapper.findAllLectureNoticeList(query, getUser());

        for (StudentLectureNoticeInfo studentLectureInfo : list) {
        	

            if (StringUtil.hasValue(studentLectureInfo.getPfsnLevel()) && studentLectureInfo.getPfsnLevel().equals("1")) {
            	studentLectureInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentLectureInfo.getPfsnLevel()) && studentLectureInfo.getPfsnLevel().equals("5")) {
            	studentLectureInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
            	studentLectureInfo.setPfsnLevel("");
            }
            
            if (StringUtil.hasValue(studentLectureInfo.getIsReceiveBook()) && studentLectureInfo.getIsReceiveBook().equals("0")) {
            	studentLectureInfo.setIsReceiveBook("否");
            } else if (StringUtil.hasValue(studentLectureInfo.getIsReceiveBook()) && studentLectureInfo.getIsReceiveBook().equals("1")) {
            	studentLectureInfo.setIsReceiveBook("是");
            } else {
            	studentLectureInfo.setIsReceiveBook("");
            }
            
            if (StringUtil.hasValue(studentLectureInfo.getIsKnowTimetables()) && studentLectureInfo.getIsKnowTimetables().equals("0")) {
            	studentLectureInfo.setIsKnowTimetables("否");
            } else if (StringUtil.hasValue(studentLectureInfo.getIsKnowTimetables()) && studentLectureInfo.getIsKnowTimetables().equals("1")) {
            	studentLectureInfo.setIsKnowTimetables("是");
            } else {
            	studentLectureInfo.setIsKnowTimetables("");
            }
            
            if (StringUtil.hasValue(studentLectureInfo.getIsKnowCourseType()) && studentLectureInfo.getIsKnowCourseType().equals("0")) {
            	studentLectureInfo.setIsKnowCourseType("否");
            } else if (StringUtil.hasValue(studentLectureInfo.getIsKnowCourseType()) && studentLectureInfo.getIsKnowCourseType().equals("1")) {
            	studentLectureInfo.setIsKnowCourseType("是");
            } else {
            	studentLectureInfo.setIsKnowCourseType("");
            }
            
            if(StringUtil.hasValue(studentLectureInfo.getSubmitTime())) {
            	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	try {
					studentLectureInfo.setSubmitTime(DateUtil.formatDate(format.parse(studentLectureInfo.getSubmitTime()), "yyyy-MM-dd HH:mm:ss"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
            

        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuLectureNotice.xls");
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
    
    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        //没有职称看所有数据
        if(user.getJtList()==null||user.getJtList().size()==0) {
        	user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR") || user.getJtList().contains("CJXJ") || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")) {
            user.setUserLevel("1");
        }else if(user.getJtList().contains("CJZFDY") || user.getJtList().contains("GKZFDY")) {
        	user.setUserLevel("6");
        }
        return user;
    }
}
