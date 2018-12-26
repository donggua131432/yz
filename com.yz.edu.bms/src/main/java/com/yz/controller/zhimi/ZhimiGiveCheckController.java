package com.yz.controller.zhimi;


import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.zhimi.ZhimiGive;
import com.yz.service.zhimi.ZhimiGiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @描述: 手动赠送智米
 * @作者: DuKai
 * @创建时间: 2018/1/23 11:41
 * @版本号: V1.0
 */
@Controller
@RequestMapping("/zhimi_give_check")
public class ZhimiGiveCheckController {



    @Autowired
    private ZhimiGiveService zhimiGiveService;

    @RequestMapping("/toList")
    @Rule("zhimi_give_check")
    public String toList() {
        return "zhimi/give/give-check-list";
    }

    @RequestMapping("/list")
    @ResponseBody
    @Rule("zhimi_give_check:query")
    public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
                       @RequestParam(name = "length", defaultValue = "10") int length, ZhimiGive zhimiGive) {
        return zhimiGiveService.queryGiveZhimiByPage(start, length, zhimiGive);
    }

    @RequestMapping("/toCheck")
    @Token(action = Flag.Save, groupId = "zhimi_give_check:update")
    @Rule("zhimi_give_check:update")
    public Object getZhimiGiveInfo(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("zhimiGive", zhimiGiveService.getZhimiGiveInfo(id));
        return "zhimi/give/give-check";
    }

    @RequestMapping("/check")
    @ResponseBody
    @Log
    @Rule("zhimi_give_check:update")
    @Token(action = Flag.Remove, groupId = "zhimi_give_check:update")
    public Object check(@RequestParam(name = "id") String id,@RequestParam(name = "reasonStatus") String reasonStatus
            ,@RequestParam(name = "rejectDesc") String rejectDesc) {
        ZhimiGive zhimiGive = zhimiGiveService.getZhimiGiveInfo(id);

        BaseUser baseUser = SessionUtil.getUser();
        zhimiGive.setCheckUserId(baseUser.getUserId());
        zhimiGive.setCheckUserName(baseUser.getUsername());
        zhimiGive.setReasonStatus(reasonStatus);
        zhimiGive.setRejectDesc(rejectDesc);

        zhimiGiveService.checkZhimiGive(zhimiGive);
        if("2".equals(reasonStatus)){
            return "success";
        }else{
            return "reject";
        }

    }

}
