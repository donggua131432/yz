package com.yz.service.transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;

import com.yz.constants.*;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.finance.BdSubOrderMapper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.model.communi.Body;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.message.GwReceiver;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.transfer.*;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.system.SysDictService;
import com.yz.util.AmountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.WechatSendServiceMsgUtil;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.system.SysDict;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentRollModifyService {

	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;
	@Autowired
	private BdStudentModifyService studentModifyService;

	@Autowired
	private InviteUserMapper userMapper;

	@Autowired
	private BdSubOrderMapper subOrderMapper;

	@Autowired
	private BdStdEnrollMapper stdMapper;

	@Autowired
	private StudentRecruitService recruitService;

	@Autowired
	private BdLearnAnnexMapper BdLearnAnnexMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private BdStudentChangeService changeService;

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private UsInfoMapper usInfoMapper;

	public void insertStudentRollModify(BdStudentModify studentModify) {
		checkParam(studentModify);
		checkRepeat(studentModify);

		// 检查流水金额是否大于订单金额
		changeService.checkStudentSerial(studentModify.getLearnId());

		//TODO 临时加入优惠分组 后续可以从页面带过来
		String scholarship = studentModify.getScholarship();
		if(StringUtil.hasValue(scholarship)) {
			SysDict dict = sysDictService.getDict("scholarship." + scholarship);
			String sg = dict.getExt1();//优惠分组
			studentModify.setSg(sg);
		}

		String newScholarship = studentModify.getNewScholarship();
		if(StringUtil.hasValue(newScholarship)) {
			SysDict newDict = sysDictService.getDict("scholarship." + newScholarship);
			String newSg = newDict.getExt1();//优惠分组
			studentModify.setNewSg(newSg);
		}

		studentModify.setModifyId(IDGenerator.generatorId());
		studentModifyMapper.insertSelective(studentModify);
		// 添加审核记录
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		checkWeights = studentOutService.getCheckWeight(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecordService.addBdCheckRecord(checkRecord);
		}
	}

	private void checkRepeat(BdStudentModify studentModify) {

		boolean repeatFlag = true;

		if (StringUtil.hasValue(studentModify.getNewStdName())
				&& !studentModify.getStdName().equals(studentModify.getNewStdName())) {
			repeatFlag = false;
		}
		if (StringUtil.hasValue(studentModify.getNewIdCard())
				&& !studentModify.getIdCard().equals(studentModify.getNewIdCard())) {
			repeatFlag = false;
		}
		if (StringUtil.hasValue(studentModify.getNewSex())
				&& !studentModify.getSex().equals(studentModify.getNewSex())) {
			repeatFlag = false;
		}
		if (StringUtil.hasValue(studentModify.getNewNation())
				&& !studentModify.getNation().equals(studentModify.getNewNation())) {
			repeatFlag = false;
		}
		if (StringUtil.hasValue(studentModify.getNewUnvsId())
				&& !studentModify.getUnvsId().equals(studentModify.getNewUnvsId())) {
			repeatFlag = false;
		}
		if (StringUtil.hasValue(studentModify.getNewPfsnId())
				&& !studentModify.getPfsnId().equals(studentModify.getNewPfsnId())) {
			repeatFlag = false;
		}
	}

	private void checkParam(BdStudentModify studentModify){
		boolean blankFlag = false;
		if(StringUtil.hasValue(studentModify.getNewStdName())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewIdCard())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewSex())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewNation())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewUnvsId())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewPfsnId())){
			blankFlag = true;
		}
		if(StringUtil.hasValue(studentModify.getNewTaId())){
			blankFlag = true;
		}
		if(!blankFlag){
			throw new BusinessException("E000078");	// 提交内容不能为空
		}
		if(studentModifyMapper.countYMStudent(studentModify.getLearnId())>0 && StringUtil.hasValue(studentModify.getNewPfsnId())) {
			throw new BusinessException("E40006");
		}
	}

	public List<StudentRollModifyMap> findStudentRollModify(StudentModifyMap studentModifyMap) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentRollModify(studentModifyMap);
	}

	public BdStudentRollModify findStudentRollModifyById(String modifyId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentRollModifyById(modifyId);
	}

	public void passApproval(String checkStatus, String reason, String modifyId, String checkOrder) {
		// TODO Auto-generated method stub
		/*
		 * BaseUser user = SessionUtil.getUser(); String checkOrder =
		 * studentOutService.getCheckOrder(TransferConstants.
		 * CHECK_TYPE_SCHOOLROLL_MODIFY,user.getJtId());
		 */
		BdStudentModify studentModify = new BdStudentModify();
		BdCheckRecord bcr = new BdCheckRecord();
		studentModify.setModifyId(modifyId);
		// 是否通过
		if (CheckConstants.PASS_CHECK_2.equals(checkStatus)) {
			// 审核全部完成
			if (TransferConstants.CHECK_RECORD_ORDER_THIRD.equals(checkOrder)) {
				BdStudentModify bdStudentModify = studentModifyService.findStudentModifyById(modifyId);
				// 通过则修改数据bd_student_enroll bd_learn_info bd_student_info
				studentModifyService.updateBdStudentEnroll(bdStudentModify);// 根据学业id
				studentModifyService.updateBdLearnInfo(bdStudentModify);// 根据学业id
				studentModifyService.updateBdStudentInfo(bdStudentModify);// 根据学员id
				// 审核流程完成结束
				studentModify.setIsComplete("1");
				studentModify.setExt1("审核状态 ：审核通过");
			}else if(TransferConstants.CHECK_RECORD_ORDER_SECOND.equals(checkOrder)){
				studentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
			}
		} else if(CheckConstants.REJECT_CHECK_3.equals(checkStatus)){
			studentModify.setIsComplete("1");
			studentModify.setExt1("审核状态 ：审核被驳回，驳回原因："+reason);
			// 未通过
			bcr.setReason(reason);
		}else {
			return;
		}
		BaseUser user = SessionUtil.getUser();
		studentModify.setUpdateUser(user.getRealName());
		studentModify.setUpdateUserId(user.getUserId());

		studentModifyMapper.updateByPrimaryKeySelective(studentModify);
		bcr.setMappingId(modifyId);
		bcr.setCheckOrder(checkOrder);
		// 到时候登录动态获取填入
		bcr.setEmpId(user.getEmpId());
		bcr.setCheckStatus(checkStatus);
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		// bcr.setEmpId("11");
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public List<StudentRollModifyMap> findStudentAuditRollModify(StudentModifyMap studentModifyMap) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentAuditRollModify(studentModifyMap);
	}

	public List<Map<String, String>> findStudentInfo(String sName, String[] stauts, int page, int rows) {
		PageHelper.startPage(page, rows);
		BaseUser user = SessionUtil.getUser();
		user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
		return studentModifyMapper.findStudentInfo(sName, stauts, user);
	}

	public List<StudentRollModifyMap> findStudentRollModifyNew(StudentModifyQuery query) {
		BaseUser user = SessionUtil.getUser();
		if(user.getJtList().contains("FDY")){
			user.setUserLevel("6");
		}
		if(user.getJtList().contains("GKXJ")){
			if(StringUtil.hasValue(query.getRecruitType()) && query.getRecruitType().equals("1")) {
				// 设置无效参数，让查询结果为空
				query.setRecruitType("3");
			}else {
				query.setRecruitType("2");
			}
			user.setUserLevel("1");
		}
		if(user.getJtList().contains("CJXJ")){
			if(StringUtil.hasValue(query.getRecruitType()) && query.getRecruitType().equals("2")) {
				// 设置无效参数，让查询结果为空
				query.setRecruitType("3");
			}else {
				query.setRecruitType("1");
			}
			user.setUserLevel("1");
		}
		if(user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
			query.setRecruitType("");
			user.setUserLevel("1");
		}
		// 部门主任，教学干事，教学组长
		if(user.getJtList().contains("BMZR") || user.getJtList().contains("JXGS") || user.getJtList().contains("JXZZ") || user.getJtList().contains("400")){
			user.setUserLevel("1");
		}
		return studentModifyMapper.findStudentRollModifyNew(query,user);
	}

	public void insertStudentRollModifyNew(BdStudentModify studentModify) {
		checkParam(studentModify);
		if (studentModifyMapper.countFinish(studentModify.getLearnId()) > 0) {
			throw new BusinessException("E40007");
		}
		if (StringUtil.hasValue(studentModify.getNewPfsnId()) && studentModifyMapper.countNewPfsnFinish(studentModify.getLearnId()) > 0) {
			throw new BusinessException("E40008");
		}
		studentModify.setModifyId(IDGenerator.generatorId());
        studentModify.setExt3(getChangeType(studentModify));
		studentModifyMapper.insertSelective(studentModify);

		// 添加审核记录
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		checkWeights = studentOutService.getCheckWeight(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY_NEW);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecordService.addBdCheckRecord(checkRecord);
		}
	}

	public BdStudentRollModify findStudentRollModifyNewById(String modifyId) {
		return studentModifyMapper.findStudentRollModifyNewById(modifyId);
	}

	public void passApprovalNew(String checkStatus, String reason, String modifyId, String checkOrder) {
		BdStudentModify studentModify = new BdStudentModify();
		BdCheckRecord bcr = new BdCheckRecord();
		studentModify.setModifyId(modifyId);
		// 是否通过
		if (CheckConstants.PASS_CHECK_2.equals(checkStatus)) {
			// 审核全部完成
			if (TransferConstants.CHECK_RECORD_ORDER_THIRD.equals(checkOrder)) {
				BdStudentModify bdStudentModify = studentModifyService.findStudentModifyById(modifyId);
				studentModifyService.updateBdLearnInfo(bdStudentModify);// 根据学业id
				studentModifyService.updateBdStudentInfo(bdStudentModify);// 根据学员id
				// 审核流程完成结束
				studentModify.setIsComplete("1");
				studentModify.setExt2("审核状态 ：审核通过");

				 // 如果是转专业申请
				if(StringUtil.hasValue(bdStudentModify.getNewPfsnId())) {
                    String taId = StringUtil.hasValue(bdStudentModify.getNewTaId())?bdStudentModify.getNewTaId():bdStudentModify.getTaId();
					String feeId = studentModifyMapper.selectFeeStandard(bdStudentModify.getNewPfsnId(),
                            taId, bdStudentModify.getScholarship());
					if(!StringUtil.hasValue(feeId)){
						throw new BusinessException("E000040");// 请先配置收费科目
					}
					// 退款操作（退现金，智米，退优惠券，修改老订单状态，刷新订单）
					demurrageRefund(bdStudentModify.getLearnId(),bdStudentModify.getStdId());
					zhimiRefund(bdStudentModify.getLearnId(),bdStudentModify.getStdId());
					couponRefund(bdStudentModify.getLearnId(),bdStudentModify.getStdId());
					studentModifyMapper.updateOrderStatus(bdStudentModify.getLearnId());
					initStudentOrder(bdStudentModify);
					SendWeChat(bdStudentModify.getLearnId(),"");
				}
			}else if(TransferConstants.CHECK_RECORD_ORDER_SECOND.equals(checkOrder)){
				studentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
			} else if(TransferConstants.CHECK_RECORD_ORDER_FIRST.equals(checkOrder)){
				studentModify.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
			}
		} else if(CheckConstants.REJECT_CHECK_3.equals(checkStatus)){
			studentModify.setIsComplete("1");
			studentModify.setExt2("审核状态 ：审核被驳回，驳回原因："+reason);
			// 未通过
			bcr.setReason(reason);

			// 如果是转专业申请
			BdStudentModify bdStudentModify = studentModifyService.findStudentModifyById(modifyId);
			if(StringUtil.hasValue(bdStudentModify.getNewPfsnId())) {
				SendWeChat(bdStudentModify.getLearnId(), reason);
			}
		}else {
			return;
		}
		BaseUser user = SessionUtil.getUser();
		studentModify.setUpdateUser(user.getRealName());
		studentModify.setUpdateUserId(user.getUserId());
		studentModifyMapper.updateByPrimaryKeySelective(studentModify);
		bcr.setMappingId(modifyId);
		bcr.setCheckOrder(checkOrder);
		// 到时候登录动态获取填入
		bcr.setEmpId(user.getEmpId());
		bcr.setCheckStatus(checkStatus);
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public List<Map<String, String>> findStudentInfoNew(String sName, int page, int rows) {
		PageHelper.startPage(page, rows);
		BaseUser user = SessionUtil.getUser();
		if(user.getJtList().contains("FDY")){
			user.setUserLevel("6");
		}
		if(user.getJtList().contains("CJXJ")){
			user.setUserLevel("7");
		}
		if(user.getJtList().contains("GKXJ")){
			user.setUserLevel("8");
		}
		if(user.getJtList().contains("GKXJ") && user.getJtList().contains("CJXJ")) {
			user.setUserLevel("1");
		}
		if(user.getJtList().contains("400")){
			user.setUserLevel("1");
		}
		return studentModifyMapper.findStudentInfoNew(sName,user);
	}

	/**
	 * 微信消息推送
	 * @param learnId
	 * @param reason 驳回原因
	 */
	private void SendWeChat(String learnId,String reason){
		GwReceiver std = stdMapper.selectStdInfoByLearnId(learnId);
		String openId = usInfoMapper.selectUserOpenId(std.getUserId());
		// 微信推送消息
		String msg = "恭喜您，您的转专业申请已通过！";
		if (StringUtil.hasValue(reason)) {
			msg = "很遗憾，您的转专业申请已被驳回，驳回原因：" + reason;
		}
		WechatSendServiceMsgUtil.wechatSendServiceMsg(openId, msg);
	}

	/**
	 * 滞留金退款
	 * @param learnId
	 * @param stdId
	 */
	public void demurrageRefund(String learnId,String stdId){
		Map<String,Object> map = studentModifyMapper.getAccAmount(learnId,FinanceConstants.ACC_TYPE_DEMURRAGE);
		String amount = String.valueOf(map.get("amount"));
		String orderNo = String.valueOf(map.get("orderNo"));
		if (AmountUtil.str2Amount(amount).compareTo(BigDecimal.ZERO) > 0) {
			// 设置转账对象
			String grade = "";
			grade= BdLearnAnnexMapper.selectGradeByLearnId(learnId);
			Body body = new Body();
			body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
			body.put("stdId", stdId);
			body.put("amount", amount);
			body.put("action", FinanceConstants.ACC_ACTION_IN);
			body.put("excDesc", grade+"级退款，退滞留金到滞留金账户");
			body.put("mappingId", orderNo);
			accountApi.trans(body);
		}
	}

	/**
	 * 智米退款
	 * @param learnId
	 * @param stdId
	 */
	public void zhimiRefund(String learnId,String stdId){
		Map<String,Object> map = studentModifyMapper.getAccAmount(learnId,FinanceConstants.ACC_TYPE_ZHIMI);
		String amount = String.valueOf(map.get("amount"));
		String orderNo =String.valueOf(map.get("orderNo"));
		List<BdSubOrder> subOrders=subOrderMapper.getSubOrders(learnId);
		String grade = "";
		grade= BdLearnAnnexMapper.selectGradeByLearnId(learnId);
		if (AmountUtil.str2Amount(amount).compareTo(BigDecimal.ZERO) > 0) {
			// 设置转账对象
			Body body = new Body();
			body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			body.put("stdId", stdId);
			body.put("amount", amount);
			body.put("action", FinanceConstants.ACC_ACTION_IN);
			body.put("excDesc", grade+"级退款，退智米到智米账户");
			body.put("mappingId", orderNo);
			accountApi.trans(body);
		}
	}

	/**
	 * 退优惠券
	 * @param learnId
	 */
	public void couponRefund(String learnId,String stdId){
		List<String> couponIds = studentModifyMapper.getCouponIds(learnId);
		if(couponIds.size()>0) {
			studentModifyMapper.updateCouponUse(stdId, couponIds);
		}
	}

	/**
	 * 刷新订单
	 * @param bdStudentModify
	 */
	public void initStudentOrder(BdStudentModify bdStudentModify){
		// 修改后收费标准ID
        String taId = StringUtil.hasValue(bdStudentModify.getNewTaId())?bdStudentModify.getNewTaId():bdStudentModify.getTaId();
		String feeId = studentModifyMapper.selectFeeStandard(bdStudentModify.getNewPfsnId(),
                taId, bdStudentModify.getScholarship());
		String offerId = studentModifyMapper.selectOfferId(bdStudentModify.getNewPfsnId(),
                taId, bdStudentModify.getScholarship());
		BdLearnInfo learnInfo = new BdLearnInfo();
		BaseUser user = SessionUtil.getUser();
		learnInfo.setCreateUser(user.getRealName());
		learnInfo.setCreateUserId(user.getUserId());
		learnInfo.setFeeId(feeId);
		learnInfo.setMobile(bdStudentModify.getMobile());
		learnInfo.setStdName(bdStudentModify.getStdName());
		learnInfo.setStdId(bdStudentModify.getStdId());
		learnInfo.setUpdateUser(user.getRealName());
		learnInfo.setUpdateUserId(user.getUserId());
		learnInfo.setPfsnId(bdStudentModify.getNewPfsnId());
		learnInfo.setUnvsId(bdStudentModify.getUnvsId());
		learnInfo.setTaId(taId);
		learnInfo.setFeeId(feeId);
		learnInfo.setOfferId(offerId);
		learnInfo.setRecruitType(bdStudentModify.getRecruitType());
		//learnInfo.setStdStage(StudentConstants.STD_STAGE_ENROLLED);
		learnInfo.setLearnId(bdStudentModify.getLearnId());
		recruitService.initStudentOrder(learnInfo);
	}

	public void batchPassApprovalNew(String[] idArray,String checkStatus,String checkOrder){
		for (String id:idArray) {
			passApprovalNew(checkStatus, "",id, checkOrder);
		}
	}

	private String getChangeType(BdStudentModify bdStudentModify){
        String type = "";
        if (StringUtil.hasValue(bdStudentModify.getNewStdName()) || StringUtil.hasValue(bdStudentModify.getNewIdCard()) || StringUtil.hasValue(bdStudentModify.getNewSex()) || StringUtil.hasValue(bdStudentModify.getNewNation())) {
            type = "1";
        }
        if (StringUtil.hasValue(bdStudentModify.getNewUnvsId()) || StringUtil.hasValue(bdStudentModify.getNewPfsnId()) || StringUtil.hasValue(bdStudentModify.getNewTaId())) {
            type = "2";
        }
        if ((StringUtil.hasValue(bdStudentModify.getNewStdName()) || StringUtil.hasValue(bdStudentModify.getNewIdCard()) || StringUtil.hasValue(bdStudentModify.getNewSex()) || StringUtil.hasValue(bdStudentModify.getNewNation())) &&
                (StringUtil.hasValue(bdStudentModify.getNewUnvsId()) || StringUtil.hasValue(bdStudentModify.getNewPfsnId()) || StringUtil.hasValue(bdStudentModify.getNewTaId()))) {
            type = "3";
        }
        return type;
	}
}
