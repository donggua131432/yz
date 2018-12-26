package com.yz.service.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.recruit.*;
import com.yz.service.recruit.StudentAllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.transfer.BdStudentChangeMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentChange;
import com.yz.model.transfer.BdStudentChangeMap;
import com.yz.model.transfer.BdStudentChangeQuery;
import com.yz.model.transfer.BdStudentModify;
import com.yz.service.finance.BdOrderService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentChangeService {

	private static final Logger log = LoggerFactory.getLogger(BdStudentChangeService.class);

	@Autowired
	private BdStudentChangeMapper studentChangeMapper;

	@Autowired
	private StudentAllMapper stdMapper;
	@Autowired
	private BdOrderService orderService;
	@Autowired
	private StudentRecruitService recruitService;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Autowired
	private StudentAllService stdAllService;

	private final static String[] transGrade = { "2018", "201803", "201809", "2019", "2016", "2017", "201709",
			"201703" };

	public void checkStudentSerial(String learnId) {

		Map<String, String> learnInfo = stdMapper.getLearnInfo(learnId);
		String recruitType = learnInfo.get("recruitType");

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
			checkOrderSerialAmount(learnId, "Y0");

		} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
			checkOrderSerialAmount(learnId, "Y1");
			checkOrderSerialAmount(learnId, "S1");
			checkOrderSerialAmount(learnId, "W1");
		}

	}

	/**
	 * 校验流水金额是否大于订单金额
	 * 
	 * @param learnId
	 * @param itemCode
	 */
	private void checkOrderSerialAmount(String learnId, String itemCode) {
		String tutorAmount = studentChangeMapper.selectTutorSerialAmount(learnId, itemCode);
		if (!StringUtil.hasValue(tutorAmount)) {
			return;
		}
		String tutorOrderAmount = studentChangeMapper.selectTutorOrderAmount(learnId, itemCode);

		if (!StringUtil.hasValue(tutorOrderAmount)) {
			return;
		}

		if (AmountUtil.str2Amount(tutorOrderAmount).compareTo(AmountUtil.str2Amount(tutorAmount)) < 0) { // 实缴金额大于订单金额
			throw new BusinessException("E000122");
		}

	}

	public void addBdStudentChange(BdStudentChangeMap studentChangeMap, BdLearnRules learnRules) {

		BaseUser user = SessionUtil.getUser();

		Map<String, String> learnInfo = stdMapper.getLearnInfo(studentChangeMap.getOldLearnId());

		learnInfo.put("targetGrade", studentChangeMap.getGrade());

		checkStudentSerial(studentChangeMap.getOldLearnId());

		String grade = learnInfo.get("grade");

		if (!StringUtil.hasValue(transGrade, grade)) {
			throw new BusinessException("E000096"); // 当前年级不可转报
		}

		int count = stdMapper.selectLearnInfoCount(studentChangeMap.getStdId(), studentChangeMap.getGrade());

		if (count > 0) {
			throw new BusinessException("E000100"); // 目标年级已存在报读信息
		}

		/*
		 * String stdStage = learnInfo.get("stdStage"); if
		 * (StudentConstants.RECRUIT_TYPE_GK.equals(learnInfo.get("recruitType")
		 * )) { if (!StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)) {
		 * throw new BusinessException("E000099"); // 国开学员非意向学员不可转报 } }
		 */

		BdStudentBaseInfo baseInfo = new BdStudentBaseInfo();
		baseInfo.setStdId(studentChangeMap.getStdId());
		if (null != user) {
			baseInfo.setUpdateUser(user.getRealName());
			baseInfo.setUpdateUserId(user.getUserId());
			baseInfo.setCreateUser(user.getRealName());
			baseInfo.setCreateUserId(user.getUserId());
		}
		BdStudentBaseInfo stdInfo = stdMapper.getStudentBaseInfo(studentChangeMap.getStdId());
		baseInfo.setStdName(stdInfo.getStdName());
		baseInfo.setIdCard(stdInfo.getIdCard());
		baseInfo.setIdType(stdInfo.getIdType());
		baseInfo.setMobile(stdInfo.getMobile());

		BdStudentRecruit recruit = new BdStudentRecruit();

		String recruitType = selectRecruitTypeByUnvsId(studentChangeMap.getUnvsId());

		recruit.setRecruitType(recruitType);
		recruit.setGrade(studentChangeMap.getGrade());

		recruit.setUnvsId(studentChangeMap.getUnvsId());
		recruit.setPfsnId(studentChangeMap.getPfsnId());
		recruit.setTaId(studentChangeMap.getTaId());
		recruit.setScholarship(studentChangeMap.getScholarship());

		String feeId = studentModifyMapper.selectFeeStandard(studentChangeMap.getPfsnId(), studentChangeMap.getTaId(),
				studentChangeMap.getScholarship());
		if (!StringUtil.hasValue(feeId)) {
			throw new BusinessException("E000048"); // 收费项目为空
		}

		String offerId = studentModifyMapper.selectOfferId(studentChangeMap.getPfsnId(), studentChangeMap.getTaId(),
				studentChangeMap.getScholarship());
		recruit.setFeeId(feeId);
		recruit.setOfferId(offerId);

		BdStudentOther other = new BdStudentOther();
		BdStudentHistory history = new BdStudentHistory();

		String empId = studentModifyMapper.selectRecruitEmpId(studentChangeMap.getOldLearnId());
		if (!StringUtil.hasValue(empId)) {
			empId = "false";
		}
		recruit.setEmpId(empId);
		recruit.setPfsnLevel(studentChangeMap.getPfsnLevel());

		// 修改学员状态
		exitStudent(studentChangeMap.getOldLearnId());
		String newLearnId = recruitService.recruit(baseInfo, other, recruit, history);

		// 添加招生老师信息
		if (StringUtil.hasValue(newLearnId)) {
			learnRules.setLearnId(newLearnId);
			stdAllService.updateLearnRules(learnRules);
		}
		studentChangeMap.setLearnId(newLearnId);
		// 插入 bd_student_change
		studentChangeMap.setIsComplete("1");
		addBSC(studentChangeMap);
		/*
		 * 插入转报信息结束
		 */

		addStudentModify(studentChangeMap);
		// 退费处理
		log.debug("-----------------退费开始------------------");
		orderService.studentOrderDirectRefund(learnInfo);
		log.debug("-----------------退费成功------------------");
	}

	public Map<String, String> findBdStudentChange(String learnId, String stdName, String phone, String idCard) {
		// TODO Auto-generated method stub
		return studentChangeMapper.findBdStudentChange(learnId, stdName, phone, idCard);
	}

	@SuppressWarnings("unchecked")
	public Object findAllBdStudentChange(int start, int length, BdStudentChangeQuery query) {
		// TODO Auto-generated method stub
		PageHelper.offsetPage(start, length);
		BaseUser user = SessionUtil.getUser();
		if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
			user.setUserLevel("14");
		}
		List<BdStudentChange> studentChangeList = studentChangeMapper.findAllBdStudentChange(query, user);
		return new IPageInfo((Page) studentChangeList);
	}

	public BdStudentChange selectByPrimaryKey(String changeId) {
		// TODO Auto-generated method stub
		return studentChangeMapper.selectByPrimaryKey(changeId);
	}

	public Map<String, String> findTransferByStdId(String learnId) {
		// TODO Auto-generated method stub
		Map<String, String> resultMap = studentChangeMapper.findTransferByStdId(learnId);
		RecruitEmployeeInfo recruitEmpInfo = stdMapper.getRecruitEmpInfo(learnId);
		if (recruitEmpInfo != null) {
			resultMap.put("recruitCampusName", recruitEmpInfo.getRecruitCampusName());
			resultMap.put("recruitDpName", recruitEmpInfo.getRecruitDpName());
		}
		return resultMap;
	}

	public List<Map<String, String>> findStudentInfo(String stdName) {
		BaseUser user = SessionUtil.getUser();
		if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
			user.setUserLevel("14");
		}
		// TODO Auto-generated method stub
		return studentChangeMapper.findStudentInfo(stdName, user);
	}

	public void updateBSEByLearbId(BdStudentChangeMap studentChangeMap) {
		// TODO Auto-generated method stub
		studentChangeMapper.updateBSEByLearbId(studentChangeMap);
	}

	public void updateBLIByLearbId(BdStudentChangeMap studentChangeMap) {
		// TODO Auto-generated method stub
		studentChangeMapper.updateBLIByLearbId(studentChangeMap);
	}

	public void updateBSCByLearbId(BdStudentChangeMap studentChangeMap) {
		// TODO Auto-generated method stub
		studentChangeMapper.updateBSCByLearbId(studentChangeMap);
	}

	public int addBLI(BdStudentChangeMap studentChangeMap) {
		studentChangeMap.setLearnId(IDGenerator.generatorId());
		// TODO Auto-generated method stub
		return studentChangeMapper.addBLI(studentChangeMap);
	}

	public void addBSE(BdStudentChangeMap studentChangeMap) {
		// TODO Auto-generated method stub
		studentChangeMapper.addBSE(studentChangeMap);
	}

	public void addBSC(BdStudentChangeMap studentChangeMap) {
		studentChangeMap.setChangeId(IDGenerator.generatorId());
		// TODO Auto-generated method stub
		studentChangeMapper.addBSC(studentChangeMap);
	}

	public void exitStudent(String oldLearnId) {
		// TODO Auto-generated method stub
		studentChangeMapper.exitStudent(oldLearnId);
	}

	public String selectRecruitTypeByUnvsId(String unvsId) {
		return studentChangeMapper.selectRecruitTypeByUnvsId(unvsId);
	}

	public Object findStudentGrade(String grade) {
		return studentChangeMapper.findStudentGrade(grade);
	}

	public void addStudentModify(BdStudentChangeMap studentChangeMap) {
		/*
		 * 插入变更记录
		 */
		// 得到旧的学员报读信息
		Map<String, String> oldleaninfo = stdMapper.getEnrollInfo(studentChangeMap.getOldLearnId());
		BdStudentModify studentModify = new BdStudentModify();
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_CHANGE_BEFORE);
		// 变更类型为新生信息修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_ENROLL_3);
		studentModify.setCheckOrder("1");
		studentModify.setLearnId(studentChangeMap.getLearnId());
		studentModify.setStdId(studentChangeMap.getStdId());
		studentModify.setUnvsId(oldleaninfo.get("unvsId"));
		studentModify.setNewUnvsId(studentChangeMap.getUnvsId());
		studentModify.setNewTaId(studentChangeMap.getTaId());
		studentModify.setTaId(oldleaninfo.get("taId"));
		studentModify.setNewPfsnId(studentChangeMap.getPfsnId());
		studentModify.setPfsnId(oldleaninfo.get("pfsnId"));
		studentModify.setNewScholarship(studentChangeMap.getScholarship());
		studentModify.setScholarship(oldleaninfo.get("scholarship"));
		studentModify.setNewStdStage(StudentConstants.STD_STAGE_PURPOSE);
		studentModify.setOldStdStage(studentChangeMap.getOldStdStage());
		studentModify.setIsComplete("1");
		studentModify.setCreateUser(studentChangeMap.getCreateUser());
		studentModify.setCreateUserId(studentChangeMap.getCreateUserId());
		studentModify.setModifyId(IDGenerator.generatorId());
		// 插入
		studentModifyMapper.insertSelective(studentModify);
	}
}
