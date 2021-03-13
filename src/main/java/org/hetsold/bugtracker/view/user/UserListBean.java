package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.hetsold.bugtracker.view.filter.ContractBuilder;
import org.hetsold.bugtracker.view.filter.FieldMaskFilter;
import org.hetsold.bugtracker.view.filter.FilterComponentBuilder;
import org.hetsold.bugtracker.view.filter.SortedColumnsContainer;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Set;

@ManagedBean
@ViewScoped
public class UserListBean implements Serializable {
    private static final long serialVersionUID = -1231997257240711340L;
    private transient UserService userService;
    private UserDTO selectedUserDTO;
    private LazyDataModel<UserDTO> usersLazyDataModel;
    private transient Set<FieldMaskFilter> fieldMaskFilters;

    private String columnKey;
    private transient SortedColumnsContainer sortedColumnsContainer;
    private transient Contract contract;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        createIssueFilterWrappersAction();
        usersLazyDataModel = new UserDTOLazyDataModel(userService);
    }

    public void createIssueFilterWrappersAction() {
        fieldMaskFilters = FilterComponentBuilder.buildFieldMaskFilters(UserDTO.class,
                "firstName lastName");
        sortedColumnsContainer = new SortedColumnsContainer(FilterComponentBuilder.buildFieldOrderFilters(UserDTO.class, "registrationDate"));
    }

    private void buildContract() {
        contract = ContractBuilder.buildContact(fieldMaskFilters, sortedColumnsContainer.getFinalOrderFilters());
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
        ((UserDTOLazyDataModel) usersLazyDataModel).setContract(contract);
    }

    public void modelFiltersUpdateAction() {
        ((UserDTOLazyDataModel) usersLazyDataModel).setContract(ContractBuilder.buildContact(fieldMaskFilters, sortedColumnsContainer.getFinalOrderFilters()));
    }

    @Secured("ROLE_DELETE_USER")
    public void deleteUser() {
        userService.delete(selectedUserDTO);
    }

    public LazyDataModel<UserDTO> getUsersLazyDataModel() {
        return usersLazyDataModel;
    }

    public UserDTO getSelectedUserDTO() {
        return selectedUserDTO;
    }

    public void setSelectedUserDTO(UserDTO selectedUserDTO) {
        this.selectedUserDTO = selectedUserDTO;
    }

    public Set<FieldMaskFilter> getIssueFilterList() {
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