package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class TicketHibernateDAO implements TicketDAO {
    private HibernateTemplate hibernateTemplate;

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
}
