package org.hetsold.bugtracker.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class LoginBean {
    private String login;
    private String pass;
    @Autowired
    private AuthenticationManager manager;

    public LoginBean() {
    }

//    @Autowired
    /*
    public LoginBean(AuthenticationManager manager) {
        this.manager = manager;
    }*/


    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public void loginAction() {
        Authentication authenticationResult;
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, pass);
        authenticationResult = manager.authenticate(authenticationRequest);
        if (authenticationResult.isAuthenticated()) {
            int i = 0;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
