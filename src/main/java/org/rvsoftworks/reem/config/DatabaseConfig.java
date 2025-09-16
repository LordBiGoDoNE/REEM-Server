package org.rvsoftworks.reem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Value("${DATABASE_IP:localhost}")
    private String ip;

    @Value("${DATABASE_USUARIO:postgres}")
    private String user;

    @Value("${DATABASE_SENHA:179324865}")
    private String password;

    @Value("${DATABASE_PORTA:5432}")
    private String port;

    @Value("${DATABASE_NOME:reem}")
    private String name;

    @Value("${SERVICE_NAME:REEM-SERVER}")
    private String serviceName;

    public String getUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s?ApplicationName=%s", ip, port, name, serviceName);
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(getUrl());
        dataSourceBuilder.username(user);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
}