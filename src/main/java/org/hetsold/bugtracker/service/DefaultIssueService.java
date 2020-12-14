package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.HistoryIssueStateChangeEvent;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public class DefaultIssueService implements IssueService {
    private IssueDAO issueDAO;
    private UserDAO userDAO;
    private HistoryEventDAO historyEventDAO;


    public DefaultIssueService(IssueDAO issueDAO, UserDAO userDAO, HistoryEventDAO historyEventDAO) {
        this.issueDAO = issueDAO;
        this.userDAO = userDAO;
        this.historyEventDAO = historyEventDAO;
    }

    @Override
    public void save(Issue issue) {
        if (issue.getIssueAppearanceTime() == null || issue.getIssueAppearanceTime().getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("wrong issueAppearanceTime");
        }
        if (issue.getTicketCreationTime() == null || issue.getTicketCreationTime().getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("wrong ticketCreationTime");
        }
        if (issue.getReportedBy() == null || userDAO.getUserById(issue.getReportedBy().getUuid()) == null) {
            throw new IllegalArgumentException("wrong issueReporter");
        }
        if (issue.getShortDescription().isEmpty() || issue.getFullDescription().isEmpty()) {
            throw new IllegalArgumentException("issue description and short description is null");
        }
        issueDAO.save(issue);
    }

    @Override
    public Issue getIssueById(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("uuid cannot be empty");
        }
        return issueDAO.getIssueById(uuid);
    }

    @Override
    public void deleteIssue(Issue issue) {
        if (issue == null || issue.getUuid().isEmpty()) {
            throw new IllegalArgumentException("issue and uuid cannot be empty");
        }
        issueDAO.delete(issue);
    }

    @Override
    public List<Issue> findIssueByCriteria(Issue issue) {
        return issueDAO.getIssueByCriteria(issue);
    }

    @Override
    public Issue getIssueForViewById(String uuid) {
        return issueDAO.getIssueToDetailedViewById(uuid);
    }

    @Override
    public void changeIssueState(Issue issue, State newState, User user) {
        if (issue == null || issueDAO.getIssueById(issue.getUuid()) == null) {
            throw new IllegalArgumentException("issue not exists");
        }
        if (newState == null) {
            throw new IllegalArgumentException("incompatible newState");
        }
        if (user == null || (user = userDAO.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("non existed user");
        }
        if (newState == State.ASSIGNED && (issue.getAssignedTo() == null || userDAO.getUserById(issue.getAssignedTo().getUuid()) == null)) {
            throw new IllegalArgumentException("issue can be assigned to existed user");
        }
        //get current user;
        //todo: change after spring security integration
        issue = issueDAO.getIssueById(issue.getUuid());
        issue.setCurrentState(newState);
        HistoryIssueStateChangeEvent event = new HistoryIssueStateChangeEvent();
        event.setEventDate(new Date());
        event.setIssue(issue);
        event.setRedactor(user);
        event.setState(newState);
        issue.setCurrentState(newState);
        historyEventDAO.saveStateChange(event);
        issueDAO.save(issue);
    }
}
