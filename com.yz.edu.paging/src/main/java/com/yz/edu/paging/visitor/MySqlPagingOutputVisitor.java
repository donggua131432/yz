package com.yz.edu.paging.visitor;

 
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

/**
 * 
 * @desc 组装PagingSql
 * @author Administrator
 *
 */
public class MySqlPagingOutputVisitor extends MySqlOutputVisitor {

	public MySqlPagingOutputVisitor(Appendable appender) {
		super(appender);
	}

	@Override
	public boolean visit(MySqlSelectQueryBlock x) {
		if (x.getOrderBy() != null) {
			x.getOrderBy().setParent(x);
		} 

		print0(ucase ? "SELECT " : "select ");

		printSelectList(x.getSelectList());

		if (x.getInto() != null) {
			println();
			print0(ucase ? "INTO " : "into ");
			x.getInto().accept(this);
		}

		if (x.getFrom() != null) {
			println();
			print0(ucase ? "FROM " : "from ");
			x.getFrom().accept(this);
		}

		if (x.getWhere() != null) {
			println();
			print0(ucase ? "WHERE " : "where ");
			x.getWhere().setParent(x);
			x.getWhere().accept(this);
		}

		if (x.getGroupBy() != null) {
			println();
			x.getGroupBy().accept(this);
		}

		if (x.getOrderBy() != null) {
			println();
			x.getOrderBy().accept(this);
		}

		if (x.getLimit() != null) {
			println();
			x.getLimit().accept(this);
		}

		return false;
	}
}
