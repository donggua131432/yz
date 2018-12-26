package com.yz.service.stdService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.finance.BdSubOrderMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.dao.stdService.StudentClassPlanMapper;
import com.yz.dao.stdService.StudentStudyingMapper;
import com.yz.dao.stdService.StudentTaskFollowUpMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.SystemException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.stdService.StudentStudyingQuery;
import com.yz.model.educational.MakeSchedule;
import com.yz.model.finance.AtsAccount;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.recruit.BdLearnRemarkLog;
import com.yz.model.stdService.StudentStudyingListInfo;
import com.yz.model.stdService.StudentTaskFollowUp;
import com.yz.model.stdService.StudyingContacts;
import com.yz.model.stdService.StudyingContactsShow;
import com.yz.model.stdService.StudyingPaymentInfo;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StudentStudyingService {

	private static final Logger log = LoggerFactory.getLogger(StudentStudyingService.class);

	@Autowired
	private YzSysConfig yzSysConfig ; 
	
	@Autowired
	private StudentStudyingMapper studyingMapper;

	@Autowired
	private BdSubOrderMapper subOrderMapper;

	@Autowired
	private StudentRecruitMapper recruitMapper;

	@Autowired
	private AtsAccountMapper accountMapper;
	
	@Autowired
	private StudentTaskFollowUpMapper studentTaskFollowUpMapper;
	
	@Autowired
	private StudentClassPlanMapper classplanMapper;
	
	/**
	 * 修改学员编号或学号
	 * @param learnId
	 * @param stdNo
	 * @param schoolRoll
	 */
	public void updateStudentNo(String learnId,String stdNo,String schoolRoll) {
		studyingMapper.updateStudyIngNo(learnId, stdNo, schoolRoll);
	}
	public StudentStudyingListInfo getStudyingInfoByLearnId(String learnId){
		StudentStudyingListInfo info=studyingMapper.getStudyingInfoByLearnId(learnId);
		return info;
		
	}
	public IPageInfo<StudentStudyingListInfo> getStudyingListJoinAcc(StudentStudyingQuery queryInfo) {
		BaseUser user = SessionUtil.getUser();
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength()).setCountMapper("com.yz.dao.stdService.StudentStudyingMapper.getStudyingCount");;

		//特殊处理临时
		List<String> jtList = user.getJtList();
		if (jtList != null) {
			//辅导员只能开到所带班级的待录取、已录取、注册学员、在读学员数据
			if(jtList.contains("FDY")) {
				user.setUserLevel("5");
			}
			//成教学籍只能看到成教在读学员的数据
			if(jtList.contains("CJXJ")) {
				user.setUserLevel("6");
			}
			//国开学籍、国开审核能看到全部所属招生类型的数据（含考前辅导、待录取、已录取、注册学员、在读学员）
			if(jtList.contains("GKXJ") || jtList.contains("GKSH")) {
				user.setUserLevel("7");
			}
			//BMZR部门主管能看到全部数据（成教学籍组长+国开学籍组长）
			if(jtList.contains("BMZR")) {
				user.setUserLevel("8");
			}
			if(jtList.contains("GKXJ") && jtList.contains("FDY")){
				user.setUserLevel("9");
			}
		}
		//按学年查询是否欠费
		if(StringUtil.hasValue(queryInfo.getYearArrears())){
			//TODO 后期再抽工具类
			String yearArrears  = queryInfo.getYearArrears();
			if(yearArrears.equals("1") || yearArrears.equals("2")){
				queryInfo.setItemYear("1");
			}else if(yearArrears.equals("3") || yearArrears.equals("4")){
				queryInfo.setItemYear("2");
			}else if(yearArrears.equals("5") || yearArrears.equals("6")){
				queryInfo.setItemYear("3");
			}else if(yearArrears.equals("7") || yearArrears.equals("8")){
				queryInfo.setItemYear("4");
			}
		}
		if(StringUtil.hasValue(queryInfo.getYearArrearsValue()) && StringUtil.hasValue(queryInfo.getFeeStandard())){
			if(queryInfo.getYearArrearsValue().contains("学期") && queryInfo.getFeeStandard().equals("1")){ //国开
				queryInfo.setIfSemester("Y");
			}else{
				queryInfo.setIfSemester("N");
			}
		}
		if(StringUtil.hasValue(queryInfo.getFeeStandard()) && StringUtil.isEmpty(queryInfo.getYearArrears())){
			queryInfo.setYearArrears("-3");
			if(queryInfo.getFeeStandard().equals("1")){
				queryInfo.setIfSemester("Y");
			}else{
				queryInfo.setIfSemester("N");
			}
			
		}
		if(StringUtil.isEmpty(queryInfo.getFeeStandard()) && StringUtil.hasValue(queryInfo.getYearArrears())){
			if(queryInfo.getYearArrearsValue().contains("学期")){ //国开
				queryInfo.setIfSemester("Y");
			}else{
				queryInfo.setIfSemester("N");
			}
		}
		
		List<StudentStudyingListInfo> list = studyingMapper.getStudyingListJoinAccNew(queryInfo, user);
		//String[] remarkTypes = {StudentConstants.REMARK_TYPE_WECHAT, StudentConstants.REMARK_TYPE_TELEPHONE,
		//		StudentConstants.REMARK_TYPE_QQ};
		for (StudentStudyingListInfo studyingInfo : list) {
			if (StringUtil.hasValue(queryInfo.getYearArrears())) {
				//String feeAmount = studyingMapper.getStudyingFeeAmountByLearnId(studyingInfo.getLearnId());
				String feeAmount = studyingMapper.getStuFeeAmountByLearnIdAndYear(studyingInfo.getLearnId(),queryInfo.getItemYear());
				if (StringUtil.isEmpty(feeAmount)) {
					feeAmount = "0.00";
				}
				studyingInfo.setFeeAmount(feeAmount);
			}else{
				String feeAmount = studyingMapper.getStudyingFeeAmountByLearnId(studyingInfo.getLearnId());
				if (StringUtil.isEmpty(feeAmount)) {
					feeAmount = "0.00";
				}
				studyingInfo.setFeeAmount(feeAmount);
			}
			
			//获取滞留账户信息
			AtsAccount account = new AtsAccount();
			account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
			account.setStdId(studyingInfo.getStdId());
			account = accountMapper.getAccount(account);
			String accAmount = "0.00";
			if (account != null) {
				accAmount = account.getAccAmount();
				if (StringUtil.isEmpty(accAmount)) {
					accAmount = "0.00";
				}
			}
			studyingInfo.setAccAmount(accAmount);

			// 查询备注
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("learnId", studyingInfo.getLearnId());
//			param.put("remarkTypes", remarkTypes);
//			List<BdLearnRemark> remarks = recruitMapper.findLearnRemark(param);
//			studyingInfo.setRemarkList(remarks);
		}
		return new IPageInfo<StudentStudyingListInfo>((Page<StudentStudyingListInfo>) list);
	}

	public IPageInfo<StudentStudyingListInfo> getStudyingList(StudentStudyingQuery queryInfo) {
		BaseUser user = SessionUtil.getUser();
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		
		//特殊处理 临时
		List<String> jtList = user.getJtList();
		if(jtList != null) {
			if(jtList.contains("GKSH")) {
				queryInfo.setRecruitType(StudentConstants.RECRUIT_TYPE_GK);
				user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);//1
			}
		}
		
		List<StudentStudyingListInfo> list = studyingMapper.getStudyingList(queryInfo, user);

		String[] remarkTypes = { StudentConstants.REMARK_TYPE_WECHAT, StudentConstants.REMARK_TYPE_TELEPHONE,
				StudentConstants.REMARK_TYPE_QQ };

		for (StudentStudyingListInfo studyingInfo : list) {
			//获取当前学年
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR)+1;
			int month = calendar.get(Calendar.MONTH)+1;
			if(month >= 11){
				year = year + 1;
			}
			//获取要收费的学年
			int itemYear = (year - Integer.parseInt(studyingInfo.getGrade()));

			List<BdSubOrder> subOrderInfos = subOrderMapper.getSubOrders(studyingInfo.getLearnId());
			BigDecimal paid = BigDecimal.ZERO;
			BigDecimal debt = BigDecimal.ZERO;
			for (BdSubOrder subOrder : subOrderInfos) {
				String subOrderStatus = subOrder.getSubOrderStatus();
				int subItemYear = "".equals(subOrder.getItemYear())? 0:Integer.parseInt(subOrder.getItemYear());
				if(subItemYear <= itemYear && ("1".equals(subOrder.getItemType()) || "2".equals(subOrder.getItemType()))){
					if (FinanceConstants.ORDER_STATUS_UNPAID.equals(subOrderStatus)) {
						debt = debt.add(AmountUtil.str2Amount(subOrder.getFeeAmount()));
					} else {
						paid = paid.add(AmountUtil.str2Amount(subOrder.getFeeAmount()));
					}
				}
			}

			studyingInfo.setPaid(paid.setScale(2, BigDecimal.ROUND_DOWN).toString());
			studyingInfo.setDebt(debt.setScale(2, BigDecimal.ROUND_DOWN).toString());

			//获取滞留账户信息
			AtsAccount account = new AtsAccount();
			account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
			account.setStdId(studyingInfo.getStdId());
			account = accountMapper.getAccount(account);
			String accAmount = "0.00";
			if (account != null) {
				accAmount = account.getAccAmount();
				if (StringUtil.isEmpty(accAmount)) {
					accAmount = "0.00";
				}
			}
			studyingInfo.setAccAmount(accAmount);

			// 查询备注
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("learnId", studyingInfo.getLearnId());
			param.put("remarkTypes", remarkTypes);
			List<BdLearnRemark> remarks = recruitMapper.findLearnRemark(param);
			studyingInfo.setRemarkList(remarks);
		}

		return new IPageInfo<StudentStudyingListInfo>((Page<StudentStudyingListInfo>) list);
	}

	/**
	 * 查询在读学员缴费明细
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, Object> getPaymentDetail(String learnId, String stdId) {
		List<StudyingPaymentInfo> paymentInfos = studyingMapper.getPaymentInfos(learnId);
		BigDecimal payableTotal = BigDecimal.ZERO;
		BigDecimal feeTotal = BigDecimal.ZERO;
		BigDecimal offerTotal = BigDecimal.ZERO;
		if (paymentInfos != null) {
			for (StudyingPaymentInfo paymentInfo : paymentInfos) {
				payableTotal = payableTotal.add(AmountUtil.str2Amount(paymentInfo.getPayable()));
				feeTotal = feeTotal.add(AmountUtil.str2Amount(paymentInfo.getFeeAmount()));
				offerTotal = offerTotal.add(AmountUtil.str2Amount(paymentInfo.getOfferAmount()));
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("paymentInfos", paymentInfos);
		result.put("payableTotal", payableTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());
		result.put("feeTotal", feeTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());
		result.put("offerTotal", offerTotal.setScale(2, BigDecimal.ROUND_DOWN).toString());
		// 查询滞留账户余额
		AtsAccount account = new AtsAccount();
		account.setStdId(stdId);
		account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		account = accountMapper.getAccount(account);

		String demurrage = null;

		if (account != null && StringUtil.hasValue(account.getAccAmount())) {
			demurrage = account.getAccAmount();
		}
		
		String grade = studyingMapper.selectGradeByLearnId(learnId);
		result.put("grade", grade);
		result.put("demurrage", demurrage);

		return result;
	}

	/**
	 * 获取在读学员联系信息
	 * 
	 * @param learnId
	 * @return
	 */
	public StudyingContactsShow getStudyingContacts(String learnId) {
		return studyingMapper.getStudyingContacts(learnId);
	}

	/**
	 * 更新在读学员联系信息
	 * 
	 * @param contacts
	 */
	public void updateContacts(StudyingContacts contacts) {
		boolean isDeleteFile = false;
		// 文件物理地址
		String realFilePath = null;
		boolean isUpdate = false;
		byte[] fileByteArray = null;
		String headPortrait = contacts.getHeadPortrait();

		if (GlobalConstants.TRUE.equals(contacts.getIsPhotoChange())) {
			Object headPic = contacts.getHeadPic();
			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) headPic;
				if (StringUtil.isEmpty(headPortrait)) {
					realFilePath = FileSrcUtil.createFileSrc(Type.STUDENT, contacts.getStdId(),
							file.getOriginalFilename());
				} else {
					realFilePath = headPortrait;
				}

				isUpdate = true;

				try {
					fileByteArray = file.getBytes();
				} catch (IOException e) {
					throw new SystemException("-------------------- 正在上传的文件的状态异常", e);
				}

				contacts.setHeadPortrait(realFilePath);
			} else {
				realFilePath = headPortrait;
				contacts.setHeadPortrait("");
				isDeleteFile = true;
			}
		}

		log.debug("------------------------ 文件路径：" + realFilePath);

		studyingMapper.updateContacts(contacts);

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}
		log.debug("---------------------------- 在读学员[" + contacts.getStdId() + "]联系方式更新成功");
	}
	
	public IPageInfo<StudentTaskFollowUp> getStudentServiceLog(int page,int pageSize,String learnId){
		PageHelper.offsetPage(page, pageSize);
		List<StudentTaskFollowUp> taskList = studentTaskFollowUpMapper.getStudentServiceLog(learnId);
		
		return new IPageInfo<>((Page<StudentTaskFollowUp>)taskList);
	}
	
	
	/**
	 * 新增业务备注状态
	 * 
	 * @param remarkInfo
	 */
	public void insertLearnRemarkLogs(BdLearnRemarkLog remarkInfo) {
		BaseUser user=SessionUtil.getUser();
		remarkInfo.setCreateTime(new Date());
		remarkInfo.setCreateUser(user.getUsername());
		remarkInfo.setCreateUserId(user.getUserId());
		remarkInfo.setLrId(IDGenerator.generatorId());
		recruitMapper.insertLearnRemarkLogs(remarkInfo);
	}

	/**
	 * 得到业务备注列表
	 * @param page
	 * @param pageSize
	 * @param learnId
	 * @return
	 */
	public IPageInfo<BdLearnRemarkLog> findLearnRemarkLog(int page,int pageSize,String learnId) {
		PageHelper.offsetPage(page, pageSize);
		List<BdLearnRemarkLog> remarklist=recruitMapper.findLearnRemarkLog(learnId);
		return new IPageInfo<>((Page<BdLearnRemarkLog>)remarklist);
	}
	
	/**
	 * 得到课程安排列表
	 * @param page
	 * @param pageSize
	 * @param makeSchedule
	 * @return
	 */
	public IPageInfo<Map<String, Object>> getStudentClassPlan(int page,int pageSize,MakeSchedule makeSchedule){
		BaseUser user=SessionUtil.getUser();
		//特殊处理临时
		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if(jtList.contains("CJZFDY")) { //成教组辅导员看到自己所带班级的在读学员数据
				user.setUserLevel("2");
			}else if(jtList.contains("GKZFDY")) { //国开班主任只能看到自己所带班级的待录取、已录取、注册学员、在读学员数据
				user.setUserLevel("3");
			}else if(jtList.contains("FDY")) { 
				user.setUserLevel("4");
			}else if(jtList.contains("CJXJ")||jtList.contains("GKXJ")||jtList.contains("GKSH")) {  //成教学籍组长能看到全部学员数据{成教在读学员，国开待录取、已录取、注册学员、在读学员数据}
				user.setUserLevel("1");
			}else if(jtList.contains("BMZR")) {//BMZR部门主管能看到全部数据（成教学籍组长+国开学籍组长）
				user.setUserLevel("1");
			}
		}
		PageHelper.offsetPage(page, pageSize);
		List<Map<String, Object>> classplanList = classplanMapper.findClassPlan(makeSchedule,user);
		if (null != classplanList) {
			
			for (Map<String, Object> bdCourse : classplanList) {
				int stdtCount=classplanMapper.stdtCount((String) bdCourse.get("courseId"),user);
				bdCourse.put("stdtCount",stdtCount );
			}
		}
		return new IPageInfo<>((Page<Map<String, Object>>)classplanList);
	}


	/**
	 * 得到课程专业列表
	 * @param page
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	public IPageInfo<Map<String, Object>> getStudentClassPlanPfsn(int page,int pageSize,String courseId){
		PageHelper.offsetPage(page, pageSize);
		List<Map<String, Object>> classplanList = classplanMapper.findUnvsPfsn(courseId);
		if (null != classplanList) {
			BaseUser user=SessionUtil.getUser();
			//特殊处理临时
			List<String> jtList = user.getJtList();
			if (jtList != null) {
				if(jtList.contains("CJZFDY")) { //成教组辅导员看到自己所带班级的在读学员数据
					user.setUserLevel("2");
				}else if(jtList.contains("GKZFDY")) { //国开班主任只能看到自己所带班级的待录取、已录取、注册学员、在读学员数据
					user.setUserLevel("3");
				}else if(jtList.contains("FDY")) { 
					user.setUserLevel("4");
				}else if(jtList.contains("CJXJ")||jtList.contains("GKXJ")||jtList.contains("GKSH")) {  //成教学籍组长能看到全部学员数据{成教在读学员，国开待录取、已录取、注册学员、在读学员数据}
					user.setUserLevel("1");
				}else if(jtList.contains("BMZR")) {//BMZR部门主管能看到全部数据（成教学籍组长+国开学籍组长）
					user.setUserLevel("1");
				}
			}

			for (Map<String, Object> bdCourse : classplanList) {
				int stdtCount=classplanMapper.stdtWithPfsnCount((String) bdCourse.get("pfsnId"),(String) bdCourse.get("grade"), user);
				bdCourse.put("stdtCount",stdtCount );
			}
		}
		return new IPageInfo<>((Page<Map<String, Object>>)classplanList);
	}


	/**
	 * 得到上课学员列表
	 * @param page
	 * @param pageSize
	 * @param pfsnId
	 * @param unvsId
	 * @param grade
	 * @return
	 */
	public IPageInfo<Map<String, Object>> getStudentInfoByPfsn(int page,int pageSize,String pfsnId,String unvsId,String grade){
		BaseUser user=SessionUtil.getUser();
		//特殊处理临时
		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if(jtList.contains("CJZFDY")) { //成教组辅导员看到自己所带班级的在读学员数据
				user.setUserLevel("2");
			}else if(jtList.contains("GKZFDY")) { //国开班主任只能看到自己所带班级的待录取、已录取、注册学员、在读学员数据
				user.setUserLevel("3");
			}else if(jtList.contains("FDY")) { 
				user.setUserLevel("4");
			}else if(jtList.contains("CJXJ")||jtList.contains("GKXJ")||jtList.contains("GKSH")) {  //成教学籍组长能看到全部学员数据{成教在读学员，国开待录取、已录取、注册学员、在读学员数据}
				user.setUserLevel("1");
			}else if(jtList.contains("BMZR")) {//BMZR部门主管能看到全部数据（成教学籍组长+国开学籍组长）
				user.setUserLevel("1");
			}
		}
		PageHelper.offsetPage(page, pageSize);
		List<Map<String, Object>> list = classplanMapper.findStudentClassPlan(pfsnId, unvsId, grade,user);
		
		return new IPageInfo<>((Page<Map<String, Object>>)list);
	}
	
	
	
	
}
