package com.yz.app;

 

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.yz.pay.YzPayCertService; 

@SpringBootApplication(scanBasePackages = {
		"com.yz.conf", 
		"com.yz.service",
		"com.yz.redis",
		"com.yz.dao",   
		"com.yz.datasource",   
		"com.yz.convert",
		"com.yz.dubbo",
		"com.yz.pay" ,
		"com.yz.cache",
		"com.yz.mq",
		"com.yz.trace.aspect"
}, exclude = { WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class })
public class MyUserApplication {
	
	/** 
	 * {@link YzPayCertService}
	 * @desc 用户启动类
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		new SpringApplicationBuilder(MyUserApplication.class).bannerMode(Mode.CONSOLE).run(args);
		
	}
}
