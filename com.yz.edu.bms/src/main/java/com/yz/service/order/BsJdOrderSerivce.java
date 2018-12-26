package com.yz.service.order;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.order.BsOrderMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.order.BsOrder;
import com.yz.model.order.BsOrderQuery;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.redis.RedisService;
import com.yz.util.BigDecimalUtil;
import com.yz.util.ExcelUtil; 
import com.yz.util.ExcelUtil.IExcelConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 智米中心-订单管理
 * 
 * @author lx
 * @date 2017年8月8日 下午2:32:02
 */
@Service
@Transactional
public class BsJdOrderSerivce {
	private static Logger log = LoggerFactory.getLogger(BsJdOrderSerivce.class);
	@Autowired
	private BsOrderMapper bsOrderMapper;
	
	@Autowired
	private SysProvinceMapper provinceMapper;

	@Autowired
	private SysCityMapper cityMapper;

	@Autowired
	private SysDistrictMapper districtMapper;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	/**
	 * 得到对账列表
	 * @param start
	 * @param length
	 * @param orderQuery
	 * @return
	 */
	public IPageInfo<BsOrder> getBsJdOrderList(int start, int length, BsOrderQuery orderQuery) {
		PageHelper.offsetPage(start, length);
		List<BsOrder> resultList = bsOrderMapper.getBsJdOrderList(orderQuery);
		if(null != resultList && resultList.size() >0){
			for(BsOrder order :resultList){
				SysProvince province = provinceMapper.selectByPrimaryKey(order.getProvince());
				SysCity city = cityMapper.selectByPrimaryKey(order.getCity());
				SysDistrict district = districtMapper.selectByPrimaryKey(order.getDistrict());
				StringBuilder sb = new StringBuilder();
				if (null != province) {
					sb.append(province.getProvinceName());
				}
				if (null != city) {
					sb.append(city.getCityName());
				}
				if (null != district) {
					sb.append(district.getDistrictName());
				}
				order.setAddress(sb.toString()+order.getAddress());
			}
		}
		
		return new IPageInfo<BsOrder>((Page<BsOrder>) resultList);
	}
	
	/**
	 * 得到列表统计值
	 * @param orderQuery
	 * @return
	 */
	public Object getOrderlistCount(BsOrderQuery orderQuery) {
		HashMap<String,String> mapresult=new HashMap<String,String>();
		//实物订单总数orderNum,totalAmount订单总额
		orderQuery.setJdGoodsType("0");
		HashMap<String,Double> swMap=(HashMap<String, Double>) bsOrderMapper.selectJdOrderTotal(orderQuery);
		if(swMap!=null&&swMap.get("orderNum")!=null) {
			mapresult.put("orderNum", String.valueOf(swMap.get("orderNum")));
		}else {
			mapresult.put("orderNum", "0");
		}
		if(swMap!=null&&swMap.get("totalAmount")!=null) {
			mapresult.put("totalAmount", BigDecimalUtil.round(swMap.get("totalAmount"), 2));
		}else {
			mapresult.put("totalAmount","0");
		}
		
		//实体卡订单总数,订单总额
		orderQuery.setJdGoodsType("1");
		HashMap<String,Double> sktMap=(HashMap<String, Double>) bsOrderMapper.selectJdOrderTotal(orderQuery);
		if(sktMap!=null&&sktMap.get("orderNum")!=null) {
			mapresult.put("stkOrderNum", String.valueOf(sktMap.get("orderNum")));
		}else {
			mapresult.put("stkOrderNum", "0");
		}
		if(sktMap!=null&&sktMap.get("totalAmount")!=null) {
			mapresult.put("stkTotalAmount", BigDecimalUtil.round(sktMap.get("totalAmount"), 2));
		}else {
			mapresult.put("stkTotalAmount","0");
		}
		//实物账户余额
		String balance=getBalance();
		mapresult.put("balance", BigDecimalUtil.round(balance, 2));
		//实体卡账户余额
		String stkbalance=getStkBalance();
		mapresult.put("stkbalance", BigDecimalUtil.round(stkbalance, 2));
//		//拒收订单
//		String refusedAmount=bsOrderMapper.selectRefusedJdOrderAmount(orderQuery);
//		mapresult.put("refusedAmount", BigDecimalUtil.round(refusedAmount, 2));
		return mapresult;
	}
	
	/**
	 * 对账单批量导出
	 * @param orderQuery
	 * @param response
	 */
	@SuppressWarnings("unchecked")
    public void exportJdOrderInfo(BsOrderQuery orderQuery, HttpServletResponse response) {
		// 对导出工具进行字段填充
        IExcelConfig<BsOrder> testExcelCofing = new IExcelConfig<BsOrder>();
        testExcelCofing.setSheetName("index").setType(BsOrder.class)
                .addTitle(new ExcelUtil.IExcelTitle("订单号", "orderNo"))
                .addTitle(new ExcelUtil.IExcelTitle("活动类型", "salesType"))
                .addTitle(new ExcelUtil.IExcelTitle("商品名称", "goodsName"))
                .addTitle(new ExcelUtil.IExcelTitle("单价(京东)", "jdPrice"))
                .addTitle(new ExcelUtil.IExcelTitle("数量", "goodsCount"))
                .addTitle(new ExcelUtil.IExcelTitle("总价(京东)", "orderPrice"))
                .addTitle(new ExcelUtil.IExcelTitle("运费", "freight"))
                .addTitle(new ExcelUtil.IExcelTitle("下单时间", "orderTime"))
                .addTitle(new ExcelUtil.IExcelTitle("收货信息", "address"))
                .addTitle(new ExcelUtil.IExcelTitle("订单状态", "orderStatus"));
        List<BsOrder> resultList = bsOrderMapper.getBsJdOrderList(orderQuery);
        if(null != resultList && resultList.size() >0) {
        	throw new IllegalArgumentException("导出条数不能大于50000条！请先刷选后再导出");
        }
		if(null != resultList && resultList.size() >0){
			for(BsOrder order : resultList){
				//转换活动类型
				String salesType = dictExchangeUtil.getParamKey("salesType", order.getSalesType());
				order.setSalesType(salesType);
				
				//转换订单状态
				String orderStatus = dictExchangeUtil.getParamKey("saleOrderStatus", order.getOrderStatus());
				order.setOrderStatus(orderStatus);
				
				String tempStr="";
				//转换收货信息
				if(order.getTakeUserName()!=null){
					tempStr+="收货人："+order.getTakeUserName()+"\n\r";
					tempStr +="联系手机："+order.getTakeMobile() +"\n\r";
					tempStr +="收货地址："+order.getAddress();
				}else {
					tempStr="无";
				}
				
				order.setAddress(tempStr);
			}
		}
		
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=jdOrderExcel.xls");
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
	}
	
	
	
	//得到京东订单编号得到订单信息
    public BsOrder getJdOrderInfoByjdOrderId  (String jdOrderId) {
    	BsOrder order=null;
    	String url="https://bizapi.jd.com/api/order/selectJdOrder";
    	String token = RedisService.getRedisService().get("jdAccessToken");
		String   data="token="+token+"&jdOrderId="+jdOrderId+"&&queryExts=jdOrderState";
		String   result = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		  
	    JSONObject orderObject = JSONObject.fromObject(result);
		if(orderObject.has("success")&&orderObject.getBoolean("success")) {
				JSONObject orderResult =(JSONObject) orderObject.get("result");
	    		if(orderResult!=null){  
	    			order=new BsOrder();
	    			//订单状态  0 为取消订单  1 为有效 如果返回的订单状态为0则返回空
	    			if(orderResult.has("orderState")&&orderResult.getString("orderState")!=null&&orderResult.getString("orderState").equals("0")) {
	    				return null;
	    			}
	    			//物流状态 0 是新建  1 是妥投   2 是拒收
	    			if(orderResult.has("state")&&orderResult.getString("state")!=null) {
	    				String state=orderResult.getString("state");
	    				if(state.equalsIgnoreCase("1")) {
	    					order.setOrderStatus("3");//对应订单状态是已完成
	    				}else if(state.equalsIgnoreCase("2")) {
	    					order.setOrderStatus("5");//对应订单状态是已拒收
	    				}else {
	    					order.setOrderStatus("6");//对应订单状态是待收货
	    				}
	    			}
	    			if(orderResult.has("orderPrice")&&orderResult.getString("orderPrice")!=null) {
	    				String orderPrice=orderResult.getString("orderPrice");//订单总价格 
		    			order.setOrderPrice(orderPrice);
	    			}
	    			
	    			if(orderResult.has("freight")&&orderResult.getString("freight")!=null) {
	    				String freight=orderResult.getString("freight");//运费（合同有运费配置才会返回，否则不会返回该字段） 
	    				order.setFreight(freight);
	    			}
	    			JSONArray skujsonArray = JSONArray.fromObject(orderResult.get("sku") ); 
	    			if(skujsonArray!=null&&skujsonArray.size()>0){  
	    				JSONObject skuJson=(JSONObject) skujsonArray.get(0);
	    				
	    				String num=skuJson.getString("num");
	    				String price=skuJson.getString("price");//商品价格
	    				String name=skuJson.getString("name");//商品名称
	    				//String type=skuJson.getString("type");//商品类型
	    				order.setJdPrice(price);
	    				order.setGoodsName(name);
	    				order.setGoodsCount(num);
	    			}	
	    		}
			  
		}	
    	return order;
    	
    }
    
    //同步京东对账订单
    public void synchronousJdOrder() {
    	List<BsOrder> resultList = bsOrderMapper.getNeedSynchronousJdOrderList();
    	//主要同步的信息有价格，运费，状态。
    	List<BsOrder> orderResult=new ArrayList<BsOrder>();
    	if(resultList!=null&&resultList.size()>0) {
    		for (BsOrder bsOrder : resultList) {
    			BsOrder order=getJdOrderInfoByjdOrderId(bsOrder.getJdOrderId());
    			if(order!=null) {
    				order.setSkuId(bsOrder.getSkuId());
    				order.setJdOrderId(bsOrder.getJdOrderId());
    				order.setOrderNo(bsOrder.getOrderNo());
    				orderResult.add(order);
    			}
    			
			}
    		
    		if(orderResult.size()>0) {
    			bsOrderMapper.synchronousJdOrder(orderResult);
    		}
    		
    	}
    	
    }
    
    /**
     * 得到京东账户余额
     * @return
     */ 
    public String getBalance() {
    	String balance="0.0";
    	String url="https://bizapi.jd.com/api/price/getBalance";
    	//String token="TiJ5PRR1p5zncGSTgKZnDWtUq";
    	String token = RedisService.getRedisService().get("jdAccessToken");
    	//Assert.hasText(token, "京东实体卡token获取为空！");
		String   data="token="+token+"&payType=4";	
		String   result = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		//result:{"success":true,"resultMessage":"","resultCode":"0000","result":"200.0000"}
		JSONObject resultObject =JSONObject.fromObject(result);
		if(resultObject.has("success")&&resultObject.getBoolean("success")) {
			balance=resultObject.getString("result");
		}
		return balance;		
		
    }
   
    /**
     * 得到京东实体卡账户余额
     * @return
     */ 
    public String getStkBalance() {
    	String balance="0.0";
    	String url="https://bizapi.jd.com/api/price/getBalance";
    	//String token="TiJ5PRR1p5zncGSTgKZnDWtUq";
    	String token = RedisService.getRedisService().get("jdEntityCardToken");
    	//Assert.hasText(token, "京东实体卡token获取为空！");
    	String   data="token="+token+"&payType=4";
		String   result =HttpUtil.sendPost(url , data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		//result:{"success":true,"resultMessage":"","resultCode":"0000","result":"200.0000"}
		JSONObject resultObject =JSONObject.fromObject(result);
		if(resultObject.containsKey("success")&&resultObject.getBoolean("success")) {
			balance=resultObject.getString("result");
		}
		return balance;		
		
    }
    /**
	 * 获取订单配送信息
	 * @param header
	 * @param body
	 * @return
	 */
    public String orderTrackByNo(String jdOrderId){	
		String trackUrl = "https://bizapi.jd.com/api/order/orderTrack";
		StringBuilder sb = new StringBuilder();
		String token = RedisService.getRedisService().get("jdAccessToken");
		sb.append("token="+token);
		sb.append("&jdOrderId="+jdOrderId);
		String trackResult = HttpUtil.sendPost(trackUrl, sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
		log.debug("下单结果:-------"+trackResult);
		JSONObject obj = JSONObject.fromObject(trackResult);
		if(null != obj && obj.containsKey("resultCode") && obj.getString("resultCode").equals("0000")){
			JSONObject resultObj = JSONObject.fromObject(obj.getString("result"));
			return resultObj.getString("orderTrack");
		}
		return null;	
	}

}
