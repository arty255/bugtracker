package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.Issue_;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IssueHibernateDAO implements IssueDAO {
    private HibernateTemplate hibernateTemplate;

    public IssueHibernateDAO(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(Issue issue) {
        hibernateTemplate.save(issue);
    }

    @Override
    public List<Issue> listAll() {
        return hibernateTemplate.loadAll(Issue.class);
    }

    @Override
    public void delete(Issue issue) {
        hibernateTemplate.delete(issue);
    }

    @Override
    public long getIssueCount() {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Issue> root = query.from(Issue.class);
            query.select(criteriaBuilder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public Issue getIssueById(String uuid) {
        return hibernateTemplate.get(Issue.class, uuid);
    }

    @Override
    public List<Issue> getIssueByCriteria(Issue issue) {
        return hibernateTemplate.execute(session -> {
            EntityGraph<Issue> issueEntityGraph = session.createEntityGraph(Issue.class);
            issueEntityGraph.addAttributeNodes("uuid");
            issueEntityGraph.addSubgraph("reportedBy").addAttributeNodes("firstName", "lastName");
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = criteriaBuilder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            query.select(root);
            Predicate[] predicates = getPredicates(issue, root, criteriaBuilder);
            if (predicates.length > 1) {
                query.where(criteriaBuilder.and(predicates));
            } else {
                query.where(predicates);
            }
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            typedQuery.setHint("javax.persistence.fetchgraph", issueEntityGraph);
            return typedQuery.getResultList();
        });
    }

    @Override
    public Issue getIssueToDetailedViewById(String uuid) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = criteriaBuilder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            query.where(criteriaBuilder.equal(root.get("uuid"), uuid));
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            return typedQuery.getSingleResult();
        });
    }

    private Predicate[] getPredicates(Issue issue, Root<Issue> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();
        if (issue != null) {
            if (issue.getCurrentState() != null) {
                predicateList.add(criteriaBuilder.equal(root.get(Issue_.currentState), issue.getCurrentState()));
            }
            if (issue.getDescription() != null) {
                predicateList.add(criteriaBuilder.like(root.get(Issue_.description), issue.getDescription()));
            }
            if (issue.getCreationTime() != null) {
                predicateList.add(criteriaBuilder.lessThan(root.get(Issue_.creationTime), issue.getCreationTime()));
            }
            if (issue.getReportedBy() != null) {
                predicateList.add(criteriaBuilder.equal(root.get(Issue_.reportedBy), issue.getReportedBy()));
            }
        }
        return predicateList.toArray(new Predicate[0]);
    }
}
