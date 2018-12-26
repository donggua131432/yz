package com.yz.controller.gk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.gk.BdStudentGkUnifiedExamInfo;
import com.yz.model.gk.BdStudentGkUnifiedExamQuery;
import com.yz.service.gk.BdStudentGKUnifiedExamService;
import com.yz.util.Assert;

/**
 *  国开统考设置
 * @author lx
 * @date 2018年6月6日 上午10:06:40
 */
@Controller
@RequestMapping("/studentGKUnifiedExam")
public class BdStudentGKUnifiedExamController {
	
	@Autowired
	private BdStudentGKUnifiedExamService gkUnifiedExamService;

	/**
	 * 调整到页面
	 * @param request
	 * @return
	 */
    @RequestMapping("/tolist")
    @Rule("gkUnifiedExam:query")
    public String showList(HttpServletRequest request) {
        return "gk/exam/gk_unified_exam_list";
    }

    /**
     * 查询国开统考设置信息
     * @param start
     * @param length
     * @param query
     * @return
     */
    @RequestMapping("/findStdGKUnifiedExam")
	@Rule("gkUnifiedExam:query")
	@ResponseBody
	public Object findStdGKUnifiedExamList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,BdStudentGkUnifiedExamQuery query) {
		PageHelper.offsetPage(start, length);
		return gkUnifiedExamService.findStdGKUnifiedExamList(query);
	}
    
    /**
     * 跳转到编辑
     * @param exType
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/edit")
	@Token(action = Flag.Save, groupId = "gkUnifiedExamEdit")
	@Rule("gkUnifiedExam:insert")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,Model model) {
		BdStudentGkUnifiedExamInfo examInfo = new BdStudentGkUnifiedExamInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String id = RequestUtil.getString("id");
			Assert.hasText(id, "参数名称不能为空");
			examInfo = gkUnifiedExamService.getGkUnifiedExam(id);	
		}
		model.addAttribute("exType", exType);
		model.addAttribute("examInfo", examInfo);
		return "gk/exam/gk_unified_exam_edit";
	}
    
    /**
     * 新增和修改
     * @param request
     * @param exType
     * @param examInfo
     * @param attachment
     * @return
     */
    @RequestMapping("/insertStdGKUnifiedExamInfo")
    @Token(action = Flag.Remove, groupId = "gkUnifiedExamEdit")
    @Rule("gkUnifiedExam:insert")
	@ResponseBody
	@Log
	public Object insertStdGKUnifiedExamInfo(HttpServletRequest request,@RequestParam(name = "exType", required = true) String exType, BdStudentGkUnifiedExamInfo examInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		
		if ("UPDATE".equalsIgnoreCase(deal)) {
			examInfo.setUpdateUserId(user.getUserId());
			examInfo.setUpdateUserName(user.getRealName());
			
			gkUnifiedExamService.updateGkUnifiedExam(examInfo);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			examInfo.setCreateUserId(user.getUserId());
			examInfo.setCreateUserName(user.getRealName());
			
			gkUnifiedExamService.insertStdGKUnifiedExamInfo(examInfo);
		}

		return "success";
	}
    
    /**
     * 禁用和启用
     * @param request
     * @param id
     * @param ifShow
     * @return
     */
    @RequestMapping("/startOrStopGkUnifiedExam")
    @Rule("gkUnifiedExam:insert")
	@ResponseBody
	public Object startOrStopGkUnifiedExam(HttpServletRequest request,@RequestParam(name = "id", required = true) String id, 
			@RequestParam(name = "ifShow", required = true) String ifShow) {
    	gkUnifiedExamService.startOrStopGkUnifiedExam(id, ifShow);
		return "success";
	}
    
    /**
     * 删除
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/delGkUnifiedExam")
    @Rule("gkUnifiedExam:insert")
	@ResponseBody
	public Object delGkUnifiedExam(HttpServletRequest request,@RequestParam(name = "id", required = true) String id) {
    	gkUnifiedExamService.delGkUnifiedExam(id);
		return "success";
	}
    
}
