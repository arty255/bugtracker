package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageHibernateDAO implements MessageDAO {
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public MessageHibernateDAO(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(Message message) {
        hibernateTemplate.save(message);
    }

    @Override
    public Message getMessageById(String uuid) {
        return hibernateTemplate.get(Message.class, uuid);
    }

    @Override
    public void delete(Message message) {
        hibernateTemplate.delete(message);
    }

    @Override
    public List<Message> loadAll() {
        return hibernateTemplate.loadAll(Message.class);
    }
}
