package com.yz.service.educational;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.educational.OaTaskInfoMapper;
import com.yz.dao.gk.StudentCityAffirmGKMapper;
import com.yz.dao.gk.StudentGraduateExamGKMapper;
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

/**
 * 异步导入信息
 * @author lx
 * @date 2017年12月12日 上午10:58:12
 */
@Service
public class OaTaskImportStuService
{
	private static final Logger log = LoggerFactory.getLogger(OaTaskImportStuService.class);
	
	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;
	
	@Autowired
	private StudentExamAffirmMapper examAffirmMapper;
	
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
    private StudentCityAffirmGKMapper cityAffirmGKMapper;
    
    @Autowired
    private StudentGraduateExamGKMapper gkUnifiedExamMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public void importStuInfoAsyn(List<OaTaskStudentInfo> list,OaTaskInfo taskInfo) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					importStu(ii, eSize, count, list,taskInfo);
					isDone++;
					log.error("----------------------------------- 学服任务导入学员信息： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void importStu(int num, int eSize, int all,List<OaTaskStudentInfo> importList, OaTaskInfo taskInfo) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 学服任务导入学员信息： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
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
				for (OaTaskStudentInfo studentInfo : list) {
					try {
						OaTaskStudentInfo stuInfo = oaTaskInfoMapper.getOaTaskStudentInfoByIdCard(studentInfo.getExt1(),studentInfo.getGrade());
						if (null == stuInfo) {
							throw new IllegalArgumentException("请检查学员身份证:"+studentInfo.getExt1()+"无报读信息");
						}
						int count = oaTaskInfoMapper.getCountByLearnIdAndTaskId(stuInfo.getLearnId(), taskInfo.getTaskId());
						if(null!=stuInfo && count ==0){
							oaTaskInfoMapper.singleImport(stuInfo.getLearnId(), taskInfo.getTaskId(),stuInfo.getTutor());
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
							}else if(taskInfo.getTaskType().equals("7")){ //学信网
								StudentXuexinInfo xueXinInfo = new StudentXuexinInfo();
								xueXinInfo.setLearnId(stuInfo.getLearnId());
								xueXinInfo.setTaskId(taskInfo.getTaskId());
								xueXinInfo.setXuexinId(IDGenerator.generatorId());
								stuXueXinMapper.singleAddStuXueXinInfo(xueXinInfo);
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
							}else  if(taskInfo.getTaskType().equals("13")){ //国开城市确认
								int cunt = cityAffirmGKMapper.checkIfExistByLearnId(taskInfo.getEyId(),studentInfo.getLearnId());
								if(cunt == 0){
									StudentCityAffirmGKInfo cityAffirmGkInfo = new StudentCityAffirmGKInfo();
									cityAffirmGkInfo.setLearnId(stuInfo.getLearnId());
									cityAffirmGkInfo.setTaskId(taskInfo.getTaskId());
									cityAffirmGkInfo.setEyId(taskInfo.getEyId());
									cityAffirmGkInfo.setAffirmId(IDGenerator.generatorId());
									cityAffirmGKMapper.singleAddStuCityAffirmInfo(cityAffirmGkInfo);
								}
							}else if(taskInfo.getTaskType().equals("14")){ //国开统考
								StudentGraduateExamGKInfo gkUnifiedInfo = gkUnifiedExamMapper.getStudentGraduateExamGKById(studentInfo.getLearnId());
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
						}else{
							log.error("------------ 学服任务导入学员信息：无对应信息="+studentInfo.getExt1());
						}
					} catch (Exception e) {
						log.error("------------ 学服任务导入学员信息：导入异常="+studentInfo.getExt1()+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 学服任务导入学员信息： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
}
