package com.yz.edu.mq;

import com.google.common.collect.Maps;
import com.yz.edu.disruptor.TraceEventDisruptor;
import com.yz.mq.Event;
import com.yz.mq.KafkaConfig;
import com.yz.serializ.MQFstSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener; 
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lingdian
 */
public class MQConsumer {

	private static Logger log = LoggerFactory.getLogger(MQConsumer.class);
	private int pollTime = 5;
	private ExecutorService streamThreadPool;
	private KafkaConsumer<String, Event> consumer;
	private Map<String, MessageHandler<Event>> handlerMaps = Maps.newHashMap();
	private volatile Status status = Status.INIT;

	private MQConsumer() {

	}

	public static MQConsumer getMQConsumer() {
		return ConsumerHolder.instance;
	}

	/**
	 *
	 * @param topic
	 * @param handler
	 */
	public void addTopicHandler(String topic, MessageHandler<Event> handler) {
		this.handlerMaps.put(topic, handler);
	}

	
	/**
	 *
	 * @param topic 
	 */
	public MessageHandler<Event> getHandler(String topic) {
		return this.handlerMaps.get(topic);
	}
	
	/**
	 * 
	 * @param info
	 * @param groupId
	 */
	public void init(KafkaConfig info, String groupId) {
		Map<String, Object> props = Maps.newHashMap();
		props.put("key.deserializer", StringDeserializer.class);
		props.put("value.deserializer", MQFstSerializer.class);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "5000");
		props.put("fetch.min.bytes", "1");
		props.put("max.partition.fetch.bytes", 102400);
		props.put("enable.auto.commit", "true");
		props.put("session.timeout.ms", "30000");
		props.put("bootstrap.servers", info.getBootstrapServers());
		props.put("group.id", groupId);
		consumer = new KafkaConsumer<>(props);
		Runtime.getRuntime().addShutdownHook(new Thread(this::close));
	}

	public void subscribe() {
		List<String> topics = new ArrayList<>(this.handlerMaps.keySet());
		if (topics.size() <= 0) {
			throw new IllegalArgumentException("topic can't be null.");
		}
		if (consumer == null) {
			throw new IllegalArgumentException("cosumer init error, please check your config! ");
		}
		streamThreadPool = Executors.newFixedThreadPool(4);
		consumer.subscribe(topics, new ConsumerRebalanceListener() {
			long assigneStart;

			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				this.assigneStart = System.currentTimeMillis();
				log.info("mq-client Revoked ");
			}

			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				for (TopicPartition topicPartition : partitions) {
					log.info(
							"mq-client Assigned ,topic: {}, partitions: {} duration: "
									+ (System.currentTimeMillis() - this.assigneStart) + " ms",
							topicPartition.topic(), topicPartition.partition());
				}
			}
		});
		this.status = Status.RUNNING;
		try {
			this.streamThreadPool.submit(new MessageTask(consumer));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	private void close() {
		this.status = Status.STOPPING;
		log.info("MQ Consumer is stopping.......");
		if (null != streamThreadPool) {
			this.streamThreadPool.shutdown();
			try {
				if (!this.streamThreadPool.awaitTermination(10L, TimeUnit.SECONDS)) {
					this.streamThreadPool.shutdownNow();
					if (!this.streamThreadPool.awaitTermination(10L, TimeUnit.SECONDS)) {
						log.error("Pool did not terminate");
					}
				}
			} catch (InterruptedException ie) {
				this.streamThreadPool.shutdownNow();
				Thread.currentThread().interrupt();
			}
			consumer.unsubscribe();
			consumer.close();
			log.info("MQ Consumer is closed!");
			this.status = Status.STOPPED;
		}
	}

	enum Status {
		INIT, RUNNING, STOPPING, STOPPED;

		Status() {
		}
	}

	private static class ConsumerHolder {

		static MQConsumer instance = new MQConsumer();
	}

	private class MessageTask implements Runnable {

		protected final KafkaConsumer<String, Event> consumer;

		public MessageTask(KafkaConsumer<String, Event> consumer) {
			this.consumer = consumer;
		}

		public void run() {
			while (MQConsumer.this.status == MQConsumer.Status.RUNNING) {
				try {					
					ConsumerRecords<String, Event> records = this.consumer.poll(MQConsumer.this.pollTime);
					//HashMultimap<String, Event> events = HashMultimap.create();
					records.forEach(record -> {
						if ((record == null) || (record.value() == null)) {
							log.error("the record or record value is null, skip it!");
						} else {
							//events.put(record.topic(), record.value());
							TraceEventDisruptor.getInstance().collectEvent(record.value());
						}
					}); 
					
				} catch (Exception e) {
					log.error("poll exception, e:", e);
				}
			}
		}
	}

}
