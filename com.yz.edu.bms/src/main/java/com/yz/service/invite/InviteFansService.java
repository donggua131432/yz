package com.yz.service.invite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;

import com.yz.constants.WechatMsgConstants;
import com.yz.dao.common.BaseInfoMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.dao.system.SysDictMapper;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.oa.OaEmpFollowInfo;
import com.yz.model.oa.OaEmployeeLearnInfo;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.system.SysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.UsConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.invite.InviteFansMapper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.us.UsFollowLogMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.exception.CustomException;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.invite.InviteFansQuery;
import com.yz.model.invite.InviteAssignInfo;
import com.yz.model.invite.InviteFansList;
import com.yz.model.invite.InviteUserInfo;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.us.UsFollow;
import com.yz.model.us.UsFollowLog;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.CodeUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class InviteFansService {

	@Autowired
	private InviteFansMapper fansMapper;

	@Autowired
	private InviteUserMapper userMapper;

	@Autowired
	private OaEmployeeMapper employeeMapper;

	@Autowired
	private AtsAccountMapper accMapper;

	@Autowired
	private UsFollowLogMapper logMapper;

	@Autowired
	private UsFollowMapper followMapper;

	@Autowired
	private UsFollowLogMapper flMapper;

	@Autowired
	private OaEmployeeMapper eMapper;

	@Autowired
	private BaseInfoMapper baseInfoMapper;

	@Autowired
	private StudentRecruitMapper studentRecruitMapper;



	@Autowired
	private SysDictMapper sysDictMapper;

	@Autowired
	private InviteUserMapper inviteUserMapper;

	/**
	 * 获取我的粉丝集合信息
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<InviteFansList> getAssignedList(InviteUserQuery queryInfo) {
		//获取当前用户的员工ID
		BaseUser user = SessionUtil.getUser();
		queryInfo.setEmpId(user.getEmpId());

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<InviteFansList> fansList = fansMapper.getAssignedList(queryInfo);
		if (fansList == null) return null;

		for (InviteFansList listInfo : fansList) {
			listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
			listInfo.setpNickname(CodeUtil.base64Decode2String(listInfo.getpNickname()));
			String empId = listInfo.getEmpId();
			String dpManager = listInfo.getDpManager();

			//设置跟进人信息 校监信息
			OaEmpFollowInfo empInfo = employeeMapper.getEmpFollowInfo(empId);
			if (empInfo != null) {
				listInfo.setEmpName(empInfo.getEmpName());
				listInfo.setEmpMobile(empInfo.getMobile());
				listInfo.setEmpStatus(empInfo.getEmpStatus());
				listInfo.setDpManager(empInfo.getDpManager());
				listInfo.setDpManagerName(empInfo.getDpManagerName());
			}

			//获取日志记录条数
			listInfo.setAllotCount(logMapper.selectCountByUserId(listInfo.getUserId()));

			// 查询账户余额
			List<Map<String, String>> accList = accMapper.getAccountList(listInfo.getUserId());
			listInfo.setAccList(accList);
		}
		return new IPageInfo<InviteFansList>((Page<InviteFansList>) fansList);
	}


	/**
	 * 获取我的下属粉丝信息
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<InviteFansList> getSubFansList(InviteUserQuery queryInfo) {
		//queryInfo.setEmpId(queryInfo.getEmpId());
		//获取下属员工ID集合
		BaseUser user = SessionUtil.getUser();
		List<String> empIdList = baseInfoMapper.getSubEmpIdList(user.getEmpId());
		queryInfo.setEmpIds(empIdList);

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<InviteFansList> fansList = fansMapper.getSubFansList(queryInfo);
		if (fansList == null) return null;
		for (InviteFansList listInfo : fansList) {
			listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
			listInfo.setpNickname(CodeUtil.base64Decode2String(listInfo.getpNickname()));
			String empId = listInfo.getEmpId();
			String dpManager = listInfo.getDpManager();
			//设置跟进人信息 校监信息
			OaEmpFollowInfo empInfo = employeeMapper.getEmpFollowInfo(empId);
			if (empInfo != null) {
				listInfo.setEmpName(empInfo.getEmpName());
				listInfo.setEmpMobile(empInfo.getMobile());
				listInfo.setEmpStatus(empInfo.getEmpStatus());
				listInfo.setDpManager(empInfo.getDpManager());
				listInfo.setDpManagerName(empInfo.getDpManagerName());
			}

			//获取日志记录条数
			listInfo.setAllotCount(logMapper.selectCountByUserId(listInfo.getUserId()));
			// 查询账户余额
			List<Map<String, String>> accList = accMapper.getAccountList(listInfo.getUserId());
			listInfo.setAccList(accList);
		}
		return new IPageInfo<InviteFansList>((Page<InviteFansList>) fansList);
	}


	/**
	 * 获取未分配的粉丝信息
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<InviteFansList> getUndistributedList(InviteFansQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<InviteFansList> fansList = fansMapper.getUndistributedList(queryInfo);

		if (fansList == null)
			return null;

		for (InviteFansList listInfo : fansList) {
			listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
			String userId = listInfo.getUserId();
			String pUserId = listInfo.getpId();

			Map<String, String> userInfo = userMapper.getUserInfo(pUserId);
			if (userInfo != null && !userInfo.isEmpty()) {
				listInfo.setpNickname(CodeUtil.base64Decode2String(userInfo.get("nickname")));
				listInfo.setpIdCard(userInfo.get("idCard"));
				listInfo.setpMobile(userInfo.get("mobile"));
				listInfo.setpMobileLocation(userInfo.get("mobileLocation"));
				listInfo.setpName(userInfo.get("name"));
				listInfo.setpYzCode(userInfo.get("yzCode"));
			}

			// 查询账户余额
			List<Map<String, String>> accList = accMapper.getAccountList(userId);
			listInfo.setAccList(accList);
		}
		return new IPageInfo<InviteFansList>((Page<InviteFansList>) fansList);
	}

	/**
	 * 分配跟进人
	 * @param assignInfo
	 */
	public void addAssign(InviteAssignInfo assignInfo) {
		String[] userIds = assignInfo.getUserIds();
		String empId = assignInfo.getEmpId();
		OaEmployeeBaseInfo baseInfo = eMapper.getEmpBaseInfo(empId);
		//获取招生老师所属部门校区信息
		OaEmpFollowInfo oaEmpFollowInfo = employeeMapper.getEmpFollowInfo(empId);
		//获取招生老师openId
		String openId = followMapper.selectOpenIdByEmpId(empId);

		for (String userId : userIds) {
			Map<String,String> learnMap = studentRecruitMapper.getLearnIdByUserId(userId);
			UsFollow follow = followMapper.selectByPrimaryKey(userId);
			assignInfo.setUserId(userId);
			assignInfo.setEmpStatus("1");//在职
			//assignInfo.setAssignFlag("1");//学校分配
			String assignTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			assignInfo.setAssignTime(assignTime);//分配时间

			UsFollowLog followLog = new UsFollowLog();
			followLog.setUserId(userId);
			followLog.setCreateTime(new Date());
			followLog.setCreateUser(assignInfo.getCreateUser());
			followLog.setCreateUserId(assignInfo.getCreateUserId());
			followLog.setDrId(assignInfo.getEmpId());
			followLog.setDrName(baseInfo.getEmpName());
			followLog.setDrType(UsConstants.DR_TYPE_ZSLS);
			followLog.setRemark("分配给招生老师");
			
			if (follow != null) {
				fansMapper.updateFollow(assignInfo);
				followLog.setOldEmpId(follow.getEmpId());
				followLog.setOldDpId(follow.getDpId());
				followLog.setOldCampusId(follow.getCampusId());
				followLog.setEmpId(assignInfo.getEmpId());
				followLog.setDpId(assignInfo.getDpId());
				followLog.setCampusId(assignInfo.getCampusId());
			} else {
				fansMapper.insertFollow(assignInfo);
				followLog.setEmpId(assignInfo.getEmpId());
				followLog.setDpId(assignInfo.getDpId());
				followLog.setCampusId(assignInfo.getCampusId());
			}

			//变更记录1：报读记录也同步刷新到该招生老师下面
			//变更记录2：只有意向学员才会刷新招生老师
			//变更记录3：智哥名下的粉丝和离职招生老师名下的意向学员才会刷新到该招生老师名下
			//变更记录4：智哥名下的粉丝和离职招生老师名下的缴费学员才会刷新到该招生老师名下，未离职的招生老师分配自己的粉丝
			//根据userId获取最新的一条报读记录
			if(learnMap != null && learnMap.size()>0){
				String learnId = learnMap.get("learn_id");
				//String stdStage = learnMap.get("std_stage");
				String recruitId = StringUtil.hasValue(learnMap.get("emp_id")) ? learnMap.get("emp_id"):"";
				String empStatus = learnMap.get("emp_status");
				BdLearnRules bdLearnRules = new BdLearnRules();
				bdLearnRules.setLearnId(learnId);
				bdLearnRules.setRecruit(oaEmpFollowInfo.getEmpId());
				bdLearnRules.setRecruitDpId(oaEmpFollowInfo.getDpId());
				bdLearnRules.setRecruitCampusId(oaEmpFollowInfo.getCampusId());
				bdLearnRules.setRecruitCampusManager(oaEmpFollowInfo.getCampusManager());
				String assignFlag = StringUtil.hasValue(assignInfo.getAssignFlag()) ? assignInfo.getAssignFlag():learnMap.get("assign_flag");
				bdLearnRules.setAssignFlag(assignFlag);

				//判断是否为智哥
				if("".equals(recruitId) || "1193".equals(recruitId) || "538".equals(recruitId)){
					refreshBdLearnRules(learnId, bdLearnRules);
				}else{
					//获取学员缴纳Y0[成教],Y1[国开]费用信息
					Map<String,String> feeMap = studentRecruitMapper.getFeeByLearnId(learnId);
					//如果不是智哥判断招生老师是否已经离职且该学员是否已经缴费
					if("2".equals(empStatus)){
						if(feeMap == null || feeMap.size()<=0){
							refreshBdLearnRules(learnId, bdLearnRules);
						}
					}else{
						BaseUser user = SessionUtil.getUser();
						//如果未离职判断分配的是不是自己的粉丝
						if(recruitId.equals(user.getEmpId()) && (feeMap == null || feeMap.size()<=0)){
							refreshBdLearnRules(learnId, bdLearnRules);
						}
					}
				}
			}
			followLog.setRecrodsNo(IDGenerator.generatorId());
			//添加分配记录
			flMapper.insertSelective(followLog);

			//推送微信公众号信息
			//根据userId获取用户信息
			Map<String,String> userMap = inviteUserMapper.getUserInfo(userId);
			String scholarship = "无";
			if(userMap.get("scholarship") != null && !"".equals(userMap.get("scholarship"))){
				SysDict sysDict = sysDictMapper.selectByPrimaryKey("scholarship."+userMap.get("scholarship"));
				scholarship = sysDict.getDictName();
			}

			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			msgVo.setTemplateId(WechatMsgConstants.PUB_MSG_STUDENT_ASSIGN);
			msgVo.addData("userName", userMap.get("name"));
			msgVo.addData("assignTime", assignTime);
			msgVo.addData("mobile", userMap.get("mobile"));
			msgVo.addData("scholarship", scholarship);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}


		//===========给招生老师发送分配消息提醒 批量  记录一下免得又特么反悔============
		/*// 微信推送消息
		String msg = "您好，系统给您分配了粉丝进行跟进，请登录学员系统——我的粉丝进行查看！";
		//推送客服微信公众号信息
		WechatSendServiceMsgUtil.wechatSendServiceMsg(openId, msg);*/
	}

	/**
	 * 刷新学员招生老师
	 * @param learnId
	 * @param bdLearnRules
	 */
	public void refreshBdLearnRules(String learnId, BdLearnRules bdLearnRules){
		//判断是添加关系还是修改关系
		OaEmployeeLearnInfo oaEmployeeLearnInfo = employeeMapper.getEmpLearnInfosByLearnId(learnId);
		if(oaEmployeeLearnInfo != null){
			studentRecruitMapper.updateLearnRules(bdLearnRules);
		}else{
			studentRecruitMapper.insertLearnRules(bdLearnRules);
		}
	}


	public List<InviteUserInfo> getUser(String[] userIds) {
		List<InviteUserInfo> l = fansMapper.getUser(userIds);
		if (l == null) return null;
		for (InviteUserInfo u : l) {
			String nickname = u.getNickname();
			if (StringUtil.hasValue(nickname)) {
				u.setNickname(CodeUtil.base64Decode2String(nickname));
			}
		}
		return l;
	}

	/**
	 * 分配校监
	 * @param assignInfo
	 */
	public void addAssignXJ(InviteAssignInfo assignInfo) {
		String[] userIds = assignInfo.getUserIds();
		String empId = assignInfo.getEmpId();
		Map<String, String> xjInfo = fansMapper.getXJInfo(empId);
		if(xjInfo == null) {
			throw new CustomException("校监没有负责的部门");
		}
		
		String dpId = xjInfo.get("dpId");
		String campusId = xjInfo.get("campusId");
		String empName = xjInfo.get("empName");

		for (String userId : userIds) {
			UsFollow follow = followMapper.selectByPrimaryKey(userId);
			assignInfo.setUserId(userId);
			assignInfo.setCampusId(campusId);
			assignInfo.setDpId(dpId);
			assignInfo.setEmpId(null);
			//assignInfo.setAssignFlag("1");//学校分配
			
			UsFollowLog followLog = new UsFollowLog();
			followLog.setUserId(userId);
			followLog.setCreateTime(new Date());
			followLog.setCreateUser(assignInfo.getCreateUser());
			followLog.setCreateUserId(assignInfo.getCreateUserId());
			followLog.setDrId(empId);
			followLog.setDrName(empName);
			followLog.setDrType(UsConstants.DR_TYPE_XJ);
			followLog.setRemark("分配给校监");
			
			if (follow != null) {
				fansMapper.updateFollow(assignInfo);
				followLog.setOldEmpId(follow.getEmpId());
				followLog.setOldDpId(follow.getDpId());
				followLog.setOldCampusId(follow.getCampusId());
				followLog.setEmpId(follow.getEmpId());
				followLog.setDpId(assignInfo.getDpId());
				followLog.setCampusId(assignInfo.getCampusId());
			} else {
				fansMapper.insertFollow(assignInfo);
				followLog.setDpId(assignInfo.getDpId());
				followLog.setCampusId(assignInfo.getCampusId());
			}
			followLog.setRecrodsNo(IDGenerator.generatorId());
			flMapper.insertSelective(followLog);
		}
	}

}
