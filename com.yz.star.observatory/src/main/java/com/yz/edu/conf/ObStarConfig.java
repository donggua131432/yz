package com.yz.edu.conf;

import java.net.InetAddress;
import java.security.Security;
import java.util.Properties;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.yz.edu.mq.MQConsumer;
import com.yz.mq.KafkaConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Configuration
@AutoConfigureAfter(value = { ObStarEsConfig.class, KafkaConfig.class })
public class ObStarConfig {

	private static Logger logger = LoggerFactory.getLogger(ObStarConfig.class);

	private static final String OB_STAR_GROUP_ID = "yz-utrace-mq";

	@Bean(name = "yzKafkaConfig")
	public KafkaConfig initKafkaConfig() {
		return new KafkaConfig();
	}

	@Bean(name = "yzObStarEsConfig")
	public ObStarEsConfig initObStarEsConfig() {
		return new ObStarEsConfig();
	}

	@Bean(name = "mqConsumer")
	public MQConsumer initMQConsumer(@Autowired KafkaConfig config) {
		MQConsumer consumer = MQConsumer.getMQConsumer();
		consumer.init(config, OB_STAR_GROUP_ID);
		return consumer;
	}

	@Bean(name = "yzTransportClient")
	public TransportClient initTransportClient(@Autowired ObStarEsConfig config) throws Exception {
		Settings setting = Settings.builder()
				.put("client.transport.sniff", true)
				.put("client.transport.ignore_cluster_name", false)
				.put("client.transport.ping_timeout", "300s")
				.put("client.transport.nodes_sampler_interval", "300s")
				.put("cluster.name", config.getName())
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(setting);
		String[] arr = StringUtil.split(config.getAddr(), "|");
		for (String str : arr) {
			String addr = StringUtil.substringBefore(str, ":");
			String port = StringUtil.substringAfter(str, ":");
			transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(addr),
					org.apache.commons.lang.math.NumberUtils.toInt(port)));
		}
		logger.info("config:{}", JsonUtil.object2String(config));
		return transportClient;
	}

	@Bean(name = "emailAlarmConfig")
	/**
	 * @desc 初始化邮件预警配置
	 * @return
	 */
	public EmailAlarmConfig initEmailAlarmConfig() {
		return new EmailAlarmConfig();
	}

	@Bean(name = "mailSender")
	public JavaMailSender initJavaMailSender(@Autowired EmailAlarmConfig config) {
		 
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(config.getHost());
		sender.setPassword(config.getPassword());
		sender.setUsername(config.getUsername());
		sender.setPort(config.getPort());
		Properties properties = new Properties();
		properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.timeout", 30000);
	    properties.put("mail.smtp.starttls.enable", "true"); 
	    properties.put("mail.smtp.socketFactory.fallback", "false");
	    properties.put("mail.smtp.ssl.enable", "true");
	    properties.put("mail.transport.protocol", "smtp");
	    properties.put("mail.debug", "true");//启用调试
	    properties.put("mail.smtp.port", config.getPort());
        properties.put("mail.smtp.socketFactory.port", config.getPort());
		sender.setJavaMailProperties(properties);
		sender.setDefaultEncoding("UTF-8");  
		return sender;
	}
}
