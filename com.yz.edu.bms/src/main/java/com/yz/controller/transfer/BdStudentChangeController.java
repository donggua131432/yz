package com.yz.controller.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yz.model.recruit.BdLearnRules;
import com.yz.model.recruit.RecruitEmployeeInfo;
import com.yz.service.recruit.StudentAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentChange;
import com.yz.model.transfer.BdStudentChangeMap;
import com.yz.model.transfer.BdStudentChangeQuery;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.transfer.BdStudentChangeService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;
import com.yz.util.ValidationUtil;

@Controller
@RequestMapping("/studentChange")
public class BdStudentChangeController {
	@Autowired
	private BdStudentChangeService studentChangeService;

	@Autowired
	private StudentAllService stdAllService;

	@RequestMapping("/list")
	@Rule("studentChange:find")
	public String showList(HttpServletRequest request) {
		return "transfer/change/student-change-list";
	}

	@RequestMapping("/edit")
	@Rule("studentChange:check")
	@Token(action = Flag.Save, groupId = "studentChange:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {

		String[] words = { "old", "prevent", "reason", "updateUser", "stdName", "changeId", "learnId", "createTime" };
		Map<String, Object> map = new HashMap<String, Object>();
		for (String word : words) {
			if (word.equals("prevent") || word.equals("old")) {
				String[] words2 = { "stdId", "stdName", "learnId", "grade", "scholarship", "unvsName", "pfsnName",
						"stdStage", "updateUser", "pfsnLevel", "taName", "taId", "unvsId", "pfsnId" };
				Map<String, Object> map2 = new HashMap<String, Object>();
				for (String word2 : words2) {
					map2.put(word2, null);
				}
				map.put(word, map2);
			} else {
				map.put(word, null);
			}
		}

		RecruitEmployeeInfo recruitEmpInfo = new RecruitEmployeeInfo();
		String newLearnId = "";
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String changeId = RequestUtil.getString("changeId");
			Assert.hasText(changeId, "参数名称不能为空");
			BdStudentChange studentChange = studentChangeService.selectByPrimaryKey(changeId);
			String learnId = studentChange.getLearnId();
			newLearnId = studentChange.getNewLearnId();
			Map<String, String> studentChangeM = studentChangeService.findBdStudentChange(learnId,null, null, null);
			if (null != studentChangeM) {
				studentChangeM.put("stdStage", studentChange.getStdStage());
				map.put("old", studentChangeM);
				map.put("prevent",studentChangeService.findBdStudentChange(newLearnId, null, null, null));
				map.put("reason", studentChange.getReason());
				map.put("updateUser", studentChange.getUpdateUser());
				map.put("createTime", studentChange.getCreateTime());
				map.put("stdName", studentChangeM.get("stdName"));
				map.put("changeId", studentChange.getChangeId());

				recruitEmpInfo = stdAllService.getRecruitEmpInfo(newLearnId);
			}
		}
		model.addAttribute("exType", exType);
		model.addAttribute("studentChange", map);
		String isSuper = "0";
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();

		if (jtList != null) {
			if (jtList.contains("400")) {
				isSuper = "1";
			}
		}

		if (GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
			isSuper = "1";
		}
		//判断当前登录人是否拥有 ：优惠类型修改 权限节点
		boolean ifCanUpdate = false;
		if(null != user.getFuncs() && user.getFuncs().size()>0){
			for(BmsFunc func:user.getFuncs()){
				if(func.getFuncCode().equals("studentModify:typeUpdate")){
					ifCanUpdate = true;
					break;
				}
			}
		}
		model.addAttribute("ifCanUpdate",ifCanUpdate);
		model.addAttribute("isSuper", isSuper);
		model.addAttribute("recruitEmpInfo", recruitEmpInfo);
		model.addAttribute("learnId", newLearnId);
		return "transfer/change/student-change-edit";
	}

	/**
	 * Description: 根据学员id查询出学员报读信息
	 * @param learnId 学业id
	 * @return 返回map
	 */
	@RequestMapping("/findTransferByStdId")
	@ResponseBody
	@Rule("studentChange:find")
	public Object findTransferByStdId(String learnId) {
		Assert.hasText(learnId, "参数名称不能为空");
		return studentChangeService.findTransferByStdId(learnId);
	}

	@RequestMapping("/findStudentGrade")
	@ResponseBody
	@Rule("studentChange:find")
	public Object findStudentGrade(String grade) {
		return studentChangeService.findStudentGrade(grade);
	}

	/**
	 * Description: 根据名字,身份证,电话查询符合条件的学生信息
	 * 
	 * @param sName
	 *            名字,身份证,电话
	 * @return 返回IPageInfo
	 * @see com.yz.controller.transfer.BdStudentChangeController Note: Nothing
	 *      much.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("studentChange:find")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) studentChangeService.findStudentInfo(sName));
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * @param
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception 抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentChangeController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findBdStudentChange")
	@ResponseBody
	@Rule("studentChange:find")
	public Object findBdStudentChange(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentChangeQuery query) {

		/*
		 * List<Map<String, Object>> resultList = new ArrayList<Map<String,
		 * Object>>();
		 * 
		 * for (BdStudentChange bdStudentChange : studentChangeList) {
		 * Map<String, Object> map = new HashMap<String, Object>(); Map<String,
		 * String> studentChangeM =
		 * studentChangeService.findBdStudentChange(bdStudentChange.getLearnId()
		 * , stdName, phone, idCard); if (null != studentChangeM) {
		 * studentChangeM.put("stdStage", bdStudentChange.getStdStage());
		 * map.put("old", studentChangeM); map.put("prevent",
		 * studentChangeService.findBdStudentChange(bdStudentChange.
		 * getNewLearnId(), stdName, phone, idCard)); map.put("reason",
		 * bdStudentChange.getReason()); map.put("updateUser",
		 * bdStudentChange.getUpdateUser()); map.put("stdName",
		 * studentChangeM.get("stdName")); map.put("changeId",
		 * bdStudentChange.getChangeId()); resultList.add(map); } }
		 */
		return studentChangeService.findAllBdStudentChange(start, length, query);
	}

	/**
	 * Description: 修改报读
	 * 
	 * @param studentChangeMap
	 *            BdStudentChangeMap对象字段
	 * @return 返回boolean型
	 * @see com.yz.controller.transfer.BdStudentChangeController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/editBdStudentChange")
	@ResponseBody
	@Rule("studentChange:update")
	public Object editBdStudentChange(BdStudentChangeMap studentChangeMap) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(studentChangeMap.getLearnId());
		/*
		 * 修改转报信息开始
		 */
		// 修改 bd_student_enroll
		studentChangeService.updateBSEByLearbId(studentChangeMap);
		// 修改 bd_learn_info
		studentChangeService.updateBLIByLearbId(studentChangeMap);
		// 修改 bd_student_change
		studentChangeService.updateBSCByLearbId(studentChangeMap);
		
		//添加变更记录
		studentChangeService.addStudentModify(studentChangeMap);
		/*
		 * 修改转报信息结束
		 */
		return "success";
	}

	/**
	 * Description: 添加报读
	 * 
	 * @param studentChangeMap BdUniversity对象字段
	 * @return 返回boolean型
	 */
	@RequestMapping("/addBdStudentChange")
	@ResponseBody
	@Rule("studentChange:insert")
	@Token(action = Flag.Remove, groupId = "studentChange:check")
	public Object addBdStudentChange(BdStudentChangeMap studentChangeMap, BdLearnRules bdLearnRules) {
		// 校验唯一标识不能为空
		ValidationUtil.getInstance().isNotEmptyIgnoreBlank(studentChangeMap.getOldLearnId());
		Assert.hasText(studentChangeMap.getOldStdStage(), "原转报ID不能为空");
		/*
		 * 插入转报信息开始
		 */
		/*
		 * // 插入 bd_learn_info studentChangeService.addBLI(studentChangeMap); //
		 * 插入 bd_student_enroll studentChangeService.addBSE(studentChangeMap);
		 */

		studentChangeService.addBdStudentChange(studentChangeMap, bdLearnRules);

		return "success";
	}

	@Autowired
	private StudentRecruitService studentRecruitService;

	@RequestMapping("/showFeeList")
	@ResponseBody
	@Rule("studentChange:insert")
	public Object showFeeList(@RequestParam("pfsnId") String pfsnId, @RequestParam("scholarship") String scholarship,
			@RequestParam("taId") String taId, @RequestParam("unvsId") String unvsId) {
		if (StringUtil.isEmpty(pfsnId) || StringUtil.isEmpty(scholarship) || StringUtil.isEmpty(taId)
				|| StringUtil.isEmpty(unvsId)) {
			return null;
		}
		String recruitType = studentChangeService.selectRecruitTypeByUnvsId(unvsId);
		return studentRecruitService.findFeeOfferInfo(pfsnId, scholarship, taId, recruitType);
	}
}
