package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.model.filter.FilterOperation;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@ApplicationScoped
public class AppUtilBean implements Serializable {
    public TicketResolveState[] getTicketResolveStates() {
        return TicketResolveState.values();
    }

    public TicketVerificationState[] getTicketVerificationStates() {
        return TicketVerificationState.values();
    }

    public IssueState[] getIssueStates() {
        return IssueState.values();
    }

    public Severity[] getSeverities() {
        return Severity.values();
    }

    public FilterOperation[] getFilterOperations() {
        return FilterOperation.values();
    }

    public String getDateFormat() {
        return "dd-MM-yyyy HH:mm";
    }

    public boolean isMessageInHistory(IssueEventDTO.EventType eventType) {
        return eventType == IssueEventDTO.EventType.MessageEvent;
    }
}
