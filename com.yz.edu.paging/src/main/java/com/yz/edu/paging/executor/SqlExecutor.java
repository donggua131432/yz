package com.yz.edu.paging.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.collect.Lists;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.edu.paging.dialect.SqlDialect;
import com.yz.edu.paging.dialect.SqlDialectFactory;
import com.yz.edu.paging.dialect.SqlDialectFactory.SqlDialectType;
import com.yz.edu.paging.parameter.YzCountParameterHandler;
import com.yz.edu.paging.parameter.YzDefaultParameterHandler;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc sql执行器
 * @author Administrator
 *
 */
public class SqlExecutor {

	private static SqlDialectFactory factory = SqlDialectFactory.getInstance();

	private static Logger logger = LoggerFactory.getLogger(SqlExecutor.class);

	/**
	 * 
	 * @param mappedStatement
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Page<T> executeSql(Configuration conf, StatementHandler statementHandler,
			MappedStatement mappedStatement, ResultSetHandler resultSetHandler) {
		BoundSql sql = statementHandler.getBoundSql();
		String orgSql = sql.getSql();
		SQLSelectStatement statement = (SQLSelectStatement) convertSql(orgSql);
		SQLSelect select = statement.getSelect();
		Page page = PageHelper.getPage();
		Page data = new Page(page.getPageNum(), page.getPageSize(), page.isCount());
		data.setCountMapper(page.getCountMapper());

		SQLSelectQuery selectQuery = select.getQuery();
		SqlLocalExecutor executor = null;
		long cc = 0;
		if (selectQuery instanceof MySqlSelectQueryBlock) // 未包含union
		{
			MySqlSelectQueryBlock sel = (MySqlSelectQueryBlock) selectQuery;
			executor = new SqlLocalExecutor(sel) {
				@Override
				public void setLimit(SQLLimit limit) {
					sel.setLimit(limit);
				}

				@Override
				public SQLLimit getLimit() {
					return sel.getLimit();
				}
			};
			 cc = sel.getSelectList().stream().filter(v->(v.getExpr() instanceof SQLVariantRefExpr)).count();
		} else if (selectQuery instanceof MySqlUnionQuery) {
			MySqlUnionQuery sel = (MySqlUnionQuery) selectQuery;
			executor = new SqlLocalExecutor(sel) {
				@Override
				public void setLimit(SQLLimit limit) {
					sel.setLimit(limit);
				}

				@Override
				public SQLLimit getLimit() {
					return sel.getLimit();
				}
			};
		}
		executor.execute(data, conf, statementHandler, mappedStatement, resultSetHandler,cc);
		return data;
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private abstract class SqlLocalExecutor {

		SQLSelectQuery mysqlSelect;

		public SqlLocalExecutor(SQLSelectQuery selectQuery) {
			this.mysqlSelect = selectQuery;
		}

		public abstract void setLimit(SQLLimit limit);

		public abstract SQLLimit getLimit();

		@SuppressWarnings("unchecked")
		public void execute(Page data, Configuration conf, StatementHandler statementHandler,
				MappedStatement mappedStatement, ResultSetHandler resultSetHandler,long skipParam) {
			BoundSql sql = statementHandler.getBoundSql();
			String countSql = null, pagingSql = null;
			SqlDialect sqlDialect = factory.getSqlDialect(SqlDialectType.MYSQL);
			SQLLimit limit =  getLimit();
			if (limit == null) {
				if (data.isCount()) {
					String countMapper = data.getCountMapper();
					if (StringUtil.isNotBlank(countMapper)) {
						data.setTotal(_execCountMapper(data.getCountMapper(), conf, sql.getParameterObject()));
					} else {
						countSql = sqlDialect.createCountSql(mysqlSelect);
						data.setTotal(
								_execCountSql(conf.getEnvironment().getDataSource(), countSql, mappedStatement, sql,skipParam));
					}
				}
				limit = new SQLLimit();
				limit.setOffset(new SQLVariantRefExpr("?", false));
				limit.setRowCount(new SQLVariantRefExpr("?", false));
				setLimit(limit);
			}
			if (!data.isCount() || data.getTotal() > 0) {
				pagingSql = sqlDialect.createPagingSql(mysqlSelect);
				data.addAll(_execPagingSql(conf.getEnvironment().getDataSource(), resultSetHandler, pagingSql,
						mappedStatement, sql));
			}
			logger.info("countSql:{},pagingSql:{}", countSql, pagingSql);
		}

	}

	/**
	 * @desc 执行具体的分页查询
	 * @param dataSource
	 * @param resultSetHandler
	 * @param countSql
	 * @param mappedStatement
	 * @param sql
	 * @return
	 */
	private <E> List<E> _execPagingSql(DataSource dataSource, ResultSetHandler resultSetHandler, String countSql,
			MappedStatement mappedStatement, BoundSql sql) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(countSql);
			List<ParameterMapping> parameterMappings = Lists.newArrayList(sql.getParameterMappings());
			ParameterMapping.Builder pageNum = new ParameterMapping.Builder(mappedStatement.getConfiguration(),
					"offset", int.class).mode(ParameterMode.IN);
			ParameterMapping.Builder pageSize = new ParameterMapping.Builder(mappedStatement.getConfiguration(),
					"limit", int.class).mode(ParameterMode.IN);
			parameterMappings.add(pageNum.build());
			parameterMappings.add(pageSize.build());
			sql.setAdditionalParameter("offset", PageHelper.getPage().getStartRow());
			sql.setAdditionalParameter("limit", PageHelper.getPage().getPageSize());
			ParameterHandler parameterHandler = new YzDefaultParameterHandler(mappedStatement, sql.getParameterObject(),
					sql, parameterMappings);
			parameterHandler.setParameters(ps);
			ps.executeQuery();
			return resultSetHandler.handleResultSets(ps);
		} catch (Exception ex) {
			logger.error("_execCountSql.error:{}", ExceptionUtil.getStackTrace(ex));
			return Lists.newArrayList();
		} finally {
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	/**
	 * @desc 执行分页行数查询
	 * @param dataSource
	 * @param countSql
	 * @param mappedStatement
	 * @param sql
	 * @return
	 */
	private int _execCountSql(DataSource dataSource, String countSql, MappedStatement mappedStatement, BoundSql sql,long skipParam) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(countSql);
			ParameterHandler parameterHandler = new YzCountParameterHandler(mappedStatement, sql.getParameterObject(),
					sql,sql.getParameterMappings(),skipParam);
			parameterHandler.setParameters(ps);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (Exception ex) {
			logger.error("_execCountSql.error:{}", ExceptionUtil.getStackTrace(ex));
			return 0;
		} finally {
			logger.info("countMapper.info:{}", sql.getSql());
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	/**
	 * 
	 * @param mapperId
	 * @param conf
	 * @param parameterObject
	 * @return
	 */
	private int _execCountMapper(String mapperId, Configuration conf, Object parameterObject) {
		MappedStatement mappedStatement = conf.getMappedStatement(mapperId);
		BoundSql sql = mappedStatement.getBoundSql(parameterObject);
		return this._execCountSql(conf.getEnvironment().getDataSource(), sql.getSql(), mappedStatement, sql,0);
	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	private SQLStatement convertSql(String sql) {
		List<SQLStatement> sts = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
		return sts.stream().findFirst().get();
	}
}
