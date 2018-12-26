package com.yz.service.goods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.goods.GsGoodsMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.MsgRemindVo;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.ItemSelectInfo;
import com.yz.model.goods.GsExchangeGoodsSales;
import com.yz.model.goods.GsExchangeGoodsSalesDetail;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsSalesInsertInfo;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsGoodsStore;
import com.yz.model.goods.GsSalesExchange;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 兑换
 * @author lx
 * @date 2017年8月4日 下午5:09:00
 */
@Service
@Transactional
public class GsGoodsExchangeService {
	
	@Autowired
	private YzSysConfig yzSysConfig ;
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private GsGoodsService gsGoodsService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	private static final Logger log = LoggerFactory.getLogger(GsGoodsExchangeService.class);
	@SuppressWarnings("static-access")
	public IPageInfo<GsExchangeGoodsSales> getZmcExchangeGoodsSales(int start, int length,GsGoodsSalesQuery salesQuery) {
		PageHelper.offsetPage(start, length);
		List<GsExchangeGoodsSales> resultList = gsGoodsMapper.getZmcExchangeGoodsSales(salesQuery);
		if(null != resultList && resultList.size() >0){
			for(GsExchangeGoodsSales sales : resultList){
				sales.setActivityStatus(gsGoodsService.getSalesStatus(sales.getStartTime(),sales.getEndTime()));
			}
		}
		return new IPageInfo<GsExchangeGoodsSales>((Page<GsExchangeGoodsSales>) resultList);
	}
	public GsExchangeGoodsSalesDetail getZmcExchangeGoodsSalesDetail(String salesId){
		return gsGoodsMapper.getZmcExchangeGoodsSalesDetail(salesId);
	}
	
	public Object insertExchangeGoodsSales(GsExchangeGoodsSalesDetail salesDetail){

		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
    	//如果添加的时候 有goodsId 说明是继承
    	if(StringUtil.hasValue(salesDetail.getGoodsId())&&StringUtil.hasValue(salesDetail.getSalesId())){
    		salesDetail.setIsPhotoChange("1");
    		updateExchangeGoodsSales(salesDetail);
    		return null;
    	}else{
    		goodsInfo.setGoodsName(salesDetail.getGoodsName());
    		goodsInfo.setGoodsDesc(salesDetail.getGoodsDesc());
    		goodsInfo.setGoodsContent(salesDetail.getGoodsContent());
    		goodsInfo.setGoodsType(salesDetail.getGoodsType());
    		goodsInfo.setCostPrice(salesDetail.getCostPrice());
    		goodsInfo.setOriginalPrice(salesDetail.getOriginalPrice());
    		goodsInfo.setGoodsCount(salesDetail.getGoodsCount());
    		goodsInfo.setUpdateUser(salesDetail.getUpdateUser());
    		goodsInfo.setUpdateUserId(salesDetail.getUpdateUserId());
    		goodsInfo.setGoodsUse("1");
    		goodsInfo.setSkuId(salesDetail.getSkuId());
    		goodsInfo.setJdGoodsType(salesDetail.getJdGoodsType());
    		goodsInfo.setGoodsId(IDGenerator.generatorId());
    		gsGoodsMapper.addNewGoodsInfo(goodsInfo);
    		//库存信息
    		GsGoodsStore store = new GsGoodsStore();
    		store.setGoodsId(goodsInfo.getGoodsId());
    		store.setGoodsCount(goodsInfo.getGoodsCount());
    		store.setUpdateUser(goodsInfo.getUpdateUser());
    		store.setUpdateUserId(goodsInfo.getUpdateUserId());
    		gsGoodsMapper.addNewGsGoodsStore(store);
    		
			//附件
			goodsInfo.setAnnexUrl(salesDetail.getCoverUrl());
			goodsInfo.setAnnexUrlPortrait(salesDetail.getCoverUrlPortrait());
			gsGoodsService.initGoodsAnnex(goodsInfo);
			
			salesInfo.setGoodsId(goodsInfo.getGoodsId());
    	}
   
		salesInfo.setUpdateUser(salesDetail.getUpdateUser());
		salesInfo.setUpdateUserId(salesDetail.getUpdateUserId());
		salesInfo.setSalesName(salesDetail.getSalesName());
		salesInfo.setSalesType("1");
		salesInfo.setStartTime(salesDetail.getStartTime());
		salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesPrice(salesDetail.getSalesPrice());
		salesInfo.setSalesStatus(salesDetail.getSalesStatus());
		salesInfo.setInterval(salesDetail.getInterval());
		salesInfo.setSkuId(salesDetail.getSkuId());
		
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		salesInfo.setSalesCount(salesDetail.getGoodsCount());
		salesInfo.setShowSeq(salesDetail.getShowSeq()==null?"0":salesDetail.getShowSeq());
		salesInfo.setSalesId(IDGenerator.generatorId());
		gsGoodsMapper.addZmcGoodsSales(salesInfo);
		
		GsSalesExchange exchange = new GsSalesExchange();
		exchange.setSalesId(salesInfo.getSalesId());
		exchange.setOnceCount(salesDetail.getOnceCount());
		exchange.setUpdateUser(salesDetail.getUpdateUser());
		exchange.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.addGsSalesExchange(exchange);
		
		//开始前以进行提醒
		try {
			salesBeginRemind(salesInfo);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("兑换商品定时任务添加异常:"+e.getMessage());
		}
		
		
		return null;
	}
   
public void updateExchangeGoodsSales(GsExchangeGoodsSalesDetail salesDetail){
    	
    	updateExchangeUseGoodsInfo(salesDetail);
    	
		GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
		salesInfo.setSalesId(salesDetail.getSalesId());
		salesInfo.setGoodsId(salesDetail.getGoodsId());
		salesInfo.setUpdateUser(salesDetail.getUpdateUser());
		salesInfo.setUpdateUserId(salesDetail.getUpdateUserId());
		salesInfo.setSalesName(salesDetail.getSalesName());
		salesInfo.setSalesType("1");
		salesInfo.setStartTime(salesDetail.getStartTime());
		salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesPrice(salesDetail.getSalesPrice());
		salesInfo.setSalesStatus(salesDetail.getSalesStatus());
		salesInfo.setInterval(salesDetail.getInterval());
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		salesInfo.setSalesCount(salesDetail.getGoodsCount());
		salesInfo.setShowSeq(salesDetail.getShowSeq());
		gsGoodsMapper.updateZmcGoodsSales(salesInfo);
		
		GsSalesExchange exchange = new GsSalesExchange();
		exchange.setSalesId(salesInfo.getSalesId());
		exchange.setOnceCount(salesDetail.getOnceCount());
		exchange.setUpdateUser(salesDetail.getUpdateUser());
		exchange.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.updateGsSalesExchange(exchange);
		
		
		//TODO 由于添加的时候已经添加任务,只需要修改cron表达式的数值
		try {
			//需判断是否修改了开始时间
			if(DateUtil.compareDate(salesInfo.getStartTime(), DateUtil.getNowDateAndTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT)>0){
				salesBeginRemind(salesInfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("添加定时异常:"+e.getMessage());
		}
		
	}
	
	public List<Map<String, String>> findAllGoodsByType(String goodsType){
		return gsGoodsMapper.findAllGoodsByType(goodsType);
	}
	
	public void batchDeleteExchange(String[] idArray,String [] goodsIds){
		gsGoodsMapper.batchDeleteExchange(idArray,goodsIds);
	}
   
	/**
     * 修改兑换应用的商品信息
     */
    public void updateExchangeUseGoodsInfo(GsExchangeGoodsSalesDetail salesDetail){
    	//商品信息
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		
		goodsInfo.setGoodsId(salesDetail.getGoodsId());
		goodsInfo.setGoodsName(salesDetail.getGoodsName());
		goodsInfo.setGoodsDesc(salesDetail.getGoodsDesc());
		goodsInfo.setGoodsContent(salesDetail.getGoodsContent());
		goodsInfo.setGoodsType(salesDetail.getGoodsType());
		goodsInfo.setCostPrice(salesDetail.getCostPrice());
		goodsInfo.setOriginalPrice(salesDetail.getOriginalPrice());
		goodsInfo.setGoodsCount(salesDetail.getGoodsCount());
		goodsInfo.setIsPhotoChange(salesDetail.getIsPhotoChange());
		gsGoodsMapper.updateGoodsInfo(goodsInfo);
		//库存信息
		GsGoodsStore store = new GsGoodsStore();
		store.setGoodsId(goodsInfo.getGoodsId());
		store.setGoodsCount(goodsInfo.getGoodsCount());
		store.setUpdateUser(goodsInfo.getUpdateUser());
		store.setUpdateUserId(goodsInfo.getUpdateUserId());
		gsGoodsMapper.updateGsGoodsStore(store);
		//附件
		goodsInfo.setAnnexUrl(salesDetail.getCoverUrl());
		goodsInfo.setAnnexUrlPortrait(salesDetail.getCoverUrlPortrait());
		gsGoodsService.updateGoodsAnnex(goodsInfo);
    }
    
	public void salesBeginRemind(GsGoodsSalesInsertInfo salesInfo)
	{
    	//推送微信公众号信息
		MsgRemindVo remindVo = new MsgRemindVo();
		remindVo.setSalesId(salesInfo.getSalesId());
		remindVo.setSalesName(salesInfo.getSalesName());
		remindVo.setStartTime(salesInfo.getStartTime());
		remindVo.setSalesType(salesInfo.getSalesType());
		remindVo.setPlanCount(salesInfo.getPlanCount());
		String remindTime = DateUtil.dateTimeAddOrReduceMinutes(salesInfo.getStartTime(),-Integer.valueOf(yzSysConfig.getSalesRemindTime()));
		
		RedisService.getRedisService().zrem(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, JsonUtil.object2String(remindVo));
		RedisService.getRedisService().zadd(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, DateUtil.stringToLong(remindTime, DateUtil.YYYYMMDDHHMMSS_SPLIT),JsonUtil.object2String(remindVo));

	}
	
	//得到京东商品池列表
    public List<ItemSelectInfo> getJdPageNumList () {
    	String keyStr="JdPageNumList";
    	//缓存二个小时取一次	
    	List<ItemSelectInfo> pagelist= RedisService.getRedisService().getList(keyStr, ItemSelectInfo.class);
    	if(pagelist==null||pagelist.size()==0) {
    		pagelist=new ArrayList<ItemSelectInfo>();
        	String url="https://bizapi.jd.com/api/product/getPageNum";
        	String token = RedisService.getRedisService().get("jdAccessToken");
        	String data="token="+token;
        	String result = HttpUtil.sendPost(url,data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
        	JSONObject jsonObject =JSONObject.fromObject(result);
        	
        	if(jsonObject.getBoolean("success")) {
        		JSONArray json = JSONArray.fromObject(jsonObject.get("result") ); 
        		if(json.size()>0){  
    			  for(int i=0;i<json.size();i++){  
    				 // 遍历 jsonarray 数组，把每一个对象转成 json 对象  
    				JSONObject job = json.getJSONObject(i); 
    				ItemSelectInfo item=new ItemSelectInfo();
    				item.setItemName(job.get("name").toString());
    				item.setItemCode(job.get("page_num").toString());
    				pagelist.add(item);
    			  }  
        		}
        	}     	
        	RedisService.getRedisService().putList(keyStr,pagelist);
        	RedisService.getRedisService().expire(keyStr, 7200);
    	}
		return pagelist;
    }
    
    /**
     * 获取池内商品编号接口
     * @param pageNum 商品池编号 
     * @param pageNo  页码，默认取第一页；每页最多 10000 条数据
     * @return
     */
    public List<GsExchangeGoodsSales> getJdSkuByPage(String pageNum,String pageNo) {
    	
    	String keyStr="JdSkuByPage-"+pageNum+"-pageNo-"+pageNo;
    	//缓存二个小时取一次

    	List<GsExchangeGoodsSales> pagelist= RedisService.getRedisService().getList(keyStr, GsExchangeGoodsSales.class);
    	if(pagelist==null||pagelist.size()==0) {
    		pagelist=new ArrayList<GsExchangeGoodsSales>();
        	String url="https://bizapi.jd.com/api/product/getSkuByPage";
        	String token = RedisService.getRedisService().get("jdAccessToken");
        	String data="token="+token+"&pageNum="+pageNum+"&pageNo="+pageNo;
        	String result = HttpUtil.sendPost(url, data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
        	////result:{"success":true,"resultMessage":"操作成功","resultCode":"0000","result":{"pageCount":1,"skuIds":[981546,3234250,5001175,6008761]}}
        	JSONObject jsonObject =JSONObject.fromObject(result);
        	
        	if(jsonObject.has("success")&&jsonObject.getBoolean("success")) {
        		JSONObject resultObject=(JSONObject) jsonObject.get("result");
        		JSONArray json = JSONArray.fromObject(resultObject.get("skuIds") ); 
        		if(json.size()>0){  
    			  for(int i=0;i<json.size();i++){ 
    				  //sku 为 8 位时，返回图书或者是音像的详情结果 过滤掉
    				  if(json.get(i).toString().length()==8) {
    					  continue;
    				  }
    				  url="https://bizapi.jd.com/api/product/getDetail";
    				  data="token="+token+"&sku="+json.get(i);
    				  String detailResult = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
    				  // //"result":{"saleUnit":"个","weight":"0.68","productArea":"中国大陆","wareQD":"移动电源*1、说明书*1、安卓充电线*1","imagePath":"jfs/t16420/244/2047781187/81108/200e3ebe/5a76686cN805d140f.jpg","param":"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tr><th class=\"tdTitle\" colspan=\"2\">主体</th><tr><tr><td class=\"tdTitle\">品牌</td><td>罗马仕</td></tr><tr><td class=\"tdTitle\">型号</td><td>sense9</td></tr><tr><td class=\"tdTitle\">颜色</td><td>白色</td></tr><tr><td class=\"tdTitle\">品质联盟</td><td>是</td></tr><tr><th class=\"tdTitle\" colspan=\"2\">规格</th><tr><tr><td class=\"tdTitle\">外壳材质</td><td>塑料</td></tr><tr><td class=\"tdTitle\">容量（mAh）</td><td>25000</td></tr><tr><td class=\"tdTitle\">容量段</td><td>20000mAh及以上</td></tr><tr><td class=\"tdTitle\">电芯类型</td><td>锂聚合物电池</td></tr><tr><td class=\"tdTitle\">输入电压</td><td>5v</td></tr><tr><td class=\"tdTitle\">输出电压</td><td>5v</td></tr><tr><td class=\"tdTitle\">输入电流（mA）</td><td>2.1A</td></tr><tr><td class=\"tdTitle\">输出电流（mA）</td><td>1A/2.1A</td></tr><tr><td class=\"tdTitle\">尺寸(mm)</td><td>210*80*22.6</td></tr><tr><td class=\"tdTitle\">净重(g)</td><td>585</td></tr><tr><th class=\"tdTitle\" colspan=\"2\">特性</th><tr><tr><td class=\"tdTitle\">USB接口数</td><td>多口</td></tr><tr><td class=\"tdTitle\">LED灯</td><td>有</td></tr><tr><td class=\"tdTitle\">电量数字显示</td><td>不支持</td></tr><tr><td class=\"tdTitle\">TYPE-C输入</td><td>不支持</td></tr><tr><td class=\"tdTitle\">苹果输入</td><td>不支持</td></tr><tr><td class=\"tdTitle\">双向快充</td><td>不支持</td></tr></table>","state":1,"sku":981546,"brandName":"罗马仕（ROMOSS）","upc":"6951758347760","category":"9987;830;13658","name":"罗马仕（ROMOSS）sense9 25000毫安充电宝大容量移动电源 3USB输出 聚合物 苹果/安卓手机/平板通用 白色","introduction":"<div align=\"center\"> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t14539/120/536114491/112146/657315d5/5a30a2b0Nc5270c4d.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t14803/256/552257524/49245/ae471f1d/5a30a25fNd990e2bb.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t12901/197/2020046068/74519/cbfb2e11/5a30a25fNcba9d536.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t13756/314/2017726388/43971/b1e949ad/5a30a259Na80c0850.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t14677/171/542117263/46179/d40c5fea/5a30a25aNa836201e.jpg\" /> \n <img src=\"http://img10.360buyimg.com/imgzone/jfs/t15640/55/155348378/28182/90db2ec4/5a28d51fN887ab1c6.gif\" border=\"0\" usemap=\"#Map78444578\" /> \n <map name=\"Map78444578\"> <area shape=\"rect\" coords=\"122,183,675,583\"  ></area> </map> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t12907/199/2062856753/34976/29fb58f2/5a30a260Ne44224b7.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t16195/230/390747351/45495/74876aa6/5a30a230N0499d329.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t16387/57/289730951/42547/38cf7ded/5a30a244N75f09108.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t14737/101/528363622/32802/faa3872/5a30a234N6918562d.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t14437/93/524908231/34648/e03dbb97/5a30a265Ncdba736f.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t13171/47/2029825402/62544/8d5d959e/5a30a260N5d415991.jpg\" /> \n <img src=\"//http://img20.360buyimg.com/vc/jfs/t14530/267/2472522774/55766/e3f8e824/5a9f8465N132f481f.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t12877/22/2047394603/63630/1425cc40/5a30a269N7e22e0c9.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t15934/341/371602848/47993/48e4d487/5a30a24cNecc4b059.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t12100/94/2008294064/50888/d6fca368/5a30a26cN4493fc06.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t13549/165/653125621/55342/9e19d1a6/5a30a254N40a3578d.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t12784/22/2017543093/82179/6831daa1/5a30a26dN687dcb1b.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t13780/304/2031929387/75708/cfcb2846/5a30a270Na74d237b.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t15895/139/364746072/135917/796da670/5a30a272Ne441dc7c.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t13732/168/2077761214/86814/7409a8f1/5a30a2b0N8a106f07.jpg\" /> \n <img src=\"//img10.360buyimg.com/imgzone/jfs/t15361/26/537658912/84706/7066c171/5a30a291N69855afb.jpg\" /> \n</div>"}}

    				  JSONObject goodDetailObject =JSONObject.fromObject(detailResult);
    				  if(goodDetailObject.has("success")&&goodDetailObject.getBoolean("success")) {
    					    JSONObject detail =(JSONObject) goodDetailObject.get("result");
    						//过滤 已下架的商品 1：上架 0：下架 
    						if(detail.getString("state").equals("0")) {
    							continue;
    						}
    						GsExchangeGoodsSales good=new GsExchangeGoodsSales();
    						good.setSalesName(detail.getString("name"));
    						good.setSkuId(detail.getString("sku"));
    						
    						pagelist.add(good);
    						  
    					  
    				  }				  
    			  }  
        		}
        	}
        	RedisService.getRedisService().putList(keyStr,pagelist);
        	RedisService.getRedisService().expire(keyStr, 72000);
    	}
    	
    	return pagelist;
    }
    
    
    /**
     * 得到京东单个商品信息
     * @param skuId
     * @return
     */
    public GsExchangeGoodsSalesDetail getJdGoodDetail(String skuId) {
    	GsExchangeGoodsSalesDetail good=null;
    	String url="https://bizapi.jd.com/api/product/getDetail";
    	String token = RedisService.getRedisService().get("jdAccessToken");
		String   data="token="+token+"&sku="+skuId;
		String detailResult = HttpUtil.sendPost(url,  data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		  
	    JSONObject goodDetailObject =JSONObject.fromObject(detailResult);
		if(goodDetailObject.has("success")&&goodDetailObject.getBoolean("success")) {
				JSONObject detail =(JSONObject) goodDetailObject.get("result");
	    		if(detail!=null){  
					//过滤 已下架的商品 1：上架 0：下架 
					if(detail.getString("state").equals("0")) {
						return null;
					}
					good=new GsExchangeGoodsSalesDetail();
					good.setGoodsName(detail.getString("name"));
					good.setSkuId(detail.getString("sku"));
					good.setGoodsContent(detail.getString("introduction").replaceAll("http:", ""));
					good.setCoverUrlPortrait("//img13.360buyimg.com/n1/"+detail.getString("imagePath"));
					good.setGoodsDesc(detail.getString("brandName"));
					//得到京东商品类型
					if(detail.has("eleGift")&&detail.getString("eleGift")!=null) {
						good.setJdGoodsType("1");//实体卡
					}else {
						good.setJdGoodsType("0");//实物
					}
					//得到京东价格
					url="https://bizapi.jd.com/api/price/getSellPrice";
					data="token="+token+"&sku="+skuId;
			    	String priceresult = HttpUtil.sendPost(url, data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
			    	JSONObject priceObject =JSONObject.fromObject(priceresult);
			    	if(priceObject.has("success")&&priceObject.getBoolean("success")) {
			    		JSONArray pricejsonArray = JSONArray.fromObject(priceObject.get("result") ); 
			    		if(pricejsonArray.size()>0){  
			    			JSONObject priceJson=(JSONObject) pricejsonArray.get(0);
			    			good.setCostPrice(priceJson.getString("jdPrice"));
				    		good.setOriginalPrice(priceJson.getString("price"));
			    		}
			    		
			    	}
	    		}
			  
		}	
    	return good;
    	
    }
    
    
    /**
     * 得到京东单个商品信息
     * @param skuId
     * @return
     */
    public GsExchangeGoodsSalesDetail getJdGoodPrice(String skuId) {
    	
    	//得到京东价格
    	GsExchangeGoodsSalesDetail good=null;
    	String url="https://bizapi.jd.com/api/price/getSellPrice";
    	String token = RedisService.getRedisService().get("jdAccessToken");
		String   data="token="+token+"&sku="+skuId;
		
    	String priceresult =HttpUtil.sendPost(url, data,HttpTraceInterceptor.TRACE_INTERCEPTOR);
    	JSONObject priceObject =JSONObject.fromObject(priceresult);
    	if(priceObject.has("success")&&priceObject.getBoolean("success")) {
    		good=new GsExchangeGoodsSalesDetail();
    		good.setSkuId(skuId);
    		JSONArray pricejsonArray = JSONArray.fromObject(priceObject.get("result") ); 
    		if(pricejsonArray.size()>0){  
    			JSONObject priceJson=(JSONObject) pricejsonArray.get(0);
    			good.setCostPrice(priceJson.getString("jdPrice"));
	    		good.setOriginalPrice(priceJson.getString("price"));
    		}
    		
    	}
    	
    	
		
    	return good;
    	
    }
    
    
    
    @SuppressWarnings("unchecked")
    public void exportExchenageInfo(GsGoodsSalesQuery salesQuery, HttpServletResponse response) {
    	salesQuery.setSalesType("1");
    	// 对导出工具进行字段填充
        IExcelConfig<GsExchangeGoodsSales> testExcelCofing = new IExcelConfig<GsExchangeGoodsSales>();
        testExcelCofing.setSheetName("index").setType(GsExchangeGoodsSales.class)
                .addTitle(new ExcelUtil.IExcelTitle("商品名称", "salesName"))
                .addTitle(new ExcelUtil.IExcelTitle("可兑换数量", "goodsCount"))
                .addTitle(new ExcelUtil.IExcelTitle("京东价", "costPrice"))
                .addTitle(new ExcelUtil.IExcelTitle("市场价", "originalPrice"))
                .addTitle(new ExcelUtil.IExcelTitle("兑换单价", "salesPrice"))
                .addTitle(new ExcelUtil.IExcelTitle("兑换起止", "startTime"))
                .addTitle(new ExcelUtil.IExcelTitle("兑换止", "endTime"))
                .addTitle(new ExcelUtil.IExcelTitle("活动状态", "salesStatus"))
                .addTitle(new ExcelUtil.IExcelTitle("是否在线", "activityStatus"));
        List<GsExchangeGoodsSales> resultList = gsGoodsMapper.getZmcExchangeGoodsSales(salesQuery);
		if(null != resultList && resultList.size() >0){
			for(GsExchangeGoodsSales sales : resultList){
				//转换活动状态
				if(sales.getSalesStatus()!=null&&StringUtil.hasValue(sales.getSalesStatus())) {
					
					String salesStatus = dictExchangeUtil.getParamKey("salesStatus", sales.getSalesStatus());				
					sales.setSalesStatus(salesStatus);
				}
				String activityStatus=gsGoodsService.getSalesStatus(sales.getStartTime(),sales.getEndTime());
				if(activityStatus.equals("0")) {
					sales.setActivityStatus("即将开始");
				}else if(activityStatus.equals("1")) {
					sales.setActivityStatus("进行中");
				}else if(activityStatus.equals("2")) {
					sales.setActivityStatus("已经结束");
				}else {
					sales.setActivityStatus("");
				}
			}
		}
		
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);

        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=goodsExchange.xls");
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
	 
}
