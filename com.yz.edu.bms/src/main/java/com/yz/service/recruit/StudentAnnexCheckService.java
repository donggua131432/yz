package com.yz.service.recruit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.recruit.StudentAnnexCheckMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.recruit.BdLearnAnnex;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.StudentAnnexCheck;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StudentAnnexCheckService {

	private static final Logger log = LoggerFactory.getLogger(StudentAnnexCheckService.class);

	@Autowired
	private StudentAnnexCheckMapper annexCheckMapper;

	@Autowired
	private StudentAllMapper allMapper;

	@Autowired
	private BdLearnAnnexMapper lAnnexMapper;

	@Autowired
	private BdStudentModifyService modifyService;
	
	@Autowired
	private YzSysConfig yzSysConfig ; 

	/**
	 * 获取考前资料核查列表
	 * 
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<StudentAnnexCheck> getStudentAnnexCheckList(StudentAnnexCheckQuery queryInfo) {
		// 考前核查只查询基本信息已完善及资料已上传的学员信息
		queryInfo.setIsDataCompleted("1");
		//queryInfo.setMyAnnexStatus("2");
		BaseUser user = SessionUtil.getUser();
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<String> jtList = user.getJtList();
		if (jtList.contains("GKZFDY")) {// 国开辅导员
			queryInfo.setExt1(" and l.recruit_type = '2' and r.tutor ='" + user.getEmpId() + "'");
		} else if (jtList.contains("CJZFDY")) { // 成教辅导员
			queryInfo.setExt1(" and l.recruit_type = '1' and r.tutor ='" + user.getEmpId() + "'");
		} else if (jtList.contains("GKZFDY") && jtList.contains("CJZFDY")) {
			if (StringUtil.hasValue(queryInfo.getRecruitType())) { // 两者都有的辅导员
				queryInfo.setExt1(" and l.recruit_type='" + queryInfo.getRecruitType() + "'");
			}
		}
		List<StudentAnnexCheck> list = annexCheckMapper.getStudentAnnexCheckList(queryInfo, user);

		for (StudentAnnexCheck check : list) {
			String annexStatus = check.getMyAnnexStatus();
			String learnId = check.getLearnId();
			if (StudentConstants.ANNEX_STATUS_REJECT.equals(annexStatus)
					|| StudentConstants.ANNEX_STATUS_UNCHECK.equals(annexStatus)) {
				int count = lAnnexMapper.countBy(learnId, annexStatus);
				check.setAnnexCount(count);
			}
		}

		return new IPageInfo<StudentAnnexCheck>((Page<StudentAnnexCheck>) list);
	}

	/**
	 * 获取学员附件列表
	 * 
	 * @param learnId
	 * @return
	 */
	public IPageInfo<BdLearnAnnex> getAnnexList(String learnId) {
		List<BdLearnAnnex> list = lAnnexMapper.getAnnexList(learnId);
		if (list != null && !list.isEmpty()) {
			return new IPageInfo<BdLearnAnnex>(list, list.size());
		} else {
			list = new ArrayList<>();
			return new IPageInfo<BdLearnAnnex>(list, 0);
		}
	}

	/**
	 * 更新附件信息
	 * 
	 * @param annexInfo
	 */
	public String updateAnnexInfo(BdLearnAnnex annexInfo) {
		if (annexInfo.getAnnexFile() == null || !(annexInfo.getAnnexFile() instanceof MultipartFile)) {
			throw new IllegalArgumentException("上传文件不能为空");
		}

		MultipartFile file = (MultipartFile) annexInfo.getAnnexFile();
		Assert.hasText(annexInfo.getAnnexId(), "附件ID不能为空");
		Assert.hasText(annexInfo.getLearnId(), "附件所属学员不能为空");
		String realFilePath = null;
		String annexUrl = annexInfo.getAnnexUrl();
		if (StringUtil.isEmpty(annexUrl)) {
			annexUrl = FileSrcUtil.createFileSrc(Type.STUDENT, annexInfo.getLearnId(), file.getOriginalFilename());
		}

		realFilePath = annexUrl;

		try {
			String bucket = yzSysConfig.getBucket();
			FileUploadUtil.upload(bucket, realFilePath, file.getBytes());
		} catch (IOException e) {
			log.error("---------------------------- 文件上传失败", e);
		}

		annexInfo.setAnnexUrl(annexUrl);
		if (GlobalConstants.TRUE.equals(annexInfo.getIsRequire())) {
			annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNCHECK);
		} else {
			annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_ALLOW);
		}
		annexInfo.setReason("");
		annexInfo.setUploadTime(new Date());
		annexInfo.setUploadUser(annexInfo.getUpdateUser());
		annexInfo.setUploadUserId(annexInfo.getUpdateUserId());
		if (!StringUtil.hasValue(annexInfo.getIsRequire()) || "null".equals(annexInfo.getIsRequire())) {
			annexInfo.setIsRequire(null);
		}

		lAnnexMapper.updateByPrimaryKeySelective(annexInfo);

		// 添加变更记录
		annexInfo = lAnnexMapper.selectByPrimaryKey(annexInfo.getAnnexId());
		modifyService.addStudentModifyRecord(annexInfo.getLearnId(), "添加了" + annexInfo.getAnnexName());

		statusChange(annexInfo);
		return annexUrl;
	}

	/**
	 * 删除附件信息
	 * 
	 * @param annexInfo
	 */
	public void delAnnexInfo(BdLearnAnnex annexInfo) {
		Assert.hasText(annexInfo.getAnnexId(), "附件ID不能为空");
		Assert.hasText(annexInfo.getLearnId(), "附件所属学员不能为空");
		Assert.hasText(annexInfo.getAnnexUrl(), "附件地址不能为空");
		String bucket = yzSysConfig.getBucket();
		String realFilePath = annexInfo.getAnnexUrl();

		annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNUPLOAD);
		annexInfo.setAnnexUrl("");
		annexInfo.setReason("");
		lAnnexMapper.updateByPrimaryKeySelective(annexInfo);
		// annexCheckMapper.updateAnnexInfo(annexInfo);
		statusChange(annexInfo);

		FileUploadUtil.delete(bucket, realFilePath);

		// 添加变更记录
		annexInfo = lAnnexMapper.selectByPrimaryKey(annexInfo.getAnnexId());
		modifyService.addStudentModifyRecord(annexInfo.getLearnId(), "删除了" + annexInfo.getAnnexName());

	}

	/**
	 * 审核附件
	 * 
	 * @param annexInfo
	 */
	public void charge(BdLearnAnnex annexInfo) {
		annexInfo.setCheckUser(annexInfo.getUpdateUser());
		annexInfo.setCheckUserId(annexInfo.getUpdateUserId());
		annexInfo.setCheckTime(new Date());
		lAnnexMapper.updateByPrimaryKeySelective(annexInfo);
		// annexCheckMapper.updateAnnexInfo(annexInfo);
		statusChange(annexInfo);

		// 添加变更记录
		modifyService.addStudentModifyRecord(annexInfo.getLearnId(), "charge");
	}

	/**
	 * 考前资料审核
	 * 
	 * @param check
	 */
	public StudentCheckRecord check(StudentCheckRecord check) {
		BdLearnInfo learnInfo = annexCheckMapper.getLearnInfo(check.getLearnId());
		Assert.notNull(learnInfo, "学业信息不能为空");

		String recruitType = check.getRecruitType();
		Assert.hasText(recruitType, "招生类型不能为空");

		if("1".equals(recruitType)){
			setCheckCJ(check, learnInfo);
		}else{
			setCheckGK(check, learnInfo);
		}

		check.setEmpId(SessionUtil.getUser().getEmpId());
		check.setUpdateTime(DateUtil.formatDate(new Date(), DateUtil.YYYYMMDDHHMMSS_SPLIT));

		annexCheckMapper.updateCheckRecord(check);
		annexCheckMapper.updateIsDataCheck(learnInfo);
		return check;
	}
	private void setCheckGK(StudentCheckRecord check, BdLearnInfo learnInfo){
		String jtId = check.getJtId();
		String isDataCheck = check.getIsDataCheck();
		String nextStage = null;
		//String stdStage = learnInfo.getStdStage();

		if ("1".equals(isDataCheck)) {
			check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
			check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
			String checkMsg = "";
			if("GKZL1".equals(jtId)){
				//考前确认 3
				learnInfo.setIsDataCheck("3");
				nextStage = StudentConstants.STD_STAGE_CONFIRM;
				checkMsg = "国开资料初审";
			}else if("GKZL2".equals(jtId)){
				//待录取 12
				learnInfo.setIsDataCheck("4");
				nextStage = StudentConstants.STD_STAGE_UNENROLLED;
				checkMsg = "国开资料二审";
				// 更新计算提成时间
				annexCheckMapper.updateExpenseTime(check.getLearnId());
			}else if("GKZL3".equals(jtId)){
				//已录取 5
				learnInfo.setIsDataCheck("5");
				nextStage = StudentConstants.STD_STAGE_ENROLLED;
				checkMsg = "国开资料三审";
			}else if("GKZL4".equals(jtId)){
				//注册学员 6
				learnInfo.setIsDataCheck(isDataCheck);
				nextStage = StudentConstants.STD_STAGE_REGISTER;
				checkMsg = "国开资料终审";
			}
			// 添加变更记录
			modifyService.addStudentModifyRecord(check.getLearnId(), checkMsg);
		} else if ("2".equals(isDataCheck)) {
			// 如果驳回更改资料是否已完善 改为未完善
			learnInfo.setExt1(check.getExt1());
			check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);
			check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);
			String checkMsg = "";
			if("GKZL1".equals(jtId)){
				//考前辅导 2
				learnInfo.setIsDataCompleted("2");
				nextStage = StudentConstants.STD_STAGE_HELPING;
				checkMsg = "国开资料初审驳回："+check.getExt1();
			}else if("GKZL2".equals(jtId)){
				//考前确认 3
				nextStage = StudentConstants.STD_STAGE_CONFIRM;
				checkMsg = "国开资料二审驳回："+check.getExt1();
			}else if("GKZL3".equals(jtId)){
				//待录取 12
				nextStage = StudentConstants.STD_STAGE_UNENROLLED;
				checkMsg = "国开资料三审驳回："+check.getExt1();
			}else if("GKZL4".equals(jtId)){
				//已录取 5
				nextStage = StudentConstants.STD_STAGE_ENROLLED;
				checkMsg = "国开资料终审驳回："+check.getExt1();
			}
			// 添加变更记录
			modifyService.addStudentModifyRecord(check.getLearnId(), checkMsg);
			//设置驳回原因
			check.setReason(checkMsg);
		} else {
			if("GKZL4".equals(jtId)){
				check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				nextStage = StudentConstants.STD_STAGE_ENROLLED;
				// 添加变更记录
				modifyService.addStudentModifyRecord(check.getLearnId(), "国开终审撤销学员资料审核");
			}else{
				log.info("先记录下来：国开非终审是无法撤销的！");
			}
		}

		learnInfo.setStdStage(nextStage);
	}


	private void setCheckCJ(StudentCheckRecord check, BdLearnInfo learnInfo){
		String nextStage = null;
		String stdStage = learnInfo.getStdStage();

		learnInfo.setIsDataCheck(check.getIsDataCheck());
		if ("1".equals(check.getIsDataCheck())) {
			check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
			check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
			nextStage = StudentConstants.STD_STAGE_CONFIRM;

			/*if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
				nextStage = StudentConstants.STD_STAGE_CONFIRM;
			} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				nextStage = StudentConstants.STD_STAGE_UNENROLLED;
			}*/

			// 更新计算提成时间
			annexCheckMapper.updateExpenseTime(check.getLearnId());
			// 添加变更记录
			modifyService.addStudentModifyRecord(check.getLearnId(), "核查学员考前资料");

		} else if ("2".equals(check.getIsDataCheck())) {
			// 如果驳回更改资料是否已完善 改为未完善
			learnInfo.setIsDataCompleted("2");
			learnInfo.setExt1(check.getExt1());

			check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);
			check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);
			// 添加变更记录
			modifyService.addStudentModifyRecord(check.getLearnId(), "驳回学员考前资料："+check.getExt1());
			//设置驳回原因
			check.setReason("驳回学员考前资料："+check.getExt1());
		} else {
			check.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
			check.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
			nextStage = StudentConstants.STD_STAGE_HELPING;

			// 添加变更记录
			modifyService.addStudentModifyRecord(check.getLearnId(), "撤销核查学员考前资料");
		}

		if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage) || StudentConstants.STD_STAGE_HELPING.equals(stdStage)
				|| StudentConstants.STD_STAGE_CONFIRM.equals(stdStage)
				|| StudentConstants.STD_STAGE_TESTING.equals(stdStage)) {
			learnInfo.setStdStage(nextStage);
		}
	}

	/**
	 * 更新附件上传状态
	 * 
	 * @param annexInfo
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void statusChange(BdLearnAnnex annexInfo) {
		int reject = 0;
		int unchecked = 0;
		int unupload = 0;
		int allow = 0;
		// List<BdStudentAnnex> annexList =
		// annexCheckMapper.selectRequiredAnnex(annexInfo.getStdId());

		List<BdLearnAnnex> annexList = lAnnexMapper.selectRequiredAnnex(annexInfo.getLearnId());

		int size = annexList.size();

		if (annexList != null && !annexList.isEmpty()) {
			for (BdLearnAnnex ai : annexList) {
				if (StudentConstants.ANNEX_STATUS_REJECT.equals(ai.getAnnexStatus())) {
					reject++;
				} else if (StudentConstants.ANNEX_STATUS_UNCHECK.equals(ai.getAnnexStatus())) {
					unchecked++;
				} else if (StudentConstants.ANNEX_STATUS_UNUPLOAD.equals(ai.getAnnexStatus())) {
					unupload++;
				} else if (StudentConstants.ANNEX_STATUS_ALLOW.equals(ai.getAnnexStatus())) {
					allow++;
				}
			}
		}

		String myAnnexStatus = null;

		if (reject > 0) {
			myAnnexStatus = StudentConstants.ANNEX_STATUS_REJECT;
		} else if (unupload > 0) {
			myAnnexStatus = StudentConstants.ANNEX_STATUS_UNUPLOAD;
		} else if (unchecked > 0) {
			myAnnexStatus = StudentConstants.ANNEX_STATUS_UNCHECK;
		} else if (size > 0 && size == allow) {
			myAnnexStatus = StudentConstants.ANNEX_STATUS_ALLOW;
		}

		lAnnexMapper.updateLearnAnnexStatus(annexInfo.getLearnId(), myAnnexStatus);

		// annexCheckMapper.updateStudentAnnexStatus(annexInfo.getStdId(),
		// myAnnexStatus);

		BdStudentHistory history = allMapper.getStudentHistory(annexInfo.getLearnId());

		if (history != null) {
			String isTestCompleted = GlobalConstants.FALSE;
			if (StudentDataCheckUtil.isTestCompleted(history)
					&& (StudentConstants.ANNEX_STATUS_ALLOW.equals(myAnnexStatus)
							|| StudentConstants.ANNEX_STATUS_UNCHECK.equals(myAnnexStatus))) {
				isTestCompleted = GlobalConstants.TRUE;
			}

			BdLearnInfo learnInfo = new BdLearnInfo();
			learnInfo.setLearnId(annexInfo.getLearnId());
			learnInfo.setIsTestCompleted(isTestCompleted);

			allMapper.updateIsDataCompleted(learnInfo);
		}
	}

	public String getGrade(String learnId) {
		return lAnnexMapper.selectGradeByLearnId(learnId);
	}

}
