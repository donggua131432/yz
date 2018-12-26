package com.yz.job.conf;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig; 
import com.alibaba.dubbo.config.ProtocolConfig; 
import com.alibaba.dubbo.config.RegistryConfig;
 
import com.yz.dubbo.YzDubboBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Autowired
    private YzDubboBean dubboBean;


    @Bean(name = "yzDubboBean")
    public static YzDubboBean initDubboBean() {
        return new YzDubboBean();
    }

    @Bean(name = "registryConfig")
    public RegistryConfig getRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(dubboBean.getRegistryAddress());
        registryConfig.setProtocol(dubboBean.getRegistryProtocol());
        registryConfig.setCheck(false);
        registryConfig.setDynamic(true);
        return registryConfig;
    }


    @Bean(name = "applicationConfig")
    public ApplicationConfig getApplicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setOwner(dubboBean.getOwner());
        applicationConfig.setName(dubboBean.getApplication());
        applicationConfig.setDefault(true);
        return applicationConfig;
    }


    @Bean(name = "protocolConfig")
    public ProtocolConfig getProtocolConfig() {
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName(dubboBean.getProtocolName());
        protocol.setPort(dubboBean.getPort());
        protocol.setDispatcher(dubboBean.getDispatcher());
        protocol.setThreadpool(dubboBean.getThreadpool());
        protocol.setThreads(dubboBean.getThreads());
        protocol.setDefault(true);
        protocol.setKeepAlive(true);
        return protocol;
    }

    @Bean(name = "consumerConfig")
    public ConsumerConfig getConsumerConfig() {
        ConsumerConfig consumer = new ConsumerConfig();
        consumer.setCheck(false);
        consumer.setGeneric(false);
        consumer.setInjvm(false);
        consumer.setTimeout(300);
        consumer.setCluster(dubboBean.getCluster());
        consumer.setLoadbalance(dubboBean.getLoadbalance());
        consumer.setRetries(0);
        return consumer;
    }

}
