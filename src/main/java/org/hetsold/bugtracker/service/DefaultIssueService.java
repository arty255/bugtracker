package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class DefaultIssueService implements IssueService {
    private IssueDAO issueDAO;
    private UserDAO userDAO;
    private HistoryEventDAO historyEventDAO;
    private MessageDAO messageDAO;

    public DefaultIssueService(IssueDAO issueDAO, UserDAO userDAO, HistoryEventDAO historyEventDAO, MessageDAO messageDAO) {
        this.issueDAO = issueDAO;
        this.userDAO = userDAO;
        this.historyEventDAO = historyEventDAO;
        this.messageDAO = messageDAO;
    }

    @Override
    public void save(Issue issue) {
        if (issue.getCreationTime() == null || issue.getCreationTime().getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("wrong ticketCreationTime");
        }
        if (issue.getReportedBy() == null || userDAO.getUserById(issue.getReportedBy().getUuid()) == null) {
            throw new IllegalArgumentException("wrong issueReporter");
        }
        if (issue.getDescription().isEmpty()) {
            throw new IllegalArgumentException("issue description and short description is null");
        }
        issueDAO.save(issue);
    }

    @Override
    public void generateAndSaveIssue() {
        Random random = new Random();
        User user;
        List<User> userList = userDAO.listAll();
        if (userList.size() < 5) {
            user = new User("user" + random.nextInt(5) + " firsName", "user " + random.nextInt(5) + " lastName");
            userDAO.save(user);
            user = userDAO.getUserById(user.getUuid());
        } else {
            user = userList.get(random.nextInt(5));
        }
        Issue issue = new Issue();
        issue.setReportedBy(user);
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
    public List<Issue> getIssueList() {
        return issueDAO.listAll();
    }

    @Override
    public Issue getIssueForViewById(String uuid) {
        return issueDAO.getIssueToDetailedViewById(uuid);
    }

    @Override
    public void changeIssueState(Issue issue, State newState, User assignedTo) {
        if (issue == null || issueDAO.getIssueById(issue.getUuid()) == null) {
            throw new IllegalArgumentException("issue not exists");
        }
        if (newState == null) {
            throw new IllegalArgumentException("incompatible newState");
        }
        if (assignedTo == null || (assignedTo = userDAO.getUserById(assignedTo.getUuid())) == null) {
            throw new IllegalArgumentException("non existed user");
        }
        if (newState == State.ASSIGNED && (issue.getAssignedTo() == null || userDAO.getUserById(issue.getAssignedTo().getUuid()) == null)) {
            throw new IllegalArgumentException("issue can be assigned to existed user");
        }
        issue = issueDAO.getIssueById(issue.getUuid());
        issue.setCurrentState(newState);
        issue.setAssignedTo(assignedTo);
        HistoryIssueStateChangeEvent event = new HistoryIssueStateChangeEvent();
        event.setEventDate(new Date());
        event.setIssue(issue);
        //get current security context user;
        //todo: change after spring security integration
        //event.setRedactor(user);
        event.setState(newState);
        issue.setCurrentState(newState);
        historyEventDAO.saveStateChange(event);
        issueDAO.save(issue);
    }

    @Override
    public void addIssueMessage(Issue issue, Message message) {
        if (issue == null || (issue = issueDAO.getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("issue not exist");
        }
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message is empty");
        }
        User user;
        if ((user = message.getMessageCreator()) == null || (user = userDAO.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user not exist");
        }
        message.setMessageCreator(user);
        messageDAO.save(message);
        HistoryIssueMessageEvent messageEvent = new HistoryIssueMessageEvent();
        messageEvent.setMessage(message);
        messageEvent.setIssue(issue);
        messageEvent.setEventDate(new Date());
        historyEventDAO.saveIssueMessage(messageEvent);
    }

    @Override
    public void changeIssueMessageContent(Message message, Message newMessage) {
        if (message == null || messageDAO.getMessageById(message.getUuid()) == null) {
            throw new IllegalArgumentException("message not exist");
        }
        if (newMessage == null || newMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("new message is empty");
        }
        message.setTitle(newMessage.getTitle());
        message.setContent(newMessage.getContent());
        message.setMessageDate(new Date());
        messageDAO.save(message);
    }

    @Override
    public void deleteMessage(Message message) {
        if (message == null || (message = messageDAO.getMessageById(message.getUuid())) == null) {
            throw new IllegalArgumentException("message not exist");
        }
        HistoryIssueMessageEvent messageEvent = historyEventDAO.getHistoryIssueMessageEventByMessage(message);
        historyEventDAO.deleteIssueMessageEvent(messageEvent);
        messageDAO.delete(message);
    }
}