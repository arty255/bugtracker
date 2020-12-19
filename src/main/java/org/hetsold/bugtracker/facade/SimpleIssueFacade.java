package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SimpleIssueFacade implements IssueFacade {
    private IssueService issueService;
    private TicketService ticketService;
    private IssueConverter issueConverter;
    private UserConvertor userConvertor;
    private TicketConvertor ticketConvertor;

    @Autowired
    public SimpleIssueFacade(IssueService issueService, TicketService ticketService, IssueConverter issueConverter, UserConvertor userConvertor, TicketConvertor ticketConvertor) {
        this.issueService = issueService;
        this.ticketService = ticketService;
        this.issueConverter = issueConverter;
        this.userConvertor = userConvertor;
        this.ticketConvertor = ticketConvertor;
    }

    @Override
    public void createIssue(IssueDTO issueDTO, UserDTO userDTO) {
        Issue issue = issueConverter.getIssue(issueDTO);
        issueService.createNewIssue(issue, userConvertor.getUser(userDTO));
    }

    @Override
    public void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        issueService.createIssueFromTicket(ticketConvertor.getTicket(ticketDTO), userConvertor.getUser(userDTO));
        ticketService.applyForIssue(ticketConvertor.getTicket(ticketDTO));
    }

    @Override
    public void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO) {
        issueService.updateIssueState(issueConverter.getIssue(issueShortDTO), userConvertor.getUser(userDTO));
    }

    @Override
    public List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO) {
        return issueService.findIssueByFilter(issueConverter.getIssue(issueShortDTO)).stream().map(issueConverter::getIssueShortDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteIssue(String issueUUID, boolean includeTicket) {
        Issue issue = issueConverter.getIssue(new IssueShortDTO(issueUUID));
        issueService.deleteIssue(issue);
        if (includeTicket) {
            ticketService.delete(issue.getTicket());
        }
    }
}