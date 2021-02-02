package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return IssueMapper.getIssueShortDTO(issueService.getIssueById(issueUUID));
    }

    @Override
    public void createIssue(IssueDTO issueDTO, UserDTO userDTO) {
        Issue issue = IssueMapper.getIssue(issueDTO);
        issueService.createNewIssue(issue, UserMapper.getUser(userDTO));
    }

    @Override
    public void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        issueService.createIssueFromTicket(TicketMapper.getTicket(ticketDTO), UserMapper.getUser(userDTO));
    }

    @Override
    public void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO) {
    }

    @Override
    public List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO) {
        return issueService.getIssueList(null, 0, 100);
    }

    @Override
    public void deleteIssue(String issueUUID, boolean includeTicket) {
        Issue issue = IssueMapper.getIssue(new IssueShortDTO(issueUUID));
        issueService.deleteIssue(issue);
        if (includeTicket) {
            ticketService.delete(issue.getTicket().getUuid());
        }
    }
}