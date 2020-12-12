package org.hetsold.bugtracker.rest;

public class IssueSimpleDTO {
    private String issueID;
    private String issueData;

    public IssueSimpleDTO(String issueID, String issueData) {
        this.issueID = issueID;
        this.issueData = issueData;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getIssueData() {
        return issueData;
    }

    public void setIssueData(String issueData) {
        this.issueData = issueData;
    }
}
