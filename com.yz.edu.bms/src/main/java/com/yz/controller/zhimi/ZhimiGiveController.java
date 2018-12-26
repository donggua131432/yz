package com.yz.controller.zhimi;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.zhimi.ZhimiGive;
import com.yz.service.zhimi.ZhimiGiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @描述: 手动赠送智米
 * @作者: DuKai
 * @创建时间: 2018/1/23 11:41
 * @版本号: V1.0
 */
@Controller
@RequestMapping("/zhimi_give")
public class ZhimiGiveController {

    @Autowired
    private ZhimiGiveService zhimiGiveService;

    @RequestMapping("/toList")
    @Rule("zhimi_give")
    public String toList() {
        return "zhimi/give/give-list";
    }

    @RequestMapping("/list")
    @ResponseBody
    @Rule("zhimi_give:query")
    public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
                       @RequestParam(name = "length", defaultValue = "10") int length, ZhimiGive zhimiGive) {
        return zhimiGiveService.queryGiveZhimiByPage(start, length, zhimiGive);
    }

    @RequestMapping("/toView")
    @Rule("zhimi_give:query")
    public Object getZhimiGiveInfo(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("zhimiGive", zhimiGiveService.getZhimiGiveInfo(id));
        return "zhimi/give/give-view";
    }


    @RequestMapping("/getUserInfo")
    @ResponseBody
    public Object quitEmpList(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
                              @RequestParam(value = "page", defaultValue = "1")int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, String>> resultList = zhimiGiveService.findKeyValueUser(sName);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }



    @RequestMapping("/toAdd")
    @Token(action = Flag.Save, groupId = "zhimi_give:insert")
    @Rule("zhimi_give:insert")
    public Object toAdd() {
        return "zhimi/give/give-add";
    }


    @RequestMapping("/add")
    @ResponseBody
    @Log
    @Rule("zhimi_give:insert")
    @Token(action = Flag.Remove, groupId = "zhimi_give:insert")
    public Object add(ZhimiGive zhimiGive) {
        BaseUser baseUser = SessionUtil.getUser();
        zhimiGive.setReasonStatus("1");
        zhimiGive.setSubmitUserId(baseUser.getUserId());
        zhimiGive.setSubmitUserName(baseUser.getUsername());
        zhimiGiveService.insertZhimiGive(zhimiGive);
        return "success";
    }




    @RequestMapping("/deleteGiveRecords")
    @ResponseBody
    @Log
    @Rule("zhimi_give:delete")
    public Object deleteFeeItems(@RequestParam(name = "ids[]", required = true) String[] ids) {
        zhimiGiveService.deleteGiveRecords(ids);
        return "success";
    }
}
