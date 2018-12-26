package com.yz.service.recruit;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.oa.OaEmployeeBaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.yz.dao.educational.BdStudentEScoreMapper;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.educational.BdStudentTScoreMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.exception.SystemException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.PageCondition;
import com.yz.model.condition.recruit.AllStudentQuery;
import com.yz.model.educational.BdStudentEScore;
import com.yz.model.educational.BdStudentTScore;
import com.yz.model.oa.OaEmpFollowInfo;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdFeeOffer;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentEnroll;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.model.recruit.RecruitEmployeeInfo;
import com.yz.model.recruit.StudentAllListInfo;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.model.recruit.StudentFeeListInfo;
import com.yz.model.recruit.StudentModify;
import com.yz.model.recruit.StudentModifyListInfo;
import com.yz.model.system.SysDict;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.us.UsFollow;
import com.yz.service.system.SysDictService;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StudentAllService {

	private static final Logger log = LoggerFactory.getLogger(StudentAllService.class);

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private StudentRecruitMapper recruitMapper;

	@Autowired
	private SysDictService sysDictService;


	@Autowired
	private YzSysConfig yzSysConfig ; 

	/**
	 * 查询全部学员列表信息
	 * 
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<StudentAllListInfo> findAllStudent(AllStudentQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<StudentAllListInfo> list = stdAllMapper.findAllStudent(queryInfo);
		return new IPageInfo<StudentAllListInfo>((Page<StudentAllListInfo>) list);
	}

	/**
	 * 查询全部学员列表信息
	 * @return
	 */
	public IPageInfo<StudentAllListInfo> searchStudent(AllStudentQuery queryInfo) {
		List<StudentAllListInfo> list = null;
		if (StringUtil.hasValue(queryInfo.getStdName()) || StringUtil.hasValue(queryInfo.getMobile())
				|| StringUtil.hasValue(queryInfo.getIdCard()) || StringUtil.hasValue(queryInfo.getRecruitName())) {

			PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
			list = stdAllMapper.findAllStudent(queryInfo);
			return new IPageInfo<StudentAllListInfo>((Page<StudentAllListInfo>) list);
		} else {

			return new IPageInfo<StudentAllListInfo>();
		}
	}

	/**
	 * 查询学员基础信息
	 * 
	 * @param stdId
	 * @return
	 */
	public BdStudentBaseInfo getStudentBaseInfo(String stdId) {
		BaseUser user = SessionUtil.getUser();

		boolean isMine = false;
		int count = stdAllMapper.isMine(stdId, user); // 首先判断是否是自己
		if (count > 0) {
			isMine = true;
		} else {
			if (GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
				isMine = true;
			} else if (GlobalConstants.USER_LEVEL_DEPARTMENT.equals(user.getUserLevel())) {
				// 看下属离职招生老师的学员信息时放开
				count = stdAllMapper.getStuRecruitStatus(stdId);
				if (count == 0) {
					isMine = true;
				}
			} else if (GlobalConstants.USER_LEVEL_GROUP.equals(user.getUserLevel())) {
				// 看下属离职招生老师的学员信息时放开
				count = stdAllMapper.getStuRecruitStatus(stdId);
				if (count == 0) {
					isMine = true;
				}
			} else if (GlobalConstants.USER_LEVEL_XQZL.equals(user.getUserLevel())) {
				// 判断招生老师是否在职
				count = stdAllMapper.getStuRecruitStatus(stdId);
				if (count == 0) {
					isMine = true;
				}
			} else if (GlobalConstants.USER_LEVEL_XJZL.equals(user.getUserLevel())) {
				// 判断招生老师是否在职
				count = stdAllMapper.getStuRecruitStatus(stdId);
				if (count == 0) {
					isMine = true;
				}
			} else if (GlobalConstants.USER_LEVEL_400.equals(user.getUserLevel())) {
				//400专员
				isMine = true;
			}
		}

		BdStudentBaseInfo result = stdAllMapper.getStudentBaseInfo(stdId);
		if (result == null)
			return new BdStudentBaseInfo();

		if (!isMine) {
			String idCard = result.getIdCard();
			String mobile = result.getMobile();

			String s_idCard = StringUtil.format(idCard, "434");
			String s_mobile = StringUtil.format(mobile, "434");

			result.setS_idCard(s_idCard);
			result.setS_mobile(s_mobile);
		} else {
			result.setS_idCard(result.getIdCard());
			result.setS_mobile(result.getMobile());
		}
		return result;
	}

	/**
	 * 审核模块基本信息获取
	 * @param stdId
	 * @return
	 */
	public BdStudentBaseInfo getStudentBaseInfoAnnex(String stdId) {
		BdStudentBaseInfo result = stdAllMapper.getStudentBaseInfo(stdId);
		//隐藏手机号码
		result.setS_mobile(StringUtil.format(result.getMobile(), "434"));
		result.setS_idCard(result.getIdCard());
		return result;
	}

	/**
	 * 查询学员附属信息
	 * 
	 * @param stdId
	 * @return
	 */
	public BdStudentOther getStudentOther(String stdId) {
		BdStudentOther result = stdAllMapper.getStudentOther(stdId);
		if (result == null)
			return new BdStudentOther();
		return result;
	}

	/**
	 * 更新基础信息
	 * 
	 * @param baseInfo
	 */
	public void updateBaseInfo(BdStudentBaseInfo baseInfo, String recruitType) {
		BdStudentBaseInfo stuBaseInfo =stdAllMapper.getStudentBaseInfo(baseInfo.getStdId());
		String newAddress = StringUtil.string2Unicode(baseInfo.getNowProvinceCode()+baseInfo.getNowCityCode()+baseInfo.getNowDistrictCode()+baseInfo.getAddress());
		String oldAddress = StringUtil.string2Unicode(stuBaseInfo.getNowProvinceCode()+stuBaseInfo.getNowCityCode()+stuBaseInfo.getNowDistrictCode()+stuBaseInfo.getAddress());
		if(!newAddress.equals(oldAddress)){
			baseInfo.setAddressEditTime("1"); //变动了地址
		}
		//防止修改的时候修改到相同的身份证
		if(StringUtil.hasValue(baseInfo.getIdCard()) && StringUtil.hasValue(stuBaseInfo.getIdCard())
				&& !baseInfo.getIdCard().equals(stuBaseInfo.getIdCard())){
			//当修改了身份证的时候,根据新身份证去查询信息
			int count = studentRecruitMapper.getCountBy(null, baseInfo.getIdCard(), null);
			if(count >0){
				throw new BusinessException("E000200");
			}
		}
		
		stdAllMapper.updateBaseInfo(baseInfo);

		BdStudentHistory history = stdAllMapper.getStudentHistory(baseInfo.getLearnId());

		checkDataCompleted(baseInfo, history, recruitType);
		updateBookSendAddress(baseInfo);
	}

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	/**
	 * 修改发书审核中、驳回状态的地址
	 * 
	 * @param baseInfo
	 */
	public void updateBookSendAddress(BdStudentBaseInfo baseInfo) {
		// 修改收货地址处理
		if (StringUtil.hasValue(baseInfo.getNowProvinceCode()) && StringUtil.hasValue(baseInfo.getNowCityCode())
				&& StringUtil.hasValue(baseInfo.getAddress())) {
			// 修改地址审核状态 驳回改为待审核
			studentSendMapper.updateSendAddressStatus(baseInfo);
		}
	}

	/**
	 * 更新附属信息
	 * 
	 * @param other
	 */
	public void updateOther(BdStudentOther other) {
		boolean isDeleteFile = false;
		boolean isUpdate = false;
		String realFilePath = null;
		byte[] fileByteArray = null;
		String headPortrait = other.getHeadPortrait();

		if (GlobalConstants.TRUE.equals(other.getIsPhotoChange())) {
			Object headPic = other.getHeadPic();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) other.getHeadPic();

				if (StringUtil.isEmpty(headPortrait)) {
					realFilePath = FileSrcUtil.createFileSrc(Type.STUDENT, other.getStdId(),
							file.getOriginalFilename());
				} else {
					realFilePath = headPortrait;
				}

				try {
					fileByteArray = file.getBytes();
				} catch (IOException e) {
					throw new SystemException(e.getMessage(), e);
				}
				isUpdate = true;
				other.setHeadPortrait(realFilePath);

			} else {
				realFilePath = headPortrait;
				other.setHeadPortrait("");
				isDeleteFile = true;
			}
		}

		stdAllMapper.updateOtherInfo(other);

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

		log.debug("---------------------------- 学员[" + other.getStdId() + "]附属信息更新成功");
	}

	/**
	 * 更新学历信息
	 * 
	 * @param history
	 */
	public void updateHistory(BdStudentHistory history, String recruitType) {
		BdStudentHistory historyResult = stdAllMapper.getStudentHistory(history.getLearnId());
		if(historyResult!=null){
			stdAllMapper.updateHistory(history);
		}else{
			recruitMapper.insertStudentHistory(history);
		}

		BdStudentBaseInfo baseInfo = stdAllMapper.getStudentBaseInfoByLearnId(history.getLearnId());

		checkDataCompleted(baseInfo, history, recruitType);
		checkTestCompleted(baseInfo, history);
	}

	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Autowired
	private StudentRecruitService recruitService;

	/**
	 * 更新报读信息
	 * 
	 * @param recruitInfo
	 */
	public void updateRecruit(BdStudentRecruit recruitInfo) {
		// 如果有做修改
		if (!checkUpdate(recruitInfo)) {
			sutdentModify(recruitInfo);
		}
		stdAllMapper.updateRecruit(recruitInfo);
	}

	private void sutdentModify(BdStudentRecruit recruitInfo) {
		// 修改后收费标准ID
		String feeId = studentModifyMapper.selectFeeStandard(recruitInfo.getPfsnId(), recruitInfo.getTaId(),
				recruitInfo.getScholarship());

		if (!StringUtil.hasValue(feeId)) {
			throw new BusinessException("E000077"); // 无收费标准
		}

		// 如果收费标准变更

		String offerId = studentModifyMapper.selectOfferId(recruitInfo.getPfsnId(), recruitInfo.getTaId(),
				recruitInfo.getScholarship());

		// 修改学业阶段为意向学员，废弃订单，重新初始化
		studentModifyMapper.updateStdStageByLearnId(recruitInfo.getLearnId());

		String stdId = stdAllMapper.selectStdIdByLearnId(recruitInfo.getLearnId());
		
		// 废弃订单
		studentModifyMapper.destroyOrderByLearnId(recruitInfo.getLearnId());

		recruitInfo.setStdId(stdId);
		BdStudentBaseInfo std = stdAllMapper.getStudentBaseInfo(stdId);

		BdLearnInfo learnInfo = new BdLearnInfo();
		BaseUser user = SessionUtil.getUser();
		learnInfo.setCreateUser(user.getRealName());
		learnInfo.setCreateUserId(user.getUserId());
		learnInfo.setFeeId(feeId);
		learnInfo.setMobile(std.getMobile());
		learnInfo.setStdName(std.getStdName());
		learnInfo.setStdId(std.getStdId());
		learnInfo.setUpdateUser(user.getRealName());
		learnInfo.setUpdateUserId(user.getUserId());
		learnInfo.setPfsnId(recruitInfo.getPfsnId());
		learnInfo.setUnvsId(recruitInfo.getUnvsId());
		learnInfo.setTaId(recruitInfo.getTaId());
		learnInfo.setFeeId(feeId);
		learnInfo.setOfferId(offerId);
		learnInfo.setRecruitType(recruitInfo.getRecruitType());
		learnInfo.setStdStage(StudentConstants.STD_STAGE_PURPOSE);
		learnInfo.setLearnId(recruitInfo.getLearnId());
		recruitService.initStudentOrder(learnInfo);
		

		recruitInfo.setFeeId(feeId);
		recruitInfo.setOfferId(offerId);
		stdAllMapper.updateBdStudentEnroll(recruitInfo);
		stdAllMapper.updateBdLearnInfo(recruitInfo);

	}

	private boolean checkUpdate(BdStudentRecruit recruitInfo) {
		Map<String, String> learnInfo = stdAllMapper.getEnrollInfo(recruitInfo.getLearnId());

		if (!StringUtil.hasValue(recruitInfo.getUnvsId()) || !StringUtil.hasValue(recruitInfo.getPfsnId())
				|| !StringUtil.hasValue(recruitInfo.getScholarship()) || !StringUtil.hasValue(recruitInfo.getTaId())) {
			return true;
		}

		String amount = studentModifyMapper.selectPaidAmountByLearnId(recruitInfo.getLearnId());
		if (BigDecimal.ZERO.compareTo(AmountUtil.str2Amount(amount)) < 0) {
			throw new BusinessException("E000105"); // 学员已缴费，无法修改
		}

		boolean repeatFlag = true;
		if (StringUtil.hasValue(recruitInfo.getUnvsId()) && !learnInfo.get("unvsId").equals(recruitInfo.getUnvsId())) {
			repeatFlag = false;
		}
		if (!learnInfo.get("pfsnId").equals(recruitInfo.getPfsnId())) {
			repeatFlag = false;
		}
		if (!recruitInfo.getScholarship().equals(learnInfo.get("scholarship"))) {
			repeatFlag = false;
		}
		if (!recruitInfo.getTaId().equals(learnInfo.get("taId"))) {
			repeatFlag = false;
		}

		return repeatFlag;
	}

	/**
	 * 获取学历信息
	 * 
	 * @param learnId
	 * @return
	 */
	public BdStudentHistory getStudentHistory(String learnId) {
		BdStudentHistory history = stdAllMapper.getStudentHistory(learnId);
		if (history == null)
			return new BdStudentHistory();
		return history;
	}

	/**
	 * 获取学员报读信息
	 * 
	 * @param learnId
	 * @return
	 */
	public BdStudentEnroll getStudentEnroll(String learnId) {
		BdStudentEnroll enroll = stdAllMapper.getStudentEnroll(learnId);
		if (enroll == null)
			return new BdStudentEnroll();
		return enroll;
	}

	/**
	 * 获取报读的缴费信息
	 * 
	 * @param learnId
	 * @return
	 */
	public Map<String, Object> getFeeInfo(String learnId) {
		Map<String, Object> result = new HashMap<String, Object>();
		BdFeeOffer feeInfo = stdAllMapper.findFeeOffer(learnId);
		result.put("feeList", null);
		result.put("feeTotal", "0.00");
		result.put("feeInfo", feeInfo);

		BigDecimal total = BigDecimal.ZERO;

		if (feeInfo != null) {
			List<BdFeeInfo> feeList = stdAllMapper.findFeeItem(feeInfo);
			if (feeList != null) {
				for (BdFeeInfo fi : feeList) {
					BigDecimal payable = BigDecimal.ZERO;
					String amount = "0.00";
					String discount = "0.00";

					if (StringUtil.hasValue(fi.getAmount())) {
						amount = fi.getAmount();
					} else {
						fi.setAmount(amount);
					}

					if (StringUtil.hasValue(fi.getDiscount())) {
						discount = fi.getDiscount();
					} else {
						fi.setDiscount(discount);
					}

					payable = AmountUtil.str2Amount(amount).subtract(AmountUtil.str2Amount(discount));

					if (payable.compareTo(BigDecimal.ZERO) < 0) {
						log.error("-------------------------- 优惠大于收费，请检查收费信息_feeId : " + feeInfo.getFeeId() + ":"
								+ feeInfo.getFeeName() + " | offerId : " + feeInfo.getOfferId() + ":"
								+ feeInfo.getOfferName());
						payable = BigDecimal.ZERO;
					}

					fi.setPayable(payable.setScale(2, BigDecimal.ROUND_DOWN).toString());

					total = total.add(payable);
				}

				result.put("feeTotal", total.setScale(2, BigDecimal.ROUND_DOWN).toString());
			}

			result.put("feeList", feeList);
		}

		return result;

	}

	/**
	 * 获取报读的招生信息
	 * 
	 * @param learnId
	 * @return
	 */
	public RecruitEmployeeInfo getRecruitEmpInfo(String learnId) {
		RecruitEmployeeInfo recruitEmpInfo = stdAllMapper.getRecruitEmpInfo(learnId);
		if (recruitEmpInfo == null)
			return new RecruitEmployeeInfo();
		return recruitEmpInfo;
	}

	/**
	 * 查询学员缴费信息
	 * 
	 * @param learnId
	 * @return
	 */
	public IPageInfo<StudentFeeListInfo> getFeeList(String learnId) {
		List<StudentFeeListInfo> list = stdAllMapper.getFeeList(learnId);
		if (list != null && !list.isEmpty()) {
			return new IPageInfo<StudentFeeListInfo>(list, list.size());
		}
		return new IPageInfo<StudentFeeListInfo>(new ArrayList<StudentFeeListInfo>(), 0);
	}

	@Autowired
	private BdStudentEScoreMapper eScoreMapper;

	/**
	 * 查询学员入学成绩
	 * 
	 * @param learnId
	 * @return
	 */
	public List<BdStudentEScore> getEScoreList(String learnId) {
		return eScoreMapper.findStudentScore(learnId);
	}

	/**
	 * 查询学员考试成绩
	 */
	@Autowired
	private BdStudentTScoreMapper tScoreMapper;

	public List<BdStudentTScore> getTScoreList(String learnId) {
		return tScoreMapper.findStudentScore(learnId);
	}

	/**
	 * 查询变更记录
	 * 
	 * @param studentModifyInfo
	 * @return
	 */
	public IPageInfo<StudentModifyListInfo> getModifyList(StudentModifyListInfo studentModifyInfo, PageCondition pc) {
		PageHelper.offsetPage(pc.getStart(), pc.getLength());
		Map<String, String> map = new HashMap<>();
		map.put("learnId",studentModifyInfo.getLearnId());
		map.put("modifyType",studentModifyInfo.getModifyType());
		List<StudentModify> list = stdAllMapper.getModifyList(map);
		List<StudentModifyListInfo> listInfo = new ArrayList<StudentModifyListInfo>();
		if (list == null || list.isEmpty()) {
			return new IPageInfo<StudentModifyListInfo>(listInfo, 0);
		}

		Page<StudentModify> page = (Page<StudentModify>) list;
		for (StudentModify modify : list) {

			StudentModifyListInfo li = new StudentModifyListInfo();

			li.setLearnId(modify.getLearnId());
			li.setModifyId(modify.getModifyId());
			li.setModifyType(modify.getModifyType());
			li.setStdId(modify.getStdId());
			li.setIsComplete(modify.getIsComplete());
			li.setStdName(modify.getStdName());
			li.setCreateUser(
					StringUtil.hasValue(modify.getUpdateUser()) ? modify.getUpdateUser() : modify.getCreateUser());
			li.setCreateTime(
					StringUtil.hasValue(modify.getUpdateTime()) ? modify.getUpdateTime() : modify.getCreateTime());
			StringBuffer modifyText = new StringBuffer();

			String stdName = modify.getStdName();
			String sex = modify.getSex();
			String idType = modify.getIdType();
			String idCard = modify.getIdCard();
			String nation = modify.getNation();
			String unvsId = modify.getUnvsId();
			String pfsnId = modify.getPfsnId();
			String taId = modify.getTaId();
			String scholarship = modify.getScholarship();
			String pfsnLevel = modify.getPfsnLevel();
			String oldstdStage = modify.getOldStdStage();

			String newStdName = modify.getNewStdName();
			String newSex = modify.getNewSex();
			String newIdType = modify.getNewIdType();
			String newIdCard = modify.getNewIdCard();
			String newNation = modify.getNewNation();
			String newUnvsId = modify.getNewUnvsId();
			String newPfsnId = modify.getNewPfsnId();
			String newTaId = modify.getNewTaId();
			String newScholarship = modify.getNewScholarship();
			String newPfsnLevel = modify.getNewPfsnLevel();
			String newStdStage = modify.getNewStdStage();
			String ext1 = modify.getExt1();
			String ext2 = modify.getExt2();

			if (StringUtil.hasValue(newStdName) && StringUtil.hasValue(stdName) && !stdName.equals(newStdName)) {
				modifyText.append("姓名：").append(stdName).append(" ==> ").append(newStdName).append("<br/>");
			}

			if (StringUtil.hasValue(newSex) && StringUtil.hasValue(sex) && !sex.equals(newSex)) {
				SysDict dict = sysDictService.getDict("sex." + sex);
				SysDict newDict = sysDictService.getDict("sex." + newSex);
				String newValue = newDict == null ? "" : newDict.getDictName();

				modifyText.append("性别：").append(dict.getDictName()).append(" ==> ").append(newValue).append("<br/>");
			}

			if (StringUtil.hasValue(newIdType) && StringUtil.hasValue(idType) && !idType.equals(newIdType)) {
				SysDict dict = sysDictService.getDict("idType." + idType);
				SysDict newDict = sysDictService.getDict("idType." + newIdType);
				String newValue = newDict == null ? "" : newDict.getDictName();

				modifyText.append("证件类型：").append(dict.getDictName()).append(" ==> ").append(newValue).append("<br/>");
			}

			if (StringUtil.hasValue(newIdCard) && StringUtil.hasValue(idCard) && !idCard.equals(newIdCard)) {
				modifyText.append("身份证：").append(idCard).append(" ==> ").append(newIdCard).append("<br/>");
			}

			if (StringUtil.hasValue(newNation) && StringUtil.hasValue(nation) && !nation.equals(newNation)) {
				SysDict dict = sysDictService.getDict("nation." + nation);
				SysDict newDict = sysDictService.getDict("nation." + newNation);
				String newValue = newDict == null ? "" : newDict.getDictName();

				modifyText.append("民族：").append(dict.getDictName()).append(" ==> ").append(newValue).append("<br/>");
			}

			if (StringUtil.hasValue(newScholarship) && StringUtil.hasValue(scholarship)
					&& !scholarship.equals(newScholarship)) {
				SysDict dict = sysDictService.getDict("scholarship." + scholarship);
				SysDict newDict = sysDictService.getDict("scholarship." + newScholarship);
				String newValue = newDict == null ? "" : newDict.getDictName();

				modifyText.append("优惠类型：").append(dict.getDictName()).append(" ==> ").append(newValue).append("<br/>");
			}

			if (StringUtil.hasValue(newUnvsId) && StringUtil.hasValue(unvsId) && !unvsId.equals(newUnvsId)) {
				modifyText.append("院校：").append(modify.getUnvsName()).append(" ==> ").append(modify.getNewUnvsName())
						.append("<br/>");
			}

			if (StringUtil.hasValue(newPfsnId) && StringUtil.hasValue(pfsnId) && !pfsnId.equals(newPfsnId)) {
				modifyText.append("专业：").append(modify.getPfsnName()).append(" ==> ").append(modify.getNewPfsnName()).append("<br/>");
			}

			if (StringUtil.hasValue(pfsnLevel) && StringUtil.hasValue(newPfsnLevel) && !pfsnLevel.equals(newPfsnLevel)) {
				SysDict oldDict = sysDictService.getDict("pfsnLevel." + pfsnLevel);
				SysDict newDict = sysDictService.getDict("pfsnLevel." + newPfsnLevel);
				modifyText.append("专业层次：").append(oldDict.getDictName()).append("==>").append(newDict.getDictName())
						.append("<br/>");
			}

			if (StringUtil.hasValue(newTaId) && !taId.equals(newTaId)) {
				modifyText.append("考区：").append(modify.getTaName()).append(" ==> ").append(modify.getNewTaName())
						.append("<br/>");
			}

			if (StringUtil.hasValue(newStdStage) && StringUtil.hasValue(newStdStage)
					&& !oldstdStage.equals(newStdStage)) {
				SysDict oldStageName = sysDictService.getDict("stdStage." + oldstdStage);
				SysDict newStageName = sysDictService.getDict("stdStage." + newStdStage);
				modifyText.append("学员状态：").append(oldStageName.getDictName()).append(" ==> ")
						.append(newStageName.getDictName()).append("<br/>");
			}
			if (StringUtil.hasValue(ext1)) {
				modifyText.append(ext1);
			}

			if (StringUtil.hasValue(ext2)) {
				modifyText.append(ext2);
			}

			li.setModifyText(modifyText.toString());

			listInfo.add(li);
		}

		return new IPageInfo<StudentModifyListInfo>(listInfo, page.getTotal());

	}

	/**
	 * 获取考前资料审核状态
	 * 
	 * @param learnId
	 */
	public List<StudentCheckRecord> getCheckDataStatus(String learnId) {
		return stdAllMapper.getCheckDataStatus(learnId);

		//return null;
	}

	/**
	 * 获取毕业审核状态
	 * 
	 * @param learnId
	 */
	public List<StudentCheckRecord> getGraduateStatus(String learnId) {
		return stdAllMapper.getGraduateStatus(learnId);
	}

	/**
	 * 是否完善学员资料
	 * 
	 * @param baseInfo
	 * @param history
	 * @param recruit
	 * @return
	 */
	public String isDataComplete(BdStudentBaseInfo baseInfo, BdStudentHistory history, BdStudentRecruit recruit,
			String recruitType) {
		if (StudentDataCheckUtil.isComplete(baseInfo, recruitType)
				&& StudentDataCheckUtil.isComplete(recruit, recruitType)
				&& StudentDataCheckUtil.isComplete(history, recruitType))
			return GlobalConstants.TRUE;
		return GlobalConstants.FALSE;
	}

	@Autowired
	private StudentRecruitMapper studentRecruitMapper;

	@Autowired
	private UsFollowMapper usFollowMapper;


	@Autowired
	private OaEmployeeMapper employeeMapper;
	/**
	 * 更新学员隶属老师
	 * 
	 * @param learnRules
	 */
	public void updateLearnRules(BdLearnRules learnRules) {
		int count = recruitMapper.countLearnRules(learnRules.getLearnId());
		String learnId = learnRules.getLearnId();
		//设置为学校分配
		//learnRules.setAssignFlag("1");
		if (count == 0) {
			recruitMapper.insertLearnRules(learnRules);
		} else {
			recruitMapper.updateLearnRules(learnRules);
		}

		//如果是分配招生老师就刷新跟进人
		String recruit = learnRules.getRecruit();
		if(recruit!=null && !"".equals(recruit)){
			//更新用户信息的跟进人
			//根据学业ID查找userId
			String userId = studentRecruitMapper.getUserIdByLearnId(learnId);
			if(userId != null && !"".equals(userId)){
				UsFollow usFollow = usFollowMapper.selectByPrimaryKey(userId);

				//获取招生老师所属部门校区信息
				OaEmpFollowInfo oaEmpFollowInfo = employeeMapper.getEmpFollowInfo(recruit);
				UsFollow usFollowParam = new UsFollow();
				usFollowParam.setUserId(userId);
				usFollowParam.setEmpId(oaEmpFollowInfo.getEmpId());
				usFollowParam.setDpId(oaEmpFollowInfo.getDpId());
				usFollowParam.setDpManager(oaEmpFollowInfo.getDpManager());
				usFollowParam.setCampusId(oaEmpFollowInfo.getCampusId());
				usFollowParam.setCampusManager(oaEmpFollowInfo.getCampusManager());
				usFollowParam.setAssignFlag(learnRules.getAssignFlag());
				usFollowParam.setAssignTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//分配时间

				if(usFollow == null){
					usFollowMapper.insertSelective(usFollowParam);
				}else{
					usFollowMapper.updateByPrimaryKeySelective(usFollowParam);
				}
			}
			String ext1 = "分配了招生老师:"+employeeMapper.getEmpBaseInfo(recruit).getEmpName();
			addStudentModefy(learnId,ext1,TransferConstants.MODIFY_TYPE_CHANGE_RECRUIT_7);
		}else{
			String tutor = learnRules.getTutor();
			if(StringUtil.hasValue(tutor)){
			  	OaEmployeeBaseInfo oaEmployeeBaseInfo = employeeMapper.getEmpBaseInfo(tutor);
			  	if(oaEmployeeBaseInfo != null){
					String ext1 = "分配了辅导员:"+oaEmployeeBaseInfo.getEmpName();
					addStudentModefy(learnId,ext1,TransferConstants.MODIFY_TYPE_CHANGE_AssignInfo_4);
				}
			}
		}

	}

	//插入学员变更记录
	public void addStudentModefy(String learnId, String ext1, String modifyType) {
		//根据学业Id获取学员ID
		String stdId = stdAllMapper.selectStdIdByLearnId(learnId);
		BdStudentModify studentModify=new BdStudentModify();
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_NEW_MODIFY);
		// 变更类型为新生信息修改
		studentModify.setModifyType(modifyType);
		studentModify.setCheckOrder("1");
		studentModify.setLearnId(learnId);
		studentModify.setStdId(stdId);
		studentModify.setExt1(ext1);
		studentModify.setIsComplete("1");
		BaseUser user=SessionUtil.getUser();
		if(null != user){
			studentModify.setCreateUser(user.getUsername());
			studentModify.setCreateUserId(user.getUserId());
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		//添加变更记录
		studentModifyMapper.insertSelective(studentModify);
	}

	/**
	 * 判断学员信息是否已完善
	 * 
	 * @param baseInfo
	 * @param history
	 */
	private void checkDataCompleted(BdStudentBaseInfo baseInfo, BdStudentHistory history, String recruitType) {
		if (baseInfo == null || history == null)
			return;

		String learnId = history.getLearnId();

		String isDataCompleted = GlobalConstants.FALSE;

		if (StudentDataCheckUtil.isComplete(baseInfo, recruitType)
				&& StudentDataCheckUtil.isComplete(history, recruitType)) {
			isDataCompleted = GlobalConstants.TRUE;
		}

		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setIsDataCompleted(isDataCompleted);
		learnInfo.setLearnId(learnId);

		stdAllMapper.updateIsDataCompleted(learnInfo);
	}

	/**
	 * 判断学员考试信息是否完善
	 * 
	 * @param baseInfo
	 * @param history
	 */
	public void checkTestCompleted(BdStudentBaseInfo baseInfo, BdStudentHistory history) {

		if (baseInfo == null || history == null)
			return;

		String isTestCompleted = GlobalConstants.FALSE;

		if (StudentDataCheckUtil.isTestCompleted(history)
				&& (StudentConstants.ANNEX_STATUS_UNCHECK.equals(baseInfo.getMyAnnexStatus())
						|| StudentConstants.ANNEX_STATUS_ALLOW.equals(baseInfo.getMyAnnexStatus()))) {
			isTestCompleted = GlobalConstants.TRUE;
		}

		BdLearnInfo learnInfo = new BdLearnInfo();
		learnInfo.setIsTestCompleted(isTestCompleted);
		learnInfo.setLearnId(history.getLearnId());

		stdAllMapper.updateIsDataCompleted(learnInfo);
	}

	/**
	 * 是否包含意向学员
	 * 
	 * @param stdId
	 * @return
	 */
	public String getIsSuppose(String stdId) {
		int count = stdAllMapper.selectIsSuppose(stdId);
		if (count > 0) { // 是
			return "1";
		} else { // 否
			return "0";
		}
	}

}
