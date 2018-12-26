package com.yz.service.educational;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;

import com.yz.api.UsInfoApi;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.OaTaskInfoMapper;
import com.yz.dao.gk.StudentCityAffirmGKMapper;
import com.yz.dao.gk.StudentGraduateExamGKMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.dao.stdService.StudentCollectMapper;
import com.yz.dao.stdService.StudentDegreeEnglishMapper;
import com.yz.dao.stdService.StudentExamAffirmMapper;
import com.yz.dao.stdService.StudentGraduateDataMapper;
import com.yz.dao.stdService.StudentGraduatePaperMapper;
import com.yz.dao.stdService.StudentLectureNoticeMapper;
import com.yz.dao.stdService.StudentQingshuMapper;
import com.yz.dao.stdService.StudentXuexinMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.MsgPubVo;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaStudentTask;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStatisticsInfo;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentGraduateExamGKInfo;
import com.yz.model.gk.StudentGraduateExamGKInfoSub;
import com.yz.model.stdService.StudentCollectInfo;
import com.yz.model.stdService.StudentDegreeEnglishInfo;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduatePaperInfo;
import com.yz.model.stdService.StudentLectureNoticeInfo;
import com.yz.model.stdService.StudentQingshuInfo;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 教务任务
 * @author lx
 * @date 2017年7月19日 下午3:26:31
 */
@Service
@Transactional
public class OaTaskInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(OaTaskInfoService.class);
	
	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;
	
	@Reference(version="1.0")
	private UsInfoApi usInfoApi;
	
	@Autowired
	private StudentExamAffirmMapper examAffirmMapper;
	
	@Autowired
	private OaTaskImportStuService importStuService;
	
	@Autowired
	private OaTaskAsynAddStuService asynAddStuService;
	
	@Autowired
	private StudentGraduateDataMapper graduateDataMapper;
	
	@Autowired
	private StudentXuexinMapper stuXueXinMapper;
	
	@Autowired
	private StudentDegreeEnglishMapper stuDegreeEnglishMapper;
	
	@Autowired
	private StudentGraduatePaperMapper stuGraduatePaperMapper;
	
	@Autowired
	private StudentLectureNoticeMapper stuLectureNoticeMapper;
	
	@Autowired
	private StudentCollectMapper stuCollectMapper;
	
	@Autowired
	private StudentQingshuMapper stuQingShuMapper;

    @Autowired
    private UsInfoMapper usInfoMapper;
    
    @Autowired
    private StudentCityAffirmGKMapper cityAffirmGKMapper;
    
    @Autowired
    private StudentGraduateExamGKMapper gkUnifiedExamMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo selectOaTaskInfoByPage(int start, int length,OaTaskInfoQuery infoQuery) {
		PageHelper.offsetPage(start, length);
		List<OaTaskInfo> recruiterInfos = oaTaskInfoMapper.selectOaTaskInfo(infoQuery);
		return new IPageInfo((Page) recruiterInfos);
	}
	
	public OaTaskInfo getTaskInfoById(String taskId){
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(taskId);
		if(null != taskInfo){
			if(taskInfo.getTaskType().equals("4")){
				//查看当前任务是否有学员确认
				int affirmCount = oaTaskInfoMapper.affirmCount(taskId,taskInfo.getEyId());
				if(affirmCount >0){
					taskInfo.setIfCanOper("N");
				}
			}
		}
		return taskInfo;
	}
	
	public void insertTask(OaTaskInfo taskInfo){
		taskInfo.setTaskId(IDGenerator.generatorId());
		oaTaskInfoMapper.insertTask(taskInfo);
	}
	
	public void updateTask(OaTaskInfo taskInfo){
		//启用时,给未通知的学员发送微信推送消息
		//TODO 应学服部 廖老师要求暂时屏蔽此功能 2017-12-19
//		if(taskInfo.getIsAllow().equals("1")){
//			sendMsgService.sendTaskMessage(taskInfo.getTaskId());
//		}
		oaTaskInfoMapper.updateTask(taskInfo);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryOaTaskStudentInfo(int start, int length,OaTaskTargetStudentQuery studentQuery) {
		PageHelper.offsetPage(start, length);
		String[] stdStageArray = studentQuery.getStdStage().split(",");
		studentQuery.setStdStageArray(stdStageArray);
		List<OaTaskStudentInfo> recruiterInfos = oaTaskInfoMapper.queryOaTaskStudentInfo(studentQuery);
		return new IPageInfo((Page) recruiterInfos);
	}
	
	public void addStu(String[] idArray,String taskId,String operType){
		
		List<StudentExamAffirmInfo> stuInfo = new ArrayList<>();              //学员服务-考场确认跟进
		List<StudentGraduateDataInfo> stuDataInfo = new ArrayList<>();        //学员服务---毕业资料提交
		List<StudentXuexinInfo> xueXinList = new ArrayList<>();               //学信网
		List<StudentDegreeEnglishInfo> englishInfoList = new ArrayList<>();   //学位英语报名
		List<StudentGraduatePaperInfo> graPaperInfoList = new ArrayList<>();  //毕业论文及报告
		List<StudentLectureNoticeInfo> noticeInfoList = new ArrayList<>();    //开课通知
		List<StudentCollectInfo> collList = new ArrayList<>();                //新生学籍资料收集
		List<StudentQingshuInfo> qingshuList = new ArrayList<>();             //青书学堂通知
		List<StudentCityAffirmGKInfo> gkCityAffirm = new ArrayList<>();       //国开城市确认
		List<StudentGraduateExamGKInfo> gkUnifiedExamList = new ArrayList<>();        //国开统考
		List<StudentGraduateExamGKInfo> updateGkUnifiedExamList = new ArrayList<>();  //变更国开统考
		List<StudentGraduateExamGKInfoSub> gkUnifiedExmaSub = new ArrayList<>();      //国开统考对应的科目
		
		List<OaTaskStudentInfo> list = new ArrayList<>();
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(taskId);
		
		if (null != taskInfo) {  //当任务存在时
			if (idArray.length > 0) {
				for (String str : idArray) {
					String[] splitStr = str.split(";");
					OaTaskStudentInfo studentInfo = new OaTaskStudentInfo();
					// 由于一个学业只能收到同一个任务一次
					int count = oaTaskInfoMapper.getCountByLearnIdAndTaskId(splitStr[0], taskId);
					if (count == 0) { // 可添加
						studentInfo.setLearnId(splitStr[0]);
						studentInfo.setTutor(splitStr[1]);
						
						String userId = oaTaskInfoMapper.getUserIdByLearnId(studentInfo.getLearnId());
						if (StringUtil.hasValue(userId)) {
							//调用接口获取openId;
							Object openId = usInfoMapper.selectUserOpenId(userId);
							if(null != openId){
								studentInfo.setOpenId(openId.toString());
							}
						}
						list.add(studentInfo);
						//针对考场确认的任务执行
						if(taskInfo.getTaskType().equals("4")){
							StudentExamAffirmInfo affirmInfo  =new StudentExamAffirmInfo();
							affirmInfo.setLearnId(studentInfo.getLearnId());
							affirmInfo.setTaskId(taskId);
							affirmInfo.setEyId(taskInfo.getEyId());
							affirmInfo.setAffirmId(IDGenerator.generatorId());
							stuInfo.add(affirmInfo);
						}else if(taskInfo.getTaskType().equals("6")){ //针对毕业资料提交
							StudentGraduateDataInfo dataInfo = new StudentGraduateDataInfo();
							dataInfo.setLearnId(studentInfo.getLearnId());
							dataInfo.setTaskId(taskId);
							dataInfo.setStdId(studentInfo.getStdId());
							dataInfo.setId(IDGenerator.generatorId());
							stuDataInfo.add(dataInfo);
						}else if(taskInfo.getTaskType().equals("7")){ //学信网信息核对
							StudentXuexinInfo xueXinInfo = new StudentXuexinInfo();
							xueXinInfo.setLearnId(studentInfo.getLearnId());
							xueXinInfo.setTaskId(taskId);
							xueXinInfo.setXuexinId(IDGenerator.generatorId());
							xueXinList.add(xueXinInfo);
						}else if(taskInfo.getTaskType().equals("8")){ //学位英语报名
							StudentDegreeEnglishInfo englishInfo = new StudentDegreeEnglishInfo();
							englishInfo.setLearnId(studentInfo.getLearnId());
							englishInfo.setTaskId(taskId);
							englishInfo.setDegreeId(IDGenerator.generatorId());
							englishInfoList.add(englishInfo);
						}else if(taskInfo.getTaskType().equals("9")){ //毕业论文及报告通知
							int cunt =stuGraduatePaperMapper.checkIfExistByLearnId(studentInfo.getLearnId());
							if(cunt ==0){
								StudentGraduatePaperInfo paperInfo = new StudentGraduatePaperInfo();
								paperInfo.setLearnId(studentInfo.getLearnId());
								paperInfo.setTaskId(taskId);
								paperInfo.setGpId(IDGenerator.generatorId());
								graPaperInfoList.add(paperInfo);
							}
						}else if(taskInfo.getTaskType().equals("10")){ //开课通知
							StudentLectureNoticeInfo noticeInfo = new StudentLectureNoticeInfo();
							noticeInfo.setLearnId(studentInfo.getLearnId());
							noticeInfo.setTaskId(taskId);
							noticeInfo.setLectureId(IDGenerator.generatorId());
							noticeInfoList.add(noticeInfo);
						}else if(taskInfo.getTaskType().equals("11")){ //新生学籍资料收集
							int cunt = stuCollectMapper.checkIfExistByLearnId(studentInfo.getLearnId());
							if(cunt == 0){
								StudentCollectInfo collInfo = new StudentCollectInfo();
								collInfo.setLearnId(studentInfo.getLearnId());
								collInfo.setTaskId(taskId);
								collInfo.setCtId(IDGenerator.generatorId());

								collList.add(collInfo);
							}
						}else if(taskInfo.getTaskType().equals("12")){ //青书学堂上课通知
							int cunt = stuQingShuMapper.checkIfExistByLearnId(studentInfo.getLearnId());
							if(cunt ==0){
								StudentQingshuInfo qingShuInfo = new StudentQingshuInfo();
								qingShuInfo.setLearnId(studentInfo.getLearnId());
								qingShuInfo.setTaskId(taskId);
								qingShuInfo.setQingshuId(IDGenerator.generatorId());
								qingshuList.add(qingShuInfo);
							}
						}else if(taskInfo.getTaskType().equals("13")){ //国开城市确认
							int cunt = cityAffirmGKMapper.checkIfExistByLearnId(taskInfo.getEyId(),studentInfo.getLearnId());
							if(cunt == 0){
								StudentCityAffirmGKInfo cityAffirmGkInfo = new StudentCityAffirmGKInfo();
								cityAffirmGkInfo.setLearnId(studentInfo.getLearnId());
								cityAffirmGkInfo.setTaskId(taskId);
								cityAffirmGkInfo.setEyId(taskInfo.getEyId());
								cityAffirmGkInfo.setAffirmId(IDGenerator.generatorId());
								gkCityAffirm.add(cityAffirmGkInfo);
							}
						}else if(taskInfo.getTaskType().equals("14")){ //国开统考
							StudentGraduateExamGKInfo gkUnifiedInfo = gkUnifiedExamMapper.getStudentGraduateExamGKById(studentInfo.getLearnId());
							if(gkUnifiedInfo == null){
								StudentGraduateExamGKInfo gkUnifiedExam = new StudentGraduateExamGKInfo();
								gkUnifiedExam.setLearnId(studentInfo.getLearnId());
								gkUnifiedExam.setTaskId(taskId);
								gkUnifiedExam.setFollowId(IDGenerator.generatorId());
								gkUnifiedExamList.add(gkUnifiedExam);
								//默认初始化两个科目
								for(int i=1;i<=2;i++){
									StudentGraduateExamGKInfoSub subInfo = new StudentGraduateExamGKInfoSub();
									subInfo.setId(IDGenerator.generatorId());
									subInfo.setFollowId(gkUnifiedExam.getFollowId());
									subInfo.setEnrollSubject(i+"");
									gkUnifiedExmaSub.add(subInfo);
								}
							}else{
								gkUnifiedInfo.setTaskId(taskId);
								updateGkUnifiedExamList.add(gkUnifiedInfo);
							}
						}
					}
				}
				//主任务
				if(list.size() >0){
					oaTaskInfoMapper.addStu(list, taskId);
				}
				//初始化每个具体的任务信息
				if(stuInfo.size() >0){//考场确认
					examAffirmMapper.addStuExamAffirm(stuInfo);
				}else if(stuDataInfo.size() >0){//毕业资料
					graduateDataMapper.addStuGraduateData(stuDataInfo);
				}else if(xueXinList.size() >0){ //信息网信息核对
					stuXueXinMapper.addStuXueXinInfo(xueXinList);
				}else if(englishInfoList.size() >0){ //学位英语
					stuDegreeEnglishMapper.addStuDegreeEnglishInfo(englishInfoList);
				}else if(graPaperInfoList.size() >0){ //毕业论文及报告
					stuGraduatePaperMapper.addStuGraduatePaperInfo(graPaperInfoList);
				}else if(noticeInfoList.size() >0){  //开课通知
					stuLectureNoticeMapper.addStuLectureNotcieInfo(noticeInfoList);
				}else if(collList.size() >0){  //新生学籍资料收集
					stuCollectMapper.addStuCollectInfo(collList);
				}else if(qingshuList.size() >0){//青书学堂
					stuQingShuMapper.addStuQingShuInfo(qingshuList);
				}else if(gkCityAffirm.size() >0){ //国开城市确认
					cityAffirmGKMapper.addStuCityAffirmGK(gkCityAffirm);
				}else if(taskInfo.getTaskType().equals("14")){ //国开统考
					if(gkUnifiedExamList.size() >0){
						gkUnifiedExamMapper.addStuGkUnifiedExamInfo(gkUnifiedExamList);
					}
					if(gkUnifiedExmaSub.size()>0){
						gkUnifiedExamMapper.addStuGkUnifiedExamSubInfo(gkUnifiedExmaSub); //国开统考科目
					}
					if(updateGkUnifiedExamList.size() >0){ //需要变更的国开统考设置
						gkUnifiedExamMapper.updateGkUnifiedExamList(updateGkUnifiedExamList);
					}
				} 
			}

		}
			
			//TODO 应学服部 廖老师要求暂时屏蔽此功能 2017-12-19
//			if (operType.equals("1")) {
//				// 直接推送公众号信息
//				List<OaStudentTask> stuTaskList = oaTaskInfoMapper.getOaStudentTask(taskId);
//				if (null != stuTaskList && stuTaskList.size() > 0) {
//					for (OaStudentTask task : stuTaskList) {
//						if (StringUtil.hasValue(task.getOpenId())) {
//							// 发送消息
//							wechatApi.sendTaskMsg(task.getTaskTitle(), task.getEndTime(), task.getOpenId(),
//									task.getLearnId());
//							// 改变状态
//							oaTaskInfoMapper.updateStudentTaskIsNotify(task.getTaskId(), task.getLearnId());
//						}
//					}
//				}
//			}
	}

	public void delStu(String[] idArray,String taskId){
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(taskId);
		if(null != taskInfo){
			oaTaskInfoMapper.delStu(idArray,taskId);
			if(taskInfo.getTaskType().equals("4")){
				//删除考场确认的信息
				examAffirmMapper.delStuExamAffirm(idArray,taskId);
			}else if(taskInfo.getTaskType().equals("6")){
				//删除毕业资料提交
				graduateDataMapper.delStuGraduateData(idArray,taskId);
			}else if(taskInfo.getTaskType().equals("7")){
				//信息网信息核对
				stuXueXinMapper.delStuXueXinInfo(idArray,taskId);
			}else if(taskInfo.getTaskType().equals("8")){
				//学位英语
				stuDegreeEnglishMapper.delStuDegreeEnglishInfo(idArray,taskId);
			}else if(taskInfo.getTaskType().equals("9")){
				//毕业论文
				stuGraduatePaperMapper.delStuGraduatePaperInfo(idArray,taskId);
			}else if(taskInfo.getTaskType().equals("10")){
				//开课通知
				stuLectureNoticeMapper.delStuLectureNotcieInfo(idArray, taskId);
			}else if(taskInfo.getTaskType().equals("11")){
				//新生学籍资料收集
				stuCollectMapper.delStuCollectInfo(idArray, taskId);
			}else if(taskInfo.getTaskType().equals("12")){
				//青书学堂
				stuQingShuMapper.delStuQingShuInfo(idArray, taskId);
			}else if(taskInfo.getTaskType().equals("13")){
				//国开城市确认
				cityAffirmGKMapper.delStuCityAffirm(idArray, taskId);
			}else if(taskInfo.getTaskType().equals("14")){
				//国开统考设置
				gkUnifiedExamMapper.delStuGkUnifiedExamSubInfo(idArray, taskId);
			}
		}
	}
	
	public OaTaskStudentInfo getOaTaskStudentInfoByIdCard(String idCard,String grade){
		return oaTaskInfoMapper.getOaTaskStudentInfoByIdCard(idCard,grade);
	}
	
	public void batchImport(List<String> strs,String taskId){
		oaTaskInfoMapper.batchImport(strs,taskId);
	}
	public List<OaTaskStatisticsInfo> getOaTaskStatisticsInfo(String taskId){
		return oaTaskInfoMapper.getOaTaskStatisticsInfo(taskId);
	}
	/**
	 * 提醒某个任务未完成的学员
	 * @param taskId
	 */
	public void remindUnfinishedStudent(String taskId){
		//再次提醒,给未通知的学员发送微信推送消息
		log.debug("用户:"+SessionUtil.getUser().getUsername()+"正在操作提醒未完成学员-------");
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(taskId);
		if(null != taskInfo){
			MsgPubVo msgVo = new MsgPubVo();
			msgVo.setMsgId(taskId);
			msgVo.setMsgCode("YZ");
			msgVo.setMsgContent(taskInfo.getTaskContent());
			msgVo.setMsgTitle(taskInfo.getTaskTitle());
			msgVo.setMsgType(WechatMsgConstants.PUB_MSG_TYPE_TASK);
			msgVo.setUrl("https://zm.yzou.cn/student/mytask?learnId=");
			//添加到redis中 task 执行发送
			RedisService.getRedisService().zadd(YzTaskConstants.YZ_MSG_PUB_TASK_SET, 0, JsonUtil.object2String(msgVo));
		}else{
			log.info("task is not exists.......");
		}
		//unfinishedSendMsgService.sendTaskUnfinishedMessage(taskId);
	}
	
	@SuppressWarnings("unchecked")
	public void exportUnfinishedStudent(String taskId, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<OaStudentTask> testExcelCofing = new ExcelUtil.IExcelConfig<OaStudentTask>();
		testExcelCofing.setSheetName("taskTitle").setType(OaStudentTask.class)
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "name"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号码", "cardId"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "universitiy"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "grad"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "specialty"))
				.addTitle(new ExcelUtil.IExcelTitle("跟进人", "follow"));

		List<OaStudentTask> list = oaTaskInfoMapper.getTaskUnfinishedStudentReport(taskId);

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=taskUnfinishedStu.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	/**
	 * 导入目标学员到任务重
	 */
	public void importTargetStuToTask(IExcelConfig<OaTaskStudentInfo> testExcelCofing,MultipartFile excelStu,String taskId)
	{
		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<OaTaskStudentInfo> list = ExcelUtil.importWorkbook(excelStu.getInputStream(), testExcelCofing,excelStu.getOriginalFilename());
			OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(taskId);
			// 遍历插入
			if(null != list && list.size()>0){
				if(list.size() >10000){
					throw new BusinessException("E000107"); // 目标年级已存在报读信息
				}
				importStuService.importStuInfoAsyn(list, taskInfo);
			}
//			for (OaTaskStudentInfo studentInfo : list) {
//				// 根据身份证号查询出对应的在读学员learnId
//				OaTaskStudentInfo stuInfo = oaTaskInfoMapper.getOaTaskStudentInfoByIdCard(studentInfo.getExt1(),studentInfo.getGrade());
//				if (null == stuInfo) {
//					//throw new IllegalArgumentException("请检查第" + index + "行数据！学员身份证:"+studentInfo.getExt1()+"无报读信息");
//				} else {
//					oaTaskInfoMapper.singleImport(stuInfo.getLearnId(), taskId);
//					//针对考场确认的任务执行
//					if(taskInfo.getTaskType().equals("4")){
//						StudentExamAffirmInfo affirmInfo  =new StudentExamAffirmInfo();
//						affirmInfo.setLearnId(studentInfo.getLearnId());
//						affirmInfo.setTaskId(taskId);
//						affirmInfo.setEyId(taskInfo.getEyId());
//						
//						examAffirmMapper.singleAddStuExamAffirm(affirmInfo);
//					}
//				}
//				
//				index++;
//			}

		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}
	
	public int getCountByLearnIdAndTaskId(String learnId,String taskId){
		return oaTaskInfoMapper.getCountByLearnIdAndTaskId(learnId,taskId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo findAllTaskInfo(int start, int length,String sName) {
		PageHelper.startPage(start, length);
		List<Map<String, String>> resultMap = oaTaskInfoMapper.findAllTaskInfo(sName);
		return new IPageInfo((Page) resultMap);
	}
	/**
	 * 根据条件全部添加
	 * @param studentQuery
	 */
	public void addAllStu(OaTaskTargetStudentQuery studentQuery){
		//TODO 根据条件添加,如果数据过大,后期可以采用异步执行
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(studentQuery.getTaskId());
		if (null != taskInfo) {
			String[] stdStageArray = studentQuery.getStdStage().split(",");
			studentQuery.setStdStageArray(stdStageArray);
			List<OaTaskStudentInfo> list = oaTaskInfoMapper.queryOaTaskStudentInfo(studentQuery);
			
			asynAddStuService.addStuInfoAsyn(list, taskInfo);
		}
		
	}
	/**
	 * 根据条件全部删除
	 * @param studentQuery
	 */
	public void delAllStu(OaTaskTargetStudentQuery studentQuery){
		//TODO 根据条件删除,如果数据过大,后期可以采用异步执行
		OaTaskInfo taskInfo = oaTaskInfoMapper.getTaskInfoById(studentQuery.getTaskId());
		if (null != taskInfo) {
			String[] stdStageArray = studentQuery.getStdStage().split(",");
			studentQuery.setStdStageArray(stdStageArray);
			List<OaTaskStudentInfo> list = oaTaskInfoMapper.queryOaTaskStudentInfo(studentQuery);
			asynAddStuService.delStuInfoAsyn(list, taskInfo);
		}
	}
	
	public Object getGKExamYear(){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = oaTaskInfoMapper.getGKExamYear();
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("exam_year"));
				map.put("dictName", resultMap.get("exam_year"));
				resultList.add(map);
			}
		}
		return resultList;
	}

	/**
	 * 删除教务任务
	 *
	 * @param taskIds
	 */
	public void deleteTask(String[] taskIds) {
		oaTaskInfoMapper.deleteTask(taskIds);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo findGraduateDataTaskInfo(int start, int length,String sName) {
		PageHelper.startPage(start, length);
		List<Map<String, String>> resultMap = oaTaskInfoMapper.findGraduateDataTaskInfo(sName);
		return new IPageInfo((Page) resultMap);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo findTaskInfoByType(int start, int length,String sName,String taskType) {
		PageHelper.startPage(start, length);
		List<Map<String, String>> resultMap = oaTaskInfoMapper.findTaskInfoByType(sName,taskType);
		return new IPageInfo((Page) resultMap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo findAddressAffirmTaskInfo(int start, int length,String sName) {
		PageHelper.startPage(start, length);
		List<Map<String, String>> resultMap = oaTaskInfoMapper.findTaskInfoByType(sName,"3");
		return new IPageInfo((Page) resultMap);
	}
	
	public Object getExamYearForGK(String status){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = oaTaskInfoMapper.getExamYearForGK(status);
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("ey_id"));
				map.put("dictName", resultMap.get("exam_year"));
				resultList.add(map);
			}
		}
		return resultList;
	}
	/**
	 * 国开统考设置
	 * @param status
	 * @return
	 */
	public Object getGkUnifiedExam(String status){
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = oaTaskInfoMapper.getGkUnifiedExam(status);
		Map<String, String> map = null;
		if (null != list && !list.isEmpty()) {
			for (Map<String, String> resultMap : list) {
				map = new HashMap<String, String>();
				map.put("dictValue", resultMap.get("id"));
				map.put("dictName", resultMap.get("title"));
				resultList.add(map);
			}
		}
		return resultList;
	}

}
