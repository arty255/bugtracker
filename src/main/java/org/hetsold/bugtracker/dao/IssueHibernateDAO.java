package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.Issue_;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
        if (count != null) {
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public Issue getIssueById(String uuid) {
        return hibernateTemplate.get(Issue.class, uuid);
    }

    @Override
    public List<Issue> getIssueByCriteria(Issue issue) {
        return hibernateTemplate.execute(session -> {
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
            return session.createQuery(query).getResultList();
        });
    }

    private Predicate[] getPredicates(Issue issue, Root<Issue> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();
        if (issue.getCurrentState() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.currentState), issue.getCurrentState()));
        }
        if (issue.getShortDescription() != null && !issue.getShortDescription().isEmpty()) {
            predicateList.add(criteriaBuilder.like(root.get(Issue_.shortDescription), issue.getShortDescription()));
        }
        if (issue.getTicketCreationTime() != null) {
            predicateList.add(criteriaBuilder.lessThan(root.get(Issue_.ticketCreationTime), issue.getTicketCreationTime()));
        }
        if (issue.getIssueAppearanceTime() != null) {
            predicateList.add(criteriaBuilder.lessThan(root.get(Issue_.issueAppearanceTime), issue.getIssueAppearanceTime()));
        }
        if (issue.getReportedBy() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.reportedBy), issue.getReportedBy()));
        }
        return predicateList.toArray(new Predicate[0]);
    }
}
