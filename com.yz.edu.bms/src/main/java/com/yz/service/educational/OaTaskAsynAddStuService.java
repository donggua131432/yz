package com.yz.service.educational;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.UsInfoApi;
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
import com.yz.generator.IDGenerator;
import com.yz.model.educational.OaTaskInfo;
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
import com.yz.util.StringUtil;

/**
 * 异步添加或者删除学员信息
 * @author lx
 * @date 2017年12月15日 下午12:13:10
 */
@Service
public class OaTaskAsynAddStuService
{
	private static final Logger log = LoggerFactory.getLogger(OaTaskAsynAddStuService.class);
	
	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;
	
	@Autowired
	private StudentExamAffirmMapper examAffirmMapper;
	
	@Autowired
	private StudentGraduateDataMapper graduateDataMapper;
	
	@Autowired
	private StudentXuexinMapper studentXueXinMapper;
	
	@Reference(version="1.0")
	private UsInfoApi usInfoApi;
	
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
    private StudentCityAffirmGKMapper ctiyAffirmGKMapper;
    
    @Autowired
    private StudentGraduateExamGKMapper gkUnifiedExamMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	/*******************异步添加**********************************/
	public void addStuInfoAsyn(List<OaTaskStudentInfo> list,OaTaskInfo taskInfo) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					addAllStu(ii, eSize, count, list,taskInfo);
					isDone++;
					log.error("----------------------------------- 学服任务根据条件添加学员： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void addAllStu(int num, int eSize, int all,List<OaTaskStudentInfo> importList, OaTaskInfo taskInfo) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 学服任务根据条件添加学员： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<OaTaskStudentInfo> list = importList.subList(_start, _start + __size);
			if (list != null) {
				for (OaTaskStudentInfo stuInfo : list) {
					try {
						int count = oaTaskInfoMapper.getCountByLearnIdAndTaskId(stuInfo.getLearnId(), taskInfo.getTaskId());
						if(count ==0){
							String userId = oaTaskInfoMapper.getUserIdByLearnId(stuInfo.getLearnId());
							if (StringUtil.hasValue(userId)) {
								//调用接口获取openId;
								Object openId = usInfoMapper.selectUserOpenId(userId);
								if(null != openId){
									stuInfo.setOpenId(openId.toString());
								}
							}
							stuInfo.setTaskId(taskInfo.getTaskId());
							oaTaskInfoMapper.singleAddStu(stuInfo);
							//针对考场确认的任务执行
							if(taskInfo.getTaskType().equals("4")){
								StudentExamAffirmInfo affirmInfo  =new StudentExamAffirmInfo();
								affirmInfo.setLearnId(stuInfo.getLearnId());
								affirmInfo.setTaskId(taskInfo.getTaskId());
								affirmInfo.setEyId(taskInfo.getEyId());
								affirmInfo.setAffirmId(IDGenerator.generatorId());
								examAffirmMapper.singleAddStuExamAffirm(affirmInfo);
							}else if(taskInfo.getTaskType().equals("6")){ //毕业资料提交
								StudentGraduateDataInfo dataInfo  = new StudentGraduateDataInfo();
								dataInfo.setLearnId(stuInfo.getLearnId());
								dataInfo.setTaskId(taskInfo.getTaskId());
								dataInfo.setStdId(stuInfo.getStdId());
								dataInfo.setId(IDGenerator.generatorId());
								graduateDataMapper.singleAddStuGraduateData(dataInfo);
							}else if(taskInfo.getTaskType().equals("7")){ //学信网信息核对
								StudentXuexinInfo xueXinInfo = new StudentXuexinInfo();
								xueXinInfo.setLearnId(stuInfo.getLearnId());
								xueXinInfo.setTaskId(taskInfo.getTaskId());
								xueXinInfo.setXuexinId(IDGenerator.generatorId());
								
								studentXueXinMapper.singleAddStuXueXinInfo(xueXinInfo);
							}else if(taskInfo.getTaskType().equals("8")){ //学位英语
								StudentDegreeEnglishInfo englishInfo = new StudentDegreeEnglishInfo();
								englishInfo.setLearnId(stuInfo.getLearnId());
								englishInfo.setTaskId(taskInfo.getTaskId());
								englishInfo.setDegreeId(IDGenerator.generatorId());
								stuDegreeEnglishMapper.singleAddStuEnglishInfo(englishInfo);
								
							}else if(taskInfo.getTaskType().equals("9")){ //毕业论文及报告
								int cunt =stuGraduatePaperMapper.checkIfExistByLearnId(stuInfo.getLearnId());
								if (cunt == 0) {
									StudentGraduatePaperInfo paperInfo = new StudentGraduatePaperInfo();
									paperInfo.setLearnId(stuInfo.getLearnId());
									paperInfo.setTaskId(taskInfo.getTaskId());
									paperInfo.setGpId(IDGenerator.generatorId());
									stuGraduatePaperMapper.singleAddStuGraduatePaperInfo(paperInfo);
								}
							}else if(taskInfo.getTaskType().equals("10")){ //开课通知
								StudentLectureNoticeInfo noticeInfo = new StudentLectureNoticeInfo();
								noticeInfo.setLearnId(stuInfo.getLearnId());
								noticeInfo.setTaskId(taskInfo.getTaskId());
								noticeInfo.setLectureId(IDGenerator.generatorId());
								stuLectureNoticeMapper.singleAddStuLectureNotcieInfo(noticeInfo);
							}else if(taskInfo.getTaskType().equals("11")){ //新生学籍资料提交
								int cunt =stuCollectMapper.checkIfExistByLearnId(stuInfo.getLearnId());
								if(cunt ==0){
									StudentCollectInfo collInfo = new StudentCollectInfo();
									collInfo.setLearnId(stuInfo.getLearnId());
									collInfo.setTaskId(taskInfo.getTaskId());
									collInfo.setCtId(IDGenerator.generatorId());
									stuCollectMapper.singleAddStuCollectInfo(collInfo);
								}
							}else if(taskInfo.getTaskType().equals("12")){ //青书学堂
								int cunt = stuQingShuMapper.checkIfExistByLearnId(stuInfo.getLearnId());
								if(cunt ==0){
									StudentQingshuInfo shuInfo = new StudentQingshuInfo();
									shuInfo.setLearnId(stuInfo.getLearnId());
									shuInfo.setTaskId(taskInfo.getTaskId());
									shuInfo.setQingshuId(IDGenerator.generatorId());
									stuQingShuMapper.singleAddStuQingShuInfo(shuInfo);
								}
							}else if(taskInfo.getTaskType().equals("13")){ //国开城市确认
								int cunt = ctiyAffirmGKMapper.checkIfExistByLearnId(taskInfo.getEyId(),stuInfo.getLearnId());
								if(cunt ==0){
									StudentCityAffirmGKInfo cityAffirmInfo = new StudentCityAffirmGKInfo();
									cityAffirmInfo.setLearnId(stuInfo.getLearnId());
									cityAffirmInfo.setTaskId(taskInfo.getTaskId());
									cityAffirmInfo.setEyId(taskInfo.getEyId());
									cityAffirmInfo.setAffirmId(IDGenerator.generatorId());
									ctiyAffirmGKMapper.singleAddStuCityAffirmInfo(cityAffirmInfo);
								}
							}else if(taskInfo.getTaskType().equals("14")){ //国开统考
								StudentGraduateExamGKInfo gkUnifiedInfo = gkUnifiedExamMapper.getStudentGraduateExamGKById(stuInfo.getLearnId());
								if(gkUnifiedInfo == null){
									StudentGraduateExamGKInfo gkUnifiedExam = new StudentGraduateExamGKInfo();
									gkUnifiedExam.setLearnId(stuInfo.getLearnId());
									gkUnifiedExam.setTaskId(taskInfo.getTaskId());
									gkUnifiedExam.setFollowId(IDGenerator.generatorId());
									gkUnifiedExamMapper.singleAddStuUnifiedExamInfo(gkUnifiedExam);
									//默认初始化两个科目
									List<StudentGraduateExamGKInfoSub> gkUnifiedExmaSub = new ArrayList<>();      //国开统考对应的科目
									for(int i=1;i<=2;i++){
										StudentGraduateExamGKInfoSub subInfo = new StudentGraduateExamGKInfoSub();
										subInfo.setId(IDGenerator.generatorId());
										subInfo.setFollowId(gkUnifiedExam.getFollowId());
										subInfo.setEnrollSubject(i+"");
										gkUnifiedExmaSub.add(subInfo);
									}
									gkUnifiedExamMapper.addStuGkUnifiedExamSubInfo(gkUnifiedExmaSub);
								}else{
									gkUnifiedInfo.setTaskId(taskInfo.getTaskId());
									gkUnifiedExamMapper.singleUpdateStuUnifiedExamInfo(gkUnifiedInfo);
								}
							}
						}
					} catch (Exception e) {
						log.error("------------ 学服任务根据条件添加学员：添加异常="+stuInfo.getLearnId()+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 学服任务根据条件添加学员： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
	
	
	
	
	
	
	/**********************异步删除*************************************/
	public void delStuInfoAsyn(List<OaTaskStudentInfo> list,OaTaskInfo taskInfo) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					delAllStu(ii, eSize, count, list,taskInfo);
					isDone++;
					log.error("----------------------------------- 学服任务根据条件删除学员： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void delAllStu(int num, int eSize, int all,List<OaTaskStudentInfo> importList, OaTaskInfo taskInfo) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 学服任务根据条件删除学员： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<OaTaskStudentInfo> list = importList.subList(_start, _start + __size);
			if (list != null) {
				for (OaTaskStudentInfo stuInfo : list) {
					try {
						oaTaskInfoMapper.aloneDelStu(stuInfo.getLearnId(), taskInfo.getTaskId());
						if (taskInfo.getTaskType().equals("4")) {
							// 删除考场确认的信息
							examAffirmMapper.aloneDelStuExamAffirm(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("6")){
							//删除毕业资料提交
							graduateDataMapper.aloneDelStuGraduateData(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("7")){
							//删除信息网信息核对
							studentXueXinMapper.aloneDelStuXueXinInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("8")){
							//学位英语
							stuDegreeEnglishMapper.aloneDelStuDegreeEnglishInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("9")){
							//毕业论文及报告
							stuGraduatePaperMapper.aloneDelStuGraduatePaperInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("10")){
							//开课通知
							stuLectureNoticeMapper.aloneDelStuLectureNotcieInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("11")){
							//学籍资料收集
							stuCollectMapper.aloneDelStuCollectInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("12")){
							//青书学堂
							stuQingShuMapper.aloneDelStuQingShuInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("13")){
							//国开城市确认
							ctiyAffirmGKMapper.aloneDelStuCityAffirmInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}else if(taskInfo.getTaskType().equals("14")){
							gkUnifiedExamMapper.aloneDelStuGkUnifiedExamInfo(stuInfo.getLearnId(), taskInfo.getTaskId());
						}
					} catch (Exception e) {
						log.error("------------ 学服任务根据条件删除学员：添加异常="+stuInfo.getLearnId()+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 学服任务根据条件删除学员： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
}
