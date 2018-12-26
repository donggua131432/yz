package com.yz.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.order.BadPaymentInfo;
/**
 * 执行失败订单手动触发
* @ClassName: BadPaymentOrderMapper  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author zhanggh  
* @date 2018年5月25日  
*
 */
public interface BadPaymentOrderMapper {
	/**
	 * 获取所有异常订单数据
	 * @param payNo
	 * @return
	 */
	List<BadPaymentInfo> getAllData(@Param("payNo") String payNo);
	/**
	 * 根据指定id获取异常订单数据
	 * @param badPaymentIds
	 * @return
	 */
	List<BadPaymentInfo> getBadPaymentIds(@Param("badPaymentIds") List<String> badPaymentIds);
	
	void delBadPayment(@Param("id") String id);
	
	void addBadPaymentExecuteCount(@Param("id") String id,@Param("executeCount") String executeCount,@Param("lastExecuteDate") String lastExecuteDate);
}
