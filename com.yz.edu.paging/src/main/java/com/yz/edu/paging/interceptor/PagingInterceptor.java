package com.yz.edu.paging.interceptor;

import java.util.Properties;

import org.apache.commons.lang.time.StopWatch;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.edu.paging.common.PageHelper;
import com.yz.edu.paging.executor.SqlExecutor;
import com.yz.util.ReflectionUtils;

@Intercepts(@Signature(method = "query", type = StatementHandler.class, args = { java.sql.Statement.class,
		ResultHandler.class }))
public class PagingInterceptor implements Interceptor {

	private Logger logger = LoggerFactory.getLogger(PagingInterceptor.class);

	// sql执行器
	private static SqlExecutor sqlExecutor = new SqlExecutor();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (!PageHelper.isPageQuery()) {
			logger.info("not enable paging query");
			return invocation.proceed();
		}
		Object obj = null;
		StopWatch watch = new StopWatch();
		try {
			watch.start();
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			MappedStatement mappedStatement = null;
			ResultSetHandler resultSetHandler = null;
			Configuration conf = null;
			if ((statementHandler instanceof RoutingStatementHandler)) {
				StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler,
						"delegate");
				mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
				resultSetHandler = (ResultSetHandler) ReflectionUtils.getFieldValue(delegate, "resultSetHandler");
				conf = (Configuration) ReflectionUtils.getFieldValue(delegate, "configuration");
			} else {
				MetaObject metaStatementHandler = this.getMetaObject(statementHandler);
				mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
				resultSetHandler = (ResultSetHandler) metaStatementHandler.getValue("delegate.resultSetHandler");
			}
			PageHelper.getPage().setMajorMapper(mappedStatement.getId());
			SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
			if (sqlCommandType == SqlCommandType.SELECT) {
				obj = sqlExecutor.executeSql(conf, statementHandler, mappedStatement, resultSetHandler);
			} else {
				obj = invocation.proceed();
			}
			logger.info("mapperId:{},costTime:{}", mappedStatement.getId(), watch.getTime());
		} finally {
			PageHelper.clear();
		}
		return obj;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

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

}
