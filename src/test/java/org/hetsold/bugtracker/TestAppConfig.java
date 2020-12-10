package org.hetsold.bugtracker;

import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.IssueHibernateDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.dao.UserHibernateDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestAppConfig {
    @Bean(name = "MySQLTest")
    @Profile("test")
    public DataSource getDataSourceBean() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/h_bugtracker_test");
        dataSource.setUsername("root");
        dataSource.setPassword("OnHG8^nnP");
        return dataSource;
    }

    @Bean
    @Primary
    @Profile("mock")
    public UserDAO getUserDAOMockBean() {
        return Mockito.mock(UserHibernateDAO.class);
    }

    @Bean
    @Primary
    @Profile("mock")
    public IssueDAO gIssueDAOMockBean() {
        return Mockito.mock(IssueHibernateDAO.class);
    }
}