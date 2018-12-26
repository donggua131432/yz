package com.yz.edu.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.edu.alarm.EmailAlarmService;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.epl.annotation.EplListener;
import com.yz.edu.epl.listener.YzEsperlListener;
import com.yz.edu.model.AnnotationAlarm;

@EplListener(name = "esTraceAnnotationListener", alarms = { ObservatoryStarConstant.ObserStarEnum.SLOWRES_ALARM, })
@Service(value = "esTraceAnnotationListener")
public class TraceAnnotationListener implements YzEsperlListener<AnnotationAlarm> {

	@Autowired
	private EmailAlarmService emailAlarmService;
	
	@Override
	public void listener(List<AnnotationAlarm> annotations) {
       this.emailAlarmService.sendSlowResEmail(annotations);
	}

}
