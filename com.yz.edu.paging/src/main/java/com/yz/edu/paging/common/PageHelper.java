package com.yz.edu.paging.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.edu.paging.bean.Page;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc pageHelper 工具类
 * @author Administrator
 *
 */
public class PageHelper {

	private static Logger logger = LoggerFactory.getLogger(PageHelper.class);

	private static final ThreadLocal<Page> page = ThreadLocal.withInitial(Page::new);

	/***
	 * @desc 设置分页参数
	 * @param offset
	 *            起始页码
	 * @param limit
	 *            每页的数量
	 * @return
	 */
	public static <E> Page<E> offsetPage(int offset, int limit) {
		return offsetPage(offset, limit, true);
	}

	/***
	 * @desc 设置分页参数
	 * @param offset
	 *            起始页码
	 * @param limit
	 *            每页的数量
	 * @param count
	 *            是否统计行数
	 * @return
	 */
	public static <E> Page<E> offsetPage(int offset, int limit, boolean count) {
		Page<E> pageBean = page.get();
		pageBean.setPageSize(limit);
		pageBean.setPageNum(offset / limit);
		pageBean.setStartRow(offset);
		pageBean.setEndRow(offset + limit);
		pageBean.setCount(count);
		logger.info("offsetPage.pageBean:{}", JsonUtil.object2String(pageBean));
		return pageBean;
	}

	/**
	 * 
	 * @param pageNum
	 * @param limit
	 * @return
	 */
	public static <E> Page<E> startPage(int pageNum, int limit) {
		return startPage(pageNum, limit, true);
	}

	/**
	 * 
	 * @param pageNum
	 *            页码
	 * @param limit
	 *            每页数
	 * @param count
	 *            是否统计行数
	 * @return
	 */
	public static <E> Page<E> startPage(int pageNum, int limit, boolean count) {
		Page<E> pageBean = page.get();
		pageBean.setPageSize(limit);
		pageBean.setPageNum(pageNum);
		pageBean.setStartRow(Math.max(pageNum - 1, 0) * limit);
		pageBean.setEndRow(pageNum * limit);
		pageBean.setCount(count);
		logger.info("startPage.pageBean:{}", JsonUtil.object2String(pageBean));
		return pageBean;
	}

	/**
	 * 
	 * @desc 当前查询是否启用了分页查询
	 * @return
	 *
	 */
	public static boolean isPageQuery() {
		Page data = page.get();
		return (data.getPageSize() > 0 && StringUtil.isBlank(data.getMajorMapper()));
	}

	/**
	 * 
	 * @desc 获取当前线程的分页配置信息
	 * @return
	 */
	public static <E> Page<E> getPage() {
		return page.get();
	}

	/**
	 * 
	 * @desc 清空线程局部变量
	 * @return
	 *
	 */
	public static void clear() {
		page.remove();
	}
}
