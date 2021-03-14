package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.user.FullUserDTO;
import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;

import java.util.List;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static User getUser(UserDTO userDTO) {
        if (userDTO != null) {
            return new User.Builder()
                    .withUUID(UUIDMapper.getUUID(userDTO))
                    .withNames(userDTO.getFirstName(), userDTO.getLastName())
                    .build();
        }
        return null;
    }

    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user);
    }

    public static List<UserDTO> getUserDTOS(List<User> userList) {
        return userList.stream().map(UserMapper::getUserDTO).collect(Collectors.toList());
    }

    public static SecurityUser getSecurityUser(FullUserDTO fullUserDTO) {
        if (fullUserDTO != null) {
            SecurityUser securityUser = new SecurityUser.Builder()
                    .withNameAndPassword(fullUserDTO.getUsername(), fullUserDTO.getPassword())
                    .withEmail(fullUserDTO.getEmail())
                    .withUUID(UUIDMapper.getUUID(fullUserDTO))
                    .build();
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
            return new SecurityUser.Builder()
                    .withNameAndPassword(registrationDataDTO.getLogin(), registrationDataDTO.getPassword())
                    .withEmail(registrationDataDTO.getEmail())
                    .withUUID(null)
                    .build();
        }
        return null;
    }

    public static User getUser(RegistrationDataDTO registrationDataDTO) {
        if (notNullFields(registrationDataDTO)) {
            return new User.Builder()
                    .withNames(registrationDataDTO.getFirstName(), registrationDataDTO.getLastName())
                    .withUUID(null)
                    .build();
        }
        return null;
    }

    private static boolean notNullFields(RegistrationDataDTO registrationDataDTO) {
        return (registrationDataDTO != null &&
                (registrationDataDTO.getLastName() != null && !registrationDataDTO.getLastName().isEmpty() ||
                        registrationDataDTO.getFirstName() != null && !registrationDataDTO.getFirstName().isEmpty())
        );
    }

    public static SecurityUserDetails getSecurityUserDetails(SecurityUser securityUser) {
        return new SecurityUserDetails(securityUser);
    }

    public static FullUserDTO getFullUserDTO(SecurityUser securityUser) {
        return new FullUserDTO(securityUser);
    }
}