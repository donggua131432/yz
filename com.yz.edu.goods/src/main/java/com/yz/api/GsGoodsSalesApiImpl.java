package com.yz.api;


import com.alibaba.dubbo.config.annotation.Service;
import com.yz.constants.GlobalConstants;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.SessionInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.service.ZmcGoodsSalesService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version="1.0",timeout=30000, retries=0)
public class GsGoodsSalesApiImpl implements GsGoodsSalesApi {

	@Autowired
	private ZmcGoodsSalesService zmcGoodsSalesService;
	
	@Override
	public Object findGsGoodsSalesInfo(Header header,Body body) throws BusinessException {
		int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
		int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 15);
		return zmcGoodsSalesService.queryGoodsSalesByPage(page,pageSize,body);
	}

	@Override
	public Object getGsGoodsSalesDetailInfo(Header header, Body body) throws BusinessException {
		return zmcGoodsSalesService.getGsGoodsSalesDetailInfo(header,body);
	}
	
	@Override
	public void insertGoodsComment(Header header, Body body) throws BusinessException {
		zmcGoodsSalesService.insertGoodsComment(header,body);
	}

	@Override
	public void addNewSalesNotify(Header header, Body body) throws BusinessException {
		zmcGoodsSalesService.addNewSalesNotify(header,body);
	}

	@Override
	public void addGsAuctionPart(Header header, Body body) throws BusinessException {
		zmcGoodsSalesService.addGsAuctionPart(header,body);
	}

	@Override
	public void addGsLotteryPart(Header header, Body body) throws BusinessException {
		zmcGoodsSalesService.addGsLotteryPart(header,body);
	}

	@Override
	public Object addGsExchangePart(Header header, Body body) throws BusinessException {
		return zmcGoodsSalesService.addGsExchangePart(header,body);
	}

	@Override
	public Object confirmExchangeGoods(Header header, Body body) throws IRpcException {
		return zmcGoodsSalesService.confirmExchangeGoods(header,body);
	}
	
}
