package com.yz.mq;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps; 
import com.yz.serializ.MQFstSerializer;
import com.yz.util.JsonUtil;

@SuppressWarnings("rawtypes")
public class MQProducer {

	private static Logger logger = LoggerFactory.getLogger(MQProducer.class);

	private KafkaProducer kafkaProducer = null;

	private boolean init = false;

	/**
	 * 
	 */
	private MQProducer() {

	}

	public static MQProducer getMQProducer() {
		return MQProducerHolder.insance.mqProducer;
	}

	/**
	 * @author lingdian
	 */
	private enum MQProducerHolder {
		insance;
		private MQProducer mqProducer = new MQProducer();
	}

	@SuppressWarnings("unchecked")
	public void init(KafkaConfig info) {
		if (init) {
			return;
		}
		// kafka的链路 是否关闭
		if (!info.getKafkaDerail()) {
			return;
		}
		Map<String, Object> props = Maps.newHashMap();
		props.put("acks", info.getAcks());
		props.put("retries", info.getRetries());
		props.put("batch.size", info.getBatchSize());
		props.put("buffer.memory", info.getBufferMemory());
		props.put("linger.ms", Integer.valueOf(0));
		props.put("value.serializer", MQFstSerializer.class);
		props.put("key.serializer", StringSerializer.class);
		props.put("bootstrap.servers", info.getBootstrapServers());
		kafkaProducer = new KafkaProducer(props);
		init = true;
		logger.info("initKafkaProducer.props:{}", JsonUtil.object2String(props));
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaProducer::close));
	}

	/**
	 * @desc 发送Event
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void send(Event event) {
		if (kafkaProducer == null) {
			logger.warn("not init kafkaProducer,not send Event");
			return;
		}
		checkEvent(event);
		ProducerRecord<String, Event> record = new ProducerRecord(event.getTopic(), event);
		kafkaProducer.send(record);
	}

	/**
	 * 
	 * @param event
	 *            event 事件
	 * @param partition
	 *            分区
	 */
	@SuppressWarnings("unchecked")
	public void send(Event event, Integer partition) {
		if (kafkaProducer == null) {
			logger.warn("not init kafkaProducer,not send Event");
			return;
		}
		checkEvent(event);
		ProducerRecord<String, Event> record = new ProducerRecord(event.getTopic(), partition,
				String.valueOf(partition), event);
		kafkaProducer.send(record);
	}

	/**
	 * 
	 * @param event
	 */
	private void checkEvent(Event event) {
		if ((event == null) || (event.getTopic() == null)) {
			logger.error("event or event.topic is null");
			throw new IllegalArgumentException("Event和Topic名不能为空");
		}
	}


}
