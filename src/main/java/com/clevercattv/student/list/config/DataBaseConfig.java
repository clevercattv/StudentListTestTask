package com.clevercattv.student.list.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.Objects;

@Configuration
@PropertySource("classpath:database.properties")
@EnableR2dbcRepositories("com.clevercattv.student.list.repository")
@RequiredArgsConstructor
public class DataBaseConfig extends AbstractR2dbcConfiguration {

    private final Environment environment;

    @Bean
    @Override
    public H2ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url(Objects.requireNonNull(environment.getProperty("h2.url")))
                        .username(Objects.requireNonNull(environment.getProperty("h2.username")))
                        .build()
        );
    }

}
