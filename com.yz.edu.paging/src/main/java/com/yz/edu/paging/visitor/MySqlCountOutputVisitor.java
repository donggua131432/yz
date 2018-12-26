package com.yz.edu.paging.visitor;

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.yz.edu.paging.common.PageHelper;
import com.yz.util.StringUtil; 

/**
 * 
 * @author Administrator
 *
 */
public class MySqlCountOutputVisitor extends MySqlOutputVisitor {

	public MySqlCountOutputVisitor(Appendable appender) {
		super(appender);
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public boolean visit(MySqlSelectQueryBlock x) {
		if (x.getParent() instanceof MySqlUnionQuery) {
			return this.visitUnionQuery(x);
		}
		if (x.getParent() instanceof SQLJoinTableSource) {
			return this.visitSubQuery(x);
		}
		if (x.getParent().getParent() instanceof SQLExistsExpr) {
			return this.visitSubQuery(x);
		} 
		boolean isGroup  = PageHelper.getPage().isRmGroup();
        if(!isGroup)
        {
        	return this.visitGroup(x);
        }
		return this.visitNonGroup(x);
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	private boolean visitGroup(MySqlSelectQueryBlock x)
	{ 
		print0("select  count(0) from ( select " );
		List<String>columns= x.getGroupBy().getItems().parallelStream().map(v-> 
			((SQLPropertyExpr)v).toString() ).collect(Collectors.toList());
		print0(StringUtil.join(columns, ","));
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
		print(" ) as temp");
		return false;
	
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	private boolean visitNonGroup(MySqlSelectQueryBlock x)
	{
		print0("select  count(0) ");
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
 
		
		return false;
	}

	@Override
	public boolean visit(SQLInSubQueryExpr x) {
		x.getExpr().accept(this);
		if (x.isNot()) {
			print0(ucase ? " NOT IN (" : " not in (");
		} else {
			print0(ucase ? " IN (" : " in (");
		}

		incrementIndent();
		visitSubQuery((MySqlSelectQueryBlock) x.getSubQuery().getQuery());
		decrementIndent();
		print(')');

		return false;
	}

	@Override
	public boolean visit(SQLSubqueryTableSource x) {
		print('(');
		incrementIndent();
		visitSubQuery((MySqlSelectQueryBlock) x.getSelect().getQuery());
		println();
		decrementIndent();
		print(')');

		if (x.getAlias() != null) {
			print(' ');
			print0(x.getAlias());
		}
  

		return false;
	}

	 

	 

	/**
	 * @desc 处理subQuery查询
	 * @param x
	 * @return
	 */
	private boolean visitSubQuery(MySqlSelectQueryBlock x) {
		return this.visitUnionQuery(x);
	}

	/**
	 * @desc 处理unionQuery查询
	 * @param x
	 * @return
	 */
	private boolean visitUnionQuery(MySqlSelectQueryBlock x) {
		return this.countWithCloumQuery(x);
	}

	/**
	 * 
	 * @desc 查询count带列查询 用于union，subQuery查询
	 * @param x
	 * @return
	 */
	private boolean countWithCloumQuery(MySqlSelectQueryBlock x) {
		print0("select ");
		printSelectList(x.getSelectList());
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
		if (!PageHelper.getPage().isRmGroup()&&x.getGroupBy() != null) {
			println();
			x.getGroupBy().accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(MySqlUnionQuery x) {
		if(x.getParent()!=null && !(x.getParent() instanceof SQLSelect))
		{
			print("( ");
			x.getLeft().accept(this);
			print(')');
			println();
			print0(ucase ? x.getOperator().name : x.getOperator().name_lcase);
			println();
			print('(');
			x.getRight().accept(this);
			print(") ");
		}
		else
		{
			print("select count(0) from ((");
			x.getLeft().accept(this);
			print(')');
			println();
			print0(ucase ? x.getOperator().name : x.getOperator().name_lcase);
			println();
			print('(');
			x.getRight().accept(this);
			print(") ) as temp");
		} 
		return false;
	}
}
