package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ticket")
public class Ticket extends AbstractIdentity {
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
    private List<Message> messageList;

    {
        verificationState = TicketVerificationState.NotVerified;
        messageList = new ArrayList<>();
    }

    public Ticket() {
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
}