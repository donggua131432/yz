package com.yz.service.stdService;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;

import com.yz.constants.UsConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.stdService.StudentCollectMapper;
import com.yz.dao.stdService.StudentGraduatePaperMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwReceiver;
import com.yz.model.stdService.*;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 学员服务---新生学籍资料收集
 *
 * @author jyt
 */
@Service
public class StudentCollectService {
    private static Logger log = LoggerFactory.getLogger(StudentCollectService.class);

    @Autowired
    private StudentCollectMapper studentCollectMapper;



    public Object findAllCollectList(StudentCollectQuery query) {
        List<StudentCollectInfo> list = studentCollectMapper.findAllCollectList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public StudentCollectInfo getById(String ctId){
        StudentCollectQuery query = new StudentCollectQuery();
        query.setCtId(ctId);
        List<StudentCollectInfo> list = studentCollectMapper.findAllCollectList(query, getUser());
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public int updateRemark(String ctId, String remark) {
        return studentCollectMapper.updateRemark(ctId, remark);
    }

    public int updateCollect(StudentCollectInfo studentCollectInfo){
        if(studentCollectInfo.getIsQualified().equals("1")){
            studentCollectInfo.setUnqualifiedReason("");
        }
        return studentCollectMapper.updateCollect(studentCollectInfo);
    }

    @SuppressWarnings("unchecked")
    public void exportCollectInfo(StudentCollectQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentCollectInfo> testExcelCofing = new IExcelConfig<StudentCollectInfo>();
        testExcelCofing.setSheetName("index").setType(StudentCollectInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("任务名称", "taskTitle"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("资料收取状态", "receiveStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("是否合格", "isQualified"))
                .addTitle(new ExcelUtil.IExcelTitle("不合格原因", "unqualifiedReason"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        List<StudentCollectInfo> list = studentCollectMapper.findAllCollectList(query, getUser());

        for (StudentCollectInfo studentCollectInfo : list) {

            if (StringUtil.hasValue(studentCollectInfo.getPfsnLevel()) && studentCollectInfo.getPfsnLevel().equals("1")) {
                studentCollectInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentCollectInfo.getPfsnLevel()) && studentCollectInfo.getPfsnLevel().equals("5")) {
                studentCollectInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
                studentCollectInfo.setPfsnLevel("");
            }

            if (StringUtil.hasValue(studentCollectInfo.getIsQualified())) {
                if (studentCollectInfo.getIsQualified().equals("0")) {
                    studentCollectInfo.setIsQualified("不合格");
                } else if  (studentCollectInfo.getIsQualified().equals("1")){
                    studentCollectInfo.setIsQualified("合格");
                }else{
                    studentCollectInfo.setIsQualified("");
                }
            }

            if (StringUtil.hasValue(studentCollectInfo.getReceiveStatus())) {
                if (studentCollectInfo.getReceiveStatus().equals("1")) {
                    studentCollectInfo.setReceiveStatus("已收到");
                }else{
                    studentCollectInfo.setReceiveStatus("未收到");
                }
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuCollect.xls");
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
        if(user.getJtList().contains("FDY")){
            user.setUserLevel("6");
        }
        if(user.getJtList().size()==0){
            user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR") || user.getJtList().contains("CJXJ") || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")) {
            user.setUserLevel("1");
        }

        return user;
    }

}
