package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketHibernateDAO implements TicketDAO {
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public TicketHibernateDAO(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(Ticket ticket) {
        hibernateTemplate.save(ticket);
    }

    @Override
    public Ticket getTicketById(String uuid) {
        return hibernateTemplate.get(Ticket.class, uuid);
    }

    @Override
    public List<Ticket> loadAll() {
        return hibernateTemplate.loadAll(Ticket.class);
    }

    @Override
    public void delete(Ticket ticket) {
        hibernateTemplate.delete(ticket);
    }
}
