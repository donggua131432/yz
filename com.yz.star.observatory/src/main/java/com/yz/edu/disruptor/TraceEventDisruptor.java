package com.yz.edu.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yz.edu.epl.service.EPLService;
import com.yz.edu.mq.MQConsumer;
import com.yz.edu.mq.MessageHandler;
import com.yz.mq.Event;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class TraceEventDisruptor {

	private Logger logger = LoggerFactory.getLogger(TraceEventDisruptor.class);

	private ExecutorService executor = null;

	private Disruptor<Event> disruptor = null;

	private EPLService eplService = EPLService.getInstance();

	private MQConsumer mqConsumer = MQConsumer.getMQConsumer();

	/** 
	 * 
	 * 
	 */
	private TraceEventDisruptor() {
		executor = Executors.newFixedThreadPool(8, r -> {
			Thread thread = new Thread(r);
			thread.setName("TraceEventThread");
			return thread;
		});
		disruptor = new Disruptor<>(Event::new, 1024, executor, ProducerType.SINGLE, new SleepingWaitStrategy());
		startTask();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			disruptor.shutdown();
			executor.shutdown();
		}));
	}

	private enum TraceEventHolder {
		_instance;
		TraceEventDisruptor disruptor = new TraceEventDisruptor();

		public TraceEventDisruptor getDisruptor() {
			return disruptor;
		}
	}

	/**
	 * @desc 返回CacheTask实例
	 * @return
	 */
	public static TraceEventDisruptor getInstance() {
		return TraceEventHolder._instance.getDisruptor();
	}

	/**
	 * 
	 * @param key
	 * @param cache
	 */
	public void collectEvent(Event info) {
		if (info != null) {
			this.disruptor.getRingBuffer().publishEvent(new EventTranslator<Event>() {
				@Override
				public void translateTo(Event event, long sequence) {
					event.setTopic(info.getTopic());
					event.setEvent(info.getEvent());
					eplService.sendEvent(info);
				}
			});
			logger.info("collectEvent.info=>{}", JsonUtil.object2String(info));
		}
	}

	/**
	 * 
	 * 
	 * @desc 启动任务调度命令
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void startTask() {
		disruptor.handleEventsWith(new TraceEventHandler());
		disruptor.handleExceptionsWith(new IgnoreExceptionHandler());
		disruptor.start();
	}

	/**
	 * @desc 处理traceEvent事件
	 * @param event
	 */
	private void handlerEvent(Event event) {
		MessageHandler<Event> handler = mqConsumer.getHandler(event.getTopic());
		logger.debug("handlerEvent:{}", JsonUtil.object2String(event));
		try {
			handler.execute(event);
		} catch (Exception e) {
			logger.error("handlerEvent:{},msg:{}", JsonUtil.object2String(event), ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * 
	 * @author lingdian
	 *
	 */
	private class TraceEventHandler implements EventHandler<Event>, WorkHandler<Event> {

		@Override
		public void onEvent(Event event) throws Exception {
			logger.debug("onEvent.invoker,envetInfo:{}", JsonUtil.object2String(event));
			handlerEvent(event);
		}

		@Override
		public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
			this.onEvent(event);
		}

	}

}
