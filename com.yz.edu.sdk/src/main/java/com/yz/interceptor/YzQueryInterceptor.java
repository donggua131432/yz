package com.yz.interceptor;

import java.util.Collection; 
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import com.yz.cache.common.YzCacheBean;
import com.yz.cache.handler.RedisCacheHandler;
import com.yz.util.ApplicationContextUtil;

 
@Intercepts(@Signature(method = "query", type = StatementHandler.class, args = { java.sql.Statement.class,
		ResultHandler.class }))
/**
 * @desc 查询相关的缓存配置 
 * @author Administrator
 *
 */
public class YzQueryInterceptor extends BaseCacheInterceptor {

	
	@Override
	public Object processQuery(Invocation invocation, MappedStatement mappedStatement, YzCacheBean bean)
			throws Throwable {
		String cacheKey = getCacheKey(bean);
		RedisCacheHandler redisCacheHandler = ApplicationContextUtil.getBean(bean.getCacheHandler());
		Object obj = redisCacheHandler.getCache(bean.getRedisName(), cacheKey, bean.getReturnCls());
		if(obj==null)
		{
			obj = invocation.proceed();
			if(obj!=null&&!isEmpty(obj))
			{
				redisCacheHandler.setCache(bean.getRedisName(), cacheKey, obj,bean.getExpire());
			} 
		}
		return obj;
	}
	
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	private boolean isEmpty(Object result )
	{
		if(result!=null)
		{
			if(result instanceof Collection)
			return ((Collection)result).isEmpty();
		}
		return true;
	}
}
