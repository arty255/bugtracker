package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.*;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hetsold.bugtracker.service.ValidationHelper.*;

@Service
public class IssueServiceImpl implements IssueService, IssueServiceInternal {
    private IssueDAO issueDAO;
    private HistoryEventDAO historyEventDAO;
    private MessageServiceInternal messageService;
    private TicketServiceInternal ticketService;
    private UserServiceInternal userService;

    public IssueServiceImpl() {
    }

    @Autowired
    public IssueServiceImpl(IssueDAO issueDAO,
                            UserServiceInternal userService,
                            HistoryEventDAO historyEventDAO,
                            MessageServiceInternal messageService,
                            TicketServiceInternal ticketService) {
        this.issueDAO = issueDAO;
        this.userService = userService;
        this.historyEventDAO = historyEventDAO;
        this.messageService = messageService;
        this.ticketService = ticketService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueDTO createNewIssue(IssueDTO issueDTO) {
        Issue issue = IssueMapper.getIssue(issueDTO);
        createNewIssue(issue);
        return IssueMapper.getIssueDTO(issue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewIssue(Issue issue) {
        validateIssueBeforeSave(issue);
        initIssueLinkedUsers(issue);
        validateNotNull(issue.getReportedBy(), "");
        prepareIssueSeverityAndState(issue);
        issue.setUuid(UUID.randomUUID());
        issueDAO.save(issue);
    }

    private void initIssueLinkedUsers(Issue issue) {
        issue.setReportedBy(reinitializeUser(issue.getReportedBy()));
        issue.setAssignedTo(reinitializeUser(issue.getAssignedTo()));
    }

    private User reinitializeUser(User user) {
        if (user != null) {
            return userService.getUser(user);
        }
        return null;
    }

    private void prepareIssueSeverityAndState(Issue issue) {
        if (issue.getSeverity() == null) {
            issue.setSeverity(Severity.UNRATED);
        }
        if (issue.getCurrentIssueState() == null) {
            issue.setCurrentIssueState(IssueState.OPEN);
        } else {
            issue.setCurrentIssueState(issue.getCurrentIssueState());
            issue.setAssignedTo(issue.getAssignedTo());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Issue updateIssue(Issue issue, User user) {
        Issue oldIssue = getIssue(issue);
        validateNotNull(oldIssue, ISSUE_NOT_PERSISTED);
        User fetchedUser = userService.getUser(user);
        validateNotNull(fetchedUser, "user is not persisted");
        oldIssue.update(issue);
        return oldIssue;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueDTO updateIssue(IssueDTO issueDTO, UserDTO userDTO) {
        Issue issue = IssueMapper.getIssue(issueDTO);
        updateIssue(issue, UserMapper.getUser(userDTO));
        return IssueMapper.getIssueDTO(issue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Issue createIssueFromTicket(Ticket ticket, User user) {
        User fetchedUser = userService.getUser(user);
        validateNotNull(fetchedUser, "user can not be not persisted");
        Ticket fetchedTicket = ticketService.getTicket(ticket);
        validateNotNull(fetchedTicket, "ticket can not be not persisted");
        if (fetchedTicket.getIssue() != null) {
            throw new IllegalArgumentException("issue for this ticket is already created");
        }
        Issue issue = buildIssueFromTicket(fetchedTicket);
        issue.setReportedBy(fetchedUser);
        issue.setTicket(fetchedTicket);
        createNewIssue(issue);
        ticketService.applyForIssue(fetchedTicket);
        return issue;
    }

    private Issue buildIssueFromTicket(Ticket ticket) {
        return new Issue.Builder()
                .withIssueNumber("")
                .withIssueUuid(null)
                .withReproduceSteps(ticket.getReproduceSteps())
                .withProductVersion(ticket.getProductVersion())
                .withCreationTime(new Date())
                .withDescription(ticket.getDescription()).build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public IssueShortDTO createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        Issue issue = createIssueFromTicket(TicketMapper.getTicket(ticketDTO), UserMapper.getUser(userDTO));
        return IssueMapper.getIssueShortDTO(issue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void linkIssueToTicket(Issue issue, Ticket ticket, boolean replaceOldAssignment) {
        Issue fetchedIssue = getIssue(issue);
        Ticket fetchedTicket = ticketService.getTicket(ticket);
        validateNotNull(fetchedIssue, ISSUE_NOT_PERSISTED);
        validateNotNull(fetchedTicket, TICKET_NOT_PERSISTED);
        if (isAlreadyAssigned(fetchedIssue, fetchedTicket) && !replaceOldAssignment) {
            throw new IllegalArgumentException("issue or ticket already assigned");
        }
        if (replaceOldAssignment) {
            unLinkTicket(fetchedTicket);
            unLinkIssueOneDirection(fetchedIssue);
        }
        fetchedIssue.setTicket(fetchedTicket);
    }

    private boolean isAlreadyAssigned(Issue issue, Ticket ticket) {
        return issue.getTicket() != null || ticket.getIssue() != null;
    }

    private void unLinkIssueOneDirection(Issue issue) {
        issue.setTicket(null);
    }

    private void unLinkTicket(Ticket ticket) {
        Issue issueToUnLink = ticket.getIssue();
        issueToUnLink.setTicket(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void linkIssueToTicket(IssueShortDTO issueShortDTO, TicketDTO ticketDTO, boolean replaceOldAssignment) {
        linkIssueToTicket(IssueMapper.getIssue(issueShortDTO), TicketMapper.getTicket(ticketDTO), replaceOldAssignment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void unLinkIssue(IssueShortDTO issueShortDTO) {
        unLinkIssue(IssueMapper.getIssue(issueShortDTO));
    }

    private void unLinkIssue(Issue issue){
        Issue fetchedIssue = getIssue(issue);
        validateNotNull(fetchedIssue, ISSUE_NOT_PERSISTED);
        unLinkIssueOneDirection(fetchedIssue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueArchiveState(Issue issue, boolean newState) {
        Issue oldIssue = getIssue(issue);
        validateNotNull(oldIssue, ISSUE_NOT_PERSISTED);
        oldIssue.setArchived(newState);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void makeIssueArchived(IssueShortDTO issueShortDTO) {
        changeIssueArchiveState(IssueMapper.getIssue(issueShortDTO), true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void makeIssueUnarchived(IssueShortDTO issueShortDTO) {
        changeIssueArchiveState(IssueMapper.getIssue(issueShortDTO), false);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Issue getIssue(Issue issue) {
        validateNotNullEntityAndUUID(issue);
        return issueDAO.getIssueByUUID(issue.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public IssueDTO getIssueDTO(IssueDTO issueDTO) {
        return IssueMapper.getIssueDTO(getIssue(IssueMapper.getIssue(issueDTO)));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public IssueShortDTO getIssueShortDTO(IssueShortDTO issueShortDTO) {
        return IssueMapper.getIssueShortDTO(getIssue(IssueMapper.getIssue(new IssueDTO(issueShortDTO.getUuid()))));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Issue issue) {
        Issue fetchedIssue = getIssue(issue);
        validateNotNull(fetchedIssue, ISSUE_NOT_PERSISTED);
        issueDAO.delete(fetchedIssue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteIssue(IssueDTO issueDTO) {
        delete(IssueMapper.getIssue(issueDTO));
    }

    @Override
    @Secured("ROLE_LIST_ISSUES")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<IssueShortDTO> getIssueList(Contract contract, int startPosition, int limit) {
        return IssueMapper.getShortDTOList(issueDAO.getIssueList(contract, startPosition, limit));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getIssuesCount(Contract contract) {
        return issueDAO.getIssueCount(contract);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO) {
        changeIssueState(IssueMapper.getIssue(issueDTO), newIssueState, UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueState(Issue issue, IssueState newIssueState, User user) {
        validateIssueState(newIssueState);
        if (newIssueState == IssueState.ASSIGNED && issue.getAssignedTo() == null) {
            throw new IllegalArgumentException("cannot change State to assigned with unassigned User. use changeIssueAssignedUser");
        }
        Issue fetchedIssue = getIssue(issue);
        validateNotNull(fetchedIssue, ISSUE_NOT_PERSISTED);
        User fetchedUser = userService.getUser(user);
        validateNotNull(fetchedUser, "user can not be null");
        changeIssueStateAndSaveHistory(fetchedIssue, newIssueState, fetchedUser);
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
        changeIssueAssignedUser(IssueMapper.getIssue(issueDTO),
                UserMapper.getUser(assignedToDTO),
                UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeIssueAssignedUser(Issue issue, User assignedTo, User user) {
        User fetchedAssignedUser = null;
        if (assignedTo != null) {
            fetchedAssignedUser = userService.getUser(assignedTo);
            validateNotNull(fetchedAssignedUser, "assigned user is not persisted");
        }
        Issue fetchedIssue = getIssue(issue);
        validateNotNull(fetchedIssue, "issue is not persisted");
        User fetchedUser = userService.getUser(user);
        validateNotNull(fetchedUser, "user is not persisted");
        if (isAssignedUserChanged(fetchedIssue, fetchedAssignedUser)) {
            processAssignation(fetchedIssue, fetchedAssignedUser, fetchedUser);
        }
    }

    private boolean isAssignedUserChanged(Issue issue, User newAssignedTo) {
        return issue.getAssignedTo() != newAssignedTo;
    }

    private void processAssignation(Issue issue, User newAssignedTo, User stateEditor) {
        if (isIssueInFixedState(issue)) {
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

    private void generateAndSaveMessageEvent(Issue issue, Message message) {
        IssueMessageEvent messageEvent = new IssueMessageEvent();
        messageEvent.setMessage(message);
        messageEvent.setIssue(issue);
        historyEventDAO.saveIssueMessage(messageEvent);
    }

    private List<IssueEvent> getIssueEvents(Issue issue, int startPosition, int limit, boolean inverseDateOrder) {
        Issue fetchedIssue = getIssue(issue);
        return historyEventDAO.getHistoryIssueEventsByIssue(fetchedIssue, startPosition, limit, inverseDateOrder);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<IssueEventDTO> getIssueEventsDTO(IssueDTO issueDTO, int startPosition, int limit, boolean inverseDateOrder) {
        return IssueEventMapper.getIssueEventList(getIssueEvents(IssueMapper.getIssue(issueDTO),
                startPosition,
                limit,
                inverseDateOrder));
    }

    private long getIssueHistoryEventsCount(Issue issue) {
        Issue fetchedIssue = getIssue(issue);
        return historyEventDAO.getHistoryIssueEventsCountForIssue(fetchedIssue);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getIssueEventsCount(IssueDTO issueDTO) {
        return getIssueHistoryEventsCount(IssueMapper.getIssue(issueDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO) {
        addIssueMessage(IssueMapper.getIssue(issueDTO),
                MessageMapper.getMessage(messageDTO));
    }

    public void addIssueMessage(Issue issue, Message message) {
        validateMessageBeforeSave(message);
        Issue fetchedIssue = getIssue(issue);
        validateNotNull(fetchedIssue, "issue is not persisted");
        validateNotNull(message.getMessageCreator(), "message creator can not be null");
        messageService.saveNewMessage(message);
        generateAndSaveMessageEvent(fetchedIssue, message);
    }

    @Override
    public void deleteIssueMessage(MessageDTO messageDTO) {
        Message message = messageService.getMessage(MessageMapper.getMessage(messageDTO));
        IssueMessageEvent issueMessageEvent = historyEventDAO.getMessageEventByMessage(message);
        historyEventDAO.deleteIssueMessageEvent(issueMessageEvent);
        messageService.delete(message);
    }
}