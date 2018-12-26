package com.yz.core.handler;

 
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yz.cache.common.CacheContent;
import com.yz.cache.common.YzCacheBean;
import com.yz.cache.handler.BaseRedisCacheHandler;
import com.yz.conf.YzSysConfig;
import com.yz.core.constants.AppConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.model.system.SysDict;
import com.yz.service.SysParameterService;
import com.yz.service.system.SysDictService;
import com.yz.util.JsonUtil;

@Component(value =  AppConstants.BMS_CACHE_HANDLER)
public class BmsCacheHandler extends BaseRedisCacheHandler {
 
	@Value("${cacheFile.dict}")
	private String dictFileName;

	@Value("${cacheFile.sysParam}")
	private String paramFileName;
	
	@Autowired
	private SysDictService dictService;
	
	@Autowired
	private SysParameterService parameterService;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Override
	public void setCache(String redisName, String key, Object result, int cacheExpire) {
		
		YzCacheBean cacheBean =CacheContent.getInstance().getCacheBean();
		if(null != cacheBean){
			if(cacheBean.getCacheRelation().equals("dict")){ //数据字典
				Map<String, Object> dictMaps = new HashMap<String, Object>();
				
				List<SysDict> pDicts = dictService.getDicts("0");
				
				for(SysDict dict : pDicts) {
					String dictId = dict.getDictId();
					List<SysDict> dicts = dictService.getDicts(dictId);
					if(dicts == null || dicts.isEmpty())
						continue;
					
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					
					for(SysDict subDict : dicts) {
						String subDictId = subDict.getDictId();
						String subDictName = subDict.getDictName();
						String subDictValue = subDict.getDictValue();
						String subExt1 = subDict.getExt1();
						
						Map<String, String> m = new HashMap<String, String>();
						
						m.put("dictId", subDictId);
						m.put("dictName", subDictName);
						m.put("dictValue", subDictValue);
						m.put("ext1", subExt1);
						
						list.add(m);
					}
					
					dictMaps.put(dictId, list);
				}
				
				String json = "var dictJson = " + JsonUtil.object2String(dictMaps);
				String bucket = yzSysConfig.getBucket();
				
				String filePath = FileSrcUtil.createOrign(Type.CACHE, dictFileName);
				
				try {
					FileUploadUtil.upload(bucket, filePath, json.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					
				}
				
			}else if(cacheBean.getCacheRelation().equals("param")){  //系统参数
				//List<SysParameter> list = parameterService.sellectAll(null, null);
				List<Map<String, Object>> list = parameterService.sellectAll();
				String bucket = yzSysConfig.getBucket();
				
				if(list != null && !list.isEmpty()) {
					Map<String, String> map = new HashMap<String, String>();
					
					for(Map<String, Object> param : list) {
						map.put(param.get("paramName").toString(), param.get("paramValue").toString());
					}
					
					String json = JsonUtil.object2String(map);
					
					json = "var bmsParamJson = " + json + ";";
					json += "var _FILE_URL = bmsParamJson['file.browser.url'];";
					json += "var _TEMP_FILE_URL = bmsParamJson['temp.file.browser.url'];";
					
					String filePath = FileSrcUtil.createOrign(Type.CACHE, paramFileName);
					
					try {
						FileUploadUtil.upload(bucket, filePath, json.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						
					}
				}
			}
		}
		logger.info("setCache:{}", key);
	}

	@Override
	public Object getCache(String redisName, String key, Class<?> cls) {
		return null;
	}

}
