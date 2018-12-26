package com.yz.edu.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.edu.alarm.EmailAlarmService;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.epl.annotation.EplListener;
import com.yz.edu.epl.listener.YzEsperlListener;
import com.yz.edu.model.EsTraceInfo;
import com.yz.util.JsonUtil;

@EplListener(name = "esTraceInfoListener", alarms = {
		ObservatoryStarConstant.ObserStarEnum.COSTTIME_ALARM,
		})
@Service(value = "traceListener")
public class TraceInfoListener implements YzEsperlListener<EsTraceInfo> {

	private Logger logger = LoggerFactory.getLogger(TraceInfoListener.class);
	
	@Autowired
	private EmailAlarmService emailAlarmService;

	@Override
	public void listener(List<EsTraceInfo> infos) {
		if (infos != null) {
			infos.parallelStream().forEach(v -> {
				emailAlarmService.sendCostTimeEmail(v);
				logger.info("TraceListener.info:{}", JsonUtil.object2String(v));
			});
		}
	}
}
