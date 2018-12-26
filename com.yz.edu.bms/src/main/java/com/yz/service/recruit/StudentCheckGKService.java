package com.yz.service.recruit;

import com.yz.constants.TransferConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.admin.BmsUserMapper;
import com.yz.dao.oa.OaCampusGroupMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.StudentConstants;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.dao.recruit.StudentCheckGKMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @描述: 国开资料审核
 * @作者: DuKai
 * @创建时间: 2018/5/8 19:03
 * @版本号: V1.0
 */
@Service
@Transactional
public class StudentCheckGKService {
	private static final Logger log = LoggerFactory.getLogger(StudentCheckGKService.class);


	@Autowired
	private StudentCheckGKMapper studentCheckGKMapper;

	@Autowired
	private BdLearnAnnexMapper lAnnexMapper;

	@Autowired
	private StudentRecruitMapper recruitMapper;

	@Autowired
	private StudentAllMapper studentAllMapper;

	@Autowired
	private BmsUserMapper bmsUserMapper;

	@Autowired
	private OaCampusGroupMapper oaCampusGroupMapper;


	/**
	 * 获取全部国开学员信息列表
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<Map<String, Object>> findAllStudentGKList(StudentAnnexCheckQuery queryInfo) {
		//只查询基本信息已完善及资料已上传的学员信息
		//queryInfo.setIsDataCompleted("1");
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		//获取校区组所有的校区信息
		List<String> campusIdList = new ArrayList<>();
		campusIdList.add(user.getCampusId());
		if (jtList.contains("GKZL1") && !jtList.contains("GKZL2")
				&& !jtList.contains("GKZL3") && !jtList.contains("GKZL4")) {
			//国开资料初审
			user.setUserLevel("15");
			List<String> resultList = oaCampusGroupMapper.selectCampusGroupByCampusId(user.getCampusId());
			if(resultList != null && resultList.size()>0){
				campusIdList.clear();
				campusIdList.addAll(resultList);
			}
		}

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<Map<String, Object>> list = studentCheckGKMapper.selectAllStudentInfoGKList(queryInfo,user,campusIdList);

		for (Map<String, Object> map:list){
			String annexStatus = map.get("annexStatus").toString();
			if (StudentConstants.ANNEX_STATUS_REJECT.equals(annexStatus)
					|| StudentConstants.ANNEX_STATUS_UNCHECK.equals(annexStatus)
					|| StudentConstants.ANNEX_STATUS_UNUPLOAD.equals(annexStatus)) {
				int count = lAnnexMapper.countBy(map.get("learnId").toString(), annexStatus);
				map.put("annexCount",count);
			}
		}

		IPageInfo<Map<String,Object>> page = new IPageInfo<Map<String,Object>>((Page<Map<String,Object>>) list);
		return page;
	}

	/**
	 * 批量审核国开资料
	 * @param learnIds
	 * @return
	 */
	public boolean batchChecks(String[] learnIds){
		BaseUser user = SessionUtil.getUser();
		List<String> jtIdList = user.getJtList();
		//获取需要审核学业信息
		List<Map<String,Object>> mapList = studentCheckGKMapper.selectAllLearnInfoGKList(learnIds);

		//准备修改学业信息
		List<BdLearnInfo> bdLearnInfoList = new ArrayList<>();
		//准备修改审核记录信息
		List<BdCheckRecord> checkRecordList = new ArrayList<>();
		//准备添加变更记录信息
		List<BdStudentModify> bdStudentModifyList = new ArrayList<>();
		for (Map<String,Object> map:mapList) {
			//学业
			BdLearnInfo bdLearnInfo = new BdLearnInfo();
			bdLearnInfo.setLearnId(map.get("learnId").toString());
			if(jtIdList.contains("GKZL3")){
				bdLearnInfo.setIsDataCheck("5");
				bdLearnInfo.setStdStage(StudentConstants.STD_STAGE_ENROLLED);
				//变更记录
				bdStudentModifyList.add(initBdStudentModify(map,user,"国开资料三审"));
			}

			if(jtIdList.contains("GKZL4")){
				bdLearnInfo.setIsDataCheck("2");
				bdLearnInfo.setStdStage(StudentConstants.STD_STAGE_REGISTER);
				//变更记录
				bdStudentModifyList.add(initBdStudentModify(map,user,"国开资料终审"));
			}
			bdLearnInfoList.add(bdLearnInfo);

			//审核信息
			List<BdCheckRecord> checkRecords = (List<BdCheckRecord>) map.get("checkRecordList");
			if(!checkRecords.isEmpty()){
				for (BdCheckRecord checkRecord: (List<BdCheckRecord>) map.get("checkRecordList")) {
					if(jtIdList.contains(checkRecord.getJtId())){
						checkRecord.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
						checkRecord.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
						checkRecord.setEmpId(user.getEmpId());
						checkRecord.setUpdateTime(new Date());
						checkRecord.setUpdateUser(user.getRealName());
						checkRecord.setUpdateUserId(user.getUserId());
						checkRecordList.add(checkRecord);
					}
				}
			}
		}

		if(!checkRecordList.isEmpty()){
			log.info("=========信息准备完毕，开始批量修改数据==========");
			studentCheckGKMapper.updateIsDataCheckList(bdLearnInfoList);
			studentCheckGKMapper.updateBdCheckRecordList(checkRecordList);
			studentCheckGKMapper.insertBdStudentModifyList(bdStudentModifyList);
		}else{
			log.error("=========审核记录数据不完整，请初始化审核数据==========");
			return false;
		}
		return true;
	}


	/**
	 * 添加变更记录
	 * @param map
	 * @param user
	 * @return
	 */
	private BdStudentModify initBdStudentModify(Map<String,Object> map, BaseUser user, String ext1){
		BdStudentModify bdStudentModify = new BdStudentModify();
		bdStudentModify.setModifyId(IDGenerator.generatorId());
		bdStudentModify.setLearnId(map.get("learnId").toString());
		bdStudentModify.setStdId(map.get("stdId").toString());
		bdStudentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
		bdStudentModify.setIsComplete("1");
		bdStudentModify.setCreateUser(user.getUsername());
		bdStudentModify.setCreateUserId(user.getUserId());
		bdStudentModify.setExt1(ext1);
		return bdStudentModify;
	}

	/**
	 * 初始化国开审核记录信息
	 * @param learnId
	 * @return
	 */
	public List<StudentCheckRecord> initGkCheckRecord(String learnId){
		// 插入国开资料审核记录
		List<StudentCheckRecord> recordList = new ArrayList<>();

		//获取已有的审核记录信息
		List<StudentCheckRecord> bdCheckRecords =  studentAllMapper.getCheckDataStatus(learnId);
		if(bdCheckRecords!=null && bdCheckRecords.size()>0){
			StudentCheckRecord bdCheckRecord = bdCheckRecords.get(0);
			bdCheckRecord.setJtId("GKZL4");
			bdCheckRecord.setCheckOrder("4");
			bdCheckRecord.setCheckType(TransferConstants.CHECK_TYPE_DATA_GK);

			String empId = null;
			if(TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(bdCheckRecord.getCrStatus())){
				BaseUser baseUser = bmsUserMapper.selectBaseUserByUserId(bdCheckRecord.getUpdateUserId());
				if(baseUser != null){
					empId = baseUser.getEmpId();
					bdCheckRecord.setEmpId(empId);
				}
			}
			String[] jtIdArray = {"GKZL1", "GKZL2", "GKZL3"};
			for (int i=0; i<jtIdArray.length; i++){
				StudentCheckRecord record = new StudentCheckRecord();
				record.setCrId(IDGenerator.generatorId());
				record.setMappingId(learnId);
				record.setJtId(jtIdArray[i]);
				// 审核阶段
				if("GKZL1".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
				}else if ("GKZL2".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
				}else if("GKZL3".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
				}
				record.setCheckType(TransferConstants.CHECK_TYPE_DATA_GK);
				if(TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(bdCheckRecord.getCrStatus())){
					record.setCheckStatus(bdCheckRecord.getCheckStatus());
					record.setCrStatus(bdCheckRecord.getCrStatus());
					record.setEmpId(empId);
					record.setUpdateTime(bdCheckRecord.getUpdateTime());
					record.setUpdateUserId(bdCheckRecord.getUpdateUserId());
					record.setUpdateUser(bdCheckRecord.getUpdateUser());
				}else{
					record.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
					record.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				}
				recordList.add(record);
			}

			//更新第一条记录信息
			studentCheckGKMapper.updateBdCheckRecordById(bdCheckRecord);
			studentCheckGKMapper.insertCheckRecordBatch(recordList);
			recordList.add(bdCheckRecord);
		}else{
			String[] jtIdArray = {"GKZL1", "GKZL2", "GKZL3", "GKZL4"};
			for (int i=0; i<jtIdArray.length; i++){
				StudentCheckRecord record = new StudentCheckRecord();
				record.setCrId(IDGenerator.generatorId());
				record.setMappingId(learnId);
				record.setJtId(jtIdArray[i]);
				record.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				// 审核阶段
				if("GKZL1".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FIRST);
				}else if ("GKZL2".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_SECOND);
				}else if("GKZL3".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_THIRD);
				}else if("GKZL4".equals(jtIdArray[i])){
					record.setCheckOrder(TransferConstants.CHECK_RECORD_ORDER_FOUR);
				}
				record.setCheckType(TransferConstants.CHECK_TYPE_DATA_GK);
				record.setCrStatus(TransferConstants.CHECK_RECORD_STATUS_CHECKING);
				recordList.add(record);
			}
			recruitMapper.insertCheckRecordBatch(recordList);
		}
		return recordList;
	}
}
