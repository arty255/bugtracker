package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Issue;

import javax.transaction.Transactional;

@Transactional
public class DefaultIssueService implements IssueService {
    private IssueDAO issueDAO;
    private UserDAO userDAO;


    public DefaultIssueService(IssueDAO issueDAO, UserDAO userDAO) {
        this.issueDAO = issueDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void save(Issue issue) {
        if (issue.getIssueAppearanceTime() == null || issue.getIssueAppearanceTime().getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("wrong issueAppearanceTime");
        }
        if (issue.getTicketCreationTime() == null || issue.getTicketCreationTime().getTime() > System.currentTimeMillis()) {
            throw new IllegalArgumentException("wrong ticketCreationTime");
        }
        if (issue.getReportedBy() == null || userDAO.getUserById(issue.getReportedBy().getUuid()) == null) {
            throw new IllegalArgumentException("wrong issueReporter");
        }
        issueDAO.save(issue);
    }
}
