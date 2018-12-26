package com.yz.edu.paging.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 * @author Administrator
 *
 */
public class SqlDialectFactory {

	private static Logger logger = LoggerFactory.getLogger(SqlDialectFactory.class);

	private static class SqlDialectFactoryHolder {
		private static SqlDialectFactory _instance = new SqlDialectFactory();
	}

	private SqlDialectFactory() {
	}

	/**
	 * 
	 * @return
	 */
	public static SqlDialectFactory getInstance() {
		return SqlDialectFactoryHolder._instance;
	}

	/**
	 * @desc
	 * @param type
	 * @return
	 */
	public SqlDialect getSqlDialect(SqlDialectType type) {
		switch (type) {
		case MYSQL:
			return new MysqlSqlDialect();
		default:
			logger.error("not support {}.SqlDialect", type);
			break;
		}
		return null;
	}

	/**
	 * @desc 数据库类型 
	 * @author Administrator
	 *
	 */
	public static enum SqlDialectType {
		MYSQL, SQLSERVER, DB2, ORACLE
	}
	
	/**
	 * @desc 数据库操作类型 
	 * @param count 统计
	 * @param paging 分页查询
	 * @author Administrator
	 *
	 */
	public static enum SqlDialectOperType {
		COUNT,PAGING
	}
}
