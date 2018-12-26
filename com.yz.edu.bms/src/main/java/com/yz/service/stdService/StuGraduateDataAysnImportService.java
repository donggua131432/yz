package com.yz.service.stdService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.educational.OaTaskInfoMapper;
import com.yz.dao.stdService.StudentExamAffirmMapper;
import com.yz.dao.stdService.StudentGraduateDataMapper;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentGraduateDataInfo;

/**
 * 异步导入信息
 * @author lx
 * @date 2017年12月12日 上午10:58:12
 */
@Service
public class StuGraduateDataAysnImportService
{
	private static final Logger log = LoggerFactory.getLogger(StuGraduateDataAysnImportService.class);
	
	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;
	
	@Autowired
	private StudentGraduateDataMapper graduateDataMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public void importStuGraduateDataAsyn(List<StudentGraduateDataInfo> list) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					importStu(ii, eSize, count, list);
					isDone++;
					log.error("----------------------------------- 学服毕业资料任务导入学员信息： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void importStu(int num, int eSize, int all,List<StudentGraduateDataInfo> importList) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 学服毕业资料任务导入学员信息： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<StudentGraduateDataInfo> list = importList.subList(_start, _start + __size);
			if (list != null) {
				for (StudentGraduateDataInfo studentInfo : list) {
					try {
						OaTaskStudentInfo stuInfo = oaTaskInfoMapper.getOaTaskStudentInfoByIdCard(studentInfo.getIdCard(),studentInfo.getGrade());
						if (null == stuInfo) { //讲道理不会走这不一步
							throw new IllegalArgumentException("请检查学员身份证:"+studentInfo.getIdCard()+"无报读信息");
						}
						studentInfo.setLearnId(stuInfo.getLearnId());
						graduateDataMapper.updateStuGraduateData(studentInfo);
					} catch (Exception e) {
						log.error("------------ 学服毕业资料任务导入学员信息：导入异常="+studentInfo.getIdCard()+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 学服毕业资料任务导入学员信息： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
}
