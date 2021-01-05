package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;

import java.util.List;

public interface UserService {
    void save(User user);

    User getUserById(User user);

    UserDTO getUserById(String id);

    void delete(User user);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsers(int first, int count);

    long getUserCount();
}
