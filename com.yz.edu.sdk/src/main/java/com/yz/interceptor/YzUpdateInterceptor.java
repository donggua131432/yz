package com.yz.interceptor;

import java.sql.Statement;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import com.yz.cache.common.CacheContent;
import com.yz.cache.common.YzCacheBean;
import com.yz.cache.task.CacheTask;
import com.yz.cache.task.SyncCacheCmd;
 

@Intercepts({ @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
/**
 * 
 * @desc mhs 更新缓存配置 
 * @author Administrator
 *
 */
public class YzUpdateInterceptor extends BaseCacheInterceptor
{
	
	/**
	 * 执行更新惭怍
	 * @param invocation
	 * @param mappedStatement
	 * @param bean
	 * @return
	 */
	public Object processUpdate(Invocation invocation, MappedStatement mappedStatement, YzCacheBean bean) throws Throwable 
	{
		Object obj = invocation.proceed();
		setUpdateCache(obj,mappedStatement,bean);
		return obj;
	}
	
    /**
     * 
     * @param result
     * @param mappedStatement
     * @param bean
     */
	private void setUpdateCache(Object result, MappedStatement mappedStatement, YzCacheBean bean)
	{
		//单条更新记录 返回单条结果
		String resultStr =String.valueOf(result);
		if(NumberUtils.isNumber(String.valueOf(resultStr)))
		{
			boolean succ = NumberUtils.toInt(resultStr)>0; //表示更新成功???
			if(succ)
			{  
				BoundSql sql = CacheContent.getInstance().getBoundSql();
				String key = getCacheKey(bean); //获取当前更新的key
				SyncCacheCmd info = new SyncCacheCmd(key, bean);
				info.setCacheHandler(bean.getCacheHandler());
				info.setParam(sql.getParameterObject());
				if(bean.isSync())
				{
					CacheTask.getInstance().syncCache(info);
				}
				else
				{
					CacheTask.getInstance().addCacheTask(info);
				}
			}
		}
	} 
}
