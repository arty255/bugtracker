package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.hetsold.bugtracker.dto.IssueEventDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.view.user.UserPreset;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@ApplicationScoped
public class AppUtilBean implements Serializable {
    private static final String DEFAULT_TIME_PATTERN = "dd-MM-yyyy HH:mm";

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
        return DEFAULT_TIME_PATTERN;
    }

    public boolean isMessageInHistory(IssueEventDTO.EventType eventType) {
        return eventType == IssueEventDTO.EventType.MESSAGE_EVENT;
    }

    public boolean isLoggedInUserAreTicketCreator(TicketDTO ticketDTO) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDTO user = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
            if (user != null && ticketDTO != null && ticketDTO.getUser() != null) {
                return ticketDTO.getUser().equals(user);
            }
        }
        return false;
    }
}
