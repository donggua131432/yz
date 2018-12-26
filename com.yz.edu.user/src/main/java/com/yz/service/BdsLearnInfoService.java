package com.yz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.UsConstants;
import com.yz.core.util.IdCardUtil;
import com.yz.core.util.IdCardUtil.IdInfo;
import com.yz.dao.BdLearnAnnexMapper;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.BdsStudentMapper;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.exception.BusinessException;
import com.yz.model.AtsAccount;
import com.yz.model.SessionInfo;
import com.yz.model.UsBaseInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.session.AppSessionHolder;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdsLearnInfoService {

	private static final Logger log = LoggerFactory.getLogger(BdsLearnInfoService.class);

	@Autowired
	private BdsLearnMapper learnMapper;

	@Autowired
	private BdsOrderService orderService;

	@Autowired
	private BdLearnAnnexMapper lAnnexMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BdsStudentMapper studentMapper;
	
	@Autowired
	private UsBaseInfoMapper usBaseInfoMapper;

	public Object selectStudentInfo(String userId) {
		log.error("---------------------用户:" + userId + "在获取学业信息------------");
		Map<String, Object> rt = learnMapper.selectStudentInfo(userId);
		if (rt != null) {
			List<Map<String, String>> learnInfos = (List<Map<String, String>>) rt.get("learnInfos");
			for (Map<String, String> map : learnInfos) {
				int paidCount = learnMapper.selectPaidCount((String) map.get("learnId"));
				String hasSerial = paidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;

				int unpaidCount = learnMapper.selectUnpaidCount((String) map.get("learnId"));
				String hasUnpaid = unpaidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;
				map.put("hasSerial", hasSerial);
				map.put("hasUnpaid", hasUnpaid);
			}
		}

		return rt;
	}

	public Map<String, String> addLearnInfo(Body body) {
		// 判断学员是否存在
		boolean isNew = checkStudent(body);

		// 解析中国公民身份证信息
		resolveIdCard(body);

		if (!isNew) {
			check(body);// 报名限制
		} else {
			addStudentInfo(body);// 新增学员信息 并将stdId 存入body
			addStudentOther(body);// 新增学员附属信息
			// addStudentAnnex(body);// 新增学员附件信息
		}

		addStudentLearnInfo(body);// 新增学业信息 并将learnId 存入body
		addStudentHistory(body);// 新增学历信息
		addStudentEnroll(body);// 新增报读信息
		addStduentOrder(body);// 新增缴费订单信息
		addBind(body);// 添加绑定关系

		if (isNew) {
			// 初始化学员账户
			// accountApi.initAccount(body.getString("userId"),
			// body.getString("stdId"), null,
			// new String[] { FinanceConstants.ACC_TYPE_DEMURRAGE,
			// FinanceConstants.ACC_TYPE_NORMAL,
			// FinanceConstants.ACC_TYPE_ZHIMI });

			// 调用本地方法
			String userId = body.getString("userId");
			String stdId = body.getString("stdId");
			String empId = null;
			String[] accTypes = new String[] { FinanceConstants.ACC_TYPE_DEMURRAGE, FinanceConstants.ACC_TYPE_NORMAL,
					FinanceConstants.ACC_TYPE_ZHIMI };

			Assert.isTrue(StringUtil.hasValue(userId) || StringUtil.hasValue(stdId) || StringUtil.hasValue(empId),
					"用户、学员、员工ID至少有一个不为空");
			Assert.notNull(accTypes, "账户类型不能为空");

			List<AtsAccount> accountList = new ArrayList<AtsAccount>();

			for (String accType : accTypes) {
				AtsAccount account = new AtsAccount();

				switch (accType) {
				case FinanceConstants.ACC_TYPE_DEMURRAGE:
					account.setCanDeposit(GlobalConstants.FALSE);
					account.setIsVisiable(GlobalConstants.TRUE);
					account.setUnit(FinanceConstants.ACC_UNIT_RMB);
					break;
				case FinanceConstants.ACC_TYPE_NORMAL:
					account.setCanDeposit(GlobalConstants.TRUE);
					account.setIsVisiable(GlobalConstants.TRUE);
					account.setUnit(FinanceConstants.ACC_UNIT_RMB);
					break;
				case FinanceConstants.ACC_TYPE_ZHIMI:
					account.setCanDeposit(GlobalConstants.FALSE);
					account.setIsVisiable(GlobalConstants.TRUE);
					account.setUnit(FinanceConstants.ACC_UNIT_ZHIMI);
					break;
				}

				account.setEmpId(empId);
				account.setUserId(userId);
				account.setStdId(stdId);
				account.setAccType(accType);
				account.setAccStatus(FinanceConstants.ACC_STATUS_NORMAL);

				accountList.add(account);
			}

			accountService.initAccount(accountList);

		}

		Map<String, String> result = new HashMap<String, String>();

		result.put("stdId", body.getString("stdId"));
		result.put("learnId", body.getString("learnId"));

		return result;
	}

	/**
	 * 检测学员是否存在
	 * 
	 * @param body
	 * @return
	 */
	private boolean checkStudent(Body body) {
		String certNo = body.getString("idCard");
		String certType = UsConstants.CERT_TYPE_SFZ;
		String stdId = body.getString("stdId");

		Map<String, String> studentInfo = null;

		boolean isNew = false;

		if (StringUtil.hasValue(stdId)) {
			studentInfo = learnMapper.getStudentInfoById(stdId);
			if (studentInfo == null)
				throw new BusinessException("E60019"); // 学员信息不存在
			String idCard = studentInfo.get("idCard");
			// 原身份证信息与报读填入的身份证信息不匹配
			// if(!idCard.equals(certNo)) throw new BusinessException("E60029");
			body.putAll(studentInfo);
		} else {
			studentInfo = learnMapper.getStudentInfo(certNo, certType);
			if (studentInfo == null || StringUtil.isEmpty(studentInfo.get("stdId"))) {
				isNew = true;
			} else {
				// throw new BusinessException("E60004");
				isNew = false;
				body.putAll(studentInfo);
			}
		}

		return isNew;
	}

	/**
	 * 解析身份证信息
	 * 
	 * @param body
	 */
	private void resolveIdCard(Body body) {
		String idCard = body.getString("idCard");
		// 身份证获取信息
		IdInfo info = IdCardUtil.convert(idCard);
		body.put("sex", info.getSex());
		body.put("age", info.getAge());
		body.put("birthday", info.getBirthday());
		body.put("idType", StudentConstants.ID_TYPE_SFZ);
	}

	private void addBind(Body body) {
		String stdId = body.getString("stdId");
		String userId = body.getString("userId");

		SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
		String pType = session.getpType();
		String pIsMb = session.getpIsMb();
		String userType = session.getUserType();
		String pId = session.getPid();

		UsBaseInfo usBaseInfo = new UsBaseInfo();
		usBaseInfo.setUserId(userId);
		usBaseInfo.setStdId(stdId);
		usBaseInfo.setpType(pType);
		usBaseInfo.setpIsMb(pIsMb);
		usBaseInfo.setUserType(userType);
		usBaseInfo.setpId(pId);
		
		/*String existUserId = studentMapper.getUserIdByStdId(stdId);
		if (StringUtil.hasValue(existUserId)) {
			throw new BusinessException("E60011");
		}*/
		//更新绑定关系
		studentMapper.updateUserIdByStdId(userId,stdId);
		
		usBaseInfoMapper.updateByPrimaryKeySelective(usBaseInfo);

	}

	private void addStduentOrder(Body body) {
		orderService.initStudentOrder(body);
	}

	private void addStudentEnroll(Body body) {
		int age = body.getInt("age");
		if (age > 25) {
			body.put("bpType", StudentConstants.BP_TYPE_25);
			body.put("points", "20");
		} else {
			body.put("bpType", StudentConstants.BP_TYPE_NONE);
			body.put("points", "0");
		}

		learnMapper.addStudentEnroll(body);
	}

	private void addStudentHistory(Body body) {
		learnMapper.addStudentHistory(body);
	}

	private void addStudentLearnInfo(Body body) {

		int count = learnMapper.countGrade(body.getString("stdId"), body.getString("grade"));

		if (count > 0) {
			log.error("--------------------------- 学员[" + body.getString("stdId") + "] " + body.get("grade")
					+ "年级，已有报读记录");
			throw new BusinessException("E60010");// 当前年级已有报读记录
		}

		Map<String, String> feeInfo = learnMapper.selectFeeInfo(body.getString("pfsnId"), body.getString("taId"),
				body.getString("scholarship"));

		if (feeInfo == null || feeInfo.isEmpty()) {
			throw new BusinessException("E60003");
		}

		body.put("stdType", StudentConstants.STD_TYPE_LOCATION);
		body.put("stdStage", StudentConstants.STD_STAGE_PURPOSE);
		body.put("feeId", feeInfo.get("feeId"));
		body.put("offerId", feeInfo.get("offerId"));
		body.put("isOnline", GlobalConstants.TRUE);

		learnMapper.addStudentLearnInfo(body);

		String learnId = body.getString("learnId");

		log.debug("---------------------------- 学员[" + body.getString("stdId") + "]学业[" + learnId + "]信息初始化成功");

		// 插入标签信息
		learnMapper.insertLearnRemarks(learnId, "remarkType");
		log.debug("---------------------------- 学业[" + learnId + "]备注信息初始化成功");

		// 插入考前资料审核记录
		Map<String, String> record = new HashMap<String, String>();
		record.put("checkType", TransferConstants.CHECK_TYPE_DATA_CHECK);
		record.put("mappingId", learnId);
		record.put("checkOrder", "1");
		record.put("crStatus", TransferConstants.CHECK_RECORD_STATUS_CHECKING);
		record.put("checkStatus", TransferConstants.CHECK_RECORD_STATUS_CHECKING);

		learnMapper.insertCheckRecord(record);
		log.debug("---------------------------- 学业[" + learnId + "]考前核查记录初始化成功");

		// 关联招生老师
		String recruitId = body.getString("recruitId");
		Map<String, String> empInfo = new HashMap<String, String>();

		if (StringUtil.hasValue(recruitId)) {
			// 查询招生老师
			Map<String, String> ei = learnMapper.selectEmpInfo(recruitId);

			if (ei != null && ei.size() > 0) {
				empInfo.putAll(ei);
			}
		}

		empInfo.put("learnId", learnId);
		log.debug("---------------------------- 学业[" + learnId + "]学员数据权限初始化成功");

		learnMapper.insertLearnRule(empInfo);

		// 初始化附件信息
		// List<SysDict> annexTypeList = sysDictService.getDicts("annexType");

		String recruitType = body.getString("recruitType");
		lAnnexMapper.batchInsert(learnId, recruitType);

		log.debug("---------------------------- 学业[" + learnId + "]附件信息初始化成功");
	}


	/**
	 * 添加学员订单
	 * 
	 * @param body
	 */
	private void addStudentOther(Body body) {
		learnMapper.addStudentOther(body);

		log.debug("---------------------------- 学员[" + body.getString("stdId") + "]附属信息初始化成功");
	}

	/**
	 * 添加学员基础信息
	 * 
	 * @param body
	 */
	@SuppressWarnings("unchecked")
	private void addStudentInfo(Body body) {
		body.put("stdSource", "W");
		learnMapper.addStudentInfo(body);

		log.debug("---------------------------- 学员[" + body.getString("stdId") + "]信息初始化成功");
	}

	/**
	 * 检测学员是否可报名
	 * 
	 * @param body
	 */
	private void check(Body body) {
		String scholarship = body.getString("scholarship");
		String stdId = body.getString("stdId");
		String pfsnLevel = body.getString("pfsnLevel");

		List<Map<String, String>> learnInfo = learnMapper.getLearnList(stdId);

		// 报读年级
		int grade = Integer.parseInt(body.getString("grade").substring(0, 4));
		// 获取当前时间年
		// int year = Calendar.getInstance().get(Calendar.YEAR);
		for (Map<String, String> l : learnInfo) {
			String isCompleted = l.get("isCompleted");
			String pfsnLvl = l.get("pfsn_level");
			String stdStage = l.get("std_stage");
			// 获取当前学员在读学业是第几年
			int subYear = (grade - Integer.parseInt(l.get("grade").substring(0, 4)));
			// 每个学员只能有一条非退学学员报读记录，除非是应届专科毕业生。
			if (GlobalConstants.FALSE.equals(isCompleted) && !"10".equals(stdStage)) {
				if (subYear >= 3 && "7".equals(stdStage)) {
					if ("1".equals(pfsnLvl) && "5".equals(pfsnLevel)) {
						throw new BusinessException("E60031");
					}
				}
				// 在读状态 未毕业
				if (subYear < 3 && "7".equals(stdStage))
					throw new BusinessException("E60030");
				// 非在读状态
				if (!"7".equals(stdStage))
					throw new BusinessException("E60030");
			}
		}

		// 需要检测的优惠活动
		String dp = yzSysConfig.getPlanSetting();
		String[] discountPlan = dp.split(",");
		// 是否需要白名单检测
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
			for (Map<String, String> l : learnInfo) {
				String isCompleted = l.get("isCompleted");
				String lScholarship = l.get("scholarship");
				if (GlobalConstants.FALSE.equals(isCompleted)) {
					isStudying = true;
				}
				for (String _dp : discountPlan) {
					if (_dp.equals(lScholarship)) {
						isDiscountPlan = true;
						break;
					}
				}
			}
			// 判断是否在读 或者 已结业并且报读过圆梦计划的
			if (isStudying || ((!isStudying) && isDiscountPlan)) {
				// 检测白名单
				int count = learnMapper.countWhiteListBy(stdId, scholarship);
				if (count == 0) {
					throw new BusinessException("E60001");
				}
			}
		}
	}

	public Map<String, String> getLearnStatus(String userId) {
		Map<String, String> result = new HashMap<String, String>();

		List<String> learnIds = learnMapper.selectLearnIdByUserId(userId);

		// 判断是否报读
		if (learnIds.isEmpty()) {
			result.put("ticketStatus", "1");
			return result; // 尚未报读
		}

		// 判断是否缴纳考前辅导费
		for (String learnId : learnIds) {

			String recruitType = learnMapper.selectRecruitTypeByLearnId(learnId);

			String itemCode = "Y0";
			if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				itemCode = "Y1";
			}

			// 找出是否有未缴费订单
			int tutionUnpaidCount = learnMapper.selectUnpaidCountByItemCode(learnId, itemCode);

			if (tutionUnpaidCount > 0) {
				result.put("ticketStatus", "2"); // 尚未缴费，去缴费
				result.put("learnId", learnId);
				return result;
			}
		}
		result.put("ticketStatus", "3");
		return result; // 邀约报读
	}

	/**
	 * 获取具体的学业信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 */
	public Object getLearnInfoByLearnId(Header header, Body body) {
		String userId = header.getUserId();
		String learnId = body.getString("learnId");
		log.error("---------------------用户:" + userId + "正在获取" + learnId + "的学业信息------------");

		Map<String, String> learnMap = learnMapper.selectLearnInfoByLearnId(learnId);
		if (null != learnMap) {
			int paidCount = learnMapper.selectPaidCount(learnId);
			String hasSerial = paidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;

			int unpaidCount = learnMapper.selectUnpaidCount(learnId);
			String hasUnpaid = unpaidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;
			learnMap.put("hasSerial", hasSerial);
			learnMap.put("hasUnpaid", hasUnpaid);
		}
		return learnMap;
	}

}
