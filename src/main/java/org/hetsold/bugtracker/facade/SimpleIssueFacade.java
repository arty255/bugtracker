package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleIssueFacade implements IssueFacade {
    private IssueService issueService;

    @Autowired
    public SimpleIssueFacade(IssueService issueService) {
        this.issueService = issueService;
    }

    public SimpleIssueFacade() {
    }

    @Override
    public IssueShortDTO getIssue(String issueUUID) {
        return issueService.getIssueShortDTO(new IssueShortDTO(issueUUID));
    }

    @Override
    public void createIssue(IssueDTO issueDTO) {
        issueService.createNewIssue(issueDTO);
    }

    @Override
    public void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        issueService.createIssueFromTicket(ticketDTO, userDTO);
    }

    @Override
    public void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO) {
        return issueService.getIssueList(null, 0, 100);
    }

    @Override
    public void deleteIssue(String issueUUID, boolean includeTicket) {
        issueService.deleteIssue(new IssueDTO(issueUUID));
    }
}