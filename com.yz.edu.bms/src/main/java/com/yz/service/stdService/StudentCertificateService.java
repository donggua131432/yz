package com.yz.service.stdService;

import com.yz.edu.paging.bean.Page;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.stdService.StudentCertificateMapper;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwReceiver;
import com.yz.model.stdService.*;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.redis.RedisService;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.JsonUtil;
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
 * 学员服务---学员证明申请/审核
 *
 * @author jyt
 */
@Service
public class StudentCertificateService {
    private static Logger log = LoggerFactory.getLogger(StudentCertificateService.class);

    @Autowired
    private StudentCertificateMapper studentCertificateMapper;

    @Autowired
    private BdStdEnrollMapper stdMapper;
    
    @Autowired
    private UsInfoMapper usInfoMapper;

    @Autowired
    private BdCheckRecordService checkRecordService;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findAllList(StudentCertificateQuery query) {
        BaseUser user = null;
        if(query.getQueryType().equals("zs")){
            user = getUserZS();
        }else {
            user = getUser();
            if (user.getJtList().contains("GKXJ")) {
                query.setRecruitType("2");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("CJXJ")) {
                query.setRecruitType("1");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
                query.setRecruitType("");
                user.setUserLevel("1");
            }
        }
        List<StudentCertificateInfo> list = studentCertificateMapper.findAllList(query, user);
        return new IPageInfo<>((Page) list);
    }

    public StudentCertificateInfo getCertificateInfoById(String certId) {
        StudentCertificateInfo studentCertificateInfo = studentCertificateMapper.getCertificate(certId);
        if (studentCertificateInfo != null) {
            if (studentCertificateInfo.getCheck().get(0).getReason() != null) {
                studentCertificateInfo.setReason(studentCertificateInfo.getCheck().get(0).getReason());
            }
            if (studentCertificateInfo.getCheck().get(1).getReason() != null) {
                studentCertificateInfo.setReason(studentCertificateInfo.getCheck().get(1).getReason());
            }
            return studentCertificateInfo;
        }
        return null;
    }

    public int updateExpressNo(String certId, String remark) {
        return studentCertificateMapper.updateExpressNo(certId, remark);
    }

    public Map<String, Object> getStudyInfo(String learnId) {
        return studentCertificateMapper.getStudyInfo(learnId);
    }

    @Transactional
    public void check(String checkStatus, String reason, String certId, String checkOrder, String applyType, String learnId) {
        String title = "";
        if (checkOrder.equals("1") && checkStatus.equals("2")) {
            studentCertificateMapper.updateCheckOrder(certId, "2");
            title = getApplyName(applyType) + "审批通知";
            SendWeChat(learnId, title, reason + "\n处理时间:" + DateUtil.getNow("yyyy-MM-dd HH:mm:ss"), getApplyName(applyType));
        }
        if (checkOrder.equals("2") && checkStatus.equals("2")) {
            title = getApplyName(applyType) + "审批通知";
            SendWeChat(learnId, title, reason + "\n处理时间:" + DateUtil.getNow("yyyy-MM-dd HH:mm:ss"), getApplyName(applyType));
        }
        if (checkStatus.equals("3")) {
            title = getApplyName(applyType) + "驳回通知";
            SendWeChat(learnId, title, reason + "\n处理时间:" + DateUtil.getNow("yyyy-MM-dd HH:mm:ss"), getApplyName(applyType));
        }
        BaseUser user = SessionUtil.getUser();
        BdCheckRecord bcr = new BdCheckRecord();
        bcr.setMappingId(certId);
        bcr.setCheckStatus(checkStatus);
        bcr.setCheckOrder(checkOrder);
        bcr.setReason(reason);
        bcr.setEmpId(user.getEmpId());
        bcr.setUpdateUser(user.getRealName());
        bcr.setUpdateUserId(user.getUserId());
        checkRecordService.updateBdCheckRecord(bcr);
    }

    @SuppressWarnings("unchecked")
    public void importCertificate(MultipartFile importFile) {
        //对导入工具进行字段填充
        IExcelConfig<StudentCertificateExcel> testExcelCofing = new IExcelConfig<StudentCertificateExcel>();
        testExcelCofing.setSheetName("index").setType(StudentCertificateExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("ID", "certId"))
                .addTitle(new ExcelUtil.IExcelTitle("快递单号", "expressNo"));

        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<StudentCertificateExcel> list = ExcelUtil.importWorkbook(importFile.getInputStream(), testExcelCofing,
                    importFile.getOriginalFilename());
            // 遍历插入
            for (StudentCertificateExcel studentCertificateExcel : list) {
                if (!StringUtil.hasValue(studentCertificateExcel.getCertId())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！ID不能为空");
                }
                if (StringUtil.hasValue(studentCertificateExcel.getExpressNo())) {
                    studentCertificateExcel.setIsSend("1");
                } else {
                    studentCertificateExcel.setIsSend("0");
                }
                index++;
            }
            List<Map<String, Object>> resultList = studentCertificateMapper.getNonExistsStudent(list);
            if (resultList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("学员证明申请进以下ID不存在：<br/>");
                for (Map<String, Object> map : resultList) {
                    sb.append(map.get("cert_id") + "<br/>");
                }
                throw new IllegalArgumentException(sb.toString());
            }
            BaseUser user = SessionUtil.getUser();
            studentCertificateMapper.updateExpressNoByExcel(list, user);
        } catch (IOException e) {
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
    }

    @SuppressWarnings("unchecked")
    public void exportCertificateInfo(StudentCertificateQuery query, HttpServletResponse response) {
        BaseUser user = null;
        // 对导出工具进行字段填充
        IExcelConfig<StudentCertificateInfo> testExcelCofing = new IExcelConfig<StudentCertificateInfo>();
        if(query.getQueryType().equals("zs")){
            testExcelCofing.setSheetName("index").setType(StudentCertificateInfo.class)
                    .addTitle(new ExcelUtil.IExcelTitle("ID", "certId"))
                    .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                    .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                    .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                    .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                    .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                    .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                    .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                    .addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruit"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请类型", "applyType"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请用途", "applyPurpose"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请时间", "createTime"))
                    .addTitle(new ExcelUtil.IExcelTitle("是否寄出", "isSend"))
                    .addTitle(new ExcelUtil.IExcelTitle("快递单号", "expressNo"))
                    .addTitle(new ExcelUtil.IExcelTitle("领取方式", "receiveType"))
                    .addTitle(new ExcelUtil.IExcelTitle("审核状态", "checkStatus"));

            user = getUserZS();
        }else {
            testExcelCofing.setSheetName("index").setType(StudentCertificateInfo.class)
                    .addTitle(new ExcelUtil.IExcelTitle("ID", "certId"))
                    .addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
                    .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                    .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                    .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                    .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                    .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                    .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                    .addTitle(new ExcelUtil.IExcelTitle("班主任", "tutor"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请类型", "applyType"))
                    .addTitle(new ExcelUtil.IExcelTitle("材料名称", "materialName"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请用途", "applyPurpose"))
                    .addTitle(new ExcelUtil.IExcelTitle("申请时间", "createTime"))
                    .addTitle(new ExcelUtil.IExcelTitle("是否寄出", "isSend"))
                    .addTitle(new ExcelUtil.IExcelTitle("快递单号", "expressNo"))
                    .addTitle(new ExcelUtil.IExcelTitle("领取方式", "receiveType"))
                    .addTitle(new ExcelUtil.IExcelTitle("审核状态", "checkStatus"));

            user = getUser();
            if (user.getJtList().contains("GKXJ")) {
                query.setRecruitType("2");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("CJXJ")) {
                query.setRecruitType("1");
                user.setUserLevel("1");
            }
            if (user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
                query.setRecruitType("");
                user.setUserLevel("1");
            }
        }

        List<StudentCertificateInfo> list = studentCertificateMapper.findAllList(query, user);

        for (StudentCertificateInfo studentCertificateInfo : list) {

            if (StringUtil.hasValue(studentCertificateInfo.getPfsnLevel()) && studentCertificateInfo.getPfsnLevel().equals("1")) {
                studentCertificateInfo.setPfsnLevel("1>专科升本科类");
            } else if (StringUtil.hasValue(studentCertificateInfo.getPfsnLevel()) && studentCertificateInfo.getPfsnLevel().equals("5")) {
                studentCertificateInfo.setPfsnLevel("5>高中起点高职高专");
            } else {
                studentCertificateInfo.setPfsnLevel("");
            }

            if (StringUtil.hasValue(studentCertificateInfo.getIsSend())) {
                if (studentCertificateInfo.getIsSend().equals("1")) {
                    studentCertificateInfo.setIsSend("是");
                } else {
                    studentCertificateInfo.setIsSend("否");
                }
            }

            if (StringUtil.hasValue(studentCertificateInfo.getReceiveType())) {
                if (studentCertificateInfo.getReceiveType().equals("1")) {
                    studentCertificateInfo.setReceiveType("邮寄");
                } else {
                    studentCertificateInfo.setReceiveType("自取");
                }
            }

            if (StringUtil.hasValue(studentCertificateInfo.getApplyType())) {
                studentCertificateInfo.setApplyType(getApplyName(studentCertificateInfo.getApplyType()));
            }

            if (StringUtil.hasValue(studentCertificateInfo.getCheckStatus()) && StringUtil.hasValue(studentCertificateInfo.getCheckOrder())) {
                if (studentCertificateInfo.getCheckStatus().equals("1") && studentCertificateInfo.getCheckOrder().equals("1")) {
                    studentCertificateInfo.setCheckStatus("审核中");
                } else if (studentCertificateInfo.getCheckStatus().equals("1") && studentCertificateInfo.getCheckOrder().equals("2")) {
                    studentCertificateInfo.setCheckStatus("已初审");
                } else if (studentCertificateInfo.getCheckStatus().equals("2") && studentCertificateInfo.getCheckOrder().equals("2")) {
                    studentCertificateInfo.setCheckStatus("已受理");
                } else {
                    studentCertificateInfo.setCheckStatus("已驳回");
                }
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuCertificate.xls");
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

    private BaseUser getUser() {
        BaseUser user = SessionUtil.getUser();
        if (user.getJtList().contains("FDY")) {
            user.setUserLevel("6");
        }
        if (user.getJtList().contains("400") || user.getJtList().contains("BMZR")) {
            user.setUserLevel("1");
        }
        if (user.getJtList().size() == 0) {
            user.setUserLevel("1");
        }
        //国开学籍且是辅导员
        if (user.getJtList().contains("GKXJ")  && user.getJtList().contains("FDY")) {
            user.setUserLevel("7");
        }
        //成教学籍且辅导员
        if (user.getJtList().contains("CJXJ")  && user.getJtList().contains("FDY")) {
            user.setUserLevel("8");
        }
        return user;
    }

    private BaseUser getUserZS() {
        BaseUser user = SessionUtil.getUser();
        if(user.getUserLevel().equals("4")){
            user.setUserLevel("5");
        }
        if(user.getUserLevel().equals("8")){
            user.setUserLevel("5");
        }
        //副校长助理,校区助理
        if (user.getJtList().contains("FXZZL") || user.getJtList().contains("XQZL")) {
            user.setUserLevel("1");
        }
        return user;
    }

    /**
     * 推送消息
     *
     * @param learnId
     * @param msg
     */
    private void SendWeChat(String learnId, String title, String msg, String msgName) {
        GwReceiver std = stdMapper.selectStdInfoByLearnId(learnId);
        String openId = usInfoMapper.selectUserOpenId(std.getUserId());
        // 微信推送消息
        WechatMsgVo wechatVo = new WechatMsgVo();
        wechatVo.setTouser(openId);
        wechatVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
        wechatVo.addData("title", title);
        wechatVo.addData("msgName", msgName);
        wechatVo.addData("code", "YZ");
        wechatVo.addData("content", msg);
        wechatVo.setExt1("");
        wechatVo.setIfUseTemplateUlr(false);

        RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(wechatVo));
    }

    public String getApplyName(String applyType) {
        String applyName = "";
        switch (applyType) {
            case "1":
                applyName = "在读证明";
                break;
            case "2":
                applyName = "课时证明";
                break;
            case "3":
                applyName = "发票";
                break;
            case "4":
                applyName = "收据";
                break;
            case "5":
                applyName = "其它";
                break;
            case "6":
                applyName = "报读证明";
                break;
        }
        return applyName;
    }
}
