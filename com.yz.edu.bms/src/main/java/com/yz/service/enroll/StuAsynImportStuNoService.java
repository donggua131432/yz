package com.yz.service.enroll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.enroll.BdStdRegisterMapper;
import com.yz.model.enroll.regist.BdRegistImport;

/**
 * 异步导入学号
 * @author lx
 * @date 2017年12月16日 下午3:31:47
 */
@Service
public class StuAsynImportStuNoService
{
	private static final Logger log = LoggerFactory.getLogger(StuAsynImportStuNoService.class);
	
	@Autowired
	private BdStdRegisterMapper stuMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	/*******************异步添加**********************************/
	public void importStuNoAsyn(List<BdRegistImport> list) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					updateStuNo(ii, eSize, count, list);
					isDone++;
					log.error("----------------------------------- 导入学员编号和学号： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void updateStuNo(int num, int eSize, int all,List<BdRegistImport> importList) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 导入学员编号和学号： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<BdRegistImport> list = importList.subList(_start, _start + __size);
			if (list != null) {
				for (BdRegistImport reg : list) {
					try {
						ArrayList<String> learnIds = stuMapper.selectLearnIdByCond(reg.getIdCard(), reg.getGrade());
						if (null == learnIds || learnIds.size() <= 0) {
							throw new IllegalArgumentException("身份证号为" + reg.getIdCard() + "的学员，学业信息为空！");
						}
						stuMapper.updateSchoolRollStdNo(learnIds, reg.getSchoolRoll(),reg.getStdNo());
					} catch (Exception e) {
						log.error("------------ 导入学员编号和学号：添加异常="+reg.getIdCard()+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 导入学员编号和学号： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
}
