package com.yz.service.refund;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.api.AtsAccountApi;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.refund.StdRefundMapper;
import com.yz.dao.transfer.BdStudentOutMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.finance.AtsAccount;
import com.yz.model.refund.BdCheckWeight;
import com.yz.model.refund.BdRefundQuery;
import com.yz.model.refund.BdRefundReponse;
import com.yz.model.refund.BdStudentRefund;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.service.transfer.BdCheckRecordService;
import com.yz.util.AmountUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StdRefundService {

	@Autowired
	private StdRefundMapper refundMapper;

	@Autowired
	private BdCheckRecordService checkRecordService;

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private BdStudentOutMapper outMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@SuppressWarnings("unchecked")
	public void addStudentRefund(BdStudentRefund refund) {

		int exsit = refundMapper.selectByLearnId(refund.getLearnId());
		if (exsit > 0) {
			throw new BusinessException("E000063"); // 重复提交
		}

		BigDecimal amount = AmountUtil.str2Amount(refund.getRefundAmount());

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("E000064"); // 金额必须大于0
		}

		refund.setCheckType(TransferConstants.CHECK_TYPE_REFUND);

		BaseUser user = SessionUtil.getUser();
		refund.setEmpId(user.getEmpId());
		refund.setRefundAmount(amount.toString());
		refund.setRefundId(IDGenerator.generatorId());
		refundMapper.insertSelective(refund);

		String stdId = refundMapper.selectStdId(refund.getLearnId());
		// 设置转账对象
		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		body.put("stdId", stdId);
		body.put("amount", amount);
		body.put("action", FinanceConstants.ACC_ACTION_OUT);
		body.put("excDesc", "学员提滞留金申请扣款");// TODO 描述补充 2017/7/28 to 倪宇鹏
		body.put("mappingId", refund.getRefundId());
		body.put("updateUser", refund.getUpdateUser());
		body.put("updateUserId", refund.getUpdateUserId());

		accountApi.trans(body);

		// 获取审核权重
		List<BdCheckWeight> checkWeights = refundMapper.getCheckWeight(TransferConstants.CHECK_TYPE_REFUND);

		for (BdCheckWeight w : checkWeights) {
			BdCheckRecord checkRecord = new BdCheckRecord();
			if (TransferConstants.CHECK_RECORD_ORDER_FIRST.equals(w.getCheckOrder())) {
				checkRecord.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
			}
			// 初始化审核记录
			checkRecord.setMappingId(refund.getRefundId());
			checkRecord.setCheckOrder(w.getCheckOrder());
			checkRecord.setCheckType(w.getCheckType());
			checkRecord.setJtId(w.getJtId());
			checkRecordService.addBdCheckRecord(checkRecord);
		}

	}

	public List<BdCheckWeight> getCheckWeight(String checkType) {
		return refundMapper.getCheckWeight(checkType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object selectRefundInfoByPage(int start, int length, BdRefundQuery query) {
		PageHelper.offsetPage(start, length);
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();

		if (jtList != null) {
			if (jtList.contains("400") || jtList.contains("CWZJ") || jtList.contains("CW")) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
			}
		}

		List<BdRefundReponse> list = refundMapper.selectRefundByPage(query, user);
		if (list != null & list.size() > 0) {
			for (BdRefundReponse response : list) {
				AtsAccount account = new AtsAccount();

				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);
				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				// 滞留账户金额
				response.setDemurrageAmount(accAmount);
				if (account != null) {
					account.setAccType(FinanceConstants.ACC_TYPE_NORMAL);
				}
				account = accountMapper.getAccount(account);
				String cashAmount = "0.00";
				if (account != null) {
					cashAmount = account.getAccAmount();
					if (StringUtil.isEmpty(cashAmount)) {
						cashAmount = "0.00";
					}
				}
				// 现金账户金额
				response.setCashAmount(cashAmount);
			}
		}

		return new IPageInfo<BdRefundReponse>((Page) list);
	}

	public BdRefundReponse selectRefundInfo(String refundId) {
		return refundMapper.selectRefundInfo(refundId);
	}

	public Object findDirectorApproval(int start, int length, BdRefundQuery query) {
		PageHelper.offsetPage(start, length);
		BaseUser user = SessionUtil.getUser();
		List<BdRefundReponse> o = refundMapper.selectDirectorApproval(query, user);
		if (o != null && o.size() > 0) {
			for (BdRefundReponse response : o) {
				AtsAccount account = new AtsAccount();

				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);
				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				// 滞留账户金额
				response.setDemurrageAmount(accAmount);
			}
		}
		return new IPageInfo((Page) o);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findStudentInfo(int rows, int page, String stdName, String phone, String idCard) {
		PageHelper.startPage(page, rows);
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		if (jtList != null) {
			// TODO
			if (jtList.contains("400")) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
			} else if (jtList.contains("FDY")) { // 班主任
				user.setUserLevel("6");
			} else if (jtList.contains("XJZZ")) { // 学籍组长
				user.setUserLevel("7");
			}
		}
		List<Map<String, String>> o = refundMapper.findStudentInfo(stdName, phone, idCard, user);
		return new IPageInfo((Page) o);
	}

	@SuppressWarnings("unchecked")
	public void cancelRefunds(String[] refundIds) {
		String[] cancelIds = refundMapper.selectUnCheckRefund(refundIds);
		if (cancelIds != null && cancelIds.length > 0) {

			BaseUser user = SessionUtil.getUser();

			for (String id : cancelIds) {
				BdRefundReponse refund = refundMapper.selectRefundInfo(id);
				Body body = new Body();
				body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
				body.put("stdId", refund.getStdId());
				body.put("amount", refund.getRefundAmount());
				body.put("action", FinanceConstants.ACC_ACTION_IN);
				body.put("excDesc", "学员提现申请扣款");// TODO 描述补充 2017/7/28 to 倪宇鹏
				body.put("mappingId", refund.getRefundId());
				body.put("updateUser", user.getRealName());
				body.put("updateUserId", user.getUserId());
				accountApi.trans(body);
			}
			refundMapper.deleteRefunds(cancelIds);
		}
	}

	public Object findStudentInfoByGraStdId(String learnId) {
		Map<String, String> o = outMapper.findStudentInfoByGraStdId(learnId);
		if (o != null) {

			AtsAccount account = new AtsAccount();

			account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
			account.setStdId(o.get("stdId"));

			account = accountMapper.getAccount(account);
			String accAmount = "0.00";
			if (account != null) {
				accAmount = account.getAccAmount();
				if (StringUtil.isEmpty(accAmount)) {
					accAmount = "0.00";
				}
			}
			o.put("accAmount", accAmount);
		}
		return o;
	}

	/**
	 * 校监审核通过
	 * 
	 * @param refundId
	 */
	public void checkDirector(String refundId) {
		BaseUser user = SessionUtil.getUser();

		BdStudentRefund refund = new BdStudentRefund();
		refund.setRefundId(refundId);
		refund.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
		refund.setUpdateUser(user.getRealName());
		refund.setUpdateUserId(user.getUserId());
		// 修改退费记录
		refundMapper.updateRefundCheckOrder(refund);

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		bcr.setEmpId(user.getEmpId());
		bcr.setMappingId(refundId);
		bcr.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
		bcr.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);

		// 修改当前审核记录为审核通过
		checkRecordService.updateBdCheckRecord(bcr);

		BdCheckRecord c = new BdCheckRecord();
		c.setUpdateUser(user.getRealName());
		c.setUpdateUserId(user.getUserId());
		c.setMappingId(refundId);
		c.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
		c.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
		// 开启财务审核记录
		checkRecordService.updateBdCheckRecord(c);
	}

	/**
	 * 退费驳回
	 * 
	 * @param refundId
	 *            退费ID
	 * @param reason
	 *            原因
	 * @param checkOrder
	 *            驳回记录审核排序
	 */
	@SuppressWarnings("unchecked")
	public void checkReject(String refundId, String reason, String checkOrder) {
		BaseUser user = SessionUtil.getUser();
		BdStudentRefund re = new BdStudentRefund();
		re.setRefundId(refundId);
		re.setUpdateUser(user.getRealName());
		re.setUpdateUserId(user.getUserId());
		// 驳回.. 状态修改为已完成
		refundMapper.finishRefund(re);

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		bcr.setEmpId(user.getEmpId());
		bcr.setMappingId(refundId);
		bcr.setReason(reason);
		bcr.setCheckOrder(checkOrder);
		bcr.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);

		BdRefundReponse refund = refundMapper.selectRefundInfo(refundId);
		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		body.put("stdId", refund.getStdId());
		body.put("amount", refund.getRefundAmount());
		body.put("action", FinanceConstants.ACC_ACTION_IN);
		body.put("excDesc", "学员提现申请扣款");// TODO 描述补充 2017/7/28 to 倪宇鹏
		body.put("mappingId", refund.getRefundId());
		body.put("updateUser", user.getRealName());
		body.put("updateUserId", user.getUserId());
		accountApi.trans(body);

		refundMapper.updateReject(bcr);
	}

	/**
	 * 财务审批列表
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findFinancialApproval(int start, int length, BdRefundQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdRefundReponse> o = refundMapper.selectFinancialApproval(query);
		if (o != null && o.size() > 0) {
			for (BdRefundReponse response : o) {
				AtsAccount account = new AtsAccount();

				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);
				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				// 滞留账户金额
				response.setDemurrageAmount(accAmount);
			}
		}
		return new IPageInfo((Page) o);
	}

	/**
	 * 校办审批列表
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findPrincipalApproval(int start, int length, BdRefundQuery query) {
		PageHelper.offsetPage(start, length);
		BaseUser user = SessionUtil.getUser();
		List<BdRefundReponse> o = refundMapper.selectPrincipalApproval(query, user);
		if (o != null && o.size() > 0) {
			for (BdRefundReponse response : o) {
				AtsAccount account = new AtsAccount();

				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);
				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				// 滞留账户金额
				response.setDemurrageAmount(accAmount);
			}
		}
		return new IPageInfo((Page) o);
	}

	/**
	 * 财务审核
	 * 
	 * @param refundId
	 */
	public void checkFinancial(String refundId) {
		BaseUser user = SessionUtil.getUser();

		BdStudentRefund refund = new BdStudentRefund();
		refund.setRefundId(refundId);
		refund.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
		refund.setUpdateUser(user.getRealName());
		refund.setUpdateUserId(user.getUserId());
		// 修改退费记录
		refundMapper.updateRefundCheckOrder(refund);

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		bcr.setEmpId(user.getEmpId());
		bcr.setMappingId(refundId);
		bcr.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
		bcr.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);

		// 修改当前审核记录为审核通过
		checkRecordService.updateBdCheckRecord(bcr);

		BdCheckRecord c = new BdCheckRecord();
		c.setUpdateUser(user.getRealName());
		c.setUpdateUserId(user.getUserId());
		c.setMappingId(refundId);
		c.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
		c.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
		// 开启校办审核记录
		checkRecordService.updateBdCheckRecord(c);
	}

	/**
	 * 校办审批
	 * 
	 * @param refundId
	 */
	@SuppressWarnings("unchecked")
	public void checkPricipal(String refundId) {
		BaseUser user = SessionUtil.getUser();
		BdRefundReponse refund = refundMapper.selectRefundInfo(refundId);

		if (GlobalConstants.TRUE.equals(refund.getIsComplete())) {
			throw new BusinessException("E000119"); // 该退款已完成，请勿重复提交
		}

		BdStudentRefund re = new BdStudentRefund();
		re.setRefundId(refundId);
		re.setUpdateUser(user.getRealName());
		re.setUpdateUserId(user.getUserId());
		refundMapper.finishRefund(re);

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		bcr.setEmpId(user.getEmpId());
		bcr.setMappingId(refundId);
		bcr.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
		bcr.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);

		// 修改当前审核记录为审核通过
		checkRecordService.updateBdCheckRecord(bcr);
		// 设置转账对象
		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_NORMAL);
		body.put("stdId", refund.getStdId());
		body.put("amount", refund.getRefundAmount());
		body.put("action", FinanceConstants.ACC_ACTION_IN);
		body.put("excDesc", "学员退费成功，转入现金账户");// TODO 描述补充 2017/7/28 to 倪宇鹏
		body.put("mappingId", refund.getRefundId());
		body.put("updateUser", user.getRealName());
		body.put("updateUserId", user.getUserId());
		accountApi.trans(body);
	}

	/**
	 * 撤销审核
	 * 
	 * @param refundId
	 * @param checkOrder
	 */
	public void rollBackCheck(String refundId, String checkOrder) {
		BaseUser user = SessionUtil.getUser();

		BdStudentRefund refund = new BdStudentRefund();
		refund.setRefundId(refundId);
		refund.setCheckOrder(checkOrder);
		refund.setUpdateUser(user.getRealName());
		refund.setUpdateUserId(user.getUserId());
		// 修改退费记录
		refundMapper.updateRefundCheckOrder(refund);

		BdCheckRecord bcr = new BdCheckRecord();
		bcr.setUpdateUser(user.getRealName());
		bcr.setUpdateUserId(user.getUserId());
		bcr.setEmpId(user.getEmpId());
		bcr.setMappingId(refundId);
		bcr.setCheckOrder(checkOrder);
		bcr.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);

		// 修改当前审核记录为审核通过
		checkRecordService.updateBdCheckRecord(bcr);

		// 下一个状态回退
		String order = String.valueOf(Integer.parseInt(checkOrder) + 1);

		BdCheckRecord c = new BdCheckRecord();
		c.setUpdateUser(user.getRealName());
		c.setUpdateUserId(user.getUserId());
		c.setMappingId(refundId);
		c.setCheckOrder(order);
		c.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_UNOPEN);
		// 开启校办审核记录
		checkRecordService.updateBdCheckRecord(c);

	}

}
