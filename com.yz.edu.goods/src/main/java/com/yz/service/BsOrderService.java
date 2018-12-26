package com.yz.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.common.PageHelper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.api.UsAddressApi;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.dao.BsOrderMapper;
import com.yz.dao.GsGoodsMapper;
import com.yz.exception.BusinessException;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.*;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.util.BigDecimalUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * @author lx
 * @date 2017年7月26日 上午11:43:06
 */
@Service
@Transactional
public class BsOrderService {

	private static final Logger log = LoggerFactory.getLogger(BsOrderService.class);
	@Autowired
	private BsOrderMapper bsOrderMapper;

	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Reference(version = "1.0")
	private UsAddressApi usAddressApi;
	
	public Object addNewBsOrderForActivity(BsOrderParamInfo paramInfo){
		
		//主订单
		BsOrderInfo orderInfo = new BsOrderInfo();
		orderInfo.setOrderType("1");
		int salesPrice = Integer.parseInt(paramInfo.getSalesPrice());
		int count = Integer.parseInt(paramInfo.getCount());
		orderInfo.setTransAmount(String.valueOf(salesPrice*count));
		orderInfo.setOrderStatus("1");
		orderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		orderInfo.setMobile(paramInfo.getMobile());
		orderInfo.setUnit(paramInfo.getUnit());
		orderInfo.setUserId(paramInfo.getUserId());
		orderInfo.setUserName(paramInfo.getUserName());
		bsOrderMapper.addNewBsOrder(orderInfo);
		
		
		BsSerialInfo serialInfo =new BsSerialInfo();
		serialInfo.setOrderNo(orderInfo.getOrderNo());
		serialInfo.setTransAmount(orderInfo.getTransAmount());
		bsOrderMapper.addNewBsSerial(serialInfo);
		
		BsSalesOrderInfo salesOrderInfo = new BsSalesOrderInfo();
		salesOrderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		salesOrderInfo.setCostPrice(paramInfo.getCostPrice());
		salesOrderInfo.setGoodsCount(paramInfo.getCount());
		salesOrderInfo.setGoodsId(paramInfo.getGoodsId());
		salesOrderInfo.setGoodsName(paramInfo.getGoodsName());
		salesOrderInfo.setOrderNo(orderInfo.getOrderNo());
		salesOrderInfo.setOriginalPrice(paramInfo.getOriginalPrice());
		salesOrderInfo.setSalesId(paramInfo.getSalesId());
		salesOrderInfo.setSalesName(paramInfo.getSalesName());
		salesOrderInfo.setSalesType(paramInfo.getSalesType());
		salesOrderInfo.setTransAmount(orderInfo.getTransAmount());
		salesOrderInfo.setUnitPrice(paramInfo.getSalesPrice());
		salesOrderInfo.setUnit(paramInfo.getUnit());
		salesOrderInfo.setUserId(paramInfo.getUserId());
		salesOrderInfo.setGoodsType(paramInfo.getGoodsType());
		salesOrderInfo.setGoodsImg(paramInfo.getGoodsImg());
		salesOrderInfo.setSubOrderStatus("1");
		
		bsOrderMapper.addNewBsSalesOrder(salesOrderInfo);
		
		if(StringUtil.hasValue(paramInfo.getSaId())){
			Map<String, String> addressMap = usAddressApi.getAddressDetailById(paramInfo.getSaId());
			if(null != addressMap && addressMap.size() >0){
				//收货地址信息
				BsLogistics logistics = new BsLogistics();
				logistics.setAddress(addressMap.get("address"));
				logistics.setUserName(addressMap.get("saName"));
				logistics.setProvince(addressMap.get("provinceCode"));
				logistics.setCity(addressMap.get("cityCode"));
				logistics.setDistrict(addressMap.get("districtCode"));
				logistics.setLogisticsName("顺丰快递");
				logistics.setMobile(addressMap.get("mobile"));
				logistics.setLogisticsStatus("1"); //代发货
				logistics.setOrderNo(orderInfo.getOrderNo());
				bsOrderMapper.insertBsLogistics(logistics);
			}else{
				throw new BusinessException("E200024");
			}
		}
		
		return salesOrderInfo.getSubOrderNo();
	}
	
    public Object addNewBsOrderForGoods(BsOrderParamInfo paramInfo){
		
		//主订单
		BsOrderInfo orderInfo = new BsOrderInfo();
		orderInfo.setOrderType("2");
		orderInfo.setTransAmount(BigDecimalUtil.multiply(paramInfo.getSalesPrice(),paramInfo.getCount()));
		orderInfo.setOrderStatus("1");
		
		bsOrderMapper.addNewBsOrder(orderInfo);
		
		
		BsSerialInfo serialInfo =new BsSerialInfo();
		serialInfo.setOrderNo(orderInfo.getOrderNo());
		serialInfo.setTransAmount(orderInfo.getTransAmount());
		bsOrderMapper.addNewBsSerial(serialInfo);
		
		BsGoodsOrder goodsOrder = new BsGoodsOrder();
		goodsOrder.setCostPrice(paramInfo.getCostPrice());
		goodsOrder.setGoodsCount(paramInfo.getCount());
		goodsOrder.setGoodsId(paramInfo.getGoodsId());
		goodsOrder.setGoodsName(paramInfo.getGoodsName());
		goodsOrder.setOrderNo(orderInfo.getOrderNo());
		goodsOrder.setOriginalPrice(paramInfo.getOriginalPrice());
		goodsOrder.setTransAmount(orderInfo.getTransAmount());
		goodsOrder.setUnitPrice(paramInfo.getSalesPrice());
		
		bsOrderMapper.addNewBsGoodsOrder(goodsOrder);
		
		return orderInfo.getOrderNo();
	}
	
	public Object getBsMyOrderInfo(Header header, Body body){
		
		int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
		int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 15);
		PageHelper.startPage(page, pageSize);
		List<Map<String, String>> list = bsOrderMapper.getBsMyOrderInfo(header.getUserId());
	
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	public Object getBsOrderDetailInfo(Header header, Body body){
		String orderNo = body.getString("orderNo");
		
		Map<String, String> map = bsOrderMapper.getBsOrderDetailInfo(orderNo);

		if (StringUtil.hasValue(map.get("jdOrderId")) && !map.get("orderStatus").equals("3")) {
			// 同步京东的信息
			String orderStatus = getJdOrderStatus(map.get("jdOrderId"), map.get("orderNo"));
			map.put("orderStatus", orderStatus);
		}
		if(null != map && map.size() >0){
			if (map.get("goodsType") != null && map.get("goodsType").equals("2")) {
				GsCourseGoods gsCourseGoods = gsGoodsMapper.getGsCourseGoods(map.get("goodsId"));
				if (gsCourseGoods != null) {
					map.put("courseType", gsCourseGoods.getCourseType());
					map.put("courseAddress", gsCourseGoods.getAddress());
					map.put("courseStartTime", gsCourseGoods.getStartTime());
					map.put("courseEdnTime", gsCourseGoods.getEndTime());
				}
			}
			else if (map.get("goodsType") != null && map.get("goodsType").equals("3")) {
				GsCourseGoods gsCourseGoods = gsGoodsMapper.getGsActivitiesGoods(map.get("goodsId"));
				if(gsCourseGoods != null){
					map.put("activityAddress", gsCourseGoods.getAddress());
					map.put("activityStartTime", gsCourseGoods.getStartTime());
					map.put("activityEdnTime", gsCourseGoods.getEndTime());
				}
			}else if(map.get("goodsType") != null && map.get("goodsType").equals("1")){
				//转换地址
				if(StringUtil.hasValue(map.get("jdOrderId"))){
					StringBuilder sb = new StringBuilder();
					if(StringUtil.hasValue(map.get("provinceName"))){
						sb.append(map.get("provinceName"));
					}
					if(StringUtil.hasValue(map.get("cityName"))){
						sb.append(map.get("cityName"));
					}
					if(StringUtil.hasValue(map.get("districtName"))){
						sb.append(map.get("districtName"));
					}if(StringUtil.hasValue(map.get("streetName"))){
						sb.append(map.get("streetName"));
					}
					sb.append(map.get("address"));
					map.put("address", sb.toString());
					
				}
			}
			
		}
		return map;
	}
	
	public Object insertBsActionMember(Header header, Body body){
		Object jsonStr = body.getValue("items");
		List<BsActionMember> list = new ArrayList<>();
		JSONArray jan = JSONArray.fromObject(jsonStr);
		if(null !=jan && jan.size() >0){
			for(int i=0;i<jan.size();i++){ 
				BsActionMember member = new BsActionMember();
				JSONObject jo = JSONObject.fromObject(jan.get(i));
				member.setName(jo.getString("name"));
				member.setMobile(jo.getString("phone"));
				member.setSubOrderNo(jo.getString("orderNo"));
				
				list.add(member);
			}
			bsOrderMapper.insertBsActionMember(list);
		}
		
		return null;
	}
	public Object completeOrderAddress(Header header, Body body){
		String saId = body.getString("saId");
		String orderNo = body.getString("orderNo");
		//收货地址信息
		BsLogistics logistics = null;
		if(StringUtil.hasValue(saId)){
			Map<String, String> addressMap = usAddressApi.getAddressDetailById(saId);
			if(null != addressMap && addressMap.size() >0){
				
				logistics = new BsLogistics();
				logistics.setAddress(addressMap.get("address"));
				logistics.setUserName(addressMap.get("saName"));
				logistics.setProvince(addressMap.get("provinceCode"));
				logistics.setCity(addressMap.get("cityCode"));
				logistics.setDistrict(addressMap.get("districtCode"));
				logistics.setLogisticsName("顺丰快递");
				logistics.setMobile(addressMap.get("mobile"));
				logistics.setLogisticsStatus("1"); //代发货
				logistics.setOrderNo(orderNo);
				bsOrderMapper.insertBsLogistics(logistics);
				
				return logistics.getLogisticsId();
			}else{
				throw new BusinessException("E200024");
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Object addJDNewBsOrder(Body body){
		
		//主订单
		BsOrderInfo orderInfo = new BsOrderInfo();
		orderInfo.setOrderType("1");
		int salesPrice = Integer.parseInt(body.getString("salesPrice"));
		int count = Integer.parseInt(body.getString("count"));
		orderInfo.setTransAmount(String.valueOf(salesPrice*count));
		orderInfo.setOrderStatus("6"); //下单即待收货
		orderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		orderInfo.setMobile(body.getString("mobile"));
		orderInfo.setUnit(body.getString("unit"));
		orderInfo.setUserId(body.getString("userId"));
		orderInfo.setUserName(body.getString("userName"));
		
		orderInfo.setJdOrderId(body.getString("jdOrderId"));
		orderInfo.setFreight(body.getString("freight"));
		orderInfo.setOrderPrice(body.getString("orderPrice"));
		orderInfo.setOrderNakedPrice(body.getString("orderNakedPrice"));
		orderInfo.setOrderTaxPrice(body.getString("orderTaxPrice"));
		log.info("order info......."+JsonUtil.object2String(orderInfo));
		bsOrderMapper.addNewBsOrder(orderInfo);
		
		
		BsSerialInfo serialInfo =new BsSerialInfo();
		serialInfo.setOrderNo(orderInfo.getOrderNo());
		serialInfo.setTransAmount(orderInfo.getTransAmount());
		bsOrderMapper.addNewBsSerial(serialInfo);
		
		List<Map<String, String>> skuMap = (List<Map<String, String>>)body.getValue("skus");
		if(null != skuMap){
			for(Map<String, String> map : skuMap){
			
				BsSalesOrderInfo salesOrderInfo = new BsSalesOrderInfo();
				salesOrderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
				salesOrderInfo.setCostPrice(body.getString("costPrice"));
				salesOrderInfo.setGoodsCount(body.getString("count"));
				salesOrderInfo.setGoodsId(body.getString("goodsId"));
				salesOrderInfo.setGoodsName(body.getString("goodsName"));
				salesOrderInfo.setOrderNo(orderInfo.getOrderNo());
				salesOrderInfo.setOriginalPrice(body.getString("originalPrice"));
				salesOrderInfo.setSalesId(body.getString("salesId"));
				salesOrderInfo.setSalesName(body.getString("salesName"));
				salesOrderInfo.setSalesType(body.getString("salesType"));
				salesOrderInfo.setTransAmount(orderInfo.getTransAmount());
				salesOrderInfo.setUnitPrice(body.getString("salesPrice"));
				salesOrderInfo.setUnit(body.getString("unit"));
				salesOrderInfo.setUserId(body.getString("userId"));
				salesOrderInfo.setGoodsType(body.getString("goodsType"));
				salesOrderInfo.setGoodsImg(body.getString("goodsImg"));
				salesOrderInfo.setSubOrderStatus("6"); //下单即待收货
				
				
				salesOrderInfo.setSkuId(String.valueOf(map.get("skuId")));
				salesOrderInfo.setCategory(String.valueOf(map.get("category")));
				salesOrderInfo.setNakedPrice(String.valueOf(map.get("nakedPrice")));
				salesOrderInfo.setName(String.valueOf(map.get("name")));
				salesOrderInfo.setNum(String.valueOf(map.get("num")));
				salesOrderInfo.setOid(String.valueOf(map.get("oid")));
				salesOrderInfo.setPrice(String.valueOf(map.get("price")));
				salesOrderInfo.setTax(String.valueOf(map.get("tax")));
				salesOrderInfo.setTaxPrice(String.valueOf(map.get("taxPrice")));
				salesOrderInfo.setType(String.valueOf(map.get("type")));
				
				bsOrderMapper.addNewBsSalesOrder(salesOrderInfo);
			}
		}
		
		if(StringUtil.hasValue(body.getString("saId"))){
			Map<String, String> addressMap = usAddressApi.getAddressDetailById(body.getString("saId"));
			if(null != addressMap && addressMap.size() >0){
				//收货地址信息
				BsLogistics logistics = new BsLogistics();
				logistics.setAddress(addressMap.get("address"));
				logistics.setUserName(addressMap.get("saName"));
				logistics.setProvince(addressMap.get("provinceCode"));
				logistics.setCity(addressMap.get("cityCode"));
				logistics.setDistrict(addressMap.get("districtCode"));
				logistics.setStreet(addressMap.get("streetCode"));
				logistics.setLogisticsName("京东自营快递");
				logistics.setMobile(addressMap.get("mobile"));
				logistics.setLogisticsStatus("0"); // 0 是新建  1 是妥投   2 是拒收
				logistics.setOrderNo(orderInfo.getOrderNo());
				logistics.setProvinceName(addressMap.get("provinceName"));
				logistics.setCityName(addressMap.get("cityName"));
				logistics.setDistrictName(addressMap.get("districtName"));
				logistics.setStreetName(addressMap.get("streetName"));
				logistics.setTransportNo(body.getString("jdOrderId"));
				bsOrderMapper.insertBsLogistics(logistics);
			}else{
				throw new BusinessException("E200024");
			}
		}
		
		return orderInfo.getJdOrderId();
	}
	/**
     * 更改jd订单状体
     * @param jdOrderId
     * @return
     */
    public String getJdOrderStatus(String jdOrderId,String orderNo){
    	String orderStatus = "1";
    	String url = "https://bizapi.jd.com/api/order/selectJdOrder";
		String token = RedisService.getRedisService().get("jdAccessToken");
		String data = "token=" + token + "&jdOrderId=" + jdOrderId + "&&queryExts=jdOrderState";
		String result = HttpUtil.sendPost(url, data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		JSONObject orderObject = JSONObject.fromObject(result);
		if (orderObject.has("success") && orderObject.getBoolean("success")) {
			JSONObject orderResult = (JSONObject) orderObject.get("result");
			if (orderResult != null) {
				// 物流状态 0 是新建 1 是妥投 2 是拒收
				if (orderResult.has("state") && orderResult.getString("state") != null) {
					String state = orderResult.getString("state");
					if (state.equalsIgnoreCase("1")) {
						orderStatus = "3";// 对应订单状态是已完成
					} else if (state.equalsIgnoreCase("2")) {
						orderStatus = "5";// 对应订单状态是已拒收
					} else {
						if (orderResult.has("jdOrderState")
								&& orderResult.getString("jdOrderState") != null) {
							String jdOrderState = orderResult.getString("jdOrderState");
							if (jdOrderState != null && Integer.parseInt(jdOrderState) <= 16) {
								orderStatus = "6";// 对应订单状态是待发货
							} else if (jdOrderState != null && Integer.parseInt(jdOrderState) == 19) {
								orderStatus = "3";// 对应订单状态是已完成
							} else {
								orderStatus = "2";// 对应订单状态是已发货
							}
						}
					}
				}
				//更新订单状体
				bsOrderMapper.updateJdOrderStatus(orderStatus, orderNo);
			}
		}
		return orderStatus;
    }
}
