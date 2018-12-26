package com.yz.service.order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.order.BadPaymentOrderMapper;
import com.yz.model.UserPaySuccessEvent;
import com.yz.model.order.BadPaymentInfo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

/**
 * 异常订单回调手动触发处理
 * 
 * @ClassName: BadPaymentOrderService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月25日
 *
 */
@Service
@Transactional
public class BadPaymentOrderService {
	private static Logger log = LoggerFactory.getLogger(BadPaymentOrderService.class);

	@Autowired
	private BadPaymentOrderMapper mapper;

	/**
	 * 根据支付单号获取数据
	 * 
	 * @param payNo
	 * @return
	 */
	public List<BadPaymentInfo> getBsOrderList(String payNo) {
		List<BadPaymentInfo> resultList = mapper.getAllData(payNo);
		return resultList;
	}
	/**
	 * 根据ids获取订单
	 * @param badPaymentIds
	 * @return
	 */
	public void getBadPaymentOrderByIds(String[] badPaymentIds) {
		List<BadPaymentInfo> resultList = mapper.getBadPaymentIds(Arrays.asList(badPaymentIds));
		log.info("异常订单回调重新发送到支付成功回调队列 resultList:{}",JsonUtil.object2String(resultList));
		//发送指令到redis
		resultList.stream().forEach(v->{
			//发送指令
			UserPaySuccessEvent event =new UserPaySuccessEvent();
			event.setAmount(v.getAmount());
			event.setCreateDate(new Date());
			event.setDealType(v.getDealType());
			event.setId(new Date().getTime());
			event.setOutOrderNo(v.getPayNo());
			event.setPayType(v.getPayType());
			event.setSerialNo(v.getTransNo());
			log.info("发送缴费成功指令 lpush {} {}", YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT, JsonUtil.object2String(event));
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT, JsonUtil.object2String(event));
			//增加次数
			mapper.addBadPaymentExecuteCount(v.getId(),String.valueOf(Integer.valueOf(v.getExecuteCount())+1),DateUtil.getCurrentDate(DateUtil.YYYYMMDDHHMMSS_SPLIT));
		});
	}
}
