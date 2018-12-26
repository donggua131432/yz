package com.yz.service.oa;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.UsCertApi;
import com.yz.dao.invite.InviteFansMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.PageCondition;
import com.yz.model.communi.Body;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.oa.EmpModifyListInfo;
import com.yz.model.oa.EmployeeSelectInfo;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.oa.OaEmployeeLearnInfo;
import com.yz.model.oa.OaEmployeeModifyInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;
import com.yz.model.system.SysDict;
import com.yz.redis.RedisService;
import com.yz.service.system.SysDictService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class OaEmployeeService {

	private static final Logger log = LoggerFactory.getLogger(OaEmployeeService.class);
	@Autowired
	private OaEmployeeMapper empMapper;

	@Reference(version = "1.0")
	private UsCertApi usCertApi;

	@Autowired
	private InviteFansMapper fansMapper;

	@Autowired
	private SysDictService sysDictService;

	public IPageInfo<EmployeeSelectInfo> getTutorList(SelectQueryInfo queryInfo) {
		PageHelper.startPage(queryInfo.getPage(), queryInfo.getRows());
		List<EmployeeSelectInfo> list = empMapper.getTutorList(queryInfo);
		return new IPageInfo<EmployeeSelectInfo>((Page<EmployeeSelectInfo>) list);
	}
	
	/**
	 * 所有在职员工的列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> findAllKeyValue(String eName) {
		return empMapper.findAllKeyValue(eName);
	}

	/**
	 * 根据状态获取所有员工列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> findKeyValueByStatus(String eName) {
		return empMapper.findKeyValueByStatus(eName, "");
	}
	
	/**
	 * 根据状态获取所有员工列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> findKeyValueByStatus(String eName,String empStatus) {
		return empMapper.findKeyValueByStatus(eName, empStatus);
	}

	/**
	 * 某个员工的基本信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeBaseInfo getEmpBaseInfo(String empId) {
		OaEmployeeBaseInfo baseInfo = empMapper.getEmpBaseInfo(empId);
		if (baseInfo == null)
			return new OaEmployeeBaseInfo();
		return baseInfo;
	}

	/**
	 * 某个员工的其他附属信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeOtherInfo getEmpOtherInfo(String empId, String annexType) {
		OaEmployeeOtherInfo otherInfo = empMapper.getEmpOtherInfo(empId, annexType);
		if (otherInfo == null)
			return new OaEmployeeOtherInfo();
		return otherInfo;
	}

	/**
	 * 某个证件类型下的证件号是否存在
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	public Object ifExistInfo(String idType, String idCard) {
		int count = empMapper.getCountBy(idType, idCard);

		if (count == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 某个证件类型下的证件号是否存在教师
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	public Object ifExistInfoByEmpType(String idType, String idCard,String empType) {
		int count = empMapper.getCountByEmpType(idType, idCard,empType);
		if (count == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 某个员工的具体职位信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeJobInfo getEmployeeJobInfo(String empId) {
		OaEmployeeJobInfo jobInfo = empMapper.getEmployeeJobInfo(empId);
		if (jobInfo == null)
			return new OaEmployeeJobInfo();
		return jobInfo;
	}

	/**
	 * 职位信息的修改
	 * 
	 * @param jobInfo
	 */
	@SuppressWarnings({ "unchecked" })
	public void empJobUpdate(OaEmployeeJobInfo jobInfo, String[] jdIds, BaseUser user) {
		// 招生老师老的部门信息
		OaEmployeeJobInfo oldJobInfo = empMapper.getEmployeeJobInfo(jobInfo.getEmpId());

		List<Map<String, String>> jdIdMap = empMapper.findEmpTitle(oldJobInfo.getEmpId());
		StringBuilder sb = new StringBuilder();
		String oldTitle = null;
		if (null != jdIdMap && jdIdMap.size() > 0) {
			for (int i = 0; i < jdIdMap.size(); i++) {
				sb.append(jdIdMap.get(i).get("jtId") + ",");
			}
			oldTitle = sb.toString();
		}
		StringBuilder sb2 = new StringBuilder();
		String newTitle = null;
		if (null != jdIds && jdIds.length > 0) {
			for (int i = 0; i < jdIds.length; i++) {
				sb2.append(jdIds[i] + ",");
			}
			newTitle = sb2.toString();
		}
		// 校区,部门,招生组,职位有一个发生变化时候
		String oldInfo = StringUtil
				.string2Unicode(oldJobInfo.getCampusId() + oldJobInfo.getDpId() + oldJobInfo.getGroupId() + oldTitle);

		String newInfo = StringUtil
				.string2Unicode(jobInfo.getCampusId() + jobInfo.getDpId() + jobInfo.getGroupId() + newTitle);

		if (!oldInfo.equals(newInfo)) {

			// 新增部门信息变更记录
			OaEmployeeModifyInfo modifyLog = empMapper.getEmpModifyInfoId(jobInfo.getEmpId());

			OaEmployeeModifyInfo modifyInfo = new OaEmployeeModifyInfo();
			if (StringUtil.hasValue(oldJobInfo.getCampusId())) {
				if (null != modifyLog) {
					// 在再次变更前,需要判断老的学员是否变更完全
					int totalCount = empMapper.totalLearnCount(modifyLog.getOldDpId(), jobInfo.getEmpId(),modifyLog.getModifyId());
					int finishCount = empMapper.finishLearnCount(modifyLog.getOldDpId(), jobInfo.getEmpId(),modifyLog.getModifyId());
					if (totalCount != finishCount) {
						throw new BusinessException("E000093");
					}
					// 最后一条变更的截止时间
					String endTime = empMapper.getEmpModifyEndTime(modifyLog.getModifyId());
					modifyInfo.setBeginTime(endTime);
				} else {
					modifyInfo.setBeginTime(jobInfo.getEntryDate());
				}
				modifyInfo.setEmpId(jobInfo.getEmpId());

				modifyInfo.setOldCampusId(oldJobInfo.getCampusId());
				modifyInfo.setOldDpId(oldJobInfo.getDpId());
				modifyInfo.setOldGroupId(oldJobInfo.getGroupId());
				modifyInfo.setOldRecruitCode(oldJobInfo.getRecruitCode());
				modifyInfo.setOldTitle(oldTitle);

				modifyInfo.setNewCampusId(jobInfo.getCampusId());
				modifyInfo.setNewDpId(jobInfo.getDpId());
				modifyInfo.setNewGroupId(jobInfo.getGroupId());
				modifyInfo.setNewRecruitCode(jobInfo.getRecruitCode());
				modifyInfo.setNewTitle(newTitle);

				modifyInfo.setCreateUser(user.getRealName());
				modifyInfo.setCreateUserId(user.getUserId());
				modifyInfo.setStatus("2"); // 已完成
				modifyInfo.setModifyId(IDGenerator.generatorId());

				empMapper.addEmpModifyInfo(modifyInfo);

				// 把转部门前涉及到的学员 放到记录表中
				List<OaEmployeeLearnInfo> empLearnInfos = empMapper.getEmpLearnInfos(jobInfo.getEmpId(),
						oldJobInfo.getDpId());
				if (null != empLearnInfos && empLearnInfos.size() > 0) {
					for (OaEmployeeLearnInfo learnInfo : empLearnInfos) {
						learnInfo.setModifyId(modifyInfo.getModifyId());
						learnInfo.setRecrodsNo(IDGenerator.generatorId());
					}
					log.debug("------------- empId :[ " + jobInfo.getEmpId() + "]招生信息 : "
							+ JsonUtil.object2String(empLearnInfos));
					empMapper.insertEmpLearnInfos(empLearnInfos);
				}
			}
		}

		// 再插入一条信息的记录
		if (StringUtil.isEmpty(jobInfo.getLeaveDate())) {
			jobInfo.setLeaveDate(null);
		}
		if (StringUtil.isEmpty(jobInfo.getTurnRightTime())) {
			jobInfo.setTurnRightTime(null);
		}
		if (null != jdIds && jdIds.length > 0) {
			empMapper.deleteEmpTitle(jobInfo.getEmpId());
			empMapper.insertEmpJdIds(jobInfo.getEmpId(), jdIds);
		}
		empMapper.empJobUpdate(jobInfo);
		if (jobInfo.getIfNeedRelate().equals("0")) {
			if (empMapper.checkUserIfExistsByYzCode(jobInfo.getYzId()) < 1) {
				throw new BusinessException("E000201");
			}
			// 绑定员工关系
			Body body = new Body();
			body.put("type", "EMP");
			body.put("yzCode", jobInfo.getYzId());
			body.put("empId", jobInfo.getEmpId());
			usCertApi.createRelation(body);
		}

		/*
		 * if("2".equals(jobInfo.getEmpStatus())) {//离职清理用户跟进关系
		 * //不用远程调用,直接多数据源切换 2017-11-09 List<Map<String, String>> list =
		 * fansMapper.selectList(jobInfo.getEmpId());
		 * log.debug("------------------------------- empId :[ " +
		 * jobInfo.getEmpId() + "]先记录一下， 免的那群鬼又TM反悔 GGG : " +
		 * JsonUtil.object2String(list));
		 */
		fansMapper.clearFollow(jobInfo.getEmpId(), jobInfo.getEmpStatus());
		// empMapper.clearLearnRules(jobInfo.getEmpId());
		// }
		if("2".equals(jobInfo.getEmpStatus())){
			//还原教师的身份
			empMapper.updateEmpUserIdByEmpId(jobInfo.getEmpId());
			//改变用户的角色
			if(StringUtil.isNotBlank(oldJobInfo.getUserId())){
				fansMapper.updateUserRelationByUserId(oldJobInfo.getUserId());
				//同时清空session
				AppSessionHolder.delSessionInfo(oldJobInfo.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
			}
		}
	}

	/**
	 * 根据生效时间定时修改员工信息
	 * 
	 * @param jobInfo
	 * @param jdIds
	 * @param user
	 * @throws ParseException
	 */
	public void empJobUpdateOnEffectTime(OaEmployeeJobInfo jobInfo, String[] jdIds, BaseUser user)
			throws ParseException {

		String effectTime = jobInfo.getEffectTime();

		long nowTime = System.currentTimeMillis();
		long effect = DateUtil.stringToLong(effectTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		if (effect < nowTime) {
			throw new BusinessException("E000123"); // 生效时间不能小于当前时间
		}

		OaEmployeeJobInfo oldJobInfo = empMapper.getEmployeeJobInfo(jobInfo.getEmpId());
		List<Map<String, String>> jdIdMap = empMapper.findEmpTitle(oldJobInfo.getEmpId());
		StringBuilder sb = new StringBuilder();
		String oldTitle = null;
		if (null != jdIdMap && jdIdMap.size() > 0) {
			for (int i = 0; i < jdIdMap.size(); i++) {
				sb.append(jdIdMap.get(i).get("jtId") + ",");
			}
			oldTitle = sb.toString();
		}
		StringBuilder sb2 = new StringBuilder();
		String newTitle = null;
		if (null != jdIds && jdIds.length > 0) {
			for (int i = 0; i < jdIds.length; i++) {
				sb2.append(jdIds[i] + ",");
			}
			newTitle = sb2.toString();
		}

		// 查询是否有未完成修改，并删除
		List<OaEmployeeModifyInfo> modifyInfos = empMapper.selectUnModifyInfos(jobInfo.getEmpId());
		if (!modifyInfos.isEmpty()) {
			// 从redis删除任务
			for (OaEmployeeModifyInfo modifyInfo : modifyInfos) {
				// 删除变更记录
				empMapper.deleteEmpModifyInfo(modifyInfo.getModifyId());
				// 删除定时任务
				RedisService.getRedisService().zrem(YzTaskConstants.YZ_MSG_PUB_TASK_SET, modifyInfo.getJobInfo());
			}
		}

		OaEmployeeModifyInfo modifyInfo = new OaEmployeeModifyInfo();
		// 校区,部门,招生组,职位有一个发生变化时候
		String oldInfo = StringUtil
				.string2Unicode(oldJobInfo.getCampusId() + oldJobInfo.getDpId() + oldJobInfo.getGroupId() + oldTitle);

		String newInfo = StringUtil
				.string2Unicode(jobInfo.getCampusId() + jobInfo.getDpId() + jobInfo.getGroupId() + newTitle);

		if (!oldInfo.equals(newInfo)) {

			// 新增部门信息变更记录
			OaEmployeeModifyInfo modifyLog = empMapper.getEmpModifyInfoId(jobInfo.getEmpId());

			if (StringUtil.hasValue(oldJobInfo.getCampusId())) {
				if (null != modifyLog) {
					// 在再次变更前,需要判断老的学员是否变更完全
					int totalCount = empMapper.totalLearnCount(modifyLog.getOldDpId(), jobInfo.getEmpId(),modifyLog.getModifyId());
					int finishCount = empMapper.finishLearnCount(modifyLog.getOldDpId(), jobInfo.getEmpId(),modifyLog.getModifyId());
					if (totalCount != finishCount) {
						throw new BusinessException("E000093");
					}
					// 最后一条变更的截止时间
					String endTime = empMapper.getEmpModifyEndTime(modifyLog.getModifyId());
					modifyInfo.setBeginTime(endTime);
				} else {
					modifyInfo.setBeginTime(jobInfo.getEntryDate());
				}

				modifyInfo.setOldCampusId(oldJobInfo.getCampusId());
				modifyInfo.setOldDpId(oldJobInfo.getDpId());
				modifyInfo.setOldGroupId(oldJobInfo.getGroupId());
				modifyInfo.setOldRecruitCode(oldJobInfo.getRecruitCode());
				modifyInfo.setOldTitle(oldTitle);

				modifyInfo.setNewCampusId(jobInfo.getCampusId());
				modifyInfo.setNewDpId(jobInfo.getDpId());
				modifyInfo.setNewGroupId(jobInfo.getGroupId());
				modifyInfo.setNewRecruitCode(jobInfo.getRecruitCode());
				modifyInfo.setNewTitle(newTitle);

			}
		}
		modifyInfo.setEmpId(jobInfo.getEmpId());
		modifyInfo.setCreateUser(user.getRealName());
		modifyInfo.setCreateUserId(user.getUserId());
		modifyInfo.setEffectTime(jobInfo.getEffectTime());
		jobInfo.setJdIds(jdIds);
		jobInfo.setUserId(user.getUserId());
		jobInfo.setUserRealName(user.getRealName());
		modifyInfo.setModifyId(IDGenerator.generatorId());
		modifyInfo.setJobInfo(JsonUtil.object2String(jobInfo));
		modifyInfo.setStatus("1"); // 待执行
		empMapper.addEmpModifyInfo(modifyInfo);
		empMapper.updateEmpJObEffectTime(jobInfo.getEmpId(), effectTime);

		// 加入redis任务
		RedisService.getRedisService().zadd(YzTaskConstants.YZ_EMP_MODIFY_TASK,
				DateUtil.stringToLong(jobInfo.getEffectTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT),
				JsonUtil.object2String(jobInfo));

	}

	public List<EmployeeSelectInfo> findEmployeeByName(String sName, String campusId) {
		return empMapper.findEmployeeByName(sName, campusId);
	}

	/**
	 * 某个部门下的员工
	 * 
	 * @param dpId
	 * @return
	 */
	public List<Map<String, String>> findAllListByDpId(String dpId) {
		return empMapper.findAllListByDpId(dpId);
	}

	public void aloneUpdateEmpJob(OaEmployeeJobInfo jobInfo) {
		empMapper.aloneUpdateEmpJob(jobInfo);
	}

	public IPageInfo<EmpModifyListInfo> getModifyList(String empId, PageCondition pc) {
		PageHelper.offsetPage(pc.getStart(), pc.getLength());
		List<EmpModifyListInfo> list = empMapper.getModifyList(empId);
		if (null != list && list.size() > 0) {
			for (EmpModifyListInfo modifyInfo : list) {
				StringBuffer modifyText = new StringBuffer();
				// 校区
				String oldCampusName = StringUtil.hasValue(modifyInfo.getOldCampusName())
						? modifyInfo.getOldCampusName() : "无";
				String newCampusName = StringUtil.hasValue(modifyInfo.getNewCampusName())
						? modifyInfo.getNewCampusName() : "无";
				String oldCampusNameUnicode = StringUtil.string2Unicode(oldCampusName);
				String newCampusNameUnicode = StringUtil.string2Unicode(newCampusName);

				if (!oldCampusNameUnicode.equals(newCampusNameUnicode)) {
					modifyText.append("校区变更:").append(oldCampusName).append(" ==> ").append(newCampusName)
							.append("<br/>");
				}
				// 部门
				String oldDpName = StringUtil.hasValue(modifyInfo.getOldDpName()) ? modifyInfo.getOldDpName() : "无";
				String newDpName = StringUtil.hasValue(modifyInfo.getNewDpName()) ? modifyInfo.getNewDpName() : "无";
				String oldDpNameUnicode = StringUtil.string2Unicode(oldCampusName);
				String newDpNameUnicode = StringUtil.string2Unicode(newCampusName);
				if (!oldDpNameUnicode.equals(newDpNameUnicode)) {
					modifyText.append("部门变更:").append(oldDpName).append(" ==> ").append(newDpName).append("<br/>");
				}
				// 招生组
				String oldGroupName = StringUtil.hasValue(modifyInfo.getOldGroupName()) ? modifyInfo.getOldGroupName()
						: "无";
				String newGroupName = StringUtil.hasValue(modifyInfo.getNewGroupName()) ? modifyInfo.getNewGroupName()
						: "无";

				String oldGroupNameUnicode = StringUtil.string2Unicode(oldGroupName);
				String newGroupNameUnicode = StringUtil.string2Unicode(newGroupName);
				if (!oldGroupNameUnicode.equals(newGroupNameUnicode)) {
					modifyText.append("招生组变更:").append(oldGroupName).append(" ==> ").append(newGroupName)
							.append("<br/>");
				}
				// 职位
				String oldTitle = modifyInfo.getOldTitle();
				String newTitle = modifyInfo.getNewTitle();
				if (StringUtil.hasValue(newTitle) && StringUtil.hasValue(oldTitle) && !oldTitle.equals(newTitle)) {
					StringBuilder oldTitleSb = new StringBuilder();

					String[] oldTitles = oldTitle.split(",");
					if (null != oldTitles && oldTitles.length > 0) {
						for (int i = 0; i < oldTitles.length; i++) {
							SysDict dict = sysDictService.getDict("jtId." + oldTitles[i]);
							String oldValue = dict == null ? "" : dict.getDictName();
							oldTitleSb.append(oldValue + ",");
						}
					}
					StringBuilder newTitleSb = new StringBuilder();
					String[] newTitles = newTitle.split(",");
					if (null != newTitles && newTitles.length > 0) {
						for (int i = 0; i < newTitles.length; i++) {
							SysDict dict = sysDictService.getDict("jtId." + newTitles[i]);
							String newValue = dict == null ? "" : dict.getDictName();
							newTitleSb.append(newValue + ",");
						}
					}
					modifyText.append("职位变更:").append(oldTitleSb.toString()).append(" ==> ")
							.append(newTitleSb.toString()).append("<br/>");
				}

				modifyInfo.setModifyText(modifyText.toString());
			}
		}

		Page<EmpModifyListInfo> page = (Page<EmpModifyListInfo>) list;

		return new IPageInfo<EmpModifyListInfo>(list, page.getTotal());
	}

	/**
	 * 最后一次的变更记录
	 * 
	 * @param empId
	 * @return
	 */
	public EmpModifyListInfo getLastModifyInfo(String empId) {
		EmpModifyListInfo modifyInfo = empMapper.getLastModifyInfo(empId);
		if (null == modifyInfo) {
			return new EmpModifyListInfo();
		}

		if (StringUtil.hasValue(modifyInfo.getOldTitle())) {
			StringBuilder oldTitleSb = new StringBuilder();
			String[] oldTitles = modifyInfo.getOldTitle().split(",");
			if (null != oldTitles && oldTitles.length > 0) {
				for (int i = 0; i < oldTitles.length; i++) {
					SysDict dict = sysDictService.getDict("jtId." + oldTitles[i]);
					String oldValue = dict == null ? "" : dict.getDictName();
					oldTitleSb.append(oldValue + ",");
				}
			}
			modifyInfo.setOldTitle(oldTitleSb.toString());
		}
		if (StringUtil.hasValue(modifyInfo.getNewTitle())) {
			StringBuilder newTitleSb = new StringBuilder();
			String[] newTitles = modifyInfo.getNewTitle().split(",");
			if (null != newTitles && newTitles.length > 0) {
				for (int i = 0; i < newTitles.length; i++) {
					SysDict dict = sysDictService.getDict("jtId." + newTitles[i]);
					String newValue = dict == null ? "" : dict.getDictName();
					newTitleSb.append(newValue + ",");
				}
			}
			modifyInfo.setNewTitle(newTitleSb.toString());
		}
		return modifyInfo;
	}

}
