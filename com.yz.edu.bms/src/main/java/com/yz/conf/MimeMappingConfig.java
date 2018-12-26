package com.yz.conf;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MimeMappingConfig implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        configurableEmbeddedServletContainer.setMimeMappings(mappings);
    }
}
