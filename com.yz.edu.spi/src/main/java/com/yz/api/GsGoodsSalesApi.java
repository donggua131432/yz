package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface GsGoodsSalesApi {
	/**
	 * 智米中心-首页 活动列表
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="goodsList",methodRemark="商品列表")
    Object findGsGoodsSalesInfo(Header header, Body body) throws IRpcException;
	/**
	 * 获取活动详细
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="goodsDetail",methodRemark="活动详情",needLogin = true)
	Object getGsGoodsSalesDetailInfo(Header header, Body body) throws IRpcException;

	/**
	 * 活动的评论
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="addComment",methodRemark="活动评论",needLogin = true)
	void insertGoodsComment(Header header, Body body) throws IRpcException;

	/**
	 * 活动开始前提醒
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="addSalesNotify",methodRemark="添加活动提醒",needLogin = true)
	void addNewSalesNotify(Header header, Body body) throws IRpcException;
	
	/**
	 * 参与竞拍
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="addGsAuctionPart",methodRemark="参与竞拍",needLogin = true)
	void addGsAuctionPart(Header header, Body body) throws IRpcException;

	/**
	 * 参与抽奖
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="addGsLotteryPart",methodRemark="参与抽奖",needLogin = true)
	void addGsLotteryPart(Header header, Body body) throws IRpcException;

	/**
	 * 兑换
	 * @param header
	 * @param body
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="addGsExchangePart",methodRemark="兑换商品",needLogin = true)
	Object addGsExchangePart(Header header, Body body) throws IRpcException;

	/**
	 * 确定兑换商品(新增的接口针对兑换京东对接的商品)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="gs",methodName="confirmExchangeGoods",methodRemark="确认兑换京东商品",needLogin = true)
	public Object confirmExchangeGoods(Header header,Body body) throws IRpcException;
}
