package com.yz.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

import org.apache.commons.lang.ClassUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.cache.common.CacheContent;
import com.yz.cache.common.YzCache;
import com.yz.cache.common.YzCacheBean;
import com.yz.cache.param.CacheParamWrapper;
import com.yz.cache.rule.RedisCacheRule;
import com.yz.cache.rule.RedisCacheRuleFactory;
import com.yz.util.ReflectionUtils;
import com.yz.util.StringUtil;
 

/**
 * @desc mybatis 缓存统一拦截器
 * @author Administrator
 *
 */
public abstract class BaseCacheInterceptor implements Interceptor {

	public RedisCacheRuleFactory factory = RedisCacheRuleFactory.getInstance();

	private Logger logger = LoggerFactory.getLogger(BaseCacheInterceptor.class);

	// key mappingId ,value RedisCache 配置
	private static Map<String, YzCacheBean> cacheMap = new ConcurrentHashMap<>();

	private Map<String, String> props = new HashMap<>();


	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MappedStatement mappedStatement = null;
		if ((statementHandler instanceof RoutingStatementHandler)) {
			StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
			mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
		} else {
			MetaObject metaStatementHandler = this.getMetaObject(statementHandler);
			mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		}
		YzCacheBean bean = getCache(mappedStatement);
		if (bean == null || !bean.isUseCache())// 未开启缓存配置
		{
			logger.info("{} not enable cache!", mappedStatement.getId());
			return invocation.proceed();
		}
		CacheContent.getInstance().setBoundSql(statementHandler.getBoundSql());
		CacheContent.getInstance().setCacheBean(bean);
		try {
			return process(invocation, mappedStatement, bean);
		} finally {
			CacheContent.getInstance().clear();
		} 
	}
 

	/**
	 * 
	 * @param statementHandler
	 * @return
	 */
	protected MetaObject getMetaObject(StatementHandler statementHandler) {
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
		// 可以分离出最原始的的目标类)
		while (metaStatementHandler.hasGetter("h") || metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("h");
			if (object != null) {
				metaStatementHandler = SystemMetaObject.forObject(object);
			}
			object = metaStatementHandler.getValue("target");
			if (object != null) {
				metaStatementHandler = SystemMetaObject.forObject(object);
			}
		}
		return metaStatementHandler;
	}

	/**
	 * 
	 * @param invocation
	 * @param mappedStatement
	 * @param bean
	 * @return
	 */
	private Object process(Invocation invocation, MappedStatement mappedStatement, YzCacheBean bean) throws Throwable {
		SqlCommandType type = mappedStatement.getSqlCommandType();
		if (type == SqlCommandType.SELECT) {
			return processQuery(invocation, mappedStatement, bean);
		}
		 //增 删 改 启用缓存策略 
		if (type == SqlCommandType.UPDATE || type == SqlCommandType.DELETE||type == SqlCommandType.INSERT)
		{
			return processUpdate(invocation, mappedStatement, bean);
		} 
		logger.error("not support type:{}", type);
		return null;
	}

	/**
	 * 
	 * @param invocation
	 * @param mappedStatement
	 * @param bean
	 * @return
	 */
	public Object processQuery(Invocation invocation, MappedStatement mappedStatement, YzCacheBean bean)
			throws Throwable {
		return null;
	}

	/**
	 * 
	 * @param invocation
	 * @param mappedStatement
	 * @param bean
	 * @return
	 */
	public Object processUpdate(Invocation invocation, MappedStatement mappedStatement, YzCacheBean bean)
			throws Throwable {
		return null;
	}

	/**
	 * 
	 * 
	 * @desc 将method配置 进行缓存
	 * @param mappedStatement
	 */
	private YzCacheBean getCache(MappedStatement mappedStatement) {
		String mapperId = mappedStatement.getId();
		YzCacheBean bean = cacheMap.get(mapperId);
		if (bean != null) {
			return bean;
		}
		if (StringUtil.isNotBlank(mapperId)) {
			int index = StringUtil.lastIndexOf(mapperId, ".");
			String cls = StringUtil.substring(mapperId, 0, index);
			String method = StringUtil.substring(mapperId, index + 1);
			try {
				Method[] ms = ClassUtils.getClass(cls).getDeclaredMethods();
				if (ms != null && ms.length > 0) {
					for (Method m : ms) {
						if (StringUtil.equalsIgnoreCase(m.getName(), method)) {
							return setCacheBean(mapperId, m.getReturnType(), m.getAnnotation(YzCache.class));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return setCacheBean(mapperId, null, null);
		}
		return null;
	}

	/**
	 * @desc 根据 method维度设置 CacheBean
	 * @param mapperId
	 *            mybatis mapperId
	 * @param cls
	 *            返回类型
	 * @param cache
	 *            源注释配置
	 */
	private YzCacheBean setCacheBean(String mapperId, Class<?> cls, YzCache cache) {
		if (cache == null) {
			YzCacheBean info = new YzCacheBean();
			cacheMap.put(mapperId, info);
			return info;
		}
		YzCacheBean cacheBean = new YzCacheBean();
		cacheBean.setCachePrefix(cache.cachePrefix());
		cacheBean.setExpire(cache.expire());
		cacheBean.setCacheHandler(cache.cacheHandler());
		cacheBean.setCacheKey(cache.cacheKey());
		cacheBean.setCacheRule(cache.cacheRule());
		cacheBean.setRedisName(cache.redisName());
		cacheBean.setReturnCls(cls);
		cacheBean.setUseCache(cache.useCache());
		cacheBean.setMapKey(StringUtil.substringAfterLast(mapperId, "."));
        cacheBean.setCacheRelation(cache.cacheRelation());
        cacheBean.setSync(cache.sync());
		cacheMap.put(mapperId, cacheBean);
		return cacheBean;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		if (properties != null) {
			Iterator<Entry<Object, Object>> iter = properties.entrySet().iterator();
			Entry<Object, Object> entry = null;
			while (iter.hasNext()) {
				entry = iter.next();
				props.put(StringUtil.obj2String(entry.getKey()), StringUtil.obj2String(entry.getValue()));
			}
			logger.info("properties:{}", properties);
		}
	}

	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	public String getProp(String key, String def) {
		if (this.props.containsKey(key)) {
			return this.props.get(key);
		}
		return def;
	}

	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	public Object getProp(String key, Object def) {
		if (this.props.containsKey(key)) {
			return this.props.get(key);
		}
		return def;
	}

	/**
	 * 
	 * @param cacheRule
	 * @return
	 */
	private RedisCacheRule getCacheRule(String cacheRule) {
		return factory.getRedisCacheRule(cacheRule);
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	protected String getCacheKey(YzCacheBean bean) {
		RedisCacheRule rule = this.getCacheRule(bean.getCacheRule());
		Map<String,Object> paramMap = CacheParamWrapper.wrapMap(CacheContent.getInstance().getBoundSql().getParameterObject());
		String key = rule.getCacheName(bean.getCacheKey(), paramMap);
		//bean.setCacheKey(key);
		logger.info("bean.mapperId:{},key:{}", bean.getMapKey(), key);
		return key;
	}
}
