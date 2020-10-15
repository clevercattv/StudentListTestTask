package com.clevercattv.student.list.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setValidationMessageSource(messageSource());
        return validatorFactory;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:validation/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
