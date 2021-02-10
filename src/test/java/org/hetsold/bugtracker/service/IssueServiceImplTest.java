package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.exception.EmptyIssueDescriptionException;
import org.hetsold.bugtracker.service.exception.EmptyUUIDKeyException;
import org.hetsold.bugtracker.util.FactoryIssueType;
import org.hetsold.bugtracker.util.IssueFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
public class IssueServiceImplTest {
    @Mock
    private IssueDAO issueDAO;
    @Mock
    private UserServiceInternal userService;
    @Mock
    private HistoryEventDAO historyEventDAO;
    @Mock
    private MessageServiceInternal messageService;
    @Mock
    private TicketServiceInternal ticketService;
    @InjectMocks
    private final IssueServiceImpl issueService = new IssueServiceImpl();

    private static final User savedUser = new User("Alex", "Tester");
    private static final Issue savedIssue = new Issue("issue description", "steps", savedUser);
    private static final Message savedMessage = new Message(savedUser, "message content");
    private static final Ticket savedTicket = new Ticket("ticket description", "steps", savedUser);
    private IssueFactory issueFactory;

    @Before
    public void prepareData() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(userService.getUser(savedUser)).thenReturn(savedUser);
        Mockito.when(issueDAO.getIssueByUUID(savedIssue.getUuid())).thenReturn(savedIssue);
        Mockito.when(messageService.getMessage(savedMessage)).thenReturn(savedMessage);
        Mockito.when(ticketService.getTicket(savedTicket)).thenReturn(savedTicket);
        issueFactory = new IssueFactory(savedIssue, savedUser);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewIssue_notPersistedReportedByThrowExceptionOnSave() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_NOT_PERSISTED_USER);
        issue.setUuid(null);
        issueService.createNewIssue(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewIssue_nullReportedByThrowExceptionOnSave() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_NOT_PERSISTED_USER);
        issue.setReportedBy(null);
        issueService.createNewIssue(issue);
    }

    @Test(expected = EmptyIssueDescriptionException.class)
    public void createNewIssue_emptyDescriptionThrowExceptionOnSave() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issue.setUuid(null);
        issue.setDescription("");
        issueService.createNewIssue(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewIssue_nullIssueThrowExceptionOnSave() {
        issueService.createNewIssue((Issue) null);
    }

    @Test
    public void createNewIssue_correctCreate() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issue.setUuid(null);
        issueService.createNewIssue(issue);
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        Mockito.verify(issueDAO).save(issueArgumentCaptor.capture());
        assertEquals(issue.getDescription(), issueArgumentCaptor.getValue().getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIssue_nullIssueThrowException() {
        issueService.updateIssue(null, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateIssue_notPersistedEditorThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_ON_BASE_OF_PERSISTED);
        User user = new User("testFN", "testLN");
        issueService.updateIssue(issue, user);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void updateIssue_nullEditorThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_ON_BASE_OF_PERSISTED);
        issueService.updateIssue(issue, null);
    }

    @Test
    public void updateIssue_correctUpdated() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_ON_BASE_OF_PERSISTED);
        Issue updatedIssue = issueService.updateIssue(issue, savedUser);
        assertEquals(issue.getUuid(), updatedIssue.getUuid());
        assertEquals(issue.getDescription(), updatedIssue.getDescription());
    }

    @Test
    public void createIssueFromTicket_correctCreate() {
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        issueService.createIssueFromTicket(savedTicket, savedUser);
        Mockito.verify(issueDAO).save(issueArgumentCaptor.capture());
        Mockito.verify(ticketService).applyForIssue(ticketArgumentCaptor.capture());
        assertEquals(savedTicket, issueArgumentCaptor.getValue().getTicket());
        assertEquals(savedTicket.getUuid(), ticketArgumentCaptor.getValue().getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIssueFromTicket_nullTicketThrowException() {
        issueService.createIssueFromTicket(null, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIssueFromTicket_notPersistedTicketThrowException() {
        Ticket ticket = new Ticket();
        ticket.setCreatedBy(savedUser);
        issueService.createIssueFromTicket(ticket, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIssueFromTicket_inkedTicketThrowException() {
        savedTicket.setIssue(savedIssue);
        issueService.createIssueFromTicket(savedTicket, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIssueFromTicket_nullCreatorThrowException() {
        issueService.createIssueFromTicket(savedTicket, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createIssueFromTicket_notPersistedCreatorThrowException() {
        User user = new User();
        issueService.createIssueFromTicket(savedTicket, user);
    }

    @Test
    public void assignIssueToTicket_correctAssign() {
        issueService.assignIssueToTicket(savedIssue, savedTicket);
        assertEquals(savedIssue.getTicket(), savedTicket);
    }

    @Ignore
    @Test
    public void assignIssueToTicket_nullIssueLinkedToTicketCorrect() {
        savedTicket.setIssue(savedIssue);
        issueService.assignIssueToTicket(null, savedTicket);
        assertNull(savedTicket.getIssue());
    }

    @Ignore
    @Test
    public void assignIssueToTicket_nullTicketLinkedToIssueCorrect() {
        savedIssue.setTicket(savedTicket);
        issueService.assignIssueToTicket(savedIssue, null);
        assertNull(savedIssue.getTicket());
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignIssueToTicket_linkedIssueAndTicketThrowException() {
        savedIssue.setTicket(savedTicket);
        savedTicket.setIssue(savedIssue);
        issueService.assignIssueToTicket(savedIssue, savedTicket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignIssueToTicket_notPersistedIssueThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issueService.assignIssueToTicket(issue, savedTicket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignIssueToTicket_notPersistedTicketThrowException() {
        Ticket ticket = new Ticket("description", "steps", savedUser);
        issueService.assignIssueToTicket(savedIssue, ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfNullIssueAndNullTicketThrowExceptionOnLinking() {
        issueService.assignIssueToTicket((Issue) null, null);
    }

    @Test
    public void changeIssueState_correctChange() {
        issueService.changeIssueState(savedIssue, IssueState.OPEN, savedUser);
        assertEquals(IssueState.OPEN, savedIssue.getCurrentIssueState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeIssueState_notPersistedIssueThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issueService.changeIssueState(issue, IssueState.OPEN, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeIssueState_assignedStateWithNotAssignedUserThrowException() {
        savedIssue.setAssignedTo(null);
        issueService.changeIssueState(savedIssue, IssueState.ASSIGNED, savedUser);
    }

    @Test()
    public void changeIssueState_assignedStateWithAssignedUserCorrectChange() {
        savedIssue.setAssignedTo(savedUser);
        issueService.changeIssueState(savedIssue, IssueState.ASSIGNED, savedUser);
        assertEquals(IssueState.ASSIGNED, savedIssue.getCurrentIssueState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeIssueState_changeToNullThrowException() {
        issueService.changeIssueState(savedIssue, null, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeIssueAssignedUser_notPersistedUserThrowException() {
        User notPersisted = new User("testFN", "testLN");
        issueService.changeIssueAssignedUser(savedIssue, notPersisted, savedUser);
    }

    @Test
    public void changeIssueArchiveState_correctChange() {
        issueService.changeIssueArchiveState(savedIssue, false);
        assertEquals(false, savedIssue.getArchived());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeIssueArchiveState_notPersistedThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issueService.changeIssueArchiveState(issue, false);
    }

    @Test
    public void getIssue_correctGet() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_ON_BASE_OF_PERSISTED);
        Issue resultIssue = issueService.getIssue(issue);
        Mockito.verify(issueDAO).getIssueByUUID(issue.getUuid());
        assertEquals(issue.getUuid(), resultIssue.getUuid());
    }

    @Test(expected = EmptyUUIDKeyException.class)
    public void getIssue_nullIssueUUIDThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_ON_BASE_OF_PERSISTED);
        issue.setUuid(null);
        issueService.getIssue(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getIssue_nullIssueThrowException() {
        issueService.getIssue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_nullIssueThrowException() {
        issueService.delete(null);
    }

    @Test(expected = EmptyUUIDKeyException.class)
    public void delete_nullIssueUUIDThrowException() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issue.setUuid(null);
        issueService.delete(issue);
    }

    @Test
    public void delete_correctDelete() {
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        issueService.delete(savedIssue);
        Mockito.verify(issueDAO).delete(issueArgumentCaptor.capture());
        assertEquals(savedIssue.getUuid(), issueArgumentCaptor.getValue().getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIssueMessage_nullMessageThrowException() {
        IssueDTO issueDTO = new IssueDTO(savedIssue);
        UserDTO userDTO = new UserDTO(savedUser);
        issueService.addIssueMessage(issueDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIssueMessage_emptyMessageContentThrowException() {
        IssueDTO issueDTO = new IssueDTO(savedIssue);
        UserDTO userDTO = new UserDTO(savedUser);
        MessageDTO messageDTO = new MessageDTO(new Message(savedUser, ""));
        issueService.addIssueMessage(issueDTO, messageDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIssueMessage_notPersistedUserThrowException() {
        IssueDTO issueDTO = new IssueDTO(savedIssue);
        MessageDTO messageDTO = new MessageDTO(new Message(new User(), "messageContent"));
        issueService.addIssueMessage(issueDTO, messageDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIssueMessage_notPersistedIssueThrowException() {
        IssueDTO issueDTO = new IssueDTO(issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER));
        MessageDTO messageDTO = new MessageDTO(new Message(savedUser, "messageContent"));
        issueService.addIssueMessage(issueDTO, messageDTO);
    }

    @Test
    public void addIssueMessage_correctAdd() {
        IssueDTO issueDTO = new IssueDTO(savedIssue);
        MessageDTO messageDTO = new MessageDTO(new Message(savedUser, "messageContent"));
        messageDTO.setUuid("");
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        issueService.addIssueMessage(issueDTO, messageDTO);
        Mockito.verify(historyEventDAO, Mockito.times(1)).saveIssueMessage(Mockito.any());
        Mockito.verify(messageService, Mockito.times(1)).saveNewMessage(messageArgumentCaptor.capture());
        assertEquals("messageContent", messageArgumentCaptor.getValue().getContent());
    }

    @Test
    public void checkIfIssueMessageCanBeDeleted() {
        MessageDTO messageDTO = new MessageDTO(savedMessage);
        ArgumentCaptor<IssueMessageEvent> messageEventArgumentCaptor = ArgumentCaptor.forClass(IssueMessageEvent.class);
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        issueService.deleteIssueMessage(messageDTO);
        Mockito.verify(historyEventDAO, Mockito.times(1)).deleteIssueMessageEvent(messageEventArgumentCaptor.capture());
        Mockito.verify(messageService).delete(messageArgumentCaptor.capture());
        assertEquals(savedMessage.getUuid(), messageArgumentCaptor.getValue().getUuid());
    }
}