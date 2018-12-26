package com.yz.edu.mq;

import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yz.mq.Event;

/**
 * 
 * @desc MQConsumer的注册
 * @author lingdian
 *
 */
@Component
public class MQRegister implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory.getLogger(MQRegister.class);
	
	MQConsumer mqConsumer = MQConsumer.getMQConsumer();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
		if (parent == null) {
			ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
			Map<String, Object> beans = ac.getBeansWithAnnotation(Processor.class);
			registerMQConsumer(beans);
		}
	}

	/**
	 * @desc 注册
	 * @param beans
	 */
	private void registerMQConsumer(Map<String, Object> beans) {
		if (beans != null) {
			for (Entry<String, Object> entry : beans.entrySet()) {
				Object handler = entry.getValue();
				Processor processor = handler.getClass().getAnnotation(Processor.class);
				mqConsumer.addTopicHandler(processor.topic(), (MessageHandler<Event>) handler);
			}
			mqConsumer.subscribe();
			logger.info("MQConsumer.subscribe");
		}
	}

}
