package com.yz.template;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yz.template.tag.BuzCustomizeDirective; 
import com.yz.template.tag.BuzRuleLoaderDirective;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;
import com.yz.util.XmlUtil;

import freemarker.cache.SoftCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template; 

/**
 * 
 * @desc yzBuz
 * @author Administrator
 *
 */
public class YzBuzResolverEngine {

	private static Logger logger = LoggerFactory.getLogger(YzBuzResolverEngine.class);

	private Configuration config = null;

	private static Map<String, String> ENGINE_TEMPLATE_CACHE = new ConcurrentHashMap<String, String>();

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class YzBuzResolverEngineHolder {
		private static YzBuzResolverEngine _instance = new YzBuzResolverEngine();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private void initConfiguration() throws Exception {
		config = new Configuration(Configuration.VERSION_2_3_25);
		Map<String, Object> freemarkerVariables = Maps.newHashMap();
		freemarkerVariables.put("yzRuleTag", new BuzCustomizeDirective());
		freemarkerVariables.put("yzRuleList", new BuzRuleLoaderDirective());
		
		Properties settings = new Properties();
		settings.put("tag_syntax", "auto_detect");
		settings.put("template_update_delay", "5");
		settings.put("defaultEncoding", "UTF-8");
		settings.put("output_encoding", "UTF-8");
		settings.put("url_escaping_charset", "UTF-8");
		settings.put("locale", "zh_CN");
		settings.put("boolean_format", "true,false");

		settings.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
		settings.put("date_format", "yyyy-MM-dd");
		settings.put("time_format", "HH:mm:ss");
		settings.put("number_format", "0.######");
		settings.put("whitespace_stripping", "true");

		config.setSettings(settings);

		config.setSharedVaribles(freemarkerVariables);
		config.setCacheStorage(new SoftCacheStorage());
		config.setAutoFlush(false);
	}

	private YzBuzResolverEngine() {
		try {
			initConfiguration();
		} catch (Exception e) {
			logger.error("initConfiguration.error:{}", ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static YzBuzResolverEngine getInstance() {
		return YzBuzResolverEngineHolder._instance;
	}

	/**
	 * @desc 将模板内容进行缓存
	 * @param resource
	 * @return
	 */
	private String loadTemplateContent(String resource) {
		if (ENGINE_TEMPLATE_CACHE.containsKey(resource)) {
			return ENGINE_TEMPLATE_CACHE.get(resource);
		}
		InputStream is = null;
		try {
			is = YzBuzResolverEngine.class.getClassLoader().getResourceAsStream("template/" + resource + ".xml");
			String content = IOUtils.toString(is, "utf-8");
			ENGINE_TEMPLATE_CACHE.put(resource, content);
			return content;
		} catch (Exception ex) {
			logger.error("resolveTemplate.error:{}", ExceptionUtil.getStackTrace(ex));
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * 
	 * @param resource
	 * @param param
	 * @return
	 */
	public String resolveTemplate(String resource, Map<String, Object> param) {
		try {
			String content = loadTemplateContent(resource);
			if(StringUtil.isBlank(content))
			{
				logger.error("not fould resource:{} ,please check config !" ,resource);
				return null;
			}
			YzBuzEngineContext.getInstance().addContextParam(param);
			Template template = new Template(resource + "_template", new StringReader(content), config);
			StringWriter result = new StringWriter();
			template.process(param, result);
			logger.info("resolver.template:{}.content:{}.result:{}", resource, content, result);
			return result.toString();
		} catch (Exception ex) {
			logger.error("resolveTemplate.error:{}", ExceptionUtil.getStackTrace(ex));
			return null;
		}finally
		{
			YzBuzEngineContext.getInstance().clear();
		}
	}

	/**
	 * 
	 * @param resource
	 * @param param
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T resolver(String resource, Map<String, Object> param, Class<?> cls) {
		String result = resolveTemplate(resource, param);
		return (T) XmlUtil.xml2Obj(result, cls);
	}

	/**
	 * 
	 * @param resource
	 * @param param
	 * @param cls
	 * @return
	 */
	public <T> List<T> resolverList(String resource, Map<String, Object> param, Class<?> cls) {
		String result = resolveTemplate(resource, param);
		return XmlUtil.xml2List(result, cls);
	}
}
