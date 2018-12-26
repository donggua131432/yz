package com.yz.app;

import org.springframework.boot.Banner; 
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
		"com.yz.cache",
		"com.yz.redis",
		"com.yz.datasource",
		"com.yz.conf",
		"com.yz.service",
		"com.yz.convert",
		"com.yz.controller",
		"com.yz.core.util",
		"com.yz.classloader",
		"com.yz.oss",
		"com.yz.core.*",
		"com.yz.dubbo",
		"com.yz.webservice"
}, exclude = {
		WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class,
		ThymeleafAutoConfiguration.class
})
public class BstApplication {
	/**
	 * @desc BST启动类
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(BstApplication.class).web(true).bannerMode(Banner.Mode.CONSOLE).run(args);
	}
}
