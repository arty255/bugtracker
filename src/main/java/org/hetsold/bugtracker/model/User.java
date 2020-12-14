package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends AbstractIdentity {
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "reportedBy", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Issue> foundIssues;
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<Issue> assignedIssues;
    @OneToMany(mappedBy = "messageCreator", fetch = FetchType.LAZY)
    private List<Message> messageList;

    {
        foundIssues = new ArrayList<>();
        assignedIssues = new ArrayList<>();
        messageList = new ArrayList<>();
    }

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public List<Issue> getFoundIssues() {
        return foundIssues;
    }

    public void setFoundIssues(List<Issue> foundIssues) {
        this.foundIssues = foundIssues;
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
}
