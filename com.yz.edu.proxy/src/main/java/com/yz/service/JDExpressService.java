package com.yz.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yz.constants.CommonConstants;
import com.yz.exception.IRpcException;
import com.yz.http.HttpUtil;
import com.yz.model.JDAddressVo;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 京东物流(商城)
 * @author lx
 * @date 2018年4月13日 下午3:41:38
 */
@Service
public class JDExpressService {

	
	/**
	 * 京东配送信息查询
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "orderTrackByNo", methodRemark = "京东物流配送信息查询",cacheExpire = 7200,
			useCache =true ,cacheKey = "yz.jd.${jdOrderId}",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	public Object getOrderTrackByNo(Header header, Body body) {
		String jdOrderId = body.getString("jdOrderId");
		String trackUrl = "https://bizapi.jd.com/api/order/orderTrack";
		StringBuilder sb = new StringBuilder();
		sb.append("token="+RedisService.getRedisService().get("jdAccessToken"));
		sb.append("&jdOrderId="+jdOrderId);
		String trackResult = HttpUtil.sendPost(trackUrl, sb.toString(), null);
		JSONObject obj = JSONObject.fromObject(trackResult);
		if(null != obj && obj.containsKey("resultCode") && obj.getString("resultCode").equals("0000")){
			JSONObject resultObj = JSONObject.fromObject(obj.getString("result"));
			return resultObj.getString("orderTrack");
		}
		return null;
	}
	
	/**
	 * 获取京东一级地址(省)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getJDProvince", methodRemark = "JD一级地址获取",cacheExpire = 7200,
			useCache =true ,cacheKey = "jdProvince",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	public Object getJDProvince(Header header, Body body)
	{
		//放内存15天
		List<JDAddressVo> list = new ArrayList<>();
		String provinceUrl ="https://bizapi.jd.com/api/area/getProvince";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken;
		String result = HttpUtil.sendPost(provinceUrl, paramData, null);
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
		return JsonUtil.object2String(list);
		
	}
	/**
	 * 获取京东二级地址(市)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getJDCity", methodRemark = "JD二级地址获取",cacheExpire = 7200,
			useCache =true ,cacheKey = "jdCity.${id}",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	public Object getJDCity(Header header, Body body)
	{
		//暂时放缓存7天时间,以后再说
		String id = body.getString("id");
		List<JDAddressVo> list = new ArrayList<>();
		String cityUrl ="https://bizapi.jd.com/api/area/getCity";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token="+accessToken+"&id="+id;
		String result =HttpUtil.sendPost(cityUrl, paramData, null);
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
		return JsonUtil.object2String(list);
	}
	/**
	 * 获取京东三级地址(区)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getJDCounty", methodRemark = "JD三级地址获取",cacheExpire = 7200,
			useCache =true ,cacheKey = "getJDCounty.${id}",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	public Object getJDCounty(Header header, Body body)
	{
		//暂时放缓存7天时间,以后再说
		String id = body.getString("id");
		List<JDAddressVo> list = new ArrayList<>();
		String countyUrl = "https://bizapi.jd.com/api/area/getCounty";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token=" + accessToken + "&id=" + id;
		String result = HttpUtil.sendPost(countyUrl, paramData, null);
		if (null != result) {
			JSONObject obj = JSONObject.fromObject(result);
			if (obj.getString("resultCode").equals("0000")) {// 请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");

				for (int i = 0; i < spl.length; i++) {
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"", ""));
					vo.setCode(Integer.parseInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return JsonUtil.object2String(list);
	}
	/**
	 * 获取京东四级地址(街道)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong = "gs", methodName = "getJDTown", methodRemark = "JD四级地址获取",cacheExpire = 7200,
			useCache =true ,cacheKey = "getJDTown.${id}",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	public Object getJDTown(Header header, Body body)
	{
		//暂时放缓存7天时间,以后再说
		String id = body.getString("id");
		List<JDAddressVo> list = new ArrayList<>();
		String townurl = "https://bizapi.jd.com/api/area/getTown";
		String accessToken = RedisService.getRedisService().get("jdAccessToken");
		String paramData = "token=" + accessToken + "&id=" + id;
		String result = HttpUtil.sendPost(townurl, paramData, null);
		if (null != result) {
			JSONObject obj = JSONObject.fromObject(result);
			if (obj.getString("resultCode").equals("0000")) {// 请求正常返回数据
				JSONObject objResult = JSONObject.fromObject(obj.getString("result"));
				String[] spl = objResult.toString().replace("{", "").replace("}", "").split(",");

				for (int i = 0; i < spl.length; i++) {
					JDAddressVo vo = new JDAddressVo();
					String[] str = spl[i].split(":");
					vo.setName(str[0].replace("\"", ""));
					vo.setCode(Integer.parseInt(str[1]));
					list.add(vo);
				}
			}
		}
		list.sort(Comparator.naturalOrder());
		return JsonUtil.object2String(list);
	}
	
}
