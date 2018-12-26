package com.yz.executor;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.yz.dubbo.YzDubboBean;
import com.yz.exception.ExceptionFactory;
import com.yz.locator.BccServiceLocator;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ClassUtil;
import com.yz.util.JsonUtil;
import com.yz.util.MethodUtil;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class YzServiceExecutor {

	private static final Logger logger = LoggerFactory.getLogger(YzServiceExecutor.class);
	

	@Autowired
	private YzDubboBean yzDubboBean;

	@Autowired
	private RegistryConfig registryConfig;

	@Autowired
	private ApplicationConfig applicationConfig;

	/**
	 * 
	 * @param interfaceInfo
	 * @param header
	 * @param body
	 * @return
	 * @throws Throwable
	 */
	public Object execute(YzServiceInfo interfaceInfo, Header header, Body body) throws Exception {
		try {
			CacheInvokeInterceptor invoker = new CacheInvokeInterceptor() {
				@Override
				public Object action() throws Exception {
					return interfaceInfo.isLocal() ? 
							 _execLocal(interfaceInfo, header, body):_execRemote(interfaceInfo, header, body);
				}
			};
			return invoker.invoke(interfaceInfo, header, body);
		} catch (GenericException e) {
			logger.error("interfaceInfo.{}.invoke.error:{}",JsonUtil.object2String(interfaceInfo), ExceptionUtils.getFullStackTrace(e));
			throw ExceptionFactory.getInstance().getRuntimeException(e.getExceptionClass(), e.getExceptionMessage());
		}  catch (Exception e) {
			logger.error("interfaceInfo.{}.invoke.error:{}",JsonUtil.object2String(interfaceInfo),ExceptionUtils.getFullStackTrace(e));
			throw e;
		}
	}

	/**
	 * 
	 * @param interfaceInfo
	 * @param header
	 * @param body
	 * @return
	 * @throws Throwable
	 */
	private Object _execLocal(YzServiceInfo interfaceInfo, Header header, Body body) throws Exception {
		Object bean = ApplicationContextUtil.getApplicationContext()
				.getBean(ClassUtil.getClass(interfaceInfo.getInterfaceName()));
		Object result = MethodUtil.invokeExactMethod(bean, interfaceInfo.getInterfaceMethod(),
				new Object[] { header == null ? new Header() : header, body == null ? new Body() : body });
		logger.info("_execLocal.name{};cost:{}ms", interfaceInfo.getName() );
		return result;
	}

	/**
	 * 
	 * @param interfaceInfo
	 * @param header
	 * @param body
	 * @return
	 * @throws Throwable
	 */
	private Object _execRemote(YzServiceInfo interfaceInfo, Header header, Body body) throws Exception {
		try
		{
			GenericService genericService = this.genericInvoke(interfaceInfo);
			Object result = genericService.$invoke(interfaceInfo.getInterfaceMethod(),
					new String[] { Header.class.getName(),Body.class.getName() }, new Object[] { header, body });
			logger.info("_execRemote.name:{}", interfaceInfo.getName());
			return result;
		}catch(Exception ex)
		{
			logger.error("{}_execRemote.error:{}",JsonUtil.object2String(interfaceInfo),ex);
			throw ex;
		} 
	}

	/***
	 * @desc 获取调用接口信息 
	 * @param interfaceName 服务名称 
	 * @param interfaceVersion 服务版本 
	 * @return
	 */
	public YzServiceInfo getInterfaceInfo(String sysBelog,String interfaceName, String interfaceVersion) {
		StringBuilder sb = new StringBuilder(sysBelog).append("/").append(interfaceName).append("/").append(interfaceVersion);
		return BccServiceLocator.getInstance().getService(sb.toString());
	}

	/**
	 * @desc 泛化调用
	 * @param MhsServiceInfo serviceInfo 信息 
	 * @param interfaceClass 相关的接口类
	 * @param version 服务版本号
	 * @return
	 */
	public GenericService genericInvoke(YzServiceInfo interfaceInfo) {
		ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
		reference.setApplication(applicationConfig);
		reference.setRegistry(registryConfig);
		reference.setInterface(interfaceInfo.getInterfaceName()); // 接口名
		reference.setVersion("1.0");
		reference.setGeneric(true); // 声明为泛化接口
		reference.setCache("lru");
		reference.setProtocol("dubbo");
		reference.setCheck(false);
		reference.setTimeout(interfaceInfo.getTimeout());// 超时时间 3000ms
		//reference.setTimeout(30000);// 超时时间 3000ms
		reference.setRetries(0); // 重试次数
		reference.setCluster(yzDubboBean.getCluster()); // 设置集群方式
		reference.setLoadbalance(yzDubboBean.getLoadbalance()); 
	 
		// ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，
		// 需要缓存，否则重复生成ReferenceConfig可能造成性能问题并且会有内存和连接泄漏。
		// API方式编程时，容易忽略此问题。
		// 这里使用dubbo内置的简单缓存工具类进行缓存

		ReferenceConfigCache cache = ReferenceConfigCache.getCache();
		GenericService genericService = cache.get(reference);
		return genericService;

	}

}
