package com.yz.service.enroll;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.model.admin.BaseUser;
/**
 * 分配学员校区归属异步类
 * @author lx
 * @date 2017年12月21日 下午8:13:42
 */
@Service
public class BdStdCampusAsynService
{

	private static final Logger log = LoggerFactory.getLogger(BdStdCampusAsynService.class);
	
	@Autowired
	private BdStdEnrollMapper enrollMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	
	/*******************异步分配**********************************/
	public void asynStdCampusOper(List<Map<String, String>> list,String campusId,BaseUser user) {
		
		final int count = list.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					updateStuCampus(ii, eSize, count, list,campusId,user);
					isDone++;
					log.error("----------------------------------- 分配学员归属校区： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void updateStuCampus(int num, int eSize, int all,List<Map<String, String>> stuList,String campusId,BaseUser user) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 分配学员归属校区： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<Map<String, String>> list = stuList.subList(_start, _start + __size);
			if (list != null) {
				for (Map<String, String> reg : list) {
					try {
						enrollMapper.updateStuCampusId(reg.get("learn_id"), campusId,user);
						log.info("分配学员:------操作人:"+user.getRealName()+"--学业--"+reg.get("learn_id")+"所属校区:"+campusId);
					} catch (Exception e) {
						log.error("------------ 分配学员归属校区：添加异常="+reg.get("learn_id")+"信息:"+e.getMessage());
					}
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 分配学员归属校区： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}
}
