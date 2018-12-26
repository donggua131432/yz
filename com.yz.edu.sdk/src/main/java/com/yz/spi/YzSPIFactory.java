package com.yz.spi;

import org.slf4j.LoggerFactory;

import com.yz.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

/**
 * 
 * @author lingdian
 *
 */
public class YzSPIFactory {

	private static Logger logger = LoggerFactory.getLogger(YzSPIFactory.class);

	private ConcurrentHashMap<String, YzSPIList> cacheMap = null;

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class YzSpiFactoryHolder {
		private static YzSPIFactory instance = new YzSPIFactory();
	}

	private YzSPIFactory() {
		cacheMap = new ConcurrentHashMap<>();
	}

	/**
	 * 
	 * @return
	 */
	public static YzSPIFactory getInstance() {
		return YzSpiFactoryHolder.instance;
	}

	/**
	 * 
	 * @param interCls
	 * @return
	 */
	public <T> T getDefaultExtension(Class<?> interCls) {
		if (interCls == null || !interCls.isInterface()) {
			logger.error("interCls must a interface!");
			return null;
		}
		String interName = interCls.getName();
		if (this.cacheMap.containsKey(interName)) {
			return getDefaultExtension(this.cacheMap.get(interName));
		}
		YzSPIList spiList = new YzSPIList();
		ServiceLoader serviceLoader = ServiceLoader.load(interCls);
		Iterator iter = serviceLoader.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			SPI spi = obj.getClass().getAnnotation(SPI.class);
			if(spi==null)
			{
				spiList.addSPI(obj.getClass().getName(), false, obj);
			}
			else
			{
				spiList.addSPI(spi.value(), spi.isDefault(), obj);
			}
			
		}
		this.cacheMap.put(interName,spiList);
		return this.getDefaultExtension(spiList);
	}
	
	/**
	 * 
	 * @param interCls
	 * @param val
	 * @return
	 */
	public <T> T getExtension(Class<?> interCls, String val) {
		if (interCls == null || !interCls.isInterface()) {
			logger.error("interCls must a interface!");
			return null;
		}
		String interName = interCls.getName();
		if (this.cacheMap.containsKey(interName)) {
			return getExtension(this.cacheMap.get(interName),val);
		}
		YzSPIList spiList = new YzSPIList();
		ServiceLoader serviceLoader = ServiceLoader.load(interCls);
		Iterator iter = serviceLoader.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			SPI spi = obj.getClass().getAnnotation(SPI.class);
			spiList.addSPI(spi.value(), spi.isDefault(), obj);
		}
		this.cacheMap.put(interName,spiList);
		return this.getExtension(spiList, val);
	}
	
	/**
	 * 
	 * @param list
	 * @param val
	 * @return
	 */
	private <T> T getExtension(YzSPIList list,String val)
	{
		if(list!=null)
		{
			List<YzSPIObj> spiList = list.getSPIList();
			if(spiList==null||spiList.isEmpty())
			{
				logger.error("not found spilist ");
				return null;
			} 
			for(YzSPIObj obj:spiList)
			{
				 if(StringUtil.equals(obj.getValue(), val))
				 {
					 return (T)obj.getTarget();
				 } 
			}
			logger.info("find val:{} failed,use Default Object",val);
			return getDefaultExtension(list);
		}
		logger.error("YzSPIList is null! ");
		return null;
	}
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private <T> T getDefaultExtension(YzSPIList list)
	{
		if(list!=null)
		{
			List<YzSPIObj> spiList = list.getSPIList();
			if(spiList==null||spiList.isEmpty())
			{
				logger.error("not found spilist ");
				return null;
			} 
			for(YzSPIObj obj:spiList)
			{
				 if(obj.isDefault)
				 {
					 return (T)obj.getTarget();
				 } 
			}
			logger.error("Default Object is null,please check config again!");
			return null;
		}
		logger.error("MhsSPIList is null! ");
		return null;
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class YzSPIList implements java.io.Serializable {
		private List<YzSPIObj> objs = null;

		public YzSPIList() {
			objs = new ArrayList<>();
		}

		public void addSPI(YzSPIObj obj) {
			if (obj != null) {
				objs.add(obj);
			}
		}

		public void addSPI(String val, boolean isDefault, Object target) {
			YzSPIObj obj = new YzSPIObj();
			obj.setDefault(isDefault);
			obj.setTarget(target);
			obj.setValue(val);
			this.addSPI(obj);
		}
		
		public List<YzSPIObj> getSPIList() {
             return objs;
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class YzSPIObj implements java.io.Serializable {
		private boolean isDefault;

		private String value;

		private Object target;

		public boolean isDefault() {
			return isDefault;
		}

		public void setDefault(boolean isDefault) {
			this.isDefault = isDefault;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Object getTarget() {
			return target;
		}

		public void setTarget(Object target) {
			this.target = target;
		}

	}
}
