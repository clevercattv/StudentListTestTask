package com.clevercattv.student.list.config;

import org.springframework.web.server.adapter.AbstractReactiveWebInitializer;

public class WebInitializer extends AbstractReactiveWebInitializer {

    protected Class<?>[] getConfigClasses() {
        return new Class[]{DataBaseConfig.class, WebFluxConfig.class};
    }

}
