package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
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
    private MessageService messageService;
    private TicketService ticketService;

    public DefaultIssueService() {
    }

    public DefaultIssueService(IssueDAO issueDAO, UserDAO userDAO, HistoryEventDAO historyEventDAO, MessageService messageService, TicketService ticketService) {
        this.issueDAO = issueDAO;
        this.userDAO = userDAO;
        this.historyEventDAO = historyEventDAO;
        this.messageService = messageService;
        this.ticketService = ticketService;
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
        issue.setCreationTime(new Date());
        issue.setDescription("description" + random.nextInt());
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
    public List<Issue> findIssueByFilter(Issue issue) {
        if (issue == null) {
            issue = new Issue();
            issue.setUuid("");
        }
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
    public void changeIssueState(Issue issue, State newState, User assignedTo, User user) {
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
        event.setRedactor(user);
        event.setState(newState);
        issue.setCurrentState(newState);
        historyEventDAO.saveStateChange(event);
        issueDAO.save(issue);
    }

    @Override
    public void addIssueMessage(Issue issue, Message message, User user) {
        if (issueDAO.getIssueById(issue.getUuid()) == null) {
            throw new IllegalArgumentException("issue not exist");
        }
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message is empty");
        }
        if (user == null || (user = userDAO.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user not exist");
        }
        boolean messageNotExists = messageService.getMessageById(message) == null;
        messageService.saveMessage(message, user);
        if (messageNotExists) {
            HistoryIssueMessageEvent messageEvent = new HistoryIssueMessageEvent();
            messageEvent.setMessage(message);
            messageEvent.setIssue(issue);
            messageEvent.setEventDate(new Date());
            historyEventDAO.saveIssueMessage(messageEvent);
        }
    }

    @Override
    public void createIssue(Issue issue, User user) {
        issue.setCreationTime(new Date());
        issue.setReportedBy(user);
        this.save(issue);
    }

    @Override
    public void createIssueFromTicket(Ticket ticket, User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        Issue issue = ticketToIssueTransfer(ticket);
        issue.setReportedBy(user);
        issue.setTicket(ticket);
        issueDAO.save(issue);
        ticketService.applyForIssue(ticket);
    }

    @Override
    public void updateIssueState(Issue issue, User user) {
        Issue oldIssue = this.getIssueById(issue.getUuid());
        if (oldIssue.getAssignedTo() != issue.getAssignedTo() || oldIssue.getSeverity() != issue.getSeverity() || !oldIssue.getFixVersion().equals(issue.getFixVersion())) {
            HistoryIssueStateChangeEvent stateChangeEvent = new HistoryIssueStateChangeEvent();
            stateChangeEvent.setState(issue.getCurrentState());
            stateChangeEvent.setRedactor(user);
            stateChangeEvent.setExpectedFixVersion(issue.getFixVersion());
            stateChangeEvent.setEventDate(new Date());
        }
    }

    private Issue ticketToIssueTransfer(Ticket ticket) {
        return new Issue.Builder()
                .withIssueNumber("")
                .withReproduceSteps(ticket.getReproduceSteps())
                .withProductVersion(ticket.getProductVersion())
                .withCreationTime(new Date())
                .withDescription(ticket.getDescription()).build();
    }
}