package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.HistoryIssueMessageEvent;
import org.hetsold.bugtracker.model.HistoryIssueStateChangeEvent;
import org.hetsold.bugtracker.model.Message;

import java.util.List;

public interface HistoryEventDAO {
    void saveIssueMessage(HistoryIssueMessageEvent messageEvent);

    List<HistoryIssueMessageEvent> listAllMessageEvents();

    HistoryIssueMessageEvent getMessageEventById(String uuid);

    void saveStateChange(HistoryIssueStateChangeEvent stateChangeEvent);

    HistoryIssueStateChangeEvent getStateChangeEventById(String uuid);

    void deleteIssueMessageEvent(HistoryIssueMessageEvent messageEvent);

    HistoryIssueMessageEvent getHistoryIssueMessageEventByMessage(Message message);
}
