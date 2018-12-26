package com.yz.dao.statistics;

import java.util.List;

import com.yz.model.statistics.SendBookStatInfo;
import com.yz.model.statistics.SendBookStatQuery;

/**
 * 订书统计
 * @author lx
 * @date 2017年11月23日 下午5:41:32
 */
public interface SendBookStatMapper
{
	public List<SendBookStatInfo> getSendBookStatByPage(SendBookStatQuery statQuery);
	
	public void okOrder(String statGroup);
}
