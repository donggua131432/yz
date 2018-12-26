package com.yz.job.task;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.service.UserRegisterService;
import com.yz.model.UserRegisterEvent; 
import com.yz.task.YzTaskConstants; 

/**
 * thread safe
 * 
 * @desc 用户注册事件
 * @author Administrator
 * @description
 * @step 1 // 建立跟进关系 
 *       2 发送公众号消息 推送给招生老师，注册信息 
 *       3 赠送智米优惠券 通过规则引擎解析相应的规则 
 *       4 //如果已赠送过智米，后续赠送优惠券，不造成数据回滚，手动添加优惠券 ？
 *       5 赠送抽奖券 ？
 */
@Component(value = "userRegisterTask")
@YzJob(queueName = YzTaskConstants.YZ_USER_REGISTER_EVENT, 
taskDesc = YzTaskConstants.YZ_USER_REGISTER_EVENT_DESC, cron = "0/5 * * * * ?", 
shardingTotalCount = 3, targetCls = UserRegisterEvent.class,log= true)
public class UserRegisterTask extends AbstractSimpleTask<UserRegisterEvent> {

	@Autowired
	private UserRegisterService userRegisterService;
	
	@Override
	public void executeRedis(UserRegisterEvent event) {
		userRegisterService.register(event);
	}

}
