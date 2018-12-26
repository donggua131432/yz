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
 * @desc 用户绑定手机事件
 * @author Administrator
 * @description
 * @step   
 *       1 赠送智米优惠券 通过规则引擎解析相应的规则 
 *       2 //如果已赠送过智米，后续赠送优惠券，不造成数据回滚，手动添加优惠券 ？
 *       3 赠送抽奖券 ？
 */
@Component(value = "userBindMobileTask")
@YzJob(queueName = YzTaskConstants.YZ_USER_BINDMOBILE_EVENT, 
taskDesc = YzTaskConstants.YZ_USER_BINDMOBILE_EVENT_DESC, cron = "0/5 * * * * ?", 
shardingTotalCount = 3, targetCls = UserRegisterEvent.class,log= true)
public class UserBindMobileTask extends AbstractSimpleTask<UserRegisterEvent> {

	@Autowired
	private UserRegisterService userRegisterService;
	
	@Override
	public void executeRedis(UserRegisterEvent event) {
		userRegisterService.bindMobile(event);
	}

}
