package com.yz.service.recruit;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.dao.recruit.StudentWhitelistMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.SendSmsVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.StudentRecruitQuery;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.finance.AtsAccount;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdFeeOffer;
import com.yz.model.recruit.BdLearnAnnex;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.recruit.BdRecruitStudentListInfo;
import com.yz.model.recruit.BdShoppingAddress;
import com.yz.model.recruit.BdStudentAnnex;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.model.system.SysDict;
import com.yz.redis.RedisService;
import com.yz.service.finance.BdOrderService;
import com.yz.service.oa.OaCampusService;
import com.yz.service.system.SysDictService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Transactional
@Service
public class StudentRecruitService {

	private static final Logger log = LoggerFactory.getLogger(StudentRecruitService.class);

	@Autowired
	private StudentRecruitMapper recruitMapper;

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private StudentAllService allService;

	@Autowired
	private BdStudentSendMapper sendMapper;

	@Autowired
	private StudentWhitelistMapper whitelistMapper;

	@Autowired
	private BdLearnAnnexMapper lAnnexMapper;

	@Autowired
	private BdStudentModifyService modifyService;

	@Autowired
	private YzSysConfig yzSysConfig ; 

	@Autowired
	private SysDictService sysDictService;

	/**
	 * 查询学员列表
	 * 
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<BdRecruitStudentListInfo> findRecruitStudents(StudentRecruitQuery queryInfo) {
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if (jtList.contains("BMZR")) { // 学服部门主任 辅导员
				user.setUserLevel("8");
			}
		}

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		
		List<BdRecruitStudentListInfo> list = recruitMapper.findRecruitStudents(queryInfo, user);

		String[] remarkType = { StudentConstants.REMARK_TYPE_CHARGE, StudentConstants.REMARK_TYPE_NOTIFY };

		for (BdRecruitStudentListInfo studentInfo : list) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("learnId", studentInfo.getLearnId());
			param.put("remarkTypes", remarkType);
			/*
			 * List<BdLearnRemark> remarks =
			 * recruitMapper.findLearnRemark(param);
			 * studentInfo.setRemarkList(remarks);
			 */
			AtsAccount account = new AtsAccount();
			account.setStdId(studentInfo.getStdId());
			account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
			account = accountMapper.getAccount(account);
			String amount = null;
			if (account == null || StringUtil.isEmpty(amount = account.getAccAmount())) {
				amount = "滞留账户信息异常";
			}

			studentInfo.setDemurrageAccount(amount);

			String learnId = studentInfo.getLearnId();

			List<Map<String, String>> subOrders = recruitMapper.getSubOrder(learnId);

			String counsellingfeesstatus = null;

			if (subOrders != null && subOrders.size() > 0) {
				for (Map<String, String> so : subOrders) {
					String itemCode = so.get("itemCode");
					String subOrderStatus = so.get("subOrderStatus");
					String payable = so.get("payable");

					if (FinanceConstants.FEE_ITEM_CODE_Y0.equals(itemCode)) {
						// 辅导费
						studentInfo.setCounsellingFees(payable);
						studentInfo.setCounsellingFeesStatus(subOrderStatus);
						counsellingfeesstatus = subOrderStatus;
					} else if (FinanceConstants.FEE_ITEM_CODE_Y1.equals(itemCode)) {
						studentInfo.setTuition(payable);
						studentInfo.setTuitionStatus(subOrderStatus);
					}
				}
			}

			if (FinanceConstants.ORDER_STATUS_PAID.equals(counsellingfeesstatus)) {
				BdStudentSend sendInfo = sendMapper.getRecordByLearnId(studentInfo.getLearnId(), null);
				if (sendInfo != null) {
					String orderBookStatus = sendInfo.getOrderBookStatus();
					studentInfo.setSendStatus(orderBookStatus);
				}
			}

			String annexStatus = studentInfo.getMyAnnexStatus();
			if (StudentConstants.ANNEX_STATUS_REJECT.equals(annexStatus)
					|| StudentConstants.ANNEX_STATUS_UNCHECK.equals(annexStatus)
					|| StudentConstants.ANNEX_STATUS_UNUPLOAD.equals(annexStatus)) {
				int count = lAnnexMapper.countBy(learnId, annexStatus);
				studentInfo.setAnnexCount(count);
			}
		}

		return new IPageInfo<BdRecruitStudentListInfo>((Page<BdRecruitStudentListInfo>) list);
	}

	/**
	 * 新增意向学员
	 * 
	 * @param baseInfo
	 * @param other
	 * @param recruit
	 * @param history
	 * @throws IOException
	 */
	public String recruit(BdStudentBaseInfo baseInfo, BdStudentOther other, BdStudentRecruit recruit,
			BdStudentHistory history) {

		// 如果报读为圆梦计划或者助学计划,需要检测学员是否有资质报名
		boolean canRecruit = check(baseInfo.getStdId(), recruit.getScholarship());
		if (!canRecruit)
			throw new BusinessException("E000065");

		// 每个学员只能有一条非退学学员报读记录，除非是应届专科毕业生。
		checkLearn(baseInfo.getStdId(), recruit.getGrade(), recruit.getPfsnLevel());

		initStudentInfo(baseInfo);

		other.setStdId(baseInfo.getStdId());
		// 如果为新增学员， 需要新增附属信息、附件信息、账户信息
		if (baseInfo.isNew()) {
			// 添加附属信息
			initStudentOther(other);
			// 初始化收货地址信息
			initStudentShoppingAddress(baseInfo);
		} else {
			allService.updateOther(other);
		}

		Map<String, String> param = new HashMap<String, String>();
		param.put("idType", baseInfo.getIdType());
		param.put("idCard", baseInfo.getIdCard());
		param.put("grade", recruit.getGrade());

		if (isExist(param)) {
			throw new BusinessException("E000066");
		}

		String recruitType = recruit.getRecruitType();

		// 初始化学业信息
		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setStdId(baseInfo.getStdId());
		learnInfo.setStdType(StudentConstants.STD_TYPE_LOCATION);
		learnInfo.setRecruitType(recruitType);
		learnInfo.setGrade(recruit.getGrade());
		learnInfo.setUpdateUser(baseInfo.getUpdateUser());
		learnInfo.setUpdateUserId(baseInfo.getUpdateUserId());
		learnInfo.setCreateUser(baseInfo.getCreateUser());
		learnInfo.setCreateUserId(baseInfo.getCreateUserId());
		learnInfo.setStdStage(StudentConstants.STD_STAGE_PURPOSE);
		learnInfo.setOfferId(recruit.getOfferId());
		learnInfo.setFeeId(recruit.getFeeId());
		learnInfo.setStdName(baseInfo.getStdName());
		learnInfo.setUnvsId(recruit.getUnvsId());
		learnInfo.setPfsnId(recruit.getPfsnId());
		learnInfo.setTaId(recruit.getTaId());

		String scholarship = recruit.getScholarship();
		SysDict dict = sysDictService.getDict("scholarship." + scholarship);
		String sg = dict.getExt1();// 优惠分组

		learnInfo.setSg(sg);
		recruit.setSg(sg);
		learnInfo.setScholarship(recruit.getScholarship());
		learnInfo.setIsOnline(GlobalConstants.FALSE);
		learnInfo.setEmpId(recruit.getEmpId());
		learnInfo.setEnrollChannel(StudentConstants.ENROLL_CHANNEL_LOCAL);
		learnInfo.setMobile(baseInfo.getMobile());

		// 检测资料是否已完善
		String isDataComplete = allService.isDataComplete(baseInfo, history, recruit, recruitType);
		learnInfo.setIsDataCompleted(isDataComplete);

		// 初始化学业信息
		initLearnInfo(learnInfo);

		String learnId = learnInfo.getLearnId();
		// 初始化报读信息
		recruit.setLearnId(learnId);
		recruit.setStdId(baseInfo.getStdId());
		initStudentEnroll(recruit);

		// 初始化学历信息
		history.setStdId(baseInfo.getStdId());
		history.setLearnId(learnId);
		initStudentHistory(history);

		// 初始化加分信息
		// initStuentBpRecords(recruit);

		// 初始化学员订单信息
		initStudentOrder(learnInfo);

		if (baseInfo.isNew()) {
			// 初始化账户信息
			initStudentAccount(baseInfo);
			log.info("=========学员:" + baseInfo.getStdName() + "账户信息初始化成功");
			// 添加学员变更记录
			if (!baseInfo.getStdSource().equals("D")) {
				modifyService.addStudentModifyRecord(learnId,
						"新增了意向学员：" + baseInfo.getStdName() + ",证件号码:" + baseInfo.getIdCard());
			}
		}
		return learnId;
	}

	/**
	 * 如果报读为圆梦计划或者助学计划,需要检测学员是否有资质报名
	 * 
	 * @param stdId
	 * @param scholarship
	 * @return
	 */
	public boolean check(String stdId, String scholarship) {
		boolean isNew = StringUtil.isEmpty(stdId);
		// 尚未在学员系统录入过的学员可以报读任何优惠活动
		if (isNew) {
			return true;
		}
		Assert.hasText(scholarship, "优惠类型不能为空");
		String dp = yzSysConfig.getPlanSetting();
		String[] discountPlan = dp.split(",");
		boolean needCheck = false;
		for (String _dp : discountPlan) {
			if (scholarship.equals(_dp)) {
				needCheck = true;
				break;
			}
		}

		if (needCheck) {
			// 是否在读
			boolean isStudying = false;
			// 是否报读过圆梦计划
			boolean isDiscountPlan = false;
			// 判断是否为新学员
			List<BdLearnInfo> learnInfo = recruitMapper.getLearnList(stdId);
			for (BdLearnInfo l : learnInfo) {
				if (GlobalConstants.FALSE.equals(l.getIsCompleted())) {
					isStudying = true;
				}
				for (String _dp : discountPlan) {
					if (_dp.equals(l.getScholarship())) {
						isDiscountPlan = true;
						break;
					}
				}
			}
			// 判断是否在读或者已结业并且报读过圆梦计划的
			if (isStudying || ((!isStudying) && isDiscountPlan)) {
				// 检测白名单
				int count = whitelistMapper.countBy(stdId, scholarship);
				if (count == 0)
					return false;
			}
		}
		return true;
	}

	/**
	 * 每个学员只能有一条非退学学员报读记录，除非是应届专科毕业生
	 * 
	 * @param stdId
	 * @return
	 */
	public void checkLearn(String stdId, String grade, String pfsnLevel) {
		if (StringUtil.hasValue(stdId)) { // 如果新录入学员不走检查是否报读
			// 判断是否为在读专科学员
			List<BdLearnInfo> learnInfo = recruitMapper.getLearnList(stdId);
			// 获取当前时间年
			// int year = Calendar.getInstance().get(Calendar.YEAR);

			// 根据3年判断在读学员是否可以在报读 先注释起来做记录 别特么又反悔
			// 报读年级
			/*
			 * int year = Integer.parseInt(grade.substring(0, 4));
			 * if(learnInfo!=null && learnInfo.size()>0){ for (BdLearnInfo
			 * l:learnInfo) { //获取当前学员在读学业是第几年 int subYear =
			 * (year-Integer.parseInt(l.getGrade().substring(0,4))); if
			 * (GlobalConstants.FALSE.equals(l.getIsCompleted()) &&
			 * !"10".equals(l.getStdStage())){ if(subYear>=3 &&
			 * "7".equals(l.getStdStage())){ if("1".equals(l.getPfsnLevel()) &&
			 * "5".equals(pfsnLevel)){ throw new BusinessException("E000104"); }
			 * } //在读状态 未毕业 if(subYear<3 && "7".equals(l.getStdStage())) throw
			 * new BusinessException("E000103"); //非在读状态
			 * if(!"7".equals(l.getStdStage())) throw new
			 * BusinessException("E000103"); } } }
			 */

			// 根据30个月判断在读学员是否可以在报读
			// 报读年级
			Date toDate = grade.length() == 4 ? DateUtil.convertDateStrToDate(grade, "yyyy")
					: DateUtil.convertDateStrToDate(grade, "yyyyMM");
			if (learnInfo != null && learnInfo.size() > 0) {
				for (BdLearnInfo l : learnInfo) {
					// 在读学业年级
					Date fromDate = l.getGrade().length() == 4 ? DateUtil.convertDateStrToDate(l.getGrade(), "yyyy")
							: DateUtil.convertDateStrToDate(l.getGrade(), "yyyyMM");
					// 获取当前读学业年级和报读年级相差多少个月
					int month = DateUtil.dateCompare(fromDate, toDate, "month");
					if (GlobalConstants.FALSE.equals(l.getIsCompleted()) && !"10".equals(l.getStdStage())) {
						if (month >= 30 && "7".equals(l.getStdStage())) {
							if ("1".equals(l.getPfsnLevel()) && "5".equals(pfsnLevel)) {
								throw new BusinessException("E000104");
							}
						}
						// 在读状态 未毕业
						if (month < 30 && "7".equals(l.getStdStage()))
							throw new BusinessException("E000103");
						// 非在读状态
						if (!"7".equals(l.getStdStage()))
							throw new BusinessException("E000103");
					}
				}
			}
		}
	}

	/**
	 * 初始化地址信息
	 * 
	 * @param baseInfo
	 */
	public void initStudentShoppingAddress(BdStudentBaseInfo baseInfo) {
		BdShoppingAddress saInfo = new BdShoppingAddress();
		saInfo.setAddress(baseInfo.getAddress());
		saInfo.setProvinceCode(baseInfo.getNowProvinceCode());
		saInfo.setCityCode(baseInfo.getNowCityCode());
		saInfo.setDistrictCode(baseInfo.getNowDistrictCode());
		saInfo.setIsDefault(GlobalConstants.TRUE);
		saInfo.setStdId(baseInfo.getStdId());
		saInfo.setUpdateUser(baseInfo.getUpdateUser());
		saInfo.setUpdateUserId(baseInfo.getUpdateUserId());
		saInfo.setSaId(IDGenerator.generatorId());
		recruitMapper.insertShoppingAddress(saInfo);

		log.debug("---------------------------- 学员[" + baseInfo.getStdId() + "]收货地址信息初始成功成功");
	}

	/**
	 * 初始化学员基础信息
	 * 
	 * @param baseInfo
	 * @return
	 */
	public void initStudentInfo(BdStudentBaseInfo baseInfo) {
		BdStudentBaseInfo o_baseInfo = recruitMapper.getStudentBaseInfo(baseInfo.getIdType(), baseInfo.getIdCard());

		if (o_baseInfo == null) {
			baseInfo.setStdId(IDGenerator.generatorId());
			recruitMapper.insertStudentBaseInfo(baseInfo);
			baseInfo.setNew(true);
			log.debug("---------------------------- 学员[" + baseInfo.getStdId() + "]基础信息插入成功");
		} else {
			baseInfo.setStdId(o_baseInfo.getStdId());
			String newAddress = StringUtil.string2Unicode(baseInfo.getNowProvinceCode() + baseInfo.getNowCityCode()
					+ baseInfo.getNowDistrictCode() + baseInfo.getAddress());
			String oldAddress = StringUtil.string2Unicode(o_baseInfo.getNowProvinceCode() + o_baseInfo.getNowCityCode()
					+ o_baseInfo.getNowDistrictCode() + o_baseInfo.getAddress());
			if (!newAddress.equals(oldAddress)) {
				baseInfo.setAddressEditTime("1"); // 变动了地址
			}
			recruitMapper.updateStudentBaseInfo(baseInfo);
			baseInfo.setNew(false);
			log.debug("---------------------------- 学员[" + baseInfo.getStdId() + "]基础信息已存在，信息更新成功");
		}

	}

	/**
	 * 初始化学员附属信息
	 * 
	 * @param other
	 * @throws IOException
	 */
	public void initStudentOther(BdStudentOther other) {
		// 上传图片
		Object headPic = other.getHeadPic();
		if (headPic != null && headPic instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) other.getHeadPic();

			String filePath = FileSrcUtil.createFileSrc(Type.STUDENT, other.getStdId(), file.getOriginalFilename());

			try {
				String bucket = yzSysConfig.getBucket();
				FileUploadUtil.upload(bucket, filePath, file.getBytes());
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}
			other.setHeadPortrait(filePath);
		}

		// 新增附属信息
		recruitMapper.insertStudentOtherInfo(other);

		log.debug("---------------------------- 学员[" + other.getStdId() + "]附属信息初始化成功");
	}

	/**
	 * 初始化学员附件信息
	 * 
	 * @param baseInfo
	 * @param other
	 */
	public void initStudentAnnex(BdStudentBaseInfo baseInfo, BdStudentOther other) {
		// 上传图片
		Object headPic = other.getHeadPic();
		if (headPic != null && headPic instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) other.getHeadPic();

			String filePath = FileSrcUtil.createFileSrc(Type.STUDENT, other.getStdId(), file.getOriginalFilename());

			try {
				String bucket = yzSysConfig.getBucket();
				FileUploadUtil.upload(bucket, filePath, file.getBytes());
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}
			other.setHeadPortrait(filePath);
		}

		String[] isRequire = new String[] { StudentConstants.ANNEX_TYPE_IDCARD_FRONT,
				StudentConstants.ANNEX_TYPE_IDCARD_BEHIND, StudentConstants.ANNEX_TYPE_EDUCATION };

		// 初始化附件信息
		List<SysDict> annexTypes = sysDictService.getDicts("annexType");
		if (annexTypes != null && !annexTypes.isEmpty()) {
			List<BdStudentAnnex> annexInfos = new ArrayList<BdStudentAnnex>();
			for (SysDict dict : annexTypes) {
				BdStudentAnnex annexInfo = new BdStudentAnnex();
				annexInfo.setStdId(baseInfo.getStdId());
				annexInfo.setIsRequire(dict.getExt1());
				String at = dict.getDictValue();

				for (String ir : isRequire) {
					if (ir.equals(at)) {
						annexInfo.setIsRequire(GlobalConstants.TRUE);
						break;
					}
				}

				annexInfo.setAnnexType(dict.getDictValue());
				annexInfo.setAnnexName(dict.getDictName());
				annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNUPLOAD);
				annexInfos.add(annexInfo);

				if (StudentConstants.ANNEX_TYPE_PHOTO.equals(dict.getDictValue())) {
					String fileUrl = other.getHeadPortrait();
					annexInfo.setAnnexUrl(fileUrl);
					if (StringUtil.hasValue(fileUrl)) {
						annexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_ALLOW);
						annexInfo.setUploadUser(other.getUpdateUser());
						annexInfo.setUploadUserId(other.getUpdateUserId());
						annexInfo.setUploadTime(new Date());
					}
					annexInfo.setAnnexId(IDGenerator.generatorId());
				}
			}
			recruitMapper.insertStudentAnnexInfos(annexInfos);
			log.debug("---------------------------- 学员[" + baseInfo.getStdId() + "]附件信息初始化成功");
		}
	}

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	/**
	 * 初始化学员账户信息
	 * 
	 * @param baseInfo
	 */
	public void initStudentAccount(BdStudentBaseInfo baseInfo) {
		String[] accTypes = new String[] { FinanceConstants.ACC_TYPE_DEMURRAGE, FinanceConstants.ACC_TYPE_NORMAL,
				FinanceConstants.ACC_TYPE_ZHIMI };
		accountApi.initAccount(null, baseInfo.getStdId(), null, accTypes);
	}

	/**
	 * 初始化学员学业信息
	 * 
	 * @param learnInfo
	 */
	public String initLearnInfo(BdLearnInfo learnInfo) {
		learnInfo.setLearnId(IDGenerator.generatorId());
		recruitMapper.initLearnInfo(learnInfo);
		log.debug("---------------------------- 学员[" + learnInfo.getStdId() + "]学业信息初始化成功");

		String learnId = learnInfo.getLearnId();

		Map<String, String> dpInfo = new HashMap<String, String>();

		if (StringUtil.hasValue(learnInfo.getEmpId())) {
			dpInfo = recruitMapper.getDepartmentInfo(learnInfo.getEmpId());
		} else {
			dpInfo = recruitMapper.getDepartmentInfo(SessionUtil.getUser().getEmpId());
		}

		BdLearnRules rulesInfo = new BdLearnRules();
		rulesInfo.setLearnId(learnInfo.getLearnId());
		if (dpInfo != null && !dpInfo.isEmpty()) {
			rulesInfo.setRecruit(dpInfo.get("empId"));
			rulesInfo.setRecruitDpId(dpInfo.get("dpId"));
			rulesInfo.setRecruitCampusId(dpInfo.get("campusId"));
			rulesInfo.setRecruitCampusManager(dpInfo.get("superintendent"));
		}
		// 插入数据权限
		recruitMapper.insertLearnRules(rulesInfo);

		log.debug("---------------------------- 学业[" + learnId + "]数据权限信息初始化成功");

		// 插入标签信息
		List<SysDict> remarkTypeList = sysDictService.getDicts("remarkType");

		List<BdLearnRemark> remarkList = new ArrayList<BdLearnRemark>();

		for (SysDict remarkType : remarkTypeList) {
			String value = remarkType.getDictValue();
			BdLearnRemark lr = new BdLearnRemark();

			lr.setUpdateUser(learnInfo.getUpdateUser());
			lr.setUpdateUserId(learnInfo.getUpdateUserId());
			lr.setRemarkType(value);
			lr.setLearnId(learnInfo.getLearnId());
			lr.setLrId(IDGenerator.generatorId());
			remarkList.add(lr);
		}

		recruitMapper.insertLearnRemarks(remarkList);
		log.debug("---------------------------- 学业[" + learnId + "]备注信息初始化成功");

		//获取招生类型  【1】成教 【2】国开
		String recruitType = learnInfo.getRecruitType();

		if("1".equals(recruitType)){
			// 插入考前资料审核记录
			StudentCheckRecord record = new StudentCheckRecord();
			record.setCrId(IDGenerator.generatorId());
			record.setMappingId(learnInfo.getLearnId());
			record.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
			record.setCheckOrder("1");// 审核阶段
			record.setCheckType(TransferConstants.CHECK_TYPE_DATA_CHECK);
			record.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
			recruitMapper.insertCheckRecord(record);
		}else{
			// 插入国开资料审核记录
			List<StudentCheckRecord> recordList = new ArrayList<>();
			String[] jtIdArray = {"GKZL1", "GKZL2", "GKZL3", "GKZL4"};
			for (int i=0; i<jtIdArray.length; i++){
				StudentCheckRecord record = new StudentCheckRecord();
				record.setCrId(IDGenerator.generatorId());
				record.setMappingId(learnInfo.getLearnId());
				record.setJtId(jtIdArray[i]);
				record.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				// 审核阶段
				if("GKZL1".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
				}else if ("GKZL2".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
				}else if("GKZL3".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
				}else if("GKZL4".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FOUR);
				}
				record.setCheckType(TransferConstants.CHECK_TYPE_DATA_GK);
				record.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				recordList.add(record);
			}
			recruitMapper.insertCheckRecordBatch(recordList);
		}

		// 初始化附件信息
		List<SysDict> annexTypeList = sysDictService.getDicts("annexType");

		List<String> isRequire = new ArrayList<String>();

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
			isRequire.add(StudentConstants.ANNEX_TYPE_EDUCATION);
			isRequire.add(StudentConstants.ANNEX_TYPE_IDCARD_FRONT);
			isRequire.add(StudentConstants.ANNEX_TYPE_IDCARD_BEHIND);
		} else {
			isRequire.add(StudentConstants.ANNEX_TYPE_PHOTO);
		}

		List<BdLearnAnnex> annexList = new ArrayList<BdLearnAnnex>();
		for (SysDict annexType : annexTypeList) {
			BdLearnAnnex annex = new BdLearnAnnex();
			String type = annexType.getDictValue();
			String name = annexType.getDictName();

			annex.setAnnexType(type);
			annex.setAnnexName(name);
			annex.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNUPLOAD);

			if (isRequire.contains(type)) {
				annex.setIsRequire(GlobalConstants.TRUE);
			} else {
				annex.setIsRequire(GlobalConstants.FALSE);
			}
			annex.setLearnId(learnId);
			annex.setAnnexId(IDGenerator.generatorId());

			annexList.add(annex);
		}

		lAnnexMapper.batchInsert(annexList);

		log.debug("---------------------------- 学业[" + learnId + "]附件信息初始化成功");

		return learnId;
	}

	/**
	 * 初始化学员报读信息
	 * 
	 * @param recruit
	 */
	public void initStudentEnroll(BdStudentRecruit recruit) {
		recruitMapper.insertStudentEnrolInfo(recruit);
		log.debug("---------------------------- 学员[" + recruit.getStdId() + "]报读信息初始化成功");
	}

	/**
	 * 初始化学员录取信息
	 * 
	 * @param recruit
	 */
	public void initStudentAdmin(BdStudentRecruit recruit) {
		recruitMapper.insertStudentAdmitInfo(recruit);
		log.debug("---------------------------- 学员[" + recruit.getStdId() + "]报读信息初始化成功");
	}

	/**
	 * 初始化学员加分信息
	 * 
	 * @param recruit
	 */
	public void initStuentBpRecords(BdStudentRecruit recruit) {
		recruitMapper.insertStudentBpRecords(recruit);
		log.debug("---------------------------- 学员[" + recruit.getStdId() + "]加分信息初始化成功");
	}

	/**
	 * 初始化学员学历信息
	 * 
	 * @param history
	 */
	public void initStudentHistory(BdStudentHistory history) {
		recruitMapper.insertStudentHistory(history);
		log.debug("---------------------------- 学员[" + history.getStdId() + "]原学历信息初始化成功");
	}

	@Autowired
	private BdOrderService orderService;

	@Autowired
	private OaCampusService campusService;

	/**
	 * 初始化学员订单信息
	 * 
	 * @param learnInfo
	 */
	public void initStudentOrder(BdLearnInfo learnInfo) {
		String financeCode = campusService.getFinanceCode(learnInfo.getCreateUserId());

		if (StringUtil.isEmpty(financeCode)) {
			financeCode = FinanceConstants.DEFAULT_FINANCE_CODE;
		}

		learnInfo.setFinanceCode(financeCode);

		orderService.initStudentOrder(learnInfo);
	}

	/**
	 * 如果已存在学员信息（已结业），则返回学员信息
	 *
	 * @param idType
	 * @param idCard
	 * @return
	 */
	public Object ifExistInfo(String idType, String idCard, String oldIdCard, String recruitType) {
		// 判断是否存在有学员信息存在
		int count = recruitMapper.getCountBy(idType, idCard, oldIdCard);

		if (count == 0) {
			return null;
		}

		BaseUser userInfo = SessionUtil.getUser();
		if (!userInfo.getUserLevel().equals("1")) {
			// 如果有学员信息存在,判断其招生老师是否在职
			int recruitCount = recruitMapper.getStuRecruitStatus(idType, idCard);
			if (recruitCount > 0) {
				// 验证已经存在的是否是招生关系
				int ifRecruit = recruitMapper.getStuCountByEmpId(idType, idCard, userInfo.getEmpId());
				Map<String, Object> result = new HashMap<String, Object>();
				if (ifRecruit == 0) {
					result.put("errCode", "E00098");
					return result;
				} else {
					if (recruitType.equals("1")) {
						result.put("errCode", "E00099");
						return result;
					}
				}
			}

		}

		BdStudentBaseInfo baseInfo = recruitMapper.getStudentBaseInfo(idType, idCard);
		BdStudentOther other = recruitMapper.getStudentOtherInfo(baseInfo.getStdId());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("baseInfo", baseInfo);
		result.put("other", other);
		return result;
	}

	/**
	 * 获取收费信息
	 * 
	 * @param pfsnId
	 * @param scholarship
	 * @param taId
	 * @param recruitType
	 * @return
	 */
	public Map<String, Object> findFeeOfferInfo(String pfsnId, String scholarship, String taId, String recruitType) {
		Map<String, Object> result = new HashMap<String, Object>();

		BdFeeOffer feeInfo = recruitMapper.findFeeOffer(pfsnId, scholarship, taId);
		result.put("feeList", null);
		result.put("feeTotal", "0.00");
		result.put("feeInfo", feeInfo);

		BigDecimal total = BigDecimal.ZERO;

		if (feeInfo != null) {

			List<BdFeeInfo> rList = new ArrayList<BdFeeInfo>();

			List<BdFeeInfo> feeList = recruitMapper.findFeeItem(recruitType);
			if (feeList != null) {
				for (BdFeeInfo fi : feeList) {
					BigDecimal payable = BigDecimal.ZERO;
					BigDecimal feeAmount = null;
					BigDecimal discount = null;

					BdFeeInfo info = recruitMapper.findFeeInfo(feeInfo.getFeeId(), feeInfo.getOfferId(),
							fi.getItemCode());
					if (info == null)
						continue;

					feeAmount = AmountUtil.str2Amount(info.getAmount());
					discount = AmountUtil.str2Amount(info.getDiscount());

					/*
					 * if (BigDecimal.ZERO.compareTo(feeAmount) == 0) continue;
					 */

					fi.setAmount(feeAmount.setScale(2, BigDecimal.ROUND_DOWN).toString());
					fi.setDiscount(discount.setScale(2, BigDecimal.ROUND_DOWN).toString());

					payable = feeAmount.subtract(discount);

					if (payable.compareTo(BigDecimal.ZERO) < 0) {
						log.error("-------------------------- 优惠大于收费，请检查收费信息_feeId : " + feeInfo.getFeeId() + ":"
								+ feeInfo.getFeeName() + " | offerId : " + feeInfo.getOfferId() + ":"
								+ feeInfo.getOfferName());
						payable = BigDecimal.ZERO;
					}

					fi.setPayable(payable.setScale(2, BigDecimal.ROUND_DOWN).toString());
					total = total.add(payable);

					rList.add(fi);
				}

				result.put("feeTotal", total.setScale(2, BigDecimal.ROUND_DOWN).toString());
			}

			result.put("feeList", rList);
		}

		return result;
	}

	/**
	 * 判断学员当年是否已注册
	 * 
	 * @param param
	 * @return
	 */
	public boolean isExist(Map<String, String> param) {
		int count = recruitMapper.countLearnBy(param);
		return count > 0;
	}

	/**
	 * 改变业务备注状态
	 * 
	 * @param remarkInfo
	 */
	public void changeRemark(BdLearnRemark remarkInfo) {
		recruitMapper.updateLearnRemark(remarkInfo);
	}

	/**
	 * 根据专业和考区获取收费标准类型
	 * 
	 * @param pfsnId
	 * @param taId
	 * @return
	 */
	public List<String> getScholarships(String pfsnId, String taId) {
		return recruitMapper.getScholarships(pfsnId, taId);
	}

	/**
	 * 201709 国开学员信息
	 * 
	 * @return
	 */
	public void sendSmsToStudent() {
		List<Map<String, String>> resultList = recruitMapper.getStuUnboundUserIdInfo("201803"); // recruitMapper.getStudentInfoForSendSMS();
		if (null != resultList && resultList.size() > 0) {
			for (Map<String, String> so : resultList) {
				String mobile = so.get("mobile");
				String stdName = so.get("stdName");
				Map<String, String> content = new HashMap<String, String>();
				content.put("sname", stdName);
				try {
					SendSmsVo vo = new SendSmsVo();
					vo.setContent(content);
					vo.setMobiles(mobile);
					vo.setTemplateId(GlobalConstants.TEMPLATE_MSG_NOTIFY);
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
				} catch (Exception e) {
					log.error(" ---------------- 学员[" + stdName + "] 手机号码为 [" + mobile + "] 发送失败", e);

				}
			}
		}
	}
	/**
	 * 验证手机号是否有学员
	 * @param mobile
	 * @return
	 */
	public int ifMobileExistInfo(String mobile){
		return recruitMapper.getStudentCountByMobile(mobile);
	}
}
