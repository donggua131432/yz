package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.stdService.StudentGraduateDataMapper;
import com.yz.dao.stdService.StudentXuexinMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduateDataQuery;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.model.stdService.StudentXuexinQuery;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 学员服务---学信网信息核对
 *
 * @author jyt
 */
@Service
@Transactional
public class StudentXuexinService {
    private static Logger log = LoggerFactory.getLogger(StudentXuexinService.class);

    @Autowired
    private StudentXuexinMapper studentXuexinMapper;

    public Object findAllXuexinList(StudentXuexinQuery query) {
        List<StudentXuexinInfo> list = studentXuexinMapper.findAllXuexinList(query, getUser());
        return new IPageInfo<>((Page) list);
    }

    public StudentXuexinInfo getXuexinInfoById(String xuexinId) {
        StudentXuexinQuery query = new StudentXuexinQuery();
        query.setXuexinId(xuexinId);
        List<StudentXuexinInfo> list = studentXuexinMapper.findAllXuexinList(query, getUser());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void importStuGraduateDataInfo(MultipartFile stuXuexinImport) {
        //对导入工具进行字段填充
        IExcelConfig<StudentXuexinInfo> testExcelCofing = new IExcelConfig<StudentXuexinInfo>();
        testExcelCofing.setSheetName("index").setType(StudentXuexinInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否完成", "isView"))
                .addTitle(new ExcelUtil.IExcelTitle("完成时间", "viewTime"))
                .addTitle(new ExcelUtil.IExcelTitle("信息是否有误", "isError"))
                .addTitle(new ExcelUtil.IExcelTitle("学员反馈", "feedback"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentXuexinInfo> list = ExcelUtil.importWorkbook(stuXuexinImport.getInputStream(), testExcelCofing,
                    stuXuexinImport.getOriginalFilename());
            // 遍历插入
            for (StudentXuexinInfo studentXuexinInfo : list) {
                if (!StringUtil.hasValue(studentXuexinInfo.getStdNo())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学籍编号不能为空");
                }
                if (!StringUtil.hasValue(studentXuexinInfo.getSchoolRoll())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学号不能为空");
                }
                if (!StringUtil.hasValue(studentXuexinInfo.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                if (StringUtil.hasValue(studentXuexinInfo.getViewTime())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        format.setLenient(false);
                        format.parse(studentXuexinInfo.getViewTime());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！完成时间格式错误，正确格式如:2018-01-26 12:00:00");
                    }
                } else {
                    studentXuexinInfo.setViewTime(null);
                }

                if (StringUtil.hasValue(studentXuexinInfo.getIsView())) {
                    if (studentXuexinInfo.getIsView().equals("是")) {
                        studentXuexinInfo.setIsView("1");
                    } else {
                        studentXuexinInfo.setIsView("0");
                    }
                } else {
                    studentXuexinInfo.setIsView("0");
                }

                if (StringUtil.hasValue(studentXuexinInfo.getIsError())) {
                    if (studentXuexinInfo.getIsError().equals("有误")) {
                        studentXuexinInfo.setIsError("1");
                    } else if (studentXuexinInfo.getIsError().equals("无误")) {
                        studentXuexinInfo.setIsError("0");
                    }
                } else {
                    studentXuexinInfo.setIsError(null);
                }
                index++;

            }
            List<Map<String, Object>> resultList = studentXuexinMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("学信网信息核对以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("school_roll") + "-" + map.get("std_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            studentXuexinMapper.insert(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    public int updateRemark(String xuexinId, String remark) {
        return studentXuexinMapper.updateRemark(xuexinId, remark);
    }

    public int resetTask(String xuexinId, String taskId, String learnId) {
        return studentXuexinMapper.resetTask(xuexinId, taskId, learnId);
    }

    @SuppressWarnings("unchecked")
    public void exportXuexinInfo(StudentXuexinQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentXuexinInfo> testExcelCofing = new IExcelConfig<StudentXuexinInfo>();
        testExcelCofing.setSheetName("index").setType(StudentXuexinInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否完成", "isView"))
                .addTitle(new ExcelUtil.IExcelTitle("完成时间", "viewTime"))
                .addTitle(new ExcelUtil.IExcelTitle("信息是否有误", "isError"))
                .addTitle(new ExcelUtil.IExcelTitle("学员反馈", "feedback"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"));

        List<StudentXuexinInfo> list = studentXuexinMapper.findAllXuexinList(query, getUser());

        for (StudentXuexinInfo studentXuexinInfo : list) {

            if (studentXuexinInfo.getIsView().equals("0")) {
                studentXuexinInfo.setIsView("否");
            } else {
                studentXuexinInfo.setIsView("是");
            }

            if (StringUtil.hasValue(studentXuexinInfo.getIsError()) && studentXuexinInfo.getIsError().equals("0")) {
                studentXuexinInfo.setIsError("无误");
            } else if (StringUtil.hasValue(studentXuexinInfo.getIsError()) && studentXuexinInfo.getIsError().equals("1")) {
                studentXuexinInfo.setIsError("有误");
            } else {
                studentXuexinInfo.setIsError("");
            }

            if (StringUtil.hasValue(studentXuexinInfo.getPfsnLevel()) && studentXuexinInfo.getPfsnLevel().equals("1")) {
                studentXuexinInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentXuexinInfo.getPfsnLevel()) && studentXuexinInfo.getPfsnLevel().equals("5")) {
                studentXuexinInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
                studentXuexinInfo.setPfsnLevel("");
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuXuexin.xls");
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
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR") || user.getJtList().contains("CJXJ") || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")) {
            user.setUserLevel("1");
        }
        return user;
    }
}
