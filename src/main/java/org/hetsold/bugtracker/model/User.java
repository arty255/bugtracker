package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
public class User extends AbstractEntity {
    private String firstName;
    private String lastName;
    private Date registrationDate;
    @OneToMany(mappedBy = "reportedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Issue> reportedIssues;
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<Issue> assignedIssues;
    @OneToMany(mappedBy = "messageCreator", fetch = FetchType.LAZY)
    private List<Message> messageList;

    protected User() {
        reportedIssues = new ArrayList<>();
        assignedIssues = new ArrayList<>();
        messageList = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        this.registrationDate = new Date();
    }

    public void update(User updatedUser) {
        this.firstName = updatedUser.getFirstName();
        this.lastName = updatedUser.getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Issue> getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues(List<Issue> foundIssues) {
        this.reportedIssues = foundIssues;
    }

    public List<Issue> getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(List<Issue> assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder withNames(final String firstName, final String lastName) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return this;
        }

        public Builder withUUID(final UUID uuid) {
            user.setUuid(uuid);
            return this;
        }

        public Builder withRegistrationDate(final Date registrationDate) {
            user.setRegistrationDate(registrationDate);
            return this;
        }

        public User build() {
            return user;
        }
    }
}