package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.hetsold.bugtracker.dto.IssueEventDTO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.view.user.UserPreset;

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

    public SecurityUserAuthority[] getSecurityUserAuthorities() {
        return SecurityUserAuthority.values();
    }

    public UserPreset[] getUserPresets() {
        return UserPreset.values();
    }

    public String getDateFormat() {
        return "dd-MM-yyyy HH:mm";
    }

    public boolean isMessageInHistory(IssueEventDTO.EventType eventType) {
        return eventType == IssueEventDTO.EventType.MessageEvent;
    }
}
