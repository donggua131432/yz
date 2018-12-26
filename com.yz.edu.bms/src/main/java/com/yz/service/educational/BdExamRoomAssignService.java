package com.yz.service.educational;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdExamRoomAssignMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.*;
import com.yz.service.pubquery.TutorshipBookSendService;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
@Transactional
public class BdExamRoomAssignService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    BdExamRoomAssignMapper examRoomAssignMapper;

    /**
     * 考场安排查询
     *
     * @param query
     * @return
     */
    public List<ExamRoomAssign> findAllExamRoomAssign(ExamRoomAssignQuery query) {
        List<ExamRoomAssign> result = examRoomAssignMapper.findAllExamRoomAssign(query);
        return result;
    }

    /**
     * 更新状态
     *
     * @param pyIds
     * @param status
     */
    public void updateStatus(String[] pyIds, String status) {
        examRoomAssignMapper.updateStatus(pyIds, status);
    }

    /**
     * 删除考场安排
     *
     * @param pyIds
     */
    public void deleteExamRoomAssign(String[] pyIds) {
        for (String pyId : pyIds) {
            int classCount = examRoomAssignMapper.selectClassCountByPyId(pyId);
            if (classCount > 0) {
                String pyCode = examRoomAssignMapper.selectPyCodeByPyId(pyId);
                throw new BusinessException("E000112", new String[]{pyCode});
            }
        }
        examRoomAssignMapper.deleteExamRoomAssign(pyIds);
    }

    /**
     * 根据ID获取考场安排
     *
     * @param pyId
     * @return
     */
    public BdPlaceYear getById(String pyId) {
        return examRoomAssignMapper.getById(pyId);
    }

    /**
     * 根据考场编号、考场名称、省、市、区来搜索
     *
     * @param sName
     * @return
     */
    public List<Map<String, String>> findAllKeyValue(String sName) {
        return examRoomAssignMapper.findAllKeyValue(sName);
    }

    /**
     * 更新考场安排
     *
     * @param config
     * @return
     */
    public int update(ExamRoomConfig config) {
        BaseUser user = SessionUtil.getUser();
        BdPlaceYear placeYear = new BdPlaceYear();
        placeYear.setPyId(config.getPyId());
        placeYear.setPlaceId(config.getPlaceId());
        placeYear.setSeats(config.getSeats());
        placeYear.setRemark(config.getRemark());
        placeYear.setUpdateTime(new Date());
        placeYear.setUpdateUser(user.getRealName());
        placeYear.setUpdateUserId(user.getUserId());
        placeYear.setEyId(config.getEyId());
        String startTime = config.getDate() + " " + config.getStartTime();
        String endTime = config.getDate() + " " + config.getEndTime();
        placeYear.setStartTime(DateUtil.convertDateStrToDate(startTime, "yyyy-MM-dd HH:mm"));
        placeYear.setEndTime(DateUtil.convertDateStrToDate(endTime, "yyyy-MM-dd HH:mm"));
        // nyp 2017/12/14 删除课室划分关联
        examRoomAssignMapper.deleteExamClass(config.getPyId());
        return examRoomAssignMapper.update(placeYear);
    }

    /**
     * 插入考场安排
     *
     * @param assignInfo
     * @return
     */
    public int insert(ExamRoomAssignInfo assignInfo) {
        int count = 0;
        for (ExamRoomConfig config : assignInfo.getExamConfig()) {
            BaseUser user = SessionUtil.getUser();
            BdPlaceYear placeYear = new BdPlaceYear();
            placeYear.setPlaceId(config.getPlaceId());
            placeYear.setPyCode(
                    config.getPlaceId() + "-" + examRoomAssignMapper.getMaxExamPlaceNum(config.getPlaceId()));
            placeYear.setSeats(config.getSeats());
            placeYear.setRemark(config.getRemark());
            placeYear.setUpdateTime(new Date());
            placeYear.setUpdateUser(user.getRealName());
            placeYear.setUpdateUserId(user.getUserId());
            placeYear.setEyId(assignInfo.getEyId());
            String startTime = config.getDate() + " " + config.getStartTime();
            String endTime = config.getDate() + " " + config.getEndTime();
            placeYear.setStartTime(DateUtil.convertDateStrToDate(startTime, "yyyy-MM-dd HH:mm"));
            placeYear.setEndTime(DateUtil.convertDateStrToDate(endTime, "yyyy-MM-dd HH:mm"));
            placeYear.setPyId(IDGenerator.generatorId());
            examRoomAssignMapper.insert(placeYear);
            count++;
        }
        return count;
    }

    /**
     * 返回考场编号最大序号
     *
     * @param placeId
     * @return
     */
    public int getMaxExamPlaceNum(String placeId) {
        return examRoomAssignMapper.getMaxExamPlaceNum(placeId);
    }

    /**
     * 获取考场确认数量
     *
     * @param pyIds
     * @return
     */
    public int countExamAffirm(String[] pyIds) {
        return examRoomAssignMapper.countExamAffirm(pyIds);
    }

    /**
     * 根据时间来获取考场安排数量
     *
     * @param placeId
     * @param startTime
     * @param endTime
     * @return
     */
    public int countExamAssign(String eyId, String placeId, Date startTime, Date endTime) {
        return examRoomAssignMapper.countExamAssign(eyId, placeId, startTime, endTime);
    }

    /**
     * 根据年度、考场编号、考场名称、省、市、区来搜索
     *
     * @param sName
     * @return
     */
    public List<Map<String, String>> findAllKeyValueByEyId(String sName, String eyId, String provinceCode, String cityCode, String districtCode) {
        return examRoomAssignMapper.findAllKeyValueByEyId(sName, eyId, provinceCode, cityCode, districtCode);
    }

    /**
     * 查询考场时间
     *
     * @param eyId
     * @param epId
     * @return
     */
    public List<Map<String, String>> findExamTime(String eyId, String epId) {
        return examRoomAssignMapper.findExamTime(eyId, epId);
    }

    /**
     * Excel导出
     *
     * @param query
     * @param response
     */
    public void assignExport(ExamRoomAssignQuery query, HttpServletResponse response) {
        // 对导出工具进行字段填充
        ExcelUtil.IExcelConfig<ExamRoomAssign> testExcelCofing = new ExcelUtil.IExcelConfig<ExamRoomAssign>();
        testExcelCofing.setSheetName("index").setType(ExamRoomAssign.class)
                .addTitle(new ExcelUtil.IExcelTitle("考试年度", "examYear"))
                .addTitle(new ExcelUtil.IExcelTitle("考场名称", "epName"))
                .addTitle(new ExcelUtil.IExcelTitle("考试时间", "examTime"))
                .addTitle(new ExcelUtil.IExcelTitle("考场地址", "address"))
                .addTitle(new ExcelUtil.IExcelTitle("考场容量", "seats"))
                .addTitle(new ExcelUtil.IExcelTitle("已选人数", "checkSeats"))
                .addTitle(new ExcelUtil.IExcelTitle("剩余座位", "restSeats"))
                .addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

        List<ExamRoomAssign> list = examRoomAssignMapper.findAllExamRoomAssign(query);

        for (ExamRoomAssign examRoomAssign : list) {
            examRoomAssign.setExamTime(examRoomAssign.getStartTime().replace("AM", "上午").replace("PM", "下午") + "-" + examRoomAssign.getEndTime());
            examRoomAssign.setCheckSeats(String.valueOf(Integer.parseInt(examRoomAssign.getSeats()) - Integer.parseInt(examRoomAssign.getRestSeats())));
            examRoomAssign.setAddress(examRoomAssign.getProvinceName() + examRoomAssign.getCityName() + examRoomAssign.getDistrictName() + examRoomAssign.getAddress());
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=stuExamAssign.xls");
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
}
