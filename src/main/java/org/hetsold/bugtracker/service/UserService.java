package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;

public interface UserService {
    void save(User user);

    User getUserById(User user);

    UserDTO getUserById(String id);

    void delete(User user);
}
