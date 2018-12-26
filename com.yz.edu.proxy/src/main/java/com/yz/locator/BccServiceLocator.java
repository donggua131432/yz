package com.yz.locator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.constants.RedisKeyConstants;
import com.yz.model.YzServiceInfo;
import com.yz.redis.RedisService;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

/**
 * 
 * @author Administrator
 *
 */
public class BccServiceLocator {

	private boolean start = false;

	private AtomicLong indexCount = null;

	private static Logger logger = LoggerFactory.getLogger(BccServiceLocator.class);

	/**
	 * 定时拉取最新的Redis服务数据
	 */
	private static ScheduledExecutorService seekGateScheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread thread = new Thread(r);
					thread.setName("proxyServiceLocatorThread");
					return thread;
				}
			});

	/**
	 * @desc 缓存本地服务信息
	 **/
	private static Map<String, YzServiceInfo> _localServiceCache = new HashMap<String, YzServiceInfo>();

	/**
	 * @desc 缓存远程服务信息
	 **/
	private static Map<String, YzServiceInfo> _remoteServiceCache = new ConcurrentHashMap<>();

	private static class BccServiceLocatorHolder {
		private static final BccServiceLocator _instance = new BccServiceLocator();
	}

	private BccServiceLocator() {
		indexCount = new AtomicLong(0);
		startSeekService();
	}

	/**
	 * @desc 启动定时任务 获取redis服务列表
	 *       seekGateScheduledExecutor.scheduleAtFixedRate(()->seekGatewayInfo()
	 *       , 1000l*3, 1000l, TimeUnit.MILLISECONDS);
	 */
	public void startSeekService() {
		if (!start) {
			seekGateScheduledExecutor.scheduleAtFixedRate(
					() -> seekGatewayInfo(), 
					1000l, 1000 * 60l,
					TimeUnit.MILLISECONDS);
			start = true;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static BccServiceLocator getInstance() {
		return BccServiceLocatorHolder._instance;
	}

	/**
	 * @desc 根据类名获取相应的bean
	 * @param name
	 * @return
	 */
	public YzServiceInfo getService(String name) {
		if (_localServiceCache.containsKey(name)) {
			return _localServiceCache.get(name);
		}
		if (_remoteServiceCache.containsKey(name)) {
			return _remoteServiceCache.get(name);
		}
		logger.error("{}not publish!", name);
		return null;
	}
	
	
	/**
	 * @desc 根据key替换serviceInfo
	 * @param name
	 * @return
	 */
	public void replaceRemoteService(YzServiceInfo info) { 
		String name = this.getServiceKey(info);
		_remoteServiceCache.put(name, info);
		logger.info("replaceRemoteService:name.{},info:{}!", name,JsonUtil.object2String(info)); 
	}
	
	/**
	 * @desc 根据key删除serviceInfo
	 * @param name
	 * @return
	 */
	public void removeRemoteService(YzServiceInfo info) { 
		String name = this.getServiceKey(info);
		_remoteServiceCache.remove(name);
		logger.info("removeRemoteService.name.{},info:{}!", name,JsonUtil.object2String(info)); 
	}

	/**
	 * @desc 添加mhsServiceInfo
	 * @param serviceInfo
	 */
	public void addLocalServiceInfo(YzServiceInfo serviceInfo) {
		if (serviceInfo != null) {
			String key = this.getServiceKey(serviceInfo);
			_localServiceCache.put(key, serviceInfo);
		}
	}

	/**
	 * 
	 * 
	 * @desc
	 * 
	 */
	public void seekGatewayInfo() {
		try {
			Map<String, String> serviceList = RedisService.getRedisService()
					.hgetAll(RedisKeyConstants.BCC_GATE_KEY_COUNT_LIST);
			if (serviceList != null && !serviceList.isEmpty()) {
				// 长度一致 认为未改变 ？
				if (_remoteServiceCache.size() == serviceList.size() && indexCount.incrementAndGet() % 1000 != 0) {
					logger.info("not change!indexCount:{}", indexCount);
					return;
				}
				List<String> unableList = new ArrayList<>();
				Iterator<Entry<String, String>> iter = serviceList.entrySet().iterator();
				Entry<String, String> entry = null;
				while (iter.hasNext()) {
					entry = iter.next();
					if (NumberUtils.toInt(entry.getValue()) > 0) {
						Set<String> serviceSet = RedisService.getRedisService().smembers(entry.getKey());
						if (serviceSet != null && !serviceSet.isEmpty()) {
							YzServiceInfo info = serviceSet.stream()
									.map(infoStr -> JsonUtil.str2Object(infoStr, YzServiceInfo.class)).sorted()
									.findFirst().get();
							// 将最新创建的写入本地缓存中
							_remoteServiceCache.put(entry.getKey(), info); // 添加缓存列表
							logger.debug("serviceSet:{},info:{}", JsonUtil.object2String(serviceSet),
									JsonUtil.object2String(info));
						} else {
							logger.error("serviceInfo is null:{}", entry.getKey());
						}
					} else {
						unableList.add(entry.getKey());
					}
				}
				if (!unableList.isEmpty()) {
					String[] arr = new String[unableList.size()];
					unableList.toArray(arr);
					RedisService.getRedisService().hdel(RedisKeyConstants.BCC_GATE_KEY_COUNT_LIST, arr);
				}
			}
		} catch (Exception ex) {
			logger.error("seekGatewayInfo.error:{}", ExceptionUtil.getStackTrace(ex));
		}
	}
	
	/**
	 * 
	 * @param serviceInfo
	 * @return
	 */
	private String getServiceKey(YzServiceInfo serviceInfo)
	{
		StringBuilder key = new StringBuilder(serviceInfo.getAppName()).append("/").append(serviceInfo.getName());
		key.append("/").append(serviceInfo.getVersion());
		return key.toString();
	}
}
