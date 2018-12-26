package com.yz.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.yz.redis.YzRedisBean;
import com.yz.redis.YzRedisInfo;
import com.yz.serializ.FstSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 14400)
public class BmsSessionConfig {

	private FstSerializer fstSerializer = FstSerializer.getInstance();

	@Bean(name = "yzRedisBean")
	public YzRedisBean getRedisBean() {
		return new YzRedisBean();
	}

	@SuppressWarnings("rawtypes")
	@Bean(name = "redisTemplate")
	public RedisTemplate<Object, Object> initRedisTemplate(@Autowired RedisConnectionFactory factory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.setDefaultSerializer(new RedisSerializer<Object>() {

			@Override
			public byte[] serialize(Object t) throws SerializationException {
				return fstSerializer.serialize(t);
			}

			@Override
			public Object deserialize(byte[] bytes) throws SerializationException {
				return fstSerializer.deserialize(bytes, Object.class);
			}
		});
		return redisTemplate;
	}

	@Bean(name = "redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory(@Autowired YzRedisBean bean) {
		JedisConnectionFactory factory = new JedisConnectionFactory(initGenericObjectPoolConfig(bean));
		factory.setDatabase(0);
		YzRedisInfo redisInfo = bean.getRedisInfos().stream().filter(v -> v.isDefault()).findFirst().get();
		factory.setHostName(redisInfo.getHost());
		factory.setPassword(redisInfo.getAuthKey());
		factory.setPort(redisInfo.getPort());
		factory.setUsePool(true);
		return factory;
	}

	private JedisPoolConfig initGenericObjectPoolConfig(YzRedisBean config) {
		JedisPoolConfig conf = new JedisPoolConfig();
		conf.setLifo(true);
		conf.setMaxIdle(config.getMaxIdle());
		conf.setMinIdle(config.getMinIdle());
		conf.setMaxTotal(config.getMaxActive());
		conf.setBlockWhenExhausted(false);
		conf.setTestOnBorrow(config.isTestOnBorrow());
		conf.setTestOnReturn(config.isTestOnReturn());
		conf.setTestOnCreate(true);
		conf.setMaxWaitMillis(config.getMaxWait());
		return conf;
	}
}
