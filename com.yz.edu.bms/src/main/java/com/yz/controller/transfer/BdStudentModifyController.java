package com.yz.controller.transfer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.CheckConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/studentModify")
public class BdStudentModifyController {

	private static final Logger log = LoggerFactory.getLogger(BdStudentModifyController.class);

	@Autowired
	private BdStudentModifyService studentModifyService;
	@Autowired
	private BdCheckRecordService checkRecordService;

	@RequestMapping("/list")
	@Rule("studentModify:find")
	public String showList(HttpServletRequest request) {
		return "transfer/modify/student-modify-list";
	}

	@RequestMapping("/listToAudit")
	@Rule("studentModify:findAudit")
	public String listToAudit(HttpServletRequest request) {
		return "transfer/modify/student-modify-audit-list";
	}

	@RequestMapping("/add")
	@Rule("studentModify:insert")
	@Token(action = Flag.Save, groupId = "studentModify:insert")
	public String addList(HttpServletRequest request,Model model) {
		
		//判断当前登录人是否拥有 ：优惠类型修改 权限节点
				BaseUser user = SessionUtil.getUser();
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
		
		return "transfer/modify/student-modify-add";
	}

	/*
	 * @Description:根据修改记录id查询修改信息
	 */
	@RequestMapping("/edit")
	@Rule("studentModify:check")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentModify studentModify = studentModifyService.findStudentModifyById(modifyId);
		
		model.addAttribute("exType", exType);
		model.addAttribute("studentModifyInfo", studentModify);
		return "transfer/modify/student-modify-edit";
	}

	/*
	 * @Description:根据修改记录id查询修改信息审核
	 */
	@RequestMapping("/editToAudit")
	@Rule("studentModify:checkAudit")
	@Token(action = Flag.Save, groupId = "studentModify:passAudit")
	public String editToAudit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		String modifyId = RequestUtil.getString("modifyId");
		Assert.hasText(modifyId, "参数名称不能为空");
		BdStudentModify studentModify = studentModifyService.findStudentModifyById(modifyId);
		model.addAttribute("exType", exType);
		model.addAttribute("studentModifyInfo", studentModify);
		return "transfer/modify/student-modify-audit-edit";
	}

	/**
	 * Description: 根据条件查询所有信息修改数据
	 * 
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentModify")
	@ResponseBody
	@Rule("studentModify:find")
	public Object findStudentModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		List<Map<String, String>> resultList = studentModifyService.findStudentModify(studentModifyMap, start, length);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 根据条件查询所有信息修改审核数据
	 * 
	 * @param studentModifyMap
	 *            StudentModifyMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentModifyController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentAuditModify")
	@ResponseBody
	@Rule("studentModify:findAudit")
	public Object findStudentAuditModify(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, StudentModifyMap studentModifyMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> resultList = studentModifyService.findStudentAuditModify(studentModifyMap);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 审核通过
	 * 
	 * @param crId
	 *            审核记录id
	 * @return checkStatus 审核结果
	 */
	@RequestMapping("/passStudentAuditModify")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "studentModify:passAudit")
	@Rule("studentModify:passAudit")
	public Object passStudentAuditModify(String modifyId, String checkStatus, String reason) {

		studentModifyService.passModify(modifyId, checkStatus, reason);

		return "SUCCESS";
	}

	/**
	 * Description: 审核通过
	 * 
	 * @param crId
	 *            审核记录id
	 * @return checkStatus 审核结果
	 */
	@RequestMapping("/checkModifyBatch")
	@ResponseBody
	@Rule("studentModify:passAudit")
	public Object checkModifyBatch(@RequestParam(name = "modifyIds[]", required = true) String[] modifyIds) {

		studentModifyService.passModifyBatch(modifyIds);

		return "SUCCESS";
	}

	/*
	 * @Description:查询学员（针对【考前辅导】【考前确认】阶段学员）
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("studentModify:find")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "5") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		String[] stauts = { StudentConstants.STD_STAGE_HELPING, StudentConstants.STD_STAGE_CONFIRM };
		List<Map<String, String>> studentInfoMap = studentModifyService.findStudentInfo(sName, stauts, page, rows);
		if (null == studentInfoMap) {
			return "不存在!";
		}
		return new IPageInfo((Page) studentInfoMap);
	}

	/*
	 * @Description:根据学员ID和年级查询 学员报读信息
	 */
	@RequestMapping("/findStudentEnrollInfo")
	@ResponseBody
	@Rule("studentModify:find")
	public Object findStudentEnrollInfo(String learnId) {
		Map<String, String> studentEnrollInfoMap = studentModifyService.findStudentEnrollInfo(learnId);
		return studentEnrollInfoMap;
	}

	/*
	 * @Description:新增修改申请
	 */
	@RequestMapping("/insertStudentModify")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "studentModify:insert")
	@Rule("studentModify:insert")
	public Object insertStudentModify(BdStudentModify studentModify) {
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_NEW_MODIFY);
		// 变更类型为新生信息修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_NEW_STUDENT_1);
		studentModify.setCheckOrder("1");
		// 插入
		studentModifyService.insertStudentModify(studentModify);
		return "SUCCESS";
	}

	/*
	 * @Description:修改修改申请
	 */
	@RequestMapping("/updateStudentModify")
	@ResponseBody
	@Rule("studentModify:update")
	public Object updateStudentModify(BdStudentModify studentModify) {
		studentModifyService.updateStudentModify(studentModify);
		return "SUCCESS";
	}

	/*
	 * @Description:批量取消删除
	 */
	@RequestMapping("/deleteStudentModify")
	@ResponseBody
	@Rule("studentModify:delete")
	public Object deleteStudentModify(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentModifyService.deleteStudentModify(idArray);
		return "SUCCESS";
	}

	/*
	 * @Description:通过过驳回
	 * 
	 * @param:
	 */
	public void passModifyApproval(String modifyId, String checkStatus, String reason, String learnId) {
		// TODO Auto-generated method stub
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(modifyId);
		bcr.setCheckOrder("1");
		// 到时候登录动态获取填入

		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		bcr.setCheckStatus(checkStatus);
		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);
	}
}
