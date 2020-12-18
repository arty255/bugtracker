package org.hetsold.bugtracker;

import org.hetsold.bugtracker.dao.*;
import org.hetsold.bugtracker.service.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {"org.hetsold.bugtracker.rest"})
public class AppConfig {
    @Bean()
    @Primary
    @Profile("prod")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/h_bugtracker");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("OnHG8^nnP");
        return dataSource;
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.hetsold.bugtracker.model");
        sessionFactory.setHibernateProperties(getAdditionalHibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public IssueDAO getIssueDAO(SessionFactory sessionFactory) {
        return new IssueHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public UserDAO getUserDAO(SessionFactory sessionFactory) {
        return new UserHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public HistoryEventDAO getHistoryEventDAO(SessionFactory sessionFactory) {
        return new HistoryEventHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public MessageDAO getMessageDAO(SessionFactory sessionFactory) {
        return new MessageHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public TicketDAO getTicketDAO(SessionFactory sessionFactory) {
        return new TicketHibernateDAO(sessionFactory);
    }

    @Bean
    @Autowired
    public UserService getUserService(UserDAO userDAO) {
        return new DefaultUserService(userDAO);
    }

    @Bean
    @Autowired
    public IssueService getIssueService(IssueDAO issueDAO, UserDAO userDAO, HistoryEventDAO historyEventDAO, MessageDAO messageDao) {
        return new DefaultIssueService(issueDAO, userDAO, historyEventDAO, messageDao);
    }

    @Bean
    @Autowired
    public TicketService getTicketService(TicketDAO ticketDAO, UserDAO userDAO) {
        return new DefaultTicketService(ticketDAO, userDAO);
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