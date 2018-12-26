package com.yz.edu.epl.service;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.edu.constant.ObservatoryStarConstant.ObserStarEnum;
import com.yz.edu.epl.annotation.EplListener;
import com.yz.edu.epl.listener.YzEsperlListener;
import com.yz.edu.model.BaseEsTraceObj;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.edu.model.EsTraceInfo;
import com.yz.edu.model.EsTraceSpan;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.mq.Event;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

/**
 * 
 * @desc EPLService
 * @author Administrator
 *
 */
public class EPLService {

	private Logger logger = LoggerFactory.getLogger(EPLService.class);

	private Map<EplListener, YzEsperlListener<?>> eplListener = Maps.newHashMap();

	private HashMultimap<Class<?>, EPRuntimeExt> ePRuntimeMap = HashMultimap.create();

	private List<EPServiceProvider> epServiceProviders = Lists.newArrayList();

	private EPLService() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::closeListener));
	}

	private enum EPLServiceEnum {
		_instance;

		private EPLService service = new EPLService();
	}

	public static EPLService getInstance() {
		return EPLServiceEnum._instance.service;
	}

	public void addListener(EplListener listener, YzEsperlListener<?> obj) {
		eplListener.put(listener, obj);
	}

	/**
	 * @desc 初始化EpRunTime
	 * @param eplListener
	 * @param listener
	 * @return
	 */
	private void initEPRuntime(EplListener eplListener, YzEsperlListener<?> listener) {
		ObserStarEnum[] alarms = eplListener.alarms();
		for (ObserStarEnum alarm : alarms) {
			Configuration configuration = new Configuration();
			Class<?> cls = alarm.getTargetCls();
			configuration.addEventType(cls.getSimpleName(),cls);
			EPServiceProvider epServiceProvider = EPServiceProviderManager.getDefaultProvider(configuration);
			EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();
			Class<?>[] importCls = alarm.getImportCls();
			if (importCls != null) {
				for (Class<?> importClass : importCls) {
					epAdministrator.getConfiguration().addImport(importClass);
				}
			}
			EPStatement statement = epAdministrator.createEPL(alarm.getEpl());
			statement.addListener(listener);
			EPRuntime runTime = epServiceProvider.getEPRuntime();
			ePRuntimeMap.put(cls, new EPRuntimeExt(runTime, alarm));
			epServiceProviders.add(epServiceProvider);
			logger.info("initEPRuntime.start：{}", alarm.getRemark());
		}
	}

	/**
	 *
	 * @desc 启动EPL监听器
	 *
	 */
	public void startListener() {
		this.eplListener.entrySet().parallelStream().forEach(v -> {
			EplListener eplListener = v.getKey();
			YzEsperlListener<?> listener = v.getValue();
			this.initEPRuntime(eplListener, listener);
		});
	}

	/**
	 *
	 * @desc 关闭监听器
	 *
	 */
	public void closeListener() {
		this.epServiceProviders.parallelStream().forEach(v -> {
			v.destroy();
		});
	}

	/***
	 * @desc 发送Event事件
	 * @param event
	 */
	public void sendEvent(Event event) {
		String topic = event.getTopic();
		Object obj = event.getEvent();
		switch (topic) {
		case TraceConstant.TRACE_INFO_TOPIC:
			sendEventTraceInfo((TraceTransfer) obj);
			break;
		case TraceConstant.TRACE_SPAN_TOPIC:
			sendEventTraceSpan((TraceSpan) obj);
			break;
		}
	}

	/**
	 * @desc
	 * @param trace
	 */
	private void sendEventTraceSpan(TraceSpan span) {
		List<BaseEsTraceObj> datas = Lists.newArrayList();
		datas.addAll(EsTraceAnnotation.wrap(span));
		datas.addAll(EsTraceSpan.wrap(span));
		sendEplEvent(datas);
	}

	/**
	 * @desc
	 * @param trace
	 */
	private void sendEventTraceInfo(TraceTransfer trace) {
		List<BaseEsTraceObj> datas = Lists.newArrayList();
		datas.add(EsTraceInfo.wrap(trace));
		datas.addAll(EsTraceAnnotation.wrap(trace));
		datas.addAll(EsTraceSpan.wrap(trace));
		sendEplEvent(datas);
	}

	/**
	 * @desc 发送Event事件
	 * @param trace
	 */
	private void sendEplEvent(List<BaseEsTraceObj> traces) {
		if (traces != null) {
			traces.parallelStream().filter(Objects::nonNull).forEach(trace -> {
				Class<?> cls = trace.getClass();
				Set<EPRuntimeExt> set = ePRuntimeMap.get(cls);
				if (set != null && !set.isEmpty()) {
					set.parallelStream().forEach(runTimeExt -> {
						if (runTimeExt != null) {
							ObserStarEnum alarm = runTimeExt.getAlarm();
							trace.setAlarm(alarm);
							runTimeExt.getRunTime().sendEvent(trace);
						}
					});
				}
			});
		}
	}

	private class EPRuntimeExt {
		private EPRuntime runTime;

		private ObserStarEnum alarm;

		public EPRuntimeExt(EPRuntime runTime, ObserStarEnum alarm) {
			this.runTime = runTime;
			this.alarm = alarm;
		}

		public EPRuntime getRunTime() {
			return runTime;
		}

		public ObserStarEnum getAlarm() {
			return alarm;
		}
	}
}
