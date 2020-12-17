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

    public void save(Issue issue) {
        if (issue.getCreationTime() == null) {
            throw new IllegalArgumentException("issue creationTime can not be null");
        }
        if (issue.getReportedBy() == null || userDAO.getUserById(issue.getReportedBy().getUuid()) == null) {
            throw new IllegalArgumentException("issueReporter argument can not be null or not persisted");
        }
        if (issue.getDescription().isEmpty()) {
            throw new IllegalArgumentException("issue description description can not be empty");
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
            throw new IllegalArgumentException("uuid argument can not be empty");
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
    public void changeIssueState(Issue issue, State newState, User user) {
        if (issue == null || (issue = issueDAO.getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        if (newState == null) {
            throw new IllegalArgumentException("newState argument can not be null");
        }
        if (newState == State.ASSIGNED && issue.getAssignedTo() == null) {
            throw new IllegalArgumentException("cannot change State to assigned with unassigned User. use changeIssueAssignedUser");
        }
        if (issue.getCurrentState() != newState) {
            issue.setCurrentState(newState);
            HistoryIssueStateChangeEvent event = new HistoryIssueStateChangeEvent();
            event.setEventDate(new Date());
            event.setIssue(issue);
            event.setRedactor(user);
            event.setState(newState);
            issue.setCurrentState(newState);
            historyEventDAO.saveStateChange(event);
        }
    }

    public void changeIssueAssignedUser(Issue issue, User assignedTo, User user) {
        if (assignedTo == null || (userDAO.getUserById(assignedTo.getUuid())) == null) {
            throw new IllegalArgumentException("assignedUser argument can not be null or not persisted");
        }
        issue = getIssueById(issue.getUuid());
        if (issue == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        issue.setAssignedTo(assignedTo);
        if (issue.getCurrentState() != State.ASSIGNED) {
            changeIssueState(issue, State.ASSIGNED, user);
        }
    }

    @Override
    public void addIssueMessage(Issue issue, Message message, User user) {
        if (issue == null || (issue = getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message can not be null or with empty content ");
        }
        if (user == null || (user = userDAO.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        boolean messageNotExists = messageService.getMessageById(message) == null;
        if (messageNotExists) {
            messageService.saveMessage(message, user);
            HistoryIssueMessageEvent messageEvent = new HistoryIssueMessageEvent();
            messageEvent.setMessage(message);
            messageEvent.setIssue(issue);
            messageEvent.setEventDate(new Date());
            historyEventDAO.saveIssueMessage(messageEvent);
        }
    }

    @Override
    public void createNewIssue(Issue issue, User user) {
        if (getIssueById(issue.getUuid()) != null) {
            throw new IllegalArgumentException("new issue wit this id can not be created");
        }
        if (user == null || (user = userDAO.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        issue.setCreationTime(new Date());
        issue.setReportedBy(user);
        save(issue);
    }

    @Override
    public void createIssueFromTicket(Ticket ticket, User user) {
        if (ticket == null || (ticket = ticketService.getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket argument can not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        Issue issue = buildIssueFromTicket(ticket);
        createNewIssue(issue, user);
        issue.setTicket(ticket);
        ticketService.applyForIssue(ticket);
    }

    private Issue buildIssueFromTicket(Ticket ticket) {
        return new Issue.Builder()
                .withIssueNumber("")
                .withReproduceSteps(ticket.getReproduceSteps())
                .withProductVersion(ticket.getProductVersion())
                .withCreationTime(new Date())
                .withDescription(ticket.getDescription()).build();
    }

    @Override
    public void updateIssueState(Issue issue, User user) {
        Issue oldIssue = getIssueById(issue.getUuid());
        if (oldIssue.getAssignedTo() != issue.getAssignedTo() || oldIssue.getSeverity() != issue.getSeverity() || !oldIssue.getFixVersion().equals(issue.getFixVersion())) {
            HistoryIssueStateChangeEvent stateChangeEvent = new HistoryIssueStateChangeEvent();
            stateChangeEvent.setState(issue.getCurrentState());
            stateChangeEvent.setRedactor(user);
            stateChangeEvent.setExpectedFixVersion(issue.getFixVersion());
            stateChangeEvent.setEventDate(new Date());
        }
    }
}