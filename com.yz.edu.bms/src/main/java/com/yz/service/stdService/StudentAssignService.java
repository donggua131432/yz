package com.yz.service.stdService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.finance.BdUnawardMapper;
import com.yz.dao.finance.StudentMpFlowMapper;
import com.yz.dao.stdService.StudentAssignMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.CommunicationMap;
import com.yz.model.UserReChargeEvent;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.condition.stdService.StudentAssignQuery;
import com.yz.model.finance.BdOrder;
import com.yz.model.stdService.StudentAssignInfo;
import com.yz.model.stdService.StudentAssignListInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

@Service
@Transactional
public class StudentAssignService {

	private static final Logger log = LoggerFactory.getLogger(StudentAssignService.class);

	@Autowired
	private StudentAssignMapper assignMapper;

	@Autowired
	private StudentMpFlowMapper mpFlowMapper;

	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	public IPageInfo<StudentAssignListInfo> findAssignList(StudentAssignQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<StudentAssignListInfo> list = assignMapper.findAssignList(queryInfo);
		return new IPageInfo<StudentAssignListInfo>((Page<StudentAssignListInfo>) list);
	}

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private BdUnawardMapper unwardMapper;

	@Autowired
	private BdStdPayFeeMapper payMapper;

	public void addAssign(StudentAssignInfo assignInfo) {

		String[] learnIds = assignInfo.getLearnIds();

		List<CommunicationMap> list = new ArrayList<CommunicationMap>();

		if (learnIds != null && learnIds.length > 0) {

			assignMapper.addAssignInfos(assignInfo);
			for (String learnId : learnIds) {
				StudentAssignInfo stuinfo = assignMapper.getStudentInfo(learnId);
				String stdId = stuinfo.getStdId();
				assignInfo.setStdId(stdId);
				// 只有注册学员可以改状态为：学员阶段-在读学员
				if (stuinfo.getStdStage() != null
						&& stuinfo.getStdStage().equals(StudentConstants.STD_STAGE_REGISTER)) {
					assignInfo.setStdStage(StudentConstants.STD_STAGE_STUDYING);
					assignMapper.updateStudentsStage(assignInfo);
					// 订单信息
					BdOrder order = payMapper.selectOrder(learnId);
					List<Map<String, String>> relation = mpFlowMapper.getRelation(learnId);

					if (relation != null && relation.size() > 1) {
						log.error("-------------------------- 学业[" + learnId + "]查询到多个用户关系 不做赠送处理");
						continue;
					}
					Body condition = mpFlowMapper.getCondition(learnId);
					// 判断是否历届学员
					List<Map<String, String>> learnList = mpFlowMapper.getHistoryLearn(stdId, learnId);
					UserReChargeEvent event = new UserReChargeEvent();
					event.setPayable("0");
					event.setMappingId(order.getOrderNo());
					event.setCreateTime(DateUtil.convertDateStrToDate(condition.getString("createTime", ""),
							DateUtil.YYYYMMDDHHMMSS_SPLIT));
					event.setPayDateTime(new Date());
					// map.put("learnList", learnList);
					event.setlSize(String.valueOf(learnList != null ? learnList.size() : 0));
					String[] itemCodes = payMapper.selectItemCodeByStdid(stdId);
					event.setItemCode(Arrays.asList(itemCodes));
					event.setItemYear(new ArrayList<>());
					if (condition.containsKey("scholarship"))
						event.setScholarship(condition.getString("scholarship"));
					if (condition.containsKey("recruitType"))
						event.setRecruitType(condition.getString("recruitType"));
					// 上线缴费赠送流程 + 个人缴费赠送流程
					event.setUserId(condition.getString("userId", ""));
					event.setpId(condition.getString("pId", ""));
					event.setGrade(condition.getString("grade"));
					event.setStdStage(StudentConstants.STD_STAGE_STUDYING);
					log.info("发送个人缴费上级赠送指令 lpush {} {}", YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(event));
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
				}
				// 插入学员变更记录
				addStudentModefy(learnId, assignInfo);
			}
		}
	}

	// 插入学员变更记录
	public void addStudentModefy(String learnId, StudentAssignInfo assignInfo) {
		BdStudentModify studentModify = new BdStudentModify();
		// 审核类型为新生信息修改
		studentModify.setCheckType(TransferConstants.CHECK_TYPE_NEW_MODIFY);
		// 变更类型为新生信息修改
		studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_AssignInfo_4);
		studentModify.setCheckOrder("1");
		studentModify.setLearnId(learnId);
		studentModify.setStdId(assignInfo.getStdId());
		if (assignInfo.getStdStage() != null && assignInfo.getStdStage().equals(StudentConstants.STD_STAGE_REGISTER)) {
			studentModify.setExt1("分配了辅导员:" + assignInfo.getEmpName() + "，成为在读学员");
		} else {
			studentModify.setExt1("分配了辅导员:" + assignInfo.getEmpName());
		}
		studentModify.setIsComplete("1");
		BaseUser user = SessionUtil.getUser();
		studentModify.setCreateUser(user.getUsername());
		studentModify.setCreateUserId(user.getUserId());
		studentModify.setModifyId(IDGenerator.generatorId());
		// 插入
		studentModifyMapper.insertSelective(studentModify);
	}

	public void getStudentInfo(StudentAssignInfo assignInfo) {
		String learnId = assignInfo.getLearnId();
		StudentAssignInfo studentInfo = assignMapper.getStudentInfo(learnId);
		assignInfo.setCampusId(studentInfo.getCampusId());
		assignInfo.setCampusName(studentInfo.getCampusName());
		assignInfo.setGrade(studentInfo.getGrade());
		assignInfo.setPfsnCode(studentInfo.getPfsnCode());
		assignInfo.setPfsnLevel(studentInfo.getPfsnLevel());
		assignInfo.setPfsnName(studentInfo.getPfsnName());
		assignInfo.setUnvsName(studentInfo.getUnvsName());
		assignInfo.setStdId(studentInfo.getStdId());
		assignInfo.setStdName(studentInfo.getStdName());
		assignInfo.setStdNo(studentInfo.getStdNo());
		assignInfo.setStdStage(studentInfo.getStdStage());
		assignInfo.setRecruitType(studentInfo.getRecruitType());
		assignInfo.setRecruitCode(studentInfo.getRecruitCode());
		assignInfo.setRecruitName(studentInfo.getRecruitName());
	}

	public List<Map<String, String>> getStudentList(StudentAssignInfo assignInfo) {
		String[] learnIds = null;

		if ("1".equals(assignInfo.getAddType())) {// 单个分配
			learnIds = new String[] { assignInfo.getLearnId() };
		} else if ("2".equals(assignInfo.getAddType())) {// 批量分配
			if (assignInfo.getLearnIds() == null)
				return null;
			learnIds = assignInfo.getLearnIds();
		}

		return assignMapper.getStudentList(learnIds);
	}

	public List<Map<String, String>> getStudentListByquery(StudentAssignQuery queryInfo) {
		List<Map<String, String>> list = assignMapper.getStudentListByquery(queryInfo);
		return list;
	}

}
