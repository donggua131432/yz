package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.stdService.StudentDegreeEnglishMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentDegreeEnglishInfo;
import com.yz.model.stdService.StudentDegreeEnglishQuery;
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
import java.util.List;
import java.util.Map;

/**
 * 学员服务---学位英语跟进
 *
 * @author zlp
 */
@Service
public class StudentDegreeService {
    private static Logger log = LoggerFactory.getLogger(StudentDegreeService.class);


    @Autowired
    private StudentDegreeEnglishMapper studentDegreeMapper;
    
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

    public Object findAllDegreeEnglishList(StudentDegreeEnglishQuery query) {
        List<StudentDegreeEnglishInfo> list = studentDegreeMapper.findAllDegreeEnglishList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public StudentDegreeEnglishInfo getDegreeEnglishInfoById(String degreeId) {
    	StudentDegreeEnglishQuery query = new StudentDegreeEnglishQuery();
        query.setDegreeId(degreeId);
        List<StudentDegreeEnglishInfo> list = studentDegreeMapper.findAllDegreeEnglishList(query, getUser());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void importStuDegreeDataInfo(MultipartFile stuDegreeImport) {
        //对导入工具进行字段填充
        IExcelConfig<StudentDegreeEnglishInfo> testExcelCofing = new IExcelConfig<StudentDegreeEnglishInfo>();
        testExcelCofing.setSheetName("index").setType(StudentDegreeEnglishInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("考试成绩", "score"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentDegreeEnglishInfo> list = ExcelUtil.importWorkbook(stuDegreeImport.getInputStream(), testExcelCofing,
            		stuDegreeImport.getOriginalFilename());
            // 遍历插入
            for (StudentDegreeEnglishInfo studentDegreeInfo : list) {
                
                if (!StringUtil.hasValue(studentDegreeInfo.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (!StringUtil.hasValue(studentDegreeInfo.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
                }
                if (!StringUtil.hasValue(studentDegreeInfo.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                index++;

            }
            List<Map<String, Object>> resultList = studentDegreeMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("核对以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append("身份证："+map.get("id_card") + "-姓名：" + map.get("std_name") +"-年级：" + map.get("grade")+"<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            studentDegreeMapper.insert(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    @Transactional
    public int updateRemark(String degreeId, String remark,String isSceneConfirm) {
    	if(StringUtil.isEmpty(isSceneConfirm)) {
    		isSceneConfirm="0";
    	}
        return studentDegreeMapper.updateRemark(degreeId, remark,isSceneConfirm);
    }

    @Transactional
    public int resetTask(String degreeId, String taskId, String learnId) {
        return studentDegreeMapper.resetTask(degreeId, taskId, learnId);
    }

    @SuppressWarnings("unchecked")
    public void exportDegreeEnglishInfo(StudentDegreeEnglishQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentDegreeEnglishInfo> testExcelCofing = new IExcelConfig<StudentDegreeEnglishInfo>();
        testExcelCofing.setSheetName("index").setType(StudentDegreeEnglishInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否报名", "isEnroll"))
                .addTitle(new ExcelUtil.IExcelTitle("报名编号", "enrollNo"))
                .addTitle(new ExcelUtil.IExcelTitle("现场确认结果", "isSceneConfirm"))
                .addTitle(new ExcelUtil.IExcelTitle("考试成绩", "score"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        List<StudentDegreeEnglishInfo> list = studentDegreeMapper.findAllDegreeEnglishList(query, getUser());

        for (StudentDegreeEnglishInfo studentDegreeInfo : list) {
        	//现场确认结果
            if (studentDegreeInfo.getIsSceneConfirm().equals("0")) {
            	studentDegreeInfo.setIsSceneConfirm("未确认");
            } else {
            	studentDegreeInfo.setIsSceneConfirm("已确认");
            } 
            // 是否报名转换
			String valueTemple = dictExchangeUtil.getParamKey("enrollresult", studentDegreeInfo.getIsEnroll().trim());
			studentDegreeInfo.setIsEnroll(valueTemple);
           

            if (StringUtil.hasValue(studentDegreeInfo.getPfsnLevel()) && studentDegreeInfo.getPfsnLevel().equals("1")) {
            	studentDegreeInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentDegreeInfo.getPfsnLevel()) && studentDegreeInfo.getPfsnLevel().equals("5")) {
            	studentDegreeInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
            	studentDegreeInfo.setPfsnLevel("");
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuDegreeEnglish.xls");
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
