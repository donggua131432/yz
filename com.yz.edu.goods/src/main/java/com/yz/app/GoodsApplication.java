package com.yz.app;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(
	scanBasePackages = {
		"com.yz.conf",
		"com.yz.service",
		"com.yz.redis",
		"com.yz.dao",
		"com.yz.datasource",
		"com.yz.convert",
		"com.yz.dubbo",
		"com.yz.cache",
		"com.yz.mq"
	},
	exclude = {
		WebSocketAutoConfiguration.class,
		RedisAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class
	}
)
public class GoodsApplication {
	/**
	 * @desc 商品订单 程序入口
	 * @param args 
	 */
	public static void main(String[] args)throws Exception {
		new SpringApplicationBuilder(GoodsApplication.class).bannerMode(Banner.Mode.CONSOLE).run(args);
	}
}
