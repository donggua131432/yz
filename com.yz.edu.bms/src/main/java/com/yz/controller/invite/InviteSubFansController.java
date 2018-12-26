package com.yz.controller.invite;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.service.invite.InviteFansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @描述: 校监待分配粉丝
 * @作者: DuKai
 * @创建时间: 2017/12/4 16:19
 * @版本号: V1.0
 */

@RequestMapping("/invite_subFans")
@Controller
public class InviteSubFansController {

    @Autowired
    InviteFansService inviteFansService;

    @RequestMapping("/toList")
    @Rule("invite_subFans")
    public String toList() {
        return "invite/fans/fans_subfans_list";
    }

    /**
     * 获取我的下属粉丝集合信息
     * @param queryInfo
     * @return
     */
    @RequestMapping("/getSubFansList")
    @Rule("invite_subFans")
    @ResponseBody
    public Object getSubFansList(InviteUserQuery queryInfo) {
        return inviteFansService.getSubFansList(queryInfo);
    }
}
