package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.facade.IssueConverter;
import org.hetsold.bugtracker.facade.TicketConvertor;
import org.hetsold.bugtracker.facade.UserConvertor;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultIssueService implements IssueService {
    private IssueDAO issueDAO;
    private HistoryEventDAO historyEventDAO;
    private MessageService messageService;
    private TicketService ticketService;
    private UserService userService;

    public DefaultIssueService() {
    }

    @Autowired
    public DefaultIssueService(IssueDAO issueDAO, UserService userService, HistoryEventDAO historyEventDAO, MessageService messageService, TicketService ticketService) {
        this.issueDAO = issueDAO;
        this.userService = userService;
        this.historyEventDAO = historyEventDAO;
        this.messageService = messageService;
        this.ticketService = ticketService;
    }

    @Override
    public void save(Issue issue) {
        if (issue.getCreationTime() == null) {
            throw new IllegalArgumentException("issue creationTime can not be null");
        }
        if (issue.getReportedBy() == null || userService.getUserById(issue.getReportedBy().getUuid()) == null) {
            throw new IllegalArgumentException("issueReporter argument can not be null or not persisted");
        }
        if (issue.getDescription().isEmpty()) {
            throw new IllegalArgumentException("issue description description can not be empty");
        }
        issueDAO.save(issue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueDTO saveOrUpdateIssue(IssueDTO issueDTO) {

        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Issue getIssueById(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("uuid argument can not be empty");
        }
        return issueDAO.getIssueById(uuid);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public IssueDTO getIssueDTOById(String uuid) {
        return IssueConverter.getIssueDTO(getIssueById(uuid));
    }

    @Override
    public void deleteIssue(Issue issue) {
        if (issue == null || issue.getUuid().isEmpty()) {
            throw new IllegalArgumentException("issue and uuid cannot be empty");
        }
        issueDAO.delete(issue);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Issue> findIssueByFilter(Issue issue) {
        if (issue == null) {
            throw new IllegalArgumentException("issue can not be empty");
        }
        return issueDAO.getIssueByCriteria(issue);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Issue> getIssueList() {
        return issueDAO.listAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<IssueShortDTO> getIssueList(int startPosition, int limit) {
        return issueDAO.getIssueList(startPosition, limit)
                .stream()
                .map(IssueConverter::getIssueShortDTO)
                .collect(Collectors.toList());
    }

    public long getIssuesCount() {
        return issueDAO.getIssueCount();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Issue getIssueForViewById(String uuid) {
        return issueDAO.getIssueToDetailedViewById(uuid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO) {
        if (issueDTO == null || issueDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null");
        }
        if (userDTO == null || userDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user: user can not be null");
        }
        return changeIssueState(IssueConverter.getIssue(issueDTO), newIssueState, UserConvertor.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean changeIssueState(Issue issue, IssueState newIssueState, User user) {
        if (issue == null || (issue = issueDAO.getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        if (newIssueState == null) {
            throw new IllegalArgumentException("newState argument can not be null");
        }
        if (newIssueState == IssueState.ASSIGNED && issue.getAssignedTo() == null) {
            throw new IllegalArgumentException("cannot change State to assigned with unassigned User. use changeIssueAssignedUser");
        }
        if (user == null || user.getUuid().isEmpty() || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be null or not persisted");
        }
        changeStateIfChangedAndSaveHistory(issue, newIssueState, user);
        return true;
    }

    private boolean changeStateIfChangedAndSaveHistory(Issue issue, IssueState newIssueState, User editor) {
        if (isStateChanged(issue, newIssueState)) {
            issue.setCurrentIssueState(newIssueState);
            generateAndSaveStateChangeEvent(issue, newIssueState, editor);
            return true;
        }
        return false;
    }

    private boolean isStateChanged(Issue issue, IssueState newIssueState) {
        return issue.getCurrentIssueState() != newIssueState;
    }

    private void generateAndSaveStateChangeEvent(Issue issue, IssueState issueState, User eventActor) {
        HistoryIssueStateChangeEvent event = new HistoryIssueStateChangeEvent();
        event.setIssue(issue);
        event.setRedactor(eventActor);
        event.setState(issueState);
        historyEventDAO.saveStateChange(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueAssignedUser(IssueDTO issueDTO, UserDTO assignedToDTO, UserDTO userDTO) {
        Issue issue;
        if (issueDTO == null || issueDTO.getUuid().isEmpty() || (issue = issueDAO.getIssueById(issueDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null or not persisted");
        }
        User assignedTo = null, user;
        if (userDTO == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be null or not persisted");
        }
        if (assignedToDTO != null && (assignedTo = userService.getUserById(assignedToDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect assigned user: user not persisted");
        }
        changeIssueAssignedUser(issue, assignedTo, user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueAssignedUser(Issue issue, User assignedTo, User user) {
        if (assignedTo != null && (userService.getUserById(assignedTo.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect assignedUser");
        }
        issue = getIssueById(issue.getUuid());
        if (issue == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        /*todo: add assignation history event*/
        if (isAssignedUserChanged(issue, assignedTo)) {
            issue.setAssignedTo(assignedTo);
            if (issue.getCurrentIssueState() == IssueState.OPEN && assignedTo != null) {
                changeStateIfChangedAndSaveHistory(issue, IssueState.ASSIGNED, user);
            } else if (assignedTo == null) {
                changeStateIfChangedAndSaveHistory(issue, IssueState.OPEN, user);
            }
        }
    }

    private boolean isAssignedUserChanged(Issue issue, User newAssignedTo) {
        return issue.getAssignedTo() != newAssignedTo;
    }

    public void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO, UserDTO userDTO) {
        Issue issue;
        Message message;
        if (issueDTO == null || issueDTO.getUuid().isEmpty() || (issue = getIssueById(issueDTO.getUuid())) == null) {
            throw new IllegalArgumentException("issue incorrect: issue cannot be null or not persisted");
        }
        if (messageDTO == null || messageDTO.getContent().isEmpty() || (message = messageService.getMessageById(messageDTO.getUuid())) == null) {
            throw new IllegalArgumentException("message can not be null/empty content or not persisted");
        }
        User user;
        if (userDTO == null || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        addIssueMessage(issue, message, user);
    }

    @Override
    public void addIssueMessage(Issue issue, Message message, User user) {
        if (issue == null || (issue = getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("issue argument can not be null or not persisted");
        }
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message can not be null or with empty content ");
        }
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        Message savedMessage = messageService.saveNewMessage(message, user);
        HistoryIssueMessageEvent messageEvent = new HistoryIssueMessageEvent();
        messageEvent.setMessage(savedMessage);
        messageEvent.setIssue(issue);
        messageEvent.setEventDate(new Date());
        historyEventDAO.saveIssueMessage(messageEvent);
    }

    @Override
    public void createNewIssue(Issue issue, User user) {
        if (getIssueById(issue.getUuid()) != null) {
            throw new IllegalArgumentException("new issue wit this id can not be created");
        }
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        issue.setCreationTime(new Date());
        issue.setCurrentIssueState(IssueState.OPEN);
        issue.setReportedBy(user);
        save(issue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Issue createIssueFromTicket(Ticket ticket, User user) {
        if (ticket == null || (ticket = ticketService.getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket argument can not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        if (ticket.getIssue() != null) {
            throw new IllegalArgumentException("issue for this ticket is already created");
        }
        Issue issue = buildIssueFromTicket(ticket);
        createNewIssue(issue, user);
        issue.setTicket(ticket);
        ticketService.applyForIssue(ticket);
        return issue;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueShortDTO createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        if (ticketDTO == null) {
            throw new IllegalArgumentException("ticket argument can not be null");
        }
        if (userDTO == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        Issue issue = createIssueFromTicket(TicketConvertor.getTicket(ticketDTO), UserConvertor.getUser(userDTO));
        return IssueConverter.getIssueShortDTO(issue);
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
            stateChangeEvent.setState(issue.getCurrentIssueState());
            stateChangeEvent.setRedactor(user);
            stateChangeEvent.setExpectedFixVersion(issue.getFixVersion());
            stateChangeEvent.setEventDate(new Date());
        }
    }

    @Override
    public List<HistoryEvent> getIssueEvents(IssueDTO issue, int startPosition, int limit) {
        return new ArrayList<>();
    }
}