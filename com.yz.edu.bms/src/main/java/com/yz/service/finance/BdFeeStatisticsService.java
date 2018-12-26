package com.yz.service.finance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.ExcelConstants;
import com.yz.constants.FinanceConstants;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.finance.BdExcelMapper;
import com.yz.dao.finance.BdFeeReviewMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.review.BdFeeReviewExport;
import com.yz.model.finance.review.BdFeeReviewTuition;
import com.yz.model.finance.statistics.BdExcel;
import com.yz.model.finance.statistics.BdFeeStatisticsQuery;
import com.yz.model.us.UsFollow;
import com.yz.util.BigDecimalUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdFeeStatisticsService {
	
	@Autowired
	private YzSysConfig yzSysConfig;

	private static final Logger log = LoggerFactory.getLogger(BdFeeStatisticsService.class);

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	private BdFeeReviewMapper reviewMapper;

	@Autowired
	private BdExcelMapper excelMapper;

	@Autowired
	private UsFollowMapper usMapper;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	public String insertExcelInfo(BdExcel excel) {
		excel.setExcelId(IDGenerator.generatorId());
		excelMapper.insertSelective(excel);
		return excel.getExcelId();
	}

	public void exportFinance(BdFeeStatisticsQuery query, HttpServletResponse response, BdExcel excel) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				exportSerialInfo(query, response, excel);
			}
		});
	}

	public void exportSerialInfo(BdFeeStatisticsQuery query, HttpServletResponse response, BdExcel excel) {
		ByteArrayOutputStream os = null;
		try {
			List<String> itemCodes = query.getItemCodes();
			// 对导出工具进行字段填充
			ExcelUtil.IExcelConfig<BdFeeReviewExport> testExcelCofing = new ExcelUtil.IExcelConfig<BdFeeReviewExport>();
			testExcelCofing.setSheetName("index").setType(BdFeeReviewExport.class)
					.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruit"))
					.addTitle(new ExcelUtil.IExcelTitle("招生部门", "recruitDepart"))
					.addTitle(new ExcelUtil.IExcelTitle("招生校区", "recruitCampus"))
					.addTitle(new ExcelUtil.IExcelTitle("归属校区", "homeCampusName"))
					.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
					.addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("学号", "stdNo"))
					.addTitle(new ExcelUtil.IExcelTitle("参考地", "taName"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
					.addTitle(new ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"))
					.addTitle(new ExcelUtil.IExcelTitle("考前缴费日期", "tutorPayTime"))
					.addTitle(new ExcelUtil.IExcelTitle("学员状态", "stdStage"))
					.addTitle(new ExcelUtil.IExcelTitle("年", "tutorYear"))
					.addTitle(new ExcelUtil.IExcelTitle("月", "tutorMonth"))
					.addTitle(new ExcelUtil.IExcelTitle("是否转报", "hasRoll"))
					.addTitle(new ExcelUtil.IExcelTitle("是否学校分配", "isAllot"));

			if (itemCodes.contains("Y0")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("考前辅导应缴", "tutorPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("考前辅导已缴", "tutorPaid"));
			}
			if (itemCodes.contains("S1")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第一年书费应缴", "firstBookPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第一年书费已缴", "firstBookPaid"));
			}

			if (itemCodes.contains("Y1")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第一年学费应缴", "firstPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第一年优惠", "firstOffer"))
						.addTitle(new ExcelUtil.IExcelTitle("第一年学费已缴", "firstPaid"))
						.addTitle(new ExcelUtil.IExcelTitle("第一年是否缴清", "firstIsPayUp"));
			}

			if (itemCodes.contains("S2")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第二年书费应缴", "secondBookPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第二年书费已缴", "secondBookPaid"));
			}

			if (itemCodes.contains("Y2")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第二年学费应缴", "secondPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第二年优惠", "secondOffer"))
						.addTitle(new ExcelUtil.IExcelTitle("第二年学费已缴", "secondPaid"))
						.addTitle(new ExcelUtil.IExcelTitle("第二年是否缴清", "secondIsPayUp"));
			}

			if (itemCodes.contains("S3")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年书费应缴", "thirdBookPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第三年书费已缴", "thirdBookPaid"));
			}

			if (itemCodes.contains("Y3")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年学费应缴", "thirdPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第三年优惠", "thirdOffer"))
						.addTitle(new ExcelUtil.IExcelTitle("第三年学费已缴", "thirdPaid"))
						.addTitle(new ExcelUtil.IExcelTitle("第三年是否缴清", "thirdIsPayUp"));
			}

			if (itemCodes.contains("S4")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第四年书费应缴", "fourBookPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第四年书费已缴", "fourBookPaid"));
			}

			if (itemCodes.contains("Y4")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第四年学费应缴", "fourPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第四年优惠", "fourOffer"))
						.addTitle(new ExcelUtil.IExcelTitle("第四年学费已缴", "fourPaid"))
						.addTitle(new ExcelUtil.IExcelTitle("第四年是否缴清", "fourIsPayUp"));
			}

			testExcelCofing

					.addTitle(new ExcelUtil.IExcelTitle("其它应缴", "otherPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("其它已缴", "otherPaid"));
			if (itemCodes.contains("W1")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第一年网络费应缴", "firstNetPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第一年网络费已缴", "firstNetPaid"));
			}

			if (itemCodes.contains("W2")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第二年网络费应缴", "secondNetPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第二年网络费已缴", "secondNetPaid"));
			}
			if (itemCodes.contains("W3")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第三年网络费应缴", "thirdNetPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第三年网络费已缴", "thirdNetPaid"));
			}
			if (itemCodes.contains("W4")) {
				testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("第四年网络费应缴", "fourNetPayable"))
						.addTitle(new ExcelUtil.IExcelTitle("第四年网络费已缴", "fourNetPaid"));
			}
			testExcelCofing

					.addTitle(new ExcelUtil.IExcelTitle("总计学费应缴", "totalPayable"))
					.addTitle(new ExcelUtil.IExcelTitle("总计已缴", "totalPaid"))
					.addTitle(new ExcelUtil.IExcelTitle("人数", "number"))
					.addTitle(new ExcelUtil.IExcelTitle("标准人数", "audit"));

			List<BdFeeReviewExport> list = reviewMapper.selectFeeStatisticsExport(query);

			for (BdFeeReviewExport re : list) {

				String audit = re.getAudit();
				if (!StringUtil.hasValue(audit)) {
					re.setAudit("1");
				}

				String totalPaid = "0.00";

				StringBuffer sbPfsnName = new StringBuffer();

				sbPfsnName.append("(").append(re.getPfsnCode()).append("").append(re.getPfsnName());
				re.setPfsnName(sbPfsnName.toString());

				String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", re.getPfsnLevel());
				re.setPfsnLevel(pfsnLevel);

				String grade = dictExchangeUtil.getParamKey("grade", re.getGrade());
				re.setGrade(grade);

				String inclusionStatus = dictExchangeUtil.getParamKey("inclusionStatus", re.getInclusionStatus());

				re.setInclusionStatus(inclusionStatus);
				/*
				 * String tutorCheck = null; if
				 * (FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED.equals(re.
				 * getTutorCheck())) { tutorCheck = "是"; } else { tutorCheck =
				 * "否"; } re.setTutorCheck(tutorCheck);
				 */

				// 是否学校分配
				String userId = reviewMapper.selectUserIdByStdId(re.getStdId());

				UsFollow us = usMapper.selectByPrimaryKey(userId);

				// 1-学校分配
				if (us != null && "1".equals(us.getAssignFlag())) {
					re.setIsAllot("是");
				} else {
					re.setIsAllot("否");
				}

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
						re.setFirstBookPaid(
								BigDecimalUtil.substract(firstBookPayable, re.getFirstBook().getOfferAmount()));
						totalPaid = BigDecimalUtil.add(totalPaid, re.getFirstBook().getPayable());
						firstBookPayUp = true;
					} else {
						re.setFirstBookPaid("0.00");
					}
				}

				boolean firstPayUp = false;
				if (null != re.getFirst()) {
					firstOffer = BigDecimalUtil.add(firstOffer, re.getFirst().getOfferAmount());
					String firstPayable = StringUtil.hasValue(re.getFirst().getPayable()) ? re.getFirst().getPayable()
							: "0.00";
					re.setFirstPayable(firstPayable);
					re.setFirstOffer(firstOffer);

					if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFirst().getSubOrderStatus())) {
						re.setFirstPaid(firstPayable);
						totalPaid = BigDecimalUtil.add(totalPaid, re.getFirst().getPayable());
						firstPayUp = true;
					} else {
						re.setFirstPaid("0.00");
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
						re.setSecondBookPaid(
								BigDecimalUtil.substract(secondBookPayable, re.getSecondBook().getOfferAmount()));
						totalPaid = BigDecimalUtil.add(totalPaid, re.getSecondBook().getPayable());
						secondBookPayUp = true;
					} else {
						re.setSecondBookPaid("0.00");
					}
				}

				boolean secondPayUp = false;
				if (null != re.getSecond()) {
					String secondPayable = StringUtil.hasValue(re.getSecond().getPayable())
							? re.getSecond().getPayable() : "0.00";
					re.setSecondPayable(secondPayable);

					secondOffer = BigDecimalUtil.add(secondOffer, re.getSecond().getOfferAmount());
					re.setSecondOffer(secondOffer);
					if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getSecond().getSubOrderStatus())) {
						re.setSecondPaid(secondPayable);
						totalPaid = BigDecimalUtil.add(totalPaid, re.getSecond().getPayable());
						secondPayUp = true;
					} else {
						re.setSecondPaid("0.00");
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
						re.setThirdBookPaid(
								BigDecimalUtil.substract(thirdBookPayable, re.getThirdBook().getOfferAmount()));
						totalPaid = BigDecimalUtil.add(totalPaid, re.getThirdBook().getPayable());
						thirdBookPayUp = true;
					} else {
						re.setThirdBookPaid("0.00");
					}
				}

				boolean thirdPayUp = false;
				if (null != re.getThird()) {

					String thirdPayable = StringUtil.hasValue(re.getThird().getPayable()) ? re.getThird().getPayable()
							: "0.00";
					re.setThirdPayable(thirdPayable);
					thirdOffer = BigDecimalUtil.add(thirdOffer, re.getThird().getOfferAmount());
					re.setThirdOffer(thirdOffer);
					if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getThird().getSubOrderStatus())) {
						re.setThirdPaid(thirdPayable);
						totalPaid = BigDecimalUtil.add(totalPaid, re.getThird().getPayable());
						thirdPayUp = true;
					} else {
						re.setThirdPaid("0.00");
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
						re.setFourBookPaid(
								BigDecimalUtil.substract(fourBookPayable, re.getFourBook().getOfferAmount()));
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

					fourOffer = BigDecimalUtil.add(fourOffer, re.getFour().getOfferAmount());
					re.setFourOffer(fourOffer);
					if (FinanceConstants.ORDER_STATUS_PAID.equals(re.getFour().getSubOrderStatus())) {
						re.setFourPaid(fourPayable);
						totalPaid = BigDecimalUtil.add(totalPaid, re.getFour().getPayable());
						fourPayUp = true;
					} else {
						re.setFourPaid("0.00");
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

			}

			SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

			// ServletOutputStream out = null;

			/*
			 * response.setContentType("application/vnd.ms-excel");
			 * response.setHeader("Content-disposition",
			 * "attachment;filename=StudentPaymentInfo.xls"); out =
			 * response.getOutputStream(); wb.write(out);
			 */
			os = new ByteArrayOutputStream();
			wb.write(os);
			String bucket =yzSysConfig.getBucket();
			String realFilePath = Type.EXCEL.get() + "/" + "feeStatics" + "/" + excel.getExcelName() + ".xlsx";
			FileUploadUtil.upload(bucket, realFilePath, os.toByteArray());

			// 记录下载地址
			excel.setExcelUrl(realFilePath);
			excel.setExportStatus(ExcelConstants.EXCEL_STATUS_FINISHED);

			excelMapper.updateByPrimaryKeySelective(excel);
			log.error("----------------------------------- 财务统计成功导出，excelId：" + excel.getExcelId() + "，下载地址："
					+ realFilePath);

		} catch (Exception e) {
			log.error(e.toString());
			// 导出异常，记录日志，改变状态
			excel.setExportStatus(ExcelConstants.EXCEL_STATUS_FAILED);
			excel.setRemark(e.getMessage());
			excelMapper.updateByPrimaryKeySelective(excel);

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

	public Object selectExcelByPage(int start, int length) {
		PageHelper.offsetPage(start, length);
		List<BdExcel> excels = excelMapper.selectExcel();
		return new IPageInfo<BdExcel>((Page<BdExcel>) excels);
	}

	public Object sCampusJson() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<Map<String, String>> campus = reviewMapper.selectHomeCampus();
		for (Map<String, String> map : campus) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("dictValue", map.get("id"));
			m.put("dictName", map.get("campusName"));
			result.add(m);
		}
		return result;
	}
}
