package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.facade.IssueConverter;
import org.hetsold.bugtracker.facade.MessageConvertor;
import org.hetsold.bugtracker.facade.TicketConvertor;
import org.hetsold.bugtracker.facade.UserConvertor;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
    public Issue updateIssue(Issue issue) {
        Issue oldIssue;
        if ((oldIssue = getIssueById(issue.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect issue: update can be preform on existed issue");
        }
        oldIssue.update(issue);
        return oldIssue;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Issue saveOrUpdateIssue(Issue issue, User user) {
        if (issue == null) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null");
        }
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("invalid user: user cannot be empty");
        }
        if (issue.getUuid().isEmpty()) {
            issue.setReportedBy(user);
            save(issue);
        } else {
            issue = updateIssue(issue);
        }
        return issue;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueDTO saveOrUpdateIssue(IssueDTO issueDTO, UserDTO userDTO) {
        User user;
        if (issueDTO == null) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null");
        }
        if (userDTO == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be null or not persisted");
        }
        return IssueConverter.getIssueDTO(saveOrUpdateIssue(IssueConverter.getIssue(issueDTO), user));
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
    public void changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO) {
        if (issueDTO == null || issueDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null");
        }
        if (userDTO == null || userDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user: user can not be null");
        }
        changeIssueState(IssueConverter.getIssue(issueDTO), newIssueState, UserConvertor.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueState(Issue issue, IssueState newIssueState, User user) {
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
        changeIssueStateAndSaveHistory(issue, newIssueState, user);
    }

    private void changeIssueStateAndSaveHistory(Issue issue, IssueState newIssueState, User editor) {
        if (isStateChanged(issue, newIssueState)) {
            if (issue.getCurrentIssueState() == IssueState.FIXED && newIssueState == IssueState.OPEN) {
                throw new IllegalArgumentException("new state for FIXED before issue can be only REOPEN");
            }
            issue.setCurrentIssueState(newIssueState);
            generateAndSaveStateChangeEvent(issue, newIssueState, editor);
        }
    }

    private boolean isStateChanged(Issue issue, IssueState newIssueState) {
        return issue.getCurrentIssueState() != newIssueState;
    }

    private void generateAndSaveStateChangeEvent(Issue issue, IssueState issueState, User eventActor) {
        IssueStateChangeEvent event = new IssueStateChangeEvent();
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
        if (isAssignedUserChanged(issue, assignedTo)) {
            processAssignation(issue, assignedTo, user);
        }
    }

    private boolean isAssignedUserChanged(Issue issue, User newAssignedTo) {
        return issue.getAssignedTo() != newAssignedTo;
    }

    private void processAssignation(Issue issue, User newAssignedTo, User stateEditor) {
        if (isIssueInFixedState(issue)) {
            /*todo: implement different strategy*/
            throw new IllegalArgumentException("issue in FIXED state can not be assigned to new user");
        } else {
            issue.setAssignedTo(newAssignedTo);
            if (isIssueInOpenOrReopenState(issue)) {
                changeIssueStateAndSaveHistory(issue, IssueState.ASSIGNED, stateEditor);
            } else if (isIssueInAssignedState(issue) || newAssignedTo == null) {
                IssueState previousState = getPreviousState(issue);
                changeIssueStateAndSaveHistory(issue, previousState, stateEditor);
            }
        }
    }

    public IssueState getPreviousState(Issue issue) {
        return historyEventDAO.getPreviousOpenOrReopenStateForIssue(issue);
    }

    private boolean isIssueInOpenOrReopenState(Issue issue) {
        return issue.getCurrentIssueState() == IssueState.OPEN || issue.getCurrentIssueState() == IssueState.REOPEN;
    }

    private boolean isIssueInFixedState(Issue issue) {
        return issue.getCurrentIssueState() == IssueState.FIXED;
    }

    private boolean isIssueInAssignedState(Issue issue) {
        return issue.getCurrentIssueState() == IssueState.ASSIGNED;
    }

    public void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO, UserDTO userDTO) {
        Issue issue;
        if (issueDTO == null || issueDTO.getUuid().isEmpty() || (issue = getIssueById(issueDTO.getUuid())) == null) {
            throw new IllegalArgumentException("issue incorrect: issue cannot be null or not persisted");
        }
        if (messageDTO == null || messageDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("message can not be null/empty content");
        }
        User user;
        if (userDTO == null || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("user argument can not be null or not persisted");
        }
        addIssueMessage(issue, MessageConvertor.getMessage(messageDTO), user);
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
        generateAndSaveMessageEvent(issue, savedMessage);
    }

    private void generateAndSaveMessageEvent(Issue issue, Message message) {
        IssueMessageEvent messageEvent = new IssueMessageEvent();
        messageEvent.setMessage(message);
        messageEvent.setIssue(issue);
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
            IssueStateChangeEvent stateChangeEvent = new IssueStateChangeEvent();
            stateChangeEvent.setState(issue.getCurrentIssueState());
            stateChangeEvent.setRedactor(user);
            stateChangeEvent.setEventDate(new Date());
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<IssueEvent> getIssueEvents(Issue issue, int startPosition, int limit) {
        return historyEventDAO.getHistoryIssueEventsByIssue(issue, startPosition, limit);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<IssueEventDTO> getIssueHistoryEventsDTO(IssueDTO issueDTO, int startPosition, int limit) {
        Issue issue;
        if (issueDTO == null || issueDTO.getUuid().isEmpty() || (issue = getIssueById(issueDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect issue: issue can not be null or not persisted");
        }
        return getIssueEvents(issue, startPosition, limit)
                .stream()
                .map(IssueEventDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getIssueHistoryEventsCount(Issue issue) {
        return historyEventDAO.getHistoryIssueEventsCountForIssue(issue);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getIssueHistoryEventsCount(IssueDTO issueDTO) {
        Issue issue;
        if (issueDTO == null || issueDTO.getUuid().isEmpty() || (issue = getIssueById(issueDTO.getUuid())) == null) {
            throw new IllegalArgumentException("wrong issue: issue can not be null or not persisted");
        }
        return getIssueHistoryEventsCount(issue);
    }

    @Override
    public void deleteIssueMessage(MessageDTO selectedToDeleteMessage) {
        Message message;
        if (selectedToDeleteMessage == null || selectedToDeleteMessage.getUuid().isEmpty() || (message = messageService.getMessageById(selectedToDeleteMessage.getUuid())) == null) {
            throw new IllegalArgumentException("message can not be null or not persisted");
        }
        IssueMessageEvent issueMessageEvent = historyEventDAO.getMessageEventByMessage(message);
        historyEventDAO.deleteIssueMessageEvent(issueMessageEvent);
        messageService.deleteMessage(message);
    }
}