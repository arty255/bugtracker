package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Message_;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.Ticket_;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
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

    @Override
    public long getMessageCountByTicket(Ticket ticket) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Join<Ticket, Message> messageJoin = getTicketMessageJoinByUUID(builder, ticket, countQuery);
            countQuery.select(builder.count(messageJoin));
            return session.createQuery(countQuery).getSingleResult();
        });
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public List<Message> getMessageListByTicket(Ticket ticket, int fromIndex, int limit, boolean inverseDateOrder) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Message> messageQuery = builder.createQuery(Message.class);
            Join<Ticket, Message> messageJoin = getTicketMessageJoinByUUID(builder, ticket, messageQuery);
            messageQuery.select(messageJoin);
            if (inverseDateOrder) {
                messageQuery.orderBy(builder.desc(messageJoin.get(Message_.createDate)));
            } else {
                messageQuery.orderBy(builder.asc(messageJoin.get(Message_.createDate)));
            }
            return session.createQuery(messageQuery).setFirstResult(0).setMaxResults(limit).list();
        });
    }

    private Join<Ticket, Message> getTicketMessageJoinByUUID(CriteriaBuilder builder, Ticket ticket, CriteriaQuery<?> query) {
        CriteriaQuery<Ticket> ticketQuery = builder.createQuery(Ticket.class);
        Root<Ticket> ticketRoot = query.from(Ticket.class);
        ticketQuery.where(builder.equal(ticketRoot.get(Ticket_.uuid), ticket.getUuid()));
        return ticketRoot.join(Ticket_.messageList);
    }
}
