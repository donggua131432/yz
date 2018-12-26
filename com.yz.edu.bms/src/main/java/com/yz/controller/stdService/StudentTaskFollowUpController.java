package com.yz.controller.stdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentTaskFollowUpQuery;
import com.yz.service.stdService.StudentTaskFollowUpService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 学服任务跟进
 * @author lx
 * @date 2017年8月16日 下午4:45:59
 */
@RequestMapping("/taskfollow")
@Controller
public class StudentTaskFollowUpController {
	
	@Autowired
	private StudentTaskFollowUpService taskFollowUpService;

	@RequestMapping("/toList")
	@Rule("taskfollow:query")
	public String toList(Model model) {
		return "/stdservice/taskfollow/student_taskfollow_list";
	}
	
	@RequestMapping("/getTaskList")
	@Rule("taskfollow:query")
	@ResponseBody
	public Object getStudyingList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentTaskFollowUpQuery query) {
		BaseUser user = SessionUtil.getUser();
		query.setTutorId(user.getEmpId());
		return taskFollowUpService.getStudentTaskFollowUp(start,length,query);
	}
	
	@RequestMapping("/finishTask")
	@Log(needLog = true)
	@ResponseBody
	@Rule("taskfollow:finish")
	public Object finishTask() {

		String taskId = RequestUtil.getString("taskId");
		String learnId = RequestUtil.getString("learnId");
		
		taskFollowUpService.finishTask(taskId,learnId);
	
		return "SUCCESS";
	}
	@RequestMapping("/batchFinish")
	@Rule("taskfollow:batchFinish")
	@ResponseBody
	public Object batchFinish(@RequestParam(name = "taskIds", required = true) String taskIds) {
		
		//转换
		JSONArray jso = JSONArray.fromObject(taskIds);
		if(null != jso && jso.size() >0){
			for(int i =0;i<jso.size();i++){
				JSONObject obj = JSONObject.fromObject(jso.get(i));
				String taskId = obj.getString("taskId");
				String learnId = obj.getString("learnId");
				taskFollowUpService.finishTask(taskId,learnId);
			}
		}
		return "success";
	}
}
