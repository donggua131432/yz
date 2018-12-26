package com.yz.edu.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.yz.util.ExceptionUtil;

/**
 * 
 * 
 * @author lingdian
 *
 */
@FunctionalInterface
public abstract interface MessageHandler<Event> {

	public Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	/**
	 * 
	 * @param event
	 */
	default public void execute(Event event) throws Exception {
		try {
			if (event != null) {
				this.execute(Lists.newArrayList(event));
			}
		} catch (Exception ex) {
			logger.error("MessageHandler.execute.error:{}", ExceptionUtil.getStackTrace(ex));
		}
	}

	/**
	 * @desc 批量处理events
	 * @param events
	 */
	public void execute(List<Event> events) throws Exception;
}
