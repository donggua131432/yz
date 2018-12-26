package com.yz.service.invite;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yz.dao.common.BaseInfoMapper;
import com.yz.dao.invite.UsStudentRemarkMapper;
import com.yz.model.invite.UsStudentRemark;
import com.yz.model.oa.OaEmpFollowInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FinanceConstants;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.us.UsFollowLogMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.finance.AtsAccount;
import com.yz.model.invite.InviteUserList;
import com.yz.model.us.UsFollowLog;
import com.yz.util.CodeUtil;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Transactional
@Service
public class InviteUserService {

	private static final Logger log = LoggerFactory.getLogger(InviteUserService.class);
	@Autowired
	private InviteUserMapper inviteUserMapper;

	@Autowired
	private InviteUserMapper userMapper;

	@Autowired
	private OaEmployeeMapper employeeMapper;

	@Autowired
	private AtsAccountMapper accMapper;

	@Autowired
	private UsFollowLogMapper logMapper;

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private BaseInfoMapper baseInfoMapper;

	@Autowired
	private UsStudentRemarkMapper usStudentRemarkMapper;

	public IPageInfo<InviteUserList> getList(InviteUserQuery queryInfo) {
		List<String> dpList = baseInfoMapper.getDpIdList(queryInfo.getDpManager());
		queryInfo.setDpIds(dpList);

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<InviteUserList> list = inviteUserMapper.getList(queryInfo);

		if (list == null)
			return null;
		for (InviteUserList listInfo : list) {
			log.debug("要转换的昵称:" + listInfo.getNickname());
			listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
			listInfo.setpNickname(CodeUtil.base64Decode2String(listInfo.getpNickname()));
			String userId = listInfo.getUserId();
			String empId = listInfo.getEmpId();

			// 设置跟进人,校监信息
			OaEmpFollowInfo empInfo = employeeMapper.getEmpFollowInfo(empId);
			if (empInfo != null) {
				listInfo.setEmpName(empInfo.getEmpName());
				listInfo.setEmpMobile(empInfo.getMobile());
				listInfo.setEmpStatus(empInfo.getEmpStatus());
				listInfo.setDpManager(empInfo.getDpManager());
				listInfo.setDpManagerName(empInfo.getDpManagerName());
			}

			listInfo.setAllotCount(logMapper.selectCountByUserId(userId));

			// 查询账户余额
			List<Map<String, String>> accList = accMapper.getAccountList(userId);
			listInfo.setAccList(accList);
		}
		return new IPageInfo<InviteUserList>((Page<InviteUserList>) list);
	}

	public IPageInfo<UsFollowLog> getLogs(int start, int pageSize, String userId) {
		PageHelper.offsetPage(start, pageSize);
		List<UsFollowLog> logs = logMapper.selectByUserId(userId);
		if (logs != null) {
			for (UsFollowLog l : logs) {
				List<Map<String, String>> info = userMapper.getOaInfo(l);
				String nEmpName = null;
				String oEmpName = null;
				String nDpName = null;
				String oDpName = null;
				String nCName = null;
				String oCName = null;

				if (info != null) {
					for (Map<String, String> m : info) {
						String type = m.get("nType");
						String name = m.get("name");
						switch (type) {
						case "oe":
							oEmpName = name;
							break;
						case "ne":
							nEmpName = name;
							break;
						case "od":
							oDpName = name;
							break;
						case "nd":
							nDpName = name;
							break;
						case "oc":
							oCName = name;
							break;
						case "nc":
							nCName = name;
							break;
						}
					}
				}
				if (StringUtil.isEmpty(oEmpName))
					oEmpName = "无";
				l.setEmpChange(oEmpName + " ===> " + (StringUtil.hasValue(nEmpName) ? nEmpName : "无"));
				if (StringUtil.isEmpty(oDpName))
					oDpName = "无";
				l.setDpChange(oDpName + " ===> " + (StringUtil.hasValue(nDpName) ? nDpName : "无"));
				if (StringUtil.isEmpty(oCName))
					oCName = "无";
				l.setCampusChange(oCName + " ===> " + (StringUtil.hasValue(nCName) ? nCName : "无"));
				Date d = l.getCreateTime();
				if (d != null)
					l.setCreateTimeStr(DateUtil.formatDate(d, DateUtil.YYYYMMDDHHMMSS_SPLIT));
			}
			return new IPageInfo<UsFollowLog>((Page<UsFollowLog>) logs);
		}
		return null;
	}

	public Object getAccount(int start, int length, String userId) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = accountMapper.getAccountSerial(FinanceConstants.ACC_TYPE_ZHIMI, userId, null);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public AtsAccount getAccountInfo(String userId) {
		AtsAccount account = new AtsAccount();
		account.setUserId(userId);
		account.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		return accountMapper.getAccount(account);
	}


	public List<UsStudentRemark> getUsStudentRemark(String userId, String stdId){
		return usStudentRemarkMapper.selectUsStudentRemark(userId,stdId);
	}

	public void updateUsStudentRemark(UsStudentRemark usStudentRemark){
		usStudentRemarkMapper.updateUsStudentRemark(usStudentRemark);
	}

	public void addUsStudentRemark(UsStudentRemark usStudentRemark){
		usStudentRemark.setId(IDGenerator.generatorId());
		usStudentRemarkMapper.insertUsStudentRemark(usStudentRemark);
	}

	public void deleteUsStudentRemarkById(String userId, String stdId){
		usStudentRemarkMapper.deleteUsStudentRemarkById(userId,stdId);
	}


	public void updateIntentionType(String userId, String intentionType){
		inviteUserMapper.updateIntentionType(userId, intentionType);
	}


	public IPageInfo<InviteUserList> inviteUserSearch(InviteUserQuery queryInfo) {
		if (StringUtil.hasValue(queryInfo.getName()) || StringUtil.hasValue(queryInfo.getMobile()) ||
				StringUtil.hasValue(queryInfo.getYzCode())){
			PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
			List<InviteUserList> list = inviteUserMapper.getList(queryInfo);
			if (list == null) return null;
			for (InviteUserList listInfo : list) {
				log.debug("要转换的昵称:" + listInfo.getNickname());
				listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
				listInfo.setpNickname(CodeUtil.base64Decode2String(listInfo.getpNickname()));
				String userId = listInfo.getUserId();
				String empId = listInfo.getEmpId();
				// 设置跟进人,校监信息
				OaEmpFollowInfo empInfo = employeeMapper.getEmpFollowInfo(empId);
				if (empInfo != null) {
					listInfo.setEmpName(empInfo.getEmpName());
					listInfo.setEmpMobile(empInfo.getMobile());
					listInfo.setEmpStatus(empInfo.getEmpStatus());
					listInfo.setDpManager(empInfo.getDpManager());
					listInfo.setDpManagerName(empInfo.getDpManagerName());
				}
				listInfo.setAllotCount(logMapper.selectCountByUserId(userId));
				// 查询账户余额
				List<Map<String, String>> accList = accMapper.getAccountList(userId);
				listInfo.setAccList(accList);
			}
			return new IPageInfo<InviteUserList>((Page<InviteUserList>) list);
		}else{
			return new IPageInfo<InviteUserList>();
		}
	}

}
