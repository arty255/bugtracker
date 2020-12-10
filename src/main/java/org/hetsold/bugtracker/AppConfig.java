package org.hetsold.bugtracker;

import org.hetsold.bugtracker.dao.*;
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
    public HibernateTransactionManager getDataSourceTransactionManagerBean(@Autowired SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    private Properties getAdditionalHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return properties;
    }
}