package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class RegistrationBean {
    @Autowired
    private UserService userService;

    RegistrationDataDTO registrationData;

    @PostConstruct
    private void postConstruct() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initRegistrationDataListener();
    }

    public void initRegistrationDataListener() {
        registrationData = new RegistrationDataDTO();
    }

    public void onLoginChangeListener(){
        if (userService.isLoginTaken(registrationData.getLogin())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "login is taken", "login is taken"));
        }
    }

    public void registrationAction() {
        userService.registerByData(registrationData);
    }

    public RegistrationDataDTO getRegistrationData() {
        return registrationData;
    }

    public void setRegistrationData(RegistrationDataDTO registrationData) {
        this.registrationData = registrationData;
    }
}