package com.yz.service.enroll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.EnrollConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdRegisterMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.finance.StudentMpFlowMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.CommunicationMap;
import com.yz.model.UserReChargeEvent;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.enroll.regist.BdRegistImport;
import com.yz.model.enroll.regist.BdStudentRegist;
import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;
import com.yz.model.finance.BdOrder;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StuRegisterServiceImpl {
	private static final Logger log = LoggerFactory.getLogger(StuRegisterServiceImpl.class);
	@Autowired
	private BdStdRegisterMapper stuMapper;

	@Autowired
	private StudentMpFlowMapper mpFlowMapper;
	
	@Reference(version = "1.0")
	private AtsAccountApi accountApi;
	
	@Autowired
	private BdStdPayFeeMapper payMapper;
	
	@Autowired
	private StuAsynImportStuNoService importStuNoService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object queryRegisterInfoByPage(int start, int length, BdStdEnrollQuery bdStdEnrollQuery, String registerTimer) {
		PageHelper.offsetPage(start, length);
		bdStdEnrollQuery.setRegisterTimer(registerTimer);
		List<BdStudentRegist> infos = stuMapper.selectRegisterInfoByPage(bdStdEnrollQuery);
		return new IPageInfo((Page) infos);
	}

	public void stdRegisterBatchs(String[] learnIds, String grade) {
		/*
		 * for (String learnId : learnIds) { stdRegisterBatch(learnId, grade); }
		 */
		BaseUser user = SessionUtil.getUser();
		List<String> ableLearnIds = new ArrayList<String>();
		for (int i = 0; i < learnIds.length; i++) {
			String[] unpaidItem = stuMapper.selectUnPaidItemCodes(learnIds[i], grade);
			if (unpaidItem.length <= 0) {
				ableLearnIds.add(learnIds[i]);
			}
		}

		stuMapper.stdRegisterBatchs(ableLearnIds, grade, user.getRealName(), user.getUserId());
		if (!EnrollConstants.REGISTER_GRADE_THIRD.equals(grade)) {
			List<Map<String, String>> list = stuMapper.selectNextRegistInfoBatch(ableLearnIds, grade, user.getRealName(), user.getUserId());
			for (Map<String, String> map : list) {
				map.put("register_id", IDGenerator.generatorId());
			}
			stuMapper.insertNextRegistInfos(list);
		}

		List<Body> condtions = stuMapper.getMpConditions(learnIds);

		List<CommunicationMap> cmList = new ArrayList<CommunicationMap>();

		if (condtions != null && condtions.size() > 0) {
			for (Body condition : condtions) {
				if (condition != null && condition.size() > 0) {
					// 判断是否历届学员
					List<Map<String, String>> learnList = mpFlowMapper.getHistoryLearn(condition.getString("stdId"), condition.getString("learnId"));
					// 订单信息
					BdOrder order = payMapper.selectOrder(condition.getString("learnId"));
					UserReChargeEvent event = new UserReChargeEvent();
					event.setPayable("0");
					event.setMappingId(order.getOrderNo());
					event.setCreateTime(DateUtil.convertDateStrToDate(condition.getString("createTime", ""),
							DateUtil.YYYYMMDDHHMMSS_SPLIT));
					event.setPayDateTime(new Date());
					// map.put("learnList", learnList);
					event.setlSize(String.valueOf(learnList != null ? learnList.size() : 0));
					event.setItemCode(new ArrayList<>());
					event.setItemYear(new ArrayList<>());
					if (condition.containsKey("scholarship"))
						event.setScholarship(condition.getString("scholarship"));
					if (condition.containsKey("recruitType"))
						event.setRecruitType(condition.getString("recruitType"));
					// 上线缴费赠送流程 + 个人缴费赠送流程
					event.setUserId(condition.getString("userId", ""));
					event.setpId(condition.getString("pId", ""));
					event.setGrade(condition.getString("grade",""));
					event.setStdStage(StudentConstants.STD_STAGE_STUDYING);
					log.info("发送个人缴费上级赠送指令 lpush {} {}", YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(event));
					RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
				}
			}
		}

		if (cmList.size() > 0) {
			accountApi.awardMore(cmList);
		}
	}

	public void stdRegisterBatch(String learnId, String grade) {

		/*
		 * if (EnrollConstants.REGISTER_GRADE_FIRST.equals(grade)) { String
		 * schoolRoll = stuMapper.selectSchoolRoll(learnId); if
		 * (!StringUtil.hasValue(schoolRoll)) { throw new
		 * BusinessException("E000058"); // 请先导入学号 } }
		 */
		String[] unpaidItem = stuMapper.selectUnPaidItemCodes(learnId, grade);
		if (unpaidItem.length > 0) {
			throw new BusinessException("E000059"); // 当前学费未缴清
		}
		BaseUser user = SessionUtil.getUser();
		stuMapper.stdRegisterBatch(learnId, grade, user.getRealName(), user.getUserId());

		String recruitType = stuMapper.selectRecruitTypeByLearnId(learnId);

		if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)
				&& !EnrollConstants.REGISTER_GRADE_SECOND.equals(grade)) {	// 国开第二年则截止不生成
			List<Map<String, String>> list = stuMapper.selectNextRegistInsertInfo(learnId, grade, user.getRealName(), user.getUserId());
			for (Map<String, String> map : list) {
				map.put("register_id", IDGenerator.generatorId());
			}
			stuMapper.insertNextRegistInfo(list);
		} else if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)
				&& !EnrollConstants.REGISTER_GRADE_THIRD.equals(grade)) {	// 成教第三年截止
			List<Map<String, String>> list = stuMapper.selectNextRegistInsertInfo(learnId, grade, user.getRealName(), user.getUserId());
			for (Map<String, String> map : list) {
				map.put("register_id", IDGenerator.generatorId());
			}
			stuMapper.insertNextRegistInfo(list);
		}

		Body condition = stuMapper.getMpCondition(learnId);

		if (condition != null && !condition.isEmpty()) {
//			if (null != MpContext.getStageFlow()) {
//				MpResult result = MpContext.getStageFlow().match(condition);
//				if (result.hasValue()) {
//					CommunicationMap body = result.getTarget();
//					body.put("createTime", new Date());
//					accountApi.award(body);
//				}
//			}
		}
	}

	public Object queryRegisterInfoById(String learnId, String grade) {
		return stuMapper.selectRegisterInfoById(learnId, grade);
	}

	@SuppressWarnings("unchecked")
	public void importRegister(MultipartFile excelRegist,String stdNo) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdRegistImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdRegistImport>();
		if(StringUtil.hasValue(stdNo)) {
			testExcelCofing.setSheetName("index").setType(BdRegistImport.class)
					.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("录取院校", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("录取专业", "pfsnName"))
					.addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"));
		}else{
			testExcelCofing.setSheetName("index").setType(BdRegistImport.class)
					.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("录取院校", "unvsName"))
					.addTitle(new ExcelUtil.IExcelTitle("录取专业", "pfsnName"))
					.addTitle(new ExcelUtil.IExcelTitle("高校学号", "schoolRoll"));
		}
		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdRegistImport> list = ExcelUtil.importWorkbook(excelRegist.getInputStream(), testExcelCofing,
					excelRegist.getOriginalFilename());
			// 根据高校学号重新排序
//			Collections.sort(list, new Comparator<BdRegistImport>() {
//				public int compare(BdRegistImport arg0, BdRegistImport arg1) {
//					// 定义如何比较
//					return Integer.valueOf(arg0.getSchoolRoll()) - Integer.valueOf(arg1.getSchoolRoll());
//				}
//			});

			// 遍历插入
			// 遍历插入
			if(null != list && list.size()>0){
				if(list.size() >10000){
					throw new BusinessException("E000107"); // 目标年级已存在报读信息
				}
				//多线程处理
				importStuNoService.importStuNoAsyn(list);
			}
			
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

}
