package org.hetsold.bugtracker.model;

import org.hetsold.bugtracker.dao.util.BooleanToStringConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket extends AbstractEntity {
    private String description;
    private String reproduceSteps;
    private String productVersion;
    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;
    private Date creationTime;
    private Integer voteCount;
    @Enumerated
    private TicketVerificationState verificationState;
    @Enumerated
    private TicketResolveState resolveState;
    @OneToMany
    @JoinTable(name = "ticket_message",
            joinColumns = @JoinColumn(name = "ticketId", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "messageId", referencedColumnName = "uuid"))
    @OrderBy("createDate desc")
    private List<Message> messageList;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "ticket")
    private Issue issue;
    @Column(name = "archived")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean archived;

    protected Ticket() {
        messageList = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        this.creationTime = new Date();
    }

    public void update(Ticket ticket) {
        this.setDescription(ticket.getDescription());
        this.setProductVersion(ticket.getReproduceSteps());
        this.setProductVersion(ticket.getProductVersion());
        this.setResolveState(ticket.getResolveState());
        this.setVerificationState(ticket.getVerificationState());
        this.setVoteCount(ticket.getVoteCount());
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
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

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public static class Builder {
        private final Ticket ticket;

        public Builder() {
            this.ticket = new Ticket();
        }

        public Builder withData(final String description, final String reproduceSteps) {
            ticket.setDescription(description);
            ticket.setReproduceSteps(reproduceSteps);
            return this;
        }

        public Builder withUUID(final UUID uuid) {
            ticket.setUuid(uuid);
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            ticket.setCreatedBy(createdBy);
            return this;
        }

        public Ticket build() {
            return ticket;
        }
    }
}