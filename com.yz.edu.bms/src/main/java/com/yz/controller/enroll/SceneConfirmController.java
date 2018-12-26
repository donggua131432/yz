package com.yz.controller.enroll;

import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.enroll.BdSceneConfirmQuery;
import com.yz.service.enroll.BdSceneConfirmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 学员现场确认
 * @作者: DuKai
 * @创建时间: 2017/10/17 15:59
 * @版本号: V1.0
 */
@Controller
@RequestMapping("/sceneConfirm")
public class SceneConfirmController {
    @Autowired
    BdSceneConfirmServiceImpl bdSceneConfirmService;

    @RequestMapping("/toSceneConfirmList")
    @Rule("scene_check")
    public String toListPage() {
        return "enroll/testconfirm/sceneConfirm_list";
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping("/findSceneConfirmStd")
    @Rule("scene_check:query")
    @ResponseBody
    public Object findSceneConfirmStd(BdSceneConfirmQuery confirmQuery) {
        IPageInfo result = bdSceneConfirmService.querySceneConfirmStdPage(confirmQuery);
        return result;
    }


    @RequestMapping("/toUpdateSceneConfirmStd")
    @Rule("scene_check:query")
    public String toUpdateSceneConfirmStd(Model model, @RequestParam(value = "stdId") String stdId, @RequestParam(value="learnId") String learnId) {
        Map<String, Object> resultMap = bdSceneConfirmService.querySceneConfirmStd(stdId,learnId);
        model.addAttribute("resultMap",resultMap);
        return "enroll/testconfirm/sceneConfirm-edit";
    }


    @RequestMapping("/updateSceneConfirmStd")
    @ResponseBody
    @Rule("scene_check:update")
    public Object updateSceneConfirmStd(HttpServletRequest request) {
        String stdId = request.getParameter("stdId");
        String learnId = request.getParameter("learnId");
        String followRecord = request.getParameter("followRecord");

        Map<String, Object> map = bdSceneConfirmService.querySceneConfirm(stdId, learnId);

        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("stdId",stdId);
        mapParam.put("learnId",learnId);
        mapParam.put("followRecord",followRecord);
        if(Integer.parseInt(map.get("countRow").toString())>0){
            bdSceneConfirmService.updateSceneConfirm(mapParam);
        }else{
            bdSceneConfirmService.addSceneConfirm(mapParam);
        }

        return null;
    }
}
