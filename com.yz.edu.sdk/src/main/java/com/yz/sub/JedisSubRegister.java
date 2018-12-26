package com.yz.sub;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.convert.YzDataConvert;
import com.yz.redis.RedisService;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

/**
 * 
 * @author lingdian
 *
 */
@SuppressWarnings("rawtypes")
public class JedisSubRegister {

	private Logger logger = LoggerFactory.getLogger(JedisSubRegister.class);

	private Set<JedisPubSubHandler> subRegisters = null; // 注册的订阅消费列表

	private JedisPubSubHandler _pubSubhandler = null; // pub Sub 订阅处理的类

	private Map<String, List<JedisPubSubHandler>> _pubSubCache = new ConcurrentHashMap<>(); // 缓存channel
																							// 以及对应的处理handler列表

	private enum YzJedisSubRegisterHolder {
		_instance;

		private JedisSubRegister reg = new JedisSubRegister();

		public JedisSubRegister getReg() {
			return reg;
		}
	}

	/**
	 * 构造函数私有化
	 */
	private JedisSubRegister() {
		subRegisters = new HashSet<>();
		_pubSubhandler = new JedisPubSubHandler() {

			public void onMessage(String channel, String message) {
				List<JedisPubSubHandler> handlers = _lookupPubSub(channel);
				handlers.stream().forEach((v) -> _executeLocal(v, message));
				logger.info("onMessage.channel:{},message:{},handlers:{}", channel, message, handlers);
			}

			public void onPMessage(String pattern, String channel, String message) {
				onMessage(channel, message);
				logger.info("onPMessage:pattern.{},channel:{},message:{}", pattern, channel, message);
			}

			public void _executeLocal(JedisPubSubHandler handler, String message) {
				long start = System.currentTimeMillis();
				Object result = message;
				if (com.yz.util.StringUtil.isNotBlank(handler.getConvert()) && handler.getTarget() != null) {
					YzDataConvert convert = ApplicationContextUtil.getBean(handler.getConvert());
					result = convert.convert(String.valueOf(message), handler.getTarget());
				}
				handler.execute(result);
				logger.info("channel:{},costTime:{}", handler.getChannel(), (System.currentTimeMillis() - start));
			}

			@Override
			public void execute(Object obj) {
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public static JedisSubRegister getInstance() {
		return YzJedisSubRegisterHolder._instance.getReg();
	}

	/**
	 * 
	 * @desc 添加headler处理
	 * @param
	 */
	public void addSubRegister(JedisPubSubHandler handler) {
		if (handler != null) {
			subRegisters.add(handler);
		}
	}

	/**
	 * 
	 * @desc 将所有的注册频道进行注册
	 * @desc 启动Redis消费 订阅
	 */
	public void startPubSub() {
		if (subRegisters != null && !subRegisters.isEmpty()) {
			List<String> channel =  subRegisters.stream().map(v->v.getChannel()).collect(Collectors.toList());
			String[] channels = new String[channel.size()];
			channel.toArray(channels);
			Executors.newSingleThreadExecutor().execute(()->{	
		    RedisService.getRedisService().psubscribe(_pubSubhandler, channels);
			logger.info("psubscribe.channels:{}", StringUtil.join(channels));
			});
		}
	}

	/**
	 * 
	 * @desc 根据channel获取注册链条
	 * @param channel
	 */
	private List<JedisPubSubHandler> _lookupPubSub(String channel) {
		if (_pubSubCache.containsKey(channel)) {
			return _pubSubCache.get(channel);
		} 
		List<JedisPubSubHandler> set = subRegisters.stream().filter(v->RegUtils.isMatch(v.getChannel(),channel)).sorted().collect(Collectors.toList());
		this._pubSubCache.put(channel, set);
		return set;
	}

}
