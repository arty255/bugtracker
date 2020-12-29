package org.hetsold.bugtracker.model;

import java.util.Date;
import java.util.Objects;

public class TicketDTO {
    private String uuid;
    private String description;
    private String reproduceSteps;
    private String productVersion;
    private Date creationTime;
    private TicketVerificationState verificationState;
    private TicketResolveState resolveState;
    private UserDTO user;

    public TicketDTO(Ticket ticket) {
        this.uuid = ticket.getUuid();
        this.creationTime = ticket.getCreationTime();
        this.description = ticket.getDescription();
        this.reproduceSteps = ticket.getReproduceSteps();
        this.productVersion = ticket.getProductVersion();
        this.verificationState = ticket.getVerificationState();
        this.resolveState = ticket.getResolveState();
        this.user = new UserDTO(ticket.getCreatedBy());
    }

    public TicketDTO(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDTO ticketDTO = (TicketDTO) o;
        return Objects.equals(uuid, ticketDTO.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
