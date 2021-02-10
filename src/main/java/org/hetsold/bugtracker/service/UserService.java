package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.MinimalRegistrationData;
import org.hetsold.bugtracker.dto.SecurityUserDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.SecurityUserAuthority;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDTO register(UserDTO userDTO, MinimalRegistrationData minimalRegistrationData);

    UserDTO register(UserDTO userDTO, SecurityUserDTO SecurityUserDTO);

    void updateUserProfileData(UserDTO userDTO);

    void updateEmail(UserDTO userDTO, String email);

    void updatePassword(UserDTO userDTO, String newPassword);

    String getAutogeneratedPassword();

    void updateSecurityData(SecurityUserDTO securityUserDTO);

    void changeUserRoles(UserDTO userDTO, Set<SecurityUserAuthority> securityUserAuthorities);

    boolean isLoginTaken(String login);

    List<UserDTO> getUsers(Contract contract, int first, int count, boolean dateAsc);

    long getUsersCount(Contract contract);

    void delete(UserDTO userDTO);

    UserDTO getUser(UserDTO userDTO);
}