package com.yz.service.finance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.ExcelConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.finance.BdExcelMapper;
import com.yz.dao.finance.BdFeeReviewMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.review.BdFeeReview;
import com.yz.model.finance.review.BdFeeReviewQuery;
import com.yz.model.finance.statistics.BdExcel;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.util.AmountUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdFeeReviewService {

	private static final Logger log = LoggerFactory.getLogger(BdFeeReviewService.class);

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private BdFeeReviewMapper reviewMapper;

	@Autowired
	private BdStdPayFeeService feeService;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private BdExcelMapper excelMapper;

	@Autowired
	private BdStudentModifyService modifyService;

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object queryFeeReviewByPage(int start, int length, BdFeeReviewQuery feeReview) {
		PageHelper.offsetPage(start, length);
		List<BdFeeReview> reviews = reviewMapper.selectFeeReviewByPage(feeReview);
		return new IPageInfo((Page) reviews);
	}

	public Object reviewSerial(String[] serialNos) {
		BaseUser user = SessionUtil.getUser();

		for (String serialNo : serialNos) {
			String learnId = reviewMapper.selectLearnIdBySerialNo(serialNo,
					FinanceConstants.STUDENT_SERIAL_STATUS_UNCHECK);

			if (!StringUtil.hasValue(learnId)) {
				continue;
			}
			String[] itemCodes = reviewMapper.selectItemCodesBySeriaoNo(serialNo);
			String[] years = reviewMapper.selectItemYearsByItemCodes(itemCodes);

			String isRepeal = reviewMapper.isRepeal(serialNo);
			if (!GlobalConstants.TRUE.equals(isRepeal)) {
				// 查询缴费变更操作
				feeService.checkPayChange(learnId, itemCodes, user.getRealName(), user.getUserId(), years);
			}

			// 添加变更记录
			List<BdStdPayInfoResponse> payinfos = reviewMapper.selectPaidInfo(serialNo);
			String item = "";
			if (payinfos != null && payinfos.size() > 0) {
				for (BdStdPayInfoResponse pay : payinfos) {
					item = item + pay.getItemCode() + ":" + pay.getItemName() + ",";
				}
			}
			if (StringUtil.hasValue(item)) {
				item = item.substring(0, item.length() - 1);
			}
			modifyService.addStudentModifyRecord(learnId, "缴费科目：" + item + " 收费审核");
		}

		return reviewMapper.reviewSerial(serialNos, user.getUserId(), user.getRealName());
	}

	public Object rollBackSerialReviews(String[] serialNos) {
		BaseUser user = SessionUtil.getUser();

		for (String serialNo : serialNos) {
			String learnId = reviewMapper.selectLearnIdBySerialNo(serialNo,
					FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED);
			if (!StringUtil.hasValue(learnId)) {
				continue;
			}
			// 添加变更记录
			List<BdStdPayInfoResponse> payinfos = reviewMapper.selectPaidInfo(serialNo);
			String item = "";
			if (payinfos != null && payinfos.size() > 0) {
				for (BdStdPayInfoResponse pay : payinfos) {
					item = item + pay.getItemCode() + ":" + pay.getItemName() + ",";
				}
			}
			if (StringUtil.hasValue(item)) {
				item = item.substring(0, item.length() - 1);
			}
			modifyService.addStudentModifyRecord(learnId, "缴费科目：" + item + " 撤销收费审核");
		}

		return reviewMapper.rollBackSerialReviews(serialNos, user.getUserId(), user.getRealName());
	}

	@Transactional(readOnly = true)
	public void exportSerialInfo(BdFeeReviewQuery feeReview, HttpServletResponse response, BdExcel excel) {
		if (null == feeReview) {
			throw new BusinessException("E000094"); // 数据量过大，请选择筛选条件
		}

		if (!StringUtil.hasValue(feeReview.getStartTime()) || !StringUtil.hasValue(feeReview.getEndTime())) {
			throw new BusinessException("E000095"); // 数据量过大，请选择时间段
		}

		ByteArrayOutputStream os = null;

		try {
			// 对导出工具进行字段填充
			ExcelUtil.IExcelConfig<BdFeeReview> testExcelCofing = new ExcelUtil.IExcelConfig<BdFeeReview>();
			testExcelCofing.setSheetName("index").setType(BdFeeReview.class)
					.addTitle(new ExcelUtil.IExcelTitle("缴费日期", "payDate"))
					.addTitle(new ExcelUtil.IExcelTitle("时间", "payTime"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("招生类型", "recruitType"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
					.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
					.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
					.addTitle(new ExcelUtil.IExcelTitle("学员阶段", "stdStage"))
					.addTitle(new ExcelUtil.IExcelTitle("缴费科目", "paidItems"))
					.addTitle(new ExcelUtil.IExcelTitle("缴费金额", "amount"))
					.addTitle(new ExcelUtil.IExcelTitle("收款方式", "paymentType"))
					.addTitle(new ExcelUtil.IExcelTitle("收款账号", "payeeName"))
					.addTitle(new ExcelUtil.IExcelTitle("智米抵扣", "zmDeduction"))
					.addTitle(new ExcelUtil.IExcelTitle("滞留抵扣", "accDeduction"))
					.addTitle(new ExcelUtil.IExcelTitle("优惠券抵扣", "couponDeduction"))
					.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruitName"))
					.addTitle(new ExcelUtil.IExcelTitle("招生老师部门", "recruitDepart"))
					.addTitle(new ExcelUtil.IExcelTitle("招生老师校区", "recruitCampus"))
					.addTitle(new ExcelUtil.IExcelTitle("票据号", "serialMark"))
					.addTitle(new ExcelUtil.IExcelTitle("第三方订单号", "outSerialNo"))
					.addTitle(new ExcelUtil.IExcelTitle("收款人", "empName"))
					// .addTitle(new ExcelUtil.IExcelTitle("收款账号", ""))
					.addTitle(new ExcelUtil.IExcelTitle("是否审核", "serialStatus"))
					.addTitle(new ExcelUtil.IExcelTitle("审核人", "checkUser"));

			List<BdFeeReview> list = reviewMapper.selectFeeReviewExport(feeReview);

			for (BdFeeReview re : list) {
				StringBuffer bs = new StringBuffer();
				if (null != re.getPayInfos() && re.getPayInfos().size() > 0) {
					for (BdStdPayInfoResponse sp : re.getPayInfos()) {
						log.debug("------------------------ 缴费信息：" + JsonUtil.object2String(sp));
						if (null != sp && StringUtil.hasValue(sp.getItemCode())
								&& StringUtil.hasValue(sp.getItemName())) {
							bs.append(sp.getItemCode()).append(":").append(sp.getItemName()).append("; ");
						}
					}
				}

				re.setPaidItems(bs.toString());

				String recruitType = re.getRecruitType();
				if (StringUtil.hasValue(recruitType)) {
					recruitType = dictExchangeUtil.getParamKey("recruitType", recruitType);
					re.setRecruitType(recruitType);
				} else {
					re.setRecruitType("");
				}

				String payTime = re.getPayTime();
				String[] t = payTime.split(" ");
				re.setPayDate(t[0]);
				re.setPayTime(t[1]);

				String zmDeduction = re.getZmDeduction();
				if (StringUtil.hasValue(zmDeduction)) {
					re.setZmDeduction(zmDeduction);
				} else {
					re.setZmDeduction("0");
				}
				String accDeduction = re.getAccDeduction();
				if (StringUtil.hasValue(accDeduction)) {
					re.setAccDeduction(accDeduction);
				} else {
					re.setAccDeduction("0");
				}

				String couponDeduction = re.getCouponDeduction();
				if (StringUtil.hasValue(couponDeduction)) {
					re.setCouponDeduction(couponDeduction);
				} else {
					re.setCouponDeduction("0");
				}

				String stdStage = dictExchangeUtil.getParamKey("stdStage", re.getStdStage());
				re.setStdStage(stdStage);
				String scholarship = dictExchangeUtil.getParamKey("scholarship", re.getScholarship());
				re.setScholarship(scholarship);
				String paymentType = dictExchangeUtil.getParamKey("paymentType", re.getPaymentType());
				re.setPaymentType(paymentType);
				if (FinanceConstants.STUDENT_SERIAL_STATUS_UNCHECK.equals(re.getSerialStatus())) {
					re.setSerialStatus("否");
				} else {
					re.setSerialStatus("是");
				}

				String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", re.getPfsnLevel());
				re.setPfsnLevel(pfsnLevel);

				String pfsnCode = re.getPfsnCode();
				String pfsnName = re.getPfsnName();

				pfsnName = pfsnName + "(" + pfsnCode + ")";
				re.setPfsnName(pfsnName);

			}

			SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

			os = new ByteArrayOutputStream();
			wb.write(os);
			String bucket = yzSysConfig.getBucket();
			String realFilePath = Type.EXCEL.get() + "/" + "feeReview" + "/" + excel.getExcelName() + ".xlsx";
			FileUploadUtil.upload(bucket, realFilePath, os.toByteArray());

			// 记录下载地址
			excel.setExcelUrl(realFilePath);
			excel.setExportStatus(ExcelConstants.EXCEL_STATUS_FINISHED);

			excelMapper.updateByPrimaryKeySelective(excel);
			log.error("----------------------------------- 收费审核成功导出，excelId：" + excel.getExcelId() + "，下载地址："
					+ realFilePath);

		} catch (IOException e) {
			// 导出异常，记录日志，改变状态
			excel.setExportStatus(ExcelConstants.EXCEL_STATUS_FAILED);
			excel.setRemark(e.getMessage());
			excelMapper.updateByPrimaryKeySelective(excel);

		} finally {
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	public Object countAmount(BdFeeReviewQuery feeReview) {
		String amount = reviewMapper.selectCountAmount(feeReview);
		BigDecimal count = BigDecimal.ZERO;
		if (StringUtil.hasValue(amount)) {
			count = AmountUtil.str2Amount(amount);
		}
		return count.toString();
	}

	/**
	 * 导出收费审核到本地
	 * 
	 * @param feeReview
	 * @param response
	 */
	public void exportSerialInfoClienJUnitTest(BdFeeReviewQuery feeReview, HttpServletResponse response) {
		if (null == feeReview) {
			throw new BusinessException("E000094"); // 数据量过大，请选择筛选条件
		}

		if (!StringUtil.hasValue(feeReview.getStartTime()) || !StringUtil.hasValue(feeReview.getEndTime())) {
			throw new BusinessException("E000095"); // 数据量过大，请选择时间段
		}

		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdFeeReview> testExcelCofing = new ExcelUtil.IExcelConfig<BdFeeReview>();
		testExcelCofing.setSheetName("index").setType(BdFeeReview.class)
				.addTitle(new ExcelUtil.IExcelTitle("缴费日期", "payTime"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("招生类型", "recruitType"))
				.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
				.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("学员阶段", "stdStage"))
				.addTitle(new ExcelUtil.IExcelTitle("缴费科目", "paidItems"))
				.addTitle(new ExcelUtil.IExcelTitle("缴费金额", "amount"))
				.addTitle(new ExcelUtil.IExcelTitle("收款方式", "paymentType"))
				.addTitle(new ExcelUtil.IExcelTitle("智米抵扣", "zmDeduction"))
				.addTitle(new ExcelUtil.IExcelTitle("滞留抵扣", "accDeduction"))
				.addTitle(new ExcelUtil.IExcelTitle("优惠券抵扣", "couponDeduction"))
				.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruitName"))
				.addTitle(new ExcelUtil.IExcelTitle("招生老师部门", "recruitDepart"))
				.addTitle(new ExcelUtil.IExcelTitle("招生老师校区", "recruitCampus"))
				.addTitle(new ExcelUtil.IExcelTitle("票据号", "serialMark"))
				.addTitle(new ExcelUtil.IExcelTitle("第三方订单号", "outSerialNo"))
				.addTitle(new ExcelUtil.IExcelTitle("收款人", "empName"))
				// .addTitle(new ExcelUtil.IExcelTitle("收款账号", ""))
				.addTitle(new ExcelUtil.IExcelTitle("是否审核", "serialStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("审核人", "checkUser"));

		List<BdFeeReview> list = reviewMapper.selectFeeReviewExport(feeReview);

		for (BdFeeReview re : list) {
			StringBuffer bs = new StringBuffer();
			if (null != re.getPayInfos() && re.getPayInfos().size() > 0) {
				for (BdStdPayInfoResponse sp : re.getPayInfos()) {
					log.debug("------------------------ 缴费信息：" + JsonUtil.object2String(sp));
					if (null != sp && StringUtil.hasValue(sp.getItemCode()) && StringUtil.hasValue(sp.getItemName())) {
						bs.append(sp.getItemCode()).append(":").append(sp.getItemName()).append("; ");
					}
				}
			}
			re.setPaidItems(bs.toString());

			String recruitType = re.getRecruitType();
			if (StringUtil.hasValue(recruitType)) {
				recruitType = dictExchangeUtil.getParamKey("recruitType", recruitType);
				re.setRecruitType(recruitType);
			} else {
				re.setRecruitType("");
			}

			String zmDeduction = re.getZmDeduction();
			if (StringUtil.hasValue(zmDeduction)) {
				re.setZmDeduction(zmDeduction);
			} else {
				re.setZmDeduction("0");
			}
			String accDeduction = re.getAccDeduction();
			if (StringUtil.hasValue(accDeduction)) {
				re.setAccDeduction(accDeduction);
			} else {
				re.setAccDeduction("0");
			}

			String couponDeduction = re.getCouponDeduction();
			if (StringUtil.hasValue(couponDeduction)) {
				re.setCouponDeduction(couponDeduction);
			} else {
				re.setCouponDeduction("0");
			}

			String stdStage = dictExchangeUtil.getParamKey("stdStage", re.getStdStage());
			re.setStdStage(stdStage);
			String scholarship = dictExchangeUtil.getParamKey("scholarship", re.getScholarship());
			re.setScholarship(scholarship);
			String paymentType = dictExchangeUtil.getParamKey("paymentType", re.getPaymentType());
			re.setPaymentType(paymentType);
			if (FinanceConstants.STUDENT_SERIAL_STATUS_UNCHECK.equals(re.getSerialStatus())) {
				re.setSerialStatus("否");
			} else {
				re.setSerialStatus("是");
			}

			String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", re.getPfsnLevel());
			String pfsnCode = re.getPfsnCode();
			String pfsnName = re.getPfsnName();

			pfsnName = "[" + pfsnLevel + "]" + pfsnName + "(" + pfsnCode + ")";
			re.setPfsnName(pfsnName);

		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;

		OutputStream os = null;
		try {
			/*
			 * response.setContentType("application/vnd.ms-excel");
			 * response.setHeader("Content-disposition",
			 * "attachment;filename=SerialInfo.xls"); out =
			 * response.getOutputStream();
			 */
			// wb.write(out);

			File date = new File("E:\\finance.xls");
			date.createNewFile();
			os = new FileOutputStream(date);

			wb.write(os);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();

				/*
				 * out.flush(); out.close();
				 */
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void exportSerial(BdFeeReviewQuery feeReview, HttpServletResponse response, BdExcel excel) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				exportSerialInfo(feeReview, response, excel);
			}
		});
	}

}
