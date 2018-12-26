package com.yz.edu.paging.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;

/**
 * 
 * @desc sql方言
 * @author Administrator
 *
 */
public interface SqlDialect {
	
	Logger logger =LoggerFactory.getLogger(SqlDialect.class);

	/**
	 * @desc 生成分页的sql
	 * @param select
	 * @param sql
	 * @return
	 */
	default public String createPagingSql(SQLSelectQuery selectQuery) {
		if (selectQuery instanceof MySqlSelectQueryBlock) // 未包含union
		{
			return this.createPagingSql((MySqlSelectQueryBlock) selectQuery);
		} else if (selectQuery instanceof MySqlUnionQuery) {
			return this.createPagingSql((MySqlUnionQuery) selectQuery);
		}
		logger.error("not support type!",selectQuery);
		return null;
	}

	/**
	 * @desc 生成count sql语句
	 * @param select
	 * @param sql
	 * @return
	 */
	default public String createCountSql(SQLSelectQuery selectQuery) {
		if (selectQuery instanceof MySqlSelectQueryBlock) // 未包含union
		{
			return this.createCountSql((MySqlSelectQueryBlock) selectQuery);
		} else if (selectQuery instanceof MySqlUnionQuery) {
			return this.createCountSql((MySqlUnionQuery) selectQuery);
		}
		logger.error("not support type!",selectQuery);
		return null;
	}

	/**
	 * @desc 生成分页的sql
	 * @param select
	 * @param sql
	 * @return
	 */
	public String createPagingSql(SQLSelectQueryBlock select);

	/**
	 * @desc 生成count sql语句
	 * @param select
	 * @param sql
	 * @return
	 */
	public String createCountSql(SQLSelectQueryBlock select);

	/**
	 * @desc 生成分页的sql
	 * @param select
	 * @param sql
	 * @return
	 */
	 public String createPagingSql(MySqlUnionQuery select);

	/**
	 * @desc 生成count sql语句
	 * @param select
	 * @param sql
	 * @return
	 */
     public String createCountSql(MySqlUnionQuery select);

}
