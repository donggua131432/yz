package com.yz.edu.paging.bean;

import java.util.ArrayList;
import java.util.List;

import com.yz.util.StringUtil;

/**
 * 
 * 
 * @author Administrator
 *
 * @param <E>
 */
public class Page<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;

	/**
	 * 页码，从1开始
	 */
	private int pageNum;
	/**
	 * 页面大小
	 */
	private int pageSize;
	/**
	 * 起始行
	 */
	private int startRow;
	/**
	 * 末行
	 */
	private int endRow;
	/**
	 * 总数
	 */
	private long total;
	/**
	 * 总页数
	 */
	private int pages;
	/**
	 * 包含count查询
	 */
	private boolean count;
	/**
	 * count信号，3种情况，null的时候执行默认BoundSql,true的时候执行count，false执行分页
	 */
	private transient String countMapper;

	private transient String majorMapper;

	private boolean rmGroup = true; // 是否去除group操作

	public Page setMajorMapper(String majorMapper) {
		if (StringUtil.isBlank(this.majorMapper)) {
			this.majorMapper = majorMapper;
		}
		return this;
	}

	public String getMajorMapper() {
		return majorMapper;
	}

	public Page() {
		super();
	}

	public Page(int pageNum, int pageSize) {
		this(pageNum, pageSize, true);
	}

	public Page(int pageNum, int pageSize, boolean count) {
		super(0);
		if (pageNum == 1 && pageSize == Integer.MAX_VALUE) {
			pageSize = 0;
		}
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.count = count;
	}

	public Page setCountMapper(String countMapperId) {
		this.count = true;
		this.countMapper = countMapperId;
		return this;
	}

	public String getCountMapper() {
		return countMapper;
	}

	public List<E> getResult() {
		return this;
	}

	public int getPages() {
		return pages;
	}

	public Page<E> setPages(int pages) {
		this.pages = pages;
		return this;
	}

	public int getEndRow() {
		return endRow;
	}

	public Page<E> setEndRow(int endRow) {
		this.endRow = endRow;
		return this;
	}

	public int getPageNum() {
		return pageNum;
	}

	public Page<E> setPageNum(int pageNum) {
		// 分页合理化，针对不合理的页码自动处理
		this.pageNum = (pageNum <= 0) ? 1 : pageNum;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page<E> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getStartRow() {
		return startRow;
	}

	public Page<E> setStartRow(int startRow) {
		this.startRow = startRow;
		return this;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		if (total == -1) {
			pages = 1;
			return;
		}
		if (pageSize > 0) {
			pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
		} else {
			pages = 0;
		}
		// 分页合理化，针对不合理的页码自动处理
		if (pageNum > pages) {
			pageNum = pages;
		}
	}

	public boolean isCount() {
		return this.count;
	}

	public Page<E> setCount(boolean count) {
		this.count = count;
		return this;
	}

	/**
	 * 设置页码
	 *
	 * @param pageNum
	 * @return
	 */
	public Page<E> pageNum(int pageNum) {
		// 分页合理化，针对不合理的页码自动处理
		this.pageNum = (pageNum <= 0) ? 1 : pageNum;
		return this;
	}

	/**
	 * 设置页面大小
	 *
	 * @param pageSize
	 * @return
	 */
	public Page<E> pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * 是否执行count查询
	 *
	 * @param count
	 * @return
	 */
	public Page<E> count(Boolean count) {
		this.count = count;
		return this;
	}

	/**
	 * 转换为PageInfo
	 *
	 * @return
	 */
	public PageInfo<E> toPageInfo() {
		PageInfo<E> pageInfo = new PageInfo<E>(this);
		return pageInfo;
	}

	public void setRmGroup(boolean rmGroup) {
		this.rmGroup = rmGroup;
	}

	public boolean isRmGroup() {
		return rmGroup;
	}

	@Override
	public String toString() {
		return "Page{" + "count=" + count + ", pageNum=" + pageNum + ", pageSize=" + pageSize + ", startRow=" + startRow
				+ ", endRow=" + endRow + ", total=" + total + ", pages=" + pages + ", countSqlMapper=" + countMapper
				+ '}';
	}
}
