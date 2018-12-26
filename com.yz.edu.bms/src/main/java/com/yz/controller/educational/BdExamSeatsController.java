package com.yz.controller.educational;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.BdExamYearProfessionMapper;
import com.yz.model.baseinfo.BdPlanTextbookKey;
import com.yz.model.baseinfo.BdTeachPlan;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.*;
import com.yz.service.educational.BdExamRoomAssignService;
import com.yz.service.educational.BdExamRoomSeatsService;
import com.yz.service.educational.BdExamRoomService;
import com.yz.service.educational.BdExamYearProfessionService;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Controller
@RequestMapping("/examRoomSeats")
public class BdExamSeatsController {
    @Autowired
    BdExamRoomSeatsService examRoomSeatsService;

    @Autowired
    BdExamYearProfessionService examYearProfessionService;

    @RequestMapping("/list")
    @Rule("examRoomSeats")
    public String showList(HttpServletRequest request) {
        return "educational/examRoom/room-seats-list";
    }

    @RequestMapping("/professionImport")
    public String professionImport(HttpServletRequest request) {
        return "educational/examRoom/room-year-profession-import";
    }

    @RequestMapping("/findAllExamRoomSeats")
    @Rule("examRoomSeats:findAll")
    @ResponseBody
    public Object findAllExamRoomSeats(@RequestParam(value = "start", defaultValue = "1") int start,
                                       @RequestParam(value = "length", defaultValue = "10") int length, ExamRoomSeatsQuery query) {
        PageHelper.offsetPage(start, length);
        List<Map<String, Object>> resultList = examRoomSeatsService.findAllExamRoomSeats(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/uploadExamProfession")
    @Rule("examRoomSeats:uploadExamProfession")
    @ResponseBody
    public Object uploadTextBook(
            @RequestParam(value = "excelExamProfession", required = false) MultipartFile excelExamProfession) {
        // 对导入工具进行字段填充
        ExcelUtil.IExcelConfig<BdExamYearProfession> testExcelCofing = new ExcelUtil.IExcelConfig<BdExamYearProfession>();
        testExcelCofing.setSheetName("index").setType(BdExamYearProfession.class)
                .addTitle(new ExcelUtil.IExcelTitle("考试年度", "eyId"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsId"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnId"))
                .addTitle(new ExcelUtil.IExcelTitle("专业代号编码", "eypCode"));
        // 行数记录
        int index = 2;
        try {
            // 对文件进行分析转对象
            List<BdExamYearProfession> list = ExcelUtil.importWorkbook(excelExamProfession.getInputStream(), testExcelCofing,
                    excelExamProfession.getOriginalFilename());
            // 遍历插入
            for (BdExamYearProfession profession : list) {
                if (!StringUtil.hasValue(profession.getEyId())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考试年度不能为空");
                }
                if (!StringUtil.hasValue(profession.getGrade())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
                }
                if (!StringUtil.hasValue(profession.getUnvsId())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！院校不能为空");
                }
                if (!StringUtil.hasValue(profession.getPfsnLevel())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！层次不能为空");
                }
                if (!StringUtil.hasValue(profession.getPfsnId())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业名称不能为空");
                }
                if (!StringUtil.hasValue(profession.getEypCode())) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！专业代号编码不能为空");
                }
                String eyId = examYearProfessionService.selectExamYearId(profession.getEyId());
                if (eyId == null) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！考试年度不存在或已停用");
                }
                Map<String, String> professionMap = examYearProfessionService.selectProfession(profession.getGrade().trim(), profession.getUnvsId().trim(), profession.getPfsnLevel().trim(), profession.getPfsnId().trim());
                if (professionMap == null) {
                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！该院校专业信息不存在");
                }
                BdExamYearProfession examYearProfession = examYearProfessionService.getProfession(eyId, professionMap.get("grade"), professionMap.get("unvs_id"), professionMap.get("pfsn_level"), professionMap.get("pfsn_id"));
                if (examYearProfession != null) {
                    examYearProfessionService.updateCode(examYearProfession.getEypId(), profession.getEypCode().trim());
                } else {
                    examYearProfession = new BdExamYearProfession();
                    examYearProfession.setEyId(eyId);
                    examYearProfession.setGrade(professionMap.get("grade"));
                    examYearProfession.setUnvsId(professionMap.get("unvs_id"));
                    examYearProfession.setPfsnLevel(professionMap.get("pfsn_level"));
                    examYearProfession.setPfsnId(professionMap.get("pfsn_id"));
                    examYearProfession.setEypCode(profession.getEypCode());
                    examYearProfessionService.insert(examYearProfession);
                }
                index++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
        }
        return "SUCCESS";
    }

    @RequestMapping("/getExamSeatsInfo")
    @Rule("examRoomSeats:getExamSeatsInfo")
    @ResponseBody
    public Object getExamSeatsInfo(BdExamSeatsInfoQuery query) {
        return examRoomSeatsService.getExamSeatsInfo(query);
    }

    @RequestMapping("/generateExamSeats")
    @Rule("examRoomSeats:generateExamSeats")
    @ResponseBody
    public Object generateExamSeats(BdExamSeatsInfoQuery query) {
        examRoomSeatsService.generateExamSeats(query);
        return "SUCCESS";
    }

    @RequestMapping("/toGenerateExamSeats")
    @Rule("examRoomSeats:generateExamSeats")
    public String toGenerateExamSeats(HttpServletRequest request) {
        return "educational/examRoom/room-seats-generate";
    }

    @RequestMapping("/toGetExamSeatsInfo")
    @Rule("examRoomSeats:getExamSeatsInfo")
    public String toGetExamSeatsInfo(HttpServletRequest request) {
        return "educational/examRoom/room-seats-pdf";
    }

    @RequestMapping("/findPlaceInfo")
    @ResponseBody
    public Object findPlaceInfo(String eyId, String epId, String startTime, String endTime) {
        return examRoomSeatsService.findPlaceInfo(eyId, epId, startTime, endTime);
    }

    @RequestMapping("/seatsExport")
    @Rule("examRoomSeats:seatsExport")
    @ResponseBody
    public void seatsExport(BdExamSeatsInfoQuery query, HttpServletResponse response) {
        examRoomSeatsService.seatsExport(query,response);
    }


}
