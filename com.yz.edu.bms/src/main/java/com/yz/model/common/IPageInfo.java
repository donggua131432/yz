package com.yz.model.common;

import java.util.ArrayList;
import java.util.List;

import com.yz.edu.paging.bean.Page;

public class IPageInfo<T> {
	/** 查询结果 */
	private List<T> data = new ArrayList<T>(); 
	
	/** 总数量 */
	private long recordsTotal;
	/** 当前显示数量*/
	private long recordsFiltered;
	
	public IPageInfo(){}
	
	public IPageInfo(Page<T> page) {
		this.recordsFiltered = this.recordsTotal = page.getTotal();
		this.data = page.getResult();
	}
	
	public IPageInfo(List<T> data, long total) {
		this.data = data;
		this.recordsFiltered = this.recordsTotal = total;
	}
	
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> aaData) {
		this.data = aaData;
	}
	
	public void setTotal(long total) {
		this.recordsFiltered = this.recordsTotal = total;
	}
	
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}
