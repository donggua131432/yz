package com.yz.star.observatory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.model.AnnotationAlarm;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.edu.trace.TraceAnnotation;
import com.yz.template.YzBuzResolverEngine;
import com.yz.util.JsonUtil;

public class EPLSlowResTest {

	private static final String epl = "select new AnnotationAlarm(avg(destination),max(destination),resouceType,count(0)) from EsTraceAnnotation.win:time(60 sec) group by resouceType output snapshot every 10 seconds";
    	
	private static YzBuzResolverEngine templateEngine = YzBuzResolverEngine.getInstance();
	
	public static void main(String[] args) throws Exception {
       // epl= "";
		Configuration configuration = new Configuration();
		Class<?>cls = EsTraceAnnotation.class;
		configuration.addEventType(  cls);
		EPServiceProvider epServiceProvider = EPServiceProviderManager.getDefaultProvider(configuration);
		EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();
		epAdministrator.getConfiguration().addImport(AnnotationAlarm.class);
		EPStatement statement = epAdministrator.createEPL(epl);
		statement.addListener(new UpdateListener() {
			
			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				List<AnnotationAlarm>annotations = Lists.newArrayListWithCapacity(newEvents.length);
				for(EventBean eb:newEvents)
				{
					MapEventBean mp = (MapEventBean)eb;
					annotations.add((AnnotationAlarm)mp.getProperties().values().parallelStream().findFirst().get());
				}
				Map<String,Object>data =Maps.newHashMap();
				data.put("annotations",annotations);
				templateEngine.resolveTemplate(ObservatoryStarConstant.SLOW_TEMPLATE, data);
			}
		});
		EPRuntime runTime = epServiceProvider.getEPRuntime();
		send(runTime, 10);
		System.in.read();
	}
	
	private static void send(EPRuntime runTime,int times)throws Exception
	{
		for(int i =0;i<times;i++)
		{
			EsTraceAnnotation annotation = new EsTraceAnnotation();
			annotation.setDate(new Date());
			annotation.setResouceType(Math.max(1,i%6));
			annotation.setDestination(200+i*10);
			annotation.setOperation("hget");
			annotation.setTraceId("1");
			annotation.setSpanId("1");
			annotation.setSort(1);
			annotation.setResouceURI("localhost");
			runTime.sendEvent(annotation);
			Thread.sleep(20);
			System.out.println(annotation.getDestination());
		}
	
	}

}
