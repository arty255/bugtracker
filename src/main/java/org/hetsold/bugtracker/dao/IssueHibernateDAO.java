package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.Issue_;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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

    /*
    hibernate.query
    * default Query<R> applyFetchGraph(RootGraph graph) {
		return applyGraph( graph, GraphSemantic.FETCH );
	}
    * */

    @Override
    public List<Issue> getIssueList(Issue issue, int startPosition, int limit) {
        return hibernateTemplate.execute(session -> {
            EntityGraph issueEntityGraph = session.getEntityGraph("IssueEntityGraphToShortView");
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = builder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            Predicate[] predicates = getPredicates(issue, root, builder);
            if (predicates.length > 0) {
                query.where(predicates);
            }
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            typedQuery.setHint("javax.persistence.loadgraph", issueEntityGraph);
            return typedQuery.setFirstResult(startPosition).setMaxResults(limit).getResultList();
        });
    }

    @Override
    public void delete(Issue issue) {
        hibernateTemplate.delete(issue);
    }

    @Override
    public long getIssueCount(Issue issue) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Issue> root = query.from(Issue.class);
            Predicate[] predicates = getPredicates(issue, root, criteriaBuilder);
            if (predicates.length > 0) {
                query.where(predicates);
            }
            query.select(criteriaBuilder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        if (count != null) {
            return count;
        }
        return 0;
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
                query.where(predicates);
            }
            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public Issue getIssueToDetailedViewById(String uuid) {
        return hibernateTemplate.execute(session -> {
            EntityGraph issueEntityGraph = session.getEntityGraph("IssueEntityGraphToDetailedView");
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> query = criteriaBuilder.createQuery(Issue.class);
            Root<Issue> root = query.from(Issue.class);
            query.where(criteriaBuilder.equal(root.get("uuid"), uuid));
            TypedQuery<Issue> typedQuery = session.createQuery(query);
            typedQuery.setHint("javax.persistence.loadgraph", issueEntityGraph);
            return typedQuery.getSingleResult();
        });
    }

    private Predicate[] getPredicates(Issue issue, Root<Issue> root, CriteriaBuilder criteriaBuilder) {
        if (issue == null) {
            return new Predicate[0];
        }
        List<Predicate> predicateList = new ArrayList<>();
        if (issue.getCurrentIssueState() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.currentIssueState), issue.getCurrentIssueState()));
        }
        if (issue.getDescription() != null) {
            predicateList.add(criteriaBuilder.like(root.get(Issue_.description), issue.getDescription()));
        }
        if (issue.getCreationTime() != null) {
            predicateList.add(criteriaBuilder.lessThan(root.get(Issue_.creationTime), issue.getCreationTime()));
        }
        if (issue.getSeverity() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.severity), issue.getSeverity()));
        }
        if (issue.getReportedBy() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.reportedBy), issue.getReportedBy()));
        }
        if (issue.getArchived() != null) {
            predicateList.add(criteriaBuilder.equal(root.get(Issue_.archived), issue.getArchived()));
        }
        return predicateList.toArray(new Predicate[0]);
    }
}
