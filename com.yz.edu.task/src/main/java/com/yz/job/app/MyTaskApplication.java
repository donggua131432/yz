package com.yz.job.app;

 
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
 
 
@SpringBootApplication(scanBasePackages = {
		"com.yz.dubbo",  
		"com.yz.conf",
		"com.yz.job.service",
		"com.yz.redis",
		"com.yz.job.dao", 
		"com.yz.convert", 
		"com.yz.job.register",
		"com.yz.job.task", 
		"com.yz.job.conf",
		"com.yz.job.sub",
		"com.yz.job.handler",
		"com.yz.sub",
		"com.yz.cache",
		"com.yz.job.listener",
		"com.yz.job.disruptor",
		"com.yz.mq", 
}, exclude = { WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class, DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class })
public class MyTaskApplication {

	/**
	 * @desc task 启动类
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		new SpringApplicationBuilder(MyTaskApplication.class).bannerMode(Mode.CONSOLE).run(args);
	}

}
