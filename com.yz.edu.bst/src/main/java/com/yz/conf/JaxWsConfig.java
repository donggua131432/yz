package com.yz.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter;
import com.yz.webservice.YzWebServiceBean;

@Configuration
public class JaxWsConfig {

	@Bean(name = "YzWebServiceBean")
    public static YzWebServiceBean getWebserviceBean() {
        return new YzWebServiceBean();
    }
	
	@Bean(name = "simpleJaxWsServiceExporter")
	public SimpleJaxWsServiceExporter simpleJaxWsServiceExporter(@Autowired YzWebServiceBean bean) {
		SimpleJaxWsServiceExporter factory = new SimpleJaxWsServiceExporter();
		factory.setBaseAddress(bean.getUrl());
		return factory;
	}


}
