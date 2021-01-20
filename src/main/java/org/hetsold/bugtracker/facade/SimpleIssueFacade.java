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

    @Autowired
    public SimpleIssueFacade(IssueService issueService, TicketService ticketService) {
        this.issueService = issueService;
        this.ticketService = ticketService;
    }

    public SimpleIssueFacade() {
    }

    @Override
    public void generateRandomIssue() {

    }

    @Override
    public IssueShortDTO getIssue(String issueUUID) {
        return IssueConverter.getIssueShortDTO(issueService.getIssueById(issueUUID));
    }

    @Override
    public void createIssue(IssueDTO issueDTO, UserDTO userDTO) {
        Issue issue = IssueConverter.getIssue(issueDTO);
        issueService.createNewIssue(issue, UserConvertor.getUser(userDTO));
    }

    @Override
    public void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        issueService.createIssueFromTicket(TicketConvertor.getTicket(ticketDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    public void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO) {
        issueService.updateIssueState(IssueConverter.getIssue(issueShortDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    public List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO) {
        return issueService.findIssueByFilter(IssueConverter.getIssue(issueShortDTO))
                .stream()
                .map(IssueConverter::getIssueShortDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteIssue(String issueUUID, boolean includeTicket) {
        Issue issue = IssueConverter.getIssue(new IssueShortDTO(issueUUID));
        issueService.deleteIssue(issue);
        if (includeTicket) {
            ticketService.delete(issue.getTicket());
        }
    }
}