package org.hetsold.bugtracker.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public String loginAction() {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(login, pass);
        try {
            Authentication authenticationResult = manager.authenticate(authenticationRequest);
            if (authenticationResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                return "tickets";
            }
        } catch (BadCredentialsException e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No User with that login or password",
                            "No User with that login or password"));
        }
        return "";
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