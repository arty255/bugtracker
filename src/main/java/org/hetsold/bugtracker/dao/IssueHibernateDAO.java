package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.AbstractEntity_;
import org.hetsold.bugtracker.model.Issue;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
public class IssueHibernateDAO implements IssueDAO {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public IssueHibernateDAO(final SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(final Issue issue) {
        hibernateTemplate.save(issue);
    }

    @Override
    public List<Issue> getIssueList(final Contract contract, final int startPosition, final int limit) {
        return hibernateTemplate.execute(session -> {
            EntityGraph issueEntityGraph = session.getEntityGraph("IssueEntityGraphToShortView");
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = builder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.orderBy(ContractReader.getOrders(contract, root, builder));
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            typedQuery.setHint("javax.persistence.loadgraph", issueEntityGraph);
            return typedQuery.setFirstResult(startPosition).setMaxResults(limit).getResultList();
        });
    }

    @Override
    public void delete(final Issue issue) {
        hibernateTemplate.delete(issue);
    }

    @Override
    public long getIssueCount(final Contract contract) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Issue> root = query.from(Issue.class);
            query.where(ContractReader.readContract(contract, root, criteriaBuilder));
            query.select(criteriaBuilder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public Issue getIssueByUUID(final UUID uuid) {
        return hibernateTemplate.get(Issue.class, uuid);
    }

    @Override
    public Issue getIssueToDetailedViewById(final UUID uuid) {
        return hibernateTemplate.execute(session -> {
            EntityGraph issueEntityGraph = session.getEntityGraph("IssueEntityGraphToDetailedView");
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = criteriaBuilder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            query.where(criteriaBuilder.equal(root.get(AbstractEntity_.uuid), uuid));
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            typedQuery.setHint("javax.persistence.loadgraph", issueEntityGraph);
            return typedQuery.getSingleResult();
        });
    }
}