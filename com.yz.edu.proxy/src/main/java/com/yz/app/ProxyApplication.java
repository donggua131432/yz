package com.yz.app;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder; 

@SpringBootApplication(scanBasePackages = {
		"com.yz.conf",
		"com.yz.controller",
		"com.yz.service",
		"com.yz.redis",
		"com.yz.dao",
		"com.yz.datasource", 
		"com.yz.executor",
		"com.yz.convert",
		"com.yz.pay",
		"com.yz.cache",
		"com.yz.sub",
		"com.yz.mq"
}, exclude = { WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class })
public class ProxyApplication {

	/**
	 * @desc 代理网关启动类
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(ProxyApplication.class).web(true).bannerMode(Mode.CONSOLE).run(args);
	}
}
