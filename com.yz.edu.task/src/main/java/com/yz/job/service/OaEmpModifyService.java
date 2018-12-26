package com.yz.job.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.UsCertApi;
import com.yz.job.dao.OaEmployeeMapper;
import com.yz.job.model.OaEmployeeJobInfo;
import com.yz.job.model.OaEmployeeLearnInfo;
import com.yz.model.communi.Body;
import com.yz.session.AppSessionHolder;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
public class OaEmpModifyService {

	private static final Logger log = LoggerFactory.getLogger(OaEmpModifyService.class);

	@Autowired
	private OaEmployeeMapper empMapper;

	@Reference(version = "1.0")
	private UsCertApi usCertApi;

	/**
	 * 职位信息的修改
	 * 
	 * @param jobInfo
	 */
	@SuppressWarnings({ "unchecked" })
	public void empJobUpdate(OaEmployeeJobInfo jobInfo) {

		String[] jdIds = jobInfo.getJdIds();

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
		// 修改变更记录状态
		String modifyId = empMapper.selectModifyIdByEffectTime(jobInfo.getEmpId(), jobInfo.getEffectTime());
		// 校区,部门,招生组,职位有一个发生变化时候
		String oldInfo = StringUtil
				.string2Unicode(oldJobInfo.getCampusId() + oldJobInfo.getDpId() + oldJobInfo.getGroupId() + oldTitle);

		String newInfo = StringUtil
				.string2Unicode(jobInfo.getCampusId() + jobInfo.getDpId() + jobInfo.getGroupId() + newTitle);

		if (!oldInfo.equals(newInfo)) {

			// 把转部门前涉及到的学员 放到记录表中
			List<OaEmployeeLearnInfo> empLearnInfos = empMapper.getEmpLearnInfos(jobInfo.getEmpId(),
					oldJobInfo.getDpId());
			if (null != empLearnInfos && empLearnInfos.size() > 0) {
				for (OaEmployeeLearnInfo learnInfo : empLearnInfos) {
					learnInfo.setModifyId(jobInfo.getModifyId());
				}
				log.debug("------------- empId :[ " + jobInfo.getEmpId() + "]招生信息 : "
						+ JsonUtil.object2String(empLearnInfos));
				empMapper.insertEmpLearnInfos(empLearnInfos);
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
		jobInfo.setYzId(null);
		empMapper.empJobUpdate(jobInfo);
		if (jobInfo.getIfNeedRelate().equals("0")) {
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
		empMapper.clearFollow(jobInfo.getEmpId(), jobInfo.getEmpStatus());
		// empMapper.clearLearnRules(jobInfo.getEmpId());
		// }

		// 修改状态
		if (StringUtil.hasValue(modifyId)) {
			// 变更成功
			empMapper.updateModifyStatus(modifyId, "2");
		}
		if("2".equals(jobInfo.getEmpStatus())){
			//还原教师的身份
			empMapper.updateEmpUserIdByEmpId(jobInfo.getEmpId());
			//改变用户的角色
			if(StringUtil.isNotBlank(oldJobInfo.getUserId())){
				empMapper.updateUserRelationByUserId(oldJobInfo.getUserId());
				//同时清空session
				AppSessionHolder.delSessionInfo(oldJobInfo.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
			}
		}
	}
}
