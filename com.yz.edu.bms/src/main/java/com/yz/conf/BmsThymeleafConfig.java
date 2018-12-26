package com.yz.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine; 
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

 

@Configuration
public class BmsThymeleafConfig {

	@Bean(name="templateResolver")
	public ClassLoaderTemplateResolver servletContextTemplateResolver() {
		ClassLoaderTemplateResolver resolver =  new ClassLoaderTemplateResolver();
		//resolver.setResourceResolver(new BmsThymeleafResolver());
		resolver.setCacheable(false);
		resolver.setSuffix(".html");
		resolver.setPrefix("template/");
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8"); 
		return resolver;
	}
	
	@Bean(name="templateEngine")
	public SpringTemplateEngine springTemplateEngine(@Autowired ClassLoaderTemplateResolver templateResolver)
	{
		SpringTemplateEngine templateEngine =new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}
	
	@Bean(name="thymeleafViewResolver")
	public ThymeleafViewResolver thymeleafViewResolver(@Autowired SpringTemplateEngine templateEngine)
	{
		ThymeleafViewResolver thymeleafViewResolver =new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine);
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}
	 
}
