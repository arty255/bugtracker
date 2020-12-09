package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
}
