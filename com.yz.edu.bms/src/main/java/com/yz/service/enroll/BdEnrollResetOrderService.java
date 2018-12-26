package com.yz.service.enroll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.finance.BdCouponMapper;
import com.yz.dao.finance.BdFeeReviewMapper;
import com.yz.dao.order.BsOrderMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.communi.Body;
import com.yz.model.enroll.BdInclusionStatusImport;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.coupon.BdStdCoupon;
import com.yz.model.finance.review.BdFeeReviewExport;
import com.yz.model.finance.review.BdFeeReviewTuition;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.service.finance.BdOrderService;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
public class BdEnrollResetOrderService {

	private final static Logger log = LoggerFactory.getLogger(BdEnrollResetOrderService.class);

	@Autowired
	private BdStdEnrollMapper enrollMapper;

	@Autowired
	private BdOrderService orderService;

	@Autowired
	private BdCouponMapper couponMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@Autowired
	private BdFeeReviewMapper reviewMapper;

	@Autowired
	private BsOrderMapper bsMapper;

	public void importStdInclusionStatus(String unvsId, String[] scholarships) {
		// 对导入工具进行字段填充
		/*
		 * ExcelUtil.IExcelConfig<BdInclusionStatusImport> testExcelCofing = new
		 * ExcelUtil.IExcelConfig<BdInclusionStatusImport>();
		 * testExcelCofing.setSheetName("index").setType(BdInclusionStatusImport
		 * .class) .addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
		 * .addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard")) .addTitle(new
		 * ExcelUtil.IExcelTitle("年级", "grade")) .addTitle(new
		 * ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"));
		 */
		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdInclusionStatusImport> list = enrollMapper.selectEnrolledLearnId(unvsId, scholarships);
			// List<BdInclusionStatusImport> list =
			// enrollMapper.selectEnrolledLearnIdGuangbo("51");

			if (null != list && list.size() > 0) {
				for (BdInclusionStatusImport in : list) {
					log.debug("------------------------------------------- 当前处理数：" + index);
					index++;
					String learnId = null;

					try {
						List<String> learnIds = enrollMapper.selectLearnIdByInclusion(in);

						if (null == learnIds || learnIds.size() == 0) {
							enrollMapper.insertResetOrderError(IDGenerator.generatorId(),learnId, in.getStdName(), in.getIdCard(), in.getGrade(),
									"未查询学业");
							continue;
							// throw new CustomException("数据异常，请检查第" + index +
							// "行数据，学员:" + in.getStdName() + "未查询学业");
						}
						if (learnIds.size() > 1) {
							enrollMapper.insertResetOrderError(IDGenerator.generatorId(),learnId, in.getStdName(), in.getIdCard(), in.getGrade(),
									"查询到多条学业");
							continue;
							// throw new CustomException("数据异常，请检查第" + index +
							// "行数据，学员:" + in.getStdName() + "查询到多条学业");
						}

						learnId = learnIds.get(0);
						/*
						 * int errorCount =
						 * enrollMapper.selectResetOrderErrorCount(in.getIdCard(
						 * )); if (errorCount > 0) { continue; }
						 */
						int count = enrollMapper.selectResetOrderCount(learnId);
						if (count > 0) {
							continue;
						}

						String scholarship = enrollMapper.selectScholarshipByLearnId(learnId);

						/*
						 * if ("14".equals(scholarship) ||
						 * "15".equals(scholarship) || "16".equals(scholarship)
						 * || "17".equals(scholarship) ||
						 * "18".equals(scholarship) || "19".equals(scholarship)
						 * || "20".equals(scholarship) ||
						 * "4".equals(scholarship)) { //
						 * enrollMapper.deleteTestHelpCoupon(learnId); continue;
						 * }
						 */

						/*
						 * String inclusionStatus =
						 * DictExchangeUtil.getParamValue("inclusionStatus",
						 * in.getInclusionStatus());
						 */

						String inclusionStatus = enrollMapper.selectInclusionStatus(learnId);

						/*
						 * if (!StringUtil.hasValue(inclusionStatus)) { throw
						 * new CustomException("数据异常，请检查第" + index + "行数据，学员:" +
						 * in.getStdName() + ",圆梦状态不存在"); }
						 */

						inclusionChange(learnIds.get(0), inclusionStatus);
						enrollMapper.insertResetOrderInfo(learnId, in.getStdName(), in.getIdCard());

					} catch (Exception e) {
						enrollMapper.insertResetOrderError(IDGenerator.generatorId(),learnId, in.getStdName(), in.getIdCard(), in.getGrade(),
								e.getMessage());
						log.error(e.getMessage());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}

	}

	public void resetCoupon(InputStream in, String unvsId, String grade) {
		// 对导入工具进行字段填充

		ExcelUtil.IExcelConfig<BdInclusionStatusImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdInclusionStatusImport>();
		testExcelCofing.setSheetName("index").setType(BdInclusionStatusImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"));

		List<BdInclusionStatusImport> list = ExcelUtil.importWorkbook(in, testExcelCofing, "resetCoupon.xlsx");
		if (null != list && list.size() > 0) {
			for (BdInclusionStatusImport map : list) {
				String idCard = map.getIdCard();
				if (!StringUtil.hasValue(idCard)) {
					continue;
				}

				String learnId = enrollMapper.selectLearnIdByIdCard(idCard, unvsId, grade);

				List<BdCoupon> coupons = couponMapper.selectAvailableScoreCouponResetOrder(learnId);
				String stdId = enrollMapper.selectstdIdByLearnId(learnId);
				// 删除所有优惠券
				couponMapper.deleteAllCouponByStdId(stdId);
				// 判断能否领取优惠券
				String stdScoreStr = enrollMapper.selectStdScore(learnId);
				if (StringUtil.hasValue(stdScoreStr)) {

					if (coupons != null && coupons.size() > 0) {
						for (BdCoupon coupon : coupons) {

							BdStdCoupon stdCoupon = new BdStdCoupon();
							stdCoupon.setCouponId(coupon.getCouponId());
							stdCoupon.setStdId(stdId);
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
			}
		}
	}

	public void resetInclusion(InputStream in, String unvsId, String grade, String inclusionStatus) {
		// 对导入工具进行字段填充

		ExcelUtil.IExcelConfig<BdInclusionStatusImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdInclusionStatusImport>();
		testExcelCofing.setSheetName("index").setType(BdInclusionStatusImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("考生号", "examNo")).addTitle(new ExcelUtil.IExcelTitle("考区", "taName"))
				;

		List<BdInclusionStatusImport> list = ExcelUtil.importWorkbook(in, testExcelCofing, "inclusion.xlsx");
		if (null != list && list.size() > 0) {
			for (BdInclusionStatusImport map : list) {
				String examNo = map.getExamNo();
				String learnId = enrollMapper.selectLearnIdByExamNo(examNo);
				// TODO
				enrollMapper.updateInclusionStatus(learnId, inclusionStatus);
				enrollMapper.updateScholarship(learnId,"22");
				//String taId = enrollMapper.selectTaIdByTaName(map.getTaName());
				//enrollMapper.updateAdmitTaId(learnId,taId);

			}
		}
	}

	public void inclusionChange(String learnId, String inclusionStatus) {

		// BaseUser user = SessionUtil.getUser();

		BdLearnInfo learn = new BdLearnInfo();

		if (!StringUtil.hasValue(inclusionStatus)) {

			inclusionStatus = StudentConstants.INCLUSION_STATUS_HOLD;
		}

		BdLearnInfo learnInfo = enrollMapper.selectLearnInfoByLearnId(learnId);

		List<BdCoupon> coupons = couponMapper.selectAvailableScoreCouponResetOrder(learnId);

		String stdId = enrollMapper.selectstdIdByLearnId(learnId);
		// 删除所有优惠券
		couponMapper.deleteAllCouponByStdId(stdId);

		String feeId = enrollMapper.selectFeeIdByLearnId(learnId);

		if (!StringUtil.hasValue(feeId)) {
			throw new BusinessException("E000048"); // 此专业与考区暂无收费标准
		}
		learnInfo.setFeeId(feeId);

		String tutorPaidTime = null;

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learnInfo.getRecruitType())) {
			// 辅导费缴纳时间
			tutorPaidTime = enrollMapper.selectTutorPaidTime(learnId);
		}

		String offerId = enrollMapper.selectOfferIdByLearnId(learnId, tutorPaidTime, inclusionStatus);
		if (StringUtil.hasValue(offerId)) {
			learnInfo.setOfferId(offerId);
		}

		/*
		 * learn.setCreateUser(user.getRealName());
		 * learn.setCreateUserId(user.getUserId());
		 */
		learn.setFinanceCode(learnInfo.getFinanceCode());
		learn.setLearnId(learnId);
		learn.setMobile(learnInfo.getMobile());
		learn.setStdName(learnInfo.getStdName());
		learn.setStdId(learnInfo.getStdId());
		/*
		 * learn.setUpdateUser(user.getRealName());
		 * learn.setUpdateUserId(user.getUserId());
		 */
		learn.setUnvsId(learnInfo.getUnvsId());
		learn.setPfsnId(learnInfo.getPfsnId());
		learn.setTaId(learnInfo.getTaId());
		learn.setFeeId(learnInfo.getFeeId());
		learn.setOfferId(learnInfo.getOfferId());
		learn.setRecruitType(learnInfo.getRecruitType());
		learn.setStdStage(learnInfo.getStdStage());

		BigDecimal refundAmount = checkSubjectOrder(learnId);

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learn.getRecruitType())) {
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

			accountApi.trans(body);
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

	public void exportUnpaidStudent() {
		FileOutputStream os = null;
		try {
			ExcelUtil.IExcelConfig<BdFeeReviewExport> testExcelCofing = new ExcelUtil.IExcelConfig<BdFeeReviewExport>();
			testExcelCofing.setSheetName("index").setType(BdFeeReviewExport.class)
					.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"));
			List<BdFeeReviewExport> list = reviewMapper.selectUnPaidStudent("2018");

			SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

			// ServletOutputStream out = null;

			/*
			 * response.setContentType("application/vnd.ms-excel");
			 * response.setHeader("Content-disposition",
			 * "attachment;filename=StudentPaymentInfo.xls"); out =
			 * response.getOutputStream(); wb.write(out);
			 */

			File date = new File("F:\\2018级第一年费用未缴清.xlsx");
			date.createNewFile();
			os = new FileOutputStream(date);

			wb.write(os);

		} catch (Exception e) {
			// 导出异常，记录日志，改变状态
			log.error(e.getMessage());
		} finally {
			try {
				/*
				 * out.flush(); out.close();
				 */
				os.flush();
				os.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void exportSerialInfo(String unvsId) {
		FileOutputStream os = null;
		try {
			// 对导出工具进行字段填充
			ExcelUtil.IExcelConfig<BdFeeReviewExport> testExcelCofing = new ExcelUtil.IExcelConfig<BdFeeReviewExport>();
			testExcelCofing.setSheetName("index").setType(BdFeeReviewExport.class)
					.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruit"))
					.addTitle(new ExcelUtil.IExcelTitle("招生部门", "recruitDepart"))
					.addTitle(new ExcelUtil.IExcelTitle("校区", "recruitCampus"))
					.addTitle(new ExcelUtil.IExcelTitle("班主任", "master"))
					.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
					.addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("学号", "stdNo"))
					.addTitle(new ExcelUtil.IExcelTitle("手机号", "mobile"))
					.addTitle(new ExcelUtil.IExcelTitle("参考地", "taName"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
					.addTitle(new ExcelUtil.IExcelTitle("考前缴费日期", "tutorPayTime"))
					.addTitle(new ExcelUtil.IExcelTitle("学员状态", "stdStage"))
					.addTitle(new ExcelUtil.IExcelTitle("年", "tutorYear"))
					.addTitle(new ExcelUtil.IExcelTitle("月", "tutorMonth"))
					.addTitle(new ExcelUtil.IExcelTitle("是否转报", "hasRoll"))
					.addTitle(new ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"))
					.addTitle(new ExcelUtil.IExcelTitle("成考分数", "score"))
					.addTitle(new ExcelUtil.IExcelTitle("收费标准", "feeName"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠政策", "offerName"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠券", "couponNames"))
					.addTitle(new ExcelUtil.IExcelTitle("滞留金", "delay"));
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("考前辅导应缴", "tutorPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("考前辅导已缴", "tutorPaid"));
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第一年书费应缴", "firstBookPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第一年书费已缴", "firstBookPaid"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第一年学费应缴", "firstPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第一年优惠", "firstOffer"))
					.addTitle(new ExcelUtil.IExcelTitle("第一年学费已缴", "firstPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("第一年是否缴清", "firstIsPayUp"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第二年书费应缴", "secondBookPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第二年书费已缴", "secondBookPaid"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第二年学费应缴", "secondPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第二年优惠", "secondOffer"))
					.addTitle(new ExcelUtil.IExcelTitle("第二年学费已缴", "secondPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("第二年是否缴清", "secondIsPayUp"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年书费应缴", "thirdBookPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第三年书费已缴", "thirdBookPaid"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年学费应缴", "thirdPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第三年优惠", "thirdOffer"))
					.addTitle(new ExcelUtil.IExcelTitle("第三年学费已缴", "thirdPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("第三年是否缴清", "thirdIsPayUp"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第四年书费应缴", "fourBookPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第四年书费已缴", "fourBookPaid"));

			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第四年学费应缴", "fourPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("第四年优惠", "fourOffer"))
					.addTitle(new ExcelUtil.IExcelTitle("第四年学费已缴", "fourPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("第四年是否缴清", "fourIsPayUp"));

			/*
			 * testExcelCofing
			 * 
			 * .addTitle(new ExcelUtil.IExcelTitle("其它应缴", "otherPayable"))
			 * .addTitle(new ExcelUtil.IExcelTitle("其它已缴", "otherPaid")); if
			 * (itemCodes.contains("W1")) { testExcelCofing.addTitle(new
			 * ExcelUtil.IExcelTitle("第一年网络费应缴", "firstNetPayable"))
			 * .addTitle(new ExcelUtil.IExcelTitle("第一年网络费已缴", "firstNetPaid"));
			 * }
			 * 
			 * if (itemCodes.contains("W2")) { testExcelCofing.addTitle(new
			 * ExcelUtil.IExcelTitle("第二年网络费应缴", "secondNetPayable"))
			 * .addTitle(new ExcelUtil.IExcelTitle("第二年网络费已缴",
			 * "secondNetPaid")); } if (itemCodes.contains("W3")) {
			 * testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年网络费应缴",
			 * "thirdNetPayable")) .addTitle(new
			 * ExcelUtil.IExcelTitle("第三年网络费已缴", "thirdNetPaid")); } if
			 * (itemCodes.contains("W4")) { testExcelCofing.addTitle(new
			 * ExcelUtil.IExcelTitle("第四年网络费应缴", "fourNetPayable"))
			 * .addTitle(new ExcelUtil.IExcelTitle("第四年网络费已缴", "fourNetPaid"));
			 * }
			 */
			testExcelCofing

					.addTitle(new ExcelUtil.IExcelTitle("总计学费应缴", "totalPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("总计已缴", "totalPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("人数", "number"))
					.addTitle(new ExcelUtil.IExcelTitle("标准人数", "audit"));

			List<BdFeeReviewExport> list = reviewMapper.selectEnrollFeeStatisticsExport(unvsId);

			for (BdFeeReviewExport re : list) {

				try {
					String audit = re.getAudit();
					if (!StringUtil.hasValue(audit)) {
						re.setAudit("1");
					}

					if (null != re.getCouponName() && re.getCouponName().size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (String c : re.getCouponName()) {
							sb.append(c).append("/r/n");
						}
						re.setCouponNames(sb.toString());
					} else {
						re.setCouponNames("");
					}

					re.setDelay(bsMapper.selectDelayByStdId(re.getStdId()));

					String inclusionStatus = dictExchangeUtil.getParamKey("inclusionStatus", re.getInclusionStatus());
					re.setInclusionStatus(inclusionStatus);

					String totalPaid = "0.00";

					StringBuffer sbPfsnName = new StringBuffer();

					sbPfsnName.append("(").append(re.getPfsnCode()).append("").append(re.getPfsnName());
					re.setPfsnName(sbPfsnName.toString());

					String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", re.getPfsnLevel());
					re.setPfsnLevel(pfsnLevel);

					String grade = dictExchangeUtil.getParamKey("grade", re.getGrade());
					re.setGrade(grade);

					/*
					 * String tutorCheck = null; if
					 * (FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED.equals(
					 * re. getTutorCheck())) { tutorCheck = "是"; } else {
					 * tutorCheck = "否"; } re.setTutorCheck(tutorCheck);
					 */

					String scholarship = dictExchangeUtil.getParamKey("scholarship", re.getScholarship());

					re.setScholarship(scholarship);

					String stdStage = dictExchangeUtil.getParamKey("stdStage", re.getStdStage());
					re.setStdStage(stdStage);

					if (null != re.getTutor()) {

						if (StringUtil.hasValue(re.getTutor().getSerialStatus())) {
							String tutorSerialStatus = re.getTutor().getSerialStatus();
							if (FinanceConstants.STUDENT_SERIAL_STATUS_UNCHECK.equals(tutorSerialStatus)
									|| FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED.equals(tutorSerialStatus)) {
								if (StringUtil.hasValue(re.getTutorPayTime())) {

									Calendar c = Calendar.getInstance();
									SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
									c.setTime(sdf.parse(re.getTutorPayTime()));

									re.setTutorYear(c.get(Calendar.YEAR) + "");
									re.setTutorMonth(c.get(Calendar.MONTH) + "");

								}
							}
						}

						String tutorPayable = re.getTutor().getPayable();
						if (StringUtil.hasValue(tutorPayable)) {
							re.setTutorPayable(tutorPayable);
						} else {
							re.setTutorPayable("0.00");
						}

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getTutor().getSubOrderStatus())) {
							re.setTutorPaid(tutorPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getTutor().getPayable());
						} else {
							re.setTutorPaid("0.00");
						}
					}

					String firstIsPayUp = "";
					String firstOffer = "0.00";

					boolean firstBookPayUp = false;
					if (null != re.getFirstBook()) {
						firstOffer = BigDecimalUtil.add(firstOffer, re.getFirstBook().getOfferAmount());
						String firstBookPayable = StringUtil.hasValue(re.getFirstBook().getPayable())
								? re.getFirstBook().getPayable() : "0.00";
						re.setFirstOffer(firstOffer);

						re.setFirstBookPayable(firstBookPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFirstBook().getSubOrderStatus())) {
							re.setFirstBookPaid(firstBookPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getFirstBook().getPayable());
							firstBookPayUp = true;
						} else {
							re.setFirstBookPaid("0.00");
						}
					}

					boolean firstPayUp = false;
					if (null != re.getFirst()) {
						firstOffer = BigDecimalUtil.add(firstOffer, re.getFirst().getOfferAmount());
						String firstPayable = StringUtil.hasValue(re.getFirst().getPayable())
								? re.getFirst().getPayable() : "0.00";
						re.setFirstPayable(firstPayable);
						re.setFirstOffer(firstOffer);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFirst().getSubOrderStatus())) {
							re.setFirstPaid(firstPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getFirst().getPayable());
							firstPayUp = true;
						} else {
							re.setFirstBookPaid("0.00");
						}

					}

					if (firstBookPayUp && firstPayUp) {
						firstIsPayUp = "是";
					} else {
						firstIsPayUp = "否";
					}
					re.setFirstIsPayUp(firstIsPayUp);

					/*----------------------------  第二年  -------------------------------*/

					String secondIsPayUp = "";
					String secondOffer = "0.00";

					boolean secondBookPayUp = false;
					if (null != re.getSecondBook()) {

						secondOffer = BigDecimalUtil.add(secondOffer, re.getSecondBook().getOfferAmount());
						re.setSecondOffer(secondOffer);

						String secondBookPayable = StringUtil.hasValue(re.getSecondBook().getPayable())
								? re.getSecondBook().getPayable() : "0.00";
						re.setSecondBookPayable(secondBookPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getSecondBook().getSubOrderStatus())) {
							re.setSecondBookPaid(secondBookPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getSecondBook().getPayable());
							secondBookPayUp = true;
						} else {
							re.setSecondBookPaid("0.00");
						}
					}

					boolean secondPayUp = false;
					if (null != re.getSecond()) {
						secondOffer = BigDecimalUtil.add(secondOffer, re.getSecond().getOfferAmount());
						String secondPayable = StringUtil.hasValue(re.getSecond().getPayable())
								? re.getSecond().getPayable() : "0.00";
						re.setSecondPayable(secondPayable);

						re.setSecondOffer(secondOffer);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getSecond().getSubOrderStatus())) {
							re.setSecondPaid(secondPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getSecond().getPayable());
							secondPayUp = true;
						} else {
							re.setSecondBookPaid("0.00");
						}

					}
					if (secondBookPayUp && secondPayUp) {
						secondIsPayUp = "是";
					} else {
						secondIsPayUp = "否";
					}

					re.setSecondIsPayUp(secondIsPayUp);

					/*----------------------------  第三年  -------------------------------*/

					String thirdIsPayUp = "";
					String thirdOffer = "0.00";

					boolean thirdBookPayUp = false;
					if (null != re.getThirdBook()) {

						thirdOffer = BigDecimalUtil.add(thirdOffer, re.getThirdBook().getOfferAmount());
						re.setThirdOffer(thirdOffer);

						String thirdBookPayable = StringUtil.hasValue(re.getThirdBook().getPayable())
								? re.getThirdBook().getPayable() : "0.00";
						re.setThirdBookPayable(thirdBookPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getThirdBook().getSubOrderStatus())) {
							re.setThirdBookPaid(thirdBookPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getThirdBook().getPayable());
							thirdBookPayUp = true;
						} else {
							re.setThirdBookPaid("0.00");
						}
					}

					boolean thirdPayUp = false;
					if (null != re.getThird()) {

						thirdOffer = BigDecimalUtil.add(thirdOffer, re.getThird().getOfferAmount());
						String thirdPayable = StringUtil.hasValue(re.getThird().getPayable())
								? re.getThird().getPayable() : "0.00";
						re.setThirdOffer(thirdOffer);
						re.setThirdPayable(thirdPayable);
						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getThird().getSubOrderStatus())) {
							re.setThirdPaid(thirdPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getThird().getPayable());
							thirdPayUp = true;
						} else {
							re.setThirdBookPaid("0.00");
						}

					}
					if (thirdBookPayUp && thirdPayUp) {
						thirdIsPayUp = "是";
					} else {
						thirdIsPayUp = "否";
					}

					re.setThirdIsPayUp(thirdIsPayUp);

					/*----------------------------  第四年  -------------------------------*/

					String fourIsPayUp = "";
					String fourOffer = "0.00";

					boolean fourBookPayUp = false;
					if (null != re.getFourBook()) {

						fourOffer = BigDecimalUtil.add(fourOffer, re.getFourBook().getOfferAmount());
						re.setFourOffer(fourOffer);

						String fourBookPayable = StringUtil.hasValue(re.getFourBook().getPayable())
								? re.getFourBook().getPayable() : "0.00";
						re.setFourBookPayable(fourBookPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFourBook().getSubOrderStatus())) {
							re.setFourBookPaid(fourBookPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getFourBook().getPayable());
							fourBookPayUp = true;
						} else {
							re.setFourBookPaid("0.00");
						}
					}

					boolean fourPayUp = false;
					if (null != re.getFour()) {
						String fourPayable = StringUtil.hasValue(re.getFour().getPayable()) ? re.getFour().getPayable()
								: "0.00";
						re.setFourPayable(fourPayable);
						re.setFourOffer(fourOffer);

						fourOffer = BigDecimalUtil.add(fourOffer, re.getFour().getOfferAmount());
						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFour().getSubOrderStatus())) {
							re.setFourPaid(fourPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, re.getFour().getPayable());
							fourPayUp = true;
						} else {
							re.setFourBookPaid("0.00");
						}

					}
					if (fourBookPayUp && fourPayUp) {
						fourIsPayUp = "是";
					} else {
						fourIsPayUp = "否";
					}

					re.setFourIsPayUp(fourIsPayUp);

					/*---------------------------  学费结束    --------------------------------*/

					if (null != re.getOther() && re.getOther().size() > 0) {

						String otherPayable = "0.00";
						String otherPaid = "0.00";
						for (BdFeeReviewTuition other : re.getOther()) {
							otherPayable = BigDecimalUtil.add(otherPayable, other.getPayable());
							if (FinanceConstants.ORDER_STATUS_PAID.equals(other.getSubOrderStatus())) {
								otherPaid = BigDecimalUtil.add(otherPaid, other.getPayable());
								totalPaid = BigDecimalUtil.add(totalPaid, other.getPayable());
							}
						}
						re.setOtherPayable(otherPayable);
						re.setOtherPaid(otherPaid);
					}

					/*--------------------------------- 第一年网络    -----------------------------*/

					if (null != re.getFirstNet()) {

						String firstNetPayable = StringUtil.hasValue(re.getFirstNet().getPayable())
								? re.getFirstNet().getPayable() : "0.00";
						re.setFirstNetPayable(firstNetPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFirstNet().getSubOrderStatus())) {
							re.setFirstNetPaid(firstNetPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, firstNetPayable);
						} else {
							re.setFirstNetPaid("0.00");
						}
					}

					/*--------------------------------- 第二年网络    -----------------------------*/

					if (null != re.getSecondNet()) {
						String secondNetPayable = StringUtil.hasValue(re.getSecondNet().getPayable())
								? re.getSecondNet().getPayable() : "0.00";
						re.setSecondNetPayable(secondNetPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getSecondNet().getSubOrderStatus())) {
							re.setSecondNetPaid(secondNetPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, secondNetPayable);
						} else {
							re.setSecondNetPaid("0.00");
						}
					}

					/*--------------------------------- 第三年网络    -----------------------------*/

					if (null != re.getThirdNet()) {
						String thirdNetPayable = StringUtil.hasValue(re.getThirdNet().getPayable())
								? re.getThirdNet().getPayable() : "0.00";
						re.setThirdNetPayable(thirdNetPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getThirdNet().getSubOrderStatus())) {
							re.setThirdNetPaid(thirdNetPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, thirdNetPayable);
						} else {
							re.setThirdNetPaid("0.00");
						}
					}

					/*--------------------------------- 第四年网络    -----------------------------*/

					if (null != re.getFourNet()) {
						String fourNetPayable = StringUtil.hasValue(re.getFourNet().getPayable())
								? re.getFourNet().getPayable() : "0.00";
						re.setFourNetPayable(fourNetPayable);

						if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFourNet().getSubOrderStatus())) {
							re.setFourNetPaid(fourNetPayable);
							totalPaid = BigDecimalUtil.add(totalPaid, fourNetPayable);
						} else {
							re.setFourNetPaid("0.00");
						}
					}

					re.setTotalPaid(totalPaid);

					String hasRoll = re.getHasRoll();
					if (StringUtil.hasValue(hasRoll)) {
						int hasChange = Integer.valueOf(hasRoll);
						if (hasChange > 1) {
							re.setHasRoll("新增转报");
						} else {
							re.setHasRoll("");
						}
					} else {
						re.setHasRoll("");
					}
				} catch (Exception e) {
					log.error(e.getMessage());
					continue;
				}

			}

			SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

			// ServletOutputStream out = null;

			/*
			 * response.setContentType("application/vnd.ms-excel");
			 * response.setHeader("Content-disposition",
			 * "attachment;filename=StudentPaymentInfo.xls"); out =
			 * response.getOutputStream(); wb.write(out);
			 */

			File date = new File("F:\\finance.xlsx");
			date.createNewFile();
			os = new FileOutputStream(date);

			wb.write(os);

		} catch (Exception e) {
			// 导出异常，记录日志，改变状态
			log.error(e.getMessage());
		} finally {
			try {
				/*
				 * out.flush(); out.close();
				 */
				os.flush();
				os.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

}
