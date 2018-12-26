package com.yz.controller.oa;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.oa.OaCampusGroup;
import com.yz.service.oa.OaCampusGroupService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @描述: 校区组管理
 * @作者: DuKai
 * @创建时间: 2018/5/23 11:43
 * @版本号: V1.0
 */
@Controller
@RequestMapping("/campusGroup")
public class CampusGroupController {

    @Autowired
    private OaCampusGroupService oaCampusGroupService;

    @RequestMapping("/toCampusGroupList")
    @Rule("campusGroup:query")
    public String toCampusGroupList() {
        return "oa/campus/campus-group-list";
    }

    @RequestMapping("/campusGroupList")
    @ResponseBody
    @Rule("campusGroup:query")
    public Object campusGroupList(@RequestParam(name = "start", defaultValue = "0") int start,
                             @RequestParam(name = "length", defaultValue = "10") int length,OaCampusGroup oaCampusGroup) {
        return oaCampusGroupService.getCampusGroupList(start, length, oaCampusGroup);
    }


    @RequestMapping("/toEditCampusGroup")
    @Token(action = Token.Flag.Save, groupId = "campusGroupEdit")
    @Rule("campusGroup:insert")
    public Object toEditCampusGroup(@RequestParam(name = "exType") String exType, Model model, HttpServletRequest request){
        OaCampusGroup oaCampusGroup = new OaCampusGroup();
        if ("UPDATE".equals(exType.trim().toUpperCase())) {
            String id = RequestUtil.getString("id");
            Assert.hasText(id, "参数名称不能为空");
            oaCampusGroup = oaCampusGroupService.getOaCampusGroup(id);
        }
        model.addAttribute("exType", exType);
        model.addAttribute("oaCampusGroup", oaCampusGroup);
        return "oa/campus/campus-group-edit";
    }

    @RequestMapping("/editCampusGroup")
    @ResponseBody
    @Token(action = Token.Flag.Remove, groupId = "campusGroupEdit")
    @Rule("campusGroup:insert")
    public Object editCampusGroup(@RequestParam(name = "exType") String exType, OaCampusGroup oaCampusGroup){
        String deal = exType.trim();
        BaseUser user = SessionUtil.getUser();
        if ("UPDATE".equalsIgnoreCase(deal)) {
            oaCampusGroup.setUpdateUserId(user.getUserId());
            oaCampusGroup.setUpdateUser(user.getRealName());
            oaCampusGroupService.updateCampusGroup(oaCampusGroup);
            //清除缓存
        } else if ("ADD".equalsIgnoreCase(exType)) {
            oaCampusGroup.setCreateUserId(user.getUserId());
            oaCampusGroup.setCreateUser(user.getRealName());
            oaCampusGroupService.addCampusGroup(oaCampusGroup);
        }
        return "success";
    }

    @RequestMapping("/deleteCampusGroup")
    @ResponseBody
    @Rule("campusGroup:delete")
    public Object deleteCampusGroup(String id){
        oaCampusGroupService.deleteCampusGroup(id);
        return "success";
    }

    @RequestMapping("/deleteCampusGroupBatch")
    @ResponseBody
    @Rule("campusGroup:delete")
    public Object deleteCampusGroupBatch(@RequestParam(name = "ids[]") String[] ids){
        oaCampusGroupService.deleteCampusGroupBatch(ids);
        return "success";
    }

}
