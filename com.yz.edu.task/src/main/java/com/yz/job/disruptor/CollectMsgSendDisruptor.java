package com.yz.job.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkHandler; 
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yz.generator.IDGenerator;
import com.yz.job.dao.GwReceiverMapper;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 
 * @author Administrator
 *
 */
@Service
public class CollectMsgSendDisruptor {

	private Logger logger = LoggerFactory.getLogger(CollectMsgSendDisruptor.class);

	private ExecutorService executor = null;

	private Disruptor<CollectMsgSendRespCmd> disruptor = null;
	
	@Autowired
	private GwReceiverMapper receiverMapper;
	
	/** 
	 * 
	 * 
	 */
	private CollectMsgSendDisruptor() {
		executor = Executors.newSingleThreadExecutor(r -> {
			Thread thread = new Thread(r);
			thread.setName("MsgSendCacheTaskThread");
			return thread;
		});
		disruptor = new Disruptor<>(CollectMsgSendRespCmd::new, 128, executor, ProducerType.SINGLE,
				new SleepingWaitStrategy());
		startTask();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			disruptor.shutdown();
			executor.shutdown();
		}));
	}

	private enum CollectMsgSendDisruptorHolder {
		_instance;
		CollectMsgSendDisruptor disruptor = new CollectMsgSendDisruptor();

		public CollectMsgSendDisruptor getDisruptor() {
			return disruptor;
		}

	}

	/**
	 * @desc 返回CacheTask实例
	 * @return
	 */
	public static CollectMsgSendDisruptor getInstance() {
		return CollectMsgSendDisruptorHolder._instance.getDisruptor();
	}

	/**
	 * 
	 * @param key
	 * @param cache
	 */
	public void collectMsgResp(CollectMsgSendRespCmd info) {
		this.disruptor.getRingBuffer().publishEvent(new EventTranslator<CollectMsgSendRespCmd>() {
			@Override
			public void translateTo(CollectMsgSendRespCmd event, long sequence) {
				try {
					BeanUtils.copyProperties(event, info);
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.error(ExceptionUtil.getStackTrace(e));
				}
			}
		});
		logger.info("collectMsgResp.info=>{}", JsonUtil.object2String(info));
	}

	/**
	 * 
	 * 
	 * @desc 启动任务调度命令
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void startTask() {
		disruptor.handleEventsWith(new CollectMsgRespEventHandler());
		disruptor.handleExceptionsWith(new IgnoreExceptionHandler());
		disruptor.start();
	}

	/**
	 * @desc 根据缓存策略更新相应的缓存
	 * @param info
	 */
	public void handlerMsgResp(CollectMsgSendRespCmd info) {
		logger.info("collectMsgResp:{}",JsonUtil.object2String(info));
		//TODO 如果后期处理别的,再修改
		if(StringUtil.isNotBlank(info.getMappingId())){
			info.setSrId(IDGenerator.generatorId());
			receiverMapper.insertSendRecords(info);
			logger.info("insert result:{}",JsonUtil.object2String(info));
		}
		
	}

	/**
	 * 
	 * 
	 * @author lingdian
	 *
	 */
	private class CollectMsgRespEventHandler implements EventHandler<CollectMsgSendRespCmd>, WorkHandler<CollectMsgSendRespCmd> {

		@Override
		public void onEvent(CollectMsgSendRespCmd event) throws Exception {
			logger.info("onEvent.invoker,envetInfo:{}", JsonUtil.object2String(event));
			handlerMsgResp(event);
		}

		@Override
		public void onEvent(CollectMsgSendRespCmd event, long sequence, boolean endOfBatch) throws Exception {
			this.onEvent(event);
		}

	}

}
