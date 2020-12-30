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

    @Autowired
    public SimpleIssueFacade(IssueService issueService, TicketService ticketService, IssueConverter issueConverter) {
        this.issueService = issueService;
        this.ticketService = ticketService;
        this.issueConverter = issueConverter;
    }

    public SimpleIssueFacade() {
    }

    @Override
    public void generateRandomIssue() {
        issueService.generateAndSaveIssue();
    }

    @Override
    public IssueShortDTO getIssue(String issueUUID) {
        return issueConverter.getIssueShortDTO(issueService.getIssueById(issueUUID));
    }

    @Override
    public void createIssue(IssueDTO issueDTO, UserDTO userDTO) {
        Issue issue = issueConverter.getIssue(issueDTO);
        issueService.createNewIssue(issue, UserConvertor.getUser(userDTO));
    }

    @Override
    public void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        issueService.createIssueFromTicket(TicketConvertor.getTicket(ticketDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    public void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO) {
        issueService.updateIssueState(issueConverter.getIssue(issueShortDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    public List<IssueShortDTO> getIssueList(IssueShortDTO issueShortDTO) {
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