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

@Repository
public class HistoryEventHibernateDAO implements HistoryEventDAO {
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public HistoryEventHibernateDAO(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void saveIssueMessage(HistoryIssueMessageEvent messageEvent) {
        hibernateTemplate.save(messageEvent);
    }

    @Override
    public List<HistoryIssueMessageEvent> listAllMessageEvents() {
        return hibernateTemplate.loadAll(HistoryIssueMessageEvent.class);
    }

    @Override
    public HistoryIssueMessageEvent getMessageEventById(String uuid) {
        return hibernateTemplate.get(HistoryIssueMessageEvent.class, uuid);
    }

    @Override
    public void saveStateChange(HistoryIssueStateChangeEvent stateChangeEvent) {
        hibernateTemplate.save(stateChangeEvent);
    }

    @Override
    public HistoryIssueStateChangeEvent getStateChangeEventById(String uuid) {
        return hibernateTemplate.load(HistoryIssueStateChangeEvent.class, uuid);
    }

    @Override
    public void deleteIssueMessageEvent(HistoryIssueMessageEvent messageEvent) {
        hibernateTemplate.delete(messageEvent);
    }

    @Override
    public HistoryIssueMessageEvent getHistoryIssueMessageEventByMessage(Message message) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<HistoryIssueMessageEvent> query = criteriaBuilder.createQuery(HistoryIssueMessageEvent.class);
            Root<HistoryIssueMessageEvent> root = query.from(HistoryIssueMessageEvent.class);
            Join<HistoryIssueMessageEvent, Message> joinRoot = root.join(HistoryIssueMessageEvent_.message);
            query.where(criteriaBuilder.equal(joinRoot.get(Message_.uuid), message.getUuid()));
            query.select(root);
            return session.createQuery(query).getSingleResult();
        });
    }


}
