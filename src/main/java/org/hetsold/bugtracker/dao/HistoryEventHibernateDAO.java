package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class HistoryEventHibernateDAO implements HistoryEventDAO {
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public HistoryEventHibernateDAO(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void saveIssueMessage(IssueMessageEvent messageEvent) {
        hibernateTemplate.save(messageEvent);
    }

    @Override
    public List<IssueMessageEvent> listAllMessageEvents() {
        return hibernateTemplate.loadAll(IssueMessageEvent.class);
    }

    @Override
    public IssueMessageEvent getMessageEventById(String uuid) {
        return hibernateTemplate.get(IssueMessageEvent.class, uuid);
    }

    @Override
    public void saveStateChange(IssueStateChangeEvent stateChangeEvent) {
        hibernateTemplate.save(stateChangeEvent);
    }

    @Override
    public IssueStateChangeEvent getStateChangeEventById(String uuid) {
        return hibernateTemplate.load(IssueStateChangeEvent.class, uuid);
    }

    @Override
    public void deleteIssueMessageEvent(IssueMessageEvent messageEvent) {
        hibernateTemplate.delete(messageEvent);
    }

    @Override
    public long getHistoryIssueEventsCountForIssue(Issue issue) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<IssueEvent> root = query.from(IssueEvent.class);
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        if (count != null) {
            return count;
        }
        return 0;
    }

    @Override
    public List<IssueEvent> getHistoryIssueEventsByIssue(Issue issue, int firstResult, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit cannot be negative");
        }
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<IssueEvent> query = criteriaBuilder.createQuery(IssueEvent.class);
            Root<IssueEvent> root = query.from(IssueEvent.class);
            query.where(criteriaBuilder.equal(root.get(IssueEvent_.issue), issue));
            query.select(root);
            query.orderBy(criteriaBuilder.asc(root.get(IssueEvent_.eventDate)));
            return session.createQuery(query).setFirstResult(firstResult).setMaxResults(limit).list();
        });
    }


}
