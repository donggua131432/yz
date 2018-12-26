package com.yz.controller.invite;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.service.invite.InviteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @描述: 全部粉丝信息查询
 * @作者: DuKai
 * @创建时间: 2018/5/4 15:02
 * @版本号: V1.0
 */
@Controller
@RequestMapping("/inviteUserSearch")
public class InviteUserSearchController {

    @Autowired
    private InviteUserService inviteUserService;

    /**
     * 全部粉丝列表
     * @return
     */
    @RequestMapping("/toInviteUserList")
    @Rule("inviteUserSearch:query")
    public String toList() {
        return "/invite/user/user_invite_search";
    }

    @RequestMapping("/inviteUserList")
    @Rule("inviteUserSearch:query")
    @ResponseBody
    public Object findAllStudent(InviteUserQuery queryInfo) {
        return inviteUserService.inviteUserSearch(queryInfo);
    }
}
