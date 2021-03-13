package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.view.filter.ContractBuilder;
import org.hetsold.bugtracker.view.filter.FieldMaskFilter;
import org.hetsold.bugtracker.view.filter.FilterComponentBuilder;
import org.hetsold.bugtracker.view.filter.SortedColumnsContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Set;

@ManagedBean
@ViewScoped
public class TicketListBean implements Serializable {
    private static final long serialVersionUID = 8923362778600089408L;
    private TicketLazyDataModel ticketLazyDataModel;
    @Autowired
    private transient TicketService ticketService;
    private transient Set<FieldMaskFilter> fieldMaskFilters;

    private String columnKey;
    private transient SortedColumnsContainer sortedColumnsContainer;
    private transient Contract contract;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        createIssueFilterWrappersAction();
        buildContract();
        updateTicketDataModel();
    }

    public void createIssueFilterWrappersAction() {
        fieldMaskFilters = FilterComponentBuilder.buildFieldMaskFilters(TicketDTO.class,
                "description reproduceSteps resolveState verificationState");
        sortedColumnsContainer = new SortedColumnsContainer(FilterComponentBuilder.buildFieldOrderFilters(TicketDTO.class,
                "description resolveState creationTime"));
    }

    public void createOrderFiltersAction() {
        throw new UnsupportedOperationException("not implemented");
    }

    private void buildContract() {
        contract = ContractBuilder.buildContact(fieldMaskFilters, sortedColumnsContainer.getFinalOrderFilters());
    }

    public void modelFiltersUpdateAction() {
        updateDataModelContract();
    }


    public void changeSortOrderAction() {
        sortedColumnsContainer.twoStateToggle(columnKey);
        updateDataModelContract();
    }

    public void cancelSortedOrderAction() {
        sortedColumnsContainer.removeColumnNameFromOrder(columnKey);
        updateDataModelContract();
    }

    private void updateDataModelContract() {
        buildContract();
        ticketLazyDataModel.setContract(contract);
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

    public Set<FieldMaskFilter> getIssueFilters() {
        return fieldMaskFilters;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public int getColumnKeyIndex(String columnKey) {
        return sortedColumnsContainer.getColumnKeyIndex(columnKey);
    }

    public boolean columnInOrderFilters(String columnKey) {
        return sortedColumnsContainer.containsColumnKeyInFilters(columnKey);
    }

    public boolean isAscending(String columnKey) {
        return sortedColumnsContainer.isAscending(columnKey);
    }

}