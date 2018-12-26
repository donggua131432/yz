package com.yz.service.statistics;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.statistics.DataExportMapper;
import com.yz.dao.statistics.StudentStatMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.statistics.DataExportQuery;
import com.yz.model.statistics.StudentDataGKExcel;
import com.yz.model.statistics.StudentDataXBExcel;
import com.yz.model.statistics.StudentStatQuery;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.service.stdService.StudentXuexinService;
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
import java.util.List;
import java.util.Map;

/**
 * @描述: 数据导出
 */
@Service
@Transactional
public class DataExportService {

    private static Logger log = LoggerFactory.getLogger(DataExportService.class);

    @Autowired
    DataExportMapper dataExportMapper;

    public void exportStudentDataXB(DataExportQuery query,HttpServletResponse response){
        // 对导出工具进行字段填充
        ExcelUtil.IExcelConfig<StudentDataXBExcel> testExcelCofing = new ExcelUtil.IExcelConfig<StudentDataXBExcel>();
        testExcelCofing.setSheetName("index").setType(StudentDataXBExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("高校学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("入学形式", "enrollType"))
                .addTitle(new ExcelUtil.IExcelTitle("加分条件", "bpType"))
                .addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("志愿1学校", "firstUnvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("志愿1专业", "firstPfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("志愿2学校", "secUnvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("志愿2专业", "secPfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("录取院校", "admitUnvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("录取专业", "admitPfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("在读院校", "learnUnvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("在读专业", "learnPfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("分校", "campusName"))
                .addTitle(new ExcelUtil.IExcelTitle("状态", "stdStage"))
                .addTitle(new ExcelUtil.IExcelTitle("招生老师", "empName"))
                .addTitle(new ExcelUtil.IExcelTitle("招生部门", "dpName"))
                .addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
                .addTitle(new ExcelUtil.IExcelTitle("生日", "birthday"))
                .addTitle(new ExcelUtil.IExcelTitle("户口所在省市区", "provCityDist"))
                .addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
                .addTitle(new ExcelUtil.IExcelTitle("户口类型", "rprType"))
                .addTitle(new ExcelUtil.IExcelTitle("证件类型", "idType"))
                .addTitle(new ExcelUtil.IExcelTitle("工作单位", "workPlace"))
                .addTitle(new ExcelUtil.IExcelTitle("手机", "mobile"))
                .addTitle(new ExcelUtil.IExcelTitle("省", "provinceName"))
                .addTitle(new ExcelUtil.IExcelTitle("市", "cityName"))
                .addTitle(new ExcelUtil.IExcelTitle("区", "districtName"))
                .addTitle(new ExcelUtil.IExcelTitle("地址", "wpAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("入学年度", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("招生日期", "createTime"))
                .addTitle(new ExcelUtil.IExcelTitle("是否转报", "isChange"))
                .addTitle(new ExcelUtil.IExcelTitle("优惠分组", "sg"))
                .addTitle(new ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("辅导员", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("收件地址", "sendAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("考试区县", "taName"))
                .addTitle(new ExcelUtil.IExcelTitle("考生号", "examNo"))
                .addTitle(new ExcelUtil.IExcelTitle("成考分", "sumScore"))
                .addTitle(new ExcelUtil.IExcelTitle("加分", "points"));

        List<StudentDataXBExcel> list = dataExportMapper.exportStudentDataXB(query);

        List<Map<String,String>> enrollTypeList = dataExportMapper.getDictName("enrollType");
        List<Map<String,String>> bpTypeList = dataExportMapper.getDictName("bpType");
        List<Map<String,String>> pfsnLevelList = dataExportMapper.getDictName("pfsnLevel");
        List<Map<String,String>> stdStageList = dataExportMapper.getDictName("stdStage");
        List<Map<String,String>> nationList = dataExportMapper.getDictName("nation");
        List<Map<String,String>> rprTypeList = dataExportMapper.getDictName("rprType");
        List<Map<String,String>> idTypeList = dataExportMapper.getDictName("idType");
        List<Map<String,String>> sgList = dataExportMapper.getDictName("sg");
        List<Map<String,String>> inclusionStatusList = dataExportMapper.getDictName("inclusionStatus");
        List<Map<String,String>> sexList = dataExportMapper.getDictName("sex");

        for (StudentDataXBExcel studentDataXBExcel : list) {
            studentDataXBExcel.setEnrollType(getDictName(enrollTypeList,studentDataXBExcel.getEnrollType()));
            studentDataXBExcel.setBpType(getDictName(bpTypeList,studentDataXBExcel.getBpType()));
            studentDataXBExcel.setPfsnLevel(getDictName(pfsnLevelList,studentDataXBExcel.getPfsnLevel()));
            studentDataXBExcel.setStdStage(getDictName(stdStageList,studentDataXBExcel.getStdStage()));
            studentDataXBExcel.setNation(getDictName(nationList,studentDataXBExcel.getNation()));
            studentDataXBExcel.setRprType(getDictName(rprTypeList,studentDataXBExcel.getRprType()));
            studentDataXBExcel.setIdType(getDictName(idTypeList,studentDataXBExcel.getIdType()));
            studentDataXBExcel.setSg(getDictName(sgList,studentDataXBExcel.getSg()));
            studentDataXBExcel.setInclusionStatus(getDictName(inclusionStatusList,studentDataXBExcel.getInclusionStatus()));
            studentDataXBExcel.setSex(getDictName(sexList,studentDataXBExcel.getSex()));
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuDataXB.xls");
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

    public void exportStudentDataGK(DataExportQuery query,HttpServletResponse response){
        // 对导出工具进行字段填充
        ExcelUtil.IExcelConfig<StudentDataGKExcel> testExcelCofing = new ExcelUtil.IExcelConfig<StudentDataGKExcel>();
        testExcelCofing.setSheetName("index").setType(StudentDataGKExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
                .addTitle(new ExcelUtil.IExcelTitle("政治面貌", "politicalStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("证件类型", "idType"))
                .addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("校区", "campusName"))
                .addTitle(new ExcelUtil.IExcelTitle("学员状态", "stdStage"))
                .addTitle(new ExcelUtil.IExcelTitle("招生部门", "dpName"))
                .addTitle(new ExcelUtil.IExcelTitle("招生老师", "empName"))
                .addTitle(new ExcelUtil.IExcelTitle("出生日期", "birthday"))
                .addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
                .addTitle(new ExcelUtil.IExcelTitle("婚姻状况", "maritalStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("在职状况", "jobStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("户口所在地", "provCityDist"))
                .addTitle(new ExcelUtil.IExcelTitle("户口性质", "rprType"))
                .addTitle(new ExcelUtil.IExcelTitle("手机号码", "mobile"))
                .addTitle(new ExcelUtil.IExcelTitle("固定电话", "telephone"))
                .addTitle(new ExcelUtil.IExcelTitle("邮箱", "email"))
                .addTitle(new ExcelUtil.IExcelTitle("通信地址", "contactAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("邮编", "zipCode"))
                .addTitle(new ExcelUtil.IExcelTitle("专业层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("考区", "taName"))
                .addTitle(new ExcelUtil.IExcelTitle("是否电大毕业", "isOpenUnvs"))
                .addTitle(new ExcelUtil.IExcelTitle("原毕业学校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("毕业时间", "graduateTime"))
                .addTitle(new ExcelUtil.IExcelTitle("原学历证件类型", "edcsType"))
                .addTitle(new ExcelUtil.IExcelTitle("原学历证件号码", "diploma"))
                .addTitle(new ExcelUtil.IExcelTitle("原校区地址", "hisAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("原入学时间", "adminssionTime"))
                .addTitle(new ExcelUtil.IExcelTitle("原毕业专业", "profession"))
                .addTitle(new ExcelUtil.IExcelTitle("原毕业学制", "edcsSystem"))
                .addTitle(new ExcelUtil.IExcelTitle("毕业证上传状态", "annexStatus"));

        List<StudentDataGKExcel> list = dataExportMapper.exportStudentDataGK(query);

        List<Map<String,String>> sexList = dataExportMapper.getDictName("sex");
        List<Map<String,String>> politicalStatusList = dataExportMapper.getDictName("politicalStatus");
        List<Map<String,String>> idTypeList = dataExportMapper.getDictName("idType");
        List<Map<String,String>> stdStageList = dataExportMapper.getDictName("stdStage");
        List<Map<String,String>> nationList = dataExportMapper.getDictName("nation");
        List<Map<String,String>> maritalStatusList = dataExportMapper.getDictName("maritalStatus");
        List<Map<String,String>> rprTypeList = dataExportMapper.getDictName("rprType");
        List<Map<String,String>> jobStatusList = dataExportMapper.getDictName("jobStatus");
        List<Map<String,String>> pfsnLevelList = dataExportMapper.getDictName("pfsnLevel");
        List<Map<String,String>> edcsTypeList = dataExportMapper.getDictName("edcsType");
        List<Map<String,String>> edcsSystemList = dataExportMapper.getDictName("edcsSystem");

        for (StudentDataGKExcel studentDataGKExcel : list) {
            studentDataGKExcel.setSex(getDictName(sexList,studentDataGKExcel.getSex()));
            studentDataGKExcel.setPoliticalStatus(getDictName(politicalStatusList,studentDataGKExcel.getPoliticalStatus()));
            studentDataGKExcel.setIdType(getDictName(idTypeList,studentDataGKExcel.getIdType()));
            studentDataGKExcel.setStdStage(getDictName(stdStageList,studentDataGKExcel.getStdStage()));
            studentDataGKExcel.setNation(getDictName(nationList,studentDataGKExcel.getNation()));
            studentDataGKExcel.setMaritalStatus(getDictName(maritalStatusList,studentDataGKExcel.getMaritalStatus()));
            studentDataGKExcel.setRprType(getDictName(rprTypeList,studentDataGKExcel.getRprType()));
            studentDataGKExcel.setJobStatus(getDictName(jobStatusList,studentDataGKExcel.getJobStatus()));
            studentDataGKExcel.setPfsnLevel(getDictName(pfsnLevelList,studentDataGKExcel.getPfsnLevel()));
            studentDataGKExcel.setEdcsType(getDictName(edcsTypeList,studentDataGKExcel.getEdcsType()));
            studentDataGKExcel.setEdcsSystem(getDictName(edcsSystemList,studentDataGKExcel.getEdcsSystem()));

            if(studentDataGKExcel.getIsOpenUnvs()!=null){
                if(studentDataGKExcel.getIsOpenUnvs().equals("0")){
                    studentDataGKExcel.setIsOpenUnvs("否");
                }else{
                    studentDataGKExcel.setIsOpenUnvs("是");
                }
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuDataGK.xls");
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

    public String getDictName(List<Map<String,String>> list,String value){
        if(value==null){
            return "";
        }
        String dictName = "";
        for(Map<String,String> m:list){
            if(m.get("dict_value").equals(value)){
                dictName = m.get("dict_name");
                break;
            }
        }
        return dictName;
    }
}
