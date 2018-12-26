package com.yz.template.handler;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Administrator
 *
 */
public interface YzBuzHandlerChain {
	

	/**
	 * @desc 添加处理器链条
	 * @param handler
	 * @return
	 */
	default public YzBuzHandlerChain addHandler(YzBuzHandler handler) {
		if (handler != null)
			this.handlers().add(handler);
		return this;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	default public boolean doHandler(Map<String, Object> param) {
		Set<YzBuzHandler> sets = this.handlers();
		if (sets != null) {
			for (YzBuzHandler handler : sets) {
				if (!handler.match(param)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public Set<YzBuzHandler> handlers();
}
