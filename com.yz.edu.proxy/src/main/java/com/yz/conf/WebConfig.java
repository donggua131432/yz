package com.yz.conf;

import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.yz.core.handler.IHandlerAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import(value = { IHandlerAutoConfiguration.class })
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	@SuppressWarnings("rawtypes")
	public HttpMessageConverters httpMessageConverter() {
		List<HttpMessageConverter<?>> l = new ArrayList<HttpMessageConverter<?>>();

		l.add(new MappingJackson2HttpMessageConverter());
		l.add(new ByteArrayHttpMessageConverter());
		l.add(new SourceHttpMessageConverter());

		l.add(formHttpMessageConverter());
		l.add(new StringHttpMessageConverter());
		l.add(new AllEncompassingFormHttpMessageConverter());
		l.add(new MappingJackson2XmlHttpMessageConverter());
		HttpMessageConverters converters = new HttpMessageConverters(l);
		return converters;
	}

	/**
	 * 
	 * @return
	 */
	private FormHttpMessageConverter formHttpMessageConverter() {
		List<MediaType> types = new ArrayList<>();

		types.add(MediaType.APPLICATION_FORM_URLENCODED);
		types.add(MediaType.MULTIPART_FORM_DATA);
		types.add(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"));

		FormHttpMessageConverter fhmc = new FormHttpMessageConverter();
		fhmc.setSupportedMediaTypes(types);

		return fhmc;
	}

	/**
	 * 跨域处理
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedHeaders("Content-Type", "Accept", "X-Requested-With", "remember-me", "auth")
				.exposedHeaders("auth").allowedMethods("POST", "GET", "DELETE", "OPTIONS").allowCredentials(true)
				.maxAge(3600);
		super.addCorsMappings(registry);
	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		JettyEmbeddedServletContainerFactory factory =
				new JettyEmbeddedServletContainerFactory();
		factory.setSelectors(64);
		factory.setAcceptors(256);
		factory.setThreadPool(new ExecutorThreadPool());
		factory.setContextPath("/bcc");
		return factory;
	}

}
