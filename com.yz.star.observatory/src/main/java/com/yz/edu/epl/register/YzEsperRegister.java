package com.yz.edu.epl.register;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.epl.annotation.EplListener;
import com.yz.edu.epl.listener.YzEsperlListener;
import com.yz.edu.epl.service.EPLService;

/**
 * 
 * @desc
 * @author Administrator
 *
 */
@Component(value = "esperRegister")
public class YzEsperRegister implements ObservatoryStarConstant, ApplicationListener<ContextRefreshedEvent> {


	private EPLService eplService = EPLService.getInstance();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
		if (parent == null) {
			ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
			Map<String, Object> listeners = ac.getBeansWithAnnotation(EplListener.class);
			listener(listeners);
		}
	}

	/**
	 * 
	 * @param listeners
	 */
	@SuppressWarnings("rawtypes")
	private void listener(Map<String, Object> listeners) {
		if (listeners != null) {
			listeners.entrySet().parallelStream().forEach(v -> {
				Object bean = v.getValue();
				EplListener listener = v.getValue().getClass().getAnnotation(EplListener.class);
				YzEsperlListener obj = null;
				if (bean instanceof YzEsperlListener) {
					obj = (YzEsperlListener) bean;
					eplService.addListener(listener, obj);
				}
			});
			eplService.startListener();
		}
	}

}
