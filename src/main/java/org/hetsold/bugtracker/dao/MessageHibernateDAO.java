package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class MessageHibernateDAO implements MessageDAO {
    private HibernateTemplate hibernateTemplate;

    public MessageHibernateDAO(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(Message message) {
        hibernateTemplate.save(message);
    }

    @Override
    public Message getMessageById(String uuid) {
        return hibernateTemplate.load(Message.class, uuid);
    }
}
