package org.hetsold.bugtracker.dto.user;

import org.hetsold.bugtracker.dto.AbstractDTO;
import org.hetsold.bugtracker.model.User;

import java.util.Date;

public class UserDTO extends AbstractDTO {
    private String firstName;
    private String lastName;
    private Date registrationDate;

    public UserDTO(String uuid) {
        this.setUuid(uuid);
    }

    public UserDTO(User user) {
        this.setUuid(user.getUuid().toString());
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.registrationDate = user.getRegistrationDate();
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}