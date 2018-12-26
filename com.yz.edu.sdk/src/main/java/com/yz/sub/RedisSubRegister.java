package com.yz.sub;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.context.ApplicationContext; 
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
 

@Component(value="redisSubRegister")
/**
 * 
 * @desc RedisSubRegister 注册 
 * @author Administrator
 *
 */
public class RedisSubRegister implements ApplicationListener<ContextRefreshedEvent>  {

	private Logger logger =LoggerFactory.getLogger(RedisSubRegister.class);
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
		if (parent == null) {
			ApplicationContext ac = contextRefreshedEvent.getApplicationContext(); 
			Map<String, Object> beans = ac.getBeansWithAnnotation(JedisPubSub.class);
			registerRedisSub(beans);
		}
	}
	
	/**
	 * 
	 * @param beans
	 */
	private void registerRedisSub(Map<String, Object> beans)
	{
		if(beans!=null)
		{
		 	Iterator<Entry<String, Object>>iter = beans.entrySet().iterator();
		 	while(iter.hasNext())
		 	{
		 		Entry<String, Object> entry = iter.next();
		 		Object handler= entry.getValue();
		 		if(handler!=null&&handler instanceof JedisPubSubHandler)
		 		{
		 			JedisPubSub sub = handler.getClass().getAnnotation(JedisPubSub.class);
		 			JedisPubSubHandler subHandler = (JedisPubSubHandler)handler;
		 			subHandler.setChannel(sub.channel());
		 			subHandler.setConvert(sub.convert());
		 			subHandler.setEnable(sub.enable());
		 			subHandler.setName(entry.getKey());
		 			subHandler.setTarget(sub.target());
		 			subHandler.setPubType(sub.pubType());
		 			subHandler.setSeq(sub.seq());
		 			JedisSubRegister.getInstance().addSubRegister(subHandler);
		 		}
		 	}
		 	JedisSubRegister.getInstance().startPubSub();
		 	logger.info("beans:{}",beans);
		}
	}
 

}
