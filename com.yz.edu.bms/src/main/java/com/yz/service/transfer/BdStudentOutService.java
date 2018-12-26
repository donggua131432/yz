package com.yz.service.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.CheckConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.transfer.BdStudentOutMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.transfer.ApprovalMap;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.BdStudentOut;
import com.yz.model.transfer.BdStudentOutExport;
import com.yz.model.transfer.BdStudentOutMap;
import com.yz.service.finance.BdOrderService;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentOutService {
	private static final Logger log = LoggerFactory.getLogger(BdStudentOutService.class);
	
	@Autowired
	private YzSysConfig yzSysConfig ; 
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdStudentOutMapper studentOutMapper;
	@Autowired
	private BdOrderService orderService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentChangeService studentChangeService;
	@Autowired
	private BdStudentModifyService modifyService;

	public IPageInfo<BdStudentOutMap> findAllBdStudentOut(int start, int length, BdStudentOutMap bdStudentOut) {
		BaseUser user = SessionUtil.getUser();

		List<String> jtList = user.getJtList();

		if (jtList != null) {
			if (jtList.contains("400") || jtList.contains("CWZJ") || jtList.contains("CW")
					|| GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
			} else if (null != user.getMyDpList() && user.getMyDpList().size() > 0) {
				user.setUserLevel("3");
			} else if (jtList.contains("XJZZ") || jtList.contains("BMZR")) { // 学籍组长
				user.setUserLevel("11");
			} else if (jtList.contains("CJXJ")) {
				user.setUserLevel("12");
			} else if (jtList.contains("GKXJ")) {
				user.setUserLevel("13");
			} else if (jtList.contains("FDY")) {
				user.setUserLevel("10");
			} else if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
				user.setUserLevel("14");
			}
		}
		PageHelper.offsetPage(start, length);
		List<BdStudentOutMap> list = studentOutMapper.findAllBdStudentOut(bdStudentOut, user);
		IPageInfo<BdStudentOutMap> page = new IPageInfo<BdStudentOutMap>((Page<BdStudentOutMap>) list);
		return page;
	}

	public IPageInfo<Map<String, String>> findStudentInfo(int page, int rows, String sName) {
		BaseUser user = SessionUtil.getUser();
		// 此处再针对班主任做数据权限2017-11-29
		List<String> jtList = user.getJtList();

		if (jtList != null) {
			// TODO
			if (jtList.contains("400") || GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
			} else if (null != user.getMyDpList() && user.getMyDpList().size() > 0) {
				user.setUserLevel("3");
			} else if (jtList.contains("XJZZ") || jtList.contains("BMZR")) { // 学籍组长
				user.setUserLevel("11");
			} else if (jtList.contains("CJXJ")) {
				user.setUserLevel("12");
			} else if (jtList.contains("GKXJ")) {
				user.setUserLevel("13");
			} else if (jtList.contains("FDY")) {
				user.setUserLevel("10");
			} else if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
				user.setUserLevel("14");
			}
		}
		PageHelper.startPage(page, rows);
		return new IPageInfo<Map<String, String>>(
				(Page<Map<String, String>>) studentOutMapper.findStudentInfo(sName, user));
	}

	public void addStudentOut(BdStudentOut studentOut, MultipartFile attachment) {

		int count = studentOutMapper.selectOutCount(studentOut.getLearnId());
		if (count > 0) {
			throw new BusinessException("E000117"); // 请勿重复提交申请
		}

		// 学员状态是否为在读
		if (StudentConstants.STD_STAGE_STUDYING.equals(studentOut.getStdStage())) {
			// 审批需要通过教务
			studentOut.setCheckType(TransferConstants.CHECK_TYPE_OUT_AFTER);
		} else {
			// 审批不需要通过教务
			studentOut.setCheckType(TransferConstants.CHECK_TYPE_OUT_BEFORE_STUDYING);
		}

		studentOut.setOutId(IDGenerator.generatorId());
		studentOutMapper.insertSelective(studentOut);
		/*
		 * 根据审核类型生成审核流程结束
		 */
		orderService.studentOrderRefund(studentOut.getLearnId(),studentOut.getStdId(), studentOut.getCheckType());
		log.debug("--------------初始化订单退款成功---------------");
		/*
		 * 根据审核类型生成审核流程开始
		 */
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		if (StudentConstants.STD_STAGE_STUDYING.equals(studentOut.getStdStage())) {
			// 审批需要通过教务
			checkWeights = getCheckWeight(TransferConstants.CHECK_TYPE_OUT_AFTER);
		} else {
			// 审批不需要通过教务
			checkWeights = getCheckWeight(TransferConstants.CHECK_TYPE_OUT_BEFORE_STUDYING);
		}
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentOut.getOutId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecordService.addBdCheckRecord(checkRecord);
		}

		// 添加退学 学员变更记录
		BdStudentModify studentModify = new BdStudentModify();
		studentModify.setLearnId(studentOut.getLearnId());
		studentModify.setStdId(studentOut.getStdId());
		studentModify.setExt1("添加了退学学员申请!");
		studentModify.setIsComplete("1");
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
		modifyService.addStudentModifyRecord(studentModify);

		if (null != attachment) {

			String bucket = yzSysConfig.getBucket();
			String fileName = attachment.getOriginalFilename();
			try {
				StringBuffer s = new StringBuffer(Type.OUT.get()).append("/");

				if (StringUtil.hasValue(studentOut.getOutId())) {
					s.append(studentOut.getOutId()).append("/");
				}
				s.append(fileName);
				String url = s.toString();

				FileUploadUtil.upload(bucket, url, attachment.getBytes());
				studentOutMapper.updateFileUrl(studentOut.getOutId(), url, fileName);
			} catch (Exception e) {
				log.error("--------------- 上传文件失败");
				throw new BusinessException("E000118"); // 文件上传失败；
			}

		}
	}

	public List<Map<String, String>> getCheckWeight(String stdStage) {
		// TODO Auto-generated method stub
		return studentOutMapper.getCheckWeight(stdStage);
	}

	public Map<String, String> findStudentInfoById(String learnId) {
		// TODO Auto-generated method stub
		return studentOutMapper.findStudentInfoById(learnId);
	}

	public List<Map<String, String>> findFinancialApproval(BdStudentOutMap studentOutMap) {
		// TODO Auto-generated method stub
		return studentOutMapper.findFinancialApproval(studentOutMap);
	}

	public void updateBdStudentOut(BdStudentOut bso) {
		// TODO Auto-generated method stub
		studentOutMapper.updateByPrimaryKeySelective(bso);
	}

	public List<BdStudentOutMap> findDirectorApproval(BdStudentOutMap studentOutMap) {
		// TODO Auto-generated method stub
		return studentOutMapper.findDirectorApproval(studentOutMap, SessionUtil.getUser());
	}

	public List<BdStudentOutMap> findSchoolManagedApproval(BdStudentOutMap studentOutMap) {
		// TODO Auto-generated method stub
		return studentOutMapper.findSchoolManagedApproval(studentOutMap);
	}

	public List<BdStudentOutMap> findSenateApproval(BdStudentOutMap studentOutMap) {
		// TODO Auto-generated method stub
		return studentOutMapper.findSenateApproval(studentOutMap);
	}

	public BdStudentOut selectBdStudentOutBuId(String outId) {
		// TODO Auto-generated method stub
		return studentOutMapper.selectByPrimaryKey(outId);
	}

	public Map<String, String> findStudentInfoByGraStdId(String learnId) {
		// TODO Auto-generated method stub
		return studentOutMapper.findStudentInfoByGraStdId(learnId);
	}

	public List<Map<String, String>> selectOpareRecord(String outId, String order) {
		// TODO Auto-generated method stub
		return studentOutMapper.selectOpareRecord(outId, order);
	}

	public void passSchoolManagedApproval(ApprovalMap approvalMap) {
		// TODO Auto-generated method stub
		// 查询退学学员原状态
		BdStudentOut oldBso = selectBdStudentOutBuId(approvalMap.getOutId());
		// 是否为在读学员
		if ("7".equals(oldBso.getStdStage())) {
			// 审批需要通过教务
			BdStudentOut bso = new BdStudentOut();
			bso.setOutId(approvalMap.getOutId());
			bso.setCheckOrder("4");
			if ("3".equals(approvalMap.getCheckStatus())) {
				bso.setIsComplete("1");
			}

			BdCheckRecord bcr = new BdCheckRecord();
			bcr.setMappingId(approvalMap.getOutId());
			bcr.setCheckOrder("3");
			// 到时候登录动态获取填入
			BaseUser user = SessionUtil.getUser();
			bcr.setEmpId(user.getEmpId());
			// bcr.setEmpId("11");
			bcr.setReason(approvalMap.getReason());
			if (CheckConstants.REJECT_CHECK_3.equals(approvalMap.getCheckStatus())) {
				log.debug("-------------被驳回更新账户状态---------------");
				orderService.rejectRefund(approvalMap.getLearnId());
				bso.setIsComplete(CheckConstants.CHECK_COMPLETE);
			} else {
				List<BdStdPayInfoResponse> items = approvalMap.getItems();
				// 调用接口修改当前学员需返回的金额
				if (null != items && items.size() > 0) {
					log.debug("-------------退费金额修改开始---------------");
					orderService.updateOrderRefundAmount(approvalMap.getLearnId(), items);
					log.debug("-------------退费金额修改成功---------------");
				}
			}
			updateBdStudentOut(bso);
			bcr.setCheckStatus(approvalMap.getCheckStatus());
			checkRecordService.updateBdCheckRecord(bcr);
		} else {
			// 审批不需要通过教务
			BdStudentOut bso = new BdStudentOut();
			bso.setOutId(approvalMap.getOutId());
			bso.setIsComplete("1");
			updateBdStudentOut(bso);
			BdCheckRecord bcr = new BdCheckRecord();
			bcr.setMappingId(approvalMap.getOutId());
			bcr.setCheckOrder("3");
			// 到时候登录动态获取填入
			BaseUser user = SessionUtil.getUser();
			bcr.setEmpId(user.getEmpId());
			// bcr.setEmpId("11");
			bcr.setCheckStatus(approvalMap.getCheckStatus());
			bcr.setReason(approvalMap.getReason());
			checkRecordService.updateBdCheckRecord(bcr);
			if (CheckConstants.PASS_CHECK_2.equals(approvalMap.getCheckStatus())) {
				// 退学
				studentChangeService.exitStudent(oldBso.getLearnId());
				List<BdStdPayInfoResponse> items = approvalMap.getItems();
				// 调用接口修改当前学员需返回的金额
				if (null != items && items.size() > 0) {
					log.debug("-------------退费金额修改开始---------------");
					orderService.updateOrderRefundAmount(approvalMap.getLearnId(), items);
					log.debug("-------------退费金额修改成功---------------");
				}
				log.debug("-------------退费金额修改成功---------------");
				// 退费至现金账户
				log.debug("-------------退费退费至现金账户开始---------------");
				orderService.finishRefund(oldBso.getLearnId());
				log.debug("-------------退费退费至现金账户成功---------------");

				// 退回智米
				log.debug("-------------从智米账户扣减智米开始---------------");
				orderService.giveBackZhimi(oldBso.getLearnId());
				log.debug("-------------从智米账户扣减智米结束---------------");
			} else {
				log.debug("-------------被驳回更新账户状态---------------");
				orderService.rejectRefund(approvalMap.getLearnId());
			}
		}
	}

	public void passFinancialApproval(ApprovalMap approvalMap) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(approvalMap.getOutId());
		bso.setCheckOrder("3");
		bso.setFinancial_remark(approvalMap.getFinancial_remark());
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(approvalMap.getOutId());
		bcr.setCheckOrder("2");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setReason(approvalMap.getReason());
		if (CheckConstants.REJECT_CHECK_3.equals(approvalMap.getCheckStatus())) {
			log.debug("-------------被驳回更新账户状态---------------");
			orderService.rejectRefund(approvalMap.getLearnId());
			bso.setIsComplete(CheckConstants.CHECK_COMPLETE);
		} else {
			// 调用接口修改当前学员需返回的金额
			log.debug("-------------退费金额修改开始---------------");
			List<BdStdPayInfoResponse> items = approvalMap.getItems();
			if (null != items && items.size() > 0) {
				orderService.updateOrderRefundAmount(approvalMap.getLearnId(), items);
			}
			/*
			 * for (ApprovalMap approvalMap2 : approvalMap) {
			 * BdStdPayInfoResponse stdPayInfo = new BdStdPayInfoResponse();
			 * stdPayInfo.setItemCode(approvalMap2.getItemCode());
			 * stdPayInfo.setFeeAmount(approvalMap2.getFeeAmount());
			 * items.add(stdPayInfo); }
			 */
			log.debug("-------------退费金额修改成功---------------");
		}
		updateBdStudentOut(bso);
		bcr.setCheckStatus(approvalMap.getCheckStatus());
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public void passSenateApproval(String outId, String checkStatus, String reason, String learnId) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(outId);
		bso.setCheckOrder("4");
		if ("2".equals(checkStatus)) {
			bso.setIsComplete("1");
		}

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(outId);
		bcr.setCheckOrder("4");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setCheckStatus(checkStatus);
		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);
		if (CheckConstants.PASS_CHECK_2.equals(checkStatus)) {
			// 退学
			studentChangeService.exitStudent(learnId);
			// 退费至现金账户
			log.debug("-------------退费退费至现金账户开始---------------");
			orderService.finishRefund(learnId);
			log.debug("-------------退费退费至现金账户成功---------------");

			//退回智米
			log.debug("-------------从智米账户扣减智米开始---------------");
			orderService.giveBackZhimi(learnId);
			log.debug("-------------从智米账户扣减智米结束---------------");
		} else {
			log.debug("-------------被驳回更新账户状态---------------");
			orderService.rejectRefund(learnId);
			bso.setIsComplete(CheckConstants.CHECK_COMPLETE);
		}
		updateBdStudentOut(bso);
	}

	public void undoSchoolManagedApproval(String outId, String reason) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(outId);
		bso.setCheckOrder("3");
		updateBdStudentOut(bso);
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(outId);
		bcr.setCheckOrder("3");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setCheckStatus(CheckConstants.WAIT_CHECK_1);
		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public void undoFinancialApproval(String outId, String reason) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(outId);
		bso.setCheckOrder("2");
		updateBdStudentOut(bso);
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(outId);
		bcr.setCheckOrder("2");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setCheckStatus(CheckConstants.WAIT_CHECK_1);
		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public void undoDirectorApproval(String outId, String reason) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(outId);
		bso.setCheckOrder("1");
		updateBdStudentOut(bso);
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(outId);
		bcr.setCheckOrder("1");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setCheckStatus(CheckConstants.WAIT_CHECK_1);
		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);
	}

	public void passDirectorApproval(String outId, String checkStatus, String reason, String learnId) {
		// TODO Auto-generated method stub
		BdStudentOut bso = new BdStudentOut();
		bso.setOutId(outId);
		bso.setCheckOrder("2");
		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setMappingId(outId);
		bcr.setCheckOrder("1");
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		bcr.setEmpId(user.getEmpId());
		// bcr.setEmpId("11");
		bcr.setCheckStatus(checkStatus);

		bcr.setReason(reason);
		checkRecordService.updateBdCheckRecord(bcr);

		if (CheckConstants.REJECT_CHECK_3.equals(checkStatus)) {
			log.debug("-------------被驳回更新账户状态---------------");
			orderService.rejectRefund(learnId);
			bso.setIsComplete(CheckConstants.CHECK_COMPLETE);
		}
		updateBdStudentOut(bso);
	}

	public void cancelCheck(String[] outIds) {
		List<BdStudentOut> ids = studentOutMapper.selectUnCheckIds(outIds);
		if (ids != null && ids.size() > 0) {
			for (BdStudentOut out : ids) {
				log.debug("-------------被驳回更新账户状态---------------");
				orderService.rejectRefund(out.getLearnId());
				studentOutMapper.deleteStudentOut(out.getOutId());

				// 添加退学 学员变更记录
				BdStudentModify studentModify = new BdStudentModify();
				studentModify.setLearnId(out.getLearnId());
				studentModify.setStdId(out.getStdId());
				studentModify.setExt1("撤销了退学学员申请!");
				studentModify.setIsComplete("1");
				studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
				modifyService.addStudentModifyRecord(studentModify);
			}
		}
	}

	public String getCheckOrder(String checkType, String jtId) {
		// TODO Auto-generated method stub
		return studentOutMapper.getCheckOrder(checkType, jtId);
	}

	public Map<String, String> selectStdInfo(String learnId) {
		return studentOutMapper.selectStdInfoByLearnId(learnId);
	}

	public void exportOutStdInfo() {
		FileOutputStream os = null;
		try {
			ExcelUtil.IExcelConfig<BdStudentOutExport> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentOutExport>();
			testExcelCofing.setSheetName("index").setType(BdStudentOutExport.class)
					.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("缴费金额", "amount"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruitName"))
					.addTitle(new ExcelUtil.IExcelTitle("审批人", "checkUser"))
					.addTitle(new ExcelUtil.IExcelTitle("审批状态", "checkStatus"))
					.addTitle(new ExcelUtil.IExcelTitle("退款金额", "refundAmount"));
			List<BdStudentOutExport> list = studentOutMapper.selectOutExportInfo();

			for (BdStudentOutExport out : list) {

				List<String> checkUsers = out.getCheckUsers();
				StringBuffer sb = new StringBuffer();
				for (String s : checkUsers) {
					if (StringUtil.hasValue(s)) {
						sb.append(s).append(",");
					}
				}
				out.setCheckUser(sb.toString());

				String checkStatus = null;

				String checkSta = out.getCheckStatus();
				String isComplete = out.getIsComplete();
				if ("1".equals(isComplete)) {
					checkStatus = "审核通过";
				} else {
					checkStatus = "待审核";
				}

				dictExchangeUtil.getParamKey("checkStatus", out.getCheckStatus());
				out.setCheckStatus(checkStatus);
			}

			SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

			// ServletOutputStream out = null;

			/*
			 * response.setContentType("application/vnd.ms-excel");
			 * response.setHeader("Content-disposition",
			 * "attachment;filename=StudentPaymentInfo.xls"); out =
			 * response.getOutputStream(); wb.write(out);
			 */

			File date = new File("F:\\退学退费列表.xlsx");
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
