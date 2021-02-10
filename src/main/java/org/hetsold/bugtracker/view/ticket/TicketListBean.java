package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.view.filter.ContractBuilder;
import org.hetsold.bugtracker.view.filter.DisplayableFieldFilter;
import org.hetsold.bugtracker.view.filter.FilterComponentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class TicketListBean implements Serializable {
    private TicketLazyDataModel ticketLazyDataModel;
    @Autowired
    private TicketService ticketService;
    private List<DisplayableFieldFilter> displayableFieldFilters;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        createIssueFilterWrappersAction();
        updateTicketDataModel();
    }

    public void createIssueFilterWrappersAction() {
        displayableFieldFilters = FilterComponentBuilder.buildWrappers(TicketDTO.class,
                "description reproduceSteps resolveState verificationState");
    }

    public void modelFiltersUpdateAction() {
        ticketLazyDataModel.setContract(ContractBuilder.buildContact(displayableFieldFilters));
    }

    public void deleteTicket(String uuid) {
        ticketService.delete(new TicketDTO(uuid));
    }

    public void updateTicketDataModel() {
        ticketLazyDataModel = new TicketLazyDataModel(ticketService, null);
    }

    public TicketLazyDataModel getTicketLazyDataModel() {
        return ticketLazyDataModel;
    }

    public List<DisplayableFieldFilter> getIssueFilterList() {
        return displayableFieldFilters;
    }
}