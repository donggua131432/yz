package com.yz.conf;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.yz.locator.BccServiceLocator;
import com.yz.model.YzService;
import com.yz.model.YzServiceInfo;
import com.yz.util.JsonUtil;
 

/**
 * 
 * @author Administrator
 *
 */
@Component(value="yzBccRegister")
public class BccRegister implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
 
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
		if (parent == null) {
			ApplicationContext ac = contextRefreshedEvent.getApplicationContext(); 
			Map<String, Object> beans = ac.getBeansWithAnnotation(Service.class);
			register(beans);
		}
	}
	
	/**
	 * 
	 * @param tasks
	 */
	private void register(Map<String, Object> beans) 
	{
		if(beans!=null&&!beans.isEmpty())
		{
			Iterator<Entry<String, Object>>iter = beans.entrySet().iterator();
			Entry<String, Object>entry = null;
			while(iter.hasNext())
			{
				 entry = iter.next();
				 addServiceInfo(entry.getValue());
			}
		}
	}
	
	/**
	 * 扫描所有bean  
	 * @param bean
	 */
	private void addServiceInfo(Object bean)
	{
		 if(bean!=null)
		 {
			  Method[] methods = AopUtils.getTargetClass(bean).getDeclaredMethods();
			  for(Method method:methods)
			  {
				  YzService info = method.getDeclaredAnnotation(YzService.class);
				  if(info!=null)
				  {
					  YzServiceInfo serviceInfo = new YzServiceInfo();
					  serviceInfo.setAppName(info.sysBelong());
					  serviceInfo.setAppType(info.appType());
					  serviceInfo.setName(info.methodName());
					  serviceInfo.setInterfaceName(bean.getClass().getName());
					  serviceInfo.setInterfaceMethod(method.getName());
					  serviceInfo.setMethodRemark(info.methodRemark());
					  serviceInfo.setNeedLogin(info.needLogin());
					  serviceInfo.setLocal(true);
					  serviceInfo.setVersion(info.version());
					  serviceInfo.setUseCache(info.useCache());
					  serviceInfo.setCacheRule(info.cacheRule());
					  serviceInfo.setCacheKey(info.cacheKey());
					  serviceInfo.setRedisName(info.redisName());
					  serviceInfo.setCacheHandler(info.cacheHandler());
					  serviceInfo.setCacheExpire(info.cacheExpire());
					  logger.debug("addLocalServiceInfo.serviceInfo{}",JsonUtil.object2String(serviceInfo));
					  BccServiceLocator.getInstance().addLocalServiceInfo(serviceInfo );
				  }
			  }
		 }
		 
	}

}
