package com.yz.service.educational;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.*;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.*;
import com.yz.service.educational.doc.TemplateDoc;
import com.yz.service.educational.excel.ExamRoomSeatsExcel;
import com.yz.service.pubquery.TutorshipBookSendService;
import com.yz.util.DateUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
@Transactional
public class BdExamRoomSeatsService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    BdExamRoomSeatsMapper examRoomSeatsMapper;

    @Autowired
    BdExamYearProfessionMapper examYearProfessionMapper;

    @Autowired
    BdExamRoomAssignMapper examRoomAssignMapper;

    /**
     * 考场座位安排查询
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> findAllExamRoomSeats(ExamRoomSeatsQuery query) {
        List<Map<String, Object>> result = examRoomSeatsMapper.findAllExamRoomSeats(query);
        return result;
    }

    /**
     * 获取考场座位表信息
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> getExamSeatsInfo(BdExamSeatsInfoQuery query) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        BdPlaceYear placeYear = examRoomAssignMapper.getBdPlaceYear(query.getEyId(), query.getEpId(), query.getStartTime(), query.getEndTime());
        if (placeYear != null) {
            result = examRoomSeatsMapper.getExamSeatsInfo(placeYear.getPyId(), query.getPlaceId());
        }
        return result;
    }

    /**
     * 生成座位信息
     *
     * @param query
     */
    public void generateExamSeats(BdExamSeatsInfoQuery query) {
        BaseUser user = SessionUtil.getUser();
        ExamRoomAssignQuery assignQuery = new ExamRoomAssignQuery();
        if (query.getEyId() != null) {
            assignQuery.setEyId(query.getEyId());
            if (query.getEpId() != null) {
                assignQuery.setPlaceId(query.getEpId());
                if (query.getStartTime() != null && query.getEndTime() != null) {
                    assignQuery.setStartTime(query.getStartTime());
                    assignQuery.setEndTime(query.getEndTime());
                }
            }
        }
        List<Map<String, String>> list = examRoomAssignMapper.findExamRoomAssignPyId(assignQuery);
        if (list.size() > 0) {
            examRoomSeatsMapper.deleteExamRoomSeats(query.getEyId(), query.getEpId(), query.getStartTime(), query.getEndTime());
        }
        List<BdExamSeats> bdExamSeatslist = new ArrayList<BdExamSeats>();
        for (Map<String, String> map : list) {
            List<Map<String, Object>> maxCountList = examRoomSeatsMapper.getPlaceMaxCount(map.get("pyId").toString());
            List<Map<String, Object>> seatsList = examRoomSeatsMapper.getExamSeatsInfo(map.get("pyId").toString(), "");
            int start = 0;
            for (Map<String, Object> maxCount : maxCountList) {
                int count = Integer.parseInt(maxCount.get("maxCount").toString());
                String placeId = maxCount.get("placeId").toString();
                int num = 1;
                int i = 0;
                for (i = start; i < seatsList.size(); i++) {
                    BdExamSeats examSeats = new BdExamSeats();
                    examSeats.setEsNum(num);
                    examSeats.setPlaceId(placeId);
                    examSeats.setLearnId(seatsList.get(i).get("learnId").toString());
                    examSeats.setPyId(map.get("pyId").toString());
                    examSeats.setCreateUser(user.getRealName());
                    examSeats.setCreateTime(new Date());
                    examSeats.setCreateUserId(user.getUserId());
                    examSeats.setEsId(IDGenerator.generatorId());
                    bdExamSeatslist.add(examSeats);
                    num++;
                    if ((i + 1 - start) == count) {
                        num = 1;
                        start += count;
                        break;
                    }
                }
                if (seatsList.size() == i) {
                    break;
                }
            }
        }
        if (bdExamSeatslist.size() > 0) {
        	
            examRoomSeatsMapper.insertExamRoomSeatList(bdExamSeatslist);
        }
    }

    /**
     * 查询课室
     *
     * @param eyId
     * @param epId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> findPlaceInfo(String eyId, String epId, String startTime, String endTime) {
        return examRoomSeatsMapper.findPlaceInfo(eyId, epId, startTime, endTime);
    }

    /**
     * Excel导出
     * @param query
     */
    public void seatsExport(BdExamSeatsInfoQuery query, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            ExamRoomSeatsExcel examRoomSeatsExcel = new ExamRoomSeatsExcel();
            String fileName =  query.getEyName() + "_" +query.getEpName() + "_" + query.getExamTimeStr() + "_"  + query.getPlaceName() + ".xls";
            String name = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-disposition", "attachment;filename=\"" + name + "\"");
            File file = ResourceUtils.getFile("classpath:static/excel/examRoomSeatTemplate.xls");
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String text = new String(buffer, "UTF-8");
            List<Map<String, Object>> list  =  getExamSeatsInfo(query);
            text = examRoomSeatsExcel.excelRender(list, query, text);
            out = response.getOutputStream();
            out.write(text.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
