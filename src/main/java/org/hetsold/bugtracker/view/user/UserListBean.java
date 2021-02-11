package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.hetsold.bugtracker.view.filter.ContractBuilder;
import org.hetsold.bugtracker.view.filter.DisplayableFieldFilter;
import org.hetsold.bugtracker.view.filter.FilterComponentBuilder;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class UserListBean implements Serializable {
    private UserService userService;
    private UserDTO selectedUserDTO;
    private LazyDataModel<UserDTO> usersLazyDataModel;
    private List<DisplayableFieldFilter> displayableFieldFilters;

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
        displayableFieldFilters = FilterComponentBuilder.buildWrappers(UserDTO.class,
                "firstName lastName");
    }

    public void modelFiltersUpdateAction() {
        ((UserDTOLazyDataModel) usersLazyDataModel).setContract(ContractBuilder.buildContact(displayableFieldFilters));
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

    public List<DisplayableFieldFilter> getIssueFilterList() {
        return displayableFieldFilters;
    }
}
