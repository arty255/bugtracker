package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class UserProfileBean implements Serializable {
    private String uuid;
    private UserDTO user;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public void initUserData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            user = userService.getUserDTOById(uuid);
        }
    }


    public void save() {
        userService.updateUser(user);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

}
