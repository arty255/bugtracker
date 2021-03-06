package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.Ticket_;
import org.hetsold.bugtracker.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
public class TicketHibernateDAO implements TicketDAO {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public TicketHibernateDAO(final SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(final Ticket ticket) {
        hibernateTemplate.save(ticket);
    }

    @Override
    public Ticket getTicketById(final UUID uuid) {
        return hibernateTemplate.get(Ticket.class, uuid);
    }

    @Override
    public void delete(final Ticket ticket) {
        hibernateTemplate.delete(ticket);
    }

    @Override
    public List<Ticket> getTicketListReportedByUser(final User user, final Contract contract, final int startPosition, final int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(
                    builder.and(
                            builder.equal(root.get(Ticket_.createdBy), user)),
                    builder.and(ContractReader.readContract(contract, root, builder)
                    )
            );
            query.select(root);
            query.orderBy(ContractReader.getOrders(contract, root, builder));
            return session.createQuery(query).setFirstResult(startPosition).setMaxResults(limit).list();
        });
    }

    @Override
    public long getTicketsCountReportedByUser(final User user, final Contract contract) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(
                    builder.and(
                            builder.equal(root.get(Ticket_.createdBy), user)),
                    builder.and(ContractReader.readContract(contract, root, builder)
                    )
            );
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public List<Ticket> getTickets(final Contract contract, final int startPosition, final int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.select(root);
            query.orderBy(ContractReader.getOrders(contract, root, builder));
            return session.createQuery(query).setFirstResult(startPosition).setMaxResults(limit).list();
        });
    }

    @Override
    public long getTicketsCount(final Contract contract) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }
}
