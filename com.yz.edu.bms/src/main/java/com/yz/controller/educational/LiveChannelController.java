package com.yz.controller.educational;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.baseinfo.BdCourse;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdPlaceYear;
import com.yz.model.educational.LiveChannel;
import com.yz.model.educational.LiveChannelQuery;
import com.yz.service.educational.BdCourseService;
import com.yz.service.educational.LiveChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Controller
@RequestMapping("/live")
public class LiveChannelController {

    @Autowired
    LiveChannelService liveChannelService;

    @Autowired
    BdCourseService bdCourseService;

    @RequestMapping("/list")
    @Rule("live:query")
    public String showList(HttpServletRequest request) {
        return "educational/liveChannel/live-channel-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(HttpServletRequest request) {
        return "educational/liveChannel/live-channel-add";
    }

    @RequestMapping("/toRecordExport")
    public String toRecordExport(HttpServletRequest request) {
        return "educational/liveChannel/live-channel-record-export";
    }

    @RequestMapping("/toEdit")
    public String toEdit(@RequestParam(name = "lcId") String lcId, Model model) {
        LiveChannel result = liveChannelService.getById(lcId);
        model.addAttribute("liveChannel", result);
        BdCourse course = bdCourseService.findCourseById(result.getCourseId());
        model.addAttribute("course", course.getCourseName());
        return "educational/liveChannel/live-channel-edit";
    }

    @RequestMapping("/toGen")
    public String toGen(HttpServletRequest request) {
        return "educational/liveChannel/live-channel-gen";
    }

    @RequestMapping("/findAllLiveChannel")
    @Rule("live:query")
    @ResponseBody
    public Object findAllLiveChannel(@RequestParam(value = "start", defaultValue = "1") int start,
                                     @RequestParam(value = "length", defaultValue = "10") int length, LiveChannelQuery query) {
        PageHelper.offsetPage(start, length).setRmGroup(false);
        List<Map<String, Object>> resultList = liveChannelService.findAllLiveChannel(query);
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/findCourseByYear")
    @ResponseBody
    public Object findCourseByYear(String sName, String year, @RequestParam(value = "rows", defaultValue = "10") int rows,
                                   @RequestParam(value = "page", defaultValue = "1") int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, Object>> resultList = liveChannelService.findCourseByYear(sName, year);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/add")
    @ResponseBody
    @Rule("live:add")
    public Object add(LiveChannel liveChannel) {
        liveChannelService.insert(liveChannel);
        return "SUCCESS";
    }

    @RequestMapping("/edit")
    @ResponseBody
    @Rule("live:edit")
    public Object edit(LiveChannel liveChannel) {
        liveChannelService.edit(liveChannel);
        return "SUCCESS";
    }

    @RequestMapping("/gen")
    @ResponseBody
    @Rule("live:gen")
    public Object gen(String year) {
        liveChannelService.gen(year);
        return "SUCCESS";
    }

    @RequestMapping("/deletes")
    @Rule("live:delete")
    @ResponseBody
    public Object deletes(@RequestParam(name = "lcIds[]") String[] lcIds) {
        liveChannelService.deletes(lcIds);
        return "SUCCESS";
    }

    @RequestMapping("/changePassword")
    @Rule("live:changePassword")
    @ResponseBody
    public Object deletes(@RequestParam(name = "lcId") String lcId, @RequestParam(name = "channelPassword") String password) {
        liveChannelService.changePassword(lcId, password);
        return "SUCCESS";
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(){
        String channelId = "190350";
        String date = "2018-04-26";
        liveChannelService.getUserViewInfo(channelId, date);
    }

    @RequestMapping("/findChannel")
    @ResponseBody
    public Object findChannel(String sName,  @RequestParam(value = "rows", defaultValue = "10") int rows,
                                   @RequestParam(value = "page", defaultValue = "1") int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, Object>> resultList = liveChannelService.findChannel(sName);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/findLiveDate")
    @ResponseBody
    public Object findLiveDate(String channelId,  @RequestParam(value = "rows", defaultValue = "10") int rows,
                              @RequestParam(value = "page", defaultValue = "1") int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, Object>> resultList = liveChannelService.findLiveDate(channelId);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }

    @RequestMapping("/recordExport")
    @Rule("live:recordExport")
    public void recordExport(String channelId,String liveDate,HttpServletResponse response){
        liveChannelService.recordExport(channelId,liveDate,response);
    }
}
