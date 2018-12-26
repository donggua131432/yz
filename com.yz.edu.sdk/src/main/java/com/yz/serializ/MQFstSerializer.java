package com.yz.serializ;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.yz.mq.Event;

import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class MQFstSerializer implements Serializer<Event>, Deserializer<Event> {
	
	private static FstSerializer fstSerializer = FstSerializer.getInstance();
	
	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
	}

	@Override
	public Event deserialize(String topic, byte[] data) {
		return fstSerializer.deserialize(data, Event.class);
	}

	@Override
	public byte[] serialize(String topic, Event data) {
		return fstSerializer.serialize(data);
	}
}