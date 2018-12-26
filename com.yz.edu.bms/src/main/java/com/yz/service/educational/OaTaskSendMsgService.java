package com.yz.service.educational;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

import com.yz.dao.educational.OaTaskInfoMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.model.educational.OaStudentTask;
import com.yz.util.StringUtil;


/**
 * 学费任务异步发消息
 * @author lx
 * @date 2017年10月27日 下午12:18:37
 */
@Service
public class OaTaskSendMsgService
{
	private static final Logger log = LoggerFactory.getLogger(OaTaskSendMsgService.class);
	
	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;
	
    @Autowired
    private UsInfoMapper usInfoMapper;
	
	private int tSize = 5;

	private int isDone = 0;

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public void sendTaskMessage(String taskId) {
		List<OaStudentTask> stuTaskList = oaTaskInfoMapper.getOaStudentTask(taskId);
		
		final int count = stuTaskList.size();

		final int eSize = count / tSize;

		for (int i = 1; i <= tSize; i++) {
			final int ii = i;
			executor.execute(new Runnable() {

				@Override
				public void run() {
					startSend(ii, eSize, count, taskId);
					isDone++;
					log.error("----------------------------------- 学服任务信息推送： 线程[" + ii + "] is done! | " + isDone);
				}
			});
		}

	}

	private void startSend(int num, int eSize, int all, String taskId) {

		int _size = 400;

		int _count = num == tSize ? all : eSize * num;

		int _start = num == 1 ? 0 : ((num - 1) * eSize);

		log.error("------------------------------- 学服任务信息推送： 线程[" + num + "] ：起始：" + _start + " | 总数：" + _count + " | 每页："
				+ _size);

		int batch = 1;

		while (_start < _count) {
			int __size = 0;
			if (_start + _size > _count) {
				__size = _count - _start;
			} else {
				__size = _size;
			}
			List<OaStudentTask> list = oaTaskInfoMapper.getOaStudentTaskForPage(_start, _start + __size,taskId);
			if (list != null) {
				for (OaStudentTask task : list) {
					String userId = oaTaskInfoMapper.getUserIdByLearnId(task.getLearnId());
					if(StringUtil.hasValue(userId) && StringUtil.isEmpty(task.getOpenId())){
						//调用接口获取openId;
						Object openId = usInfoMapper.selectUserOpenId(userId);
						if (null != openId) {
							task.setOpenId(openId.toString());
						}
					}
					if(StringUtil.hasValue(task.getOpenId())){
						try {
							//发送消息  走 task项目
							boolean isSuccess = false;//wechatApi.sendTaskMsg(task.getTaskTitle(), task.getEndTime(), task.getOpenId(),task.getLearnId());
							if (isSuccess) {
								// 改变状态
								oaTaskInfoMapper.updateStudentTaskIsNotify(task.getTaskId(), task.getLearnId());	
							}
						} catch (Exception e) {
							log.error("------------ 学服任务信息推送：修改学员通知状态异常="+task.getLearnId()+"信息:"+e.getMessage());
						}
						
					}
					
				}

				_start += _size;

				batch++;

				log.error("------------------------------- 学服任务信息推送： [" + batch + "] 线程 [" + num + "] ：当前记录数 ：" + _start);
			}
		}
	}

}
