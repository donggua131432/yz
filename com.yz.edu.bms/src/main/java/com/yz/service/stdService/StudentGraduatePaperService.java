package com.yz.service.stdService;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.core.constants.AppConstants;
import com.yz.dao.system.SysDictMapper;
import com.yz.edu.paging.bean.Page;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.stdService.StudentGraduatePaperMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwReceiver;
import com.yz.model.stdService.*;
import com.yz.model.system.SysDict;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员服务---学信网信息核对
 *
 * @author jyt
 */
@Service
public class StudentGraduatePaperService {
    private static Logger log = LoggerFactory.getLogger(StudentGraduatePaperService.class);

    @Autowired
    private StudentGraduatePaperMapper studentGraduatePaperMapper;

    @Autowired
    private BdStdEnrollMapper stdMapper;

    @Autowired
    private UsInfoMapper usInfoMapper;

    @Autowired
    private SysDictMapper sysDictMapper;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findAllPaperList(StudentGraduatePaperQuery query) {
        List<StudentGraduatePaperInfo> list = studentGraduatePaperMapper.findAllPaperList(query, getUser());
        return new IPageInfo<>((Page) list);
    }


    @SuppressWarnings("unchecked")
    public void importStuGraduatePaperInfo(MultipartFile stuPaperImport) {
        //对导入工具进行字段填充
        IExcelConfig<StudentGraduatePaperInfo> testExcelCofing = new IExcelConfig<StudentGraduatePaperInfo>();
        testExcelCofing.setSheetName("index").setType(StudentGraduatePaperInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("论文编号", "paperNo"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师", "guideTeacher"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师联系电话", "guideTeacherPhone"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师邮箱", "guideTeacherEmail"))
                .addTitle(new ExcelUtil.IExcelTitle("论文题目", "paperTitle"))
                .addTitle(new ExcelUtil.IExcelTitle("学院（系）", "schoolDepartment"))
        ;
        // 行数记录
        int index = 2;
        try {
            //查询需要导入毕业论文题目的学校
            List<SysDict> sysDicts = sysDictMapper.selectByPid(AppConstants.PAPER_TITLE_IMPORT_SCHOOL);
            boolean sysDictsIsNotNull = sysDicts != null && sysDicts.size() > 0;
            // 对文件进行分析转对象
            List<StudentGraduatePaperInfo> list = ExcelUtil.importWorkbook(stuPaperImport.getInputStream(), testExcelCofing,
                    stuPaperImport.getOriginalFilename());
            // 遍历插入
            for (StudentGraduatePaperInfo studentGraduatePaperInfo : list) {
                if (!StringUtil.hasValue(studentGraduatePaperInfo.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(studentGraduatePaperInfo.getIdCard())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号码不能为空");
                }
                if (!StringUtil.hasValue(studentGraduatePaperInfo.getStdName())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
                }
                //判断是否需要导入本科论文题目
                if(StringUtil.hasValue(studentGraduatePaperInfo.getUnvsName())){//判断是否填入学校名称
                    boolean isImport = true;
                    if(sysDictsIsNotNull){
                        for (SysDict sd : sysDicts) {
                            if(sd.getDictName().equals(studentGraduatePaperInfo.getUnvsName())){//是否为需要导入论文题目的学校
                                if(!StringUtil.hasValue(studentGraduatePaperInfo.getPaperTitle())){
                                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！" + sd.getDictName() + "的论文题目不能为空！");
                                }else {
                                    //判断是否论文题目过长
                                    if(studentGraduatePaperInfo.getPaperTitle().length() >= AppConstants.PAPER_TITLE_IMPORT_LENGTH){
                                        throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！" + sd.getDictName() + "的论文题目超过50个字！");
                                    }
                                }
                                isImport = false;
                                break;
                            }
                        }
                    }
                    if (isImport){
                        studentGraduatePaperInfo.setPaperTitle(null);
                    }
                }

                index++;
            }
            List<Map<String, Object>> resultList = studentGraduatePaperMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("业论文及报告提交以下学员不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("id_card") + "-" + map.get("std_name") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            studentGraduatePaperMapper.insert(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    public int updateRemark(String gpId, String remark) {
        return studentGraduatePaperMapper.updateRemark(gpId, remark);
    }

    public void insertAttachment(StudentAttachment attachment){
    	attachment.setAttachmentId(IDGenerator.generatorId());
        studentGraduatePaperMapper.insertAttachment(attachment);
    }

    public StudentAttachment getAttachmentById(String attachmentId){
        return studentGraduatePaperMapper.getAttachmentById(attachmentId);
    }

    @Transactional
    public void updateAttachment(StudentAttachment attachment){
        studentGraduatePaperMapper.updateAttachment(attachment);
        String msg = "您好，<"+attachment.getAttachmentName()+">通过审核，详情请用电脑登陆【远智学堂】-【我的任务】-【毕业生报告及论文】查看审核结果，并按要求在3月30日前提交纸质资料。";
        if(attachment.getCheckStatus().equals("2")){
            msg = "您好，<"+attachment.getAttachmentName()+">审核不通过，详情请用电脑登陆【远智学堂】-【我的任务】-【毕业生报告及论文】查看审核结果，并按要求在3月30日前提交纸质资料。";
            studentGraduatePaperMapper.updatePaperCheckStatus(attachment.getLearnId(),"3");
            SendWeChat(attachment.getLearnId(),msg);
        }else if(attachment.getCheckStatus().equals("1")){
            SendWeChat(attachment.getLearnId(),msg);
        }
        Map<String,String> map = studentGraduatePaperMapper.getLearnInfo(attachment.getLearnId());
        if(!map.isEmpty()){
            if(studentGraduatePaperMapper.countCheckSuccessAttachment(attachment.getLearnId())==getDocCount(map.get("unvs_id").toString(),map.get("pfsn_level"))){
                studentGraduatePaperMapper.updatePaperCheckStatus(attachment.getLearnId(),"2");
            }
        }
    }

    public void updatePaperStatus(StudentGraduatePaperInfo paperInfo){
        studentGraduatePaperMapper.updatePaperStatus(paperInfo);
    }

    public Object selectStudentAttachment(String learnId) {
        List<StudentAttachment> list = studentGraduatePaperMapper.selectUserStudentAttachment(learnId);
        if (list != null && !list.isEmpty()) {
            return new IPageInfo<StudentAttachment>(list, list.size());
        } else {
            list = new ArrayList<>();
            return new IPageInfo<StudentAttachment>(list, 0);
        }
    }

    public Object selectCheckStudentAttachment(String learnId) {
        List<StudentAttachment> list = studentGraduatePaperMapper.selectCheckStudentAttachment(learnId);
        if (list != null && !list.isEmpty()) {
            return new IPageInfo<StudentAttachment>(list, list.size());
        } else {
            list = new ArrayList<>();
            return new IPageInfo<StudentAttachment>(list, 0);
        }
    }

    @SuppressWarnings("unchecked")
    public void exportPaperInfo(StudentGraduatePaperQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        IExcelConfig<StudentGraduatePaperInfo> testExcelCofing = new IExcelConfig<StudentGraduatePaperInfo>();
        testExcelCofing.setSheetName("index").setType(StudentGraduatePaperInfo.class)
                .addTitle(new ExcelUtil.IExcelTitle("任务名称", "taskTitle"))
                .addTitle(new ExcelUtil.IExcelTitle("论文编号", "paperNo"))
                .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师", "guideTeacher"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师邮箱", "guideTeacherEmail"))
                .addTitle(new ExcelUtil.IExcelTitle("指导老师联系电话", "guideTeacherPhone"))
                .addTitle(new ExcelUtil.IExcelTitle("本论文题目", "paperTitle"))
                .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                .addTitle(new ExcelUtil.IExcelTitle("是否查看", "isView"))
                .addTitle(new ExcelUtil.IExcelTitle("是否上传", "isUpload"))
                .addTitle(new ExcelUtil.IExcelTitle("审批状态", "checkStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"))
                .addTitle(new ExcelUtil.IExcelTitle("学院（系）", "schoolDepartment"));

        List<StudentGraduatePaperInfo> list = studentGraduatePaperMapper.findAllPaperList(query, getUser());

        for (StudentGraduatePaperInfo studentGraduatePaperInfo : list) {

            if (StringUtil.hasValue(studentGraduatePaperInfo.getPfsnLevel()) && studentGraduatePaperInfo.getPfsnLevel().equals("1")) {
                studentGraduatePaperInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentGraduatePaperInfo.getPfsnLevel()) && studentGraduatePaperInfo.getPfsnLevel().equals("5")) {
                studentGraduatePaperInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
                studentGraduatePaperInfo.setPfsnLevel("");
            }
            if (StringUtil.hasValue(studentGraduatePaperInfo.getIsView())) {
                if ("0".equals(studentGraduatePaperInfo.getIsView())) {
                    studentGraduatePaperInfo.setIsView("否");
                } else {
                    studentGraduatePaperInfo.setIsView("是");
                }
            }
            if (StringUtil.hasValue(studentGraduatePaperInfo.getIsUpload())) {
                if (studentGraduatePaperInfo.getIsUpload().equals("0")) {
                    studentGraduatePaperInfo.setIsUpload("否");
                } else {
                    studentGraduatePaperInfo.setIsUpload("是");
                }
            }
            if (StringUtil.hasValue(studentGraduatePaperInfo.getCheckStatus())) {
                if (studentGraduatePaperInfo.getCheckStatus().equals("1")) {
                    studentGraduatePaperInfo.setCheckStatus("审核中");
                }
                else if(studentGraduatePaperInfo.getCheckStatus().equals("2")){
                    studentGraduatePaperInfo.setCheckStatus("审核通过");
                }
                else if(studentGraduatePaperInfo.getCheckStatus().equals("3")){
                    studentGraduatePaperInfo.setCheckStatus("审核不通过");
                }
                else {
                    studentGraduatePaperInfo.setCheckStatus("未完成");
                }
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuPaper.xls");
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

    /**
     *
     1、嘉应专科需提交：实习鉴定表（1份），实习报告（1份）

     2、嘉应本科需提交：论文（1份）、论文选题表（1份）、实习鉴定表（1份）

     3、仲恺专科需提交：实习报告（1份）

     4、仲恺本科需提交：论文（各提交两份纸质资料）论文计划书（各提交两份纸质资料）
     * @param unvsId
     * @param pfsnLevel
     * @return
     */
    private int getDocCount(String unvsId,String pfsnLevel){
        if(unvsId.equals("1") && pfsnLevel.equals("5")){
            return 2;
        }
        if(unvsId.equals("1") && pfsnLevel.equals("1")){
            return 3;
        }
        if(unvsId.equals("2") && pfsnLevel.equals("5")){
            return 1;
        }
        if(unvsId.equals("2") && pfsnLevel.equals("1")){
            return 2;
        }
        return 0;
    }

    private void SendWeChat(String learnId,String msg){
        GwReceiver std = stdMapper.selectStdInfoByLearnId(learnId);
        String openId = usInfoMapper.selectUserOpenId(std.getUserId());
        
		//推送客服微信公众号信息
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(openId);
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("content", msg);
		msgVo.setContentData(contentMap);
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
    }
}
