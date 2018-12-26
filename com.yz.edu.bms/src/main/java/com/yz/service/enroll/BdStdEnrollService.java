package com.yz.service.enroll;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.api.AtsAccountApi;
import com.yz.constants.EnrollConstants;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.enroll.BdStdRegisterMapper;
import com.yz.dao.finance.BdCouponMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.exception.IRpcException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.enroll.stdenroll.BdStdAdmit;
import com.yz.model.enroll.stdenroll.BdStdEnroll;
import com.yz.model.enroll.stdenroll.BdStdEnrollImport;
import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;
import com.yz.model.enroll.stdenroll.BdStdEnrolled;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.coupon.BdStdCoupon;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.system.SysDict;
import com.yz.service.SysErrorMessageService;
import com.yz.service.finance.BdOrderService;
import com.yz.service.system.SysDictService;
import com.yz.util.AmountUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStdEnrollService {
	
	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private BdStdEnrollMapper enrollMapper;

	@Autowired
	private BdOrderService orderService;

	@Autowired
	private BdStdPayFeeMapper payMapper;

	@Autowired
	private BdCouponMapper couponMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;
	
	@Autowired
	private BdStdCampusAsynService campusAsynService;
	
	@Autowired
	private SysErrorMessageService sysErrorMessageService ;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object queryStdEnrollByPage(int start, int length, BdStdEnrollQuery enroll) {
		PageHelper.offsetPage(start, length);
		List<BdStdEnroll> enrolls = enrollMapper.selectStdEnrollByPage(enroll);
		return new IPageInfo((Page) enrolls);
	}

	public Object queryStdEnrollInfo(String learnId) {
		return enrollMapper.selectStdEnrollInfoByLearnId(learnId);
	}

	public void studentEnroll(String learnId, String unvsId, String pfsnId) {
		BaseUser user = SessionUtil.getUser();

		BdLearnInfo learn = new BdLearnInfo();
		payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_ENROLLED);
		BdLearnInfo learnInfo = enrollMapper.selectLearnInfoByLearnId(learnId);

		List<BdCoupon> coupons = couponMapper.selectAvailableScoreCoupon(learnId);
		BdStdAdmit admit = new BdStdAdmit();
		admit.setCreateUser(user.getRealName());
		admit.setCreateUserId(user.getUserId());
		admit.setExamNum(learnInfo.getExamNum());
		admit.setGrade(learnInfo.getGrade());
		admit.setIsAdmit(EnrollConstants.ENROLL_ADMITED);
		admit.setLearnId(learnId);
		admit.setPfsnId(pfsnId);
		// TODO 临时加入优惠分组 后续可以从页面带过来
		String scholarship = learnInfo.getScholarship();
		SysDict dict = sysDictService.getDict("scholarship." + scholarship);
		String sg = dict.getExt1();// 优惠分组
		admit.setScholarship(scholarship);
		admit.setSg(sg);

		admit.setStdId(learnInfo.getStdId());

		admit.setTaId(learnInfo.getTaId());
		admit.setUnvsId(unvsId);
		admit.setUpdateUser(user.getRealName());
		admit.setUpdateUserId(user.getUserId());
		enrollMapper.insertBdAdmit(admit);

		String feeId = enrollMapper.selectFeeIdByLearnId(learnId);

		if (!StringUtil.hasValue(feeId)) {
			//throw new BusinessException("E000048"); // 此专业与考区暂无收费标准
			throw new CustomException("学员："+learnInfo.getStdName()+"此专业与考区暂无收费标准");
		}
		learnInfo.setFeeId(feeId);

		String tutorPaidTime = null;

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learnInfo.getRecruitType())) {
			// 辅导费缴纳时间
			tutorPaidTime = enrollMapper.selectTutorPaidTime(learnId);
		}

		// 默认查找入围状态为待定
		String offerId = enrollMapper.selectOfferIdByLearnId(learnId, tutorPaidTime,
				StudentConstants.INCLUSION_STATUS_HOLD);
		if (StringUtil.hasValue(offerId)) {
			learnInfo.setOfferId(offerId);
		} else {
			learnInfo.setOfferId(null);
		}

		learn.setCreateUser(user.getRealName());
		learn.setCreateUserId(user.getUserId());
		learn.setFinanceCode(learnInfo.getFinanceCode());
		learn.setLearnId(learnId);
		learn.setMobile(learnInfo.getMobile());
		learn.setStdName(learnInfo.getStdName());
		learn.setStdId(learnInfo.getStdId());
		learn.setUpdateUser(user.getRealName());
		learn.setUpdateUserId(user.getUserId());
		learn.setUnvsId(unvsId);
		learn.setPfsnId(pfsnId);
		learn.setTaId(learnInfo.getTaId());
		learn.setFeeId(learnInfo.getFeeId());
		learn.setOfferId(learnInfo.getOfferId());
		learn.setRecruitType(learnInfo.getRecruitType());
		learn.setStdStage(learnInfo.getStdStage());

		BigDecimal refundAmount = null;
		
		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learn.getRecruitType())) {
			refundAmount = checkSubjectOrder(learnId);
			// 初始化订单
			orderService.initStudentOrder(learn);
		}

		// 判断能否领取优惠券
		String stdScoreStr = enrollMapper.selectStdScore(learnId);
		if (StringUtil.hasValue(stdScoreStr)) {

			if (coupons != null && coupons.size() > 0) {
				for (BdCoupon coupon : coupons) {

					BdStdCoupon stdCoupon = new BdStdCoupon();
					stdCoupon.setCouponId(coupon.getCouponId());
					stdCoupon.setStdId(learnInfo.getStdId());
					String[] scores = coupon.getTriggerContent().split(FeeConstants.COUPON_TRIGGER_PREFIX);
					double minScore = Double.parseDouble(scores[0]);
					double maxScore = Double.parseDouble(scores[1]);

					double stdScore = Double.parseDouble(stdScoreStr);
					if (stdScore >= minScore && stdScore <= maxScore) {
						stdCoupon.setScId(IDGenerator.generatorId());
						couponMapper.insertStdCoupon(stdCoupon);
					}
				}
			}
		}

		if (null != refundAmount && refundAmount.compareTo(BigDecimal.ZERO) > 0) {
			// 设置转账对象
			Body body = new Body();
			body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
			body.put("stdId", learnInfo.getStdId());
			body.put("amount", refundAmount.toString());
			body.put("action", FinanceConstants.ACC_ACTION_IN);
			body.put("excDesc", "学员录取，"+learnInfo.getGrade()+"年级已缴学科费用退款");
			body.put("mappingId", learnId);
			try {
				accountApi.trans(body);
			}catch (Exception e) {
				if (e instanceof BusinessException) {
					BusinessException be = (BusinessException) e;
					String errorCode = be.getErrCode();
					String systemErrorMsg= sysErrorMessageService.getSysErrorMsg(errorCode);
					throw new CustomException("学员："+learnInfo.getStdName()+"录取不成功，原因："+systemErrorMsg);
				}else {
					throw new IRpcException(e);
				}
				
			}
			
		}
	}

	public BigDecimal checkSubjectOrder(String learnId) {
		BigDecimal amount = BigDecimal.ZERO;
		// 是否已经生成学科订单
		int subjectOrderCount = enrollMapper.selectSubjectOrderCount(learnId);

		if (subjectOrderCount > 0) { // 已有学科订单
			// 查询是否有已缴费订单
			int subjectPaidCount = enrollMapper.selectSubjectPaidCount(learnId);

			if (subjectPaidCount > 0) { // 有已缴费学科订单，退还滞留账户

				String paidAmount = enrollMapper.selectSubjectPaidAmount(learnId);

				if (StringUtil.hasValue(paidAmount)
						&& AmountUtil.str2Amount(paidAmount).compareTo(BigDecimal.ZERO) > 0) {
					amount = AmountUtil.str2Amount(paidAmount);
				}

			}
			// 已支付修改为已退款
			enrollMapper.updateSubjectOrderStatus(learnId, FinanceConstants.ORDER_STATUS_REFUND,
					FinanceConstants.ORDER_STATUS_PAID);
			// 未支付的订单改为废弃
			enrollMapper.updateSubjectOrderStatus(learnId, FinanceConstants.ORDER_STATUS_WASTE,
					FinanceConstants.ORDER_STATUS_UNPAID);

		}

		return amount;
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object queryEnrolledByPage(int start, int length, BdStdEnrollQuery enroll) {
		PageHelper.offsetPage(start, length);
		List<BdStdEnrolled> enrolls = enrollMapper.selectStdEnrolledByPage(enroll);
		return new IPageInfo((Page) enrolls);
	}

	public void studentEnrollUpdate(String learnId, String unvsId, String pfsnId,String campusId, String taId, String scholarship) {

		/*
		 * BaseUser user = SessionUtil.getUser();
		 * 
		 * // 查询录取志愿是否更改 int isUpdate =
		 * enrollMapper.selectEnrolledInfoExist(learnId, unvsId, pfsnId, taId,
		 * scholarship);
		 * 
		 * if (isUpdate > 0) { // 没有修改 return return; }
		 * 
		 * BdLearnInfo learnInfo =
		 * enrollMapper.selectLearnInfoByLearnId(learnId, pfsnId, taId,
		 * scholarship); String[] amountArray =
		 * enrollMapper.selectPaidItem(learnId); String amount = "0.00"; if
		 * (amountArray.length > 0) { // 已缴费 需退费至滞留账户 for (String amt :
		 * amountArray) { amount = BigDecimalUtil.add(amount, amt); }
		 * if(AmountUtil.str2Amount(amount).compareTo(BigDecimal.ZERO) > 0){ //
		 * 缴纳总金额大于0 BdAccountSerial accSerial = new BdAccountSerial();
		 * accSerial.setStdId(learnInfo.getStdId());
		 * accSerial.setAction(FinanceConstants.ACC_ACTION_IN);
		 * accSerial.setAmount(amount);
		 * accSerial.setMappingId(learnInfo.getLearnId());
		 * accSerial.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		 * 
		 * accSerial.setCreateUser(user.getRealName());
		 * accSerial.setCreateUserId(user.getUserId());
		 * accSerial.setUpdateUser(user.getRealName());
		 * accSerial.setUpdateUserId(user.getUserId());
		 * accountService.updateAccount(accSerial); // 划账到滞留账户 } }
		 * 
		 * // 删除已录取的子订单、录取信息
		 * enrollMapper.deleteRelationSubOrderAndAdmitInfo(learnId);
		 * 
		 * // 重新录取 studentEnroll(learnId, unvsId, pfsnId, taId, scholarship);
		 */
		BaseUser user = SessionUtil.getUser();
		if(!StringUtil.hasValue(campusId)){
			campusId = null;
		}
		enrollMapper.updateStuCampusId(learnId, campusId,user);
	}

	public void importExcel(MultipartFile enrollExcel) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdStdEnrollImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdStdEnrollImport>();
		testExcelCofing.setSheetName("index").setType(BdStdEnrollImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("录取院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("录取专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"));

		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdStdEnrollImport> list = ExcelUtil.importWorkbook(enrollExcel.getInputStream(), testExcelCofing,
					enrollExcel.getOriginalFilename());

			// 遍历插入
			for (BdStdEnrollImport enroll : list) {
				BdStdEnrollImport enrollInfo = enrollMapper.selectLearnId(enroll);
				if (enrollInfo == null || !StringUtil.hasValue(enrollInfo.getLearnId())) {
					throw new IllegalArgumentException("excel数据第" + index + "行，学员待录取记录查询为空！");
				}

				studentEnroll(enrollInfo.getLearnId(), enrollInfo.getUnvsId(), enrollInfo.getPfsnId());
				index++;
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

	@Autowired
	private BdStdRegisterMapper regMapper;

	public void gkCheck(String learnId) {
		BaseUser user = SessionUtil.getUser();
		
		List<Map<String, String>> list = regMapper.selectTmpAddRecord(learnId, user.getRealName(), user.getUserId());
		for (Map<String, String> map : list) {
			map.put("register_id", IDGenerator.generatorId());
		}
		regMapper.insertFirstRegist(list);
		payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_REGISTER);
	}

	public void gkChecks(String[] learnIds) {
		for (String learnId : learnIds) {
			BdLearnInfo learnInfo = payMapper.selectLearnInfoByLearnId(learnId);
			if (null != learnInfo) {
				if (StudentConstants.RECRUIT_TYPE_GK.equals(learnInfo.getRecruitType())) {
					gkCheck(learnId);
				}
			}
		}
	}
	
	/**
	 * 根据筛选条件分配归属校区
	 * @param enroll
	 */
	public void queryAllocation(BdStdEnrollQuery enroll){
		List<Map<String, String>> enrolls = enrollMapper.selectNeedOperStdByPage(enroll);
		BaseUser user = SessionUtil.getUser();
		if(null != enrolls && enrolls.size()>0){
			if(!StringUtil.hasValue(enroll.getFpCampusId())){
				enroll.setFpCampusId(null);
			}
			campusAsynService.asynStdCampusOper(enrolls,enroll.getFpCampusId(),user);
		}
	}
	
	public Object getHomeCampusInfo(String campusStatus){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = enrollMapper.getHomeCampusInfo(campusStatus);
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("id"));
				map.put("dictName", resultMap.get("campusName"));
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	public void selectAllocation(String[] idArray,String campusId) {
		BaseUser user = SessionUtil.getUser();
		if(!StringUtil.hasValue(campusId)){
			campusId = null;
		}
		enrollMapper.selectAllocation(idArray,campusId,user);
	}

	/**
	 * 筛选后批量录取
	 * @param enroll
	 */
	public void queryBatchAdmit(BdStdEnrollQuery enroll) {
		List<BdStdEnroll> enrolls = enrollMapper.selectBatchAdmitResult(enroll);
		if(enrolls!=null&&enrolls.size()>0) {
			enrolls.stream().forEach(v -> {
				studentEnroll(v.getLearnId(), v.getUnvsId(),v.getPfsnId());		
			});
		}
		 
	}
	/**
	 * 勾选后批量录取
	 * @param enroll
	 */
	public void checkBatchAdmit(String[] idArray) {
	    Arrays.asList(idArray).stream().forEach(v -> {
	    	BdStdEnroll enroll=enrollMapper.selectStdEnrollInfoByLearnId(v);
	    	if(enroll!=null) {
	    		studentEnroll(enroll.getLearnId(), enroll.getUnvsId(),enroll.getPfsnId());		
	    	}
	    });			
	}
	
	
}
