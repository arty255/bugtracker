package org.hetsold.bugtracker;

import org.hetsold.bugtracker.facade.IssueFacade;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestAppConfig {
    @Bean()
    @Profile("test")
    public DataSource getDataSourceBean() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/h_bugtracker_test");
        dataSource.setUsername("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setPassword("OnHG8^nnP");
        return dataSource;
    }
}