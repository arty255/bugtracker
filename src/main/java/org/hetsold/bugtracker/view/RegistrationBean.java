package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
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

    public void registerAction() {
        userService.register(registrationData);
    }
}