package com.yz.dubbo;

import java.lang.reflect.Method; 
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.ExporterListener;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.collect.Maps;
import com.yz.constants.RedisKeyConstants;
import com.yz.model.ServiceExportChannel;
import com.yz.model.YzService;
import com.yz.model.YzServiceInfo;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

/**
 * @author Administrator
 */
@Activate
public class YzExporterListener implements ExporterListener {

	private ThreadLocal<Long> serviceId = ThreadLocal.withInitial(System::nanoTime);

	private static final Logger logger = LoggerFactory.getLogger(YzExporterListener.class);

	private Map<String, String> exportRecordMap = Maps.newHashMap();

	@Override
	public void exported(Exporter<?> exporter) throws RpcException {
		exportServiceInfo(exporter, new ExporterExecutor() {
			@Override
			public void action(YzServiceInfo info) {
				{
					String infoStr = JsonUtil.object2String(info);
					String key = info.getAppName() + "/" + info.getName() + "/" + info.getVersion();
					if (!exportRecordMap.containsKey(key)) {
						ServiceExportChannel event = new ServiceExportChannel();
						event.setServiceInfo(info);
						RedisService.getRedisService().hincrBy(RedisKeyConstants.BCC_GATE_KEY_COUNT_LIST, key, 1l);
						RedisService.getRedisService().sadd(key, infoStr);
						RedisService.getRedisService().publish(RedisKeyConstants.DUBBO_EXPORT_CHANNEL,
								JsonUtil.object2String(event));
						logger.info("exported.serviceInfo:{}", infoStr);
					} else {
						exportRecordMap.put(key, key);
					}
				}
			}
		}, true);
	}

	@Override
	public void unexported(Exporter<?> exporter) {
		exportServiceInfo(exporter, new ExporterExecutor() {
			@Override
			public void action(YzServiceInfo info) {
				{
					String infoStr = JsonUtil.object2String(info);
					String key = info.getAppName() + "/" + info.getInterfaceMethod() + "/" + info.getVersion();
					int count = NumberUtils
							.toInt(RedisService.getRedisService().hget(RedisKeyConstants.BCC_GATE_KEY_COUNT_LIST, key));
					if (count > 0) {
						long unexported = RedisService.getRedisService()
								.hincrBy(RedisKeyConstants.BCC_GATE_KEY_COUNT_LIST, key, -1l);
						delGateWayKey(key);
						logger.info("unexported.serviceInfo:{}", infoStr);
						if (unexported <= 0) {
							ServiceExportChannel event = new ServiceExportChannel();
							event.setServiceInfo(info);
							event.setType(1);
							RedisService.getRedisService().publish(RedisKeyConstants.DUBBO_EXPORT_CHANNEL,
									JsonUtil.object2String(event));
						}
					}
				}
			}
		}, false);
	}

	/**
	 * 
	 * @param invoker
	 * @param method
	 * @param info
	 * @param clsName
	 * @return
	 */
	private void exportServiceInfo(Exporter<?> exporter, ExporterExecutor exec, boolean create) {
		Invoker invoker = exporter.getInvoker();
		Class<?> cls = invoker.getInterface();
		Method[] methods = cls.getDeclaredMethods();
		if (methods != null && methods.length > 0) {
			YzServiceInfo serviceInfo = null;
			YzService info = null;
			for (Method method : methods) {
				info = method.getAnnotation(YzService.class);
				if (info == null)
					continue;
				serviceInfo = this.createServiceInfo(invoker, method, info, cls.getName(), create);
				exec.action(serviceInfo);
			}
		}

	}

	/**
	 * 
	 * @param invoker
	 * @param method
	 * @param info
	 * @param clsName
	 * @return
	 */
	private YzServiceInfo createServiceInfo(Invoker invoker, Method method, YzService info, String clsName,
			boolean create) {
		YzServiceInfo serviceInfo = new YzServiceInfo();
		serviceInfo.setId(serviceId.get());
		serviceInfo.setAppName(info.sysBelong());
		serviceInfo.setAppType(info.appType());
		serviceInfo.setName(info.methodName());
		serviceInfo.setInterfaceName(clsName);
		serviceInfo.setInterfaceMethod(method.getName());
		serviceInfo.setMethodRemark(info.methodRemark());
		serviceInfo.setNeedLogin(info.needLogin());
		serviceInfo.setHost(OSUtil.getIp());
		serviceInfo.setCacheKey(info.cacheKey());
		serviceInfo.setUseCache(info.useCache());
		serviceInfo.setCacheRule(info.cacheRule());
		serviceInfo.setRedisName(info.redisName());
		serviceInfo.setCacheHandler(info.cacheHandler());
		serviceInfo.setVersion(info.version());
		serviceInfo.setTimeout(info.timeout());
		serviceInfo.setNeedToken(info.needToken());
		serviceInfo.setCacheExpire(info.cacheExpire());
		serviceInfo.setReceiveSent(serviceInfo.isReceiveSent());
		serviceInfo.setAsync(serviceInfo.isAsync());
		return serviceInfo;
	}

	/**
	 * 
	 * @param name
	 * @param port
	 */
	private void delGateWayKey(String key) {
		Set<String> gateList = RedisService.getRedisService().smembers(key);
		if (gateList != null && !gateList.isEmpty()) {
			String ip = OSUtil.getIp();
			List<String> unableList = gateList.stream().filter(v -> StringUtil.contains(v, ip)).filter(Objects::nonNull)
					.collect(Collectors.toList());
			if (!unableList.isEmpty()) {
				String[] arr = new String[unableList.size()];
				unableList.toArray(arr);
				RedisService.getRedisService().srem(key, arr);
			}
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private abstract class ExporterExecutor {
		public abstract void action(YzServiceInfo info);
	}

}
