package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class AddUserBean implements Serializable {
    private UserDTO user;

    private boolean isNewUserAction;
    @ManagedProperty("#{userListBean}")
    private UserListBean userListBean;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initNewUser();
    }

    public void registerUser() {
        try {
            UserDTO actionResultUser = user;
            if (isNewUserAction) {
                actionResultUser = userService.registerUser(user);
            } else {
                userService.updateUser(user);
            }
            userListBean.updateDataModel();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "new user " + actionResultUser.getUuid() + " added",
                            "new user " + actionResultUser.getUuid() + " added"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "no user added", "no user added"));
        }
    }

    public void initNewUser() {
        user = new UserDTO("");
        isNewUserAction = true;
    }

    public void setUserListBean(UserListBean userListBean) {
        this.userListBean = userListBean;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isNewUserAction() {
        return isNewUserAction;
    }

    public void setNewUserAction(boolean newUserAction) {
        isNewUserAction = newUserAction;
    }
}
