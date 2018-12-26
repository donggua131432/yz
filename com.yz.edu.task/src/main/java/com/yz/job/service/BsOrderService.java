package com.yz.job.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.job.dao.BsOrderMapper;
import com.yz.job.model.BsOrderInfo;
import com.yz.util.JsonUtil;

@Service(value="bsOrderService")
public class BsOrderService {
	
	private static Logger logger = LoggerFactory.getLogger(BsOrderService.class);
	
	@Autowired
	private BsOrderMapper bsOrderMapper;

	/**
	 * 
	 * @desc 查询需要同步的京东订单
	 * 
	 * @return
	 */
    public List<BsOrderInfo> querySynchronousJdOrderList()
    {
    	logger.info("BsOrderService.querySynchronousJdOrderList.invoke");
    	return bsOrderMapper.querySynchronousJdOrderList();
    }
    
    public void updateOrderStatus(List<BsOrderInfo> resultList)
    {
    	logger.info("updateOrderStatus:{}.invoke",JsonUtil.object2String(resultList));
    	bsOrderMapper.updateOrderStatus(resultList);
    }
    
    public void updateOrderCompleteTime(String jdOrderId,String orderTime) {
    	logger.info("updateOrderCompletTime:{}{}.invoke",jdOrderId,orderTime);
    	bsOrderMapper.updateOrderCompletTime(jdOrderId, orderTime);
    }

}
