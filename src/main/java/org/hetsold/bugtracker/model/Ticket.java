package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket extends ArchivedEntity {
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

    {
        messageList = new ArrayList<>();
    }

    public Ticket() {
    }

    public Ticket(UUID uuid) {
        this.setUuid(uuid);
    }

    public Ticket(String description, String reproduceSteps, User createdBy) {
        this.description = description;
        this.reproduceSteps = reproduceSteps;
        this.createdBy = createdBy;
    }

    public Ticket(UUID uuid, String description, String reproduceSteps, User createdBy) {
        this(description, reproduceSteps, createdBy);
        this.setUuid(uuid);
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
}