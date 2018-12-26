package com.yz.edu.epl.listener;

import java.util.List;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.bean.BeanEventType;
import com.espertech.esper.event.map.MapEventBean;
import com.espertech.esper.event.map.MapEventType;
import com.google.common.collect.Lists;

/**
 * @desc esper 监听器
 * @author Administrator
 *
 */
public interface YzEsperlListener<T> extends UpdateListener {

	@Override
	default void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			List<T> data = Lists.newArrayListWithCapacity(newEvents.length);
			for (EventBean eb : newEvents) { 
				if(eb.getEventType() instanceof BeanEventType)
				{
					data.add((T) eb.getUnderlying());
				}else if(eb.getEventType() instanceof MapEventType)
				{
					MapEventBean eventBean = (MapEventBean)eb;
					data.add((T) eventBean.getProperties().values().parallelStream().findFirst().get());
				}
				
			}
			this.listener(data);
		}
	}


	/**
	 * 
	 * @param t
	 */
	public void listener(List<T> t);
}
