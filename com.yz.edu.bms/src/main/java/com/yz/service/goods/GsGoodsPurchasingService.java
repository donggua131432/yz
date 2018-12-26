package com.yz.service.goods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.goods.GsGoodsPurchasingMapper;
import com.yz.dao.order.BsOrderMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.goods.GsGoodsPurchasingInfo;
import com.yz.model.goods.GsGoodsPurchasingQuery;
import com.yz.model.order.BsLogisticsInfo;
import com.yz.model.order.BsOrderInfo;
import com.yz.model.order.BsSalesOrderInfo;
import com.yz.model.order.BsSerialInfo;
import com.yz.model.system.JDAddressVo;
import com.yz.redis.RedisService;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 采购列表
 * @author lx
 * @date 2018年5月15日 上午10:55:12
 */
@Service
public class GsGoodsPurchasingService {

	private static final Logger log = LoggerFactory.getLogger(GsGoodsPurchasingService.class);
	
	
	@Autowired
	private GsGoodsPurchasingMapper gsGoodsPurchasingMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig ;
	
	@Autowired
	private BsOrderMapper bsOrderMapper;
	/**
	 * 采购列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public IPageInfo<GsGoodsPurchasingInfo> getGsGoodsPurchasing(int start, int length,GsGoodsPurchasingQuery query){
		PageHelper.offsetPage(start, length);
		List<GsGoodsPurchasingInfo> purchasing = gsGoodsPurchasingMapper.getGsGoodsPurchasing(query);
		return new IPageInfo<GsGoodsPurchasingInfo>((Page<GsGoodsPurchasingInfo>)purchasing);
	}
	
	/**
	 * 获取京东省
	 */
	public Object getJDProvince(){
		List<JDAddressVo> list = new ArrayList<>();
		String provinceUrl ="https://bizapi.jd.com/api/area/getProvince";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken;
		String result = HttpUtil.sendPost(provinceUrl, paramData,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		if(null != result){
			JSONObject obj = JSONObject.fromObject(result);
			if(obj.getString("resultCode").equals("0000")){//请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");
				
				for(int i=0;i<spl.length;i++){
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"",""));
					vo.setCode(NumberUtils.toInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return list;
	}
	/**
	 * 获取京东的市
	 * @param pId
	 * @return
	 */
	public Object getJDCity(String pId){
		List<JDAddressVo> list = new ArrayList<>();
		String cityUrl ="https://bizapi.jd.com/api/area/getCity";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken+"&id="+pId;
		String result = HttpUtil.sendPost(cityUrl, paramData,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		if(null != result){
			JSONObject obj = JSONObject.fromObject(result);
			if(obj.getString("resultCode").equals("0000")){//请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");
				
				for(int i=0;i<spl.length;i++){
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"",""));
					vo.setCode(Integer.parseInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return list;
	}
	
	/**
	 * 京东的区
	 * @param pId
	 * @return
	 */
	public Object getJDCounty(String cId){
		List<JDAddressVo> list = new ArrayList<>();
		String countyUrl = "https://bizapi.jd.com/api/area/getCounty";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken+"&id="+cId;
		String result =HttpUtil.sendPost(countyUrl, paramData,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		if(null != result){
			JSONObject obj = JSONObject.fromObject(result);
			if(obj.getString("resultCode").equals("0000")){//请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");
				
				for(int i=0;i<spl.length;i++){
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"",""));
					vo.setCode(Integer.parseInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return list;
	}
	
	/**
	 * 京东的街道
	 * @param pId
	 * @return
	 */
	public Object getJDTown(String pId){
		List<JDAddressVo> list = new ArrayList<>();
		String townurl = "https://bizapi.jd.com/api/area/getTown";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken+"&id="+pId;
		String result = HttpUtil.sendPost(townurl , paramData,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		if(null != result){
			JSONObject obj = JSONObject.fromObject(result);
			if(obj.getString("resultCode").equals("0000")){//请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");
				
				for(int i=0;i<spl.length;i++){
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"",""));
					vo.setCode(Integer.parseInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return list;
	}
	/**
	 * 采购
	 * @param goodsInfo
	 */
	@SuppressWarnings("unchecked")
	public String buyGoods(GsGoodsPurchasingInfo goodsInfo) {
		BaseUser user = SessionUtil.getUser();
		
		//京东下单
		String exchangeCount = goodsInfo.getGoodsNum();
  		//验证
  		if(StringUtil.hasValue(goodsInfo.getGoodsSkuId())){ //京东商品
  			String pId = goodsInfo.getProvince(); //一级地址
  			String cId = goodsInfo.getCity(); //二级地址
  			String dId = goodsInfo.getDistrict(); //三级地址
  			//去京东验证库存
  			String stockUrl = "https://bizapi.jd.com/api/stock/getNewStockById";
  			StringBuilder sb = new StringBuilder();
  			sb.append("token="+RedisService.getRedisService().get("jdAccessToken"));
  			sb.append("&skuNums=[{skuId:"+goodsInfo.getGoodsSkuId()+",num:"+exchangeCount+"}]");
  			sb.append("&area="+pId+"_"+cId+"_"+dId);
  			log.debug("验证库存请求参数:========="+sb.toString());
  			String stockResult = HttpUtil.sendPost(stockUrl, sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
  			
  			if(null != stockResult){
  				JSONObject obj = JSONObject.fromObject(stockResult);
  				if(obj.getString("resultCode").equals("0000")){
  					JSONArray stockArray = JSONArray.fromObject(obj.getString("result"));
					/* 暂时只处理无货
					 * JD库存状态描述 
					 * 33 有货 现货-下单立即发货 
					 * 39 有货 在途-正在内部配货，预计2~6天到达本仓库 
					 * 40有货 可配货-下单后从有货仓库配货 
					 * 36,99 预订 
					 * 34 无货
					 */
  					if(stockArray.size()>0){
  						JSONObject stockObj = JSONObject.fromObject(stockArray.get(0));
  						log.debug("库存状态:========="+stockObj.getString("stockStateId"));
  	  					if(stockObj.getString("stockStateId").equals("34")){
  	  						//京东无货
  	  						throw new BusinessException("E200029");
  	  					}	
  					}
  					
  				}
  			}
  		}else{
  			//不可兑换
  			throw new BusinessException("E200028");
  		}
  		
		//京东下单
		String orderResult = jdSubmitOrder(goodsInfo);
		if(null != orderResult){
			JSONObject obj = JSONObject.fromObject(orderResult);
			if(obj.containsKey("resultCode")){
				if(obj.getString("resultCode").equals("0001")){
					log.debug("京东下单成功-----"+obj.getString("resultMessage"));
					//成功下单 解析京东返回的参数
					JSONObject resultOjb = JSONObject.fromObject(obj.getString("result"));
					String jdOrderId  = resultOjb.getString("jdOrderId");            //京东订单号
					String freight = resultOjb.getString("freight");                 //运费
					String orderPrice = resultOjb.getString("orderPrice");           //订单总价格		
					String orderNakedPrice = resultOjb.getString("orderNakedPrice"); //订单裸价		
					String orderTaxPrice = resultOjb.getString("orderTaxPrice");     //订单税额
					//解析里面的商品
					JSONArray skus = resultOjb.getJSONArray("sku");
					List<Map<String, String>> skuVos = new ArrayList<>();
					for(int i =0;i<skus.size();i++){
						JSONObject skuObj = JSONObject.fromObject(skus.get(i));
						Map<String, String> skuMap = new HashMap<>();
						skuMap.put("skuId", skuObj.getString("skuId"));
						skuMap.put("category", skuObj.getString("category"));
						skuMap.put("nakedPrice", skuObj.getString("nakedPrice"));
						skuMap.put("name", skuObj.getString("name"));
						skuMap.put("num", skuObj.getString("num"));
						skuMap.put("oid", skuObj.getString("oid"));
						skuMap.put("price", skuObj.getString("price"));
						skuMap.put("tax", skuObj.getString("tax"));
						skuMap.put("taxPrice", skuObj.getString("taxPrice"));
						skuMap.put("type", skuObj.getString("type"));
					
						skuVos.add(skuMap);
					}
				
					Body orderBody = new Body();
					//京东参数 start====
					orderBody.put("jdOrderId", jdOrderId);
					orderBody.put("freight", freight);
					orderBody.put("orderPrice", orderPrice);
					orderBody.put("orderNakedPrice", orderNakedPrice);
					orderBody.put("orderTaxPrice", orderTaxPrice);
					orderBody.put("skus", skus);
					//京东参数end =====
					
					orderBody.put("purchasing", goodsInfo);
			
				
					addJDNewBsOrder(orderBody);
					
					goodsInfo.setJdOrderNo(jdOrderId);
					goodsInfo.setIfSuccess("1");
					goodsInfo.setRemark("下单成功!");
				}else if(obj.getString("resultCode").equals("0008")){
					goodsInfo.setRemark(obj.getString("resultMessage"));
					goodsInfo.setIfSuccess("0");
					log.info("重复下单-----"+obj.getString("resultMessage"));
				}else{
					goodsInfo.setRemark(obj.getString("resultMessage"));
					goodsInfo.setIfSuccess("0");
					log.info("京东下单失败--------" + obj.getString("resultMessage"));
					//throw new BusinessException("E200031");
				}
			}
		}
		
		
		goodsInfo.setId(IDGenerator.generatorId());
		goodsInfo.setAnnexPath(uploadAnnex(goodsInfo.getId(), goodsInfo.getAnnexUrl()));
		goodsInfo.setOperUserName(user.getUsername());
		goodsInfo.setOperUserId(user.getUserId());
		goodsInfo.setTotalPrice(BigDecimalUtil.multiply(goodsInfo.getGoodsPrice(), goodsInfo.getGoodsNum()));
		goodsInfo.setEmail("1047806836@qq.com");
		
		gsGoodsPurchasingMapper.initGoodsPurchasing(goodsInfo);
		
		if(goodsInfo.getIfSuccess().equals("1")){
			return "success";
		}else{
			throw new BusinessException("E40012");
		}
		
	}
	/**
	 * 上传审批附件
	 * @return
	 */
	public String uploadAnnex(String goodsId,Object annexUrl){

		String realFilePath = null;
		//附件处理
		byte[] fileByteArray = null;
		boolean isUpdate = false;

		if (annexUrl != null && annexUrl instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) annexUrl;
			realFilePath = FileSrcUtil.createFileSrc(Type.GOODS, goodsId, file.getOriginalFilename());
			try {
				fileByteArray = file.getBytes();
				isUpdate = true;
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}
		}
		String bucket = yzSysConfig.getBucket();
		if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}
		return realFilePath;
	}
	/**
	 * 详细
	 * @param id
	 */
	public GsGoodsPurchasingInfo getPurchasingDetail(String id){
		return gsGoodsPurchasingMapper.getPurchasingDetail(id);
	}
	
	/**
	 * 京东下单
	 * @param skuId  京东skuId
	 * @param exchangeCount  兑换个数
	 * @param saId  地址id
	 * @param jdGoodsType 京东商品类型 0-实物, 1- 实体卡
	 * @return
	 */
	public String jdSubmitOrder(GsGoodsPurchasingInfo goodsInfo){
		String submitState =yzSysConfig.getSubmitState();
		String regCode = yzSysConfig.getRegCode();
		String invoicePhone = yzSysConfig.getInvoicePhone();
		String invoiceType = yzSysConfig.getInvoiceType();
		goodsInfo.setEmail("1047806836@qq.com");
		//京东下单
		String thirdOrderNo = IDGenerator.generatorId();  //自己的订单号,预制
		//组合下单请求参数
		//默认实物的token下单
		String token = RedisService.getRedisService().get("jdAccessToken");
		if(goodsInfo.getJdGoodsType().equals("1")){ //如为实体卡,则切换下单的token
			token = RedisService.getRedisService().get("jdEntityCardToken");
			log.debug("京东实体卡下单............."+goodsInfo.getGoodsSkuId());
		}
		String orderUrl = "https://bizapi.jd.com/api/order/submitOrder";
		String companyName = "惠州市远智文化教育培训学校";
		StringBuilder sb = new StringBuilder();
		sb.append("token="+token);
		sb.append("&thirdOrder="+thirdOrderNo);
		sb.append("&sku=[{\"skuId\":"+goodsInfo.getGoodsSkuId()+",\"num\":"+goodsInfo.getGoodsNum()+",\"bNeedGift\":true}]");
		sb.append("&name="+goodsInfo.getReceiveName());
		sb.append("&province="+goodsInfo.getProvince());
		sb.append("&city="+goodsInfo.getCity());
		sb.append("&county="+goodsInfo.getDistrict());
		sb.append("&town="+(StringUtil.isEmpty(goodsInfo.getStreet())?"0":goodsInfo.getStreet()));
		sb.append("&address="+goodsInfo.getAddress());
		sb.append("&mobile="+goodsInfo.getReceiveMobile());
		sb.append("&email="+goodsInfo.getEmail());
		sb.append("&invoiceState=2"); 		             //集中开票,运营确认
		sb.append("&invoiceType="+invoiceType);  		 //电子发票 1 普通发票 2 增值税发票 3 电子发票
		sb.append("&selectedInvoiceTitle=5"); 			 //发票类型 单位
		sb.append("&companyName="+companyName);          //发票抬头
		sb.append("&invoiceContent=1"); 				//明细
		sb.append("&paymentType=4");                    //在线支付
		sb.append("&isUseBalance=1");                   //余额
		sb.append("&submitState="+submitState);        //不预占库存 0 预占(可通过接口取消),1 不预占
		sb.append("&regCode="+regCode);                //纳税号 
		sb.append("&invoicePhone="+invoicePhone);      //收增票人电话
		log.debug("京东下单请求参数:--------"+sb.toString());
		String orderResult = HttpUtil.sendPost(orderUrl, sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
		log.debug("下单结果:-------"+orderResult);
		return orderResult;
	}
	
	@SuppressWarnings("unchecked")
	public Object addJDNewBsOrder(Body body){
		GsGoodsPurchasingInfo info = (GsGoodsPurchasingInfo)body.get("purchasing");
		
		//主订单
		BsOrderInfo orderInfo = new BsOrderInfo();
		orderInfo.setOrderNo(IDGenerator.generatorId());
		orderInfo.setOrderDesc("采购订单");
		orderInfo.setOrderType("1");
		orderInfo.setTransAmount("0.00");
		orderInfo.setOrderStatus("6"); //下单即待收货
		orderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		orderInfo.setMobile(info.getReceiveMobile());
		orderInfo.setUserName(info.getReceiveName());
		
		orderInfo.setJdOrderId(body.getString("jdOrderId"));
		orderInfo.setFreight(body.getString("freight"));
		orderInfo.setOrderPrice(body.getString("orderPrice"));
		orderInfo.setOrderNakedPrice(body.getString("orderNakedPrice"));
		orderInfo.setOrderTaxPrice(body.getString("orderTaxPrice"));
		
		bsOrderMapper.addNewBsOrder(orderInfo);
		
		
		BsSerialInfo serialInfo =new BsSerialInfo();
		serialInfo.setSerialNo(IDGenerator.generatorId());
		serialInfo.setOrderNo(orderInfo.getOrderNo());
		serialInfo.setTransAmount(orderInfo.getTransAmount());
		bsOrderMapper.addNewBsSerial(serialInfo);
		
		List<Map<String, String>> skuMap = (List<Map<String, String>>)body.getValue("skus");
		if(null != skuMap){
			for(Map<String, String> map : skuMap){
			
				BsSalesOrderInfo salesOrderInfo = new BsSalesOrderInfo();
				salesOrderInfo.setSubOrderNo(IDGenerator.generatorId());
				salesOrderInfo.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
				salesOrderInfo.setCostPrice("0.00");
				salesOrderInfo.setGoodsCount(info.getGoodsNum());
				salesOrderInfo.setGoodsId("1");
				salesOrderInfo.setGoodsName(info.getGoodsName());
				salesOrderInfo.setOrderNo(orderInfo.getOrderNo());
				salesOrderInfo.setOriginalPrice("0.00");
				salesOrderInfo.setSalesId("1");
				salesOrderInfo.setSalesName(info.getGoodsName());
				salesOrderInfo.setSalesType("4");
				salesOrderInfo.setTransAmount(orderInfo.getTransAmount());
				salesOrderInfo.setUnitPrice("0.00");
				salesOrderInfo.setGoodsType("1");
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
		
		//收货地址信息
		BsLogisticsInfo logistics = new BsLogisticsInfo();
		logistics.setLogisticsId(IDGenerator.generatorId());
		logistics.setAddress(info.getAddress());
		logistics.setUserName(info.getReceiveName());
		logistics.setProvince(info.getProvince());
		logistics.setCity(info.getAddress());
		logistics.setDistrict(info.getDistrict());
		logistics.setStreet(info.getStreet());
		logistics.setLogisticsName("京东自营快递");
		logistics.setMobile(info.getReceiveMobile());
		logistics.setLogisticsStatus("0"); // 0 是新建  1 是妥投   2 是拒收
		logistics.setOrderNo(orderInfo.getOrderNo());
		logistics.setProvinceName(info.getProvinceName());
		logistics.setCityName(info.getCityName());
		logistics.setDistrictName(info.getDistrictName());
		logistics.setStreetName(info.getStreetName());
		logistics.setTransportNo(body.getString("jdOrderId"));
		bsOrderMapper.insertBsLogistics(logistics);
		
		return orderInfo.getJdOrderId();
	}
}
