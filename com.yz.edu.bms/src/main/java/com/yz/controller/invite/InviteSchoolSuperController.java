package com.yz.controller.invite;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.service.invite.InviteStayFansService;
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

@RequestMapping("/invite_schoolSuper")
@Controller
public class InviteSchoolSuperController {

    @Autowired
    InviteStayFansService inviteStayFansService;

    @RequestMapping("/toList")
    @Rule("invite_schoolSuper")
    public String toList() {
        return "invite/user/invite_schoolsuper_list";
    }

    /**
     * 获取校监待分配信息集合
     * @param queryInfo
     * @return
     */
    @RequestMapping("/getList")
    @Rule("invite_schoolSuper")
    @ResponseBody
    public Object getList(InviteUserQuery queryInfo, String userType) {
        return inviteStayFansService.getList(queryInfo, userType);
    }
}
