package org.hetsold.bugtracker;

import org.hetsold.bugtracker.dao.*;
import org.hetsold.bugtracker.service.DefaultIssueService;
import org.hetsold.bugtracker.service.DefaultUserService;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class AppConfig {
    @Bean(name = "mysqlDataSource")
    @Profile("production")
    public DataSource getDataSourceBean() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/h_bugtracker");
        dataSource.setUsername("root");
        dataSource.setPassword("OnHG8^nnP");
        return dataSource;
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean getSessionFactoryBean(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.hetsold.bugtracker.model");
        sessionFactory.setHibernateProperties(getAdditionalHibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public IssueDAO getIssueDAOBean(SessionFactory sessionFactory) {
        return new IssueHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public UserDAO getUserDao(SessionFactory sessionFactory) {
        return new UserHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public HistoryEventDAO getHistoryEventDAOBean(SessionFactory sessionFactory) {
        return new HistoryEventHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public MessageDAO getMessageDAOBean(SessionFactory sessionFactory) {
        return new MessageHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public UserService getUserServiceBean(UserDAO userDAO) {
        return new DefaultUserService(userDAO);
    }

    @Bean
    @Autowired
    public IssueService getIssueServiceBean(IssueDAO issueDAO, UserDAO userDAO) {
        return new DefaultIssueService(issueDAO, userDAO);
    }

    @Bean
    public HibernateTransactionManager getDataSourceTransactionManagerBean(@Autowired SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    private Properties getAdditionalHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return properties;
    }
}