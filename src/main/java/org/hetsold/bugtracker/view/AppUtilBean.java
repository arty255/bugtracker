package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.hetsold.bugtracker.dto.IssueEventDTO;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Severity;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;

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
