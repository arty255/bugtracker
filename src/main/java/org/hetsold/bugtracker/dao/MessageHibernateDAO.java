package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
public class MessageHibernateDAO implements MessageDAO {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public MessageHibernateDAO(final SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(final Message message) {
        hibernateTemplate.save(message);
    }

    @Override
    public Message getMessageById(final UUID uuid) {
        return hibernateTemplate.get(Message.class, uuid);
    }

    @Override
    public void delete(final Message message) {
        hibernateTemplate.delete(message);
    }

    @Override
    public List<Message> loadAll() {
        return hibernateTemplate.loadAll(Message.class);
    }

    @Override
    public long getMessageCountByTicket(final Ticket ticket) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Join<Ticket, Message> messageJoin = getTicketMessageJoinByUUID(builder, ticket, countQuery);
            countQuery.select(builder.count(messageJoin));
            return session.createQuery(countQuery).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public List<Message> getMessageListByTicket(final Ticket ticket, final int fromIndex, final int limit, final boolean inverseDateOrder) {
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

    private Join<Ticket, Message> getTicketMessageJoinByUUID(final CriteriaBuilder builder, final Ticket ticket, final CriteriaQuery<?> query) {
        CriteriaQuery<Ticket> ticketQuery = builder.createQuery(Ticket.class);
        Root<Ticket> ticketRoot = query.from(Ticket.class);
        ticketQuery.where(builder.equal(ticketRoot.get(AbstractEntity_.uuid), ticket.getUuid()));
        return ticketRoot.join(Ticket_.messageList);
    }
}