package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.dto.user.FullUserDTO;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User getUser(UserDTO userDTO) {
        if (userDTO != null) {
            User user = new User();
            user.setUuid(UUIDMapper.getUUID(userDTO));
            mapDataFields(userDTO, user);
            return user;
        }
        return null;
    }

    private static void mapDataFields(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
    }

    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user);
    }

    public static List<UserDTO> getUserDTOS(List<User> userList) {
        return userList.stream().map(UserMapper::getUserDTO).collect(Collectors.toList());
    }

    public static SecurityUser getSecurityUser(FullUserDTO fullUserDTO) {
        if (fullUserDTO != null) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUuid(UUIDMapper.getUUID(fullUserDTO));
            securityUser.setUsername(fullUserDTO.getUsername());
            securityUser.setPassword(fullUserDTO.getPassword());
            securityUser.setEnabled(fullUserDTO.isEnabled());
            securityUser.setCredentialsNonExpired(fullUserDTO.isCredentialsNonExpired());
            securityUser.setAccountNonLocked(fullUserDTO.isAccountNonLocked());
            securityUser.setAccountNonExpired(fullUserDTO.isAccountNonExpired());
            securityUser.getAuthorities().addAll(fullUserDTO.getAuthorities());
            return securityUser;
        }
        return null;
    }

    public static SecurityUser getSecurityUser(RegistrationDataDTO registrationDataDTO) {
        if (registrationDataDTO != null) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUuid(null);
            securityUser.setUsername(registrationDataDTO.getLogin());
            securityUser.setPassword(registrationDataDTO.getPassword());
            securityUser.setEmail(registrationDataDTO.getEmail());
            return securityUser;
        }
        return null;
    }

    public static User getUser(RegistrationDataDTO registrationDataDTO) {
        if (registrationDataDTO != null) {
            User user = new User();
            user.setFirstName(registrationDataDTO.getFirstName());
            user.setLastName(registrationDataDTO.getLastName());
            return user;
        }
        return null;
    }

    public static SecurityUserDetails getSecurityUserDetails(SecurityUser securityUser) {
        return new SecurityUserDetails(securityUser);
    }

    public static FullUserDTO getFullUserDTO(SecurityUser securityUser) {
        return new FullUserDTO(securityUser);
    }
}