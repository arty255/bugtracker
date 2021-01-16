package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class UserListBean implements Serializable {
    private UserService userService;
    private UserDTO selectedUserDTO;
    private LazyDataModel<UserDTO> usersLazyDataModel;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        updateDataModel();
    }

    public void updateDataModel() {
        usersLazyDataModel = new UsersLazyDataModel(userService);
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
}
