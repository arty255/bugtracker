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
import java.util.UUID;

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
    public IssueMessageEvent getMessageEventById(UUID uuid) {
        return hibernateTemplate.get(IssueMessageEvent.class, uuid);
    }

    @Override
    public IssueMessageEvent getMessageEventByMessage(Message message) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<IssueMessageEvent> query = builder.createQuery(IssueMessageEvent.class);
            Root<IssueMessageEvent> root = query.from(IssueMessageEvent.class);
            query.where(builder.equal(root.get(IssueMessageEvent_.message), message));
            query.select(root);
            return session.createQuery(query).getSingleResult();
        });
    }

    @Override
    public void saveStateChange(IssueStateChangeEvent stateChangeEvent) {
        hibernateTemplate.save(stateChangeEvent);
    }

    @Override
    public IssueStateChangeEvent getStateChangeEventById(UUID uuid) {
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
            query.where(builder.equal(root.get(IssueEvent_.issue), issue));
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public IssueState getPreviousOpenOrReopenStateForIssue(Issue issue) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<IssueState> query = builder.createQuery(IssueState.class);
            Root<IssueStateChangeEvent> root = query.from(IssueStateChangeEvent.class);
            query.where(builder.or(
                    builder.equal(root.get(IssueStateChangeEvent_.issueState), IssueState.OPEN),
                    builder.equal(root.get(IssueStateChangeEvent_.issueState), IssueState.REOPEN)
            ));
            query.select(root.get(IssueStateChangeEvent_.issueState));
            query.orderBy(builder.desc(root.get(IssueEvent_.eventDate)));
            return session.createQuery(query).setMaxResults(1).list().stream().findFirst().orElse(IssueState.OPEN);
        });
    }

    @Override
    public List<IssueEvent> getHistoryIssueEventsByIssue(Issue issue, int firstResult, int limit, boolean inverseDateOrder) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit cannot be negative");
        }
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<IssueEvent> query = criteriaBuilder.createQuery(IssueEvent.class);
            Root<IssueEvent> root = query.from(IssueEvent.class);
            query.where(criteriaBuilder.equal(root.get(IssueEvent_.issue), issue));
            query.select(root);
            if (inverseDateOrder) {
                query.orderBy(criteriaBuilder.asc(root.get(IssueEvent_.eventDate)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(IssueEvent_.eventDate)));
            }
            return session.createQuery(query).setFirstResult(firstResult).setMaxResults(limit).list();
        });
    }


}
