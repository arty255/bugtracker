package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;

import java.util.List;

public interface UserService {
    User save(User user);

    UserDTO registerUser(UserDTO userDTO);

    User getUserById(User user);

    UserDTO getUserById(String id);

    void delete(User user);

    void delete(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsers(int first, int count);

    long getUserCount();
}
