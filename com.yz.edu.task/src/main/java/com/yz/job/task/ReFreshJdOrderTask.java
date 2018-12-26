package com.yz.job.task;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GwConstants;
import com.yz.http.HttpUtil;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.model.BsOrderInfo;
import com.yz.job.service.BsOrderService;
import com.yz.task.YzTaskConstants;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 同步京东订单(每天晚上23点和中午12点同步一次数据)
 * 
 * @return
 */
@Component(value = "reFreshJdOrderTask")
@YzJob(taskDesc = YzTaskConstants.YZ_REFRESH_JDORDERTASK_DESC, cron = "0 00 12,23 * * ?", shardingTotalCount = 1)
public class ReFreshJdOrderTask extends AbstractSimpleTask {

	@Autowired
	private BsOrderService orderService;
	
	@Autowired
	private YzSysConfig yzSysConfig;

	@Override
	public void executeOther() {
		List<BsOrderInfo> resultList = orderService.querySynchronousJdOrderList();
		String jdAccessToken = getRedisService().get("jdAccessToken");
		
		//同步订单完成时间
		setJdOrderTime(jdAccessToken);
		sleep(500);
		// 主要同步的信息有价格，运费，状态。
		if (resultList != null && resultList.size() > 0) {
			List<BsOrderInfo> orderResult = resultList.stream()
					.map(v -> setJdOrderInfo(jdAccessToken, v.getJdOrderId(), v.getOrderNo())).filter(Objects::nonNull)
					.collect(Collectors.toList());
			if (orderResult.size() > 0) {
				orderService.updateOrderStatus(orderResult);
			}
			return;
		}
		
		
	}
	
	
	/**
	 * 同步订单完成时间
	 * @param token
	 */
	public void setJdOrderTime(String token) {
		String url="https://bizapi.jd.com/api/message/get";
		String   data="token="+token+"&type=5";
		String   result = HttpUtil.sendPost(url,  data,null); 
	    JSONObject resultObject = JSONObject.fromObject(result);
	    if(resultObject.has("success")&&resultObject.getBoolean("success")) {
	    	JSONArray orderjsonArray = JSONArray.fromObject(resultObject.getString("result")); 
	    	if(orderjsonArray!=null&&orderjsonArray.size()>0){  
	    		for (Object object : orderjsonArray) {
	    			JSONObject orderJson=(JSONObject) object;
	    			String id=orderJson.getString("id");
	    			String time=orderJson.getString("time");
	    			JSONObject orderIdJson = JSONObject.fromObject(orderJson.getString("result"));
	    			if(orderIdJson.has("orderId")) {
	    				String jdOrderId=orderIdJson.getString("orderId");
	    				orderService.updateOrderCompleteTime(jdOrderId, time);
	    			}
	    			sleep(200);
	    			
	    			String oss = yzSysConfig.getBucket();
	    			if(StringUtil.hasValue(oss)&&oss.equals("yzims")) {
	    				//删除推送信息只有在正测试环下操作
		    			delMessage(token,id);
	    			}
	    			
				}
	    	}
	    	
			
			
		}
		
	}
	
	public  void delMessage(String token,String id) {
    	String url="https://bizapi.jd.com/api/message/del ";
		String   data="token="+token+"&id="+id;
		HttpUtil.sendPost(url,  data , null);  
    }
	/**
	 * 
	 * @param token
	 * @param jdOrderId
	 * @param orderNo
	 * @return
	 */
	public BsOrderInfo setJdOrderInfo(String token, String jdOrderId, String orderNo) {
		BsOrderInfo order = null;
		String data = "token=" + token + "&jdOrderId=" + jdOrderId + "&queryExts=jdOrderState";
		String result = HttpUtil.sendPost(GwConstants.JD_SELECT_ORDER_URL, data ,null);

		JSONObject orderObject = JSONObject.fromObject(result);
		if (orderObject.has("success") && orderObject.getBoolean("success")) {
			JSONObject orderResult = (JSONObject) orderObject.get("result");
			if (orderResult != null) {
				order = new BsOrderInfo();
				// 订单状态 0 为取消订单 1 为有效 如果返回的订单状态为0则返回空
				if (orderResult.has("orderState") && orderResult.getString("orderState") != null
						&& orderResult.getString("orderState").equals("0")) {
					return null;
				}
				// 物流状态 0 是新建 1 是妥投 2 是拒收
				if (orderResult.has("state") && orderResult.getString("state") != null) {
					String state = orderResult.getString("state");
					if (state.equalsIgnoreCase("1")) {
						order.setOrderStatus("3");// 对应订单状态是已完成
					} else if (state.equalsIgnoreCase("2")) {
						order.setOrderStatus("5");// 对应订单状态是已拒收
					} else {
						if (orderResult.has("jdOrderState") && orderResult.getString("jdOrderState") != null) {
							String jdOrderState = orderResult.getString("jdOrderState");
							if (jdOrderState != null && Integer.parseInt(jdOrderState) <= 10) {
								order.setOrderStatus("1");// 对应订单状态是待发货
							} else if (jdOrderState != null && Integer.parseInt(jdOrderState) == 19) {
								order.setOrderStatus("3");// 对应订单状态是已完成
							} else {
								order.setOrderStatus("2");// 对应订单状态是已发货
							}
						}
					}
				}
				if (orderResult.has("orderPrice") && orderResult.getString("orderPrice") != null) {
					String orderPrice = orderResult.getString("orderPrice");// 订单总价格
					order.setOrderPrice(orderPrice);
				}

				if (orderResult.has("freight") && orderResult.getString("freight") != null) {
					String freight = orderResult.getString("freight");// 运费（合同有运费配置才会返回，否则不会返回该字段）
					order.setFreight(freight);
				}
				JSONArray skujsonArray = JSONArray.fromObject(orderResult.get("sku"));
				if (skujsonArray != null && skujsonArray.size() > 0) {
					JSONObject skuJson = (JSONObject) skujsonArray.get(0);

					String num = skuJson.getString("num");
					String price = skuJson.getString("price");// 商品价格
					String name = skuJson.getString("name");// 商品名称
					// String type=skuJson.getString("type");//商品类型
					order.setJdPrice(price);
					order.setGoodsName(name);
					order.setGoodsCount(num);
				}
				
				order.setJdOrderId(jdOrderId);
				order.setOrderNo(orderNo);
			}
		}
		
		return order;

	}
}
