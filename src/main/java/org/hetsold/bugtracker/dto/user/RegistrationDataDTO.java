package org.hetsold.bugtracker.dto.user;

import java.io.Serializable;

public class RegistrationDataDTO implements Serializable {
    private static final long serialVersionUID = 2936681426040726549L;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public RegistrationDataDTO() {
    }

    public RegistrationDataDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RegistrationDataDTO(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}