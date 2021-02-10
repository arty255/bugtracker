package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.*;

import java.util.List;
import java.util.UUID;

public interface HistoryEventDAO {
    void saveIssueMessage(IssueMessageEvent messageEvent);

    List<IssueMessageEvent> listAllMessageEvents();

    IssueMessageEvent getMessageEventById(UUID uuid);

    IssueMessageEvent getMessageEventByMessage(Message message);

    void saveStateChange(IssueStateChangeEvent stateChangeEvent);

    IssueStateChangeEvent getStateChangeEventById(UUID uuid);

    void deleteIssueMessageEvent(IssueMessageEvent messageEvent);

    List<IssueEvent> getHistoryIssueEventsByIssue(Issue issue, int firstResult, int limit, boolean inverseDateOrder);

    long getHistoryIssueEventsCountForIssue(Issue issue);

    IssueState getPreviousOpenOrReopenStateForIssue(Issue issue);
}