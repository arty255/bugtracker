package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.HistoryIssueMessageEvent;
import org.hetsold.bugtracker.model.HistoryIssueStateChangeEvent;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;

public class HistoryEventHibernateDAO implements HistoryEventDAO {
    private HibernateTemplate hibernateTemplate;

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
        return hibernateTemplate.load(HistoryIssueMessageEvent.class, uuid);
    }

    @Override
    public void saveStateChange(HistoryIssueStateChangeEvent stateChangeEvent) {
        hibernateTemplate.save(stateChangeEvent);
    }

    @Override
    public HistoryIssueStateChangeEvent getStateChangeEventById(String uuid) {
        return hibernateTemplate.load(HistoryIssueStateChangeEvent.class, uuid);
    }
}
