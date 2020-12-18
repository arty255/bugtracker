package org.hetsold.bugtracker;

import org.hetsold.bugtracker.dao.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
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

    @Bean
    @Primary
    @Profile("mock")
    public HistoryEventDAO getHistoryEventMockDAO() {
        return Mockito.mock(HistoryEventHibernateDAO.class);
    }

    @Bean
    @Primary
    @Profile("mock")
    public MessageDAO getMessageMockDAO() {
        return Mockito.mock(MessageHibernateDAO.class);
    }

    @Bean
    @Primary
    @Profile("mock")
    public TicketDAO ticketHibernateDao() {
        return Mockito.mock(TicketHibernateDAO.class);
    }
}