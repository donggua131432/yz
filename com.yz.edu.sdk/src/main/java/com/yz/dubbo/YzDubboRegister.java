package com.yz.dubbo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.yz.scan.CustomerScanService;
import com.yz.spi.YzSPIFactory;
import com.yz.util.StringUtil;

/**
 * 
 * @author lingdian
 *
 */
@Component(value = "yzDubboRegisterBean")
public class YzDubboRegister
		implements DisposableBean, BeanFactoryPostProcessor, BeanPostProcessor, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(YzDubboRegister.class);

	private final Set<ServiceConfig<?>> serviceConfigs = new ConcurrentHashSet<ServiceConfig<?>>();

	private final ConcurrentMap<String, ReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, ReferenceBean<?>>();

	private ApplicationContext applicationContext;

	private String scanPackage = "com.yz.api.impl,com.yz.service";

	public YzDubboRegister() {
		CustomerScanService pr = YzSPIFactory.getInstance().getExtension(CustomerScanService.class,"YZ_CUSTOM_SPI");
		if (StringUtil.isNotBlank(pr.getDubboScanPath())) {
			scanPackage = pr.getDubboScanPath();
		}
		logger.info("scanPackage:{}",scanPackage);
	}

	private BeanDefinitionRegistry beanFactory;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 */
	private void scanDubbo() {
		try {
			// init scanner
			Class<?> scannerClass = ReflectUtils
					.forName("org.springframework.context.annotation.ClassPathBeanDefinitionScanner");
			Object scanner = scannerClass.getConstructor(new Class<?>[] { BeanDefinitionRegistry.class, boolean.class })
					.newInstance(new Object[] { (BeanDefinitionRegistry) beanFactory, true });
			// add filter
			Class<?> filterClass = ReflectUtils.forName("org.springframework.core.type.filter.AnnotationTypeFilter");
			Object filter = filterClass.getConstructor(Class.class).newInstance(Service.class);
			Method addIncludeFilter = scannerClass.getMethod("addIncludeFilter",
					ReflectUtils.forName("org.springframework.core.type.filter.TypeFilter"));
			addIncludeFilter.invoke(scanner, filter);
			// scan packages
			String[] packages = Constants.COMMA_SPLIT_PATTERN.split(scanPackage);
			Method scan = scannerClass.getMethod("scan", new Class<?>[] { String[].class });
			scan.invoke(scanner, new Object[] { packages });
		} catch (Throwable e) {
			logger.error("scanner error:{}", ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (beanFactory instanceof BeanDefinitionRegistry) {
			this.beanFactory = (BeanDefinitionRegistry) beanFactory;
			scanDubbo();
		}
	}

	public void destroy() throws Exception {
		for (ServiceConfig<?> serviceConfig : serviceConfigs) {
			try {
				serviceConfig.unexport();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
		for (ReferenceConfig<?> referenceConfig : referenceConfigs.values()) {
			try {
				referenceConfig.destroy();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!isMatchPackage(bean)) {
			return bean;
		}
		Class<?> clazz = bean.getClass();
		if (isProxyBean(bean)) {
			clazz = AopUtils.getTargetClass(bean);
		}
		Service service = clazz.getAnnotation(Service.class);
		if (service != null) {
			ServiceBean<Object> serviceConfig = new ServiceBean<Object>(service);
			if (void.class.equals(service.interfaceClass()) && "".equals(service.interfaceName())) {
				if (clazz.getInterfaces().length > 0) {
					serviceConfig.setInterface(clazz.getInterfaces()[0]);
				} else {
					throw new IllegalStateException("Failed to export remote service class " + clazz.getName()
							+ ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
				}
			}
			if (applicationContext != null) {
				serviceConfig.setApplicationContext(applicationContext);
				if (service.registry() != null && service.registry().length > 0) {
					List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
					for (String registryId : service.registry()) {
						if (registryId != null && registryId.length() > 0) {
							registryConfigs
									.add((RegistryConfig) applicationContext.getBean(registryId, RegistryConfig.class));
						}
					}
					serviceConfig.setRegistries(registryConfigs);
				}
				if (service.provider() != null && service.provider().length() > 0) {
					serviceConfig.setProvider(
							(ProviderConfig) applicationContext.getBean(service.provider(), ProviderConfig.class));
				}
				if (service.monitor() != null && service.monitor().length() > 0) {
					serviceConfig.setMonitor(
							(MonitorConfig) applicationContext.getBean(service.monitor(), MonitorConfig.class));
				}
				if (service.application() != null && service.application().length() > 0) {
					serviceConfig.setApplication((ApplicationConfig) applicationContext.getBean(service.application(),
							ApplicationConfig.class));
				}
				if (service.module() != null && service.module().length() > 0) {
					serviceConfig
							.setModule((ModuleConfig) applicationContext.getBean(service.module(), ModuleConfig.class));
				}
				if (service.provider() != null && service.provider().length() > 0) {
					serviceConfig.setProvider(
							(ProviderConfig) applicationContext.getBean(service.provider(), ProviderConfig.class));
				} else {

				}
				if (service.protocol() != null && service.protocol().length > 0) {
					List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
					// modified by lishen; fix dubbo's bug
					for (String protocolId : service.protocol()) {
						if (protocolId != null && protocolId.length() > 0) {
							protocolConfigs
									.add((ProtocolConfig) applicationContext.getBean(protocolId, ProtocolConfig.class));
						}
					}
					serviceConfig.setProtocols(protocolConfigs);
				}
				try {
					serviceConfig.afterPropertiesSet();
				} catch (RuntimeException e) {
					throw (RuntimeException) e;
				} catch (Exception e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			serviceConfig.setRef(bean);
			serviceConfigs.add(serviceConfig);
			serviceConfig.export();
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!isMatchPackage(bean)) {
			return bean;
		}
		Class<?> clazz = bean.getClass();
		if (isProxyBean(bean)) {
			clazz = AopUtils.getTargetClass(bean);
		}
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.length() > 3 && name.startsWith("set") && method.getParameterTypes().length == 1
					&& Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
				try {
					Reference reference = method.getAnnotation(Reference.class);
					if (reference != null) {
						Object value = refer(reference, method.getParameterTypes()[0]);
						if (value != null) {
							method.invoke(bean, new Object[] { value });
						}
					}
				} catch (Exception e) {
					throw new BeanInitializationException("Failed to init remote service reference at method " + name
							+ " in class " + bean.getClass().getName(), e);

				}
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				Reference reference = field.getAnnotation(Reference.class);
				if (reference != null) {
					/*Object obj = ApplicationContextUtil.getBeanIgnoreEx(field.getType());
					if (obj != null) {
						field.set(bean, obj);
						return bean;
					}*/
					Object value = refer(reference, field.getType());
					if (value != null) {
						field.set(bean, value);
					}
				}
			} catch (Exception e) {
				// modified by lishen
				throw new BeanInitializationException("Failed to init remote service reference at filed "
						+ field.getName() + " in class " + bean.getClass().getName(), e);
				// logger.error("Failed to init remote service reference at
				// filed " + field.getName() + " in class " +
				// bean.getClass().getName() + ", cause: " + e.getMessage(), e);
			}
		}
		return bean;
	}

	private Object refer(Reference reference, Class<?> referenceClass) { // method.getParameterTypes()[0]
		String interfaceName;
		if (!"".equals(reference.interfaceName())) {
			interfaceName = reference.interfaceName();
		} else if (!void.class.equals(reference.interfaceClass())) {
			interfaceName = reference.interfaceClass().getName();
		} else if (referenceClass.isInterface()) {
			interfaceName = referenceClass.getName();
		} else {
			throw new IllegalStateException(
					"The @Reference undefined interfaceClass or interfaceName, and the property type "
							+ referenceClass.getName() + " is not a interface.");
		}
		String key = reference.group() + "/" + interfaceName + ":" + reference.version();
		ReferenceBean<?> referenceConfig = referenceConfigs.get(key);
		if (referenceConfig == null) {
			referenceConfig = new ReferenceBean<Object>(reference);
			if (void.class.equals(reference.interfaceClass()) && "".equals(reference.interfaceName())
					&& referenceClass.isInterface()) {
				referenceConfig.setInterface(referenceClass);
			}
			if (applicationContext != null) {
				referenceConfig.setApplicationContext(applicationContext);
				if (reference.registry() != null && reference.registry().length > 0) {
					List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
					for (String registryId : reference.registry()) {
						if (registryId != null && registryId.length() > 0) {
							registryConfigs
									.add((RegistryConfig) applicationContext.getBean(registryId, RegistryConfig.class));
						}
					}
					referenceConfig.setRegistries(registryConfigs);
				}
				if (reference.consumer() != null && reference.consumer().length() > 0) {
					referenceConfig.setConsumer(
							(ConsumerConfig) applicationContext.getBean(reference.consumer(), ConsumerConfig.class));
				}
				if (reference.monitor() != null && reference.monitor().length() > 0) {
					referenceConfig.setMonitor(
							(MonitorConfig) applicationContext.getBean(reference.monitor(), MonitorConfig.class));
				}
				if (reference.application() != null && reference.application().length() > 0) {
					referenceConfig.setApplication((ApplicationConfig) applicationContext
							.getBean(reference.application(), ApplicationConfig.class));
				}
				if (reference.module() != null && reference.module().length() > 0) {
					referenceConfig.setModule(
							(ModuleConfig) applicationContext.getBean(reference.module(), ModuleConfig.class));
				}
				if (reference.consumer() != null && reference.consumer().length() > 0) {
					referenceConfig.setConsumer(
							(ConsumerConfig) applicationContext.getBean(reference.consumer(), ConsumerConfig.class));
				}
				try {
					referenceConfig.afterPropertiesSet();
				} catch (RuntimeException e) {
					throw (RuntimeException) e;
				} catch (Exception e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			referenceConfigs.putIfAbsent(key, referenceConfig);
			referenceConfig = referenceConfigs.get(key);
		}
		return referenceConfig.get();
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	private boolean isMatchPackage(Object bean) {
		if (StringUtil.isBlank(scanPackage)) {
			return false;
		}
		Class clazz = bean.getClass();
		if (isProxyBean(bean)) {
			clazz = AopUtils.getTargetClass(bean);
		}
		String beanClassName = clazz.getName();
		for (String pkg : Constants.COMMA_SPLIT_PATTERN.split(scanPackage)) {
			if (beanClassName.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

	private boolean isProxyBean(Object bean) {
		return AopUtils.isAopProxy(bean);
	}

}
