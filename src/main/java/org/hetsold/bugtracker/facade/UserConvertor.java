package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;
import org.springframework.stereotype.Component;

public class UserConvertor {

    public static User getUser(UserDTO userDTO) {
        User user = null;
        if (userDTO != null) {
            user = new User();
            user.setUuid(userDTO.getUuid());
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
        }
        return user;
    }

    public static UserDTO geUserDTO(User user) {
        return new UserDTO(user);
    }
}