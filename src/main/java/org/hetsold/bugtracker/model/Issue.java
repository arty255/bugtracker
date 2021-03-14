package org.hetsold.bugtracker.model;

import org.hetsold.bugtracker.dao.util.BooleanToStringConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NamedEntityGraph(
        name = "IssueEntityGraphToDetailedView",
        includeAllAttributes = true,
        attributeNodes = {
                @NamedAttributeNode(value = "reportedBy", subgraph = "reportedBySubGraph"),
                @NamedAttributeNode(value = "history", subgraph = "historyEventSubGraph"),
        },
        subgraphs = {
                @NamedSubgraph(name = "reportedBySubGraph",
                        attributeNodes = {
                                @NamedAttributeNode("firstName"),
                                @NamedAttributeNode("lastName")
                        }),
                @NamedSubgraph(name = "historyEventSubGraph",
                        attributeNodes = {
                                @NamedAttributeNode("eventDate"),
                        }),
        },
        subclassSubgraphs = {
                @NamedSubgraph(name = "historyEventSubGraph",
                        type = AbstractEntity.class,
                        attributeNodes = {}
                ),
                @NamedSubgraph(name = "historyEventSubGraph",
                        type = IssueMessageEvent.class,
                        attributeNodes = {
                                @NamedAttributeNode("messageCreator")
                        }),
                @NamedSubgraph(name = "historyEventSubGraph",
                        type = IssueStateChangeEvent.class,
                        attributeNodes = {
                                @NamedAttributeNode("redactor")
                        })
        }
)

@NamedEntityGraph(
        name = "IssueEntityGraphToShortView",
        includeAllAttributes = true,
        attributeNodes = {
                @NamedAttributeNode(value = "reportedBy", subgraph = "reportedBySubGraph"),
        },
        subgraphs = {
                @NamedSubgraph(name = "reportedBySubGraph",
                        attributeNodes = {
                                @NamedAttributeNode("firstName"),
                                @NamedAttributeNode("lastName")
                        }),
                @NamedSubgraph(name = "historyEventSubGraph",
                        attributeNodes = {
                                @NamedAttributeNode("eventDate"),
                        })
        }
)

@Entity
@Table(name = "issue")
public class Issue extends AbstractEntity {
    private String issueNumber;
    private String description;
    private Date creationTime;
    private String productVersion;
    private String reproduceSteps;
    private String existedResult;
    private String expectedResult;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "issue_reportedBy",
            joinColumns = @JoinColumn(name = "issueId", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "uuid"))
    private User reportedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "issue_assigned",
            joinColumns = @JoinColumn(name = "issueId", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "uuid"))
    private User assignedTo;
    private Severity severity;
    private String fixVersion;
    @Enumerated
    @Column(columnDefinition = "tinyint")
    private IssueState currentIssueState;
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<IssueEvent> history;
    @OneToOne
    @JoinTable(name = "issue_ticket",
            joinColumns = @JoinColumn(name = "issueId", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "ticketId", referencedColumnName = "uuid"))
    private Ticket ticket;
    @Column(name = "archived")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean archived;

    protected Issue() {
        history = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        this.creationTime = new Date();
    }

    public void update(Issue newIssue) {
        this.severity = newIssue.getSeverity();
        this.productVersion = newIssue.getProductVersion();
        this.description = newIssue.getDescription();
        this.reproduceSteps = newIssue.getReproduceSteps();
        this.existedResult = newIssue.getExistedResult();
        this.expectedResult = newIssue.getExpectedResult();
        this.archived = newIssue.archived;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueId) {
        this.issueNumber = issueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date issueCreatedTime) {
        this.creationTime = issueCreatedTime;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getReproduceSteps() {
        return reproduceSteps;
    }

    public void setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
    }

    public String getExistedResult() {
        return existedResult;
    }

    public void setExistedResult(String existedResult) {
        this.existedResult = existedResult;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reported) {
        this.reportedBy = reported;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    public IssueState getCurrentIssueState() {
        return currentIssueState;
    }

    public void setCurrentIssueState(IssueState currentIssueState) {
        this.currentIssueState = currentIssueState;
    }

    public List<IssueEvent> getHistory() {
        return history;
    }

    public void setHistory(List<IssueEvent> history) {
        this.history = history;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public static class Builder {
        private final Issue issue;

        public Builder() {
            issue = new Issue();
        }

        public Builder withUUID(final UUID uuid) {
            issue.setUuid(uuid);
            return this;
        }

        public Builder withIssueNumber(final String issueNumber) {
            issue.setIssueNumber(issueNumber);
            return this;
        }

        public Builder withDescriptionAndReproduceSteps(final String description, final String reproduceSteps) {
            issue.setDescription(description);
            issue.setReproduceSteps(reproduceSteps);
            return this;
        }

        public Builder withProductVersion(String productVersion) {
            issue.setProductVersion(productVersion);
            return this;
        }

        public Builder withReportedBy(User reportedBy) {
            issue.setReportedBy(reportedBy);
            return this;
        }

        public Builder withCreationTime(Date creationTime) {
            issue.setCreationTime(creationTime);
            return this;
        }

        public Builder withIssueExistedAndExpectedResults(final String existedResult, final String expectedResult) {
            issue.setExistedResult(existedResult);
            issue.setExpectedResult(expectedResult);
            return this;
        }

        public Builder withIssueSeverity(Severity issueSeverity) {
            issue.setSeverity(issueSeverity);
            return this;
        }

        public Builder withIssueState(IssueState issueState) {
            issue.setCurrentIssueState(issueState);
            return this;
        }

        public Issue build() {
            return issue;
        }
    }
}