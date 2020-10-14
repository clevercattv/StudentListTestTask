package com.clevercattv.student.list.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@Configuration
@ComponentScan({"com.clevercattv.student.list.controller",
        "com.clevercattv.student.list.service", "com.clevercattv.student.list.exception"})
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Bean
    public HttpHandler httpHandler(ApplicationContext context) {
        return WebHttpHandlerBuilder.applicationContext(context).build();
    }

}
