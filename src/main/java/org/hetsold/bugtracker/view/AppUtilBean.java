package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class AppUtilBean {
    public TicketResolveState[] getTicketResolveStates() {
        return TicketResolveState.values();
    }

    public TicketVerificationState[] getTicketVerificationStates() {
        return TicketVerificationState.values();
    }
}
