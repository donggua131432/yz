package com.yz.controller.invite;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.edu.paging.bean.IPageInfo;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.service.common.BaseInfoService;
import com.yz.service.invite.InviteStayFansService;

/**
 * @描述: 管理员待分配粉丝
 * @作者: DuKai
 * @创建时间: 2017/12/5 18:13
 * @版本号: V1.0
 */
@RequestMapping("/invite_admin")
@Controller
public class InviteAdminController {

    @Autowired
    InviteStayFansService inviteStayFansService;

    @Autowired
    private BaseInfoService baseInfoService;

    @RequestMapping("/toList")
    @Rule("invite_admin")
    public String toList() {
        return "invite/user/invite_admin_list";
    }

    /**
     * 获取校监待分配信息集合
     * @param queryInfo
     * @return
     */
    @RequestMapping("/getList")
    @Rule("invite_admin")
    @ResponseBody
    public Object getList(InviteUserQuery queryInfo, String userType) {
        return inviteStayFansService.getList(queryInfo, userType);
    }


    /**
     * 获取校监信息
     * @param sName
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/schoolSuperKeyValue")
    @ResponseBody
    public Object schoolSuperKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
                              @RequestParam(value = "page", defaultValue = "1")int page) {
        PageHelper.startPage(page, rows);
        List<Map<String, String>> resultList = baseInfoService.schoolSuperKeyValue(sName);
        if (null == resultList) {
            return "不存在!";
        }
        return new IPageInfo((Page) resultList);
    }
}
