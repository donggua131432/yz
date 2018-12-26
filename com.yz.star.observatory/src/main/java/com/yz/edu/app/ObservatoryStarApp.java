package com.yz.edu.app;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = { 
		"com.yz.edu.disruptor", 
		"com.yz.edu.conf", 
		"com.yz.edu.service",
		"com.yz.edu.mq",
		"com.yz.mq",
		"com.yz.edu.listener",
		"com.yz.edu.epl.register",
		"com.yz.edu.alarm",
		"com.yz.edu.controller"
		},
        exclude = {
				WebSocketAutoConfiguration.class, 
				RedisAutoConfiguration.class,
				DataSourceAutoConfiguration.class, 
				JdbcTemplateAutoConfiguration.class })
public class ObservatoryStarApp {

	/**
	 * @desc 启动应用程序入口
	 * @param args  
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(ObservatoryStarApp.class).bannerMode(Mode.CONSOLE).web(true).run(args);
	}

}
