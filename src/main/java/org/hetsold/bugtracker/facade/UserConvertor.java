package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {
    private UserDAO userDAO;

    @Autowired
    public UserConvertor(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUser(UserDTO userDTO) {
        if (userDTO != null && !userDTO.getUuid().isEmpty()) {
            return userDAO.getUserById(userDTO.getUuid());
        }
        return null;
    }

    public UserDTO geUserDTO(User user) {
        return new UserDTO(user);
    }
}
