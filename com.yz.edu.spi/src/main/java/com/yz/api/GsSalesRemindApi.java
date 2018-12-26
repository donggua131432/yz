package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;

/**
 * 活动提醒
 * @author lx
 * @date 2017年8月4日 下午4:28:00
 */
public interface GsSalesRemindApi {
	
	/**
	 * 兑换/抽奖/竞拍活动商品开始前提醒
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	public boolean salesBeginRemind(Body body) throws IRpcException;
	
	/**
	 * 抽奖/竞拍活动商品延期
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	public boolean salesContinue(Body body) throws IRpcException;
	
	/**
	 * 抽奖/竞拍活动商品排期开始
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	public boolean salesStart(Body body) throws IRpcException;
	
}
