package com.yz.controller.educational;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.*;
import com.yz.service.educational.BdExamRoomAssignService;
import com.yz.service.educational.BdExamRoomService;
import com.yz.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Controller
@RequestMapping("/examRoomAssign")
public class BdExamRoomAssignController {
    @Autowired
    BdExamRoomAssignService examRoomAssignService;

    @Autowired
    BdExamRoomService examRoomService;

    @RequestMapping("/toAdd")
    @Rule("examRoomAssign:insert")
    public String toAdd(HttpServletRequest request) {
        return "educational/examRoom/room-assign-add";
    }

    @RequestMapping("/add")
    @Rule("examRoomAssign:insert")
    @ResponseBody
    public Object add(ExamRoomAssignInfo assignInfo) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (ExamRoomConfig config : assignInfo.getExamConfig()) {
            String key = config.getPlaceId() + config.getDate() + config.getStartTime() + config.getEndTime();
            String startTime = config.getDate() + " " + config.getStartTime();
            String endTime = config.getDate() + " " + config.getEndTime();
            int count = examRoomAssignService.countExamAssign(assignInfo.getEyId(),
                    config.getPlaceId(), DateUtil.convertDateStrToDate(startTime, "yyyy-MM-dd HH:mm"),
                    DateUtil.convertDateStrToDate(endTime, "yyyy-MM-dd HH:mm"));
            if (count > 0 || map.containsKey(key)) {
                BdExamPlace place = examRoomService.selectExamRoomById(config.getPlaceId());
                throw new Exception("已存在" + place.getEpName() + "考场" + startTime + "-" + config.getEndTime() + "安排时间，不能重复增加");
            } else if (!map.containsKey(key)) {
                map.put(key, config.getPlaceId());
            }
        }
        examRoomAssignService.insert(assignInfo);
        return "SUCCESS";
    }

    @RequestMapping("/toEdit")
    @Rule("examRoomAssign:edit")
    public String toEdit(@RequestParam(name = "pyId") String pyId, Model model) {

        BdPlaceYear result = examRoomAssignService.getById(pyId);
        model.addAttribute("placeYear", result);
        BdExamPlace place = examRoomService.selectExamRoomById(result.getPlaceId());
        model.addAttribute("epName", place.getEpName());
        return "educational/examRoom/room-assign-edit";
    }

    @RequestMapping("/edit")
    @Rule("examRoomAssign:edit")
    @ResponseBody
    public Object edit(ExamRoomConfig config) throws Exception {
        BdPlaceYear result = examRoomAssignService.getById(config.getPyId());
        String startTime = config.getDate() + " " + config.getStartTime();
        String endTime = config.getDate() + " " + config.getEndTime();
        if (!result.getPlaceId().equals(config.getPlaceId())
                || !startTime.equals(DateUtil.formatDate(result.getStartTime(), "yyyy-MM-dd HH:mm"))
                || !endTime.equals(DateUtil.formatDate(result.getEndTime(), "yyyy-MM-dd HH:mm"))
                ) {
            int count = examRoomAssignService.countExamAssign(config.getEyId(),
                    config.getPlaceId(), DateUtil.convertDateStrToDate(startTime, "yyyy-MM-dd HH:mm"),
                    DateUtil.convertDateStrToDate(endTime, "yyyy-MM-dd HH:mm"));
            if (count > 0) {
                BdExamPlace place = examRoomService.selectExamRoomById(config.getPlaceId());
                throw new Exception("已存在" + place.getEpName() + "考场" + startTime + "-" + config.getEndTime() + "安排时间，不能修改");
            }
        }
        examRoomAssignService.update(config);
        return "SUCCESS";
    }

    @RequestMapping("/list")
    @Rule("examRoomAssign")
    public String showList(HttpServletRequest request) {
        return "educational/examRoom/room-assign-list";
    }

    @RequestMapping("/findAllExamRoomAssign")
    @Rule("examRoomAssign:findAll")
    @ResponseBody
    public Object findAllExamRoomAssign(@RequestParam(value = "start", defaultValue = "1") int start,
                                        @RequestParam(value = "length", defaultValue = "10") int length, ExamRoomAssignQuery query) {
        PageHelper.offsetPage(start, length);
        List<ExamRoomAssign> resultList = examRoomAssignService.findAllExamRoomAssign(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/deletes")
    @Rule("examRoomAssign:deletes")
    @ResponseBody
    public Object deletes(@RequestParam(name = "pyIds[]") String[] pyIds) throws Exception {
        if (examRoomAssignService.countExamAffirm(pyIds) > 0) {
            throw new Exception("考场安排已存在考场确认名单，不能删除");
        }
        examRoomAssignService.deleteExamRoomAssign(pyIds);
        return "SUCCESS";
    }

    @RequestMapping("/updateStatus")
    @Rule("examRoomAssign:updateStatus")
    @ResponseBody
    public Object updateStatus(@RequestParam(name = "pyIds[]") String[] pyIds, @RequestParam(name = "status") String status) {
        examRoomAssignService.updateStatus(pyIds, status);
        return "SUCCESS";
    }

    @RequestMapping("/findAllKeyValue")
    @ResponseBody
    public Object findAllKeyValue(String sName, @RequestParam(value = "rows", defaultValue = "10000") int rows,
                                  @RequestParam(value = "page", defaultValue = "1") int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, String>> resultList = examRoomAssignService.findAllKeyValue(sName);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/findAllKeyValueByEyId")
    @ResponseBody
    public Object findAllKeyValueByEyId(String sName,String eyId, @RequestParam(value = "rows", defaultValue = "10000") int rows,
                                  @RequestParam(value = "page", defaultValue = "1") int page,String provinceCode,String cityCode, String districtCode) {
        PageHelper.startPage(page, rows);
        List<Map<String, String>> resultList = examRoomAssignService.findAllKeyValueByEyId(sName,eyId,provinceCode,cityCode,districtCode);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/findExamTime")
    @ResponseBody
    public Object findExamTime(String eyId,String epId) {
        return examRoomAssignService.findExamTime(eyId,epId);
    }

    @RequestMapping("/assignExport")
    @Rule("examRoomAssign:assignExport")
    public void assignExport(ExamRoomAssignQuery query,HttpServletResponse response){
        examRoomAssignService.assignExport(query,response);
    }
}
