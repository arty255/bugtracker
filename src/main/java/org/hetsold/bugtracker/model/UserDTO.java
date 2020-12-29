package org.hetsold.bugtracker.model;

import java.util.Objects;

public class UserDTO {
    private String uuid;
    private String firstName;
    private String lastName;

    public UserDTO(String uuid) {
        this.uuid = uuid;
    }

    public UserDTO(User user) {
        this.uuid = user.getUuid();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(uuid, userDTO.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
