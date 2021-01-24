package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface HistoryEventDAO {
    void saveIssueMessage(IssueMessageEvent messageEvent);

    List<IssueMessageEvent> listAllMessageEvents();

    IssueMessageEvent getMessageEventById(String uuid);

    IssueMessageEvent getMessageEventByMessage(Message message);

    void saveStateChange(IssueStateChangeEvent stateChangeEvent);

    IssueStateChangeEvent getStateChangeEventById(String uuid);

    void deleteIssueMessageEvent(IssueMessageEvent messageEvent);

    List<IssueEvent> getHistoryIssueEventsByIssue(Issue issue, int firstResult, int limit);

    long getHistoryIssueEventsCountForIssue(Issue issue);
}