package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;

import java.util.Date;

public class TicketDTO extends ArchivedIdentityDTO {
    private String description;
    private String reproduceSteps;
    private String productVersion;
    private Date creationTime;
    private TicketVerificationState verificationState;
    private TicketResolveState resolveState;
    private UserDTO user;
    private String issueID;

    public TicketDTO(Ticket ticket) {
        this.setUuid(ticket.getUuid().toString());
        this.creationTime = ticket.getCreationTime();
        this.description = ticket.getDescription();
        this.reproduceSteps = ticket.getReproduceSteps();
        this.productVersion = ticket.getProductVersion();
        this.verificationState = ticket.getVerificationState();
        this.resolveState = ticket.getResolveState();
        if (ticket.getCreatedBy() != null) {
            this.user = new UserDTO(ticket.getCreatedBy());
        }
        if (ticket.getIssue() != null) {
            this.issueID = ticket.getIssue().getUuid().toString();
        }
    }

    public TicketDTO(String uuid) {
        this.setUuid(uuid);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReproduceSteps() {
        return reproduceSteps;
    }

    public void setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public TicketVerificationState getVerificationState() {
        return verificationState;
    }

    public void setVerificationState(TicketVerificationState verificationState) {
        this.verificationState = verificationState;
    }

    public TicketResolveState getResolveState() {
        return resolveState;
    }

    public void setResolveState(TicketResolveState resolveState) {
        this.resolveState = resolveState;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }
}
