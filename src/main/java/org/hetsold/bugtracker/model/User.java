package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractIdentity {
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<Issue> foundIssues;
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<Issue> assignedIssues;
    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<Message> messageList;

    public User() {
        foundIssues = new ArrayList<>();
        assignedIssues = new ArrayList<>();
        messageList = new ArrayList<>();
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
