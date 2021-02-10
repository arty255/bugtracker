package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dto.SecurityUserDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUserAuthority;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@ViewScoped
public class AddUserBean implements Serializable {
    private UserDTO user;
    private SecurityUserDTO securityUser;
    private List<SecurityUserAuthority> selectedSecurityUserAuthorities;
    private UserPreset selectedUserPreset;

    private boolean isNewUserAction;
    @Autowired
    private UserService userService;


    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initNewUser();
        selectedSecurityUserAuthorities = new ArrayList<>();
    }

    public void registerUser() {
        try {
            UserDTO actionResultUser = user;
            if (isNewUserAction) {
                actionResultUser = userService.register(user, securityUser);
            } else {
                //todo: change edit process
                //userService.update(user, securityUser);
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "new user " + actionResultUser.getUuid() + " added",
                            "new user " + actionResultUser.getUuid() + " added"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "no user added", "no user added"));
        }
        initNewUser();
    }

    public void initNewUser() {
        user = new UserDTO("");
        securityUser = new SecurityUserDTO();
        isNewUserAction = true;
    }

    public void onPresetChangeListener() {
        securityUser.getAuthorities().clear();
        securityUser.getAuthorities().addAll(selectedUserPreset.getSecurityUserAuthorities());
    }

    public void onAuthorityChangeListener() {
        selectedUserPreset = Arrays.stream(UserPreset.values())
                .filter(item -> securityUser.getAuthorities().containsAll(item.getSecurityUserAuthorities()))
                .findFirst()
                .orElse(null);
    }

    public void onLoginChangeListener() {
        if (userService.isLoginTaken(securityUser.getUsername())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "login is taken", "login is taken"));
        }
    }

    public void onTabChangeListener(TabChangeEvent event) {
        if (!isNewUserAction && isActiveTabCredentialsTab(event)) {
//            securityUser = securityService.getSecurityUserByUserUuid(user.getUuid());
        }
    }

    private boolean isActiveTabCredentialsTab(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        return tabView.getChildren().indexOf(event.getTab()) == 1;
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

    public SecurityUserDTO getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUserDTO securityUser) {
        this.securityUser = securityUser;
    }

    public List<SecurityUserAuthority> getSelectedSecurityUserAuthorities() {
        return selectedSecurityUserAuthorities;
    }

    public void setSelectedSecurityUserAuthorities(List<SecurityUserAuthority> selectedSecurityUserAuthorities) {
        this.selectedSecurityUserAuthorities = selectedSecurityUserAuthorities;
    }

    public UserPreset getSelectedUserPreset() {
        return selectedUserPreset;
    }

    public void setSelectedUserPreset(UserPreset selectedUserPreset) {
        this.selectedUserPreset = selectedUserPreset;
    }
}