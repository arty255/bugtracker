package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.metadata.Ticket_;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public void delete(Ticket ticket) {
        hibernateTemplate.delete(ticket);
    }

    @Override
    public List<Ticket> getTicketListReportedByUser(User user, Contract contract, int startPosition, int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(
                    builder.and(
                            builder.equal(root.get(Ticket_.CREATED_BY_NAME), user)),
                    builder.and(ContractReader.readContract(contract, root, builder)
                    )
            );
            query.select(root);
            return session.createQuery(query).setFirstResult(startPosition).setMaxResults(limit).list();
        });
    }

    @Override
    public long getTicketCountReportedByUser(User user, Contract contract) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(
                    builder.and(
                            builder.equal(root.get(Ticket_.CREATED_BY_NAME), user)),
                    builder.and(ContractReader.readContract(contract, root, builder)
                    )
            );
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public List<Ticket> getTickets(Contract contract, int startPosition, int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.select(root);
            return session.createQuery(query).setFirstResult(startPosition).setMaxResults(limit).list();
        });
    }

    @Override
    public long getTicketsCount(Contract contract) {
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
