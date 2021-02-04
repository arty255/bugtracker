package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDTO registerUser(UserDTO userDTO, SecurityUser securityUser);

    UserDTO updateUser(UserDTO userDTO);

    User getUserById(String id);

    UserDTO getUserDTOById(String id);

    void delete(String uuid);

    void delete(UserDTO userDTO);

    List<UserDTO> getUsers(Contract contract, int first, int count);

    long getUsersCount(Contract contract);

    boolean isLoginTaken(String login);
}
