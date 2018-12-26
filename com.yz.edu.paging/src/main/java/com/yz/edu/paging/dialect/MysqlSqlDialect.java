package com.yz.edu.paging.dialect;
  
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.yz.edu.paging.visitor.MySqlCountOutputVisitor;
import com.yz.edu.paging.visitor.MySqlPagingOutputVisitor; 

/**
 * 
 * 
 * @author Administrator
 *
 */
public class MysqlSqlDialect implements SqlDialect {


	@Override
	public String createPagingSql(SQLSelectQueryBlock select) {
		StringBuilder appender = new StringBuilder();
		MySqlPagingOutputVisitor out = new MySqlPagingOutputVisitor(appender);
		select.accept(out); 
		return appender.toString();
	}


	@Override
	public String createCountSql(SQLSelectQueryBlock select) {
		StringBuilder appender = new StringBuilder();
		MySqlCountOutputVisitor out = new MySqlCountOutputVisitor(appender);
		select.accept(out);
		return appender.toString();
	}


	@Override
	public String createPagingSql(MySqlUnionQuery select) {
		StringBuilder appender = new StringBuilder();
		MySqlPagingOutputVisitor out = new MySqlPagingOutputVisitor(appender);
		select.accept(out); 
		return appender.toString();
	}


	@Override
	public String createCountSql(MySqlUnionQuery select) {
		StringBuilder appender = new StringBuilder();
		MySqlCountOutputVisitor out = new MySqlCountOutputVisitor(appender);
		select.accept(out);
		return appender.toString();
	} 
}
