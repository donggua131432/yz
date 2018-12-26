package com.yz.api;


import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BsOrderService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version="1.0",timeout=30000, retries=0)
public class BsOrderApiImpl implements BsOrderApi {
	
	@Autowired
	private BsOrderService bsOrderService;

	@Override
	public Object getBsMyOrderInfo(Header header, Body body) throws IRpcException{
		return bsOrderService.getBsMyOrderInfo(header,body);
	}

	@Override
	public Object getBsOrderDetailInfo(Header header, Body body) throws IRpcException{
		return bsOrderService.getBsOrderDetailInfo(header,body);
	}

	@Override
	public Object insertBsActionMember(Header header, Body body) throws IRpcException{
		return bsOrderService.insertBsActionMember(header, body);
	}

	@Override
	public Object completeOrderAddress(Header header, Body body) throws IRpcException{
		return bsOrderService.completeOrderAddress(header,body);
	}

}
